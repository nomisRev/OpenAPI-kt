# ISSUE: Renderer Module Review Findings

## Scope

This note records the concrete issues found during a review of the `renderer` module, with a focus
on code duplication, dead code, and maintainability problems that already affect correctness or
make future changes risky.

## Validation

Running `:renderer:jvmTest` currently fails during `compileTestKotlinJvm` with:

```text
renderer/src/jvmTest/resources/kotlinTestData/client/enum-wire-tostring/Jobs.kt:38:51
Unresolved reference 'Body'
```

That failure corresponds to Finding 2 below.

## Findings

### P1: Inline form-body types are referenced without being emitted

#### Problem

`inlineBodyTypeSpec()` only emits a nested `Body` type for `SetBody`-backed request bodies:

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererRequestBody.kt`

At the same time, `toInvokeParameterSpecs()` still resolves form and multipart parameter types
directly from `formData.type.toTypeName(config)`:

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererOperations.kt`

For inline enums or inline objects nested inside form or multipart bodies, the generated API can
therefore reference `Method.Body.NestedType` even though no such `Body` type was emitted.

#### Evidence

The checked-in fixture below is already uncompilable:

- `renderer/src/jvmTest/resources/kotlinTestData/client/enum-wire-tostring/Jobs.kt`

It contains:

```kotlin
public suspend operator fun invoke(mode: Post.Body.Mode) {
```

but the surrounding `Post` class does not declare `Body`.

#### Impact

1. Generated client code can fail to compile.
2. The golden fixtures can encode invalid output and break `:renderer:jvmTest`.
3. Inline body handling is currently inconsistent across request-body shapes.

#### Acceptance Criteria

1. Inline nested types referenced from form and multipart request bodies are always emitted.
2. Generated fixtures for inline form-body enums or objects compile successfully.
3. `./gradlew :renderer:jvmTest` passes.

### P2: Test harness masks generated file collisions

#### Problem

The combined render path can emit the same file name twice when both model generation and client
generation need serialization helper output:

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/Generate.kt`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRenderer.kt`

Both paths can generate `AttemptDeserialize.kt`. The test harness then builds a map keyed only by
`file.name` after forcing `modelPackage == apiPackage`:

- `renderer/src/jvmTest/kotlin/io/github/nomisrev/render/TestBalloonDsl.kt`

That means one generated file silently overwrites the other instead of failing fast.

#### Impact

1. Golden tests can pass while hiding duplicate-file bugs.
2. Package-level helper collisions are not surfaced during combined rendering.
3. Future helper generation changes can regress silently.

#### Acceptance Criteria

1. The test harness fails when two generated files share the same package and file name.
2. Combined render tests validate the full generated output set without silent overwrites.

### P3: Operation-body assembly is triplicated

#### Problem

`buildSealedOperationBody`, `buildMultiContentTypeSealedOperationBody`, and
`buildDirectOperationBody` all duplicate the same request setup:

1. compute `httpMethodName`
2. compute `pathLiteral`
3. compute `needsRequestConfig`
4. compute `bodyMayBeNull`
5. emit the `addRequestConfigCode(...)` block

Only the response tail differs.

#### Affected Code

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt`

#### Impact

1. Request-generation fixes have to be patched in three places.
2. Divergence risk is high in one of the most complex files in the module.
3. The duplication is already large enough to obscure the actual branch differences.

#### Desired Outcome

Extract the shared request setup into one helper that returns either a prepared `response` binding
or a reusable `CodeBlock`, leaving only the response-shape-specific dispatch in the three callers.

### P3: `anyOf` unique-key logic is duplicated in two places

#### Problem

The object-only `anyOf` unique-key eligibility check is implemented twice:

1. in `JsonObjectImport.kt`
2. in `UnionRendererSerialization.kt`

One copy decides whether `jsonObject` must be imported, while the other decides whether the
optimized unique-key deserializer path should be generated.

#### Affected Code

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/JsonObjectImport.kt`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRendererSerialization.kt`

#### Impact

1. The two call sites can drift and make inconsistent decisions.
2. Future fixes to `anyOf` dispatch rules may update one copy but not the other.
3. This duplication is especially risky because it couples generation shape and imports.

#### Desired Outcome

Extract a single shared predicate or analysis result for `anyOf` unique-key dispatch eligibility and
reuse it from both places.

### P3: Fixed-server strategy carries dead state

#### Problem

`ServerStrategy.SingleFixed` stores a `url`, but the downstream rendering path does not use it:

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ServerStrategy.kt`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/FactoryRenderer.kt`

`FactoryRenderer.buildSecondaryConstructors()` currently treats `SingleFixed` exactly like
`NoServers`.

#### Impact

1. The sealed strategy suggests behaviour that does not exist.
2. The stored `url` is dead state today.
3. Future readers can reasonably assume fixed servers are baked into generated constructors when
   they are not.

#### Desired Outcome

Either:

1. actually use the fixed server URL in generated constructors, or
2. collapse `SingleFixed` into a simpler strategy that does not carry unused data.

## Summary

The review found one active correctness bug, one test-harness blind spot, and three structural
maintainability issues. The highest-priority item is the inline form-body bug because it already
breaks `:renderer:jvmTest`.
