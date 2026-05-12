pluginManagement {
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

rootProject.name = "GrandmasterEdge"
include(":app")
include(":core")
include(":engine")
include(":feature-tactics")
include(":feature-multiplayer")
include(":feature-kids")
include(":ui-components")
