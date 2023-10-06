package ru.clevertec.course.git.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import ru.clevertec.course.git.service.GitServiceImpl

class PushHeadTask extends DefaultTask {
    private GitServiceImpl service = GitServiceImpl.getInstance(project);

    @Input
    abstract String remote

    @TaskAction
    def pushHead() {
        return service.pushToRemote(remote)
    }
}
