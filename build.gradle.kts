import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0

plugins {
  kotlin("multiplatform") version "2.0.0-Beta5"
  kotlin("plugin.serialization") version "2.0.0-Beta5"
  id("com.goncalossilva.resources") version "0.4.0"
}

group = "io.github.nomisrev"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

kotlin {
  @Suppress("OPT_IN_USAGE")
  compilerOptions {
    languageVersion.set(KOTLIN_2_0)
    apiVersion.set(KOTLIN_2_0)
  }
  explicitApi()
  jvmToolchain(21)

  jvm()
//  macosArm64()

  sourceSets {
    commonMain {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
      }
    }
    commonTest {
      dependencies {
        implementation(kotlin("test"))
        implementation("com.goncalossilva:resources:0.4.0")
      }
    }
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
