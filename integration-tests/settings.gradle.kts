includeBuild("..") {
    dependencySubstitution {
        substitute(module("io.github.nomisrev:openapi-kt-gradle-plugin"))
            .using(project(":gradle-plugin"))
        // The example round-trip harness drives the generator pipeline as a library (see example-roundtrip/).
        substitute(module("io.github.nomisrev:openapi-kt-parser")).using(project(":parser"))
        substitute(module("io.github.nomisrev:openapi-kt-typed")).using(project(":typed"))
        substitute(module("io.github.nomisrev:openapi-kt-renderer")).using(project(":renderer"))
    }
}

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

rootProject.name = "integration-test"
include("github")
include("youtrack")
include("openai")
include("example-roundtrip")
