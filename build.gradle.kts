plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.ktlint) apply false
}

tasks.register<Sync>("site") {
    into(layout.buildDirectory.dir("_site"))

    into("dc26") {
        from(project(":dc26:story").tasks.named("wasmJsBrowserDistribution"))
    }
}
