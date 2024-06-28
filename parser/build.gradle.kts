import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id(libs.plugins.multiplatform.get().pluginId)
  alias(libs.plugins.serialization)
  id(libs.plugins.publish.get().pluginId)
  alias(libs.plugins.dokka)
}

kotlin {
  explicitApi()

  jvm()
//  macosArm64()
//  linuxX64()

  sourceSets {
    commonMain {
      dependencies {
        api(libs.json)
        // This should be KAML, but parsing OpenAI takes 57seconds
        // Compared to 100ms with SnakeYAML
        implementation("org.yaml:snakeyaml:2.1")
      }
    }
    jvmTest {
      dependencies {
        implementation(libs.test)
      }
    }
  }
}

tasks.withType<DokkaTaskPartial>().configureEach {
  moduleName.set("OpenAPI Kotlin Parser")
  dokkaSourceSets {
    named("commonMain") {
      includes.from("README.md")
      sourceLink {
        localDirectory.set(file("src/commonMain/kotlin"))
        remoteUrl.set(uri("https://github.com/nomisRev/OpenAPI-kt/tree/main/parser/src/commonMain").toURL())
        remoteLineSuffix.set("#L")
      }
    }
  }
}
