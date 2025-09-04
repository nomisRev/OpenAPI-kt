enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    google()
  }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

rootProject.name = "openapi-kt"

include("parser")
include("typed")
include("generation")
include("plugin")
include("codegen")

include("codegen")

include("codegen-client")
