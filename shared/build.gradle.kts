import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
}

kotlin {
    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    compilerOptions {
        optIn.addAll(
            "androidx.compose.animation.core.ExperimentalTransitionApi",
            "androidx.compose.animation.ExperimentalAnimationApi",
            "androidx.compose.animation.ExperimentalSharedTransitionApi",
            "org.jetbrains.compose.resources.ExperimentalResourceApi",
        )
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.compose.runtime)
                api(libs.compose.foundation)
                api(libs.compose.material)
                api(libs.compose.components.resources)
                api(libs.compose.material.icons.core)

                api(libs.storyboard)
                api(libs.storyboard.easel)
                api(libs.storyboard.text)

                api(project.dependencies.platform(libs.ktor.bom))
                api(project.dependencies.platform(libs.kotlinx.coroutines.bom))

                api(libs.kotlinx.serialization.json)
                api(libs.ktor.client.core)
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }
        jvmMain {
            dependencies {
                api(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.debug)
                implementation(libs.ktor.client.cio)
            }
        }
        wasmJsMain {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "dev.bnorm.deck.shared.generated.resources"
}
