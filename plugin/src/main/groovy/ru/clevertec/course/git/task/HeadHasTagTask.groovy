package ru.clevertec.course.git.task


import org.gradle.api.tasks.TaskAction
import ru.clevertec.course.git.service.GitServiceImpl

class HeadHasTagTask extends AbstractResultedTask {
    private GitServiceImpl service = GitServiceImpl.getInstance(project);

    @TaskAction
    def headHasTag() {
        boolean hasTag = service.headHasTag();
        logger.info(hasTag.toString())
        addResult(hasTag)
    }


}
