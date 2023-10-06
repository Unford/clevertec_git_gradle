package ru.clevertec.course.git.service

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import ru.clevertec.course.git.model.TagVersion
import ru.clevertec.course.git.model.VersionType


class GitServiceImpl implements GitService {
    private static final String PUSH_COMMAND = "git push %s head"
    Git git
    Logger logger


    GitServiceImpl(Project project) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder()
        this.git = new Git(builder
                .setWorkTree(project.getProjectDir())
                .readEnvironment()
                .findGitDir()
                .build())
        this.logger = project.logger
    }




    @Override
    boolean hasUncommittedChanges() {
        return git.status().call().hasUncommittedChanges()
    }

    @Override
    boolean headHasTag() {
        ObjectId headId = git.getRepository().resolve(Constants.HEAD)
        List<Ref> tags = git.tagList().setContains(headId).call()

        logger.info("Commit has tag - {}", !tags.isEmpty())
        return !tags.isEmpty()
    }

    @Override
    boolean addTagVersion(TagVersion tagVersion) {
        try {
            git.tag().setName(tagVersion.toString()).call()
            logger.quiet("Tag {} added successfully", tagVersion)
            return true
        } catch (GitAPIException e) {
            logger.error(e.toString())
            return false
        }
    }

    @Override
    boolean pushToRemote(String remote) {
        Process process = (PUSH_COMMAND.formatted(remote)).execute()
        return process.waitFor() == 0
    }

    @Override
    TagVersion findLastVersion() {
        def tags = git.tagList().call()
                .findResults { tagRef ->
                    def tagName = tagRef.getName().replace(Constants.R_TAGS, "")
                    def tag = TagVersion.parseFromString(tagName).orElse(null)
                    return tag
                }
                .sort { a, b ->
                    (a.getVersion() <=> b.getVersion())
                }
        if (tags) {
            return tags.last()
        } else {
            return new TagVersion(BigDecimal.ZERO, VersionType.MASTER)
        }

    }

    @Override
    String getBranch() {
        return git.getRepository().getBranch()
    }

    @Override
    boolean checkGitRepository() {
        return git.getRepository().getObjectDatabase().exists()
    }
}
