import io.github.nomisrev.openapi.gradle.OpenApiExtension

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("io.github.nomisrev:openapi-kt-gradle-plugin:local-dev")
    }
}

plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"
}

apply(plugin = "io.github.nomisrev.openapi")

kotlin {
    jvmToolchain(11)
    sourceSets.main {
        kotlin.srcDir("build/generated/openapi/github")
    }
}

dependencies {
    implementation("io.ktor:ktor-client-core:3.3.3")
    implementation("io.ktor:ktor-client-apache5:3.3.3")
    implementation("io.ktor:ktor-client-content-negotiation:3.3.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")
    implementation("io.ktor:ktor-client-logging:3.3.3")
    implementation("ch.qos.logback:logback-classic:1.5.32")
}

configure<OpenApiExtension> {
    specFile.set(rootProject.file("../parser/src/commonTest/resources/specs/github.json"))
    modelPackage.set("io.github.model")
    apiPackage.set("io.github.api")
    targets.set(setOf("JVM"))
    outputDirectory.set(project.file("src/main/kotlin"))
}
