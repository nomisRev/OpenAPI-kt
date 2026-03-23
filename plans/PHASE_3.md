# Phase 3: Multi-Content-Type Response Generation

## Goal

When an operation has multiple success content types, generate separate named methods per content type, each with its own sealed response interface. Error/no-content cases are shared via multi-interface implementation.

## Deliverables

1. Modified `ClientRendererResponses.kt` — multi-interface generation
2. Modified `ClientRendererOperations.kt` — named methods instead of `invoke()`
3. Modified `OperationInlineModelScope.kt` — multi-CT body naming

## Tasks

### 3.1 Multi-Content-Type Response Type Generation

**File:** `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererResponses.kt`

Add new function `buildMultiContentTypeResponseSpecs()` that returns:
- A list of sealed interface TypeSpecs (one per success content type): `JsonResponse`, `SarifJsonResponse`, etc.
- A list of shared case TypeSpecs (error/no-content cases implementing all interfaces)

**Structure for each success content type:**
```kotlin
sealed interface ${CT}Response {
    // Only success cases (2xx) are nested here
    data class Ok(val value: ${OkCTBody}) : ${CT}Response
}
```

**Shared cases (at operation class level):**
```kotlin
// No-content status → data object
data object NotModified : JsonResponse, SarifJsonResponse

// Single-CT error → single case
data class Forbidden(val value: BasicError) : JsonResponse, SarifJsonResponse

// Multi-CT error → one case per error CT
data class JsonBadRequest(val value: BasicError) : JsonResponse, SarifJsonResponse
data class ScimJsonBadRequest(val value: ScimError) : JsonResponse, SarifJsonResponse
```

**Implementation outline:**
```kotlin
fun Route.buildMultiContentTypeResponseSpecs(
    config: RenderConfig,
    methodClassName: ClassName,
    successContentTypes: List<ContentType>,
): MultiContentTypeResponseResult {
    val interfaceClassNames = successContentTypes.associateWith { ct ->
        val name = "${contentTypeToIdentifier(ct)}Response"
        methodClassName.nestedClass(name)
    }

    // Build sealed interfaces with success cases
    val sealedInterfaces = successContentTypes.map { ct ->
        buildContentTypeResponseInterface(config, methodClassName, ct, interfaceClassNames[ct]!!)
    }

    // Build shared error/no-content cases
    val sharedCases = buildSharedResponseCases(
        config, methodClassName, interfaceClassNames.values.toList()
    )

    return MultiContentTypeResponseResult(sealedInterfaces, sharedCases)
}
```

### 3.2 Success Case Generation Per Content Type

For each success status (2xx) and each success content type:
- Look up the model from `returnType.types[contentType]`
- If model exists: generate `data class {StatusName}(val value: {BodyType}) : {CT}Response`
- If no model (204/205): generate `data object {StatusName} : {CT}Response`

Body type naming uses `bodyTypeName()` from Phase 2 with `hasMultipleContentTypes = true`.

### 3.3 Shared Error Case Generation

For each error status (non-2xx, non-redirect without body):

1. Collect `errorContentTypes = returnType.types` for that status
2. If `errorContentTypes.isEmpty()` → `data object {StatusName}` implementing all success interfaces
3. If `errorContentTypes.size == 1` → `data class {StatusName}(val value: {Model})` implementing all success interfaces
4. If `errorContentTypes.size > 1` → one `data class {CT}{StatusName}(val value: {Model})` per error CT, each implementing all success interfaces

### 3.4 Update addOperationResponseType

**File:** `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererOperations.kt`

**Current code (line 140-144):**
```kotlin
private fun TypeSpec.Builder.addOperationResponseType(context: OperationTypeSpecContext) {
    if (!context.route.returns.isSingleUnitResponse() && !context.route.returns.isSingleDirectModelResponse()) {
        addType(context.route.buildResponseTypeSpec(...))
    }
}
```

**Change:** Check `contentTypeStrategy()` first:
- `SingleContentType` → current behavior (single `Response` sealed interface)
- `SeparateMethods` → call `buildMultiContentTypeResponseSpecs()`, add each sealed interface AND shared cases to the operation TypeSpec

### 3.5 Update addOperationInvokeFunctions for Named Methods

**File:** `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererOperations.kt`

**Current code (line 146):** Always generates `invoke()` operator.

**Change:** When `contentTypeStrategy()` is `SeparateMethods`:
- Generate one `suspend fun {ctMethodName}(...)` per success content type
- NOT an operator `invoke` — regular named function
- Return type: `{CT}Response` (the content-type-specific sealed interface)
- Parameters: same as current `invoke()` (path params, query params, body)

### 3.6 Update invokeReturnType

**File:** `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererResponses.kt`

**Current code (line 86-97):**
```kotlin
internal fun Route.invokeReturnType(...): TypeName =
    when {
        returns.isSingleUnitResponse() -> UNIT
        returns.isSingleDirectModelResponse() -> ...
        else -> methodClassName.nestedClass("Response")
    }
```

**Change:** For multi-CT, this function is called per content-type method with a content type parameter, returning `methodClassName.nestedClass("${CT}Response")` instead of `"Response"`.

## Key Code Locations

- `buildSealedResponseTypeSpec`: `ClientRendererResponses.kt:111-129`
- `addOperationResponseType`: `ClientRendererOperations.kt:140-144`
- `addOperationInvokeFunctions`: `ClientRendererOperations.kt:146-222`
- `toInvokeFunSpec`: `ClientRendererOperations.kt:226-291`
- `invokeReturnType`: `ClientRendererResponses.kt:86-97`
- `Route.Returns`: `typed/.../routes/Route.kt:202-209`

## Acceptance Criteria

- [ ] Operations with single CT → unchanged behavior (single `invoke()`)
- [ ] Operations with 2+ success CTs → separate `json()`, `xml()`, etc. methods
- [ ] Each method returns its own `{CT}Response` sealed interface
- [ ] Success cases nested inside their sealed interface
- [ ] Error cases implement ALL success interfaces
- [ ] Multi-CT error statuses generate one case per error CT (e.g., `JsonBadRequest`, `ScimJsonBadRequest`)
- [ ] No-content statuses → `data object` implementing all interfaces
- [ ] `./gradlew :renderer:jvmTest` passes

## Verification

```bash
./gradlew :renderer:jvmTest
```

Write a test with a spec that has 200 with two content types + 404 with one content type. Verify generated code structure matches the expected shape.
