@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    plugins {
         val kotlinVersion = "2.4.0"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        kotlin("plugin.compose") version kotlinVersion
        kotlin("plugin.power-assert") version kotlinVersion
        id("org.jetbrains.compose") version "1.11.1"
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
