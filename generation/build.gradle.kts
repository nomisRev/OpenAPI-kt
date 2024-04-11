plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization") version "1.9.23"
  id("com.bnorm.power.kotlin-power-assert") version "0.13.0"
}

configure<com.bnorm.power.PowerAssertGradleExtension> {
  functions = listOf("kotlin.assert", "kotlin.test.assertTrue")
}

kotlin {
// TODO re-enable platforms after finishing core / generation
//   Not worth dealing with all extra platforms during initial phase
//  explicitApi()
  jvm()
  macosArm64 {
    binaries {
      executable { entryPoint = "main" }
    }
  }

  sourceSets {
    commonMain {
      kotlin.srcDir(project.file("build/generated/openapi/src/commonMain/kotlin"))

      dependencies {
        implementation("net.pearx.kasechange:kasechange:1.4.1")
        implementation(project(":core"))
        implementation("com.squareup.okio:okio:3.9.0")
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