import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id(libs.plugins.jvm.get().pluginId)
    alias(libs.plugins.serialization)
    id(libs.plugins.publish.get().pluginId)
    alias(libs.plugins.dokka)
    id(libs.plugins.kover.get().pluginId)
    alias(libs.plugins.testballoon)
}

kotlin {
    compilerOptions.freeCompilerArgs.addAll(
        listOfNotNull(
        "-Xcontext-sensitive-resolution",
        "-Xcontext-parameters",
        "-Xreturn-value-checker=full",
        "-Xdebug".takeIf { System.getProperty("idea.active") == "true" }
    ))

    sourceSets.test {
        kotlin.srcDir("src/test/resources/kotlinTestData")
    }
}

dependencies {
    api(projects.typed)
    api(libs.kotlinpoet)
    testImplementation(kotlin("test"))
    testImplementation(libs.testballoon)
    testImplementation(libs.kotlinxresources)
    testImplementation(libs.datetime)
    testImplementation(ktorLibs.client.contentNegotiation)
    testImplementation(ktorLibs.serialization.kotlinx.json)
}

tasks.withType<DokkaTaskPartial>().configureEach {
    moduleName.set("OpenAPI Kotlin Renderer")
    dokkaSourceSets {
        named("main") {
            includes.from("README.md")
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl.set(
                    uri("https://github.com/nomisRev/OpenAPI-kt/tree/main/renderer/src/main").toURL()
                )
                remoteLineSuffix.set("#L")
            }
        }
    }
}
