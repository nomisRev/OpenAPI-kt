# Phase 10 — Client: Response Handling

Generate return types for operations, including sealed interfaces for multiple responses.

## Tasks

- [ ] Single response handling:
  - When `Returns` has exactly one status code and no default → direct return type
  - Return type from the JSON content type's `Model.toTypeName(config)`
  - Unit return for empty responses / 204
- [ ] Multiple response handling → sealed interface:
  - Named `{Method}Result` (e.g., `GetResult`, `PostResult`)
  - Nested inside the interface that owns the path node
  - Each status code → a case: `data class Ok(val value: Repository) : GetResult`
  - Case naming: `HttpStatusCode.description` → PascalCase (e.g., `200 OK` → `Ok`, `404 Not Found` → `NotFound`, `204 No Content` → `NoContent`)
  - Unit/empty response case → `data object NoContent : GetResult`
- [ ] Default response:
  - `data class Default(val status: HttpStatusCode, val value: ErrorType) : GetResult`
  - Used in the `else` branch of the `when` block
- [ ] Operation return type:
  - Single response → `suspend fun get(): Repository`
  - Multiple responses → `suspend fun get(): GetResult`
  - Unit response → `suspend fun delete()` (no explicit return type)
- [ ] Result sealed interface annotations:
  - No `@Serializable` on the result sealed interface itself (it's not serialized directly)
  - Cases that wrap serializable types don't need annotations either — they're constructed in the implementation

## Golden Tests

- [ ] `client/response-single` — single 200 response → direct return
- [ ] `client/response-unit` — 204 no content → Unit return
- [ ] `client/response-multiple` — 200 + 404 → sealed interface with two cases
- [ ] `client/response-default` — 200 + default → sealed interface with Ok + Default cases
- [ ] `client/response-mixed` — 200 + 204 + 404 + default → full sealed interface

## Files to Create/Modify

- **Modify**: `renderer/.../ClientRenderer.kt` — add response type generation, update operation return types
- **Create**: golden test resource files under `renderer/src/test/resources/kotlinTestData/client/`
- **Modify**: `renderer/.../ClientSpec.kt` — add test cases

## Key Decisions

- Result sealed interfaces are NOT `@Serializable` — they're runtime wrappers constructed in the implementation
- Case names come from `HttpStatusCode.description` → PascalCase, with `NoContent` hardcoded for 204
- `Default` case carries `HttpStatusCode` so the caller knows which undocumented status was returned
- Multiple content types per response: prefer JSON, fall back to first available
