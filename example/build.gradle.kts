plugins {
  kotlin("multiplatform") version "2.0.0"
  kotlin("plugin.serialization") version "2.0.0"
  id("io.github.nomisrev.openapi-kt-plugin") version "0.0.4"
}

openApiConfig { spec("OpenAI", file("openai.yaml")) { packageName = "io.github.nomisrev.openai" } }

kotlin {
  jvm()
  sourceSets {
    commonMain {
      dependencies {
        implementation("io.ktor:ktor-client-core:2.3.6")
        implementation("io.ktor:ktor-client-content-negotiation:2.3.6")
        implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.6")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
      }
    }
  }
}
