package ru.clevertec.course.git.model

import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty


abstract class GitExtension {
    abstract SetProperty<String> getDevAndQABranches()
    abstract Property<String> getStageBranchName()
    abstract Property<String> getMasterBranchName()
    abstract Property<String> getRemoteName()

}
