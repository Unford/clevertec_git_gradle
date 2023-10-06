package ru.clevertec.course.git.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import ru.clevertec.course.git.GitPlugin
import ru.clevertec.course.git.model.TagVersion
import ru.clevertec.course.git.service.GitServiceImpl

class ComplexTagTask extends DefaultTask {

    @Input
    abstract String remoteName

    private GitServiceImpl service = new GitServiceImpl(project)

    @TaskAction
    def tagCommit() {
        TagVersion lastVersion = service.findLastVersion()
        if (service.hasUncommittedChanges()) {
            logger.quiet("{}.uncommitted", lastVersion)
        } else if (!service.headHasTag()) {
            def addTask = (AddHeadTagTask) project.tasks.findByName(GitPlugin.ADD_TAG_TASK)
            service.addTagVersion(addTask.calculateCurrentVersion())
            service.pushToRemote(remoteName)
        }


    }


}

