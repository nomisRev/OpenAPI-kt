# Phase 5: Remove `operationId` and update `NamingContext`

## Goal
Remove `operationId` from `Route` and refactor `NamingContext` to use `path: List<PathSegment>` + `method: HttpMethod` instead of `operationId` for uniqueness.

## Changes

### 1. Update `NamingContext.Head.Path` — `typed/.../NamingContext.kt`

Change from value class with `List<String>` to data class with segments + method:

```kotlin
// Before:
@JvmInline
value class Path(val parts: List<String>) : Head

// After:
data class Path(val segments: List<PathSegment>, val method: HttpMethod) : Head
```

Note: `Path` can no longer be a `@JvmInline value class` since it now has two fields.

Update companion functions:
```kotlin
companion object {
    fun reference(name: String, context: SchemaContext) = NamingContext(Reference(name, context), emptyList())
    fun path(segments: List<PathSegment>, method: HttpMethod) = NamingContext(Path(segments, method), emptyList())
}
```

### 2. Simplify `NamingContext.Nested` types — `typed/.../NamingContext.kt`

```kotlin
// Before:
data class RouteParam(val name: String, val operationId: String) : Nested
data class RouteBody(val name: String, val operationId: String) : Nested
data class Response(val operationId: String) : Nested

// After:
data class RouteParam(val name: String) : Nested
data object RouteBody : Nested
data object Response : Nested
```

### 3. Remove `operationId` from `Route` — `typed/.../routes/Route.kt`

```kotlin
// Before:
data class Route(
    val operationId: String,
    val summary: String?,
    val path: String,
    val segments: List<PathSegment>,
    val method: HttpMethod,
    ...
)

// After:
data class Route(
    val summary: String?,
    val path: String,              // still here, removed in Phase 6
    val segments: List<PathSegment>,
    val method: HttpMethod,
    ...
)
```

### 4. Update `toRoute()` — `typed/.../routes/Route.kt`

Remove `operationId` from Route construction. Remove `generateSyntheticOperationId()` helper.

### 5. Update `Parameters.kt`

NamingContext creation changes:

```kotlin
// Before:
parameter.toRouteInput(context(RouteParam(parameter.name(), operationId)))

// After (context is now computed from segments + method):
parameter.toRouteInput(NamingContext.path(segments, method).nest(RouteParam(parameter.name())))
```

Where `segments` and `method` are available from the enclosing `toRoute()` scope.

### 6. Update `RequestBody.kt`

All three body functions create NamingContext:

```kotlin
// Before (3 occurrences):
endpoint.context(NamingContext.RouteBody("body", endpoint.operationId))

// After:
NamingContext.path(segments, method).nest(NamingContext.RouteBody)
```

Also update error message at line 35 — replace `endpoint.operationId` with path+method description:
```kotlin
"Required request body for ${method.value} $path has no schema..."
```

### 7. Update `Return.kt`

```kotlin
// Before:
schema.toModel(endpoint.context(NamingContext.Response(endpoint.operationId)), SchemaContext.Read)

// After:
schema.toModel(NamingContext.path(segments, method).nest(NamingContext.Response), SchemaContext.Read)
```

### 8. Update `ApiModel.toString()` — `typed/.../routes/Route.kt`

```kotlin
// Before:
routes.joinToString { it.operationId }

// After:
routes.joinToString { "${it.method.value} ${it.path}" }
```

### 9. Update `Root.kt` comment

Remove the operationId reference from the documentation comment (lines 6-14).

## Serialization consideration

`NamingContext` is `@Serializable`. Changes to `Path`, `RouteParam`, `RouteBody`, `Response` affect serialization. `PathSegment` needs to be `@Serializable` as well (add in this phase if not already). `HttpMethod` may need a custom serializer.

Check if NamingContext serialization is used at runtime or only in tests/snapshots. If snapshots exist, they need updating.

## Tests

### Update all tests referencing old NamingContext shapes

- Any test creating `NamingContext.path(listOf("chat", "completions"))` must now provide segments + method
- Any test using `RouteParam(name, operationId)` must use `RouteParam(name)`
- Any test using `RouteBody(name, operationId)` must use `RouteBody`
- Any test using `Response(operationId)` must use `Response`

### New tests: NamingContext uniqueness

Verify that NamingContext produces unique names for:
- Same path, different methods: `GET /pets` vs `POST /pets`
- Same method, different paths: `GET /pets` vs `GET /users`
- Paths differing only by parameter name (if applicable)

### Regression tests
- Run full test suite to verify no naming collisions introduced

## Verification
- No references to `operationId` in typed module (except parser module which is unchanged)
- No references to `generateSyntheticOperationId`
- Project compiles: `./gradlew :typed:build`
- All tests pass: `./gradlew :typed:allTests`
