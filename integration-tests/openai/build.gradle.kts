import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id(libs.plugins.jvm.get().pluginId)
    id(libs.plugins.serialization.get().pluginId)
}

val specFile = rootProject.file("test-specs/openai.yaml")
val clientName = "OpenAI"

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
        freeCompilerArgs.addAll(
            "-Xcontext-sensitive-resolution",
            "-Xcontext-parameters"
        )
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

// Configuration to resolve the generator classpath
val generator: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    // Generator dependencies (for running code generation)
    generator(project(":renderer"))
    generator(project(":parser"))

    // Runtime dependencies for generated code
    implementation(ktorLibs.client.core)
    implementation(ktorLibs.client.cio)
    implementation(ktorLibs.client.contentNegotiation)
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(libs.json)
    implementation(libs.datetime)

    // Test dependencies
    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.client.mock)
}

val generateClient by tasks.registering(JavaExec::class) {
    group = "openapi"
    description = "Generate Kotlin client from OpenAI OpenAPI spec"

    mainClass.set("io.github.nomisrev.openapi.GenerateClientKt")
    classpath = generator

    val outputDir = layout.projectDirectory.dir("src/main/kotlin")

    inputs.file(specFile)
    outputs.dir(outputDir)

    doFirst {
        // Clear previous generated code
        outputDir.asFile.deleteRecursively()
        outputDir.asFile.mkdirs()
    }

    args = listOf(
        "--spec", specFile.absolutePath,
        "--format", "yaml",
        "--name", clientName,
        "--output", outputDir.asFile.absolutePath
    )
}

tasks.named("compileKotlin") {
    dependsOn(generateClient)
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events("SKIPPED", "FAILED", "PASSED")
    }
}
