plugins {
  id(libs.plugins.multiplatform.get().pluginId)
  alias(libs.plugins.serialization)
}

kotlin {

  jvm()
  macosArm64()
  js(IR) { browser() }

  sourceSets {
    commonMain {
      dependencies {
        api(ktorLibs.client.cio)
      }
    }
  }
}
