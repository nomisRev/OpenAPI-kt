# Proposal: Object `oneOf` Validation for Inline Request Bodies

## Goal

Document the real problem behind `plans/ISSUE_INCORRECT_OBJECT_ONE_OF_VALIDATION.md`, using the
actual OpenAPI schema and the current code paths in this repository, and propose a concrete E2E
direction we can discuss before implementation.

This proposal is intentionally focused on:

1. what the schema means,
2. what the repository does today,
3. why that is still incorrect,
4. what Kotlin we should aim to generate instead,
5. what typed/renderer changes would be required.

No code changes are included in this document.

---

## Short Conclusion

The old issue snippet is no longer the current inline-request-body behavior.

Today, inline request bodies with `type: object` plus top-level `oneOf` are explicitly flattened to
a plain `Model.Object` in `typed`, so the old sealed-union output is already avoided.

However, the flattening is currently done by **dropping** `oneOf` / `anyOf` / `allOf` completely:

```kotlin
private fun Schema.normalizedInlineRequestBodySchema(): Schema =
    if (type == Schema.Type.Basic.Object && (allOf != null || oneOf != null || anyOf != null)) {
        copy(
            allOf = null,
            oneOf = null,
            anyOf = null,
            discriminator = null,
        )
    } else {
        this
    }
```

That means the current repository preserves the Kotlin object shape, but it also silently removes
the schema validation semantics.

So the real bug today is:

- not "we still generate a sealed union for this inline body",
- but "we flatten to a regular object and lose the top-level object-composite constraints".

The correct direction is:

- keep the generated Kotlin as a regular data class,
- merge the object branches into object-level validation metadata,
- and render `init { require(...) }` checks from that metadata.

---

## What The Repository Does Today

### 1. Generic schema transformation still prefers composites over object shape

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/SchemaTransformer.kt`
currently routes structural `oneOf` / `anyOf` into `Model.Union` before falling back to
`Model.Object`.

That happens here:

```kotlin
compositeTakesPrecedence && schema.oneOf?.isNotEmpty() == true -> buildOneOf(context, schema.oneOf!!)
compositeTakesPrecedence && schema.anyOf?.isNotEmpty() == true -> buildAnyOf(context, schema.anyOf!!)
```

So outside of the request-body special case, an object schema with top-level `oneOf` is still
conceptually treated as a union.

### 2. Inline request bodies special-case object composites by stripping them

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/RequestBody.kt` explicitly removes
top-level object composites for inline request bodies.

This is also covered by the existing test:

```kotlin
test("typed object request body with top-level composite stays a set body object")
```

The assertion only verifies that the body becomes `Model.Object` with the expected properties:

```kotlin
val body = assertIs<Route.Body.SetBody>(route.body?.defaultOrNull())
val bodyModel = assertIs<Model.Object>(body.type)
assertEquals(setOf("name", "status", "conclusion"), bodyModel.properties.keys)
```

That test proves the current intent:

- keep the request body ergonomic as a single object,
- but it does **not** preserve the original `oneOf` behavior.

### 3. `Model.Object` and `ObjectRenderer` have no object-level validation mechanism

`Model.Object` currently contains:

- properties,
- additionalProperties,
- context flags,

but no representation of:

- `oneOf`,
- `anyOf`,
- conditional requiredness,
- conditional enum narrowing.

`ObjectRenderer` can already render `additional: JsonObject?` / `Map<String, T>?` when
`additionalProperties` exists, but it has no hook to emit `init { require(...) }`.

So even after flattening to a plain data class, nothing enforces the dropped composite semantics.

---

## The Actual OpenAPI Definition

This is the relevant request-body schema from
`parser/src/commonTest/resources/specs/github.json` for `POST /repos/{owner}/{repo}/check-runs`,
rewritten as YAML for readability.

```yaml
type: object
required:
  - name
  - head_sha
properties:
  name:
    type: string
  head_sha:
    type: string
  details_url:
    type: string
  external_id:
    type: string
  status:
    type: string
    enum:
      - queued
      - in_progress
      - completed
      - waiting
      - requested
      - pending
    default: queued
  started_at:
    type: string
    format: date-time
  conclusion:
    type: string
    enum:
      - action_required
      - cancelled
      - failure
      - neutral
      - success
      - skipped
      - stale
      - timed_out
  completed_at:
    type: string
    format: date-time
  output:
    type: object
    required: [title, summary]
    properties:
      title: { type: string }
      summary: { type: string }
      text: { type: string }
  actions:
    type: array
    items:
      type: object
      required: [label, description, identifier]
      properties:
        label: { type: string }
        description: { type: string }
        identifier: { type: string }
discriminator:
  propertyName: status
oneOf:
  - properties:
      status:
        enum: [completed]
    required:
      - status
      - conclusion
    additionalProperties: true
  - properties:
      status:
        enum: [queued, in_progress]
    additionalProperties: true
```

### Important observation

This schema does **not** describe two different Kotlin body types.

It describes:

- one outer object,
- with common fields (`name`, `head_sha`, ...),
- plus exactly one branch-specific validation rule set.

So the natural Kotlin model is a single data class, not a sealed interface.

---

## Minimal Reproducer

The irreducible shape of the issue is smaller than the full GitHub payload:

```yaml
type: object
required: [name]
properties:
  name:
    type: string
  status:
    type: string
    enum: [queued, in_progress, completed, waiting, requested, pending]
  conclusion:
    type: string
oneOf:
  - properties:
      status:
        enum: [completed]
    required: [status, conclusion]
    additionalProperties: true
  - properties:
      status:
        enum: [queued, in_progress]
    additionalProperties: true
```

This still demonstrates the same behavior:

- `status == completed` requires `conclusion`,
- `status == queued|in_progress` is allowed,
- `status == waiting|requested|pending` is actually invalid,
- `status == null` is still allowed because the second branch does not require `status`.

That last point matters: the schema is more precise than a naive
`require(status != Completed || conclusion != null)`.

---

## What The Schema Means After Normalization

The correct way to reason about this object is:

1. Start with the outer object schema.
2. Merge the outer object into each `oneOf` branch.
3. Validate the final payload against exactly one merged branch.

The repository already has the schema-merge primitives to express this idea:

- `Schema.merge(...)`
- `Schema.mergeCompositeUnion(...)`
- `Schema.mergeObject(...)`

### Merged branch A

```yaml
type: object
required:
  - name
  - head_sha
  - status
  - conclusion
properties:
  name: { type: string }
  head_sha: { type: string }
  details_url: { type: string }
  external_id: { type: string }
  status:
    type: string
    enum: [completed]
  started_at:
    type: string
    format: date-time
  conclusion:
    type: string
    enum:
      - action_required
      - cancelled
      - failure
      - neutral
      - success
      - skipped
      - stale
      - timed_out
  completed_at:
    type: string
    format: date-time
additionalProperties: true
```

### Merged branch B

```yaml
type: object
required:
  - name
  - head_sha
properties:
  name: { type: string }
  head_sha: { type: string }
  details_url: { type: string }
  external_id: { type: string }
  status:
    type: string
    enum: [queued, in_progress]
  started_at:
    type: string
    format: date-time
  conclusion:
    type: string
    enum:
      - action_required
      - cancelled
      - failure
      - neutral
      - success
      - skipped
      - stale
      - timed_out
  completed_at:
    type: string
    format: date-time
additionalProperties: true
```

### Consequence

The final Kotlin body should be:

- one class with the union of the merged branch properties,
- `additionalProperties = true` carried into the final object,
- plus exact branch validation.

---

## Why The Old Suggested `init` Is Incomplete

The issue note suggested roughly:

```kotlin
init {
    require(status != Status.Completed || conclusion != null)
}
```

That is directionally correct, but it is not enough for the actual schema.

It misses at least three things:

1. It would still allow `waiting`, `requested`, and `pending`, even though the `oneOf` branches
   reject them.
2. It does not reflect the exact `oneOf` semantics, only one implication extracted from them.
3. It ignores branch `additionalProperties: true`, which should be merged into the final object.

Also, the prose description says:

- conclusion is required when `completed_at` is present or `status == completed`

but the machine-readable schema only encodes the `status == completed` part.

So from OpenAPI alone we can safely generate validation for:

- `status == completed -> conclusion != null`

but not for:

- `completed_at != null -> conclusion != null`

unless we explicitly decide to invent validation from description text, which would be a separate
feature and should not be mixed into this fix.

---

## Proposed E2E Direction

## Step 1: Stop throwing away object composite semantics

Keep the inline request-body flattening to a regular object shape, but replace the current
"strip and forget" behavior with:

1. detect `type: object` with top-level `oneOf` / `anyOf`,
2. merge the outer object into each branch,
3. build one final `Model.Object`,
4. attach branch-validation metadata to that object.

Suggested shape:

```kotlin
@Serializable
sealed interface ObjectCompositeValidation {
  @Serializable
  data class OneOf(val branches: List<Branch>) : ObjectCompositeValidation

  @Serializable
  data class AnyOf(val branches: List<Branch>) : ObjectCompositeValidation

  @Serializable
  data class Branch(
    val requiredProperties: Set<String>,
    val enumValuesByProperty: Map<String, Set<String>>,
  )
}
```

Then add it to `Model.Object`:

```kotlin
data class Object(
  ...,
  val validation: ObjectCompositeValidation? = null,
)
```

That keeps the typed model explicit without leaking KotlinPoet or renderer concerns into `typed`.

## Step 2: Generate `init` validation in `ObjectRenderer`

For object models with `validation != null`, render:

- private branch matcher helpers or inline boolean expressions,
- and a single `init` block:
  - `oneOf` => exactly one branch must match,
  - `anyOf` => at least one branch must match.

For the first implementation pass, the branch matcher only needs to support the rule shapes needed
by this issue:

- required property presence,
- enum/const narrowing on direct object properties.

That is already sufficient for the GitHub `check-runs` request body.

## Step 3: Merge `additionalProperties` from the branches

When the merged branches produce `additionalProperties: true`, the final object model should also
carry that and render the existing `additional` serializer support.

This is important because the branch-level `additionalProperties: true` is not decorative. It is
what allows the branch fragment to coexist with the shared outer properties after normalization.

## Step 4: Preserve the current fallback behavior for unsupported shapes

If a top-level object composite branch contains rules we cannot yet translate into object-level
validation metadata, we should not silently miscompile it.

Safer fallback:

- keep the current object flattening only for supported branch shapes,
- otherwise stay on the existing union path.

That keeps the change focused and prevents a "looks good but wrong semantics" regression.

---

## Proposed Resulting Kotlin

For the `POST /repos/{owner}/{repo}/check-runs` request body, the generated Kotlin should move
toward a single data class like this:

```kotlin
@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = Body.Serializer::class)
public data class Body(
  public val name: String,
  @SerialName("head_sha")
  public val headSha: String,
  @SerialName("details_url")
  public val detailsUrl: String? = null,
  @SerialName("external_id")
  public val externalId: String? = null,
  public val status: Status? = null,
  @SerialName("started_at")
  public val startedAt: Instant? = null,
  public val conclusion: Conclusion? = null,
  @SerialName("completed_at")
  public val completedAt: Instant? = null,
  public val output: Output? = null,
  public val actions: List<Action>? = null,
  public val additional: JsonObject? = null,
) {
  init {
    val matchesCompletedBranch =
      status == Status.Completed && conclusion != null

    val matchesQueuedBranch =
      status == null || status == Status.Queued || status == Status.InProgress

    require(
      listOf(matchesCompletedBranch, matchesQueuedBranch).count { it } == 1
    ) {
      "Body must satisfy exactly one OpenAPI oneOf branch"
    }
  }

  @Serializable
  public enum class Status(
    public val value: String,
  ) {
    @SerialName("queued")
    Queued("queued"),
    @SerialName("in_progress")
    InProgress("in_progress"),
    @SerialName("completed")
    Completed("completed"),
    @SerialName("waiting")
    Waiting("waiting"),
    @SerialName("requested")
    Requested("requested"),
    @SerialName("pending")
    Pending("pending"),
  }

  public object Serializer : KSerializer<Body> {
    override val descriptor: SerialDescriptor = generatedSerializer().descriptor

    override fun serialize(encoder: Encoder, value: Body) {
      // existing additionalProperties serializer pattern
    }

    override fun deserialize(decoder: Decoder): Body {
      // existing additionalProperties serializer pattern
    }
  }
}
```

### Why this is the right target

It preserves the actual schema shape:

- one payload object,
- shared fields at top level,
- additional properties supported,
- conditional `oneOf` validation enforced.

It also avoids the ergonomics problem from the old union output:

- callers should not have to wrap a mostly-shared payload in `Completed(...)` or `Two(...)`.

---

## Implementation Plan

1. Add object-composite validation metadata to `Model.Object`.
2. Build a typed-side normalization path for `type: object` + top-level `oneOf` / `anyOf` that:
   - merges the outer object into each branch,
   - extracts direct-property validation rules,
   - carries merged `additionalProperties`,
   - returns `Model.Object` instead of silently dropping the composite.
3. Update `RequestBody.toRequestBodyModel(...)` to use that normalized object-composite path instead
   of deleting `oneOf` / `anyOf`.
4. Teach `ObjectRenderer` to emit `init { require(...) }` from the new validation metadata.
5. Add focused tests:
   - `typed` test asserting validation metadata exists for the GitHub-like body,
   - `renderer` golden test for the resulting `Body` data class,
   - `renderer` round-trip / deserialize test that rejects `status = waiting`,
   - `renderer` deserialize test that rejects `status = completed` with no `conclusion`,
   - `renderer` deserialize test that accepts `status = null`,
   - `renderer` deserialize test that accepts `status = in_progress`.

---

## Scope Boundaries

This proposal intentionally does **not** try to solve every possible JSON Schema composite case.

Notably:

- it does not infer validation from prose descriptions,
- it does not solve arbitrary nested branch predicates on day one,
- it does not solve presence-vs-explicit-null tracking for optional nullable fields,
- it does not change the general non-request-body union strategy yet.

It only makes the current inline object-request-body flattening semantically correct for the class
of schemas represented by this GitHub `check-runs` example.

---

## Recommendation

Discuss this issue as:

- "object-composite validation was dropped during inline request-body flattening",

not as:

- "the current repository still generates the old sealed-union body".

That distinction matters, because the correct E2E fix is no longer "replace union rendering with an
object". The repository already does that. The remaining work is to make the flattened object keep
the original OpenAPI validation semantics.
