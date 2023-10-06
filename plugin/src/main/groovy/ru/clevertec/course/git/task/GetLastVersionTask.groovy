package ru.clevertec.course.git.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.clevertec.course.git.service.GitServiceImpl

class GetLastVersionTask extends DefaultTask{
    private GitServiceImpl service = new GitServiceImpl(project)

    @TaskAction
    def lastVersionTask() {
        def v = service.findLastVersion()
        logger.quiet(v.toString())
        return v

    }
}
