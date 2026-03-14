# Phase 11 — Client: Ktor Implementation + Server + Files

Generate internal Ktor implementation classes, factory function, server configuration, and finalize file organization.

## Tasks

### Ktor Implementation

- [ ] Generate `internal class Ktor{Name}` for each interface with operations or children:
  - Constructor: `private val client: HttpClient` + all accumulated path parameters
  - Implements the corresponding interface (e.g., `KtorRepos : GitHub.Repos`)
- [ ] Static segment properties:
  - `override val repos: GitHub.Repos = KtorRepos(client)` — created at construction time
- [ ] Parameter navigation functions:
  - `override fun owner(owner: String): GitHub.Repos.Owner = KtorOwner(client, owner)`
  - Threads accumulated path params + the new one to child implementation
- [ ] Operation function implementations:
  - Build path string with captured parameters: `"/repos/$owner/$repo"`
  - Query parameters: `parameter("name", value)` (required) / `value?.let { parameter("name", it) }` (optional)
  - Header parameters: `header("X-Header-Name", value)` / `value?.let { header("name", it) }` (optional)
  - Cookie parameters: `cookie("name", value)` / `value?.let { cookie("name", it) }` (optional)
- [ ] Request body implementations:
  - JSON: `contentType(ContentType.Application.Json); setBody(body)`
  - Optional JSON: `body?.let { contentType(...); setBody(it) }`
  - Multipart inline: `MultiPartFormDataContent(formData { append("name", value) })`
  - Multipart ref: `contentType(ContentType.MultiPart.FormData); setBody(body)`
  - FormUrlEncoded: `Parameters.build { append("key", value) }.formUrlEncode()`
- [ ] Response handling implementations:
  - Single response: `client.get("/path").body()`
  - Multiple responses: `when (response.status) { HttpStatusCode.OK -> GetResult.OK(response.body()); ... }`
  - Default response: `else -> GetResult.Default(response.status, response.body())`
  - No default: `else -> throw ResponseException(response, "Undocumented status code: ${response.status}")`

### Factory Function

- [ ] Generate `fun {Name}Client(baseUrl: String, block: HttpClientConfig<*>.() -> Unit = {}): {Name}`:
  - Creates `HttpClient` with `ContentNegotiation { json() }` and `defaultRequest { url(baseUrl) }`
  - Returns `Ktor{Name}(client)`
  - Overload with server parameter when servers are defined

### Server Configuration

- [ ] No servers / single implicit default → no server sealed interface, just `baseUrl: String` factory
- [ ] Named servers → generate `sealed interface {Name}Server`:
  - `val url: String` abstract property
  - Each server → `data object ServerName : {Name}Server { override val url = "..." }`
  - `data class Custom(override val url: String) : {Name}Server` always present
  - Server naming: description → PascalCase, strip trailing "server"
- [ ] Servers with variables → `data class` instead of `data object`:
  - Constructor parameters for each variable with defaults
  - `override val url: String get() = "https://${env.value}.api.example.com/$version"`
  - Variable enum types generated inside the server case
- [ ] Factory overload: `fun {Name}Client(server: {Name}Server = {Name}Server.Production, ...): {Name}`

### File Organization

- [ ] Root file (`{Name}.kt`):
  - Root interface declaration
  - Factory function
  - Server sealed interface (if any)
  - Root implementation class
- [ ] Direct child files (`Repos.kt`, `Users.kt`, etc.):
  - Child interface + all deeper nested interfaces
  - Corresponding implementation classes
- [ ] All files share the api package

## Golden Tests

- [ ] `client/impl-basic` — simple implementation with path interpolation
- [ ] `client/impl-params` — implementation with query/header/cookie params
- [ ] `client/impl-body` — implementation with various body types
- [ ] `client/impl-response` — implementation with response when-block
- [ ] `client/factory-basic` — factory function with baseUrl
- [ ] `client/factory-servers` — factory with named servers
- [ ] `client/factory-server-variables` — factory with server variables
- [ ] `client/files-organization` — verify correct file splitting

## Files to Create/Modify

- **Create**: `renderer/.../ImplRenderer.kt` — Ktor implementation class generation
- **Create**: `renderer/.../FactoryRenderer.kt` — factory function + server sealed interface
- **Modify**: `renderer/.../ClientRenderer.kt` — file organization logic
- **Modify**: `renderer/.../Generate.kt` — wire everything together
- **Create**: golden test resource files under `renderer/src/test/resources/kotlinTestData/client/`
- **Modify**: `renderer/.../ClientSpec.kt` — add test cases

## Key Decisions

- Implementation classes are always `internal` — users interact via interfaces
- Path string uses Kotlin string templates with captured parameters (e.g., `"/repos/$owner/$repo"`)
- Implementation classes are in the same file as their interface (same file decision from earlier)
- Factory function name convention: `{RootInterface}Client` (e.g., `GitHubClient`)
- Server sealed interface always has a `Custom(url: String)` case for flexibility
