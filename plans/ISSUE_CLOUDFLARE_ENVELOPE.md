# ISSUE: Config-Driven Response Envelope Detection & Per-Operation Sealed Hierarchies

## Problem

APIs like Cloudflare use a response envelope pattern where a base schema `{success, errors, messages}` is extended via `allOf` to add a typed `result`:

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

The `success: enum [true]` and `errors: enum [[]]` are **singleton types** — they carry zero information for the consumer. Currently the generator merges everything into a flat data class, exposing ceremony fields alongside the actual payload.

### Prerequisite

`Schema.enum` must be widened from `List<String?>` to `List<JsonElement>` first — see `PARSER_CLOUDFLARE_ISSUE.md`. Without this, `enum: [true]` and `enum: [[]]` can't even be parsed.

### Cloudflare data (from investigation)

- **90 envelope base schemas** used as `allOf[0]` targets
- **20 distinct structural variants** (differ in error/message types, required fields, whether `result` is pre-declared)
- **~400 single-value non-string enums** across the spec (mostly `enum: [true]` / `enum: [false]`)
- **~22 multi-value integer enums** (HTTP status codes, TTL values, error codes)
- Pattern is consistent within the spec but is a **Cloudflare idiom**, not an OpenAPI standard

## Approach

**Config-driven, per-operation**: User declares which schemas are envelopes via glob patterns. When an allOf references an envelope base, the generator produces a **per-operation sealed interface** with Success/Failure cases. Properties with single-value enums (const/singleton types) are stripped from the public API — but **only** for envelope allOf types, never for regular schemas (which may use single-value enums as discriminators like `type: enum ["MyType"]`).

### What the consumer sees

```kotlin
sealed interface ListPetsResponse {
    data class Success(
        val result: List<Pet>,
        val messages: List<Message>,
    ) : ListPetsResponse

    data class Failure(
        val errors: List<Message>,
        val messages: List<Message>,
    ) : ListPetsResponse
}

fun ListPetsResponse.getOrThrow(): List<Pet> = when (this) {
    is ListPetsResponse.Success -> result
    is ListPetsResponse.Failure -> throw ApiException(errors, messages)
}

// Client:
suspend fun listPets(): ListPetsResponse
```

### Why per-operation, not generic

The 90 envelope bases have **20 distinct structural shapes** — different error/message types, different required fields, different base structures. A shared `ApiResponse<T>` would either be incorrect (wrong types) or require multiple type parameters (`ApiResponse<T, E, M>`). Per-operation sealed interfaces are always correct and simpler for the consumer.

### Envelope unwrapping

No per-type custom serializers needed. A generated inline utility reads raw JSON, checks the discriminator field, and deserializes `result` as `T`:

```kotlin
internal suspend inline fun <reified T> HttpResponse.unwrapEnvelope(
    json: Json,
    discriminator: String = "success",
): Pair<Boolean, JsonObject> {
    val obj = json.parseToJsonElement(bodyAsText()).jsonObject
    val isSuccess = obj[discriminator]?.jsonPrimitive?.boolean == true
    return isSuccess to obj
}
```

Each operation's generated code calls this utility and constructs the appropriate sealed case with correctly-typed deserialization for `result`, `errors`, `messages`, etc.

## Implementation

### Phase 1: Parser — `Schema.enum: List<JsonElement>?`

**File**: `parser/src/commonMain/kotlin/io/github/nomisrev/openapi/parser/Schema.kt:76`

Change `val enum: List<String?>?` → `val enum: List<JsonElement>?`. Custom serializer may be needed for YAML (reuse `toJsonElement()` from `ExampleValue.Serializer`).

All `Schema.enum` consumers need mechanical updates:

| File | Change |
|------|--------|
| `typed/.../transformers/Enum.kt` | `toClosedEnum` accepts `List<JsonElement>`, extracts `.content` from `JsonPrimitive` |
| `typed/.../transformers/SchemaTransformer.kt` | Enum routing with `JsonElement` |
| `typed/.../transformers/Union.kt` | `(element as? JsonPrimitive)?.content` at lines 125, 129, 140-141, 145, 260 |
| `typed/.../transformers/DiscriminatedObject.kt:228` | Implement allOf enum merge (intersect values) |
| `typed/.../registry/Predicates.kt` | Minimal null/empty check updates |

`Model.Enum.values` stays `List<String?>` — multi-value integer enums get stringified through the existing path.

### Phase 2: Config — `envelopeSchemas`

**Files**: `RenderConfig.kt`, `OpenApiExtension.kt`, `GenerateOpenApiDefinition.kt`

Add `envelopeSchemas: Set<String>` (glob patterns matching component schema names):

```kotlin
// RenderConfig
val envelopeSchemas: Set<String> = emptySet()

// Gradle DSL
configure<OpenApiExtension> {
    envelopeSchemas.set(setOf("*api-response-common*", "*_Response", "*good_response*"))
}
```

Pass into the typed layer so envelope detection happens during model transformation.

### Phase 3: Typed — Envelope Detection & Const-Property Stripping

**Detection**: When processing an allOf, check if `allOf[0]` references a schema matching an envelope pattern. If yes:

1. Resolve the base schema
2. Identify **const properties**: schema has `enum` with exactly 1 element → singleton type, carries no information
3. Identify the **result property**: added by the inline allOf part
4. Remaining properties are **shared** (e.g., `messages`)

**Const-property stripping**: Remove const properties from the public model. Scoped **only** to envelope allOf types. Regular schemas are unaffected.

**Model**: New `Model.Envelope` variant (or reuse `Model.Union.OneOf`):

```kotlin
data class Envelope(
    override val context: NamingContext,
    val successCase: Object,       // result + shared props, const props stripped
    val failureCase: Object,       // errors + shared props, const props stripped
    val discriminatorField: String, // "success"
    override val description: String?,
    override val title: String?,
    override val isNullable: Boolean,
) : Model, ContextHolder
```

### Phase 4: Renderer — Per-Operation Sealed Interface

**New file**: `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/EnvelopeRenderer.kt`

For `Model.Envelope`, generate:
- `sealed interface {OperationName}Response`
- `data class Success(...)` — result + shared properties
- `data class Failure(...)` — errors + shared properties
- `getOrThrow()` extension

**Client method generation** (`ClientRendererOperations.kt`): When response model is `Model.Envelope`, return the sealed interface and use the unwrap utility instead of `response.body()`. Needs `Json` instance in client + `ignoreUnknownKeys = true`.

### Phase 5: Tests

1. **Parser**: `enum: [true]`, `enum: [[]]`, `enum: [1, 2, 3]` parse correctly
2. **Typed**: allOf extending envelope base → `Model.Envelope` with const properties stripped
3. **Typed**: Non-envelope schema with single-value enum discriminator is NOT stripped
4. **Renderer**: Balloon test with minimal envelope spec → expected sealed interface
5. **Integration**: Cloudflare spec parses through full pipeline

## Key Files

| File | Change |
|------|--------|
| `parser/.../Schema.kt:76` | `List<String?>?` → `List<JsonElement>?` |
| `renderer/.../RenderConfig.kt` | Add `envelopeSchemas: Set<String>` |
| `gradle-plugin/.../OpenApiExtension.kt` | Add `envelopeSchemas` DSL |
| `typed/.../Model.kt` | Add `Model.Envelope` |
| `typed/.../transformers/SchemaTransformer.kt` | Envelope detection in allOf + const-stripping |
| `typed/.../transformers/Enum.kt` | Accept `List<JsonElement>` |
| `typed/.../transformers/Union.kt` | JsonElement enum access |
| `typed/.../transformers/DiscriminatedObject.kt:228` | allOf enum merge |
| `renderer/.../EnvelopeRenderer.kt` | NEW: sealed interface generation |
| `renderer/.../ClientRendererOperations.kt` | Envelope return type + unwrap |
