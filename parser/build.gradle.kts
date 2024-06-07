plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.9.23"
}

kotlin {
    explicitApi()


    jvm()
    macosArm64()
    linuxX64()

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
