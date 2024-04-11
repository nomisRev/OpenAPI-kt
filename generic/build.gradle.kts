plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.9.23"
    id("io.kotest.multiplatform") version "5.8.1"
}

kotlin {
    explicitApi()

    // TODO Setup targets
    jvm()
    macosArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            }
        }
        commonMain {
            dependencies {
                implementation("io.kotest:kotest-framework-engine:5.8.1")
                implementation("io.kotest:kotest-assertions-core:5.8.1")
                implementation("io.kotest:kotest-property:5.8.1")
            }
        }
        jvmMain {
            dependencies {
                implementation("io.kotest:kotest-runner-junit5:5.8.1")
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
