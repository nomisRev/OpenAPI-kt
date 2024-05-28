import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization") version "2.0.0"
  kotlin("plugin.power-assert") version "2.0.0"
}

@Suppress("OPT_IN_USAGE")
powerAssert {
  functions = listOf("kotlin.test.assertEquals", "kotlin.test.assertTrue")
}

kotlin {
//  explicitApi()
  jvm {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    mainRun { mainClass.set("io.github.nomisrev.openapi.MainKt") }
  }
  macosArm64 {
    binaries {
      executable { entryPoint = "main" }
    }
  }
  linuxX64()

  sourceSets {
    commonMain {
      kotlin.srcDir(project.file("build/generated/openapi/src/commonMain/kotlin"))

      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.3.5")
        implementation("net.pearx.kasechange:kasechange:1.4.1")
        implementation(project(":core"))
        // for build debugging example
        implementation("io.exoquery:pprint-kotlin-kmp:2.0.2")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
      }
    }
    commonTest {
      dependencies {
        implementation(kotlin("test"))
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
