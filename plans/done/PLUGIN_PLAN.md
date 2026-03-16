# Plan: Create Gradle Plugin using Gratatouille

## Context

The project has three modules (parser, typed, renderer) that form a pipeline: OpenAPI spec → parsed model → typed AST → generated Kotlin code. Currently there's no Gradle plugin for consumers to use this pipeline. We'll create one using [Gratatouille](https://github.com/GradleUp/gratatouille) which generates task classes, workers, and wiring from annotated Kotlin functions, with classloader isolation.

## Architecture

Two new modules following Gratatouille's classloader isolation pattern:

```
gradle-tasks/   → @GTask function + implementation deps (parser, typed, renderer)
gradle-plugin/  → @GPlugin function + extension + source set wiring
```

## Step 1: Update version catalog

**File:** `gradle/libs.versions.toml`

Add:
```toml
[versions]
gratatouille = "0.2.2"
coroutines = "1.10.1"

[libraries]
coroutines-core = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "coroutines" }

[plugins]
gratatouille = { id = "com.gradleup.gratatouille", version.ref = "gratatouille" }
gratatouille-tasks = { id = "com.gradleup.gratatouille.tasks", version.ref = "gratatouille" }
```

## Step 2: Update settings.gradle.kts

Add the two new modules:
```kotlin
include("gradle-tasks")
include("gradle-plugin")
```

## Step 3: Create `gradle-tasks` module

**`gradle-tasks/build.gradle.kts`**
- Apply `kotlin("jvm")`, KSP, and `com.gradleup.gratatouille.tasks`
- Depend on `projects.renderer` (transitively brings parser + typed)
- Depend on `kotlinx-coroutines-core` (for `runBlocking` bridge since `toApiTree()` is suspend)

**`gradle-tasks/gradle.properties`** — POM metadata

**`gradle-tasks/src/main/kotlin/io/github/nomisrev/openapi/gradle/GenerateOpenApiTask.kt`**

```kotlin
@GTask(description = "Generates Kotlin source code from an OpenAPI specification", group = "openapi")
internal fun generateOpenApi(
    specFile: GInputFile,
    modelPackage: String,
    apiPackage: String,
    targets: Set<String>,       // "JVM", "JS" — strings to cross classloader boundary
    outputDirectory: GOutputDirectory,
) {
    // 1. Read & parse spec (YAML/JSON based on extension)
    // 2. Build RenderConfig from string params
    // 3. runBlocking { openApi.generate(config) }
    // 4. Write each FileSpec to outputDirectory
}
```

Key reuse:
- `OpenAPI.fromYaml()` / `OpenAPI.fromJson()` from `parser` module (`parser/.../OpenAPI.kt:73-75`)
- `OpenAPI.generate(config)` from `renderer` module (`renderer/.../Generate.kt:6-7`)
- `FileSpec.writeTo(directory)` from KotlinPoet

## Step 4: Create `gradle-plugin` module

**`gradle-plugin/build.gradle.kts`**
- Apply `kotlin("jvm")`, KSP, and `com.gradleup.gratatouille`
- Configure `gratatouille { pluginMarker("io.github.nomisrev.openapi") }`
- Depend on gradle-tasks via `gratatouille(projects.gradleTasks)`

**`gradle-plugin/gradle.properties`** — POM metadata

**`gradle-plugin/src/main/kotlin/io/github/nomisrev/openapi/gradle/OpenApiExtension.kt`**

Extension with lazy Gradle properties:
- `specFile: RegularFileProperty` — path to the OpenAPI spec
- `modelPackage: Property<String>` — default `"io.github.nomisrev.openapi.generated.model"`
- `apiPackage: Property<String>` — default `"io.github.nomisrev.openapi.generated.api"`
- `targets: SetProperty<String>` — default `setOf("JVM", "JS")`

**`gradle-plugin/src/main/kotlin/io/github/nomisrev/openapi/gradle/OpenApiPlugin.kt`**

```kotlin
@GPlugin(id = "io.github.nomisrev.openapi")
fun openApiPlugin(project: Project) {
    // 1. Create "openApi" extension
    // 2. Register task via generated registerGenerateOpenApiTask()
    // 3. Wire output into Kotlin source sets (JVM or multiplatform via plugins.withId)
}
```

Source set wiring uses `plugins.withId("org.jetbrains.kotlin.jvm")` and `plugins.withId("org.jetbrains.kotlin.multiplatform")` to react to whichever Kotlin plugin the consumer applies.

## Step 5: Adjust root build.gradle.kts

Exclude `gradle-plugin` and `gradle-tasks` from the `vanniktech.maven.publish` filter (Gratatouille handles its own publishing):

```kotlin
configure(subprojects.filter {
    !it.path.startsWith(":integration-tests") &&
    !it.path.startsWith(":gradle-plugin") &&
    !it.path.startsWith(":gradle-tasks")
}) { ... }
```

## Consumer usage (for reference)

```kotlin
plugins {
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
    id("io.github.nomisrev.openapi") version "<version>"
}

openApi {
    specFile.set(file("src/main/openapi/petstore.yaml"))
    modelPackage.set("com.example.api.model")
    apiPackage.set("com.example.api")
}
```

## Key files to modify/create

| File | Action |
|------|--------|
| `gradle/libs.versions.toml` | Edit — add gratatouille + coroutines |
| `settings.gradle.kts` | Edit — include new modules |
| `build.gradle.kts` (root) | Edit — exclude new modules from publish filter |
| `gradle-tasks/build.gradle.kts` | Create |
| `gradle-tasks/gradle.properties` | Create |
| `gradle-tasks/src/main/kotlin/.../GenerateOpenApiTask.kt` | Create |
| `gradle-plugin/build.gradle.kts` | Create |
| `gradle-plugin/gradle.properties` | Create |
| `gradle-plugin/src/main/kotlin/.../OpenApiExtension.kt` | Create |
| `gradle-plugin/src/main/kotlin/.../OpenApiPlugin.kt` | Create |

## Key existing code to reuse

- `OpenAPI.fromYaml()`/`fromJson()` — `parser/src/commonMain/.../OpenAPI.kt`
- `OpenAPI.generate(config)` — `renderer/src/jvmMain/.../Generate.kt:6`
- `RenderConfig` — `renderer/src/jvmMain/.../RenderConfig.kt`
- `KmpTarget` — `renderer/src/jvmMain/.../RenderConfig.kt:9`

## Notes

- Plugin ID: `io.github.nomisrev.openapi`
- Local only for now — no Maven Central publishing setup needed
- Gradle 9.4.0, Kotlin 2.3.0, Gratatouille 0.2.2

## Verification

1. `./gradlew :gradle-tasks:build` — KSP processes `@GTask`, module compiles
2. `./gradlew :gradle-plugin:build` — KSP processes `@GPlugin`, `registerGenerateOpenApiTask` resolves
3. `./gradlew publishToMavenLocal` then apply plugin in a test project with a petstore.yaml spec and verify generated code compiles
