# Implementation Plan: Complete Content Type Support + Response Type Naming Fix

## Task Tracker

| Phase | Description | Status |
|-------|-------------|--------|
| 1 | Infrastructure: ContentType naming & strategy detection | Pending |
| 2 | Fix ISSUE_RESPONSE_TYPE: body naming collision | Pending |
| 3 | Multi-content-type response generation | Pending |
| 4 | Request body signature clash detection | Pending |
| 5 | Implementation rendering (Accept/Content-Type headers) | Pending |
| 6 | Testing & integration validation | Pending |

---

## Context

The OpenAPI code generator has two intertwined issues:

1. **ISSUE_RESPONSE_TYPE**: Inline response schemas collide with the `Response` wrapper name. E.g., `Ok(value: Response) : Response` instead of `Ok(value: OkBody) : Response`.
2. **ISSUE_CONTENT_TYPE**: Multiple content types per operation cause signature clashes (requests) and information loss (responses). Only one "preferred" content type is exposed.

These are solved together because the body naming strategy must account for content-type suffixes, and the response wrapper naming must not be `Response` when multiple content-type interfaces exist.

## Design Decisions

- **No-content statuses** (204, 304): `data object` implementing all response interfaces. HttpClient implementation handles mapping.
- **Sealed interfaces = success content types only**: `JsonResponse`, `VndGithubV3StarJsonResponse`, etc. are driven by 2xx content types. Errors are independent of the Accept header.
- **Error cases cover ALL error content types**: Since the server decides the error format, each sealed interface must include cases for every error content type variant. E.g., if 400 has both `application/json` (BasicError) and `application/scim+json` (ScimError), both `JsonBadRequest` and `ScimJsonBadRequest` appear in every sealed interface.
- **Shared error cases**: Error cases implement ALL response sealed interfaces (multi-inheritance) since they're the same regardless of which success content type was requested.
- **Accept header = success content type**: Methods are named after the success content type. Error responses use their own schemas.
- **No ResponseType enum**: When multiple content types have the same schema, still generate separate named methods. Consistent and simple.
- **Binary content types**: Supported (image/png, audio/mpeg) — return ByteArray.

## Generated API Shape

### Single content type (no changes)

```kotlin
// Single status
suspend operator fun invoke(): User

// Multiple statuses
sealed interface Response {
  data class Ok(val value: User) : Response
  data class NotFound(val value: BasicError) : Response
}
suspend operator fun invoke(): Response
```

### Multiple success content types → separate methods

```kotlin
// GET /user/starred (200: json + vnd.github, 304, 401, 403)
class GetStarred {
  sealed interface JsonResponse {
    data class Ok(val value: List<Repository>) : JsonResponse
  }
  sealed interface VndGithubV3StarJsonResponse {
    data class Ok(val value: List<StarredRepository>) : VndGithubV3StarJsonResponse
  }
  // Shared: error/no-content cases implement ALL success interfaces
  data object NotModified : JsonResponse, VndGithubV3StarJsonResponse
  data class Unauthorized(val value: BasicError) : JsonResponse, VndGithubV3StarJsonResponse
  data class Forbidden(val value: BasicError) : JsonResponse, VndGithubV3StarJsonResponse

  suspend fun json(): JsonResponse = ...
  suspend fun vndGithubV3StarJson(): VndGithubV3StarJsonResponse = ...
}
```

### Multiple success content types, same schema → still separate methods

```kotlin
class GetData {
  suspend fun json(): String = ...
  suspend fun textPlain(): String = ...
}
```

### Error status with multiple content types → all error variants in every interface

```kotlin
// 200: application/json → SuccessJson, application/xml → SuccessXml
// 400: application/json → BasicError, application/scim+json → ScimError
class PostEndpoint {
  sealed interface JsonResponse {
    data class Ok(val value: SuccessJson) : JsonResponse
  }
  sealed interface XmlResponse {
    data class Ok(val value: SuccessXml) : XmlResponse
  }
  // Error cases shared across ALL success interfaces — server picks the format
  data class JsonBadRequest(val value: BasicError) : JsonResponse, XmlResponse
  data class ScimJsonBadRequest(val value: ScimError) : JsonResponse, XmlResponse

  suspend fun json(): JsonResponse = ...
  suspend fun xml(): XmlResponse = ...
}
```

### Request body signature clash → RequestType enum

```kotlin
// POST /markdown/raw (text/plain + text/x-markdown, both String)
class PostMarkdownRaw {
  enum class RequestType(val contentType: ContentType) {
    TextPlain(ContentType.Text.Plain),
    TextXMarkdown(ContentType("text", "x-markdown"))
  }
  suspend operator fun invoke(body: String, requestType: RequestType): Response = ...
}
```

### Composition: request enum + response methods

```kotlin
class PostEndpoint {
  enum class RequestType(...) { ... }
  sealed interface JsonResponse { ... }
  sealed interface XmlResponse { ... }

  suspend fun json(body: String, requestType: RequestType): JsonResponse = ...
  suspend fun xml(body: String, requestType: RequestType): XmlResponse = ...
}
```

## Naming Conventions

### Body type naming (ISSUE_RESPONSE_TYPE fix)

| Context | Name Pattern | Example |
|---------|-------------|---------|
| Single status, single CT | `Body` | `Body` |
| Single status, multi CT | `{CT}Body` | `JsonBody`, `SarifJsonBody` |
| Multi status, single CT | `{Status}Body` | `OkBody`, `NotFoundBody` |
| Multi status, multi CT | `{Status}{CT}Body` | `OkJsonBody`, `OkSarifJsonBody` |

`Response` is NEVER used as a body type name. Reserved for the sealed wrapper (or `{CT}Response` when multiple CTs).

### Error case naming

| Error CTs | Case Name Pattern | Example |
|-----------|------------------|---------|
| Single error CT | `{Status}` | `BadRequest` |
| Multiple error CTs | `{CT}{Status}` | `JsonBadRequest`, `ScimJsonBadRequest` |

Error cases live at the operation class level (not nested in any sealed interface) and implement ALL success sealed interfaces.

### Content Type → Identifier

Algorithm: strip `application/` for common types, PascalCase subtype, handle `+suffix`.

| Content Type | Type/Method |
|---|---|
| `application/json` | `Json` / `json()` |
| `application/xml` | `Xml` / `xml()` |
| `text/plain` | `TextPlain` / `textPlain()` |
| `application/sarif+json` | `SarifJson` / `sarifJson()` |
| `application/vnd.github.v3.star+json` | `VndGithubV3StarJson` / `vndGithubV3StarJson()` |
| `image/png` | `ImagePng` / `imagePng()` |
| `text/event-stream` | `TextEventStream` / `textEventStream()` |

Collision handling: append numeric suffix (`Json`, `Json2`).

## Detection Algorithms

### Response strategy selection

```
successContentTypes = union of content types across 2xx statuses only
if successContentTypes.size <= 1 → SingleContentType (current behavior, single invoke())
else → SeparateMethods (one named method per success content type)

Methods/interfaces are driven by SUCCESS content types.
Error content types determine error case variants within those interfaces.
```

### Error case generation

```
For each error status (non-2xx):
  errorContentTypes = returnType.types  // Map<ContentType, Model>
  if errorContentTypes.size <= 1:
    → Single error case (e.g., BadRequest) shared across all success interfaces
  else:
    → One error case per error content type, ALL shared across all success interfaces
    → Named: {CT}{Status} e.g., JsonBadRequest, ScimJsonBadRequest
    → Each implements ALL success sealed interfaces (server picks error format)
```

### Request body clash detection

```
variants = body.variants()
if variants.size == 1 → single invoke
signatureGroups = variants.groupBy { body.toKotlinSignature() }
if all groups size 1 → separate overloads (no clash)
else → RequestType enum for clashing groups
```

## Files to Modify

| File | Change |
|------|--------|
| `renderer/.../ContentTypeStrategy.kt` | **NEW** — strategy detection, naming, clash detection |
| `renderer/.../ClientRendererResponses.kt` | Restructure for multi-interface generation, shared errors, body naming |
| `renderer/.../ClientRendererOperations.kt` | Content-type methods, RequestType enum, response type placement |
| `renderer/.../ClientRendererRequestBody.kt` | RequestType enum generation |
| `renderer/.../ImplRenderer.kt` | Accept header, RequestType content type, response dispatch |
| `renderer/.../OperationInlineModelScope.kt` | Body type naming with status/CT suffix, owner class resolution |
| `renderer/src/jvmTest/.../ClientSpec.kt` | Test cases for all scenarios |

## Key Existing Code to Reuse

- `Route.Bodies.variants()` → `List<Variant(contentType, body)>` (`Route.kt:104-106`)
- `Route.ReturnType.types: Map<ContentType, Model>` — all CTs per status (`Route.kt:209`)
- `OverloadedBody.distinctCaseSignatures()` with `@JvmName` — signature disambiguation pattern (`ClientRendererRequestBody.kt:404-428`)
- `toContentTypeCodeBlock()` — ContentType to KotlinPoet CodeBlock (`ImplRenderer.kt:434-459`)
- `toCaseName()` — HTTP status to case name (`ClientRendererResponses.kt:28-31`)
- `preferredModel()` — JSON-first fallback (`ClientRendererResponses.kt:17-21`)

## Phase Details

See individual phase documents:
- [PHASE_1.md](./PHASE_1.md) — Infrastructure: ContentType naming & strategy detection
- [PHASE_2.md](./PHASE_2.md) — Fix ISSUE_RESPONSE_TYPE: body naming collision
- [PHASE_3.md](./PHASE_3.md) — Multi-content-type response generation
- [PHASE_4.md](./PHASE_4.md) — Request body signature clash detection
- [PHASE_5.md](./PHASE_5.md) — Implementation rendering
- [PHASE_6.md](./PHASE_6.md) — Testing & integration validation
