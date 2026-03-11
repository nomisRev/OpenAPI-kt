# Plan: Ktor Client Generation

> Source PRD: `KTOR_CLIENT_SPEC.md`

## Architectural decisions

Durable decisions that apply across all phases:

- **Pipeline integration**: Route model (`Route`, `Root`, `API` from `routes/`) is the input. Output is `List<KFile>` via `Generation.kt`, same as model generation.
- **Renderer pattern**: Use `context(Renderer)` consistently, matching existing `ObjectRenderer`, `UnionRenderer`, etc.
- **File layout**: Generated files go under `<outputDir>/<pkg>/api/`. Root interface + factory + root impl live in one file. Each direct child of the root gets its own file containing the interface, nested sub-interfaces, sealed result types, and impl classes.
- **Naming**: Interface names = PascalCase path segments. Property names = camelCase path segments. Function names = `operationId` as-is. Impl classes use `Ktor` prefix (e.g. `KtorPetStore`).
- **Type variants**: Request positions always use the write variant of a model. Response positions always use the read variant. The transformer already produces these — the client renderer selects the correct one.
- **HTTP client**: Ktor `HttpClient` with `ContentNegotiation` and `defaultRequest { url(baseUrl) }` auto-installed. All operation functions are `suspend`. Impl classes only receive the configured `HttpClient` — no `baseUrl` parameter.
- **Implementation visibility**: All impl classes are `internal`. Only interfaces and the factory function are public.
- **Parameter order**: Path → required query → required header → required body → optional query → optional header → optional body.
- **Content type preference**: When multiple content types exist for one response, select the most structured: json > xml > plain > binary. Implemented via chained `firstNotNullOfOrNull` per content type.

---

## Phase 1: Minimal GET endpoint ✅

**User stories**: §1 root interface, §2 function signature (no params), §3.1 direct return, §4 impl class (GET only), §5 factory function, §6 file layout (single file)

### What to build

A single end-to-end vertical slice: given a spec with one GET endpoint that takes no parameters and returns a single 200 JSON response, generate a root interface with one `suspend fun`, an `internal` implementation class backed by Ktor `HttpClient`, and a top-level factory function that installs `ContentNegotiation`. Output as a single `.kt` file under `api/`. Use the correct read/write type variants from the start — request positions use write types, response positions use read types.

### Acceptance criteria

- [x] A spec with a single parameterless GET endpoint produces a compilable root interface with one `suspend fun`
- [x] An `internal` impl class is generated that calls `client.get("/path").body<T>()`
- [x] A top-level factory function is generated that creates an `HttpClient`, installs `ContentNegotiation { json() }` and `defaultRequest { url(baseUrl) }`, and returns the root interface
- [x] The factory accepts a `baseUrl: String` parameter and an `HttpClientConfig<*>.() -> Unit` lambda
- [x] Output is a single `KFile` under the `api/` package
- [x] Read/write type variants are correctly selected (response types use read variant)
- [x] Generated code compiles and the rendering follows existing `context(Renderer)` pattern

### Implementation notes

- **Files**: `ClientRenderer.kt` (rendering logic), `Generation.kt` (`Root.generateClient()` entry point)
- **Tests**: `ClientRenderSpec.kt` — 6 tests covering: parameterless GET, reference return type with import verification, Unit return, empty root (no-body impl), impl naming (`Ktor` prefix), read variant selection
- **Impl naming**: `Ktor{Name}` prefix (e.g. `KtorPetStore`) instead of `{Name}Impl`
- **Base URL**: Handled via `defaultRequest { url(baseUrl) }` in the factory; impl classes only take `HttpClient`, operation URLs use just the path
- **Content type preference**: Chained `firstNotNullOfOrNull` per content type for correct json > xml > plain > binary priority

## Phase 2: Parameters & deprecation ✅

**User stories**: §2.1 path params, §2.2 query params, §2.3 header params, §4.4 parameter placement, §7 @Deprecated, cookie params

### What to build

Extend function signatures and impl classes to support all parameter types. Path parameters become positional arguments interpolated into the URL. Query and header parameters are placed via Ktor's `parameter()` and `header()` calls, with optional params guarded by `?.let`. Cookie parameters use `cookie()`. Parameters follow the defined ordering (path → required query → required header → optional query → optional header). Operations marked `deprecated: true` produce `@Deprecated` annotations.

### Acceptance criteria

- [x] Path parameters are interpolated into the URL string and appear as required positional arguments
- [x] Required query params use `parameter("name", value)`, optional use `value?.let { parameter("name", it) }`
- [x] Header parameters use `header("Name", value)` with the same required/optional pattern
- [x] Cookie parameters use `cookie("name", value)`
- [x] Parameter ordering matches the spec: path → required query → required header → optional query → optional header
- [x] Parameters with non-null OpenAPI defaults use that default value instead of `= null`
- [x] `@Deprecated("Deprecated by the API provider")` is emitted for deprecated operations
- [x] Parameter names are converted to camelCase from OpenAPI `name` field

### Implementation notes

- **Files**: `ClientRenderer.kt` (rendering logic — `renderSuspendFun`, `renderOperationImpl`, `renderParamPlacement`, `sortedParameters`, `renderDefault`)
- **Tests**: `ClientRenderSpec.kt` — 15 tests total (6 from Phase 1, 9 new: path param interpolation, required/optional query, header, cookie, parameter ordering, default values, required+default without annotation, @Deprecated, camelCase names)
- **Parameter rendering rules**: required+no-default → `name: Type`, required+default → `name: Type = default`, optional+default → `name: Type = default`, optional+no-default → `name: Type? = null`. No `@Required` annotation — it's a serialization annotation, not applicable to function parameters.
- **Impl rules**: non-nullable params → direct `parameter()`/`header()`/`cookie()` call; nullable params → `?.let { ... }` pattern
- **Path interpolation**: `{paramName}` in route path replaced with `$camelCaseName` for Kotlin string templates

---

## Phase 3: Nested interface hierarchy

**User stories**: §1 full path-segment nesting, §6 multi-file layout

### What to build

Generate the full nested interface tree from path segments. Path segments (excluding `{...}` parameters) become nested interfaces. Each interface declares `val` properties for its children. Operations at a path's leaf become `suspend fun` on that interface. Each direct child of the root interface gets its own file containing the interface, all nested sub-interfaces, and corresponding impl classes. The root file contains the root interface, root impl, and factory function.

### Acceptance criteria

- [x] Path segments are grouped into a nested interface tree (e.g., `/chat/completions` → `Chat` interface with nested `Completions`)
- [x] Parent interfaces declare child interfaces via `val` properties (e.g., `val chat: Chat`)
- [x] Impl classes instantiate child impls and assign them to the corresponding properties
- [x] Each direct child of the root gets its own `.kt` file (e.g., `Chat.kt`, `Models.kt`)
- [x] Deeper nesting is expressed as inner interfaces within the top-level child's file
- [x] Operations at the root path (no segments after stripping params) are placed on the root interface
- [x] Interface names use PascalCase, property names use camelCase

---

## Phase 4: Request bodies

**User stories**: §2.4 JSON body, §2.4.2 multipart, §2.4.3 form-urlencoded, §4.5 body placement, §4.2 all HTTP methods

### What to build

Support request bodies across all content types and HTTP methods. JSON bodies become a single typed `body` parameter with `contentType(Application.Json)` + `setBody(body)`. Multipart form data with inline schemas expand each property into its own parameter, assembled into `MultiPartFormDataContent`; `$ref` schemas use a single `body` parameter. Form-urlencoded bodies expand properties into parameters, assembled via `Parameters.build { ... }.formUrlEncode()`. Support all HTTP methods (POST, PUT, PATCH, DELETE, HEAD, OPTIONS). Required vs optional body follows the `requestBody.required` flag.

### Acceptance criteria

- [x] JSON body is a typed `body` parameter; impl sets `contentType(Application.Json)` and `setBody(body)`
- [x] Optional JSON body has `body: T? = null` and is conditionally set
- [x] Multipart inline schema expands properties into individual parameters; impl builds `MultiPartFormDataContent`
- [x] Multipart `$ref` schema uses a single `body` parameter
- [x] Form-urlencoded body expands properties into parameters; impl builds `Parameters.build { ... }.formUrlEncode()`
- [x] Content type selection follows priority: json > xml > octet-stream > plain > multipart > form-urlencoded
- [x] All HTTP methods (GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS) map to the correct Ktor function
- [x] Body parameter appears in the correct position in the parameter ordering
- [x] Write type variants are used for request body types

---

## Phase 5: Sealed return types

**User stories**: §3.1 sealed vs direct decision, §3.2 case naming, §3.3 default response, §3.4 undocumented responses, §3.5 content type preference

### What to build

When an operation defines more than one response or includes a `default` response, generate a sealed result type nested inside the declaring interface. Each status code becomes a case (named by HTTP reason phrase in PascalCase). The `default` case carries `HttpStatusCode` alongside the typed body. The impl dispatches on `response.status` via a `when` expression. Single-response operations without `default` continue to return the body type directly. When a status code offers multiple content types, pick the most structured one (json > xml > plain > binary).

### Acceptance criteria

- [x] Single response without `default` returns the body type directly
- [x] Multiple responses OR presence of `default` generates a sealed interface nested in the declaring interface
- [x] Sealed case names follow HTTP reason phrases: `OK`, `Created`, `NoContent`, `BadRequest`, `NotFound`, etc.
- [x] `NoContent` (204) uses `data object` with body type `Unit`
- [x] Empty-body responses use `data object`; responses with body use `data class` wrapping the typed value
- [x] `Default` case is `data class Default(val status: HttpStatusCode, val value: T)`
- [x] Impl uses `when (response.status)` to dispatch to the correct sealed case
- [x] Undocumented status codes (no matching case, no `default`) fall through and throw `ResponseException`
- [x] When multiple content types exist for one status, the most structured type is selected
- [x] Read type variants are used for response body types

---

## Phase 6: Server URL generation

**User stories**: §4.3 server sealed interface, variable interpolation, factory function integration

### What to build

Generate a typed sealed interface from the OpenAPI `servers` block. Servers without variables become `data object` cases. Servers with variables become `data class` cases — enum-constrained variables produce nested `enum class` parameters, free-form variables become `String` parameters with defaults. A `Custom(override val url: String)` escape hatch is always present. The factory function accepts the server sealed type (defaulting to the first documented server) instead of a raw `baseUrl: String`. When no servers are declared, the factory requires an explicit `baseUrl` with no default.

### Acceptance criteria

- [ ] Each server entry without variables generates a `data object` with `override val url`
- [ ] Each server entry with variables generates a `data class` with parameters for each variable
- [ ] Enum-constrained variables produce a nested `enum class` with `(val value: String)` entries
- [ ] Free-form variables become `String` parameters with their default value
- [ ] Case names are derived from server `description` (PascalCase, trailing "server" stripped); fallback to `Default` or `Server1`, `Server2`, …
- [ ] `data class Custom(override val url: String)` is always generated as an escape hatch
- [ ] Factory function accepts `server: <ApiName>Server = <ApiName>Server.<First>` instead of raw `baseUrl`
- [ ] When no servers are in the spec, the factory takes `baseUrl: String` with no default
- [ ] The sealed interface is generated in the root interface file
