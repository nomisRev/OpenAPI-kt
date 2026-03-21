# ISSUE: Flattening Of Primitive Input Schemas

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

Expose primitive Kotlin types directly when applicable

This is a breaking change to the generated public input API.

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

## Eligible Types For Flattening

Flatten input models that ultimately map to a single scalar Kotlin value:

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

1. Decide the public input type at parameter-render time from the schema shape, flattening eligible
   primitive-like inputs to their Kotlin scalar type.
2. Decide the stored accumulated path type the same way, so path navigation classes use the same
   flattened type as the public API.
3. Render flattened primitive inputs directly to the wire value at transport boundaries.
4. Keep wrappers in generated model files only for request/response bodies and other non-input
   contexts that still need them.
5. Add targeted collision tests so flattening does not silently create ambiguous overloads.

## Affected Files

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRenderer.kt`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt`

## Acceptance Criteria

1.  Eligible path/query/header/cookie inputs use the underlying Kotlin scalar type in generated method signatures.
2. Request-body and response-body model generation is unchanged.
3. Enums, objects, and unions remain unflattened.
4. The GitHub alert path example generates `alertNumber(alertNumber: Long)`.
5. `./gradlew :renderer:jvmTest` passes.
6. `./gradlew :github:compileKotlin` passes in the integration tests.
