import org.gradle.api.tasks.testing.Test

plugins {
    id(libs.plugins.multiplatform.get().pluginId)
    alias(libs.plugins.serialization)
    alias(libs.plugins.dokka)
    id(libs.plugins.publish.get().pluginId)
    id("test-conventions")
    alias(libs.plugins.testballoon)
}

kotlin {
    jvm()

    compilerOptions.freeCompilerArgs.addAll(
        listOfNotNull(
            "-Xcontext-sensitive-resolution",
            "-Xreturn-value-checker=full",
            "-Xname-based-destructuring=complete",
            "-Xdebug".takeIf { System.getProperty("idea.active") == "true" }
        ))

    sourceSets {
        jvmMain {
            dependencies {
                api(projects.typed)
                api(libs.kotlinpoet)
                implementation(ktorLibs.http)
            }
        }
        jvmTest {
            kotlin.srcDir("src/jvmTest/resources/kotlinTestData")

            dependencies {
                implementation(kotlin("test"))
                implementation(libs.testballoon)
                implementation(libs.kotlinxresources)
                implementation(libs.datetime)
                implementation(ktorLibs.client.core)
                implementation(ktorLibs.client.contentNegotiation)
                implementation(ktorLibs.serialization.kotlinx.json)
                implementation(libs.java.diff.utils)
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    systemProperty("updateGolden", project.findProperty("updateGolden")?.toString() ?: "false")
}


dokka {
    moduleName = "OpenAPI Kotlin Renderer"
    dokkaSourceSets.named("jvmMain") {
        includes.from("README.md")
        sourceLink {
            localDirectory.set(file("src/jvmMain/kotlin"))
            remoteUrl("https://github.com/nomisRev/OpenAPI-kt/tree/main/renderer/src/jvmMain")
            remoteLineSuffix = "#L"
        }
    }
}
