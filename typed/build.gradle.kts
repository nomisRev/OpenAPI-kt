import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id(libs.plugins.multiplatform.get().pluginId)
    alias(libs.plugins.serialization)
    id(libs.plugins.publish.get().pluginId)
    alias(libs.plugins.dokka)
    id(libs.plugins.kover.get().pluginId)
    alias(libs.plugins.testballoon)
}

kotlin {
    val debug = if (System.getProperty("idea.active") == "true") "-Xdebug" else null
    compilerOptions.freeCompilerArgs.addAll(listOfNotNull(
        "-Xcontext-sensitive-resolution",
        "-Xcontext-parameters",
        "-Xreturn-value-checker=full",
    ))

    jvm()
    macosArm64()
    js(IR) { browser() }

    applyDefaultHierarchyTemplate()

    sourceSets {
        // jvmAndNative is the target we use for generation (we'll include js/wasm node later).
        val jvmAndNative by creating { dependsOn(commonMain.get()) }
        macosArm64Main.get().dependsOn(jvmAndNative)
        jvmMain.get().dependsOn(jvmAndNative)

        commonMain {
            dependencies {
                api(projects.parser)
                implementation(ktorLibs.client.cio)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.testballoon)
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
