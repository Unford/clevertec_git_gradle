package ru.clevertec.course.git.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.clevertec.course.git.service.GitServiceImpl

class IsGitRepositoryExistTask extends DefaultTask{
    private GitServiceImpl service = new GitServiceImpl(project)

    @TaskAction
    def IsGitRepositoryExist() {
        boolean isGitRepository = service.checkGitRepository()
        if (!isGitRepository){
            throw new RuntimeException("There's no git repository")
        }
    }
}
