import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id(libs.plugins.multiplatform.get().pluginId)
    alias(libs.plugins.serialization)
    id(libs.plugins.publish.get().pluginId)
    alias(libs.plugins.dokka)
    id(libs.plugins.kover.get().pluginId)
    alias(libs.plugins.kotlinxresources)
}

kotlin {
    explicitApi()
    jvmToolchain(11)

    jvm()
    macosArm64()
    js(IR) { browser() }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.json)
                api(libs.kaml)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.test)
                implementation(libs.kotlinxresources)
            }
        }
    }
}

tasks.withType<DokkaTaskPartial>().configureEach {
    moduleName.set("OpenAPI Kotlin Parser")
    dokkaSourceSets {
        named("commonMain") {
            includes.from("README.md")
            sourceLink {
                localDirectory.set(file("src/commonMain/kotlin"))
                remoteUrl.set(
                    uri("https://github.com/nomisRev/OpenAPI-kt/tree/main/parser/src/commonMain").toURL()
                )
                remoteLineSuffix.set("#L")
            }
        }
    }
}
