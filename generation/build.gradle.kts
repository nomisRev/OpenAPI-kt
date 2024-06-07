import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
  id(libs.plugins.multiplatform.get().pluginId)
  alias(libs.plugins.serialization)
  alias(libs.plugins.assert)
  id(libs.plugins.publish.get().pluginId)
}

@Suppress("OPT_IN_USAGE")
powerAssert {
  functions = listOf("kotlin.test.assertEquals", "kotlin.test.assertTrue")
}

kotlin {
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
        api(libs.kasechange)
        api(libs.okio)
        api(projects.parser)
      }
    }
    commonTest {
      dependencies {
        implementation(libs.test)
      }
    }
//    jsMain {
//      dependencies {
//        implementation("com.squareup.okio:okio-nodefilesystem:3.9.0")
//      }
//    }
    commonTest {
      dependencies {
        implementation(libs.okio.fakefilesystem)
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
