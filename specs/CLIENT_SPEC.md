# Client Generation Specification

This document specifies how a Kotlin Ktor client should be generated from the `ApiTree` typed model.

## Core Principle

The generated API mirrors the URL path structure exactly. Each path segment becomes a navigation step:
- **Static segments** → `val` property access
- **Parameter segments** → function call capturing the value
- **HTTP method** → terminal suspend function name

```
Path:    /repos/{owner}/{repo}/collaborators
Usage:   client.repos.owner("simon").repo("openapi").collaborators.get()
```

No dependency on `operationId`. The path structure and HTTP method fully determine the API shape.

---

## 1. Interface Structure

### Root Interface

Name of top-level client is user provided `generate("GitHub", spec.json)`. Contains:
- Suspend functions for operations at `/` (root-level operations)
- `val` properties for top-level path segments

```kotlin
interface GitHub {
    // Root-level operation: GET /
    suspend fun get(): RootInfo

    // Navigation to /repos
    val repos: Repos

    // Navigation to /users
    val users: Users
}
```

### Nested Interfaces (Static Segments)

Each static path segment generates a nested interface inside its parent. Accessed via `val` property.

```kotlin
interface GitHub {
    val repos: Repos

    interface Repos {
        // Navigation to /repos/{owner}
        fun owner(owner: String): Owner

        interface Owner {
            // Navigation to /repos/{owner}/{repo}
            fun repo(repo: String): Repo

            interface Repo {
                // GET /repos/{owner}/{repo}
                suspend fun get(): Repository
                // DELETE /repos/{owner}/{repo}
                suspend fun delete()

                val collaborators: Collaborators

                interface Collaborators {
                    // GET /repos/{owner}/{repo}/collaborators
                    suspend fun get(affiliation: String? = null): List<Collaborator>
                }
            }
        }
    }
}
```

Full type path: `GitHub.Repos.Owner.Repo.Collaborators`

### Parameter Segments (Navigation Functions)

Parameter segments generate **non-suspend** functions that return the next interface. The function name and parameter name both use the OpenAPI placeholder name directly.

```kotlin
// For path segment {owner}
fun owner(owner: String): Owner

// For path segment {pet_id} (camelCase conversion applied to parameter name)
fun petId(petId: String): PetId
```

The parameter type comes from the OpenAPI parameter definition (resolved to `Model`).

### Interface Naming

- Static segments: PascalCase of the segment name (e.g., `repos` → `Repos`, `fine_tuning` → `FineTuning`)
- Parameter segments: PascalCase of the placeholder name (e.g., `{owner}` → `Owner`, `{pet_id}` → `PetId`)
- All interfaces are nested inside their parent — no flat top-level interfaces
- Name uniqueness is guaranteed by nesting

---

## 2. Operation Functions

### Function Naming

The function name is always the **HTTP method** in lowercase:

| HTTP Method | Function Name |
|-------------|---------------|
| GET         | `get()`       |
| POST        | `post()`      |
| PUT         | `put()`       |
| DELETE      | `delete()`    |
| PATCH       | `patch()`     |
| HEAD        | `head()`      |
| OPTIONS     | `options()`   |

Multiple methods at the same path produce multiple functions on the same interface:

```kotlin
interface Repo {
    suspend fun get(): Repository      // GET /repos/{owner}/{repo}
    suspend fun delete()               // DELETE /repos/{owner}/{repo}
    suspend fun patch(body: UpdateRepoRequest): Repository  // PATCH /repos/{owner}/{repo}
}
```

### Parameter Ordering

Function parameters follow this order:

1. Required query parameters
2. Required header parameters
3. Required cookie parameters
4. Request body (if required)
5. Optional query parameters (nullable, `= null`)
6. Optional header parameters (nullable, `= null`)
7. Optional cookie parameters (nullable, `= null`)
8. Request body (if optional, nullable, `= null`)

Path parameters are **never** on operation functions — they are captured by navigation functions in the tree.

### Deprecation

Deprecated operations receive `@Deprecated` annotation on both interface and implementation:

```kotlin
@Deprecated("Deprecated by the API provider")
suspend fun get(): LegacyResponse
```

---

## 3. Implementation Pattern

### Internal Ktor Implementation

Each interface with operations or children gets an internal implementation class. Parameter navigation functions create child implementation instances, threading the captured path values through.

```kotlin
internal class KtorGitHub(private val client: HttpClient) : GitHub {
    override val repos: GitHub.Repos = KtorRepos(client)
}

internal class KtorRepos(private val client: HttpClient) : GitHub.Repos {
    override fun owner(owner: String): GitHub.Repos.Owner = KtorOwner(client, owner)
}

internal class KtorOwner(
    private val client: HttpClient,
    private val owner: String,
) : GitHub.Repos.Owner {
    override fun repo(repo: String): GitHub.Repos.Owner.Repo = KtorRepo(client, owner, repo)
}

internal class KtorRepo(
    private val client: HttpClient,
    private val owner: String,
    private val repo: String,
) : GitHub.Repos.Owner.Repo {

    override suspend fun get(): Repository =
        client.get("/repos/$owner/$repo").body()

    override suspend fun delete() {
        client.delete("/repos/$owner/$repo")
    }

    override val collaborators: GitHub.Repos.Owner.Repo.Collaborators =
        KtorCollaborators(client, owner, repo)
}

internal class KtorCollaborators(
    private val client: HttpClient,
    private val owner: String,
    private val repo: String,
) : GitHub.Repos.Owner.Repo.Collaborators {

    override suspend fun get(affiliation: String?): List<Collaborator> =
        client.get("/repos/$owner/$repo/collaborators") {
            affiliation?.let { parameter("affiliation", it) }
        }.body()
}
```

Key points:
- Each implementation class stores `HttpClient` + all accumulated path parameters
- Navigation functions (`owner()`, `repo()`) create new instances passing accumulated state
- Static segment properties (`val collaborators`) create instances at construction time
- Path interpolation uses Kotlin string templates with captured parameter values

### Factory Function

```kotlin
fun GitHubClient(
    baseUrl: String,
    block: HttpClientConfig<*>.() -> Unit = {},
): GitHub {
    val client = HttpClient {
        install(ContentNegotiation) { json() }
        defaultRequest { url(baseUrl) }
        block()
    }
    return KtorGitHub(client)
}
```

---

## 4. Query Parameters

### Required

```kotlin
// OpenAPI: required query parameter "query" of type string
suspend fun get(query: String): SearchResult =
    client.get("/search") {
        parameter("query", query)
    }.body()
```

#### Required with Default Value

```kotlin
// OpenAPI: query parameter "limit" with default: 20
suspend fun get(@Required limit: Int = 20): List<Item> =
    client.get("/items") {
        parameter("limit", limit)
    }.body()
```

### Optional

```kotlin
// OpenAPI: optional query parameter "limit" of type integer
suspend fun get(limit: Int? = null): List<Item> =
    client.get("/items") {
        limit?.let { parameter("limit", it) }
    }.body()
```

#### Optional with Default Value

```kotlin
// OpenAPI: query parameter "limit" with default: 20
suspend fun get(limit: Int? = 20): List<Item> =
    client.get("/items") {
        parameter("limit", limit)
    }.body()
```


---

## 5. Header Parameters

```kotlin
// Required header "X-Api-Key", optional header "X-Trace-Id"
suspend fun get(xApiKey: String, xTraceId: String? = null): Data =
    client.get("/data") {
        header("X-Api-Key", xApiKey)
        xTraceId?.let { header("X-Trace-Id", it) }
    }.body()
```

- Original header name preserved in `header()` call
- Parameter name is camelCase conversion (e.g., `X-Api-Key` → `xApiKey`)

---

## 6. Request Body

### JSON Body (Required)

```kotlin
suspend fun post(body: CreatePetRequest): Pet =
    client.post("/pets") {
        contentType(ContentType.Application.Json)
        setBody(body)
    }.body()
```

### JSON Body (Optional)

```kotlin
suspend fun patch(body: UpdateSettingsRequest? = null): Settings =
    client.patch("/settings") {
        contentType(ContentType.Application.Json)
        body?.let { setBody(it) }
    }.body()
```

### Multipart FormData (Inline Schema)

When the multipart schema is defined inline, parameters are expanded:

```kotlin
suspend fun post(file: ByteArray, purpose: String): FileObject =
    client.post("/files") {
        setBody(
            MultiPartFormDataContent(
                formData {
                    append("file", file)
                    append("purpose", purpose)
                }
            )
        )
    }.body()
```

### Multipart FormData (Referenced Schema)

When the schema references a named type:

```kotlin
suspend fun post(body: UploadFileRequest): FileObject =
    client.post("/files") {
        contentType(ContentType.MultiPart.FormData)
        setBody(body)
    }.body()
```

### Form URL Encoded

```kotlin
suspend fun post(grantType: String, code: String, redirectUri: String): TokenResponse =
    client.post("/oauth/token") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(
            Parameters.build {
                append("grant_type", grantType)
                append("code", code)
                append("redirect_uri", redirectUri)
            }.formUrlEncode()
        )
    }.body()
```

### Content Type Preference

When multiple content types are available, preference order:
1. JSON (`application/json`)
2. Multipart (`multipart/form-data`)
3. Form URL Encoded (`application/x-www-form-urlencoded`)

---

## 7. Response Handling

### Single Response

Direct return type:

```kotlin
suspend fun get(): List<Pet> =
    client.get("/pets").body()
```

### Multiple Responses

Sealed interface with status-specific cases. The sealed interface is nested inside the interface that owns the path node.

```kotlin
interface Repo {
    sealed interface GetResult {
        data class OK(val value: Repository) : GetResult
        data class NotFound(val value: ErrorResponse) : GetResult
    }

    suspend fun get(): GetResult
}

// Implementation:
override suspend fun get(): GitHub.Repos.Owner.Repo.GetResult {
    val response = client.get("/repos/$owner/$repo")
    return when (response.status) {
        HttpStatusCode.OK -> GitHub.Repos.Owner.Repo.GetResult.OK(response.body())
        HttpStatusCode.NotFound -> GitHub.Repos.Owner.Repo.GetResult.NotFound(response.body())
        else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
    }
}
```

### Result Type Naming

The result sealed interface is named `{Method}Result` (PascalCase HTTP method + "Result"):
- `GET` → `GetResult`
- `POST` → `PostResult`
- `DELETE` → `DeleteResult`

This is unambiguous because each interface only has one operation per HTTP method.

### Default Response

Fallback case with status code:

```kotlin
sealed interface GetResult {
    data class OK(val value: Repository) : GetResult
    data class Default(val status: HttpStatusCode, val value: ErrorResponse) : GetResult
}

// In when block:
else -> GetResult.Default(response.status, response.body())
```

### No Content (204)

```kotlin
sealed interface DeleteResult {
    data class OK(val value: String) : DeleteResult
    data object NoContent : DeleteResult
}
```

---

## 8. Server Configuration

### No Servers / Single Default

```kotlin
fun GitHubClient(
    baseUrl: String,
    block: HttpClientConfig<*>.() -> Unit = {},
): GitHub
```

### Named Servers

```kotlin
sealed interface GitHubServer {
    val url: String

    data object Production : GitHubServer {
        override val url = "https://api.github.com"
    }

    data object Staging : GitHubServer {
        override val url = "https://staging.api.github.com"
    }

    data class Custom(override val url: String) : GitHubServer
}

fun GitHubClient(
    server: GitHubServer = GitHubServer.Production,
    block: HttpClientConfig<*>.() -> Unit = {},
): GitHub
```

### Servers with Variables

```kotlin
sealed interface ExampleServer {
    val url: String

    data class MultiEnvironment(
        val environment: Environment = Environment.Production,
        val version: String = "v2",
    ) : ExampleServer {
        override val url: String
            get() = "https://${environment.value}.api.example.com/$version"

        enum class Environment(val value: String) {
            Production("production"),
            Staging("staging"),
        }
    }

    data class Custom(override val url: String) : ExampleServer
}
```

---

## 9. Inline Enum Types

Query/header parameters with enum schemas generate a nested enum:

```kotlin
interface Collaborators {
    @Serializable
    enum class Affiliation {
        @SerialName("outside") Outside,
        @SerialName("direct") Direct,
        @SerialName("all") All;
    }

    suspend fun get(affiliation: Affiliation = Affiliation.All): List<Collaborator>
}
```

---

## 10. File Organization

The root interface and its nested interfaces can be split across files. The splitting strategy:

- **Root file** (`GitHub.kt`): root interface + factory + server sealed interface + root implementation
- **Direct children** get their own files: `Repos.kt`, `Users.kt`, `Orgs.kt`
- **Deeper nesting** stays as inner interfaces within the direct child file

```
GitHub.kt           → GitHub interface, factory, server
Repos.kt            → GitHub.Repos + all nested (Owner, Repo, Collaborators, etc.)
Users.kt            → GitHub.Users + all nested
```

All files share the same package.

---

## 11. Naming Conventions

| Source | Convention | Example |
|--------|-----------|---------|
| Interface names | PascalCase from segment | `repos` → `Repos`, `fine_tuning` → `FineTuning` |
| Navigation function names | camelCase from placeholder | `{owner}` → `owner()`, `{pet_id}` → `petId()` |
| Navigation function param names | camelCase from placeholder | `{owner}` → `owner: String`, `{pet_id}` → `petId: String` |
| Operation function names | Lowercase HTTP method | `get()`, `post()`, `delete()` |
| Query/header param names | camelCase | `per_page` → `perPage`, `X-Api-Key` → `xApiKey` |
| Result type names | `{Method}Result` | `GetResult`, `PostResult` |
| Implementation class names | `Ktor{InterfaceName}` | `KtorRepos`, `KtorOwner` |
| Factory function names | `{RootInterface}Client` | `GitHubClient()` |
| Server sealed interface | `{RootInterface}Server` | `GitHubServer` |

---

## 12. Complete Example

OpenAPI paths:
```
GET    /pets
POST   /pets
GET    /pets/{petId}
DELETE /pets/{petId}
GET    /pets/{petId}/toys
```

Generated:

```kotlin
interface PetStore {
    val pets: Pets

    interface Pets {
        // GET /pets
        suspend fun get(limit: Int? = null): List<Pet>
        // POST /pets
        suspend fun post(body: CreatePetRequest): Pet

        fun petId(petId: String): PetId

        interface PetId {
            // GET /pets/{petId}
            suspend fun get(): Pet
            // DELETE /pets/{petId}
            suspend fun delete()

            val toys: Toys

            interface Toys {
                // GET /pets/{petId}/toys
                suspend fun get(): List<Toy>
            }
        }
    }
}

// Usage:
val store = PetStoreClient("https://api.example.com")

store.pets.get(limit = 10)                  // GET /pets?limit=10
store.pets.post(CreatePetRequest("Rex"))     // POST /pets
store.pets.petId("123").get()               // GET /pets/123
store.pets.petId("123").delete()            // DELETE /pets/123
store.pets.petId("123").toys.get()          // GET /pets/123/toys
```
