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

import java.util.stream.Collectors

class GitServiceImpl implements GitService {
    private static final String PUSH_COMMAND = "git push %s head --follow-tags"
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
        boolean res = false
        try {
            git.tag().setName(tagVersion.toString()).call()
            logger.info("Tag {} added successfully", tagVersion)
            res = true
        } catch (GitAPIException e) {
            logger.error(e.getMessage())
        }
        return res
    }

    @Override
    boolean pushToRemote(String remote) {
        boolean res = false
        try {
            Process process = (PUSH_COMMAND.formatted(remote)).execute()
            res = process.waitFor() == 0
            if (res) {
                logger.info("Pushed tag to remote {}", remote)
            } else {
                logger.warn("Error during pushing command - {}", process
                        .errorReader()
                        .lines()
                        .filter { !it.isBlank() }
                        .collect(Collectors.joining("; ")))
            }
        } catch (IOException e) {
            logger.error(e.getMessage())
        }

        return res
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
                    Comparator.comparingInt({ TagVersion tag -> tag.getMajorVersion() })
                            .thenComparingInt({ TagVersion tag -> tag.getMinorVersion() })
                            .compare(a, b)
                }

        return tags ? tags.last() : TagVersion.ZERO_VERSION
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
