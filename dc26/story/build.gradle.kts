import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")
}

group = "com.jzbrooks.deck"
version = "1.0-SNAPSHOT"

kotlin {
    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        binaries.executable()
        browser {
            commonWebpackConfig {
                outputFileName = "dc26.js"
            }
        }
    }

    sourceSets {
        all {
            languageSettings {
                enableLanguageFeature("MultiDollarInterpolation")

                optIn("androidx.compose.animation.core.ExperimentalTransitionApi")
                optIn("androidx.compose.animation.ExperimentalAnimationApi")
                optIn("androidx.compose.animation.ExperimentalSharedTransitionApi")
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        commonMain {
            dependencies {
                implementation(project(":shared"))

                implementation("org.jetbrains.compose.runtime:runtime:1.10.1")
                implementation("org.jetbrains.compose.foundation:foundation:1.10.1")
                implementation("org.jetbrains.compose.material:material:1.10.1")
                api("org.jetbrains.compose.components:components-resources:1.10.1")

                api("dev.bnorm.storyboard:storyboard:0.1.0-alpha03")
                api("dev.bnorm.storyboard:storyboard-easel:0.1.0-alpha03")
                api("dev.bnorm.storyboard:storyboard-layout:0.1.0-alpha03")
                api("dev.bnorm.storyboard:storyboard-text:0.1.0-alpha03")

                implementation("io.github.petertrr:kotlin-multiplatform-diff:0.7.0")
            }
        }
    }
}
