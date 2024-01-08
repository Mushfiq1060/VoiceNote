pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "VoiceNote"
include(":app")
include(":core:database")
include(":core:designsystem")
include(":core:data")
include(":feature:notes")
include(":core:ui")
include(":core:model")
include(":core:datastore")
include(":core:datastoreproto")
include(":core:common")
include(":feature:noteedit")
