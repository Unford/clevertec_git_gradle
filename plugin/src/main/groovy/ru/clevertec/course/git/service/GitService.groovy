package ru.clevertec.course.git.service

import ru.clevertec.course.git.model.TagVersion

interface GitService {
    boolean hasUncommittedChanges()
    boolean headHasTag()
    boolean addTagVersion(TagVersion tagVersion)
    boolean pushToRemote(String remote)
    TagVersion findLastVersion()
    String getBranch()

    boolean checkGitRepository()
}