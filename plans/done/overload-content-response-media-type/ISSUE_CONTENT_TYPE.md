# ISSUE: Multiple Content Types Result in Signature Clashes and Incomplete APIs

## Problem

The renderer currently handles multiple content types per operation in limited ways:

1. **Request bodies**: When multiple content types are defined, separate `invoke()` overloads are generated (ClientRendererOperations.kt:148-162). However, when these content types share the same schema type, this creates **signature clashes** that fail compilation.

2. **Response bodies**: Only a single "preferred" content type is selected via `preferredModel()` (ClientRendererResponses.kt:17-21), which picks `application/json` first, then falls back to the first available. This means **alternative content types are completely ignored** in the generated API, even when they have different schemas.

These behaviors make the generated API incomplete and sometimes uncompilable.

## Minimal Reproducers

### Reproducer 1: Request Body Signature Clash

```json
{
  "requestBody": {
    "required": true,
    "content": {
      "application/json": {
        "schema": { "type": "string" }
      },
      "application/xml": {
        "schema": { "type": "string" }
      }
    }
  }
}
```

Current broken generation:
```kotlin
public suspend operator fun invoke(body: String): Response  // application/json
public suspend operator fun invoke(body: String): Response  // application/xml - CLASH!
```

### Reproducer 2: Response Body Content Type Loss

```json
{
  "responses": {
    "200": {
      "content": {
        "application/json": {
          "schema": { "$ref": "#/components/schemas/Repository" }
        },
        "application/vnd.github.v3.star+json": {
          "schema": { "$ref": "#/components/schemas/StarredRepository" }
        }
      }
    }
  }
}
```

Current incomplete generation:
```kotlin
public suspend operator fun invoke(): List<Repository>
// application/vnd.github.v3.star+json is completely ignored!
```

The caller has no way to request the `StarredRepository` schema.

### Reproducer 3: Multiple Statuses with Different Content Types

```json
{
  "responses": {
    "200": {
      "content": {
        "application/json": { "schema": { "$ref": "#/components/schemas/SuccessJson" } },
        "application/xml": { "schema": { "$ref": "#/components/schemas/SuccessXml" } }
      }
    },
    "400": {
      "content": {
        "application/json": { "schema": { "$ref": "#/components/schemas/ErrorJson" } },
        "application/scim+json": { "schema": { "$ref": "#/components/schemas/ErrorScim" } }
      }
    }
  }
}
```

Current incomplete generation:
```kotlin
public sealed interface Response {
  public data class Ok(public val value: SuccessJson) : Response    // XML ignored
  public data class BadRequest(public val value: ErrorJson) : Response  // SCIM ignored
}

public suspend operator fun invoke(): Response
// No way to request XML success or SCIM error!
```

## Real-World Impact

### GitHub API Examples

The GitHub OpenAPI spec contains multiple operations affected by this issue:

1. **Request bodies**: Operations with `multipart/form-data` + other content types currently work because multipart expands to different parameter signatures. However, any operation with multiple content types sharing the same schema type would fail compilation.

2. **Response bodies** (content type loss):
   - `/user/starred`: Only returns `List<Repository>`, loses `List<StarredRepository>` from `application/vnd.github.v3.star+json`
   - `/repos/{owner}/{repo}/code-scanning/analyses/{analysis_id}`: Only returns `CodeScanningAnalysis`, loses SARIF document from `application/sarif+json`
   - `/repos/{owner}/{repo}/contents/{path}`: Only returns JSON union, loses `ContentTree` from `application/vnd.github.object`

3. **Multiple statuses with different content types**:
   - `bad_request` component response (400) used by 62 operations has both `application/json` (BasicError) and `application/scim+json` (ScimError) with different schemas
   - Currently only the JSON error is accessible in generated code

### Cloudflare API Examples

61 AI endpoint operations return:
- Success (200): `application/json` (error info) + media types like `image/png`, `audio/mpeg`, `text/event-stream`
- Currently only JSON is accessible; generated images/audio are unreachable

## Root Cause

### Request Body Overload Generation

`ClientRendererOperations.kt:148-162` generates separate overloads for each content type variant:

```kotlin
val bodyVariants = context.route.body?.variants().orEmpty()
if (bodyVariants.size > 1) {
    bodyVariants.forEach { variant ->
        addFunction(
            context.route.toInvokeFunSpec(
                // ...
                selectedBody = variant.body,
            )
        )
    }
    return
}
```

This works when body types differ (e.g., `String` vs `JsonObject`), but fails when they're the same (e.g., both `String`).

**No clash detection or disambiguation mechanism exists** (unlike `OverloadedBody.distinctCaseSignatures()` which uses `@JvmName` for oneOf/anyOf cases).

### Response Body Preferred Model Selection

`ClientRendererResponses.kt:17-21` collapses multiple content types to one:

```kotlin
private fun Route.ReturnType.preferredModel(): Model? {
    if (types.isEmpty()) return null
    val jsonEntry = types.entries.firstOrNull { ContentType.Application.Json.match(it.key) }
    return jsonEntry?.value ?: types.values.first()
}
```

This function is called during response wrapper generation and completely discards non-preferred content types.

## Why This Matters

1. **Compilation failures**: Request body signature clashes make the generated code uncompilable
2. **Incomplete APIs**: Response content types with different schemas are unreachable
3. **Type safety loss**: Callers cannot express their `Accept` header preference at compile time
4. **Spec contract violation**: The OpenAPI spec declares multiple content types, but the generated client only exposes one

## Desired Outcome

The generated API should:

1. **Never generate signature clashes** - use enums or method names to disambiguate
2. **Expose all content types** - every content type in the spec should be reachable
3. **Be type-safe** - different schemas should have different types at compile time
4. **Follow the spec exactly** - no assumptions or simplifications that lose information

### Request Body Strategy

#### Case 1: Different Body Types → Separate Overloads

When content types have different schema types, generate separate `invoke()` overloads:

```kotlin
// POST /api/data
// application/json → JsonData
// text/plain → String

public suspend operator fun invoke(body: JsonData): Response
public suspend operator fun invoke(body: String): Response
```

**Current behavior already works** - no changes needed.

#### Case 2: Same Body Type → RequestType Enum

When content types have the **same** schema type, generate a nested `RequestType` enum:

```kotlin
// POST /markdown/raw
// text/plain → String
// text/x-markdown → String

public enum class RequestType(public val contentType: ContentType) {
  TextPlain(ContentType.Text.Plain),
  TextXMarkdown(ContentType("text", "x-markdown"))
}

public suspend operator fun invoke(
  body: String,
  requestType: RequestType  // NO DEFAULT - explicit required
): Response = client.post("/markdown/raw") {
  contentType(requestType.contentType)
  setBody(body)
}
```

**Key aspects:**
- Enum parameter is **required** (no default) - forces explicit choice when ambiguous
- Only generated when signature clash would occur
- Compatible with multipart/form-urlencoded (only generates enum if those also clash)

### Response Body Strategy

#### Case 3: Single Status, Different Content Types → Separate Methods

When one status code has multiple content types with **different** schemas:

```kotlin
// GET /user/starred
// 200 OK:
//   application/json → List<Repository>
//   application/vnd.github.v3.star+json → List<StarredRepository>

public class GetStarred {
  public sealed interface JsonResponse {
    public data class Ok(public val value: List<Repository>) : JsonResponse
  }
  
  public sealed interface VndGithubV3StarJsonResponse {
    public data class Ok(public val value: List<StarredRepository>) : VndGithubV3StarJsonResponse
  }
  
  public suspend fun json(): JsonResponse = 
    client.get("/user/starred") {
      header(HttpHeaders.Accept, ContentType.Application.Json.toString())
    }.body()
  
  public suspend fun vndGithubV3StarJson(): VndGithubV3StarJsonResponse = 
    client.get("/user/starred") {
      header(HttpHeaders.Accept, "application/vnd.github.v3.star+json")
    }.body()
}
```

**Key aspects:**
- Separate sealed interface per content type method
- Method names derived from content type (json, xml, sarifJson, vndGithubV3StarJson)
- Each method sets appropriate Accept header
- Type-safe: different return types for different schemas

#### Case 4: Same Schema, Multiple Content Types → Separate Methods (No Enum)

When one status code has multiple content types with the **same** schema, still generate separate methods:

```kotlin
// GET /data
// 200 OK:
//   application/json → String
//   text/plain → String

public class GetData {
  public suspend fun json(): String =
    client.get("/data") {
      header(HttpHeaders.Accept, ContentType.Application.Json.toString())
    }.body()

  public suspend fun textPlain(): String =
    client.get("/data") {
      header(HttpHeaders.Accept, ContentType.Text.Plain.toString())
    }.body()
}
```

No ResponseType enum — consistent with the different-schema case. Always separate methods.

#### Case 5: Multiple Statuses, Different Content Types → Shared Error Cases

**Key insight:** The `Accept` header controls the SUCCESS content type. Error responses are sent by the server in whatever format it chooses. Therefore:
- Sealed interfaces are generated per SUCCESS content type only
- Error cases implement ALL success sealed interfaces (multi-inheritance in Kotlin)
- Error statuses with multiple content types generate one case per error content type

```kotlin
// POST /endpoint
// 200 OK:
//   application/json → SuccessJson
//   application/xml → SuccessXml
// 400 Bad Request:
//   application/json → BasicError
//   application/scim+json → ScimError

public class PostEndpoint {
  public sealed interface JsonResponse {
    public data class Ok(public val value: SuccessJson) : JsonResponse
  }

  public sealed interface XmlResponse {
    public data class Ok(public val value: SuccessXml) : XmlResponse
  }

  // Error cases shared across ALL success interfaces — server picks the format
  public data class JsonBadRequest(public val value: BasicError) : JsonResponse, XmlResponse
  public data class ScimJsonBadRequest(public val value: ScimError) : JsonResponse, XmlResponse

  public suspend fun json(): JsonResponse = ...
  public suspend fun xml(): XmlResponse = ...
}
```

**Key aspects (Shared Error Cases):**
- Sealed interfaces are driven by SUCCESS (2xx) content types only
- Error data classes implement ALL success sealed interfaces via multi-inheritance
- When an error status has a single content type → one case (e.g., `BadRequest`)
- When an error status has multiple content types → one case per error CT (e.g., `JsonBadRequest`, `ScimJsonBadRequest`)
- No-content statuses (204, 304) → `data object` implementing all interfaces
- No "fallback to preferredModel" — error schemas are used directly as defined in the spec
- The HttpClient dispatch inspects the response `Content-Type` header to pick the right error case

## Naming Conventions

### Content Type → Method/Type Names

**Algorithm:**
1. Parse content type: `{type}/{subtype}[+suffix]`
2. Convert to PascalCase for type names, camelCase for method names
3. Strip parameters (e.g., `; charset=utf-8`)
4. Handle special characters: dots (`.`), hyphens (`-`), plus (`+`)

**Examples:**
- `application/json` → `Json` / `json()`
- `application/xml` → `Xml` / `xml()`
- `text/plain` → `TextPlain` / `textPlain()`
- `application/sarif+json` → `SarifJson` / `sarifJson()`
- `application/vnd.github.v3.star+json` → `VndGithubV3StarJson` / `vndGithubV3StarJson()`
- `application/vnd.github.object` → `VndGithubObject` / `vndGithubObject()`
- `image/png` → `ImagePng` / `imagePng()`
- `text/event-stream` → `TextEventStream` / `textEventStream()`
- `application/x-www-form-urlencoded` → `XWwwFormUrlencoded` / `xWwwFormUrlencoded()`

**Collision handling:** If two content types map to same name, append numeric suffix: `Json`, `Json2`

### Enum Names

- Request bodies: `RequestType` (nested inside operation class)
- Response bodies: No enum — always separate named methods

### Response Type Names (per ISSUE_RESPONSE_TYPE.md)

**Sealed interface names:**
- Single content type: `Response`
- Multiple content types: `JsonResponse`, `XmlResponse`, `SarifJsonResponse`, etc.

**Status case names:**
- `200` → `Ok`
- `201` → `Created`
- `204` → `NoContent`
- `400` → `BadRequest`
- `404` → `NotFound`
- `422` → `UnprocessableEntity`
- `503` → `ServiceUnavailable`

**Body type names:**
- Single status, single content type: `Body`
- Single status, multiple content types (different schemas): `JsonBody`, `XmlBody`
- Multiple statuses, single content type: `OkBody`, `NotFoundBody`
- Multiple statuses, multiple content types: `OkJsonBody`, `OkXmlBody`, `BadRequestJsonBody`

## Detection Algorithm

### Request Body Signature Clash Detection

```
For operation with request body:
  variants = body.variants()  // List<Variant(contentType, body)>
  
  If variants.size == 1:
    Generate single invoke() with that body type
    
  Else:
    // Group by Kotlin signature (parameter list)
    signatures = variants.groupBy { it.body.toKotlinSignature() }
    
    If all groups have size == 1:
      // No clashes - generate separate invoke() overload per variant
      For each variant:
        Generate invoke(body: BodyType): Response
      
    Else:
      // Has clashes - need RequestType enum for clashing variants
      For each signature group:
        If group.size > 1:
          // Multiple content types with same signature
          Generate RequestType enum with these content types
          Generate invoke(body: BodyType, requestType: RequestType): Response
        Else:
          // Unique signature - can use overload
          Generate invoke(body: BodyType): Response
```

**Signature comparison:**
- Multipart/FormUrlEncoded: expand to parameter list, compare as set
- SetBody/OverloadedBody: compare TypeName
- Ignore parameter names, only compare types and arity

### Response Body Content Type Detection

```
For operation:
  // Collect content types across SUCCESS (2xx) status codes only
  successContentTypes = operation.responses
    .filter { it.code in 200..299 }
    .flatMap { it.content.keys }
    .distinct()

  If successContentTypes.size <= 1:
    // Single content type - use current behavior
    Generate Response sealed interface (if multiple statuses)
    Generate single invoke() operator method

  Else:
    // Multiple success content types - generate separate methods
    For each successCT in successContentTypes:
      Generate ${ContentType}Response sealed interface
      Generate ${contentType}() method

      // Add success cases (2xx) specific to this content type
      For each successStatus in operation.responses where code in 200..299:
        model = successStatus.content[successCT]
        If model != null:
          Add data class case nested in sealed interface
        Else if no-content status:
          Add data object case nested in sealed interface

    // Add error cases as SHARED across all success interfaces
    For each errorStatus in operation.responses where code NOT in 200..299:
      errorContentTypes = errorStatus.content

      If errorContentTypes.isEmpty():
        Generate data object {StatusName} implementing ALL success interfaces

      Else if errorContentTypes.size == 1:
        Generate data class {StatusName}(val value: {Model})
          implementing ALL success interfaces

      Else:
        // Multiple error content types → one case per error CT
        For each (errorCT, errorModel) in errorContentTypes:
          Generate data class {CT}{StatusName}(val value: {Model})
            implementing ALL success interfaces
```

**Key principle:** Sealed interfaces are driven by success content types. Error cases are shared via multi-interface implementation because the server decides the error format independently of the client's Accept header.

## Relationship to ISSUE_RESPONSE_TYPE.md

This issue depends on `ISSUE_RESPONSE_TYPE.md` being resolved:

1. **Body type naming**: When inline schemas are used with multiple content types, body types must follow the naming rules: `OkJsonBody`, `OkXmlBody`, etc.

2. **No `Response` collision**: The sealed response wrapper must be named according to content type (e.g., `JsonResponse`, `XmlResponse`), not just `Response`, to avoid collisions with body types.

3. **Composability**: The two solutions must work together:
   - Multiple status codes → sealed interface (ISSUE_RESPONSE_TYPE.md)
   - Multiple content types → separate sealed interfaces per content type (this issue)
   - Combined → `JsonResponse`, `XmlResponse` sealed interfaces with `Ok`, `BadRequest` cases containing `OkJsonBody`, `BadRequestJsonBody`, etc.

## Relationship to ISSUE_MEDIA_TYPE.md

`ISSUE_MEDIA_TYPE.md` is superseded by this document. The key differences:

1. **ISSUE_MEDIA_TYPE.md** proposed:
   - Single `Response` type for all content types
   - Accept enum parameter when schemas are the same
   - Separate methods when schemas differ (but still single Response type)

2. **This document** (ISSUE_CONTENT_TYPE.md) adds:
   - Separate sealed interfaces per content type method (`JsonResponse`, `XmlResponse`)
   - Cartesian product handling for multiple statuses with different content types
   - Request body signature clash detection and RequestType enum
   - Complete fallback strategy using preferredModel when status doesn't define a content type

## Implementation Strategy

### Phase 1: Request Body Clash Detection

**Files:**
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ContentTypeStrategy.kt` (new)
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererRequestBody.kt`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererOperations.kt`

**Steps:**
1. Create signature comparison utilities:
   - `Route.Body.toKotlinSignature(): String` - normalized signature representation
   - `List<Route.Bodies.Variant>.detectSignatureClashes(): Map<String, List<Variant>>`
2. Generate `RequestType` enum when clashes detected
3. Update `invoke()` generation to use enum parameter for clashing cases
4. Keep separate overloads for non-clashing cases

### Phase 2: Response Body Content Type Support

**Files:**
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ContentTypeNaming.kt` (new)
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererResponses.kt`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRenderer.kt`

**Steps:**
1. Add content type grouping logic:
   - `Route.Returns.allContentTypes(): Set<ContentType>`
   - `Route.Returns.schemasPerContentType(): Map<ContentType, Set<TypeName>>`
   - `Route.Returns.needsSeparateMethods(): Boolean`
2. Remove dependency on `preferredModel()` for method generation
3. Generate separate sealed interfaces per content type
4. Generate content-type-specific methods (json(), xml(), etc.)

### Phase 3: Implementation Rendering

**Files:**
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt`

**Steps:**
1. Update impl generation to set `Accept` header based on method
2. Update request body impl to set `Content-Type` from RequestType enum
3. Generate correct deserialization code per content type method

### Phase 4: Body Type Naming Integration

**Files:**
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/OperationInlineModelScope.kt`

**Steps:**
1. Extend inline model scope to track content type context
2. Generate body types with content-type suffix: `OkJsonBody`, `OkXmlBody`
3. Ensure deduplication works across content types
4. Handle cartesian product: map (status, content type) → body type name

### Phase 5: Testing

**Files:**
- `renderer/src/jvmTest/kotlin/io/github/nomisrev/render/ClientSpec.kt`
- New test cases for each scenario

**Steps:**
1. Add test cases for all 5 response body scenarios
2. Add test cases for request body clash detection
3. Test cartesian product with preferredModel fallback
4. Test content type naming edge cases
5. Update GitHub API integration tests

## Affected Files

### Parser Module
- **No changes needed** - already parses all content types correctly

### Typed Module
- **No changes needed** - already stores all content type variants
- `Route.Bodies` contains `types: Map<ContentType, Body>` ✓
- `Route.ReturnType` contains `types: Map<ContentType, Model>` ✓

### Renderer Module

**New files:**
- `ContentTypeStrategy.kt` - Clash detection, grouping, and strategy selection
- `ContentTypeNaming.kt` - Content type to method/type name conversion

**Modified files:**
- `ClientRendererOperations.kt:146-163` - Update request body overload generation
- `ClientRendererRequestBody.kt` - Add signature comparison, generate RequestType enum
- `ClientRendererResponses.kt` - Replace preferredModel() with content-type-aware logic
- `ClientRenderer.kt` - Add separate sealed interface generation per content type
- `ImplRenderer.kt` - Generate Accept/Content-Type headers per method
- `OperationInlineModelScope.kt` - Handle content-type-suffixed body types

## Acceptance Criteria

### Request Bodies

1. ✓ Operations with different request body types generate separate overloads (current behavior)
2. ✓ Operations with same request body type across multiple content types generate RequestType enum
3. ✓ RequestType enum parameter is required (no default)
4. ✓ Multipart/FormUrlEncoded only gets RequestType enum if signatures truly clash
5. ✓ Generated code compiles without signature clashes

### Response Bodies

6. ✓ Operations with single content type use current behavior (no enum, no separate methods)
7. ✓ Operations with multiple content types (different schemas) generate separate methods
8. ✓ Each content-type method generates its own sealed interface (JsonResponse, XmlResponse, etc.)
9. ✓ Each content-type method sets appropriate Accept header
10. ✓ Error cases implement ALL success sealed interfaces (shared via multi-inheritance)
11. ✓ Error statuses with multiple content types generate one case per error CT
12. ✓ Operations with multiple content types (same schema) generate separate named methods (no enum)

### Naming

13. ✓ Content types map to correct method/type names (json, xml, sarifJson, vndGithubV3StarJson, etc.)
14. ✓ Body types follow ISSUE_RESPONSE_TYPE.md naming (OkJsonBody, BadRequestXmlBody, etc.)
15. ✓ No name collisions between sealed interfaces and body types

### Integration

16. ✓ `/user/starred` can return both `List<Repository>` (json) and `List<StarredRepository>` (vndGithubV3StarJson)
17. ✓ `/repos/{owner}/{repo}/code-scanning/analyses/{analysis_id}` can request SARIF (sarifJson)
18. ✓ `/repos/{owner}/{repo}/contents/{path}` can request both JSON union and GitHub object (vndGithubObject)
19. ✓ GitHub's `bad_request` (400) with JSON and SCIM both accessible
20. ✓ `./gradlew :renderer:jvmTest` passes
21. ✓ `./gradlew :github:compileKotlin` passes in integration tests

## Migration Impact

### Breaking Changes

**Request bodies:**
- Operations with multiple content types of the same type change from potentially clashing overloads to a single method with `RequestType` enum parameter
- Example: `POST /markdown/raw` with `text/plain` and `text/x-markdown` (both String)
  - Before: Two `invoke(body: String)` overloads (compilation error)
  - After: Single `invoke(body: String, requestType: RequestType)` (explicit choice required)

**Response bodies:**
- Operations with multiple content types now expose separate methods instead of single method
- Example: `GET /user/starred`
  - Before: `api.user.starred()` returns `List<Repository>` only
  - After: `api.user.starred.json()` returns `JsonResponse` with `List<Repository>`, `api.user.starred.vndGithubV3StarJson()` returns `VndGithubV3StarJsonResponse` with `List<StarredRepository>`
- Return types change from `Response` to `JsonResponse`, `XmlResponse`, etc.
- Operation classes become containers for content-type methods instead of having single `invoke()` operator

### Non-Breaking Changes

- Operations with single content type → no changes
- Operations with different request body types → no changes (already have overloads)
- Status code handling → no changes (sealed interfaces still work the same way)

### Rollout Strategy

1. Implement request body strategy first (lower risk, fixes compilation errors)
2. Add response body strategy second (higher complexity, enables new capabilities)
3. Update integration tests incrementally
4. Document migration guide with before/after examples
5. Consider feature flag to allow gradual adoption

## Open Questions

### Resolved

1. **Content type parameter stripping**: Should `application/json; charset=utf-8` → `Json` or `JsonCharsetUtf8`?
   - **Decision**: Strip parameters, use base type only for naming

2. **Wildcard Accept headers**: Should generated methods support `*/*` or `application/*`?
   - **Decision**: No - be explicit, let users make raw requests if needed

3. **Content negotiation validation**: Should we validate server response content type matches requested?
   - **Decision**: No - Ktor client handles this, out of scope for code generation

4. **Default content type when multiple exist**: Should there be a "default" method without suffix?
   - **Decision**: No - all methods are explicit. No invoke() operator when multiple content types with different schemas exist.

### Resolved (during review)

1. **Empty response bodies (204 No Content)**: How do content types interact with status codes that have no body?
   - **Decision**: `data object` implementing all response interfaces. HttpClient implementation handles mapping. No-content statuses appear in ALL success sealed interfaces.

5. **Response body same schema, multiple content types**: Should we use a ResponseType enum?
   - **Decision**: No. Always generate separate named methods. Consistent and simple.

6. **How to handle error statuses in multi-CT operations**: Cartesian product or shared?
   - **Decision**: Shared error cases via Kotlin sealed interface multi-inheritance. Error data classes implement ALL success sealed interfaces. Error content types generate separate cases per error CT (e.g., `JsonBadRequest`, `ScimJsonBadRequest`).

### Outstanding

2. **Binary content types**: Should `image/png`, `audio/mpeg` generate methods like `imagePng()`, `audioMpeg()`?
   - **Proposed**: Yes - follow same naming convention, return `ByteArray` or appropriate type.

3. **Custom content types**: How to handle non-standard content types like `application/vnd.company.custom-v2+json`?
   - **Proposed**: Follow naming algorithm: `VndCompanyCustomV2Json` / `vndCompanyCustomV2Json()`, truncate if excessively long.
