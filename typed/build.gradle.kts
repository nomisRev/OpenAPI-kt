plugins {
    id(libs.plugins.multiplatform.get().pluginId)
    alias(libs.plugins.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinxresources)
    id(libs.plugins.publish.get().pluginId)
    id("test-conventions")
    alias(libs.plugins.testballoon)
}

kotlin {
    compilerOptions{
        freeCompilerArgs.addAll(listOfNotNull(
            "-Xcontext-sensitive-resolution",
            "-Xreturn-value-checker=full",
            "-Xname-based-destructuring=complete",
            "-Xdebug".takeIf { System.getProperty("idea.active") == "true" }
        ))
        allWarningsAsErrors = true
        extraWarnings = true
        progressiveMode = true
    }

    jvm()
    macosArm64()
    js { browser() }

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

dokka {
    moduleName = "OpenAPI Kotlin Typed"
    dokkaSourceSets.named("commonMain") {
        includes.from("README.md")
        sourceLink {
            localDirectory.set(file("src/commonMain/kotlin"))
            remoteUrl("https://github.com/nomisRev/OpenAPI-kt/tree/main/typed/src/commonMain")
            remoteLineSuffix = "#L"
        }
    }
}
