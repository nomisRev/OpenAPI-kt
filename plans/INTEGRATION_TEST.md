# Plan: Compilation Integration Tests via kotlin-compile-testing

> Goal: Take existing OpenAPI specs, run the full generation pipeline, and compile the output to prove it produces valid Kotlin.

## Library

[dev.zacsweers.kctfork:core:0.12.1](https://github.com/ZacSweers/kotlin-compile-testing) — maintained fork of kotlin-compile-testing with Kotlin 2.x / K2 compiler support.

## Architectural decisions

- **JVM-only**: kotlin-compile-testing wraps `kotlin-compiler-embeddable` which is JVM-only. Tests go in a new `jvmTest` source set (not `jvmAndNativeTest`).
- **Full pipeline**: Each test runs the complete pipeline: `OpenAPI.fromJson/fromYaml()` → `toApiModel()` → `generate()` + `routes.sort(name).generateClient()` → `List<KFile>` → compile.
- **Classpath management**: Generated code imports ktor-client, kotlinx-serialization, and kotlinx-datetime at runtime. These JARs must be on the `KotlinCompilation` classpath. We resolve them from the test's own classloader at runtime.
- **No behavioral assertions**: These tests only verify that generated code compiles (`ExitCode.OK`). Behavioral testing (calling generated clients against mock servers) is a separate concern.
- **Incremental rollout**: Start with small specs (petstore), then add real-world specs (openai, github) once the renderer is mature enough.

---

## Phase 1: Infrastructure setup

### What to build

Add the kotlin-compile-testing dependency and create a helper that takes a `List<KFile>` and compiles it, asserting `ExitCode.OK`. The helper must configure the `KotlinCompilation` with:
- Sources from `KFile.content` mapped to `SourceFile.kotlin(name, content)`
- Classpath including ktor-client-core, ktor-client-cio, ktor-serialization-kotlinx-json, ktor-client-content-negotiation, kotlinx-serialization-core, kotlinx-serialization-json, kotlinx-datetime, and kotlin-stdlib
- Kotlin language version and compiler args matching the project (`-Xcontext-parameters`, `-Xcontext-sensitive-resolution`)

### Changes

**`gradle/libs.versions.toml`**
- Update `test-compile = "0.12.1"`
- Add library entry: `compile-testing = { module = "dev.zacsweers.kctfork:core", version.ref = "test-compile" }`

**`typed/build.gradle.kts`**
- Add `jvmTest` source set (create if needed, already part of default hierarchy from `jvm()` target)
- Add dependency: `jvmTest { dependencies { implementation(libs.compile.testing) } }`

**`typed/src/jvmTest/kotlin/io/github/nomisrev/openapi/CompilationTestHelper.kt`**
```kotlin
package io.github.nomisrev.openapi

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import kotlin.test.assertEquals

/**
 * Compiles the given [KFile] list and asserts successful compilation.
 * Classpath is resolved from the current test classloader,
 * which includes ktor-client, serialization, and datetime via Gradle dependencies.
 */
fun assertCompiles(files: List<KFile>) {
    val result = KotlinCompilation().apply {
        sources = files.map { SourceFile.kotlin(it.name, it.content) }
        inheritClassPath = true  // inherit test runtime classpath (ktor, serialization, etc.)
        kotlincArguments = listOf(
            "-Xcontext-parameters",
            "-Xcontext-sensitive-resolution",
        )
    }.compile()
    assertEquals(
        KotlinCompilation.ExitCode.OK,
        result.exitCode,
        buildString {
            appendLine("Compilation failed:")
            appendLine(result.messages)
        }
    )
}
```

> **Note**: `inheritClassPath = true` resolves the classpath problem — it picks up all JARs available to the test JVM, which includes ktor-client, serialization, and datetime since they're transitive dependencies of the `typed` module.

### Acceptance criteria

- [x] `libs.versions.toml` declares `dev.zacsweers.kctfork:core:0.12.1`
- [x] `typed/build.gradle.kts` adds it as a `jvmTest` dependency
- [x] `assertCompiles(files)` helper exists and correctly configures `KotlinCompilation`
- [x] A trivial smoke test compiles a `fun main() {}` source to prove the infrastructure works

---

## Phase 2: Model compilation tests

### What to build

Tests that parse small/medium OpenAPI specs, generate **model** code via `ApiModel.generate()`, and compile it. These catch issues in `ObjectRenderer`, `UnionRenderer`, `EnumRender`, and `DiscriminatedObjectRender`.

**`typed/src/jvmTest/kotlin/io/github/nomisrev/openapi/ModelCompilationSpec.kt`**

### Test cases

| Test | Spec | What it validates |
|------|------|-------------------|
| `petstore models compile` | `petstore.json` | Basic objects, enums, primitives |
| `petstore_more models compile` | `petstore_more.json` | Extended objects with more properties |
| `basic models compile` | `basic.json` | Various schema patterns |

### Acceptance criteria

- [ ] Each test runs the full pipeline: parse → `toApiModel()` → `generate()` → `assertCompiles()`
- [ ] All listed specs produce `ExitCode.OK`
- [ ] Compilation errors surface clearly in test failure messages (the helper already includes `result.messages`)

---

## Phase 3: Client compilation tests

### What to build

Tests that parse specs, generate **client** code via `routes.sort(name).generateClient()`, and compile it together with the model files. This validates `ClientRenderer` output end-to-end.

**`typed/src/jvmTest/kotlin/io/github/nomisrev/openapi/ClientCompilationSpec.kt`**

### Test cases

| Test | Spec | What it validates |
|------|------|-------------------|
| `petstore client compiles` | `petstore.json` | Root interface, impl, factory, GET operations |
| `petstore_more client compiles` | `petstore_more.json` | Multiple operations, parameters, request bodies |

### Pipeline per test

```kotlin
val spec = OpenAPI.fromJson(readText("petstore.json"))
val apiModel = spec.toApiModel()
val modelFiles = apiModel.generate()
val clientFiles = apiModel.routes.sort(spec.info.title).generateClient()
assertCompiles(modelFiles + clientFiles)  // models + client together
```

### Acceptance criteria

- [ ] Client + model files compile together (client code references model types)
- [ ] Parameter rendering (path, query, header, cookie) produces valid code
- [ ] Request body rendering (JSON, multipart, form-urlencoded) produces valid code
- [ ] Sealed return types (when implemented) produce valid code
- [ ] Nested interface hierarchies produce valid code

---

## Phase 4: Real-world spec compilation

### What to build

Once the renderer is mature enough, add compilation tests for large real-world specs. These are slow (large codegen + compilation) so they should be tagged or gated.

### Test cases

| Test | Spec | Size | Notes |
|------|------|------|-------|
| `openai client compiles` | `openai.yaml` | 1.1 MB | Complex nested paths, discriminated unions |
| `youtrack client compiles` | `youtrack.json` | 508 KB | Medium complexity |
| `github client compiles` | `github.json` | 11 MB | Largest spec — may need longer timeout or be CI-only |

### Acceptance criteria

- [ ] At least `openai.yaml` and `youtrack.json` produce compilable code
- [ ] Tests that are too slow for local dev can be excluded via a Gradle property or JUnit tag
- [ ] Compilation errors are actionable — the test output identifies which generated file and line failed

---

## Risks & mitigations

| Risk | Mitigation |
|------|-----------|
| kotlin-compile-testing 0.12.1 doesn't support Kotlin 2.3.0 | Pin to a compatible version; the ZacSweers fork tracks K2 closely. Fall back to 0.11.x if needed. |
| `inheritClassPath` picks up too many/too few JARs | If it fails, manually resolve specific JARs via `classpaths = listOf(...)` using classloader resource lookup. |
| Large specs (github.json) are too slow to compile in tests | Gate behind a system property: `assumeTrue(System.getProperty("runSlowTests") != null)` |
| Compiler args (`-Xcontext-parameters`) not recognized | These are standard Kotlin 2.x flags. If KCT doesn't forward them, use `kotlincArguments` or `pluginOptions`. |
| Generated code has import conflicts or missing imports | The compilation failure message will pinpoint the exact file and line — fix in the renderer. |
