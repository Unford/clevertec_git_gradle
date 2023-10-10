# CLEVERTEC 1 Git&Gradle
[Задание](Backend%20Development%20Course%20'23%20-%2001.%20GIT&Gradle.pdf)

## Инструкция для запуска: 
В build.gradle создана задача для публикации в локальном репозитории maven
 ```groovy
tasks.register('publishToMavenLocalProxy') {
    dependsOn(publishToMavenLocal)
    group("git tagger")
}
```
1. запустить задачу **publishToMavenLocalProxy** в группе **git tagger**
```bash
./gradlew publishToMavenLocalProxy
```
2. Добавить плагин в build.gradle рабочего проекта
 ```groovy
plugins {
    ...
    id("ru.clevertec.course.git.tagger") version("1.0")
}
```
3. Изменить **settings.gradle** для доступа к mavenLocal
```groovy
plugins {
    pluginManagement{
    repositories{
        mavenLocal()
        gradlePluginPortal()
        ...
    }
}
}
```
4. Настроить плагин если это нужно, названия веток и remote для push для запуска после build добавить как finalizedBy
```groovy
buildTagger {
    stageBranchName="stage-1"
    masterBranchName= "superMaster"
    devAndQABranches.addAll("devs", "qvas")
    remoteName = "NotOrigin"
}

tasks.build{
    finalizedBy(tasks.pushHeadToRemote)
}
```
## Описание задач: 
Все задачи плагина находятся в группе "**git tagger**"
1. getLastTagVersion - Возвращает последнюю опубликованную версию зависит от isGitRepositoryExist(7)
2. checkUncommitted - Проверяет git status в случае если таких нет возвращает true зависит от isGitRepositoryExist(7)
3. checkHeadTags - Проверяет наличие тегов у Head если такие есть возвращает true, зависит от checkUncommitted (2)
4. addTagToHead - Добавляет тег на основе последнего актуального тега и текущей ветки зависит от checkHeadTags (3)
5. pushHeadToRemote - Обновляет удаленный репозиторий из текущей ветки теги не пушит(мб надо) зависит addTagToHead (4)
6. buildTagger - Решение задачи в 1 таске зависит от isGitRepositoryExist(7)
7. isGitRepositoryExist - Проверяет существует ли Git репозиторий
