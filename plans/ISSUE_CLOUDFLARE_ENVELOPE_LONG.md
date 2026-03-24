# ISSUE: Config-Driven Response Envelope Detection & Per-Operation Sealed Hierarchies

## Problem Statement

APIs like Cloudflare use a **response envelope pattern** where every API response is wrapped in a standard structure:

```json
{
  "success": true,
  "errors": [],
  "messages": [...],
  "result": { /* actual payload */ }
}
```

In the OpenAPI spec, this is modeled as a base schema extended via `allOf`:

```json
"allOf": [
  { "$ref": "#/components/schemas/api-response-common" },
  { "properties": {
      "errors": { "enum": [[]] },
      "result": { "$ref": "#/components/schemas/ManagedTransforms" },
      "success": { "enum": [true] }
  }}
]
```

The `success: enum [true]` and `errors: enum [[]]` are **singleton types** — they carry zero information for the consumer. `enum: [true]` doesn't mean "a boolean that defaults to true", it means "the type whose only inhabitant is `true`". Kotlin has no literal types, so we need to decide how to represent this.

Currently this fails at two levels:
1. **Parser**: `Schema.enum` is `List<String?>` — can't parse `enum: [true]`, `enum: [[]]`, or integer enums like `enum: [301, 302]`
2. **Typed/Renderer**: allOf merge produces a flat data class with all properties — no envelope awareness, no const-property stripping

---

## Investigation: Cloudflare Spec Analysis

### Non-String Enum Values

The Cloudflare spec (`parser/src/commonTest/resources/specs/cloudflare.json`) contains **421 schemas with non-string enum values**:

| Category | Count | Examples |
|----------|-------|----------|
| Single-value boolean (`enum: [true]`) | 184 | `success` fields on success envelopes |
| Single-value boolean (`enum: [false]`) | 183 | `success` fields on failure envelopes |
| Single-value empty array (`enum: [[]]`) | 8 | `errors` fields narrowed to always-empty |
| Single-value integer | 24 | Error codes like `enum: [10002]` |
| Multi-value integer | 22 | HTTP status codes `enum: [301, 302, 307, 308]`, TTL values `enum: [30, 60, 300, ...]` |
| Identity boolean (`enum: [true, false]`) | 1 | No-op constraint |

The `enum: [[]]` values appear exclusively in `components.responses` (not `components.schemas`):
- `rulesets_ManagedTransforms`, `rulesets_Ruleset`, `rulesets_Rulesets`, `rulesets_UrlNormalization`
- `snippets_Null`, `snippets_Snippet`, `snippets_SnippetRules`, `snippets_Snippets`

### Envelope Base Schemas

**90 envelope base schemas** are used as `allOf[0]` targets. They fall into **20 distinct structural variants**:

#### Dominant shape (42 schemas): `{errors, messages, success}` with `success: enum [true]`

```json
{
  "type": "object",
  "required": ["success", "errors", "messages"],
  "properties": {
    "success": { "type": "boolean", "enum": [true] },
    "errors":  { "$ref": "#/components/schemas/{namespace}_messages" },
    "messages": { "$ref": "#/components/schemas/{namespace}_messages" }
  }
}
```

Examples: `access_api-response-common`, `workers_api-response-common`, `stream_api-response-common`, etc.

#### Second shape (18 schemas): `{errors, messages, result, success}` with pre-declared `result`

Same as above but `result` already exists in the base (gets overridden by allOf extender).

Examples: `firewall_api-response-common`, `tunnel_api-response-common`, `images_api-response-common`

#### Variant differences across the 20 shapes

| Variation | Description | Impact |
|-----------|-------------|--------|
| **Error/message ref** | Each API namespace has its own `Message` type | Different `List<E>` per envelope |
| **`success` field** | Sometimes `enum: [true]`, sometimes plain `boolean` | Some bases don't guarantee success/failure via enum |
| **`result` pre-existence** | 42 bases don't have `result`; 18 already have it | allOf either adds or overrides result |
| **`required` fields** | Some require all 4, some just `success` | Affects nullability of errors/messages |
| **Error definition style** | `$ref`, inline `items: {$ref}`, or `allOf: [{$ref}]` | Different error type resolution paths |

### What Extenders Add

Overwhelmingly, schemas extending envelope bases add a `result` property:

```
adds ['result']:        ~85% of extensions
adds ['result_info']:   ~10% (pagination metadata)
adds ['result', 'result_info']: ~5%
```

A few edge cases: `stream_api-response-common` extender adds `['range', 'result', 'total']`.

### Failure Schemas

Separate schemas model failure responses (not allOf extensions of the success base):

```json
{
  "properties": {
    "success": { "type": "boolean", "enum": [false] },
    "errors": { "$ref": "#/.../messages", "minLength": 1 },
    "messages": { "$ref": "#/.../messages" },
    "result": { "enum": [null], "nullable": true, "type": "object" }
  },
  "required": ["success", "errors", "messages", "result"]
}
```

Key differences from success:
- `success: enum [false]` (singleton `false`)
- `errors` has `minLength: 1` (non-empty)
- `result: enum [null]` (always null)

### Concrete End-to-End Example

**Endpoint**: `GET /accounts/{account_id}/alerting/v3/available_alerts`

**Operation responses**:
- `200` → `aaa_alerts-response_collection` (success)
- `4XX` → `aaa_components-schemas-api-response-common-failure` (failure)

**Success schema** (`aaa_alerts-response_collection`):
```json
{
  "allOf": [
    { "$ref": "#/components/schemas/aaa_schemas-api-response-common" },
    { "properties": {
        "result": {
          "type": "object",
          "additionalProperties": {
            "items": { "$ref": "#/components/schemas/aaa_alert-types" },
            "type": "array"
          }
        }
    }}
  ]
}
```

**Success base** (`aaa_schemas-api-response-common`):
```json
{
  "type": "object",
  "required": ["success", "errors", "messages"],
  "properties": {
    "success": { "type": "boolean", "enum": [true], "description": "Whether the API call was successful" },
    "errors": { "$ref": "#/components/schemas/aaa_components-schemas-messages" },
    "messages": { "$ref": "#/components/schemas/aaa_components-schemas-messages" }
  }
}
```

**Failure schema** (`aaa_components-schemas-api-response-common-failure`):
```json
{
  "type": "object",
  "required": ["success", "errors", "messages"],
  "properties": {
    "success": { "type": "boolean", "enum": [false] },
    "errors": { "allOf": [{ "$ref": "#/components/schemas/aaa_components-schemas-messages" }], "minLength": 1 },
    "messages": { "allOf": [{ "$ref": "#/components/schemas/aaa_components-schemas-messages" }] }
  }
}
```

**Messages type** (`aaa_components-schemas-messages`):
```json
{
  "type": "array",
  "items": {
    "type": "object",
    "required": ["message"],
    "properties": {
      "code": { "type": "integer", "minimum": 1000 },
      "message": { "type": "string" }
    }
  }
}
```

---

## Design Decisions

### Why per-operation (not generic envelope)

The 90 bases have 20 structural variants with different error/message types, required fields, and property structures. A shared `ApiResponse<T>` would require either:
- Wrong types (using a common supertype that loses specificity)
- Multiple type parameters (`ApiResponse<T, E, M>`) that are cumbersome

Per-operation sealed interfaces are always correct and the types are always precise.

### How type information flows to the generated envelope

Schema resolution happens BEFORE envelope detection:

1. Operation defines `200 → successSchema`, `4XX → failureSchema`
2. Success schema's allOf is merged: base refs resolve to concrete types (e.g., `errors → List<AaaMessage>`)
3. Envelope detection classifies the **already-resolved** properties:
   - `success: enum [true]` → const → **strip** (singleton type, zero information)
   - `errors: enum [[]]` → const → **strip** on success case
   - `result` → **payload** (Success case)
   - `messages` → **shared** (both cases)
4. Failure schema resolves independently with its own concrete types
5. Per-operation sealed interface uses correctly-typed properties from both

### Why const-stripping is scoped to envelope allOf only

Single-value enums appear elsewhere as discriminators (e.g., `type: enum ["MyType"]` in oneOf). Stripping those would break discriminated unions. Const-property stripping ONLY applies when:
1. The schema is an allOf
2. The `allOf[0]` base matches a user-configured envelope pattern
3. A property on the merged result has `enum` with exactly 1 element

### Envelope unwrapping approach

No per-type custom serializers needed. A generated inline utility reads raw JSON, checks the discriminator field, and deserializes only the relevant parts:

```kotlin
// Generated once — shared internal utility
internal suspend inline fun <reified T> HttpResponse.unwrapEnvelope(
    json: Json,
    discriminator: String = "success",
): Pair<Boolean, JsonObject> {
    val obj = json.parseToJsonElement(bodyAsText()).jsonObject
    val isSuccess = obj[discriminator]?.jsonPrimitive?.boolean == true
    return isSuccess to obj
}
```

Each operation's generated code calls this and constructs the correctly-typed sealed case.

### Existing infrastructure that supports this

**`Route.Returns`** (`typed/.../routes/Route.kt:202-206`) already maps `HttpStatusCode → ReturnType`:
```kotlin
data class Returns(
    val default: ReturnType?,
    val responses: Map<HttpStatusCode, ReturnType>,
    val extensions: Map<String, JsonElement>,
)
```

**`needsSealedInterface()`** (`renderer/.../ClientRendererResponses.kt:16`) already detects multiple response codes:
```kotlin
fun Route.Returns.needsSealedInterface(): Boolean {
    val totalCases = responses.size + (if (default != null) 1 else 0)
    return totalCases > 1
}
```

**`buildSealedResponseTypeSpec()`** already generates sealed interfaces for multi-status-code responses.

The envelope work hooks into this existing infrastructure — it changes how individual response models are built (const-stripping, payload extraction), not the response dispatch structure.

---

## Desired Consumer API

```kotlin
// Per-operation sealed type
sealed interface GetAlertTypesResponse {
    data class Success(
        val result: Map<String, List<AlertType>>,
        val messages: List<AaaMessage>,
    ) : GetAlertTypesResponse

    data class Failure(
        val errors: List<AaaMessage>,
        val messages: List<AaaMessage>,
    ) : GetAlertTypesResponse
}

fun GetAlertTypesResponse.getOrThrow(): Map<String, List<AlertType>> = when (this) {
    is GetAlertTypesResponse.Success -> result
    is GetAlertTypesResponse.Failure -> throw ApiException(errors, messages)
}

// Client:
suspend fun getAlertTypes(): GetAlertTypesResponse

// Consumer:
when (val resp = api.getAlertTypes()) {
    is GetAlertTypesResponse.Success -> display(resp.result)
    is GetAlertTypesResponse.Failure -> handleErrors(resp.errors)
}
// Or:
val alerts = api.getAlertTypes().getOrThrow()
```

Note: `success` is NOT exposed — it's the discriminator consumed by the unwrap logic. `errors` on the success case is NOT exposed — it's a const (`enum: [[]]`), always empty.

---

## Implementation Plan

### Phase 1: Parser — `Schema.enum: List<JsonElement>?`

**File**: `parser/src/commonMain/kotlin/io/github/nomisrev/openapi/parser/Schema.kt:76`

Change `val enum: List<String?>? = null` → `val enum: List<JsonElement>? = null`.

This is the foundation — without it, `enum: [true]`, `enum: [[]]`, and integer enums can't be parsed. The Cloudflare integration test (`IntegrationTest.kt:19-25`, currently commented out) fails with `JsonDecodingException: Expected JsonPrimitive, but had JsonArray`.

May need a custom serializer for YAML input. The pattern already exists: `ExampleValue.Serializer` uses `toJsonElement()` to convert `YamlNode` → `JsonElement`. JSON input should work out of the box since `JsonElement` has a built-in kotlinx.serialization serializer.

#### Ripple: all `Schema.enum` consumers

| File | Change |
|------|--------|
| `typed/.../transformers/Enum.kt:11` | `toClosedEnum` signature: `List<String?>` → `List<JsonElement>`. Extract string content via `(it as? JsonPrimitive)?.content`, map `JsonNull` to `null`. Line 24: `schema.enum!!` pass-through becomes mapped extraction. |
| `typed/.../transformers/SchemaTransformer.kt:62-65,75` | Enum routing branches work with `JsonElement` instead of `String?` |
| `typed/.../transformers/Union.kt:125,129,140-141,145,260` | All `schema.enum` access: use `(element as? JsonPrimitive)?.content` |
| `typed/.../transformers/DiscriminatedObject.kt:228` | Replace `TODO("allOf Enum")` with enum value intersection |
| `typed/.../registry/Predicates.kt:28` | Null/empty checks (minimal — `.isNullOrEmpty()` still works on `List<JsonElement>?`) |

`Model.Enum.values` stays `List<String?>` — the renderer doesn't change for string/number enums. Multi-value integer enums like `[301, 302, 307, 308]` get stringified: `JsonPrimitive(301).content` → `"301"`.

### Phase 2: Config — `envelopeSchemas`

**Files**:
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/RenderConfig.kt` — add `val envelopeSchemas: Set<String> = emptySet()`
- `gradle-plugin/src/main/kotlin/.../OpenApiExtension.kt` — add `val envelopeSchemas: SetProperty<String>`
- `gradle-plugin/src/main/kotlin/.../GenerateOpenApiDefinition.kt` — wire through

Values are glob patterns matching component schema names:

```kotlin
// Gradle DSL
configure<OpenApiExtension> {
    envelopeSchemas.set(setOf(
        "*api-response-common",
        "*api-response-single",
        "*api-response-collection",
        "*_Response",
        "*_envelope",
        "*good_response"
    ))
}
```

The config needs to reach the typed layer for envelope detection during model transformation. Currently `RenderConfig` is renderer-only. Options:
- Create a shared config type in a common module
- Pass just the envelope patterns into `Registry.Scope` or transformer context
- Move envelope detection to the renderer layer (post-model)

Recommendation: pass envelope patterns into the typed layer since the model structure fundamentally changes.

### Phase 3: Typed — Envelope Detection & Const-Property Stripping

#### 3a. Detection logic

**File**: `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/SchemaTransformer.kt` (or new `Envelope.kt`)

When processing an allOf schema:
1. Check if `allOf[0]` is a `$ref` to a schema whose name matches any `envelopeSchemas` glob pattern
2. If yes: proceed with envelope handling
3. If no: existing allOf merge (unchanged)

#### 3b. Const-property identification

On the **merged** allOf schema (after existing `Schema.merge()`), classify each property:

```
Property has enum?.size == 1  →  CONST (strip from public API)
Property is "result" (added by inline allOf)  →  PAYLOAD
Everything else  →  SHARED (exposed on both Success and Failure)
```

This classification is ONLY applied when the allOf matched an envelope pattern. Regular schemas with `enum: ["MyType"]` discriminators are unaffected.

#### 3c. Model representation

**File**: `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/Model.kt`

New model variant:

```kotlin
@SerialName("Envelope")
@Serializable
data class Envelope(
    override val context: NamingContext,
    /** The Success case: result property + shared non-const properties */
    val successCase: Object,
    /** The discriminator field name (e.g., "success") */
    val discriminatorField: String,
    override val description: String?,
    override val title: String?,
    override val isNullable: Boolean,
) : Model, ContextHolder
```

The failure case comes from the operation's 4XX response model (already resolved separately in `Route.Returns.responses`). The `Model.Envelope` captures only the success-side transformation. The renderer pairs it with the 4XX model at code generation time.

Note: need to add `Model.Envelope` to all `when` exhaustive matches across the codebase (Model is a sealed interface). Files affected:
- `AllOf.kt` (Model.merge)
- `Route.kt` (closedEnumOrNull, rebuildDynamicPathSegment)
- `ObjectRenderer.kt` (defaultLiteral)
- `TypeMapping.kt` (toTypeName)
- Various renderer files

### Phase 4: Renderer — Hook Into Existing Sealed Interface Generation

#### 4a. Response type generation

**File**: `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererResponses.kt`

When a 2XX response model is `Model.Envelope`:
- Generate `sealed interface {OperationName}Response`
- `data class Success(val result: T, ...)` from envelope's successCase properties
- `data class Failure(val errors: ..., ...)` from the 4XX response model (if available)
- `fun {OperationName}Response.getOrThrow(): T` extension

This hooks into the existing `buildResponseTypeSpec()` flow — add an envelope-specific branch alongside the existing `needsSealedInterface()` and single-response paths.

#### 4b. Client method body

**File**: `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererOperations.kt`

Current pattern:
```kotlin
val response = client.get("/path")
return when (response.status.value) {
    200 -> Response.Ok(response.body())
    400 -> Response.BadRequest(response.body())
    else -> throw ResponseException(response, "")
}
```

Envelope pattern:
```kotlin
val response = client.get("/path")
return response.unwrapEnvelope<ResultType>(json)
// unwrapEnvelope reads JSON, checks discriminator, deserializes into Success or Failure
```

#### 4c. Shared unwrap utility

**New file or generated inline**: A reified function that reads raw JSON, checks the discriminator, and returns the parsed `JsonObject` with a success/failure flag. Each operation's code uses this + type-specific deserialization.

#### 4d. Json configuration

The generated client's `install(ContentNegotiation) { json() }` should use `Json { ignoreUnknownKeys = true }` — good practice for API clients (servers add fields over time) and necessary for stripped const properties.

### Phase 5: Tests

1. **Parser tests**: Schemas with `enum: [true]`, `enum: [[]]`, `enum: [1, 2, 3]`, `enum: [1, "two", null]` deserialize correctly
2. **Typed tests** (`AllOfSpec.kt`): allOf extending envelope base → `Model.Envelope` with const properties stripped
3. **Typed tests** (new): Verify non-envelope schemas with single-value enum discriminators are NOT stripped
4. **Renderer balloon test**: New test fixture with minimal envelope spec → expected sealed interface + unwrap code
5. **Integration**: Uncomment Cloudflare test in `IntegrationTest.kt:19-25`, verify full pipeline

---

## Verification

```bash
./gradlew :parser:allTests        # Phase 1 — enum parsing
./gradlew :typed:allTests          # Phase 3 — envelope detection
./gradlew :renderer:jvmTest        # Phase 4 — sealed interface generation
./gradlew build                    # Full pipeline
```

---

## Key Files

| File | Phase | Change |
|------|-------|--------|
| `parser/.../Schema.kt:76` | 1 | `List<String?>?` → `List<JsonElement>?` |
| `typed/.../transformers/Enum.kt:11` | 1 | Accept `List<JsonElement>`, extract string content |
| `typed/.../transformers/SchemaTransformer.kt:62-75` | 1,3 | JsonElement enum routing + envelope detection |
| `typed/.../transformers/Union.kt:125,129,140-141,145,260` | 1 | JsonElement enum access |
| `typed/.../transformers/DiscriminatedObject.kt:228` | 1 | allOf enum merge (replace TODO) |
| `typed/.../registry/Predicates.kt:28` | 1 | Minimal enum checks |
| `renderer/.../RenderConfig.kt` | 2 | Add `envelopeSchemas: Set<String>` |
| `gradle-plugin/.../OpenApiExtension.kt` | 2 | Add `envelopeSchemas` DSL |
| `gradle-plugin/.../GenerateOpenApiDefinition.kt` | 2 | Wire config through |
| `typed/.../Model.kt` | 3 | Add `Model.Envelope` |
| `typed/.../transformers/SchemaTransformer.kt` (or new `Envelope.kt`) | 3 | Envelope detection + const-stripping |
| `renderer/.../ClientRendererResponses.kt` | 4 | Envelope → sealed Success/Failure |
| `renderer/.../ClientRendererOperations.kt` | 4 | Unwrap utility + method body |
| `renderer/.../EnvelopeRenderer.kt` (new) | 4 | Sealed interface + getOrThrow generation |
