plugins {
  id(libs.plugins.multiplatform.get().pluginId)
  id(libs.plugins.publish.get().pluginId)
}

kotlin {
  jvm()
  macosArm64()
  js {
    browser()
    nodejs()
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(libs.stdlib)
        api(projects.typed)
        api(projects.codegen)
      }
    }
    val jvmMain by getting { dependencies { api(libs.ktor.client) } }
    val commonTest by getting { dependencies { implementation(libs.test) } }
    val jvmTest by getting {
      dependencies {
        implementation(libs.test)
        implementation(libs.test.compile)
        implementation(projects.generation)
      }
    }
  }
}
