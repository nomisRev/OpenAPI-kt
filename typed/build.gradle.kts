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
    compilerOptions.freeCompilerArgs.addAll(listOfNotNull(
        "-Xcontext-sensitive-resolution",
        "-Xcontext-parameters",
        "-Xreturn-value-checker=full",
        "-Xdebug".takeIf { System.getProperty("idea.active") == "true" } ?: null
    ))

    jvm()
    macosArm64()
    js(IR) { browser() }

    applyDefaultHierarchyTemplate()

    sourceSets {
        // jvmAndNative is the target we use for generation (we'll include js/wasm node later).
        val jvmAndNativeMain by creating { dependsOn(commonMain.get()) }
        macosArm64Main.get().dependsOn(jvmAndNativeMain)
        jvmMain.get().dependsOn(jvmAndNativeMain)

        val jvmAndNativeTest by creating { dependsOn(commonTest.get()) }
        macosArm64Test.get().dependsOn(jvmAndNativeTest)
        jvmTest.get().dependsOn(jvmAndNativeTest)

        commonMain {
            dependencies {
                api(projects.parser)
                implementation(ktorLibs.client.cio)
            }
        }
        commonTest {
            kotlin.srcDir("src/commonTest/resources/kotlinTestData")
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
