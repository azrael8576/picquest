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

rootProject.name = "picquest"
include(":app")

include(":core:designsystem")
include(":core:testing")
include(":core:common")
include(":core:data")
include(":core:domain")
include(":core:model")
include(":core:network")
include(":core:datastore")
include(":core:datastore-test")

include(":ui-test-hilt-manifest")

include(":feature:home")
include(":feature:photo")
include(":feature:contactme")
include(":feature:video")
