package ru.clevertec.course.git.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal

abstract class AbstractResultedTask extends DefaultTask {
    public static final String TASK_RESULT = "result"

    @Internal
    Object getResult() {
        return this.getExtensions().getByName(TASK_RESULT)
    }

    void addResult(Object object){
        this.getExtensions().add(TASK_RESULT, object)
    }
}
