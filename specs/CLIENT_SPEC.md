# Client Generation Specification

This document specifies how a Kotlin Ktor client should be generated from the `ApiTree` typed model.

## Core Principle

The generated API mirrors the URL path structure exactly. Each path segment becomes a navigation step:
- **Static segments** -> `val` property access
- **Parameter segments** -> function call capturing the value
- **HTTP method** -> method-node property with `invoke(...)`

```text
Path:    /repos/{owner}/{repo}/collaborators
Usage:   client.repos.owner("simon").repo("openapi").collaborators.get()
```

No dependency on `operationId`. The path structure and HTTP method fully determine the API shape.

---

## 1. Interface Structure

### Root Interface

Name of top-level client is user provided `generate("GitHub", spec.json)`. Contains:
- Method nodes for operations at `/` (root-level operations)
- `val` properties for top-level path segments

```kotlin
interface GitHub {
    // Root-level operation: GET /
    val get: Get

    interface Get {
        suspend operator fun invoke(): Unit
    }

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
                // HTTP method nodes for /repos/{owner}/{repo}
                val get: Get
                val delete: Delete
                val patch: Patch

                interface Get {
                    suspend operator fun invoke(): Response
                    data class Response(val value: Repository)
                }

                interface Delete {
                    suspend operator fun invoke(): Unit
                }

                interface Patch {
                    suspend operator fun invoke(body: UpdateRepoRequest): Response
                    data class Response(val value: Repository)
                }

                val collaborators: Collaborators

                interface Collaborators {
                    // GET /repos/{owner}/{repo}/collaborators
                    val get: Get

                    interface Get {
                        suspend operator fun invoke(affiliation: String? = null): Response
                        data class Response(val value: List<Collaborator>)
                    }
                }
            }
        }
    }
}
```

Full type path: `GitHub.Repos.Owner.Repo.Collaborators.Get.Response`

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

- Static segments: PascalCase of the segment name (e.g., `repos` -> `Repos`, `fine_tuning` -> `FineTuning`)
- Parameter segments: PascalCase of the placeholder name (e.g., `{owner}` -> `Owner`, `{pet_id}` -> `PetId`)
- Method nodes: PascalCase HTTP method (e.g., `get` -> `Get`, `post` -> `Post`)
- All interfaces are nested inside their parent — no flat top-level interfaces
- Name uniqueness is guaranteed by nesting

---

## 2. Operation Nodes

### Property Naming

The property name is always the **HTTP method** in lowercase:

| HTTP Method | Property Name | Node Type |
|-------------|---------------|-----------|
| GET         | `get`         | `Get`     |
| POST        | `post`        | `Post`    |
| PUT         | `put`         | `Put`     |
| DELETE      | `delete`      | `Delete`  |
| PATCH       | `patch`       | `Patch`   |
| HEAD        | `head`        | `Head`    |
| OPTIONS     | `options`     | `Options` |

Multiple methods at the same path produce multiple properties on the same interface.

### Invoke Signature

Each method node exposes:

```kotlin
suspend operator fun invoke(...): Response
```

### Parameter Ordering

`invoke(...)` parameters follow this order:

1. Required query parameters
2. Required header parameters
3. Required cookie parameters
4. Request body (if required)
5. Optional query parameters (nullable, `= null`)
6. Optional header parameters (nullable, `= null`)
7. Optional cookie parameters (nullable, `= null`)
8. Request body (if optional, nullable, `= null`)

Path parameters are **never** on `invoke(...)` — they are captured by navigation functions in the tree.

### Request `Body` Type Rule

For `application/json` request bodies (`Route.Body.SetBody`):

- **Inline request schema that needs generated naming** -> generate nested `Body` type in method node
- **Referenced schema** -> keep referenced model type directly on `invoke(...)`
- **Form/multipart expanded parameter modes** keep expanded parameters (no nested `Body` wrapper)

Example:

```kotlin
interface Markdown {
    val post: Post

    interface Post {
        suspend operator fun invoke(body: Body): Response

        @Serializable
        data class Body(
            val text: String,
            val mode: Mode? = null,
            val context: String? = null,
        ) {
            @Serializable
            enum class Mode { Markdown, Gfm }
        }

        data class Response(val value: String)
    }
}
```

### Deprecation

Deprecated operations receive `@Deprecated("Deprecated by the API provider")` on:
- Method-node property
- Method-node interface
- `invoke(...)` signature
- Implementation overrides

---

## 3. Implementation Pattern

### Internal Ktor Implementation

Each path interface gets an internal implementation class. Method-node properties are implemented as anonymous objects that implement the node interface.

```kotlin
internal class KtorRepo(
    private val client: HttpClient,
    private val owner: String,
    private val repo: String,
) : GitHub.Repos.Owner.Repo {

    override val get: GitHub.Repos.Owner.Repo.Get = object : GitHub.Repos.Owner.Repo.Get {
        override suspend operator fun invoke(): GitHub.Repos.Owner.Repo.Get.Response {
            val value: Repository = client.get("/repos/$owner/$repo").body()
            return GitHub.Repos.Owner.Repo.Get.Response(value)
        }
    }

    override val delete: GitHub.Repos.Owner.Repo.Delete = object : GitHub.Repos.Owner.Repo.Delete {
        override suspend operator fun invoke(): GitHub.Repos.Owner.Repo.Delete.Response {
            client.delete("/repos/$owner/$repo")
            return GitHub.Repos.Owner.Repo.Delete.Response
        }
    }
}
```

Key points:
- Path implementation classes store `HttpClient` + accumulated path parameters
- Parameter navigation functions create child impl instances with captured values
- Method-node properties are per-path anonymous implementations
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
interface Search {
    val get: Get

    interface Get {
        suspend operator fun invoke(query: String): Response
        data class Response(val value: SearchResult)
    }
}
```

### Optional

```kotlin
interface Items {
    val get: Get

    interface Get {
        suspend operator fun invoke(limit: Int? = null): Response
        data class Response(val value: List<Item>)
    }
}
```

---

## 5. Header Parameters

```kotlin
interface Data {
    val get: Get

    interface Get {
        suspend operator fun invoke(xApiKey: String, xTraceId: String? = null): Response
        data class Response(val value: DataModel)
    }
}
```

- Original header name preserved in `header()` call
- Parameter name is camelCase conversion (e.g., `X-Api-Key` -> `xApiKey`)

---

## 6. Request Body

### JSON Body (Required)

```kotlin
interface Pets {
    val post: Post

    interface Post {
        suspend operator fun invoke(body: CreatePetRequest): Unit
    }
}
```

### JSON Body (Optional)

```kotlin
interface Settings {
    val patch: Patch

    interface Patch {
        suspend operator fun invoke(body: UpdateSettingsRequest? = null): Unit
    }
}
```

### Multipart FormData (Inline Schema)

```kotlin
interface Files {
    val post: Post

    interface Post {
        suspend operator fun invoke(file: ByteArray, purpose: String): Response
        data class Response(val value: FileObject)
    }
}
```

### Multipart FormData (Referenced Schema)

```kotlin
interface Files {
    val post: Post

    interface Post {
        suspend operator fun invoke(body: UploadFileRequest): Response
        data class Response(val value: FileObject)
    }
}
```

### Form URL Encoded

```kotlin
interface OAuth {
    val post: Post

    interface Post {
        suspend operator fun invoke(grantType: String, code: String, redirectUri: String): Response
        data class Response(val value: TokenResponse)
    }
}
```

### Content Type Preference

When multiple content types are available, preference order:
1. JSON (`application/json`)
2. Multipart (`multipart/form-data`)
3. Form URL Encoded (`application/x-www-form-urlencoded`)

---

## 7. Response Handling

### Single Response

Single-response operations return:

- No body / unit -> `Unit`
- Non-inline schema or primitive/collection -> nested `data class Response(val value: T)`
- Inline object/enum schema -> nested concrete generated `Response` type

### Multiple Responses

`Response` becomes a sealed interface with status-specific cases.

```kotlin
interface PetId {
    val get: Get

    interface Get {
        sealed interface Response {
            data class OK(val value: Pet) : Response
            data class NotFound(val value: ErrorResponse) : Response
            data class Default(val status: HttpStatusCode, val value: ErrorResponse) : Response
        }

        suspend operator fun invoke(): Response
    }
}
```

### No Content (204)

`204` without schema becomes `data object` case in sealed `Response`, or `Unit` for single-response operations.

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

Query/header parameter inline enums are generated on the **path node interface** (not method node).

```kotlin
interface Collaborators {
    @Serializable
    enum class Affiliation {
        @SerialName("outside") Outside,
        @SerialName("direct") Direct,
        @SerialName("all") All;
    }

    val get: Get

    interface Get {
        suspend operator fun invoke(affiliation: Affiliation = Affiliation.All): Response
        data class Response(val value: List<Collaborator>)
    }
}
```

---

## 10. File Organization

The root interface and its nested interfaces can be split across files. The splitting strategy:

- **Root file** (`GitHub.kt`): root interface + factory + server sealed interface + root implementation
- **Direct children** get their own files: `Repos.kt`, `Users.kt`, `Orgs.kt`
- **Deeper nesting** stays as inner interfaces within the direct child file

```text
GitHub.kt           -> GitHub interface, factory, server
Repos.kt            -> GitHub.Repos + all nested (Owner, Repo, Collaborators, etc.)
Users.kt            -> GitHub.Users + all nested
```

All files share the same package.

---

## 11. Naming Conventions

| Source | Convention | Example |
|--------|-----------|---------|
| Interface names | PascalCase from segment | `repos` -> `Repos`, `fine_tuning` -> `FineTuning` |
| Navigation function names | camelCase from placeholder | `{owner}` -> `owner()`, `{pet_id}` -> `petId()` |
| Navigation function param names | camelCase from placeholder | `{owner}` -> `owner: String`, `{pet_id}` -> `petId: String` |
| Method-node property names | Lowercase HTTP method | `get`, `post`, `delete` |
| Method-node interface names | PascalCase HTTP method | `Get`, `Post`, `Delete` |
| Invoke entrypoint | `operator fun invoke` | `get(limit = 10)` |
| Query/header param names | camelCase | `per_page` -> `perPage`, `X-Api-Key` -> `xApiKey` |
| Response wrapper name | `Response` | `Get.Response`, `Post.Response` |
| Implementation class names | `Ktor{InterfaceName}` | `KtorRepos`, `KtorOwner` |
| Factory function names | `{RootInterface}Client` | `GitHubClient()` |
| Server sealed interface | `{RootInterface}Server` | `GitHubServer` |

---

## 12. Complete Example

OpenAPI paths:

```text
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
        val get: Get
        val post: Post

        interface Get {
            suspend operator fun invoke(limit: Int? = null): Response
            data class Response(val value: List<Pet>)
        }

        interface Post {
            suspend operator fun invoke(body: CreatePetRequest): Response
            data class Response(val value: Pet)
        }

        fun petId(petId: String): PetId

        interface PetId {
            val get: Get
            val delete: Delete

            interface Get {
                suspend operator fun invoke(): Response
                data class Response(val value: Pet)
            }

            interface Delete {
                suspend operator fun invoke(): Unit
            }

            val toys: Toys

            interface Toys {
                val get: Get

                interface Get {
                    suspend operator fun invoke(): Response
                    data class Response(val value: List<Toy>)
                }
            }
        }
    }
}
```

Usage:

```kotlin
val store = PetStoreClient("https://api.example.com")

store.pets.get(limit = 10)
store.pets.post(CreatePetRequest("Rex"))
store.pets.petId("123").get()
store.pets.petId("123").delete()
store.pets.petId("123").toys.get()
```
