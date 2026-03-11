# Golden File Testing Migration Plan

Move render test expected output from inline `trimMargin()` strings to compiled `.kt` source files. This gives us IDE syntax highlighting, compile-time validation of expected output, and easier maintenance.

## Architecture

- **Golden files** live in `commonTest` as compiled Kotlin source → validates syntax on all KMP targets (JVM, JS, macOS ARM64)
- **Golden-reading tests** live in `jvmAndNativeTest` using `SystemFileSystem` → same pattern as `IntegrationSpec`
- Each test case gets a **sub-package** to avoid name collisions (most tests define `Foo`, `Sort`, `Union`)
- Golden files use `@file:Suppress("unused")` since the code is never called
- **Body extraction** strips package/imports/file-annotations from golden files for body-only comparison
- **Import assertions** remain explicit in test calls (not parsed from golden files)

---

## Task 1: Create golden file infrastructure

**Files to create:**
- `typed/src/commonTest/kotlin/io/github/nomisrev/render/golden/GoldenUtils.kt`
  - `extractBody(content: String): String` — strips `package`, `import`, `@file:`, blank-line header from golden file content, returns code body
  - `normalizePackage(content: String, targetPackage: String): String` — replaces golden package with expected package (for snapshot tests)
- `typed/src/jvmAndNativeTest/kotlin/io/github/nomisrev/render/golden/GoldenReader.kt`
  - `readGolden(relativePath: String): String` — reads golden file from source tree via `SystemFileSystem` with multi-candidate path resolution (repo root vs module root, same pattern as `IntegrationSpec`)
  - `readGoldenBody(relativePath: String): String` — `extractBody(readGolden(...))`
  - `readGoldenSnapshot(dir: String, vararg files: String): String` — reads multiple golden files, normalizes packages to `io.github.nomisrev.api`, joins with `// FileName.kt\n...` format to match `snapshot()` output

**Acceptance criteria:**
- [ ] `extractBody` correctly strips file annotation, package, imports, leading blank lines
- [ ] `extractBody` preserves body annotations like `@Serializable`, `@OptIn`, `@JvmInline`
- [ ] `readGolden` resolves paths from both repo root and module root working directories
- [ ] `readGoldenSnapshot` produces output identical to the `snapshot()` format in `ClientRenderSpec`
- [ ] Unit tests for `extractBody` and `normalizePackage`

---

## Task 2: Create golden files and spec for ObjectRenderSpec (10 tests)

Migrate all 10 `verify()` calls from `ObjectRenderSpec.kt` to golden files.

**Golden files to create** under `typed/src/commonTest/kotlin/io/github/nomisrev/render/golden/model/`:
| Sub-package | File | Expected content |
|---|---|---|
| `dataobject` | `Foo.kt` | `@Serializable data object Foo` |
| `valueclassstring` | `Foo.kt` | `@Serializable @JvmInline value class Foo(val value: String)` |
| `valueclassnullable` | `Foo.kt` | nullable string value class |
| `singleline` | `Foo.kt` | single-line data class with 4 properties |
| `multiline` | `Foo.kt` | multi-line data class with `@SerialName`, `Uuid`, `LocalDateTime` |
| `nestedenum` | `Foo.kt` | value class wrapping nested enum |
| `primitiveimports` | `Foo.kt` | data class with `LocalDate`, `LocalDateTime`, `Uuid`, `JsonElement`, `JsonArray`, `JsonObject` |
| `emptyobject` | `EmptyObject.kt` | `@Serializable data object EmptyObject` |
| `additionaljson` | `PersonWithAdditionalProperties.kt` | data class + custom serializer with `JsonObject` additional properties |
| `additionaltyped` | `PersonWithAdditionalProperties.kt` | data class + custom serializer with typed `Map` additional properties |

**Test spec to create:**
- `typed/src/jvmAndNativeTest/kotlin/io/github/nomisrev/render/golden/ObjectGoldenSpec.kt`

**Acceptance criteria:**
- [ ] All 10 golden files compile on all KMP targets
- [ ] `ObjectGoldenSpec` passes on JVM and macOS ARM64
- [ ] Golden file content matches the inline strings from `ObjectRenderSpec` exactly (after body extraction)
- [ ] Breaking a golden file's syntax causes a compilation error

---

## Task 3: Create golden files and spec for EnumRenderSpec (2 tests)

**Golden files** under `typed/src/commonTest/kotlin/io/github/nomisrev/render/golden/enum_/`:
| Sub-package | File | Expected content |
|---|---|---|
| `simple` | `Sort.kt` | `@Serializable enum class Sort { ASC, DESC; }` |
| `serialname` | `Sort.kt` | enum with `@SerialName` annotations on each entry |

**Test spec:** `typed/src/jvmAndNativeTest/kotlin/io/github/nomisrev/render/golden/EnumGoldenSpec.kt`

**Acceptance criteria:**
- [ ] Both golden files compile
- [ ] `EnumGoldenSpec` passes

---

## Task 4: Create golden files and spec for CollectionRenderSpec (1 test)

**Golden file:** `typed/src/commonTest/kotlin/io/github/nomisrev/render/golden/collection/nested/Foo.kt`

**Test spec:** `typed/src/jvmAndNativeTest/kotlin/io/github/nomisrev/render/golden/CollectionGoldenSpec.kt`

**Acceptance criteria:**
- [ ] Golden file compiles
- [ ] `CollectionGoldenSpec` passes

---

## Task 5: Create golden files and spec for DiscriminatedObjectRenderSpec (1 test)

**Golden file:** `typed/src/commonTest/kotlin/io/github/nomisrev/render/golden/discriminated/sealed/User.kt`

**Test spec:** `typed/src/jvmAndNativeTest/kotlin/io/github/nomisrev/render/golden/DiscriminatedObjectGoldenSpec.kt`

**Acceptance criteria:**
- [ ] Golden file compiles
- [ ] `DiscriminatedObjectGoldenSpec` passes

---

## Task 6: Create golden files and spec for UnionRenderSpec (~15 tests)

Migrate all `verify()` calls (10) and convert `assertTrue` behavior tests (5) to full-equality golden file tests.

**Golden files** under `typed/src/commonTest/kotlin/io/github/nomisrev/render/golden/union/`:
- One sub-package per test case: `allprimitives/`, `objectcase/`, `discriminated/`, `discriminatedref/`, `nestedcollection/`, `reference/`, `singlecase/`, `multipleobjects/`, `singleobject/`, `collectionref/`
- Plus sub-packages for the 5 converted assertTrue tests

**Converting assertTrue tests:** Run the renderer for each test case, capture the full output, save as the golden file. Replace `assertTrue(actual.contains(...))` and `assertOrdered(...)` with `assertEq(readGoldenBody(...), actual)`.

**Test spec:** `typed/src/jvmAndNativeTest/kotlin/io/github/nomisrev/render/golden/UnionGoldenSpec.kt`

**Acceptance criteria:**
- [ ] All union golden files compile (these include complex custom serializers)
- [ ] `UnionGoldenSpec` passes
- [ ] Former assertTrue tests now do full equality comparison

---

## Task 7: Create client stubs for model type references

Client golden files reference types that would be generated from models (e.g., `ListPetsResponse`, `PetResponse`, `CreateChatCompletionRequest`). Create minimal stub types so golden files compile.

**File:** `typed/src/commonTest/kotlin/io/github/nomisrev/render/golden/client/stubs/Stubs.kt`

Content: empty class declarations for each referenced model type, in a shared package that client golden files can import.

**Acceptance criteria:**
- [ ] All stub types declared
- [ ] Client golden files that reference model types can import and compile against stubs

---

## Task 8: Create golden files and spec for ClientRenderSpec full-equality tests (8 tests)

Migrate the 8 `assertEq()` tests from `ClientRenderSpec.kt`.

**Golden files** under `typed/src/commonTest/kotlin/io/github/nomisrev/render/golden/client/`:
| Sub-package | File | Test |
|---|---|---|
| `singleget` | `PetStore.kt` | single parameterless GET endpoint |
| `returnsreference` | `PetStore.kt` | GET returning reference type |
| `returnsunit` | `PetStore.kt` | GET returning Unit |
| `emptyroot` | `EmptyApi.kt` | empty root interface |
| `requestbodyjson` | `OpenAI.kt` | JSON request body |
| `requestbodymultipart` | `OpenAI.kt` | multipart request body |
| `requestbodyform` | `OpenAI.kt` | form-urlencoded request body |
| `multipleresponses` | `Api.kt` | multiple response status codes |

**Test spec:** `typed/src/jvmAndNativeTest/kotlin/io/github/nomisrev/render/golden/ClientGoldenSpec.kt`

**Acceptance criteria:**
- [ ] All 8 client golden files compile (requires ktor client deps accessible in commonTest)
- [ ] `ClientGoldenSpec` passes

---

## Task 9: Convert ClientRenderSpec partial-matching tests to full-equality golden files (~18 tests)

Convert all `assertTrue(actual.contains(...))` tests to full-equality golden file tests.

**Approach:**
1. Run each test's renderer to capture complete output
2. Create golden file with full output (adding package + imports for compilation)
3. Replace `assertTrue` assertions with `assertEq(readGoldenBody(...), actual)`

**Golden files** under `typed/src/commonTest/kotlin/io/github/nomisrev/render/golden/client/`:
| Sub-package | Tests covered |
|---|---|
| `servers` | static server sealed interface |
| `servervariables` | server variables with enum + string params |
| `serverfallback` | server naming fallback (Default, Server1/Server2) |
| `readvariant` | read variant response type suffix |
| `pathparam` | path parameter interpolation |
| `queryparams` | query parameter handling |
| `headerparams` | header parameter handling |
| `cookieparams` | cookie parameter handling |
| `paramorder` | parameter ordering (path > query > header > cookie, required first) |
| `defaultvalues` | default parameter values |
| `deprecated` | @Deprecated annotation |
| `camelcase` | camelCase conversion of operation IDs |
| `nocontent` | no-content response with data object case |

Note: Some current tests check multiple behaviors in one `test()` block with multiple `assertTrue` calls. These may need to be split or the golden file captures the single full output that satisfies all assertions.

**Acceptance criteria:**
- [ ] All converted golden files compile
- [ ] All tests pass with full-equality comparison
- [ ] No behavioral coverage lost vs original partial-matching tests

---

## Task 10: Create multi-file golden dirs for snapshot tests (5 test cases)

Each `files.snapshot()` test generates multiple files. Each generated file becomes a separate golden `.kt` file.

**Golden files** under `typed/src/commonTest/kotlin/io/github/nomisrev/render/golden/client_snapshot/`:

| Sub-dir | Files | Test |
|---|---|---|
| `splitfiles/` | `Chat.kt`, `Models.kt`, `OpenAI.kt` | generateClient splits direct root children |
| `deepernesting/` | `FineTuning.kt`, `OpenAI.kt` | deeper nesting with sub-interfaces |
| `rootpathops/` | `Api.kt`, `Models.kt` | root-level operations + nested group |
| `pascalcamelcase/` | `FineTuning.kt`, `OpenAI.kt` | PascalCase/camelCase handling |
| `singleendpoint/` | `FineTuning.kt`, `OpenAI.kt` | single endpoint per group |

**Package handling:** Golden files use `package io.github.nomisrev.render.golden.client_snapshot.<subdir>` (matching directory). `readGoldenSnapshot()` normalizes this to `package io.github.nomisrev.api` during comparison.

**Acceptance criteria:**
- [ ] All snapshot golden files compile
- [ ] `readGoldenSnapshot()` produces output matching `files.snapshot()` format exactly
- [ ] All 5 snapshot tests pass

---

## Task 11: Remove original inline test specs from commonTest

After all golden-file specs pass:

**Files to delete:**
- `typed/src/commonTest/kotlin/io/github/nomisrev/render/ObjectRenderSpec.kt`
- `typed/src/commonTest/kotlin/io/github/nomisrev/render/EnumRenderSpec.kt`
- `typed/src/commonTest/kotlin/io/github/nomisrev/render/CollectionRenderSpec.kt`
- `typed/src/commonTest/kotlin/io/github/nomisrev/render/DiscriminatedObjectRenderSpec.kt`
- `typed/src/commonTest/kotlin/io/github/nomisrev/render/UnionRenderSpec.kt`
- `typed/src/commonTest/kotlin/io/github/nomisrev/render/ClientRenderSpec.kt`

**Files to move/adapt:**
- `typed/src/commonTest/kotlin/io/github/nomisrev/render/Verify.kt` → move to jvmAndNativeTest or adapt for golden-only usage
- `typed/src/commonTest/kotlin/io/github/nomisrev/render/StringDiff.kt` → keep in commonTest (used by golden specs too)

**Acceptance criteria:**
- [ ] `./gradlew :typed:allTests` passes
- [ ] No remaining inline expected strings in render test files
- [ ] `./gradlew :typed:jsTest` compiles (golden files compile, no file-reading tests on JS)
