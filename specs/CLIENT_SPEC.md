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

## 1. Class Structure

### Root Class

Name of top-level client is user provided `generate("GitHub", spec.json)`. It is emitted as a public class with an `internal` constructor. It contains:
- Method nodes for operations at `/` (root-level operations)
- `val` properties for top-level path segments

```kotlin
class GitHub internal constructor(
    private val client: HttpClient,
) {
    // Root-level operation: GET /
    val get: Get = Get(client)

    class Get internal constructor(
        private val client: HttpClient,
    ) {
        suspend operator fun invoke(): Unit
    }

    // Navigation to /repos
    val repos: Repos = Repos(client)

    // Navigation to /users
    val users: Users = Users(client)
}
```

### Nested Classes (Static Segments)

Each static path segment generates a nested class inside its parent. Accessed via `val` property.

```kotlin
class GitHub internal constructor(
    private val client: HttpClient,
) {
    val repos: Repos = Repos(client)

    class Repos internal constructor(
        private val client: HttpClient,
    ) {
        // Navigation to /repos/{owner}
        fun owner(owner: String): Owner = Owner(client, owner)

        class Owner internal constructor(
            private val client: HttpClient,
            private val owner: String,
        ) {
            // Navigation to /repos/{owner}/{repo}
            fun repo(repo: String): Repo = Repo(client, owner, repo)

            class Repo internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
            ) {
                val get: Get = Get(client, owner, repo)
                val delete: Delete = Delete(client, owner, repo)
                val patch: Patch = Patch(client, owner, repo)

                class Get internal constructor(
                    private val client: HttpClient,
                    private val owner: String,
                    private val repo: String,
                ) {
                    suspend operator fun invoke(): Response
                    data class Response(val value: Repository)
                }

                class Delete internal constructor(
                    private val client: HttpClient,
                    private val owner: String,
                    private val repo: String,
                ) {
                    suspend operator fun invoke(): Unit
                }

                class Patch internal constructor(
                    private val client: HttpClient,
                    private val owner: String,
                    private val repo: String,
                ) {
                    suspend operator fun invoke(body: UpdateRepoRequest): Response
                    data class Response(val value: Repository)
                }

                val collaborators: Collaborators = Collaborators(client, owner, repo)

                class Collaborators internal constructor(
                    private val client: HttpClient,
                    private val owner: String,
                    private val repo: String,
                ) {
                    val get: Get = Get(client, owner, repo)

                    class Get internal constructor(
                        private val client: HttpClient,
                        private val owner: String,
                        private val repo: String,
                    ) {
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

Parameter segments generate **non-suspend** functions that return the next class. The function name and parameter name both use the OpenAPI placeholder name directly.

```kotlin
// For path segment {owner}
fun owner(owner: String): Owner

// For path segment {pet_id} (camelCase conversion applied to parameter name)
fun petId(petId: String): PetId
```

The parameter type comes from the OpenAPI parameter definition (resolved to `Model`).

### Type Naming

- Static segments: PascalCase of the segment name (e.g., `repos` -> `Repos`, `fine_tuning` -> `FineTuning`)
- Parameter segments: PascalCase of the placeholder name (e.g., `{owner}` -> `Owner`, `{pet_id}` -> `PetId`)
- Method nodes: PascalCase HTTP method (e.g., `get` -> `Get`, `post` -> `Post`)
- Generated client classes stay nested inside their parent, except for the root class and direct child files
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

Multiple methods at the same path produce multiple properties on the same path class.

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
- Method-node class
- `invoke(...)` signature
- The generated method implementation

---

## 3. Implementation Pattern

### Concrete Client Tree

Each path node is emitted directly as a concrete class. Constructors are `internal`, store `HttpClient` plus accumulated path parameters, and method-node properties instantiate nested method classes directly.

```kotlin
class Repo internal constructor(
    private val client: HttpClient,
    private val owner: String,
    private val repo: String,
) {
    val get: Get = Get(client, owner, repo)
    val delete: Delete = Delete(client, owner, repo)

    class Get internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
    ) {
        suspend operator fun invoke(): Response {
            val value: Repository = client.get("/repos/$owner/$repo").body()
            return Response(value)
        }

        data class Response(val value: Repository)
    }

    class Delete internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
    ) {
        suspend operator fun invoke() {
            client.delete("/repos/$owner/$repo")
        }
    }
}
```

Key points:
- Client tree classes store `HttpClient` + accumulated path parameters
- Parameter navigation functions create child class instances with captured values
- Method-node properties instantiate nested method classes directly
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
    return GitHub(client)
}
```

---

## 4. Query Parameters

The following snippets focus on method signatures and inline types. In generated output, the enclosing client tree nodes are concrete classes with `internal` constructors, even when the abbreviated examples below omit that constructor boilerplate.

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

Query/header parameter inline enums are generated on the **path node class** (not method node).

```kotlin
class Collaborators internal constructor(
    private val client: HttpClient,
) {
    @Serializable
    enum class Affiliation {
        @SerialName("outside") Outside,
        @SerialName("direct") Direct,
        @SerialName("all") All;
    }

    val get: Get = Get(client)

    class Get internal constructor(
        private val client: HttpClient,
    ) {
        suspend operator fun invoke(affiliation: Affiliation = Affiliation.All): Response
        data class Response(val value: List<Collaborator>)
    }
}
```

---

## 10. File Organization

The root class and its nested classes can be split across files. The splitting strategy:

- **Root file** (`GitHub.kt`): root class + factory + server sealed interface
- **Direct children** get their own files: `Repos.kt`, `Users.kt`, `Orgs.kt`
- **Deeper nesting** stays as nested classes within the direct child file

```text
GitHub.kt           -> GitHub class, factory, server
Repos.kt            -> Repos + all nested (Owner, Repo, Collaborators, etc.)
Users.kt            -> Users + all nested
```

All files share the same package.

---

## 11. Naming Conventions

| Source | Convention | Example |
|--------|-----------|---------|
| Client class names | PascalCase from segment | `repos` -> `Repos`, `fine_tuning` -> `FineTuning` |
| Navigation function names | camelCase from placeholder | `{owner}` -> `owner()`, `{pet_id}` -> `petId()` |
| Navigation function param names | camelCase from placeholder | `{owner}` -> `owner: String`, `{pet_id}` -> `petId: String` |
| Method-node property names | Lowercase HTTP method | `get`, `post`, `delete` |
| Method-node class names | PascalCase HTTP method | `Get`, `Post`, `Delete` |
| Invoke entrypoint | `operator fun invoke` | `get(limit = 10)` |
| Query/header param names | camelCase | `per_page` -> `perPage`, `X-Api-Key` -> `xApiKey` |
| Response wrapper name | `Response` | `Get.Response`, `Post.Response` |
| Factory function names | `{RootClass}Client` | `GitHubClient()` |
| Server sealed interface | `{RootClass}Server` | `GitHubServer` |

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
class PetStore internal constructor(
    private val client: HttpClient,
) {
    val pets: Pets = Pets(client)

    class Pets internal constructor(
        private val client: HttpClient,
    ) {
        val get: Get = Get(client)
        val post: Post = Post(client)

        class Get internal constructor(
            private val client: HttpClient,
        ) {
            suspend operator fun invoke(limit: Int? = null): Response
            data class Response(val value: List<Pet>)
        }

        class Post internal constructor(
            private val client: HttpClient,
        ) {
            suspend operator fun invoke(body: CreatePetRequest): Response
            data class Response(val value: Pet)
        }

        fun petId(petId: String): PetId = PetId(client, petId)

        class PetId internal constructor(
            private val client: HttpClient,
            private val petId: String,
        ) {
            val get: Get = Get(client, petId)
            val delete: Delete = Delete(client, petId)

            class Get internal constructor(
                private val client: HttpClient,
                private val petId: String,
            ) {
                suspend operator fun invoke(): Response
                data class Response(val value: Pet)
            }

            class Delete internal constructor(
                private val client: HttpClient,
                private val petId: String,
            ) {
                suspend operator fun invoke(): Unit
            }

            val toys: Toys = Toys(client, petId)

            class Toys internal constructor(
                private val client: HttpClient,
                private val petId: String,
            ) {
                val get: Get = Get(client, petId)

                class Get internal constructor(
                    private val client: HttpClient,
                    private val petId: String,
                ) {
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
