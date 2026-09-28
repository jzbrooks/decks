import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
    alias(libs.plugins.ktlint)
}

ktlint {
    version.set(libs.versions.ktlint.cli)

    filter {
        exclude { it.file.path.contains("/build/generated/") }
    }
}

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

    compilerOptions {
        val runningFromIdea =
            System.getProperty("idea.active") == "true" ||
                System.getProperty("idea.sync.active") == "true"
        allWarningsAsErrors.set(!runningFromIdea)
        extraWarnings.set(!runningFromIdea)

        optIn.addAll(
            "androidx.compose.animation.core.ExperimentalTransitionApi",
            "androidx.compose.animation.ExperimentalAnimationApi",
            "org.jetbrains.compose.resources.ExperimentalResourceApi",
        )
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared"))

                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material)
                implementation(libs.compose.components.resources)

                implementation(libs.storyboard)
                implementation(libs.storyboard.easel)
                implementation(libs.storyboard.layout)
                implementation(libs.storyboard.text)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.jzbrooks.dc26.resources"
}
