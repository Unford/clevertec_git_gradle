package ru.clevertec.course.git.task

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import ru.clevertec.course.git.model.TagVersion
import ru.clevertec.course.git.model.VersionType
import ru.clevertec.course.git.service.GitServiceImpl

class AddHeadTagTask extends AbstractResultedTask {
    private GitServiceImpl service = GitServiceImpl.getInstance(project);

    @Input
    abstract Set<String> devAndQABranches
    @Input
    abstract String stageBranchName
    @Input
    abstract String masterBranchName

    @TaskAction
    def addHeadTag() {
        TagVersion tagVersion = calculateCurrentVersion()
        addResult(service.addTagVersion(tagVersion))
    }

    private VersionType IdentifyBranchType(String s) {
        VersionType type = VersionType.OTHER
        if (masterBranchName == s) {
            type = VersionType.MASTER
        } else if (stageBranchName == s) {
            type = VersionType.STAGE
        } else if (devAndQABranches.contains(s)) {
            type = VersionType.DEV_OR_QA
        }

        return type
    }

    public TagVersion calculateCurrentVersion(){
        TagVersion lastVersion = service.findLastVersion()
        String curBranch = service.getBranch()
        VersionType type = IdentifyBranchType(curBranch)
        BigDecimal version = BigDecimal.valueOf(0.0)
        logger.info("branch type - {}", type)
        if (type == VersionType.DEV_OR_QA || type == VersionType.STAGE || type == VersionType.OTHER) {
            version = lastVersion.incrementMinorVersion()
        } else if (type == VersionType.MASTER) {
            version = lastVersion.incrementMajorVersion()
        }
        return new TagVersion(version, type)
    }
}
