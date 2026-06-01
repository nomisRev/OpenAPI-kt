# Query/header/cookie union inputs are emitted as Kotlin objects instead of OpenAPI wire values

## Summary
The renderer correctly generates rich Kotlin types for complex input parameters, including unions such as `oneOf(string, array<string>)`.
However, request emission still passes those values directly to Ktor `parameter(...)`, `header(...)`, and `cookie(...)` calls using only enum/date/uuid special-casing.

That means wrapper types and array-style unions are not serialized to their OpenAPI wire format.

## Affected files
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt`

## Current behavior
Parameter emission is generated through `addRequestConfigCode(...)` and the wire helpers:
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt:647-709`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt:931-948`

`wireValueExpr(...)` / `wireItExpr()` only special-case:
- enums
- date/time
- uuid

Everything else falls back to the raw Kotlin value (`paramName` / `it`).

That is insufficient for generated union parameter types and other structured parameter models.

## Reproduction
Observed in the generated GitHub client:
- `integration-tests/github/src/main/kotlin/github/io/github/api/Advisories.kt:63-81`
- `integration-tests/github/src/main/kotlin/github/io/github/api/Enterprises.kt:1461-1474`

Examples from `parser/src/commonTest/resources/specs/github.json`:
- `GET /advisories` query params `cwes` and `affects`
- `GET /enterprises/{enterprise}/dependabot/alerts` query param `has`

These parameters are `oneOf` string/array shapes in the spec, but the generated client does:
```kotlin
cwes?.let { parameter("cwes", it) }
affects?.let { parameter("affects", it) }
has?.let { parameter("has", it) }
```

## Why this is a problem
Ktor does not use kotlinx serialization for `parameter(...)`.
Passing generated wrapper objects directly does not produce OpenAPI-compliant query strings.

Consequences:
- union wrapper instances leak into transport encoding
- array-style query parameters cannot be emitted correctly
- the generated public API looks strongly typed but the transport layer is incorrect

## Expected behavior
The renderer should generate explicit wire serialization for non-scalar input parameters used in:
- query parameters
- headers
- cookies
- form values where applicable

That serialization should respect OpenAPI parameter style/explode semantics, or at minimum produce the currently supported canonical wire format for generated union input models.

## Acceptance criteria
- Generated clients no longer pass arbitrary union wrapper objects directly to `parameter(...)`, `header(...)`, or `cookie(...)`.
- Union inputs used as request parameters are converted to explicit wire values before emission.
- Focused renderer tests cover `oneOf(string, array<string>)` and similar input parameter cases.
- GitHub-style cases such as `cwes`, `affects`, and `has` serialize correctly.
