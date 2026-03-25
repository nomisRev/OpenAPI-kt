# Phase 2: Fix ISSUE_RESPONSE_TYPE — Body Naming Collision

## Goal

Ensure inline response body types never collide with the `Response` sealed wrapper name. Inline schemas get distinct names like `OkBody`, `ServiceUnavailableBody` instead of `Response`.

## Deliverables

1. Modified `OperationInlineModelScope.kt` — content-type-aware owner resolution
2. Modified `ClientRendererResponses.kt` — body type naming in sealed cases
3. Tests validating no `Ok(value: Response) : Response` patterns

## Tasks

### 2.1 Body Type Name Generation

Add to `ContentTypeStrategy.kt` (from Phase 1):

```kotlin
fun bodyTypeName(
    statusCode: HttpStatusCode,
    contentType: ContentType?,
    hasMultipleStatuses: Boolean,
    hasMultipleContentTypes: Boolean,
): String {
    val statusPrefix = if (hasMultipleStatuses) statusCode.toCaseName() else ""
    val ctSuffix = if (hasMultipleContentTypes && contentType != null)
        contentTypeToIdentifier(contentType) else ""
    return "${statusPrefix}${ctSuffix}Body"
}
```

| Scenario | statusPrefix | ctSuffix | Result |
|----------|-------------|----------|--------|
| Single status, single CT | "" | "" | `Body` |
| Single status, multi CT | "" | "Json" | `JsonBody` |
| Multi status, single CT | "Ok" | "" | `OkBody` |
| Multi status, multi CT | "Ok" | "Json" | `OkJsonBody` |

### 2.2 Update OperationInlineModelScope

**File:** `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/OperationInlineModelScope.kt`

**Current code (line 67-69):**
```kotlin
val responseOwnerClassName =
    if (returns.isSingleDirectModelResponse()) methodClassName
    else methodClassName.nestedClass("Response")
```

**Problem:** When the response owner is `Response`, inline models placed there get names like `GetContents.Response.Response` — collision.

**Change:** When an inline response body would be named `Response`, rename it using `bodyTypeName()`:
- For single-status operations: owner stays on `methodClassName`, body named `Body`
- For multi-status operations: owner stays on `Response`, but the inline model is named `OkBody`/`ServiceUnavailableBody` instead of `Response`

The key fix is in `buildSealedResponseCaseTypeSpec` (Phase 2.3) where we use the body type name.

### 2.3 Update buildSealedResponseCaseTypeSpec

**File:** `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererResponses.kt`

**Current code (line 145):**
```kotlin
val typeName = model.toTypeName(config)
```

**Problem:** When `model` is an inline model, `toTypeName(config)` can resolve to `Response` (the wrapper's own name).

**Fix:** After computing `typeName`, check if it would collide with the enclosing `Response` class name. If so, the inline model needs to be emitted under a different name (e.g., `OkBody`).

This requires:
1. Detecting when `typeName.simpleName == "Response"` AND the type is inline (not a `$ref`)
2. Using `bodyTypeName(statusCode, contentType, hasMultipleStatuses=true, hasMultipleContentTypes=false)` to generate the replacement name
3. Adding the renamed type to the inline model scope's type remaps

### 2.4 Handle Single-Status Inline Response

**Current code (line 47):**
```kotlin
model.toInlineOperationTypeSpecOrNull(
    config = config,
    ownerClassName = methodClassName,
    nameOverride = "Response",
    ...
)
```

When a single-status operation has an inline response, it's currently named `Response`. This is fine for single-CT (no collision possible). But for multi-CT operations (Phase 3), we'll need `Body` or `{CT}Body`. Prepare the hook here but don't change behavior yet.

## Key Code Locations

- `buildSealedResponseCaseTypeSpec`: `ClientRendererResponses.kt:131-161`
- `buildResponseTypeSpec`: `ClientRendererResponses.kt:33-59`
- `responseOwnerClassName`: `OperationInlineModelScope.kt:67-69`
- `Model.isRouteInlineModel()`: check if model is inline vs referenced

## Acceptance Criteria

- [ ] `Ok(value: Response) : Response` pattern no longer appears in generated code
- [ ] GitHub `/repos/{owner}/{repo}/contents/{path}` generates `Ok(value: OkBody)` not `Ok(value: Response)`
- [ ] GitHub `/repos/{owner}/{repo}/code-scanning/analyses/{analysis_id}` generates `ServiceUnavailable(value: ServiceUnavailableBody)`
- [ ] Single-status operations with inline response still work (named `Response` or `Body`)
- [ ] `./gradlew :renderer:jvmTest` passes
- [ ] `./gradlew :github:compileKotlin` passes

## Verification

```bash
./gradlew :renderer:jvmTest
./gradlew :github:compileKotlin
```

Inspect generated GitHub client for the two known collision sites.
