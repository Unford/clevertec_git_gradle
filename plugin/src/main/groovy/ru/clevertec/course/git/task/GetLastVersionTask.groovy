package ru.clevertec.course.git.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.clevertec.course.git.service.GitServiceImpl

class GetLastVersionTask extends DefaultTask{
    @TaskAction
    def lastVersionTask() {
        def v = GitServiceImpl.getInstance(project).findLastVersion();
        logger.quiet(v.toString())
        return v

    }
}
