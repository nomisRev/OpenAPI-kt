# Phase 9 — Client: Operations + Request Bodies

Generate suspend operation functions with parameters and request body handling.

## Tasks

- [ ] Operation function rendering:
  - Function name: HTTP method lowercase (`get`, `post`, `put`, `delete`, `patch`, `head`, `options`)
  - Always `suspend`
  - `@Deprecated("Deprecated by the API provider")` when `Route.deprecated`
- [ ] Parameter ordering on operation functions:
  1. Required query parameters
  2. Required header parameters
  3. Required cookie parameters
  4. Request body (if required)
  5. Optional query parameters (`= null`)
  6. Optional header parameters (`= null`)
  7. Optional cookie parameters (`= null`)
  8. Request body (if optional, `= null`)
  - Path parameters are NEVER on operation functions (captured by navigation)
- [ ] Parameter rendering:
  - Filter out `Parameter.Input.Path` from `Route.parameters`
  - Name: `Input.name.toParamName()`
  - Type: `Input.type.toTypeName(config)`, nullable + `= null` for optional
  - `@SerialName` not needed on function params — only on data class properties
- [ ] Request body rendering (interface side — just the parameter):
  - `Bodies.defaultOrNull()` to pick the preferred body type
  - `SetBody` (JSON/XML/OctetStream) → `body: BodyType` parameter
  - `FormUrlEncoded` → expand parameters inline: `grantType: String, code: String, ...`
  - `Multipart.Value` (inline) → expand parameters inline: `file: ByteArray, purpose: String, ...`
  - `Multipart.Ref` (referenced) → `body: ReferencedType` parameter
  - Body required? → non-nullable. Optional? → `body: Type? = null`
- [ ] Content type preference (when multiple available):
  1. JSON (`application/json`)
  2. Multipart (`multipart/form-data`)
  3. Form URL Encoded (`application/x-www-form-urlencoded`)
- [ ] Inline enum types for parameters:
  - When a query/header parameter has `Model.Enum` type that's not top-level → generate enum nested in the interface
  - Wire with enum rendering from Phase 1
- [ ] Default values for parameters:
  - Query param with default → `@Required paramName: Type = defaultValue`
  - Optional with default → `paramName: Type? = defaultValue`

## Golden Tests

- [ ] `client/operations-basic` — GET with no params, POST with body
- [ ] `client/operations-params` — query, header, cookie params in correct order
- [ ] `client/operations-optional` — mix of required and optional params
- [ ] `client/operations-body-json` — JSON request body (required + optional)
- [ ] `client/operations-body-multipart` — multipart form data (inline + referenced)
- [ ] `client/operations-body-form` — form URL encoded body
- [ ] `client/operations-deprecated` — deprecated operation with @Deprecated
- [ ] `client/operations-inline-enum` — query param with inline enum type
- [ ] `client/operations-defaults` — parameters with default values

## Files to Create/Modify

- **Modify**: `renderer/.../ClientRenderer.kt` — add operation function rendering
- **Create**: golden test resource files under `renderer/src/test/resources/kotlinTestData/client/`
- **Modify**: `renderer/.../ClientSpec.kt` — add test cases

## Key Decisions

- Body parameter is always named `body` for SetBody and Multipart.Ref
- FormUrlEncoded and inline Multipart expand their parameters directly (no wrapper)
- Only the preferred content type is used (no overloads for multiple content types)
- `@Deprecated` message is always the generic "Deprecated by the API provider"
