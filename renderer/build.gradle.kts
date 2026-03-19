import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.gradle.api.tasks.testing.Test

plugins {
    id(libs.plugins.multiplatform.get().pluginId)
    alias(libs.plugins.serialization)
    id(libs.plugins.publish.get().pluginId)
    alias(libs.plugins.dokka)
    id(libs.plugins.kover.get().pluginId)
    alias(libs.plugins.testballoon)
}

kotlin {
    jvm()

    compilerOptions.freeCompilerArgs.addAll(
        "-Xreturn-value-checker=full",
        "-Xname-based-destructuring=full",
    )

    compilerOptions.freeCompilerArgs.addAll(
        listOfNotNull(
            "-Xcontext-sensitive-resolution",
            "-Xcontext-parameters",
            "-Xreturn-value-checker=full",
            "-Xname-based-destructuring=full",
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



tasks.withType<DokkaTaskPartial>().configureEach {
    moduleName.set("OpenAPI Kotlin Renderer")
    dokkaSourceSets {
        named("commonMain") {
            includes.from("README.md")
            sourceLink {
                localDirectory.set(file("src/commonMain/kotlin"))
                remoteUrl.set(
                    uri("https://github.com/nomisRev/OpenAPI-kt/tree/main/renderer/src/commonMain").toURL()
                )
                remoteLineSuffix.set("#L")
            }
        }
    }
}
