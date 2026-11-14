// Engine of the example round-trip harness (see ../README.md). The three client projects depend on it from
// their test source sets and each supplies its own spec, model package and generated classes.
plugins {
    kotlin("jvm") version "2.4.0"
    kotlin("plugin.serialization") version "2.4.0"
}

kotlin { jvmToolchain(11) }

dependencies {
    // The generator pipeline, substituted by the included build (see settings.gradle.kts).
    api("io.github.nomisrev:openapi-kt-parser:local-dev")
    api("io.github.nomisrev:openapi-kt-typed:local-dev")
    api("io.github.nomisrev:openapi-kt-renderer:local-dev")
    api("com.squareup:kotlinpoet:2.3.0")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
    // Reads the YAML spec with scalar types intact (kaml and the parser lose the quoting of scalars).
    implementation("it.krzeminski:snakeyaml-engine-kmp:4.0.1")
    api(kotlin("test"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test { useJUnitPlatform() }
