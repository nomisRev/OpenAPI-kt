import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
  kotlin("multiplatform")
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
//  explicitApi()
  jvm()
  linuxX64()
  macosArm64 {
    binaries {
      executable { entryPoint = "main" }
    }
  }
//  js {
//    nodejs()
//  }

  sourceSets {
    commonMain {
      kotlin.srcDir(project.file("build/generated/openapi/src/commonMain/kotlin"))

      dependencies {
        implementation(project(":core"))
        implementation("com.squareup.okio:okio:3.9.0")
        // for build debugging example
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
      }
    }
//    jsMain {
//      dependencies {
//        implementation("com.squareup.okio:okio-nodefilesystem:3.9.0")
//      }
//    }
    commonTest {
      dependencies {
        implementation("com.squareup.okio:okio-fakefilesystem:3.9.0")
      }
    }
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

task("runMacosArm64") {
  dependsOn("linkDebugExecutableMacosArm64")
  dependsOn("runDebugExecutableMacosArm64")
  group = "run"
}