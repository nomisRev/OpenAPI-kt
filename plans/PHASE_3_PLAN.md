# Phase 3: Add `segments` to `Endpoint` and `Route`

## Goal
Wire `PathSegment` through the existing pipeline. Both `Endpoint` and `Route` get a `segments: List<PathSegment>` property. This is additive — existing `path`, `pathSegments`, `pathParameters`, and `operationId` remain for now.

## Changes

### 1. Update `Endpoint` — `typed/.../routes/Endpoints.kt`

Add a `segments` property that produces the full ordered list of path segments with resolved types.

Since `OpenAPI.endpoints()` now has `context(ctx: Registry)`, we can resolve path parameter types at construction time. The resolution is simple: look up the parameter in `operation.parameters`, resolve its schema to a `Model`.

```kotlin
class Endpoint(val path: String, val method: HttpMethod, val operation: Operation) {
    // ... existing properties stay ...

    // New: full ordered segments with types
    // Type resolution happens later when Registry is available (see toRoute)
    val rawSegments: List<String> = path.split("/").filter { it.isNotEmpty() }
}
```

The actual `List<PathSegment>` (with resolved `Model` types) is computed in `toRoute()` where the Registry is available, since resolving parameter references requires the Registry.

### 2. Update `Route` — `typed/.../routes/Route.kt`

Add `segments` field to `Route` data class:

```kotlin
data class Route(
    val operationId: String,       // still here, removed in Phase 5
    val summary: String?,
    val path: String,              // still here, removed in Phase 6
    val segments: List<PathSegment>, // NEW
    val method: HttpMethod,
    val body: Bodies?,
    val parameters: List<Input>,
    val returns: Returns,
    val extensions: Map<String, JsonElement>,
    val deprecated: Boolean,
)
```

### 3. Update `Endpoint.toRoute()` — `typed/.../routes/Route.kt`

Compute segments during route construction using the Registry to resolve parameter types:

```kotlin
context(ctx: Registry)
suspend fun Endpoint.toRoute(): Route {
    val params = parameters()
    val pathParamTypes = params
        .filter { it.input == Parameter.Input.Path }
        .associate { it.name to it.type }
    return Route(
        operationId = operationId,
        summary = operation.summary,
        path = path,
        segments = parsePathSegments(path, pathParamTypes),
        method = method,
        parameters = params,
        body = bodies(),
        returns = returns(),
        extensions = operation.extensions,
        deprecated = operation.deprecated,
    )
}
```

Note: `parameters()` is called first so we have resolved path parameter types available for `parsePathSegments`.

## Tests

### Update `EndpointSpec.kt`

Add test cases for the new segments on Route (via a helper that calls `toRoute()`), or test `parsePathSegments` integration:

- Verify that `Route.segments` matches expected `List<PathSegment>` for various paths
- Verify parameter types are correctly resolved from the operation's parameter definitions
- Verify missing path parameters get default `String` type

### New integration-style test

Create a test that builds a Route from a minimal OpenAPI operation and verifies `route.segments` matches expected output for:
- Static-only paths
- Paths with parameters
- Mixed static and parameter paths
- Consecutive parameters

## Verification
- Project compiles: `./gradlew :typed:build`
- All tests pass: `./gradlew :typed:allTests`
- Existing behavior unchanged — `path`, `operationId`, `pathSegments`, `pathParameters` all still work
