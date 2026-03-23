# Phase 5: Implementation Rendering (Accept/Content-Type Headers)

## Goal

Update the generated method implementations to correctly set `Accept` headers for response content type methods and use `RequestType.contentType` for request body content types.

## Deliverables

1. Modified `ImplRenderer.kt` — Accept header per method, RequestType content type usage, response dispatch for multi-interface

## Tasks

### 5.1 Set Accept Header in Content-Type Methods

**File:** `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt`

When generating the implementation body for a content-type-specific method (e.g., `json()`, `sarifJson()`), add:

```kotlin
header(HttpHeaders.Accept, ContentType.Application.Json.toString())
// or for custom types:
header(HttpHeaders.Accept, "application/sarif+json")
```

Use `toContentTypeCodeBlock()` (already exists at line 434-459) for well-known types. For custom types, use the string representation.

**Integration point:** The implementation body is built in `buildOperationBody()`. This function needs a new parameter for the target content type when in multi-CT mode.

### 5.2 Use RequestType for Content-Type Header

When the `RequestType` enum is used, the implementation body must use:

```kotlin
contentType(requestType.contentType)
```

instead of the current hardcoded content type from the selected body variant.

**Current code path:** `addSetLikeBodyCode()` at `ImplRenderer.kt:482-507` calls `toContentTypeCodeBlock()` with the body's content type. When a `RequestType` parameter exists, replace this with `requestType.contentType`.

### 5.3 Response Dispatch for Multi-Interface

**Current dispatch pattern (single Response):**
```kotlin
when (response.status.value) {
    200 -> Response.Ok(response.body())
    404 -> Response.NotFound(response.body())
    else -> ...
}
```

**Multi-CT dispatch pattern:**
Each content-type method generates its own dispatch. The key differences:
- Success cases reference the CT-specific sealed interface: `JsonResponse.Ok(response.body())`
- Shared error cases reference the operation-level classes: `Forbidden(response.body())`
- No-content cases: `NotModified` (no body deserialization)

```kotlin
// In json() method:
when (response.status.value) {
    200 -> JsonResponse.Ok(response.body())
    304 -> NotModified
    401 -> Unauthorized(response.body())
    403 -> Forbidden(response.body())
    else -> ...
}
```

For error statuses with multiple CTs, the dispatch needs to check the response's `Content-Type` header:

```kotlin
// When 400 has both JSON (BasicError) and SCIM (ScimError):
400 -> {
    val ct = response.contentType()
    when {
        ct?.match(ContentType("application", "scim+json")) == true ->
            ScimJsonBadRequest(response.body())
        else -> JsonBadRequest(response.body())  // Default to JSON
    }
}
```

### 5.4 Content-Type Dispatch for Multi-CT Errors

When an error status has multiple content types, the generated code must:
1. Read the response's `Content-Type` header
2. Match against known error content types
3. Deserialize with the matching schema
4. Fall back to the preferred (JSON) schema if no match

This is a new pattern — currently the renderer doesn't inspect response content types at runtime.

**Helper to generate:**
```kotlin
private fun buildMultiCTErrorDispatch(
    errorContentTypes: Map<ContentType, Pair<String, TypeName>>,  // CT → (caseName, bodyType)
    preferredCaseName: String,  // Fallback case
): CodeBlock
```

## Key Code Locations

- `buildOperationBody()`: `ImplRenderer.kt` (generates the `when (response.status.value)` block)
- `addSetLikeBodyCode()`: `ImplRenderer.kt:482-507` (sets content type for request body)
- `toContentTypeCodeBlock()`: `ImplRenderer.kt:434-459`
- `isJsonLike()`: `ImplRenderer.kt:587-589`
- `preferredModel()`: `ImplRenderer.kt:640-644`

## Acceptance Criteria

- [ ] Content-type methods set correct `Accept` header
- [ ] `RequestType` enum is used for `Content-Type` header when present
- [ ] Success cases reference CT-specific sealed interface (`JsonResponse.Ok`)
- [ ] Shared error cases reference operation-level classes (`Forbidden`)
- [ ] Multi-CT error statuses dispatch on response `Content-Type` header
- [ ] No-content statuses generate no body deserialization
- [ ] `./gradlew :renderer:jvmTest` passes

## Verification

```bash
./gradlew :renderer:jvmTest
```

Inspect generated implementation code for correct Accept headers and response dispatch patterns.
