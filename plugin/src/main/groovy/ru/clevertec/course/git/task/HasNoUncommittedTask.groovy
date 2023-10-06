package ru.clevertec.course.git.task


import org.gradle.api.tasks.TaskAction
import ru.clevertec.course.git.service.GitServiceImpl

class HasNoUncommittedTask extends AbstractResultedTask {

    @TaskAction
    def hasNoUncommittedChanges() {
        def hasUncommittedChanges = service.hasUncommittedChanges()
        if (hasUncommittedChanges){
            logger.quiet("{}.uncommitted", service.findLastVersion())
        }else {
            logger.info(Boolean.TRUE.toString())
        }
        addResult(!hasUncommittedChanges)
    }
}
