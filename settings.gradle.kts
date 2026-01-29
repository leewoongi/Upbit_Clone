pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "chart"
include(":app")
include(":presenter:home")
include(":presenter:detail")
include(":presenter:main")
include(":data")
include(":data:repository")
include(":data:datasource")
include(":domain")
include(":core")
include(":network")
include(":chart-core")
include(":chart-ui-android")
