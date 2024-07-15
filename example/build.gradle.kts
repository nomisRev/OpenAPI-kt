plugins {
  kotlin("multiplatform") version "2.0.0"
  kotlin("plugin.serialization") version "2.0.0"
  id("io.github.nomisrev.openapi-kt-plugin") version "0.0.7"
}

openApiConfig { spec("OpenAI", file("openai.yaml")) {
  packageName = "io.github.nomisrev.openai" }
}

kotlin {
  jvm()
  js {
    browser()
    nodejs()
  }
  iosArm64()
  macosArm64()
  linuxX64()
  mingwX64()

  sourceSets {
    commonMain {
      dependencies {
        implementation("io.ktor:ktor-client-core:2.3.6")
        implementation("io.ktor:ktor-client-content-negotiation:2.3.6")
        implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.6")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
      }
    }
    val jvmMain by getting {
      dependencies {
        api("io.ktor:ktor-client-cio:2.3.6")
      }
    }
    val jsMain by getting {
      dependencies {
        api("io.ktor:ktor-client-js:2.3.6")
      }
    }
    val iosArm64Main by getting {
      dependencies {
        implementation("io.ktor:ktor-client-cio:2.3.6")
      }
    }
    val linuxX64Main by getting {
      dependencies {
        implementation("io.ktor:ktor-client-cio:2.3.6")
      }
    }
    val macosArm64Main by getting {
      dependencies {
        implementation("io.ktor:ktor-client-cio:2.3.6")
      }
    }
    val mingwX64Main by getting {
      dependencies {
        implementation("io.ktor:ktor-client-winhttp:2.3.6")
      }
    }
  }
}
