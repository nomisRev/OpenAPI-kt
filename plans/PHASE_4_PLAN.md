# Phase 4: Inline `Endpoint` into `toRoute()`

## Goal
Eliminate the `Endpoint` class. Fold its work directly into the route-building pipeline. After this phase, `OpenAPI.endpoints()` no longer exists — routes are built directly from `OpenAPI.paths`.

## Changes

### 1. Replace `OpenAPI.endpoints()` + `Endpoint.toRoute()` — `typed/.../routes/Route.kt`

Replace the two-step `endpoints().map { it.toRoute() }` with a single function:

```kotlin
context(ctx: Registry)
suspend fun OpenAPI.toRoutes(): List<Route> =
    paths.entries.flatMap { (path, pathItem) ->
        val pathParams = pathItem.parameters
        listOfNotNull(
            pathItem.get?.let { toRoute(path, HttpMethod.Get, it.withParams(pathParams)) },
            pathItem.put?.let { toRoute(path, HttpMethod.Put, it.withParams(pathParams)) },
            pathItem.post?.let { toRoute(path, HttpMethod.Post, it.withParams(pathParams)) },
            pathItem.delete?.let { toRoute(path, HttpMethod.Delete, it.withParams(pathParams)) },
            pathItem.head?.let { toRoute(path, HttpMethod.Head, it.withParams(pathParams)) },
            pathItem.options?.let { toRoute(path, HttpMethod.Options, it.withParams(pathParams)) },
            pathItem.trace?.let { toRoute(path, HttpMethod("Trace"), it.withParams(pathParams)) },
            pathItem.patch?.let { toRoute(path, HttpMethod.Patch, it.withParams(pathParams)) },
        )
    }

private fun Operation.withParams(pathParams: List<ReferenceOr<Parameter>>): Operation =
    copy(parameters = pathParams + parameters)
```

### 2. New `toRoute()` function — `typed/.../routes/Route.kt`

This absorbs the work from `Endpoint.toRoute()`, `Endpoint.context()`, and the path parsing:

```kotlin
context(ctx: Registry)
private suspend fun toRoute(path: String, method: HttpMethod, operation: Operation): Route {
    val params = resolveParameters(path, method, operation)
    val pathParamTypes = params
        .filter { it.input == Parameter.Input.Path }
        .associate { it.name to it.type }
    val segments = parsePathSegments(path, pathParamTypes)
    return Route(
        operationId = operation.operationId ?: generateSyntheticOperationId(path, method),
        summary = operation.summary,
        path = path,
        segments = segments,
        method = method,
        parameters = params,
        body = resolveBodies(path, method, operation, segments),
        returns = resolveReturns(path, method, operation, segments),
        extensions = operation.extensions,
        deprecated = operation.deprecated,
    )
}
```

### 3. Update `Parameters.kt`

Functions currently using `context(endpoint: Endpoint)` change to take path/method/operation directly:

- `Endpoint.parameters()` -> `resolveParameters(path, method, operation)` (or similar)
- `Endpoint.withMissingPathParameters()` -> takes path param names extracted from the path string
- `Endpoint.pathParameters` usage -> compute locally from path string
- `NamingContext` creation: `endpoint.context(RouteParam(...))` -> `NamingContext(Path(pathSegments), listOf(RouteParam(...)))` where `pathSegments` is computed from the path string (static parts only, to maintain current NamingContext behavior until Phase 5)

### 4. Update `RequestBody.kt`

Functions using `context(endpoint: Endpoint)`:
- `Endpoint.bodies()` -> standalone function taking path, method, operation, operationId
- `endpoint.context(NamingContext.RouteBody(...))` -> compute NamingContext directly
- `endpoint.operationId` references -> pass operationId as parameter
- `endpoint.path`, `endpoint.method` references -> pass as parameters

Touch points (lines 16, 19, 35, 47, 49, 62, 64, 72, 74):
- All `context(endpoint: Endpoint, ...)` become `context(ctx: Registry)` with explicit parameters

### 5. Update `Return.kt`

Same pattern as RequestBody.kt:
- `Endpoint.returns()` -> standalone function taking path, method, operation
- `endpoint.context(NamingContext.Response(endpoint.operationId))` -> compute directly
- Touch points (lines 13, 22, 26, 32)

### 6. Delete `Endpoints.kt`

Remove the entire file. All its functionality is now in `Route.kt` and related files.

### 7. Update `OpenAPI.toApiModel()` — `typed/.../routes/Route.kt`

```kotlin
suspend fun OpenAPI.toApiModel(): ApiModel =
    Registry(this).use { registry ->
        val globalServers = servers.normalizeForClientGeneration()
        val routes = with(registry) { toRoutes() }  // was: endpoints().map { it.toRoute() }
        // ... rest unchanged
    }
```

## Tests

### Delete `EndpointSpec.kt`

The `Endpoint` class no longer exists. Its test coverage migrates:

### Update/New: `RouteSpec.kt` or `PathSegmentSpec.kt`

Migrate relevant test cases:
- Path segment parsing (was `Endpoint.pathSegments()`) -> already covered by `PathSegmentSpec` from Phase 2
- Path parameter extraction (was `Endpoint.pathParameters()`) -> covered by `parsePathSegments` tests
- Synthetic operationId generation (was `Endpoint.operationId`) -> keep temporarily as a helper function test until Phase 5 removes it
- NamingContext creation (was `Endpoint.context()`) -> test that Route construction produces correct NamingContext through integration tests

### Run integration tests
- Run the full typed module test suite to verify no regressions in route/parameter/body/return resolution

## Verification
- `Endpoints.kt` deleted, `EndpointSpec.kt` deleted
- No references to `Endpoint` class anywhere in typed module
- Project compiles: `./gradlew :typed:build`
- All tests pass: `./gradlew :typed:allTests`
