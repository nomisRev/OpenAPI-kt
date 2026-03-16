import org.gradle.api.attributes.Usage

plugins {
    kotlin("jvm") version "2.3.0"
    id("com.gradleup.gratatouille") version "0.2.2"
    id("io.github.nomisrev.openapi") version "unspecified"
}

kotlin {
    jvmToolchain(11)
}

configurations.matching { it.name == "gratatouille" }.configureEach {
    attributes {
        attribute(
            Usage.USAGE_ATTRIBUTE,
            objects.named(Usage::class.java, Usage.JAVA_RUNTIME)
        )
    }
}

dependencies {
    add("gratatouille", "openapi-kt:gradle-tasks:unspecified")
}

openApi {
    specFile.set(file("/Users/simonvergauwen/Developer/refactor-api/parser/src/commonTest/resources/specs/github.json"))
    modelPackage.set("io.github.nomisrev.github.generated.model")
    apiPackage.set("io.github.nomisrev.github.generated.api")
    targets.set(setOf("JVM"))
}
