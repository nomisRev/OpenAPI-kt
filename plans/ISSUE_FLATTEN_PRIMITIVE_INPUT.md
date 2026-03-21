# ISSUE: Configurable Flattening Of Primitive Input Schemas

## Problem

The generated client currently preserves scalar component schemas at the public input boundary,
even when the input is just a path, query, header, or cookie parameter.

Example from the GitHub client:

```kotlin
public fun alertNumber(alertNumber: AlertNumberWrite): AlertNumberPath
```

where the referenced schema is just:

```json
"alert-number": {
  "type": "integer",
  "description": "The security alert number.",
  "readOnly": true
}
```

and the generated wrapper is:

```kotlin
@JvmInline
@Serializable
public value class AlertNumberWrite(
  public val value: Long,
)
```

This forces call sites to write:

```kotlin
repos.owner("o").repo("r").codeScanning.alerts.alertNumber(AlertNumberWrite(123))
```

instead of:

```kotlin
repos.owner("o").repo("r").codeScanning.alerts.alertNumber(123)
```

## Why This Matters

1. Path/query/header/cookie parameters are transport inputs, not payload models.
2. For primitive-like inputs, the wrapper adds ceremony without adding much runtime value.
3. The generated API becomes noticeably less Kotlin-idiomatic for large specs.
4. Some wrappers come from read/write splitting rather than a meaningful domain distinction, so the
   exposed input type can look semantically wrong at the call site.
5. At the same time, some users may still prefer wrappers for stronger type safety, so this should
   be configurable rather than forced.

## Scope

This issue only affects the public signature of:

1. path navigation parameters
2. query parameters
3. header parameters
4. cookie parameters

It does **not** flatten:

1. request bodies
2. response models
3. enums
4. objects
5. unions

## Desired Outcome

Expose primitive Kotlin types directly when configured, while preserving the current wrapped
behavior by default.

### Current behavior

```kotlin
public fun alertNumber(alertNumber: AlertNumberWrite): AlertNumberPath
public suspend fun invoke(toolName: CodeScanningAnalysisToolName? = null): Response
```

### Flattened behavior

```kotlin
public fun alertNumber(alertNumber: Long): AlertNumberPath
public suspend fun invoke(toolName: String? = null): Response
```

The generated models such as `AlertNumberWrite` and `CodeScanningAnalysisToolName` should still
exist for request/response bodies and other non-input contexts. Only the public input boundary
changes.

## Proposed Configuration

Use an explicit render config strategy instead of a bare boolean so the feature can evolve later:

```kotlin
data class RenderConfig(
  ...,
  val primitiveInputStrategy: PrimitiveInputStrategy = PrimitiveInputStrategy.Flatten,
)

enum class PrimitiveInputStrategy {
  Wrapped,
  Flatten,
}
```

## Eligible Types For Flattening

When `primitiveInputStrategy == Flatten`, flatten only input models that ultimately map to a single
scalar Kotlin value:

1. direct primitive schemas
2. direct `date`, `date-time`, `uuid`, and binary scalar schemas
3. referenced scalar schemas that generate as inline value classes around one scalar value

Do not flatten:

1. enums
2. collections
3. objects
4. unions
5. discriminated objects

## Proposed Implementation Strategy

1. Decide the public input type at parameter-render time based on `RenderConfig.primitiveInputStrategy`.
2. Decide the stored accumulated path type the same way, so path navigation classes do not keep the
   wrapper when the public API does not expose it.
3. Preserve existing wire rendering behavior:
   - flattened primitive inputs should go straight to the wire value
   - wrapped inputs continue using existing wrapper-aware logic
4. Keep wrappers in generated model files even when flattening is enabled.
5. Add targeted collision tests so flattening does not silently create ambiguous overloads.

## Affected Files

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/RenderConfig.kt`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRenderer.kt`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt`

## Acceptance Criteria

1. The default configuration preserves the current wrapped input behavior.
2. With `primitiveInputStrategy = Flatten`, eligible path/query/header/cookie inputs use the
   underlying Kotlin scalar type in generated method signatures.
3. Request-body and response-body model generation is unchanged.
4. Enums, objects, and unions remain unflattened.
5. The GitHub alert path example generates `alertNumber(alertNumber: Long)` when flattening is
   enabled.
6. `./gradlew :renderer:jvmTest` passes.
7. `./gradlew :github:compileKotlin` passes in the integration tests.
