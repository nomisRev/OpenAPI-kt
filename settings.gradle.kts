enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    google()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "openapi-kt"

include("parser")
include("typed")
include("generation")
include("plugin")
//include("generic")
