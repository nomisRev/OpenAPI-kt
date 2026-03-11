# Ktor Client Generation Specification

## Overview

This document specifies how the OpenAPI-to-Kotlin generator produces a typed Ktor HTTP client from
an OpenAPI specification. The generated client consists of:

1. **Nested interfaces** — a tree of interfaces that mirrors the API's path structure
2. **Suspend functions** — one per operation, with typed parameters and return types
3. **Implementation classes** — `internal` classes backed by Ktor's `HttpClient`
4. **A factory function** — a top-level constructor that wires everything together

The client is **multiplatform** (KMP) and targets the same platforms as the model layer.

---

## 1. Interface Hierarchy

Operations are grouped by path segments (excluding path parameters) into a nested interface tree.
The root interface is named after `info.title` (or a user-supplied name).

### Rule

> Path segments stripped of `{...}` parameters become interface names (PascalCase).
> Operations that live at a path's "leaf" become suspend functions on that interface.

### Example — OpenAI API

```
POST /chat/completions    operationId: createChatCompletion
GET  /models              operationId: listModels
GET  /models/{model}      operationId: retrieveModel
DELETE /models/{model}    operationId: deleteModel
POST /fine_tuning/jobs    operationId: createFineTuningJob
GET  /fine_tuning/jobs/{fine_tuning_job_id}/events  operationId: listFineTuningEvents
```

Generates:

```kotlin
interface OpenAI {
    val chat: Chat
    val models: Models
    val fineTuning: FineTuning
}

interface Chat {
    val completions: Completions

    interface Completions {
        suspend fun createChatCompletion(body: CreateChatCompletionRequest): CreateChatCompletionResponse
    }
}

interface Models {
    suspend fun listModels(): ListModelsResponse
    suspend fun retrieveModel(model: String): Model
    suspend fun deleteModel(model: String): DeleteModelResponse
}

interface FineTuning {
    val jobs: Jobs

    interface Jobs {
        val events: Events

        suspend fun createFineTuningJob(body: CreateFineTuningJobRequest): FineTuningJob

        interface Events {
            suspend fun listFineTuningEvents(fineTuningJobId: String): ListFineTuningEventsResponse
        }
    }
}
```

Usage:

```kotlin
val client: OpenAI = OpenAIClient(baseUrl = "https://api.openai.com/v1") {
    install(Auth) { bearer { loadTokens { BearerTokens(apiKey, "") } } }
}

val response = client.chat.completions.createChatCompletion(body)
val model    = client.models.retrieveModel("gpt-4")
```

### Naming

| Source           | Rule                                                        |
|------------------|-------------------------------------------------------------|
| Interface name   | Path segment → PascalCase (`fine_tuning` → `FineTuning`)   |
| Property name    | Path segment → camelCase (`fineTuning`)                     |
| Function name    | `operationId` as-is (already camelCase by OpenAPI contract) |

Operations at the root (path = `/` or path has no segments after stripping params) are placed
directly on the root interface.

---

## 2. Function Signatures

Each operation maps to a single `suspend fun` with the following parameter order:

```
1. Path parameters       (always required, in path-declaration order)
2. Required query params (alphabetical)
3. Required header params
4. Required body         (if body is required)
5. Optional query params (alphabetical, default = null)
6. Optional header params (alphabetical, default = null)
7. Optional body         (if body is optional, default = null)
```

Cookie parameters are treated as header parameters for generation purposes.

### 2.1 Path Parameters

Path parameters become plain positional arguments. They are **always required** (OpenAPI enforces
this).

```kotlin
// Path: /models/{model}
suspend fun retrieveModel(model: String): Model
```

Parameter names are derived from the OpenAPI `name` field, converted to camelCase:

| OpenAPI name     | Kotlin param name  |
|------------------|--------------------|
| `model`          | `model`            |
| `fine_tuning_job_id` | `fineTuningJobId` |
| `userId`         | `userId`           |

### 2.2 Query Parameters

Query parameters become named arguments.

```kotlin
suspend fun listModels(
    limit: Int? = null,
    after: String? = null,
): ListModelsResponse
```

When a query parameter has a **non-null default** in the OpenAPI spec, that default is used: 

```kotlin
suspend fun listModels(
    limit: Int = 20,
    after: String? = null,
): ListModelsResponse
```

### 2.3 Header Parameters

Header parameters follow the same rules as query parameters. Names are converted to camelCase.

```kotlin
suspend fun createChatCompletion(
    body: CreateChatCompletionRequest,
    openAIOrganization: String? = null,  // header: OpenAI-Organization
): CreateChatCompletionResponse
```

### 2.4 Request Body

The request body type comes from the schema of the preferred content type, resolved in this order:

1. `application/json`
2. `application/xml`
3. `application/octet-stream`
4. `text/plain`
5. `multipart/form-data` (see §2.4.2)
6. `application/x-www-form-urlencoded` (see §2.4.3)

#### 2.4.1 JSON / Octet-stream / Plain body

The body is a single typed parameter named `body`:

```kotlin
// required body
suspend fun createChatCompletion(body: CreateChatCompletionRequest): CreateChatCompletionResponse

// optional body (requestBody.required = false)
suspend fun updateSettings(body: UpdateSettingsRequest? = null): UpdateSettingsResponse
```

#### 2.4.2 Multipart Form Data

Each property of the multipart schema becomes its own parameter.
If the schema is a `$ref` to a top-level model, a single `body` parameter is used instead.

```kotlin
// Inline schema (properties expanded into params)
suspend fun uploadFile(
    file: ByteArray,
    purpose: String,
): FileObject

// $ref schema (single body param)
suspend fun uploadFile(body: UploadFileRequest): FileObject
```

#### 2.4.3 Form URL Encoded

Identical to multipart: each property becomes its own parameter.

```kotlin
suspend fun createToken(
    grantType: String,
    code: String,
    redirectUri: String? = null,
): TokenResponse
```

#### 2.5 @Required

Mark parameters as `@Required` when they are `required` but have a default value.
When deserializing we need to guarantee its in the payload.

---

## 3. Return Types

The guiding principle is **no surprises** — every documented response the server can send is
represented as a type, not an exception. The only remaining source of exceptions is a response
with a status code that is completely undocumented *and* not covered by `default`.

### 3.1 Deciding when to use a sealed type

| Condition                                                 | Return type                  |
|-----------------------------------------------------------|------------------------------|
| Exactly one response, no `default`                        | Body type directly           |
| More than one response, OR a `default` is present         | Sealed result type           |

**Direct return (simple case):**

```kotlin
// Spec: only 200 defined, no default
suspend fun deleteModel(model: String): Unit
suspend fun retrieveModel(model: String): Model
```

**Sealed return (any documented variation):**

```kotlin
interface Api {
    // Spec: 200 + 404 + default
    sealed interface RetrieveModelResult {
        data class OK(val value: Model) : RetrieveModelResult               // 200
        data object BadRequest : RetrieveModelResult                        // 400
        data class NotFound(val value: Error) : RetrieveModelResult         // 404
        data class Default(val status: HttpStatusCode, val value: Error) : RetrieveModelResult  // default
    }

    suspend fun retrieveModel(model: String): RetrieveModelResult
}
```

The sealed interface is always nested directly inside the interface that declares the function.
Use `data object` instead of `data class` if the response is empty.

### 3.2 Sealed case naming

| Status code          | Case name                                               |
|----------------------|---------------------------------------------------------|
| `200`                | `OK`                                                    |
| `201`                | `Created`                                               |
| `204`                | `NoContent` (body type is `Unit`)                       |
| Any other code       | HTTP reason phrase in PascalCase (`BadRequest`, `NotFound`, `UnprocessableEntity`, …) |
| OpenAPI `default`    | `Default(val status: HttpStatusCode, val value: <T>)`   |

### 3.3 The `default` response

Per the OpenAPI specification, `default` is a **catch-all** for any status code not explicitly
listed — it is not restricted to errors. In practice it is often used for error envelopes, but a
`default` response with a 2xx payload is equally valid.

The `default` case always carries the actual `HttpStatusCode` alongside its typed body so callers
can inspect the code when needed:

```kotlin
data class Default(val status: HttpStatusCode, val value: Error) : RetrieveModelResult
```

### 3.4 Undocumented responses (no default)

If the server returns a status code that is neither explicitly listed nor covered by `default`,
there is no type to decode into. In this case a `ResponseException` (Ktor) is thrown. This is the
**only** remaining exception surface in the generated client.

### 3.5 Multiple content types for the same status code

When a single status code offers multiple content types, we must pick **one** schema to generate
the Kotlin return type from. Unlike requests (where the client controls what it sends), the client
does not control what content type the server responds with at runtime — but we still need a single
static type at code-generation time.

The **most structured / richest** content type is preferred, in this order:

1. `application/json` — fully typed, self-describing, best tooling
2. `application/xml`  — structured but less common in KMP
3. `text/plain`       — unstructured string; only if nothing better is available
4. `application/octet-stream` / `*/*` — raw bytes; last resort

All other content types for that status code are ignored. If there is a genuine need to handle
multiple content types at runtime (e.g., a REST endpoint that can return either JSON or binary),
that is out of scope for now and should be handled by dropping down to the raw `HttpResponse`.

---

## 4. Generated Implementation

### 4.1 Implementation Classes

For every generated interface an `internal` implementation class is produced in the same package:

```kotlin
internal class OpenAIImpl(
    private val client: HttpClient,
    private val baseUrl: String,
) : OpenAI {
    override val chat: Chat = ChatImpl(client, baseUrl)
    override val models: Models = ModelsImpl(client, baseUrl)
    override val fineTuning: FineTuning = FineTuningImpl(client, baseUrl)
}

internal class ModelsImpl(
    private val client: HttpClient,
    private val baseUrl: String,
) : Models {

    // Single 200, no default → body type returned directly
    override suspend fun listModels(
        limit: Int? = null,
        after: String? = null,
    ): ListModelsResponse =
        client.get("$baseUrl/models") {
            limit?.let { parameter("limit", it) }
            after?.let { parameter("after", it) }
        }.body()

    // Single 200, no default → body type returned directly
    override suspend fun deleteModel(model: String): Unit =
        client.delete("$baseUrl/models/$model").body()

    // 200 + 404 + default → sealed result type
    override suspend fun retrieveModel(model: String): Models.RetrieveModelResult {
        val response = client.get("$baseUrl/models/$model")
        return when (response.status) {
            HttpStatusCode.OK       -> Models.RetrieveModelResult.OK(response.body())
            HttpStatusCode.NotFound -> Models.RetrieveModelResult.NotFound(response.body())
            else                    -> Models.RetrieveModelResult.Default(response.status, response.body())
        }
    }
}
```

### 4.2 HTTP Method Mapping

| OpenAPI method | Ktor function |
|----------------|---------------|
| `GET`          | `client.get`  |
| `POST`         | `client.post` |
| `PUT`          | `client.put`  |
| `PATCH`        | `client.patch`|
| `DELETE`       | `client.delete`|
| `HEAD`         | `client.head` |
| `OPTIONS`      | `client.options`|

### 4.3 Server URL

The OpenAPI `servers` block is the source of truth for base URLs. A typed `<ApiName>Server` sealed
interface is generated in the root interface file and passed to the factory function instead of a
raw `String`.

#### Generation rules

| Spec condition                         | Generated case kind                                     |
|----------------------------------------|---------------------------------------------------------|
| Server has no `variables`              | `data object`                                           |
| Server has variables (all have `enum`) | `data class` with enum params                           |
| Server has variables (some free-form)  | `data class` with `String` params                       |
| Always                                 | `data class Custom(override val url: String)` escape hatch |

#### Case naming

The case name is derived from the server's `description` field (converted to PascalCase, trailing
word "server" stripped). When no description is present, the name falls back to `Default` for a
single server, or `Server1`, `Server2`, … for multiple.

#### Example — simple servers (no variables)

```yaml
servers:
  - url: https://api.openai.com/v1
    description: Production
  - url: https://staging.api.openai.com/v1
    description: Staging
```

```kotlin
sealed interface OpenAIServer {
    val url: String

    data object Production : OpenAIServer {
        override val url = "https://api.openai.com/v1"
    }

    data object Staging : OpenAIServer {
        override val url = "https://staging.api.openai.com/v1"
    }

    /** Escape hatch for proxies, local dev, custom deployments, etc. */
    data class Custom(override val url: String) : OpenAIServer
}
```

#### Example — server with variables

```yaml
servers:
  - url: https://{environment}.api.example.com/{version}
    description: Multi-environment
    variables:
      environment:
        default: production
        enum: [production, staging, dev]
      version:
        default: v2
```

```kotlin
sealed interface ExampleServer {
    val url: String

    data class MultiEnvironment(
        val environment: Environment = Environment.Production,
        val version: String = "v2",             // free-form: plain String with default
    ) : ExampleServer {
        override val url: String
            get() = "https://${environment.value}.api.example.com/$version"

        enum class Environment(val value: String) {
            Production("production"),
            Staging("staging"),
            Dev("dev"),
        }
    }

    data class Custom(override val url: String) : ExampleServer
}
```

#### Example — no servers in spec

When the spec declares no servers, the factory requires an explicit `baseUrl` with no default:

```kotlin
fun ExampleClient(
    baseUrl: String,                             // no default — caller must supply
    block: HttpClientConfig<*>.() -> Unit = {},
): Example
```

#### Factory function signature (with servers)

```kotlin
fun OpenAIClient(
    server: OpenAIServer = OpenAIServer.Production,  // first documented server is the default
    block: HttpClientConfig<*>.() -> Unit = {},
): OpenAI {
    val client = HttpClient {
        install(ContentNegotiation) { json() }
        block()
    }
    return OpenAIImpl(client, server.url)
}
```

The `server.url` string is resolved once at construction time and stored as `baseUrl` in the
implementation classes. Per-path and per-operation server overrides (allowed by the OpenAPI spec)
are **not** supported; only global servers are used.

### 4.4 Parameter Placement in the Request

| Parameter location | Ktor call          |
|--------------------|--------------------|
| Path               | `path("models", "$model")` |
| Query (required)   | `parameter("name", value)` |
| Query (optional)   | `value?.let { parameter("name", it) }` |
| Header (required)  | `header("Name", value)` |
| Header (optional)  | `value?.let { header("Name", it) }` |
| Cookie             | `cookie("name", value)` |

### 4.5 Request Body Placement

```kotlin
// JSON body
client.post("/chat/completions") {
    contentType(ContentType.Application.Json)
    setBody(body)
}.body<CreateChatCompletionResponse>()

// Multipart (expanded params)
client.post("$baseUrl/files") {
    setBody(
        MultiPartFormDataContent(
            formData {
                append("purpose", purpose)
                appendInput("file", headers = Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=upload")
                }) { ByteReadChannel(file) }
            }
        )
    )
}.body<FileObject>()

// Form URL encoded
client.post("/oauth/token") {
    contentType(ContentType.Application.FormUrlEncoded)
    setBody(
        Parameters.build {
            append("grant_type", grantType)
            append("code", code)
            redirectUri?.let { append("redirect_uri", it) }
        }.formUrlEncode()
    )
}.body<TokenResponse>()
```

---

## 5. Factory Function

A single top-level factory function creates a fully wired client. It accepts a
`HttpClientConfig` lambda so callers can install plugins (auth, logging, etc.) without
losing type safety.

```kotlin
fun OpenAIClient(
    baseUrl: String = "https://api.openai.com/v1",
    httpClient: HttpClient? = null,
    block: HttpClientConfig<*>.() -> Unit = {},
): OpenAI {
    val client = (httpClient ?: HttpClient()).config {
        install(ContentNegotiation) { json() }
        block()
    }
    return OpenAIImpl(client, baseUrl)
}
```

The `ContentNegotiation` is auto installed based on the used content types.
Either 1st party KotlinX Serialization format integration will be used, or the most popular 1st party one. 

### Required Ktor plugins (auto-installed)

| Plugin              | Reason                                 |
|---------------------|----------------------------------------|
| `ContentNegotiation` | `application/json` body encode/decode |

### Recommended but not auto-installed

| Plugin   | Reason                                   |
|----------|------------------------------------------|
| `Auth`   | Bearer / Basic / API-key authentication  |
| `Logging`| Request/response logging for debugging   |

---

## 6. File Layout

Generated files live under `<outputDir>/<packageName>/api/`. Each **direct child** of the root
interface gets its own file. Deeper nesting is expressed as inner interfaces within that file.

```
<outputDir>/
  <pkg>/
    model/           ← already generated (data classes, enums, unions)
    api/
      OpenAI.kt      ← root interface + OpenAIImpl + factory function
      Chat.kt        ← Chat interface (+ nested Completions) + ChatImpl
      Models.kt      ← Models interface (all operations + sealed result types) + ModelsImpl
      FineTuning.kt  ← FineTuning interface + nested Jobs + nested Jobs.Events + impls
      ...
```

Each file contains one **top-level interface**, all its **nested sub-interfaces**, all their
**sealed result types**, and the corresponding **implementation classes**. The factory function
lives in the root interface file.

---

## 7. Deprecated Operations

Operations with `deprecated: true` are annotated with `@Deprecated`:

```kotlin
@Deprecated("Deprecated by the API provider")
suspend fun legacyEndpoint(id: String): LegacyResponse
```

---

## 8. Type Mapping

This section documents the full decision tree from OpenAPI schema to Kotlin type. The logic lives
in `transformers/SchemaTransformer.kt` and friends.

### 8.1 Primitives

Format is the secondary key when the type alone is ambiguous. Unrecognised formats fall through to
the default for that type.

| OpenAPI `type` | OpenAPI `format` | Kotlin type                     |
|----------------|------------------|---------------------------------|
| `integer`      | `int32`          | `Int`                           |
| `integer`      | *(any other)*    | `Long`                          |
| `number`       | `float`          | `Float`                         |
| `number`       | *(any other)*    | `Double`                        |
| `boolean`      | —                | `Boolean`                       |
| `string`       | —                | `String`                        |
| `string`       | `binary`         | `ByteArray`                     |
| `string`       | `uuid`           | `kotlin.uuid.Uuid`              |
| `string`       | `date`           | `kotlinx.datetime.LocalDate`    |
| `string`       | `date-time`      | `kotlinx.datetime.LocalDateTime`|

### 8.2 Nullability

Three equivalent ways to express a nullable type in OpenAPI all collapse to the same `?` suffix:

| OpenAPI construct                                | Kotlin result  |
|--------------------------------------------------|----------------|
| `nullable: true`                                 | `T?`           |
| `type: [T, null]`                                | `T?`           |
| `oneOf: [{ type: null }, <T>]` / `anyOf: [...]`  | `T?`           |

The null case is stripped from the type list and the underlying type is marked `isNullable = true`.

### 8.3 Named References (`$ref`)

`$ref` → `#/components/schemas/<Name>` references are resolved lazily. Usage sites become a
`Model.Reference` pointing at the named type; the named type itself is generated separately in
`model/`.

When a component schema is itself a primitive (e.g., a named `string` alias), it is wrapped in an
object with a single `value` property so it can carry a distinct Kotlin type:

```yaml
# components/schemas/UserId:
#   type: string
#   format: uuid
```

```kotlin
// Generated in model/
data class UserId(val value: Uuid)
```

### 8.4 Objects

The decision tree for `type: object`:

```
properties non-empty?
  yes → data class with typed properties (§8.4.1)
  no  → look at additionalProperties:
          additionalProperties: true (or absent) → JsonObject          (free-form)
          additionalProperties: false             → Unit / empty object (no-content marker)
          additionalProperties: { schema }        → Map<String, T>     (typed dictionary)
```

#### 8.4.1 Object with properties

Each property becomes a constructor parameter. Required properties (in `required: [...]`) are
non-nullable; optional properties are nullable with `= null`:

```kotlin
data class User(
    val id: Uuid,           // required
    val name: String,       // required
    val email: String? = null,  // optional
)
```

#### 8.4.2 Object with `additionalProperties: { schema }`

An object with no declared properties but a typed `additionalProperties` schema maps to a typed
map:

```yaml
additionalProperties:
  type: string
```

```kotlin
// Inline usage → Map<String, String>
// Named component → data class with a `values: Map<String, String>` property
```

#### 8.4.3 Free-form object (`additionalProperties: true`)

No schema constraints → `JsonObject` (KotlinX Serialization).

#### 8.4.4 Object with both properties and `additionalProperties: { schema }`

Generates a `data class` where known properties are explicit fields, and extra keys are captured
in a `Map<String, T>` field. A custom serializer is generated to handle the overlap (splitting
known keys from the spill-over map on deserialize; merging them back on serialize).

### 8.5 Collections

`type: array` with an `items` schema → `List<T>` where `T` is the items type, resolved
recursively through the same mapping. Nested arrays become `List<List<T>>`.

`items` being a `$ref` → `List<RefType>`.

If `items` is a free-form schema → `JsonArray`.

### 8.6 Closed Enums

`enum: [...]` on any scalar type → `enum class`:

```yaml
type: string
enum: [asc, desc]
```

```kotlin
enum class SortOrder {
    @SerialName("asc")  Asc,
    @SerialName("desc") Desc,
}
```

Null values in the enum list (`enum: [asc, desc, null]`) are stripped from the case list; the
generated type becomes nullable instead.

### 8.7 Unions (`oneOf` / `anyOf`)

Both keywords are treated identically — both express "one of these schemas".

| Situation                      | Result                                      |
|--------------------------------|---------------------------------------------|
| All cases are `null` + one T   | Nullable T (see §8.2)                       |
| Single case                    | That case's type directly (no wrapper)      |
| String enum + plain string     | **Open enum** (see §8.7.1)                  |
| Anything else                  | `sealed interface` with one case per schema |

Generated sealed interface cases:

| Case model               | Generated as                                     |
|--------------------------|--------------------------------------------------|
| `$ref` to named type     | `@JvmInline value class Case<Name>(val value: T)`|
| Object (inline)          | `data class` nested inside the sealed interface  |
| Enum (inline)            | `enum class` nested inside the sealed interface  |
| `Unit` (empty object)    | `data object Empty`                              |
| Primitive / scalar       | `@JvmInline value class Case<Type>(val value: T)`|

A custom `KSerializer` is generated for each union that tries each case in a well-defined order
(objects with more properties first; enums before strings; narrower numerics before wider; String
before FreeFormJson last). See `UnionRenderer.kt` and `unionCaseComparator` for the full ordering.

#### 8.7.1 Open Enum

When a union has **exactly two cases** — one `string` enum and one plain `string` — it represents
an extensible enum (known values + unknown future values):

```yaml
oneOf:
  - type: string
    enum: [asc, desc]
  - type: string
```

```kotlin
@Serializable(with = SortOrder.Serializer::class)
sealed interface SortOrder {
    enum class AscOrDesc : SortOrder { Asc, Desc }
    @JvmInline value class CaseString(val value: String) : SortOrder

    object Serializer : KSerializer<SortOrder> { ... }
}
```

### 8.8 `allOf` — Structural Merging

`allOf` schemas are **merged** into a single flat type by recursively combining their properties,
constraints, and nullability. No inheritance is generated; the result is a single `data class`
with all properties from all member schemas.

```yaml
allOf:
  - $ref: '#/components/schemas/Base'
  - properties:
      extra: { type: string }
```

Becomes a single `data class` with the properties of `Base` plus `extra`.

**Exception:** when `allOf` is combined with a `discriminator` on a parent schema, the result is
a `DiscriminatedObject` instead (see §8.9).

### 8.9 Discriminated Objects (`allOf` + `discriminator`)

When a parent schema carries a `discriminator.mapping` and subtypes use `allOf` to extend it, the
hierarchy is modelled as a `Model.DiscriminatedObject`:

- The **parent** becomes a `sealed interface` annotated with `@JsonClassDiscriminator`
- Each **subtype** becomes a `data class` implementing the sealed interface
- The discriminator property is included in the abstract properties of the parent

```yaml
# Parent
Animal:
  discriminator:
    propertyName: type
    mapping:
      dog: '#/components/schemas/Dog'
      cat: '#/components/schemas/Cat'

# Subtype
Dog:
  allOf:
    - $ref: '#/components/schemas/Animal'
    - properties:
        breed: { type: string }
```

```kotlin
@JsonClassDiscriminator("type")
@Serializable
sealed interface Animal {
    val type: String
}

@SerialName("dog")
@Serializable
data class Dog(override val type: String, val breed: String?) : Animal

@SerialName("cat")
@Serializable
data class Cat(override val type: String, val indoor: Boolean?) : Animal
```

### 8.10 `readOnly` / `writeOnly` — Request vs Response Split

When a component schema contains any property marked `readOnly` or `writeOnly` (directly or in any
nested schema), **two separate types** are generated:

| Generated type     | Suffix     | Excludes            |
|--------------------|------------|---------------------|
| Request (write)    | `Request`  | `readOnly` properties  |
| Response (read)    | `Response` | `writeOnly` properties |

If a schema has no `readOnly`/`writeOnly` anywhere in its tree, a single type is generated with
no suffix.

```yaml
# components/schemas/User
User:
  properties:
    id:     { type: string, readOnly: true }   # server-assigned, not in requests
    name:   { type: string }
    secret: { type: string, writeOnly: true }  # only in requests, never in responses
```

```kotlin
data class UserRequest(val name: String, val secret: String)
data class UserResponse(val id: String, val name: String)
```

### 8.11 Recursive Schemas

Self-referencing schemas (via `$ref: '#'` or a cyclic component reference) are handled by
detecting the recursion cycle and emitting a `Model.Reference` back to the containing type's
`NamingContext` instead of expanding infinitely.

### 8.12 Constraints

Numeric, string, collection, and object constraints are parsed and attached to the `Model` as
`Constraints.*` objects:

| Constraint class       | Fields                                             |
|------------------------|----------------------------------------------------|
| `Constraints.Number`   | `minimum`, `maximum`, `exclusiveMin/Max`, `multipleOf` |
| `Constraints.Text`     | `minLength`, `maxLength`, `pattern`                |
| `Constraints.Collection` | `minItems`, `maxItems`, `uniqueItems`            |
| `Constraints.Object`   | `minProperties`, `maxProperties`                  |

Constraints are **stored on the model but not enforced at compile time** in the current
implementation — they are carried for potential use by validation layers or documentation
generators.

---

## 9. Summary of Design Decisions

| Decision                          | Choice                                                                        |
|-----------------------------------|-------------------------------------------------------------------------------|
| API structure                     | Nested interfaces mirroring path segments; deeper nesting = inner interfaces  |
| Single response, no `default`     | Return body type directly                                                     |
| Multiple responses OR `default`   | Sealed result type nested inside the declaring interface                      |
| `default` response                | Catch-all for any undocumented code; `Default(status, value)` case in sealed  |
| Undocumented + no `default`       | Throw `ResponseException` (only remaining exception surface)                  |
| Optional params                   | Nullable with `= null` default                                                |
| Param order                       | Path → required body → required query/header → optional                       |
| Multipart inline vs ref           | Inline → expanded params; `$ref` → single `body` param                       |
| Factory                           | Top-level function, installs `ContentNegotiation(json())`                     |
| ContentType preference            | json > xml > octet-stream > plain > multipart > form                          |
| Naming                            | `operationId` for functions, PascalCase path segments for interfaces          |
