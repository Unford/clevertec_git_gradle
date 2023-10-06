package ru.clevertec.course.git

import org.eclipse.jgit.lib.Constants
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import ru.clevertec.course.git.model.GitExtension
import ru.clevertec.course.git.task.*

class GitPlugin implements Plugin<Project> {


    private static final String DEFAULT_STAGE_BRANCH_NAME = "stage"
    private static final String DEFAULT_DEV_BRANCH_NAME = "qa"
    private static final String DEFAULT_QA_BRANCH_NAME = "dev"

    private static final String PLUGIN_GROUP = "git tagger"

   public static final String BUILD_TAGGER_NAME = 'buildTagger'
   public static final String CHECK_UNCOMMITTED_TASK = 'checkUncommitted'
   public static final String CHECK_HEAD_TAGS_TASK = 'checkHeadTags'
   public static final String ADD_TAG_TASK = 'addTagToHead'
   public static final String PUSH_HEAD_TASK = 'pushHeadToRemote'
   public static final String GET_LAST_VERSION_TASK = 'getLastTagVersion'


    @Override
    void apply(Project project) {
        def extension = project.extensions.create(BUILD_TAGGER_NAME, GitExtension)
        setDefaultValues(extension)

        project.tasks.register(BUILD_TAGGER_NAME, ComplexTagTask) {
            remoteName = extension.getRemoteName().get()
            setGroup(PLUGIN_GROUP)
        }

        project.tasks.register(GET_LAST_VERSION_TASK, GetLastVersionTask) {
            setGroup(PLUGIN_GROUP)
        }

        def uncommittedTask = project.tasks.register(CHECK_UNCOMMITTED_TASK,
                HasNoUncommittedTask) {
            setGroup(PLUGIN_GROUP)
        }

        def headTagsTask = project.tasks.register(CHECK_HEAD_TAGS_TASK, HeadHasTagTask) {
            setGroup(PLUGIN_GROUP)
            dependsOn(CHECK_UNCOMMITTED_TASK)
            onlyIf {
                uncommittedTask.get().getResult()
            }
        }

        def addTagTask = project.tasks.register(ADD_TAG_TASK,
                AddHeadTagTask) {
            setGroup(PLUGIN_GROUP)
            dependsOn(CHECK_HEAD_TAGS_TASK)
            onlyIf {
                uncommittedTask.get().getResult() &&
                        !headTagsTask.get().getState().skipped &&
                        !headTagsTask.get().getResult()
            }
            devAndQABranches = extension.getDevAndQABranches().get()
            masterBranchName = extension.getMasterBranchName().get()
            stageBranchName = extension.getStageBranchName().get()
        }

        project.tasks.register(PUSH_HEAD_TASK, PushHeadTask) {
            setGroup(PLUGIN_GROUP)
            dependsOn(ADD_TAG_TASK)
            onlyIf {
                !addTagTask.get().getState().skipped && addTagTask.get().getResult()
            }
            remote = extension.getRemoteName().get()
        }


    }

    private void setDefaultValues(GitExtension extension) {
        extension.stageBranchName.convention(DEFAULT_STAGE_BRANCH_NAME)
        extension.masterBranchName.convention(Constants.MASTER)
        extension.devAndQABranches.convention([DEFAULT_DEV_BRANCH_NAME, DEFAULT_QA_BRANCH_NAME])
        extension.remoteName.convention(Constants.DEFAULT_REMOTE_NAME)
    }
}
