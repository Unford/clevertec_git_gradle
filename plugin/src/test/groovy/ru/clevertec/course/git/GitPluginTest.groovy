package ru.clevertec.course.git

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import static ru.clevertec.course.git.GitPlugin.*

class GitPluginTest extends Specification {

    def "plugin registers task"() {
        given:
        def project = ProjectBuilder.builder().build()
        List<String> strings = List.of(BUILD_TAGGER_NAME,
                ADD_TAG_TASK,
                GET_LAST_VERSION_TASK,
                CHECK_UNCOMMITTED_TASK,
                CHECK_HEAD_TAGS_TASK,
                PUSH_HEAD_TASK)
        when:
        project.getPlugins().apply("ru.clevertec.course.git.tagger");

        then:
        strings.stream().allMatch {project.tasks.findByName(it) != null}
    }
}
