# Plan: New KMP-compatible Generation Module without KotlinPoet

This plan outlines how to design and incrementally implement a new code generation module that does not rely on KotlinPoet and is Kotlin Multiplatform (KMP) compatible. Binary compatibility and maintaining KotlinPoet’s API are explicitly out of scope.

Goals:
- Replace KotlinPoet usage with a small, KMP-capable code emission toolkit.
- Keep design modular: parsing, typed modeling, and generation remain decoupled.
- Provide an incremental path so we can ship value early and migrate consumers gradually.

Non-goals:
- No attempt to mimic KotlinPoet’s API.
- No preservation of current KotlinPoet-based public APIs or binary compatibility.

## 1. High-level Architecture

- Parser (existing): `parser` module remains the source of OpenAPI models.
- Typed model (existing): `typed` module stays the single source for typed ADTs and transformations.
- New generator (new): `generation-kmp` (working name `codegen`) module provides:
  - Model-to-IR transformation (Typed → Generation IR)
  - Kotlin source emission toolkit (pure string/AST-like builders) with formatting
  - Target-specific emitters (common Kotlin, platform hints if needed)
  - File system abstraction for writing files (KMP friendly)
- Gradle plugin (existing): `plugin` integrates with new generator via a clean, KotlinPoet-free API.

## 2. New Module Layout (KMP)

Create a new module `codegen` (or rename existing `generation` after migration). KMP targets:
- commonMain: core IR, emitters, formatting, naming utilities, path abstractions.
- jvmMain: file I/O, integration with Gradle plugin, JVM-specific niceties (optional pretty formatter invoking ktlint/spotless if present).
- jsMain/nativeMain (optional later): output file adapters for those targets.

Directory structure proposal:
- codegen/
  - build.gradle.kts (multiplatform)
  - src/commonMain/kotlin/
    - api/ (public APIs)
    - ir/ (generation IR types)
    - emit/ (emitters for declarations)
    - format/ (minimal formatter)
    - util/ (naming, escaping, keywords, imports)
  - src/jvmMain/kotlin/
    - io/ (file system writers)
    - plugin/ (thin integration helpers)
  - src/commonTest/kotlin/ (golden tests, property tests)

## 3. Generation IR (Independent of KotlinPoet)

Purpose: Minimal, expressive Kotlin Declaration IR that maps well to source without over-engineering.

Core IR types:
- KtFile(name: String, pkg: String?, imports: List<KtImport>, declarations: List<KtDeclaration>)
- KtImport(qualifiedName: String, alias: String? = null)
- KtDeclaration sealed hierarchy:
  - KtTypeAlias(name, type)
  - KtClass(name, kind: Class|Interface|Object|Enum, modifiers, typeParams, primaryCtor, properties, functions, companion, superTypes, annotations, kdoc)
  - KtFunction(name, params, returnType, typeParams, modifiers, annotations, kdoc, body)
  - KtProperty(name, type, mutable, initializer, modifiers, annotations, kdoc)
  - KtEnumEntry(name, args, annotations, kdoc)
- Support types: KtType (simple, nullable, generic, function), KtParam(name, type, default), KtModifier, KtAnnotation, KtKDoc, KtExpr (stringly-typed or minimal AST), KtVisibility.

IR constraints:
- Keep IR minimal—only what the project uses for OpenAPI models and APIs.
- Allow raw code blocks for bodies/expressions to reduce complexity initially.
- Defer complex PSI-like features unless needed by tests.

## 4. Emission & Formatting

- Emitter design: Pure functions that take IR and produce strings (or Appendable) line-by-line.
- Indentation: small helper managing indentation levels.
- Wrapping & line length: configurable but simple; avoid perfect formatting early.
- Imports: name resolver that collects required imports, avoids conflicts with kotlin.* and current pkg.
- Keywords & escaping: utility to escape identifiers and backtick if needed.
- Annotations & KDoc: simple renderers with proper wrapping.
- Optional JVM formatter hook: if present, run ktfmt/ktlint via command or embedded formatter only on JVM builds; skip on non-JVM.

## 5. Public API of New Generator

Common API (commonMain):
- interface OpenApiCodegen {
    fun generate(api: io.github.nomisrev.openapi.Root or Typed Model, options: CodegenOptions): List<GeneratedFile>
  }
- data class GeneratedFile(path: String, content: String)
- data class CodegenOptions(targets, packageRules, namingRules, clientOptions, modelOptions, enumOptions, nullableAsOptional, etc.)

JVM integration helper:
- fun writeTo(outputDir: Path, files: List<GeneratedFile>, overwrite: Boolean)

No exposure of KotlinPoet types. The plugin will depend on this new API.

## 6. Migration Strategy (Incremental)

Milestone 1: Bootstrap (no KotlinPoet)
- Create `codegen` KMP module with IR, basic emitter, and tests using small toy declarations.
- Implement minimal name resolver, import writer, indentation, escaping.
- Add golden tests verifying stable output for a set of canonical declarations.

Milestone 2: Model Types (typed → IR)
- Implement transformers from `typed` ADTs to IR for:
  - primitives, nullable, arrays, maps
  - data classes with required/optional fields, defaults
  - enums (string/int) with serial names annotations
- Add tests mirroring typed module tests with golden files.

Milestone 3: API Surfaces
- Generate client interfaces, request/response models, error types.
- Support suspend functions, inline classes/value classes where applicable, and Ktor/HTTP abstractions (stringly fallback allowed first).
- Add tests for simple OpenAPI specs (petstore subset), compare to expected source text or compile in a JVM test.

Milestone 4: Annotations & Serialization
- kotlinx.serialization annotations (Serializable, SerialName, Contextual), required converters for oneOf/anyOf/allOf.
- Nullability/required/optional mapping rules finalized.
- Expand tests: schema edge cases, discriminator-based unions.

Milestone 5: Gradle Plugin Integration
- Add plugin option to select new generator (flag: useNewCodegen=true) default off.
- Implement writer on JVM to emit files to buildDir/generated.
- E2E test in plugin module invoking new codegen for a sample OpenAPI and compiling generated sources.

Milestone 6: Parity & Switch Default
- Reach functional parity for needed features; document any intentional differences.
- Switch plugin default to new codegen, keep old KotlinPoet path behind a temporary flag.

Milestone 7: Cleanup
- Remove KotlinPoet-specific utilities from repository once consumers are migrated.
- Rename module `generation` → `codegen` (or merge), update docs.

## 7. Design Details & Decisions

Type System Mapping:
- Prefer fully qualified Kotlin types in IR, with import resolver simplifying.
- Model nullable vs optional: optional parameters with defaults vs nullable types per OpenAPI semantics.
- Collections: List/Set/Map with platform-agnostic types; rely on kotlinx.collections if needed later.

Enums:
- Support both sealed interfaces with objects and enum classes; start with enum class when possible.
- Preserve original names via @SerialName where needed.

Unions (oneOf/anyOf/allOf):
- Start with sealed interfaces and data classes implementing them.
- For allOf, compose properties; for anyOf/oneOf, hierarchy + serializers.

Defaults & Constraints:
- Represent default values as string expressions in IR initially.
- Constraints (minLength, pattern, etc.) documented and optionally emitted as comments or validation stubs; enforcement can be a later milestone.

KDoc & Metadata:
- Render externalDocs/description into KDoc.
- Keep widths reasonable; don’t over-wrap early.

File Layout & Packages:
- Deterministic file names and package paths from naming rules.
- Models in a models package; APIs in api package; shared errors in error package.

Performance & Memory:
- Use Appendable-based emitters; avoid deep recursion in large specs.
- Post-process imports last for fewer passes.

## 8. Testing Strategy

- Unit tests for emitters (IR → text) with golden files.
- Unit tests for transformers (typed → IR)
- E2E tests using parser+typed+codegen on sample specs (petstore/openai) and compile generated code on JVM CI.
- Property tests for stable formatting (idempotence) where feasible.

## 9. Gradle & Tooling

- New codegen module as KMP, minimal deps (kotlin stdlib, kotlinx-serialization core for annotations only as compile-only?).
- Optional JVM test-time dependency on ktlint/ktfmt for formatting checks; not required on other targets.
- Plugin: add flag and wire new generator via service loader or direct dep.

## 10. Risk & Mitigations

- Formatting quality: keep simple first, allow optional JVM formatter hook.
- Union/serializer complexity: stage it (start with simple sealed hierarchies), defer custom serializers to later.
- Import conflicts: design a name resolver with backticks and aliasing as fallback.

## 11. Deliverables per Milestone

- M1: codegen module skeleton, IR types, basic emitter, 5–10 golden tests.
- M2: model generation (data classes, enums), tests mirroring typed tests.
- M3: API interface generation and sample client, E2E compile test.
- M4: serialization annotations + union handling tests.
- M5: Gradle plugin flag + writer, E2E plugin test.
- M6: Default switch + documentation.
- M7: Remove KotlinPoet remnants, rename modules as needed.

## 12. Migration Notes for Contributors

- Do not add new KotlinPoet usage.
- New features must target `codegen` IR + emitter.
- When encountering features not supported in IR, extend IR minimally with real needs.

---
This plan is intended to be implemented incrementally. Each milestone is independently valuable and testable, enabling progressive adoption without a big-bang rewrite.