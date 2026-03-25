# Phase 6: Testing & Integration Validation

## Goal

Comprehensive testing of all scenarios against both synthetic specs and real-world APIs (GitHub, Cloudflare, Supabase). Validate generated code compiles and matches expected shapes.

## Deliverables

1. New/updated test cases in `renderer/src/jvmTest/`
2. Passing GitHub integration tests
3. Validation against Cloudflare and Supabase specs

## Tasks

### 6.1 Unit Tests — Response Strategy

**File:** `renderer/src/jvmTest/kotlin/io/github/nomisrev/render/ClientSpec.kt` (or new file)

**Test cases:**

1. **Single content type, single status → invoke() returns direct type**
   - Spec: 200 with `application/json` → User
   - Verify: `suspend operator fun invoke(): User`

2. **Single content type, multiple statuses → invoke() returns Response sealed interface**
   - Spec: 200 JSON → User, 404 JSON → BasicError
   - Verify: `sealed interface Response`, `invoke(): Response`

3. **Multiple success CTs, different schemas → separate methods**
   - Spec: 200 with `application/json` → Repository, `application/vnd.github.v3.star+json` → StarredRepository
   - Verify: `sealed interface JsonResponse`, `sealed interface VndGithubV3StarJsonResponse`, `fun json()`, `fun vndGithubV3StarJson()`

4. **Multiple success CTs, same schema → separate methods (no enum)**
   - Spec: 200 with `application/json` → String, `text/plain` → String
   - Verify: `fun json(): String`, `fun textPlain(): String`

5. **Multiple success CTs + error statuses → shared error cases**
   - Spec: 200 json/xml (different schemas), 404 json (BasicError)
   - Verify: `data class NotFound(val value: BasicError) : JsonResponse, XmlResponse`

6. **No-content status (304) → data object shared across interfaces**
   - Spec: 200 json/xml, 304 no content
   - Verify: `data object NotModified : JsonResponse, XmlResponse`

7. **Error status with multiple CTs → multiple error cases per status**
   - Spec: 200 json, 400 json (BasicError) + scim+json (ScimError)
   - Verify: `data class JsonBadRequest(val value: BasicError) : JsonResponse`, `data class ScimJsonBadRequest(val value: ScimError) : JsonResponse`

### 6.2 Unit Tests — Request Body Strategy

8. **Different body types → separate overloads (no change)**
   - Spec: `application/json` → JsonData, `text/plain` → String
   - Verify: two `invoke()` overloads

9. **Same body type → RequestType enum**
   - Spec: `text/plain` → String, `text/x-markdown` → String
   - Verify: `enum class RequestType`, `invoke(body: String, requestType: RequestType)`

10. **Mixed: some clash, some don't → enum for clashing + overloads for unique**

### 6.3 Unit Tests — Body Naming (ISSUE_RESPONSE_TYPE)

11. **Inline 200 schema in multi-status operation → OkBody not Response**
    - Spec: 200 inline oneOf, 404 ref
    - Verify: `data class Ok(val value: OkBody)` not `Ok(val value: Response)`

12. **Inline error schema → ServiceUnavailableBody**
    - Spec: 200 ref, 503 inline object
    - Verify: `data class ServiceUnavailable(val value: ServiceUnavailableBody)`

### 6.4 Unit Tests — Content Type Naming

13. **All naming examples from the table**
    - `application/json` → `Json`
    - `application/sarif+json` → `SarifJson`
    - `application/vnd.github.v3.star+json` → `VndGithubV3StarJson`
    - `image/png` → `ImagePng`
    - etc.

### 6.5 Integration Tests — GitHub

**Operations to validate:**

14. **`GET /user/starred`**
    - 200: `application/json` → `List<Repository>`, `application/vnd.github.v3.star+json` → `List<StarredRepository>`
    - 304, 401, 403: error/no-content statuses
    - Verify: `json()` and `vndGithubV3StarJson()` methods exist, `NotModified` data object

15. **`GET /repos/{owner}/{repo}/code-scanning/analyses/{analysis_id}`**
    - 200: `application/json` → `CodeScanningAnalysis`, `application/sarif+json` → inline object
    - 403, 404, 422, 503: error statuses
    - Verify: `json()` and `sarifJson()` methods, no `Ok(value: Response)` collision

16. **`GET /repos/{owner}/{repo}/contents/{path}`**
    - 200: `application/vnd.github.object` → `ContentTree`, `application/json` → oneOf union
    - 302, 304, 403, 404: error/redirect/no-content
    - Verify: `json()` and `vndGithubObject()` methods, inline oneOf body named `OkJsonBody` not `Response`

17. **62 operations using `bad_request` component (400: json + scim+json)**
    - Verify: `JsonBadRequest` and `ScimJsonBadRequest` cases in affected operations

18. **`POST /markdown/raw`** (request body clash)
    - `text/plain` + `text/x-markdown`, both String
    - Verify: `RequestType` enum with `TextPlain` and `TextXMarkdown`

### 6.6 Integration Tests — Cloudflare

19. **AI image generation endpoint**
    - 200: `application/json` + `image/png`
    - Verify: `json()` and `imagePng()` methods

20. **AI text generation endpoint**
    - 200: `application/json` + `text/event-stream`
    - Verify: `json()` and `textEventStream()` methods

### 6.7 Compilation Validation

21. **Full renderer test suite**
    ```bash
    ./gradlew :renderer:jvmTest
    ```

22. **GitHub generated client compiles**
    ```bash
    ./gradlew :github:compileKotlin
    ```

## Acceptance Criteria

- [ ] All 22 test cases above pass
- [ ] No generated response branch has `value: Response` collision
- [ ] All success content types in specs are reachable via generated methods
- [ ] All error content types generate corresponding error cases
- [ ] Request body signature clashes generate RequestType enum
- [ ] `./gradlew :renderer:jvmTest` passes
- [ ] `./gradlew :github:compileKotlin` passes

## Verification

```bash
./gradlew :renderer:jvmTest
./gradlew :github:compileKotlin
```
