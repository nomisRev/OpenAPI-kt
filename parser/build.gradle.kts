import java.time.Duration

plugins {
    id(libs.plugins.multiplatform.get().pluginId)
    alias(libs.plugins.serialization)
    id(libs.plugins.publish.get().pluginId)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinxresources)
    id("test-conventions")
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
        extraWarnings = true
        progressiveMode = true
    }
    explicitApi()

    jvm()
    macosArm64()
    js {
        browser {
            testTask {
                useMocha {
                    timeout = "10s"
                }
//                useKarma {
//                    useChromeHeadless()
//                    timeout.set(Duration.ofMinutes(5))
//                }
            }
        }
    }

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

dokka {
    moduleName = "OpenAPI Kotlin Parser"
    dokkaSourceSets.named("commonMain") {
        includes.from("README.md")
        sourceLink {
            localDirectory.set(file("src/commonMain/kotlin"))
            remoteUrl("https://github.com/nomisRev/OpenAPI-kt/tree/main/parser/src/commonMain")
            remoteLineSuffix = "#L"
        }
    }
}
