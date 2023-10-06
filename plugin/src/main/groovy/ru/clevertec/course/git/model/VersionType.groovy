package ru.clevertec.course.git.model

enum VersionType {
    DEV_OR_QA,
    STAGE("-rc"),
    MASTER,
    OTHER("-SNAPSHOT");

    private final String postfix;

    VersionType(String postfix) {
        this.postfix = postfix
    }

    VersionType() {
        this.postfix = ""
    }

    String getPostfix() {
        return postfix
    }
}