pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "jzbrooks-decks"

includeBuild("storyboard")

include(":dc26:story")
include(":shared")
