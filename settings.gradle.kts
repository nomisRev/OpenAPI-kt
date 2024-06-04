enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

include("generation")
include("core")
//include("generic")
include("example")

includeBuild("plugin-build")