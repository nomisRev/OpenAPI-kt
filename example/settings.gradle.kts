rootProject.name = "example"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  repositories {
    mavenLocal()
    mavenCentral()
    google()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    google()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
