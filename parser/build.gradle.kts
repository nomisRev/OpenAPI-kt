plugins {
  id(libs.plugins.multiplatform.get().pluginId)
  alias(libs.plugins.serialization)
  id(libs.plugins.publish.get().pluginId)
}

kotlin {
  explicitApi()

  jvm()
  macosArm64()
  linuxX64()

  sourceSets {
    commonMain {
      dependencies {
        api(libs.json)
      }
    }
    jvmTest {
      dependencies {
        implementation(libs.test)
      }
    }
  }
}

