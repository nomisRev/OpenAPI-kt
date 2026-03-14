# Phase 2: Introduce `PathSegment`

## Goal
Add the `PathSegment` sealed interface that represents a single segment of a URL path. This is a purely additive change — no existing code is modified.

## Changes

### 1. New file: `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/PathSegment.kt`

```kotlin
package io.github.nomisrev.openapi

sealed interface PathSegment {
    val name: String
    data class Literal(override val name: String) : PathSegment
    data class Parameter(override val name: String, val type: Model) : PathSegment
}
```

### 2. Add parsing utility

In the same file (or as a companion/top-level function):

```kotlin
/**
 * Parses a raw OpenAPI path template into an ordered list of segments.
 * e.g. "/repos/{owner}/{repo}/collaborators" ->
 *   [Literal("repos"), Parameter("owner", ...), Parameter("repo", ...), Literal("collaborators")]
 *
 * Parameter types are resolved from the provided map of path parameter names to their Models.
 * Missing parameters default to String.
 */
fun parsePathSegments(
    path: String,
    pathParamTypes: Map<String, Model>,
): List<PathSegment> =
    path.split("/")
        .filter { it.isNotEmpty() }
        .map { segment ->
            if (segment.startsWith("{") && segment.endsWith("}")) {
                val paramName = segment.removeSurrounding("{", "}")
                val type = pathParamTypes[paramName]
                    ?: Model.Primitive.String(default = null, description = null, maxLength = null, nullable = false, pattern = null)
                PathSegment.Parameter(paramName, type)
            } else {
                PathSegment.Literal(segment)
            }
        }
```

## Tests

### New file: `typed/src/commonTest/kotlin/io/github/nomisrev/PathSegmentSpec.kt`

Test cases:
- `/` -> empty list
- `/pets` -> `[Literal("pets")]`
- `/pets/{petId}` -> `[Literal("pets"), Parameter("petId", String)]`
- `/repos/{owner}/{repo}/collaborators` -> `[Literal("repos"), Parameter("owner", String), Parameter("repo", String), Literal("collaborators")]`
- `/chat/completions` -> `[Literal("chat"), Literal("completions")]`
- Consecutive parameters: `/{owner}/{repo}` -> `[Parameter("owner", String), Parameter("repo", String)]`
- Parameter type resolution: provide `mapOf("petId" to Model.Primitive.Int(...))` and verify it's picked up
- Missing parameter type: param not in map defaults to String

## Verification
- Project compiles: `./gradlew :typed:build`
- New tests pass: `./gradlew :typed:allTests`
- No existing code modified — zero regression risk
