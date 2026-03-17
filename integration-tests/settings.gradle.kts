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

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

rootProject.name = "integration-test"
include("github")
