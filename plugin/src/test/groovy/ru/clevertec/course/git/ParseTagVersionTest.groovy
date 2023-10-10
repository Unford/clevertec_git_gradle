package ru.clevertec.course.git


import ru.clevertec.course.git.model.TagVersion
import ru.clevertec.course.git.model.VersionType
import spock.lang.Specification

class ParseTagVersionTest extends Specification {
    def "Parser works properly"() {
        expect:
        expected == TagVersion.parseFromString(str).isPresent()
        where:
        str             | expected
        "v1.0"          | true
        "v1.1-rc"       | true
        "v1.1-SNAPSHOT" | true
        "v1.4"          | true
        "1.1"           | false
        "v-1.1"         | false
        "v1.1-rc-rc"    | false
    }


    def "Parse type of version properly"() {
        expect:
        TagVersion version = TagVersion.parseFromString(str).get();
        expected == version
        where:
        str             | expected
        "v1.0"          | new TagVersion(1, 0, VersionType.MASTER)
        "v1.1-rc"       | new TagVersion(1, 1, VersionType.STAGE)
        "v1.1-SNAPSHOT" | new TagVersion(1, 1, VersionType.OTHER)
        "v1.4"          | new TagVersion(1, 4, VersionType.DEV_OR_QA)

    }


}
