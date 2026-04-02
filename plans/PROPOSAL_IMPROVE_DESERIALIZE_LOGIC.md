# Proposal: Improve Tagged Union `deserialize` Generation

## Goal

Document the full deserialization problem behind `plans/ISSUE_IMPROVE_DESERIALIZE_LOGIC.md`,
using the real OpenAPI schema and the real generated Kotlin output, and propose an end-to-end
solution that:

1. preserves current wire semantics,
2. makes the required deserialization logic explicit,
3. removes duplicated generated branches,
4. keeps the `typed` / `renderer` boundary clean,
5. gives us a concrete target Kotlin output we can discuss before implementation.

This proposal is intentionally extensive. The aim is to make the E2E fix discussable without
having to reverse-engineer the current generator from scattered code paths.

---

## Short Conclusion

The real problem is not just “ugly generated Kotlin”.

The real problem is that the current generator knows how to classify a union as
`UnionDispatch.TaggedCustom`, but it still renders the final `when(tag)` code from a low-level
`tag -> cases` map. That loses a more useful semantic concept:

- several discriminator values may route to the exact same candidate-case set,
- and the actual work then happens inside that shared collision bucket.

The OpenAI `CreateEvalCompletionsRunDataSource` schema is the cleanest example:

- the inner `template[]` union is not discriminated by `type`,
- it is inferred as tagged by `role`,
- but **all four role values** (`user`, `assistant`, `system`, `developer`) route to the exact same
  two candidate cases,
- so the generated code repeats the same body four times.

The recommended fix is:

- keep the current public `Model.Union` / `UnionDispatch` model intact,
- add an additive typed-side analysis object that groups tags into **dispatch buckets**,
- let the renderer generate one branch per bucket,
- and emit a private helper for each collision bucket so the generated `deserialize` stays short.

This keeps the semantic grouping in `typed`, but keeps JSON/Kotlinx code generation in `renderer`.

---

## Current Problem In Concrete Kotlin

The current generated code for `CreateEvalRunRequest.DataSource.Completions.InputMessages.Template.Template`
looks like this:

```kotlin
override fun deserialize(decoder: Decoder): Template {
  val value = decoder.decodeSerializableValue(JsonElement.serializer())
  val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
  val obj = value as? JsonObject
  val tag = (obj?.get("role") as? JsonPrimitive)?.content
  when(tag) {
    "user" -> {
      val keys = obj?.keys.orEmpty()
      if ("phase" in keys) {
        return json.decodeFromJsonElement(EasyInputMessage.serializer(), value)
      }
      return json.attemptDeserialize(
        value,
        EasyInputMessage::class to { decodeFromJsonElement(EasyInputMessage.serializer(), it) },
        EvalItem::class to { decodeFromJsonElement(EvalItem.serializer(), it) },
      )
    }
    "assistant" -> {
      val keys = obj?.keys.orEmpty()
      if ("phase" in keys) {
        return json.decodeFromJsonElement(EasyInputMessage.serializer(), value)
      }
      return json.attemptDeserialize(
        value,
        EasyInputMessage::class to { decodeFromJsonElement(EasyInputMessage.serializer(), it) },
        EvalItem::class to { decodeFromJsonElement(EvalItem.serializer(), it) },
      )
    }
    "system" -> {
      val keys = obj?.keys.orEmpty()
      if ("phase" in keys) {
        return json.decodeFromJsonElement(EasyInputMessage.serializer(), value)
      }
      return json.attemptDeserialize(
        value,
        EasyInputMessage::class to { decodeFromJsonElement(EasyInputMessage.serializer(), it) },
        EvalItem::class to { decodeFromJsonElement(EvalItem.serializer(), it) },
      )
    }
    "developer" -> {
      val keys = obj?.keys.orEmpty()
      if ("phase" in keys) {
        return json.decodeFromJsonElement(EasyInputMessage.serializer(), value)
      }
      return json.attemptDeserialize(
        value,
        EasyInputMessage::class to { decodeFromJsonElement(EasyInputMessage.serializer(), it) },
        EvalItem::class to { decodeFromJsonElement(EvalItem.serializer(), it) },
      )
    }
    else -> throw SerializationException("Unknown tag: " + tag + " for io.openai.model.CreateEvalRunRequest.DataSource.Completions.InputMessages.Template.Template")
  }
}
```

This is valid code, and it preserves the current semantics, but it is poor output for three reasons:

1. The same collision logic is duplicated once per role.
2. The real dispatch structure is hidden.
3. The generated method is much longer than the logic it actually expresses.

The same underlying pattern already appears elsewhere in the OpenAI integration output:

- `InputItem.Item` uses a single collision tag `"message"` and a key-based shortcut on `"id"` / `"phase"`.
- `ItemResource` uses a single collision tag `"message"` and a key-based shortcut on `"phase"`.
- `CreateEvalRunRequest` and `EvalRun` are worse because the same collision body is repeated for
  multiple tag values.

---

## The Relevant OpenAPI Definitions

These fragments were extracted from `parser/src/commonTest/resources/specs/openai.yaml` with `yq`.
Only the relevant subsets are included here.

### 1. `CreateEvalCompletionsRunDataSource`

```yaml
type: object
title: CompletionsRunDataSource
properties:
  type:
    type: string
    enum:
      - completions
  input_messages:
    description: Used when sampling from a model.
    oneOf:
      - type: object
        title: TemplateInputMessages
        properties:
          type:
            type: string
            enum:
              - template
          template:
            type: array
            items:
              oneOf:
                - $ref: '#/components/schemas/EasyInputMessage'
                - $ref: '#/components/schemas/EvalItem'
        required:
          - type
          - template
      - type: object
        title: ItemReferenceInputMessages
        properties:
          type:
            type: string
            enum:
              - item_reference
          item_reference:
            type: string
        required:
          - type
          - item_reference
required:
  - type
  - source
```

Important observation:

- the outer `input_messages` union is a normal `type`-discriminated union,
- but the inner `template[].oneOf[...]` union has no explicit discriminator at all,
- so `typed` has to infer one.

### 2. `EasyInputMessage`

```yaml
type: object
title: Input message
properties:
  role:
    type: string
    enum:
      - user
      - assistant
      - system
      - developer
  content:
    oneOf:
      - type: string
        title: Text input
      - $ref: '#/components/schemas/InputMessageContentList'
  phase:
    anyOf:
      - $ref: '#/components/schemas/MessagePhase'
      - type: 'null'
  type:
    type: string
    enum:
      - message
required:
  - role
  - content
```

### 3. `EvalItem`

```yaml
type: object
title: Eval message object
properties:
  role:
    type: string
    enum:
      - user
      - assistant
      - system
      - developer
  content:
    $ref: '#/components/schemas/EvalItemContent'
  type:
    type: string
    enum:
      - message
required:
  - role
  - content
```

### 4. `EvalItemContent` and `InputMessageContentList`

```yaml
EvalItemContent:
  oneOf:
    - $ref: '#/components/schemas/EvalItemContentItem'
    - $ref: '#/components/schemas/EvalItemContentArray'

EvalItemContentItem:
  oneOf:
    - $ref: '#/components/schemas/EvalItemContentText'
    - $ref: '#/components/schemas/InputTextContent'
    - $ref: '#/components/schemas/EvalItemContentOutputText'
    - $ref: '#/components/schemas/EvalItemInputImage'
    - $ref: '#/components/schemas/InputAudio'

EvalItemContentArray:
  type: array
  items:
    $ref: '#/components/schemas/EvalItemContentItem'

InputMessageContentList:
  type: array
  items:
    $ref: '#/components/schemas/InputContent'

InputContent:
  oneOf:
    - $ref: '#/components/schemas/InputTextContent'
    - $ref: '#/components/schemas/InputImageContent'
    - $ref: '#/components/schemas/InputFileContent'
  discriminator:
    propertyName: type
```

### 5. The content subtypes that matter to the overlap analysis

```yaml
InputTextContent:
  type: object
  properties:
    type:
      type: string
      enum: [input_text]
    text:
      type: string
  required: [type, text]

InputImageContent:
  type: object
  properties:
    type:
      type: string
      enum: [input_image]
    image_url:
      anyOf:
        - type: string
        - type: 'null'
    file_id:
      anyOf:
        - type: string
        - type: 'null'
    detail:
      $ref: '#/components/schemas/ImageDetail'
  required: [type, detail]

InputFileContent:
  type: object
  properties:
    type:
      type: string
      enum: [input_file]
    file_id:
      anyOf:
        - type: string
        - type: 'null'
    filename:
      type: string
    file_data:
      type: string
    file_url:
      type: string
  required: [type]

EvalItemContentText:
  type: string

EvalItemContentOutputText:
  type: object
  properties:
    type:
      type: string
      enum: [output_text]
    text:
      type: string
  required: [type, text]

EvalItemInputImage:
  type: object
  properties:
    type:
      type: string
      enum: [input_image]
    image_url:
      type: string
    detail:
      type: string
  required: [type, image_url]

InputAudio:
  type: object
  properties:
    type:
      type: string
      enum: [input_audio]
    input_audio:
      type: object
      properties:
        data:
          type: string
        format:
          type: string
          enum: [mp3, wav]
      required: [data, format]
  required: [type, input_audio]
```

---

## What These Definitions Mean Semantically

### Outer `input_messages`

The outer union is simple:

- `type = "template"` means `TemplateInputMessages`
- `type = "item_reference"` means `ItemReferenceInputMessages`

That union is perfectly native-discriminator-safe.

### Inner `template[]`

The inner union is the real problem:

```yaml
oneOf:
  - EasyInputMessage
  - EvalItem
```

No explicit discriminator is declared here, so the current typed logic infers one from a common
required tag-like property. It picks `role`.

That inference is valid, but it is not enough to produce a native discriminator union, because both
cases expose the exact same `role` values:

```yaml
role.enum:
  - user
  - assistant
  - system
  - developer
```

So the inner union is not:

- `user -> EasyInputMessage`
- `assistant -> EvalItem`

It is:

- `user -> {EasyInputMessage, EvalItem}`
- `assistant -> {EasyInputMessage, EvalItem}`
- `system -> {EasyInputMessage, EvalItem}`
- `developer -> {EasyInputMessage, EvalItem}`

This is the core concept the generated Kotlin should make visible.

---

## The Deserializer Logic We Actually Need

For the OpenAI `template[]` union, the deserializer has to implement the following logic:

1. Decode the payload as `JsonElement`.
2. Confirm we are on a `JsonDecoder`.
3. If the payload is an object and has a primitive string `role`, use that as the first routing key.
4. Every known role value maps to the same candidate case set:
   - `EasyInputMessage`
   - `EvalItem`
5. Inside that shared collision bucket:
   - if the object contains `phase`, decode as `EasyInputMessage`
   - otherwise fall back to stable ordered deserialization across the two candidates
6. The stable ordered fallback must preserve the current priority order.

The critical point is step 4:

> the first routing dimension is **not** “tag identifies subtype”, it is “tag identifies candidate bucket”.

That is why the current output is repetitive: it renders the candidate bucket logic once per tag,
even though the bucket is identical.

---

## Why The Current Fallback Order Matters

The current implementation does not just do key-based shortcuts. It also relies on
`attemptDeserialize(...)`, and the order passed into that function matters.

The current renderer sorts union cases by `deserializationPriority()`. Relevant excerpts:

```kotlin
internal fun Model.deserializationPriority(): Int = when (this) {
    is Model.Object -> when (val ap = additionalProperties) {
        is Model.Object.AdditionalProperties.Allowed ->
            if (ap.value) 200
            else 100 - propertyCount
        is Model.Object.AdditionalProperties.Schema -> 150
    }
    is Model.DiscriminatedObject -> 300
    is Model.Union -> 400
    is Model.Enum -> 500
    is Model.Collection -> 600
    is Model.Reference -> 700
    ...
}
```

For the OpenAI collision:

- `EasyInputMessage` is an object with 4 properties: `role`, `content`, `phase`, `type`
- `EvalItem` is an object with 3 properties: `role`, `content`, `type`

So `EasyInputMessage` is tried before `EvalItem`.

That is not incidental. It is the current overlap policy, and it should remain the final tie-break.

### Why overlap is real

There are payloads that both cases can plausibly accept:

```json
{
  "role": "user",
  "content": "hello"
}
```

Both schemas allow:

- `role`
- `content`
- no `type`
- no `phase`

So once `phase` is absent, top-level key inspection alone is not enough. We still need the stable
fallback order.

### What should *not* be done

I do **not** recommend turning the renderer into a recursive, schema-specific content-shape
decision-tree synthesizer.

For example, we could theoretically inspect nested `content` array members to distinguish
`InputMessageContentList` from `EvalItemContentArray`, but that would:

1. duplicate serializer logic,
2. be much harder to prove correct,
3. create brittle special cases,
4. blur the `typed` / `renderer` boundary badly.

The right generic rule is:

- use cheap top-level distinguishing keys when they exist,
- otherwise trust the real serializers in a documented, stable order.

---

## What The Current Generator Is Doing Internally

Today the important pieces are:

### In `typed`

- unions are classified as:
  - `NativeDiscriminator`
  - `TaggedCustom`
  - `Structural`
- each union case carries `discriminatorValues: Set<String>`

For the OpenAI inner `template[]` union, the typed model effectively looks like:

```kotlin
Model.OneOf(
  dispatch = UnionDispatch.TaggedCustom("role"),
  cases = listOf(
    Case(
      model = EasyInputMessage,
      discriminatorValues = setOf("user", "assistant", "system", "developer"),
    ),
    Case(
      model = EvalItem,
      discriminatorValues = setOf("user", "assistant", "system", "developer"),
    ),
  )
)
```

This is already enough to know the semantic problem. The renderer simply does not expose that
semantic structure cleanly.

### In `renderer`

The renderer currently does something equivalent to:

1. build `Map<tag, List<case>>`
2. emit a `when(tag)` branch for each tag
3. for each branch:
   - if one case owns the tag, decode directly
   - if multiple cases own the tag, run collision sub-dispatch
4. if there are untagged cases, use them in the `else` branch

That is already logically correct.

The weakness is the chosen intermediate representation:

- the renderer works with raw per-tag entries,
- instead of grouped per-bucket entries.

---

## The Real Semantic Model We Want

For tagged custom unions, the useful abstraction is:

### 1. Tag ownership

For each discriminator value, which cases claim it?

For the OpenAI union:

| Tag | Candidate cases |
| --- | --- |
| `user` | `EasyInputMessage`, `EvalItem` |
| `assistant` | `EasyInputMessage`, `EvalItem` |
| `system` | `EasyInputMessage`, `EvalItem` |
| `developer` | `EasyInputMessage`, `EvalItem` |

### 2. Dispatch buckets

Group tags that lead to the exact same candidate-case set:

| Bucket | Tags | Cases |
| --- | --- | --- |
| `RoleBucket0` | `user`, `assistant`, `system`, `developer` | `EasyInputMessage`, `EvalItem` |

### 3. Collision strategy inside the bucket

For `RoleBucket0`:

- unique top-level key for `EasyInputMessage`: `phase`
- no unique top-level key for `EvalItem`
- therefore:
  - if `"phase" in keys` -> `EasyInputMessage`
  - else -> try `EasyInputMessage`, then `EvalItem`

That is the exact logic the generated Kotlin should show.

---

## Proposed Design

Use a **hybrid metadata** design:

- `typed` exposes additive, semantic tagged-dispatch analysis,
- `renderer` still owns JSON/Kotlinx emission,
- no breaking change to `Model.Union` or `UnionDispatch`.

This is the best tradeoff.

### Why not renderer-only?

Because the semantic grouping is not a KotlinPoet concern. The fact that:

- several tags route to the same case set,
- and some unions have untagged fallbacks,

is union semantics. If we leave that entirely inside the renderer, we keep rediscovering the same
structure at the last stage.

### Why not a fully typed-owned deserialization AST?

Because the final logic still depends on renderer-only decisions:

- unique-key shortcuts,
- `attemptDeserialize(...)`,
- current JSON-only custom serializer limitations,
- actual generated helper shape.

Moving all of that into `typed` would make `typed` too Kotlinx/JSON-aware.

---

## Proposed Typed-Side Metadata

Add a small additive analysis API in `typed`, for example:

```kotlin
data class TaggedDispatchAnalysis(
    val propertyName: String,
    val buckets: List<TaggedDispatchBucket>,
    val untaggedCaseIndexes: List<Int>,
)

data class TaggedDispatchBucket(
    val tags: List<String>,
    val caseIndexes: List<Int>,
)

fun Model.Union.taggedDispatchAnalysisOrNull(): TaggedDispatchAnalysis?
```

### Important constraints

This should be:

- additive,
- not part of the serialized `Model` wire format,
- stable in ordering,
- purely semantic.

### What it computes

For a `TaggedCustom("role")` union:

1. build `tag -> caseIndexes`
2. group identical `caseIndexes`
3. preserve first-seen tag order
4. preserve union declaration order for case indexes
5. expose untagged cases separately

### Example result for the OpenAI union

```kotlin
TaggedDispatchAnalysis(
    propertyName = "role",
    buckets = listOf(
        TaggedDispatchBucket(
            tags = listOf("user", "assistant", "system", "developer"),
            caseIndexes = listOf(0, 1),
        )
    ),
    untaggedCaseIndexes = emptyList(),
)
```

### Example result for `InputItem`

For the already existing nested-union-case example:

```kotlin
TaggedDispatchAnalysis(
    propertyName = "type",
    buckets = listOf(
        TaggedDispatchBucket(tags = listOf("function_call"), caseIndexes = listOf(1)),
        TaggedDispatchBucket(tags = listOf("item_reference"), caseIndexes = listOf(2)),
        TaggedDispatchBucket(tags = listOf("message"), caseIndexes = listOf(0, 1)),
    ),
    untaggedCaseIndexes = emptyList(),
)
```

This is the right level of information for `typed` to own.

---

## Proposed Renderer Strategy

The renderer should consume `TaggedDispatchAnalysis` and generate code from buckets instead of raw
tags.

### High-level rendering algorithm

```kotlin
override fun deserialize(decoder: Decoder): Union {
    val value = decoder.decodeSerializableValue(JsonElement.serializer())
    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
    val obj = value as? JsonObject
    val tag = (obj?.get(propertyName) as? JsonPrimitive)?.content

    return when (tag) {
        // one branch per bucket
        "tag1", "tag2" -> decodeBucket0(json, value, obj)
        "tag3" -> decodeBucket1(json, value, obj)
        else -> decodeUntaggedOrThrow(json, value)
    }
}
```

### Bucket rules

For each bucket:

#### Direct bucket

If `caseIndexes.size == 1`:

- decode the single case directly
- no helper needed unless we want absolute uniformity

#### Collision bucket

If `caseIndexes.size > 1`:

- generate a private helper inside `Serializer`
- helper applies:
  1. unique-key shortcuts
  2. fallback direct decode if exactly one case remains after unique-key checks
  3. `attemptDeserialize(...)` in current priority order if ambiguity remains

### Else branch

If `untaggedCaseIndexes` is empty:

- keep current unknown-tag error

If `untaggedCaseIndexes` is non-empty:

- keep current behavior of attempting only those untagged cases

This is important:

> unknown or missing tags must not silently try cases that explicitly claimed tags.

That behavior is already correct and should remain unchanged.

---

## Proposed Generated Kotlin For The OpenAI Example

This is the proposed target shape for the inner `Template` serializer.

The exact helper naming is not important. The important part is the structure.

```kotlin
public object Serializer : KSerializer<Template> {
  @OptIn(
    InternalSerializationApi::class,
    ExperimentalSerializationApi::class,
  )
  override val descriptor: SerialDescriptor =
      buildSerialDescriptor(
        "io.openai.model.CreateEvalRunRequest.DataSource.Completions.InputMessages.Template.Template",
        PolymorphicKind.SEALED,
      ) {
        element("EasyInputMessage", EasyInputMessage.serializer().descriptor)
        element("EvalItem", EvalItem.serializer().descriptor)
      }

  override fun deserialize(decoder: Decoder): Template {
    val value = decoder.decodeSerializableValue(JsonElement.serializer())
    val json = requireNotNull(decoder as? JsonDecoder) {
      "Complex unions currently only supported for Json"
    }.json
    val obj = value as? JsonObject
    val tag = (obj?.get("role") as? JsonPrimitive)?.content

    return when (tag) {
      "user", "assistant", "system", "developer" ->
        deserializeRoleBucket(json, value, obj)

      else ->
        throw SerializationException(
          "Unknown tag: " + tag +
            " for io.openai.model.CreateEvalRunRequest.DataSource.Completions.InputMessages.Template.Template"
        )
    }
  }

  private fun deserializeRoleBucket(
    json: Json,
    value: JsonElement,
    obj: JsonObject?,
  ): Template {
    val keys = obj?.keys.orEmpty()

    if ("phase" in keys) {
      return json.decodeFromJsonElement(EasyInputMessage.serializer(), value)
    }

    return json.attemptDeserialize(
      value,
      EasyInputMessage::class to {
        decodeFromJsonElement(EasyInputMessage.serializer(), it)
      },
      EvalItem::class to {
        decodeFromJsonElement(EvalItem.serializer(), it)
      },
    )
  }

  override fun serialize(encoder: Encoder, value: Template) {
    when (value) {
      is EasyInputMessage ->
        encoder.encodeSerializableValue(EasyInputMessage.serializer(), value)
      is EvalItem ->
        encoder.encodeSerializableValue(EvalItem.serializer(), value)
    }
  }
}
```

### Why this is better

It makes the logic explicit:

- one tag bucket,
- one shared collision helper,
- one unique-key shortcut,
- one stable fallback.

It is strictly clearer than four identical inline branches.

---

## Proposed Generated Kotlin For Existing Similar Patterns

### `InputItem.Item`

Current logic:

- tag `"message"` routes to a collision between `OutputMessage` and `InputMessage`
- unique-key shortcut uses `"id"` or `"phase"`

Proposed shape:

```kotlin
when (tag) {
  "message" -> deserializeMessageBucket(json, value, obj)
  "function_call" -> ...
  ...
}

private fun deserializeMessageBucket(
  json: Json,
  value: JsonElement,
  obj: JsonObject?,
): Item {
  val keys = obj?.keys.orEmpty()
  if ("id" in keys || "phase" in keys) {
    return json.decodeFromJsonElement(OutputMessage.serializer(), value)
  }
  return json.attemptDeserialize(
    value,
    OutputMessage::class to { decodeFromJsonElement(OutputMessage.serializer(), it) },
    InputMessage::class to { decodeFromJsonElement(InputMessage.serializer(), it) },
  )
}
```

### `ItemResource`

Current logic:

- tag `"message"` routes to collision between `OutputMessage` and `InputMessageResource`
- unique-key shortcut uses `"phase"`

Proposed shape is the same pattern:

```kotlin
when (tag) {
  "message" -> deserializeMessageBucket(json, value, obj)
  "function_call" -> ...
  ...
}
```

This proposal therefore improves not just the eval schema, but the general tagged-collision output.

---

## Proposed Generator-Side Kotlin

The generated Kotlin above is the main target, but the E2E discussion also needs a generator-side
shape.

### Typed-side analysis

```kotlin
data class TaggedDispatchAnalysis(
    val propertyName: String,
    val buckets: List<TaggedDispatchBucket>,
    val untaggedCaseIndexes: List<Int>,
)

data class TaggedDispatchBucket(
    val tags: List<String>,
    val caseIndexes: List<Int>,
)

fun Model.Union.taggedDispatchAnalysisOrNull(): TaggedDispatchAnalysis? {
    val dispatch = dispatch as? UnionDispatch.TaggedCustom ?: return null

    val tagToCaseIndexes = linkedMapOf<String, MutableList<Int>>()
    val untaggedCaseIndexes = mutableListOf<Int>()

    cases.forEachIndexed { index, case ->
        if (case.discriminatorValues.isEmpty()) {
            untaggedCaseIndexes += index
        } else {
            case.discriminatorValues.forEach { tag ->
                tagToCaseIndexes.getOrPut(tag) { mutableListOf() } += index
            }
        }
    }

    val grouped = linkedMapOf<List<Int>, MutableList<String>>()
    tagToCaseIndexes.forEach { (tag, caseIndexes) ->
        grouped.getOrPut(caseIndexes.toList()) { mutableListOf() } += tag
    }

    return TaggedDispatchAnalysis(
        propertyName = dispatch.propertyName,
        buckets = grouped.map { (caseIndexes, tags) ->
            TaggedDispatchBucket(tags = tags, caseIndexes = caseIndexes)
        },
        untaggedCaseIndexes = untaggedCaseIndexes,
    )
}
```

### Renderer-side use

```kotlin
private fun buildTaggedCustomDeserializeCode(
    analysis: TaggedDispatchAnalysis,
    ...
): CodeBlock {
    return CodeBlock.builder()
        .addStatement("val value = decoder.decodeSerializableValue(%T.serializer())", JsonElement::class)
        .addStatement(
            "val json = requireNotNull(decoder as? %T) { %S }.json",
            JsonDecoder::class,
            "Complex unions currently only supported for Json"
        )
        .addStatement("val obj = value as? %T", JsonObject::class)
        .addStatement(
            "val tag = (obj?.get(%S) as? %T)?.content",
            analysis.propertyName,
            JsonPrimitive::class,
        )
        .beginControlFlow("return when(tag)")
        .apply {
            analysis.buckets.forEachIndexed { index, bucket ->
                val labels = bucket.tags.joinToString(", ") { "\"$it\"" }
                if (bucket.caseIndexes.size == 1) {
                    addStatement("%L -> %L", labels, decodeCaseExpression(bucket.caseIndexes.single(), ...))
                } else {
                    addStatement("%L -> deserializeTaggedBucket%L(json, value, obj)", labels, index)
                }
            }
            if (analysis.untaggedCaseIndexes.isNotEmpty()) {
                addStatement("else -> %L", buildUntaggedFallback(...))
            } else {
                addStatement("else -> throw %T(%S + tag + %S)", SerializationException::class, "Unknown tag: ", " for ...")
            }
        }
        .endControlFlow()
        .build()
}
```

The exact KotlinPoet code is flexible. The important design point is:

- the renderer consumes bucket analysis,
- and emits helpers only for actual collision buckets.

---

## The Behavioral Contract We Should Explicitly Document

This needs to be written down in `typed/README.md` and referenced from `renderer/README.md`.

### Contract

For `TaggedCustom` unions:

1. Determine candidate cases per tag.
2. Group tags with identical candidate-case sets into buckets.
3. Emit one branch per bucket.
4. For direct buckets, decode the single case directly.
5. For collision buckets:
   - use cheap top-level distinguishing keys if available,
   - then fall back to the current ordered-deserialization policy.
6. For missing or unknown tags:
   - try only untagged cases,
   - otherwise throw an unknown-tag error.

### Overlap policy

When multiple cases in the same collision bucket can still deserialize the payload:

- preserve the current `deserializationPriority()` ordering.

This is the existing behavior and should remain the final tiebreak.

### Non-goal

Do not synthesize deep recursive shape tests for nested content unions just to avoid
`attemptDeserialize(...)`.

That would be more complex, less obviously correct, and much harder to maintain.

---

## Why This Belongs Partly In `typed`

This proposal does **not** move code generation into `typed`.

It only moves the semantic grouping of tagged custom unions into `typed`.

That grouping is a typed concern because it answers:

- which discriminator values exist,
- which cases claim each value,
- which values are semantically equivalent for dispatch purposes.

The renderer remains responsible for:

- JSON-only serializer code,
- `JsonDecoder` plumbing,
- unique-key shortcuts,
- `attemptDeserialize(...)`,
- actual helper emission.

That is the right split.

---

## Expected Benefits

### Generated code quality

- shorter `deserialize` methods,
- no repeated collision bodies,
- clearer generated intent.

### Debuggability

- easier to inspect generated serializers in large OpenAI models,
- easier to explain why a payload routed to a given case,
- easier to identify overlap boundaries.

### E2E maintainability

- one semantic notion of “bucket” shared by tests and renderer,
- cleaner future work for more advanced tagged custom unions,
- less temptation to add schema-specific renderer hacks.

---

## Non-Goals

This proposal does **not** try to solve:

- renaming `Template.Template` style generated type names,
- changing native discriminator rendering,
- changing `attemptDeserialize(...)` error reporting,
- removing JSON-only limitations from custom union serializers,
- deep nested content-shape inference beyond top-level unique-key checks.

Those are separate concerns.

---

## Proposed Tests

## Typed tests

Add tests that assert the analysis object directly.

### 1. OpenAI-style multi-tag collision bucket

Model:

- two cases
- both claim `user`, `assistant`, `system`, `developer`

Expected analysis:

```kotlin
TaggedDispatchAnalysis(
    propertyName = "role",
    buckets = listOf(
        TaggedDispatchBucket(
            tags = listOf("user", "assistant", "system", "developer"),
            caseIndexes = listOf(0, 1),
        )
    ),
    untaggedCaseIndexes = emptyList(),
)
```

### 2. Existing nested `message` collision

Expected:

- direct buckets for other tags,
- one collision bucket for `"message"`.

### 3. Partial coverage

Expected:

- tagged buckets for the tagged cases,
- untagged case indexes in `untaggedCaseIndexes`.

### 4. Multi-value plus collision

Expected:

- direct buckets where a tag uniquely routes,
- collision bucket where a tag is shared.

## Renderer goldens

Update or add goldens to verify:

1. shared multi-tag collision bucket renders as one grouped branch,
2. single-tag collision renders via helper,
3. direct tagged branches remain direct,
4. else branch still handles untagged-or-throw correctly.

## OpenAI verification

After implementation, spot-check:

- `CreateEvalRunRequest.kt`
- `EvalRun.kt`
- `InputItem.kt`
- `ItemResource.kt`

The goal is to confirm that the proposal improves the real generated output, not just toy tests.

---

## Files That Would Be Touched

Likely implementation files:

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/...`
  - new tagged-dispatch analysis file
- `typed/src/commonTest/kotlin/io/github/nomisrev/transformers/UnionSpec.kt`
- `typed/README.md`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRendererSerialization.kt`
- `renderer/src/jvmTest/kotlin/io/github/nomisrev/render/UnionSpec.kt`
- `renderer/README.md`

This should stay focused. It does not require broad refactors elsewhere.

---

## Implementation Plan

1. Add typed-side tagged-dispatch analysis.
2. Add typed tests for bucket grouping.
3. Document the dispatch contract in `typed/README.md`.
4. Refactor renderer tagged-custom deserializer generation to use buckets.
5. Add helper emission for collision buckets.
6. Update renderer goldens.
7. Verify the actual OpenAI generated output.

---

## Final Recommendation

Implement the hybrid metadata approach.

Specifically:

- do not change `Model.Union` or `UnionDispatch`,
- add `TaggedDispatchAnalysis`,
- group equivalent tags into buckets in `typed`,
- render one branch per bucket,
- generate private helpers for collision buckets,
- keep current key shortcuts and current fallback priority.

That gets us the clean generated Kotlin we want, without making `typed` responsible for JSON code
generation or inventing fragile deep schema-specific heuristics.

For the OpenAI `CreateEvalCompletionsRunDataSource` case, the correct generated result is a single
shared role bucket, not four copies of the same body.
