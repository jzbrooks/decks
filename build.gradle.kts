import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization") apply false
    kotlin("plugin.compose") apply false
    id("org.jetbrains.compose") apply false
}

allprojects {
    plugins.withId("org.jetbrains.compose") {
        extensions.configure<ComposeExtension> {
            extensions.configure<ResourcesExtension> {
                publicResClass = true
            }
        }
    }

    plugins.withId("org.jetbrains.kotlin.multiplatform") {
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.all {
                languageSettings {
                    optIn("androidx.compose.animation.core.ExperimentalTransitionApi")
                    optIn("androidx.compose.animation.ExperimentalAnimationApi")
                    optIn("androidx.compose.animation.ExperimentalSharedTransitionApi")
                    optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
                }
            }
        }
    }
}

tasks.register<Sync>("site") {
    into(project.layout.buildDirectory.dir("_site"))

    from(project(":deck.jzbrooks.com").tasks.named("jsBrowserDistribution"))

    into("dc26") {
        from(project(":dc26:story").tasks.named("wasmJsBrowserDistribution"))
        into("companion") {
            from(project(":dc26:companion").tasks.named("jsBrowserDistribution"))
        }
    }
}
