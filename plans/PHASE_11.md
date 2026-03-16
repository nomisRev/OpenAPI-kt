# Phase 11 — Client: Ktor Implementation + Server + Files

Generate internal Ktor implementation classes, factory function, server configuration, and finalize file organization.

## Tasks

### Ktor Implementation

- [x] Generate `internal class Ktor{Name}` for each interface with operations or children:
  - Constructor: `private val client: HttpClient` + all accumulated path parameters
  - Implements the corresponding interface (e.g., `KtorRepos : GitHub.Repos`)
- [x] Static segment properties:
  - `override val repos: GitHub.Repos = KtorRepos(client)` — created at construction time
- [x] Parameter navigation functions:
  - `override fun owner(owner: String): GitHub.Repos.Owner = KtorOwner(client, owner)`
  - Threads accumulated path params + the new one to child implementation
- [x] Operation function implementations:
  - Build path string with captured parameters: `"/repos/$owner/$repo"`
  - Query parameters: `parameter("name", value)` (required) / `value?.let { parameter("name", it) }` (optional)
  - Header parameters: `header("X-Header-Name", value)` / `value?.let { header("name", it) }` (optional)
  - Cookie parameters: `cookie("name", value)` / `value?.let { cookie("name", it) }` (optional)
- [x] Request body implementations:
  - JSON: `contentType(ContentType.Application.Json); setBody(body)`
  - Optional JSON: `body?.let { contentType(...); setBody(it) }`
  - Multipart inline: `MultiPartFormDataContent(formData { append("name", value) })`
  - Multipart ref: `contentType(ContentType.MultiPart.FormData); setBody(body)`
  - FormUrlEncoded: `Parameters.build { append("key", value) }.formUrlEncode()`
- [x] Response handling implementations:
  - Single response: `client.get("/path").body()`
  - Multiple responses: `when (response.status.value) { 200 -> GetResult.Ok(response.body()); ... }`
  - Default response: `else -> GetResult.Default(response.status, response.body())`
  - No default: `else -> throw ResponseException(response, "")`

### Factory Function

- [x] Generate `fun {Name}Client(baseUrl: String, block: HttpClientConfig<*>.() -> Unit = {}): {Name}`:
  - Creates `HttpClient` with `ContentNegotiation { json() }` and `defaultRequest { url(baseUrl) }`
  - Returns `Ktor{Name}(client)`
  - Overload with server parameter when servers are defined

### Server Configuration

- [x] No servers / single implicit default → no server sealed interface, just `baseUrl: String` factory
- [x] Named servers → generate `sealed interface {Name}Server`:
  - `val url: String` abstract property
  - Each server → `data object ServerName : {Name}Server { override val url = "..." }`
  - `data class Custom(override val url: String) : {Name}Server` always present
  - Server naming: description → PascalCase, strip trailing "server"
- [x] Servers with variables → `data class` instead of `data object`:
  - Constructor parameters for each variable with defaults
  - `override val url: String get() = "https://${env.value}.api.example.com/$version"`
  - Variable enum types generated inside the server case
- [x] Factory overload: `fun {Name}Client(server: {Name}Server = {Name}Server.Production, ...): {Name}`

### File Organization

- [x] Root file (`{Name}.kt`):
  - Root interface declaration
  - Factory function
  - Server sealed interface (if any)
  - Root implementation class
- [x] Direct child files (`Repos.kt`, `Users.kt`, etc.):
  - Child interface + all deeper nested interfaces
  - Corresponding implementation classes
- [x] All files share the api package

## Golden Tests

- [x] Existing 21 client tests updated with impl classes + factory
- [x] `client/factory-servers` — factory with named servers
- [x] `client/factory-server-variables` — factory with server variables
- [x] `client/impl-full` — full implementation with params, body, and sealed response

## Files Created/Modified

- **Created**: `renderer/.../ImplRenderer.kt` — Ktor implementation class generation
- **Created**: `renderer/.../FactoryRenderer.kt` — factory function + server sealed interface
- **Modified**: `renderer/.../ClientRenderer.kt` — file organization logic (impl + factory in files)
- **Modified**: `renderer/.../ClientSpec.kt` — added 3 new test cases
- **Modified**: `renderer/build.gradle.kts` — added `ktor-client-core` test dependency
- **Updated**: all golden test resource files under `renderer/src/test/resources/kotlinTestData/client/`

## Key Decisions

- Implementation classes are always `internal` — users interact via interfaces
- Path string uses Kotlin string templates with captured parameters (e.g., `"/repos/$owner/$repo"`)
- Implementation classes are in the same file as their interface (same file decision from earlier)
- Factory function name convention: `{RootInterface}Client` (e.g., `GitHubClient`)
- Server sealed interface always has a `Custom(url: String)` case for flexibility
- HTTP method extension functions (get, post, etc.) use explicit `addImport` on FileSpec due to name conflicts with interface method names in KotlinPoet
- Override functions do not specify default parameter values (Kotlin restriction)
- Response status matching uses `response.status.value` (Int) for cleaner generated code
