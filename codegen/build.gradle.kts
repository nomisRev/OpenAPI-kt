plugins {
  id(libs.plugins.multiplatform.get().pluginId)
  id(libs.plugins.publish.get().pluginId)
  id(libs.plugins.kover.get().pluginId)
}

kotlin {
  jvm()
  macosArm64()
  js {
    browser()
    nodejs()
  }

  sourceSets {
    val commonMain by getting { dependencies { api(libs.stdlib) } }
    val commonTest by getting { dependencies { implementation(libs.test) } }
  }
}
