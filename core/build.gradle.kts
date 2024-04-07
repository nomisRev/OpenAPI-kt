import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "2.0.0-Beta5"
}

repositories {
    mavenCentral()
}

kotlin {
    @Suppress("OPT_IN_USAGE")
    compilerOptions {
        languageVersion.set(KotlinVersion.KOTLIN_2_0)
        apiVersion.set(KotlinVersion.KOTLIN_2_0)
    }
    explicitApi()
    jvm()
    macosArm64()
    androidNativeArm32()
    androidNativeArm64()
    linuxX64()
    wasmJs()
    wasmWasi()
    js(IR) {
        nodejs()
        browser()
    }

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
