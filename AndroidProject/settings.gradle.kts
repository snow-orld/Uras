pluginManagement {
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

rootProject.name = "UrasInterProcessProject"
include(":client")
include(":renderServiceLibrary")
project(":renderServiceLibrary").projectDir = File(rootDir, "unityexport/renderServiceLibrary/")
include(":tuanjieLibrary")
project(":tuanjieLibrary").projectDir = File(rootDir, "unityexport/tuanjieLibrary/")
include(":launcher")
project(":launcher").projectDir = File(rootDir, "unityexport/launcher/")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        flatDir {
            dirs("${project(":tuanjieLibrary").projectDir}/libs")
        }
    }
}
include(":rendertexture")
