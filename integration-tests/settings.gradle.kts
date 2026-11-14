includeBuild("..") {
    dependencySubstitution {
        substitute(module("io.github.nomisrev:openapi-kt-gradle-plugin"))
            .using(project(":gradle-plugin"))
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
