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
        maven { url = uri("https://jitpack.io") }
    }
}
include(
    ":app",
    ":appconstant",
    ":database",
    ":firebase",
    ":model",
    ":networking",
    ":repository",
    ":sharedpref",
    ":feature-presentation",
    ":util",
    ":workmanager",
)
rootProject.name = "PSTUian"
