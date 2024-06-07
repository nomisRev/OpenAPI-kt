import io.github.nomisrev.openapi.generation.NamingStrategy

plugins {
  kotlin("multiplatform") version "2.0.0"
  id("io.github.nomisrev.openapi.plugin") version "1.0.0"
  kotlin("plugin.serialization") version "2.0.0"
}

kotlin {
  jvm()
  sourceSets {
    commonMain {
      kotlin.srcDir(project.file("build/generated/openapi/src/commonMain/kotlin"))
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
      }
    }
  }
}

openApiConfig {
  spec.set(file("openai.json"))
}
