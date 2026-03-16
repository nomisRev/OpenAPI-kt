import org.gradle.api.attributes.Usage

plugins {
    kotlin("jvm") version "2.3.0"
//    id("io.github.nomisrev.openapi") version "unspecified"
}

kotlin {
    jvmToolchain(11)
}

//openApi {
//    specFile.set(file("/Users/simonvergauwen/Developer/refactor-api/parser/src/commonTest/resources/specs/github.json"))
//    modelPackage.set("io.github.nomisrev.github.generated.model")
//    apiPackage.set("io.github.nomisrev.github.generated.api")
//    targets.set(setOf("JVM"))
//}
//