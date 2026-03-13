import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id(libs.plugins.multiplatform.get().pluginId)
    alias(libs.plugins.serialization)
    id(libs.plugins.publish.get().pluginId)
    alias(libs.plugins.dokka)
    id(libs.plugins.kover.get().pluginId)
    alias(libs.plugins.testballoon)
    alias(libs.plugins.kotlinxresources)
}

kotlin {
    compilerOptions{
        freeCompilerArgs.addAll(listOfNotNull(
            "-Xcontext-sensitive-resolution",
            "-Xcontext-parameters",
            "-Xreturn-value-checker=full",
            "-Xdebug".takeIf { System.getProperty("idea.active") == "true" }
        ))
        allWarningsAsErrors = true
        extraWarnings = true
        progressiveMode = true
    }


    jvm()
    macosArm64()
    js(IR) { browser() }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            dependencies {
                api(projects.parser)
                implementation(ktorLibs.client.cio)
                implementation(ktorLibs.client.contentNegotiation)
                implementation(ktorLibs.serialization.kotlinx.json)
                implementation(libs.json)
                implementation(libs.datetime)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.testballoon)
                implementation(libs.kotlinxresources)
                implementation(libs.datetime)
                implementation(ktorLibs.client.contentNegotiation)
                implementation(ktorLibs.serialization.kotlinx.json)
            }
        }
    }
}

tasks.withType<DokkaTaskPartial>().configureEach {
    moduleName.set("OpenAPI Kotlin Typed")
    dokkaSourceSets {
        named("commonMain") {
            includes.from("README.md")
            sourceLink {
                localDirectory.set(file("src/commonMain/kotlin"))
                remoteUrl.set(
                    uri("https://github.com/nomisRev/OpenAPI-kt/tree/main/typed/src/commonMain").toURL()
                )
                remoteLineSuffix.set("#L")
            }
        }
    }
}
