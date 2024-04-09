plugins {
  kotlin("multiplatform")
}

kotlin {
// TODO re-enable platforms after finishing core / generation
//   Not worth dealing with all extra platforms during initial phase
  explicitApi()
  jvm()
  macosArm64 {
    binaries {
      executable { entryPoint = "main" }
    }
  }

  sourceSets {
    commonMain {
//      kotlin.srcDir(project.file("build/generated/openapi/src/commonMain/kotlin"))

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