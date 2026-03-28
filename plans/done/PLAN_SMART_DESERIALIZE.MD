# PLAN: Smart Union Deserialization

## Summary

We currently treat union deserialization as a binary choice:

- native discriminator via `@JsonClassDiscriminator(...)`
- or structural fallback via `attemptDeserialize(...)`

That split is too coarse.

It works for unions where every case has exactly one stable wire tag, but it breaks down for:

- recursive unions such as `CompoundFilter`, where one case is `#`
- unions where one case has multiple valid discriminator values
- unions where two cases share the same discriminator value
- unions where the schema has a discriminator field, but Kotlinx native polymorphism cannot represent it safely

The goal of this plan is to introduce a third mode:

- **native discriminator**
- **custom tagged serializer**
- **structural fallback**

This preserves useful discriminator information even when native `@JsonClassDiscriminator` is invalid.

---

## Problem

### Current behavior

Today, plain unions are effectively reduced to:

1. If `Model.Union.discriminator != null`, render a native discriminated hierarchy.
2. Otherwise, render a custom serializer using `attemptDeserialize(...)`.

That means we lose too much information once a union is not native-safe.

### Concrete failures

#### 1. Recursive refs

`CompoundFilter.filters` is a `oneOf` of:

- `ComparisonFilter`
- `$recursiveRef: "#"`

with `discriminator.propertyName = "type"`.

The field `type` is real and meaningful, but it is not a valid native subtype discriminator because:

- `ComparisonFilter.type` has multiple values: `eq`, `ne`, `gt`, `gte`, `lt`, `lte`, ...
- `CompoundFilter.type` has multiple values: `and`, `or`
- the recursive case is the same schema, not a unique subtype name

Blind native discriminator rendering produced nonsense such as `@SerialName("#")`.

#### 2. Duplicate discriminator values

The excluded OpenAI path `/responses/{response_id}/input_items` has a similar shape:

- a discriminator is declared
- some cases collapse to the same single discriminator value

That means a native `@JsonClassDiscriminator` strategy is also invalid there, because the wire value does not identify a unique subtype.

#### 3. Lost tag information

Our recent conservative fix correctly avoids native discriminator rendering for invalid cases, but once we fall back to `attemptDeserialize(...)`, we throw away the fact that a discriminator field still exists and is still useful as a routing hint.

---

## Desired behavior

We should distinguish between:

### 1. Native-safe discriminator

Use native `@JsonClassDiscriminator(...)` only when:

- every case has exactly one wire tag
- the tags are pairwise distinct
- the case can be rendered as a single Kotlin subtype with a single serial name

Example:

- `OutputItem.type = message | file_search_call | function_call | ...`

### 2. Tagged but not native-safe

Use a generated custom serializer when:

- the union has a usable discriminator field
- but native polymorphism is invalid

Examples:

- one case has multiple values
- one case is recursive
- two or more cases share the same value

### 3. No usable discriminator

Use existing structural fallback when:

- no reliable discriminator tags can be extracted
- or only some cases have tags and the rest do not

---

## Proposed model changes

The current `Model.Union` shape is too small:

```kotlin
sealed interface Union : Model, ContextHolder {
    val discriminator: String?

    data class Case(
        val model: Model,
        val discriminator: String?
    )
}
```

That only supports:

- zero discriminator values
- or one discriminator value per case

It cannot represent:

- multiple valid values for one case
- duplicate values across cases
- partial tag coverage

### Replace with an explicit dispatch strategy

Introduce something like:

```kotlin
sealed interface UnionDispatch {
    data object Structural : UnionDispatch

    data class NativeDiscriminator(
        val propertyName: String,
        val caseValues: List<String>,
    ) : UnionDispatch

    data class TaggedCustom(
        val propertyName: String,
        val caseValues: List<Set<String>>,
    ) : UnionDispatch
}
```

and update `Model.Union` to carry:

```kotlin
val dispatch: UnionDispatch
```

This keeps the routing metadata even when native rendering is not valid.

### Backwards-compatibility option

If a larger model refactor is too disruptive, an intermediate step is:

- keep `discriminator: String?` only for native-safe unions
- add a second field such as `taggedDiscriminator: String?`
- add per-case `discriminatorValues: Set<String>`

This is less clean than `UnionDispatch`, but still enough to implement smart rendering.

---

## Tag extraction algorithm

Tag extraction should operate on **sets of wire values per case**, not single nullable strings.

For each union case:

1. Check explicit `discriminator.mapping`.
   - If mapping resolves to this case, the mapping key is a tag.
   - This yields a singleton set.

2. Otherwise inspect the discriminator property on the flattened subtype schema.
   - If the property is a single-value enum, yield that singleton.
   - If the property is a multi-value enum, yield the full set.
   - If the property exists but is unconstrained, yield no tag.

3. For schema-name fallback:
   - only use it when the subtype is a named reference
   - never use it for `#`
   - only use it when the subtype does not already declare an enum that contradicts schema-name dispatch

4. Flatten inherited `allOf` before reading the discriminator property.

This yields:

- `Set<String>` per case
- possibly empty

### Classification

After tag extraction:

#### Native discriminator

Choose `NativeDiscriminator` when:

- every case has exactly one tag
- all tags are unique

#### Tagged custom serializer

Choose `TaggedCustom` when:

- every case has at least one tag
- but the native conditions fail

This includes:

- multi-value cases
- duplicate tag values across cases

#### Structural fallback

Choose `Structural` when:

- at least one case has no tags
- or extraction fails

---

## Renderer strategy

### Current split

`UnionRenderer.kt` currently branches like this:

```kotlin
return if (discriminator != null) toDiscriminatedTypeSpec(...)
else toNonDiscriminatedTypeSpec(...)
```

That should instead become:

```kotlin
return when (dispatch) {
    is UnionDispatch.NativeDiscriminator -> toNativeDiscriminatedTypeSpec(...)
    is UnionDispatch.TaggedCustom -> toTaggedCustomTypeSpec(...)
    UnionDispatch.Structural -> toStructuralTypeSpec(...)
}
```

### Native mode

Keep the current implementation:

- `@JsonClassDiscriminator`
- `@SerialName` on each subtype
- no custom serializer

### Tagged custom mode

Render the union like a normal custom-serializer union, but use the discriminator field to narrow candidates before decoding.

This means:

- no `@JsonClassDiscriminator`
- still generate `Serializer : KSerializer<Union>`
- read the JSON discriminator field directly
- dispatch by value

Pseudo-shape:

```kotlin
override fun deserialize(decoder: Decoder): Union {
    val value = decoder.decodeSerializableValue(JsonElement.serializer())
    val json = requireNotNull(decoder as? JsonDecoder).json
    val tag = value.jsonObject["type"]?.jsonPrimitive?.content

    return when (tag) {
        "eq", "ne", "gt", "gte", "lt", "lte" ->
            CaseComparisonFilter(decodeFromJsonElement(ComparisonFilter.serializer(), value))

        "and", "or" ->
            CaseCompoundFilter(decodeFromJsonElement(CompoundFilter.serializer(), value))

        else ->
            json.attemptDeserialize(
                value,
                ...
            )
    }
}
```

### Structural fallback mode

Keep current behavior:

- `attemptDeserialize(...)`
- or existing `anyOf` unique-key dispatch where applicable

---

## Collision handling

The main reason to add tagged custom mode is not just multi-value enums. It is also duplicate tags.

### Example

If two cases both resolve to `"message"`, native discriminator is impossible.

But a custom tagged serializer can still do:

1. read `"type": "message"`
2. narrow to the small set of `"message"` cases
3. use structural dispatch only within that subset

Pseudo-shape:

```kotlin
when (tag) {
    "message" ->
        json.attemptDeserialize(
            value,
            MessageCaseA::class to { ... },
            MessageCaseB::class to { ... },
        )
}
```

### Important constraint

If the collision subset is structurally indistinguishable, then the schema is genuinely ambiguous.

In that case we should:

- keep deterministic fallback behavior
- but emit a generation diagnostic

We should not silently pretend the union is safely discriminated.

---

## anyOf interaction

Blind `attemptDeserialize(...)` is already known to be risky for overlapping `anyOf` objects.

So tagged custom mode should cooperate with existing `anyOf` dispatch logic:

- first narrow by discriminator value
- then, if multiple `anyOf` candidates remain:
  - prefer unique-key dispatch if available
  - otherwise use `attemptDeserialize(...)`

This gives us:

- much better behavior than current try-everything fallback
- no invalid native discriminator hierarchies

---

## Request body behavior

We already adjusted request-body handling so an explicitly discriminated request body remains `SetBody` even when union modeling falls back from native discriminator.

That should stay.

Rationale:

- route shape should follow the OpenAPI author intent
- serializer strategy is a separate concern from route/body overload strategy

---

## Diagnostics

Add generation diagnostics for these cases:

### Info

- native discriminator downgraded to tagged custom because a case has multiple wire values
- native discriminator downgraded to tagged custom because a case is recursive

### Warning

- discriminator value collision: multiple cases share one or more tags
- falling back to structural dispatch within a collision subset

### Error or warning

- discriminator declared, but no tags can be extracted for one or more cases
- ambiguous collision subset with no distinguishing structure

The initial implementation can use warnings rather than hard failures.

---

## Implementation phases

## Phase 1: Represent dispatch strategy

### Changes

- update `Model.Union` to carry explicit dispatch metadata
- update union transformation to compute tag sets
- keep current native discriminator logic as a special case

### Files

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/Model.kt`
- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/Union.kt`

### Tests

- native-safe discriminator remains native
- multi-value discriminator becomes tagged custom
- recursive `#` never becomes schema-name fallback
- duplicate tag values are preserved as collisions, not flattened away

## Phase 2: Render tagged custom serializers

### Changes

- add `TaggedCustom` rendering branch
- generate discriminator-guided deserialize code
- keep current non-discriminated serializer layout for case wrappers

### Files

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRenderer.kt`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRendererSerialization.kt`
- possibly `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/RenderedUnionCase.kt`

### Tests

- new golden for `CompoundFilter`
- existing native discriminator goldens unchanged
- existing non-discriminated goldens unchanged

## Phase 3: Add collision subset dispatch

### Changes

- group cases by tag value
- for single-case groups, decode directly
- for multi-case groups, use subset dispatch
- prefer anyOf unique-key logic when possible

### Tests

- minimal reproducer with two cases sharing one tag
- OpenAI `/responses/{response_id}/input_items` reproducer

## Phase 4: Diagnostics and OpenAI path re-enable

### Changes

- surface warnings/errors from ambiguous tagged unions
- remove the OpenAI excluded path once generation is stable

### Tests

- integration test no longer excludes `/responses/{response_id}/input_items`
- OpenAI integration build passes

---

## Test matrix

We should add focused test coverage for:

### typed

- single unique tag per case
- explicit mapping
- inherited `allOf` discriminator tags
- multi-value enum tags
- recursive `#`
- duplicate single tag
- partial tag coverage

### renderer

- native discriminated union golden
- tagged custom union golden
- duplicate-tag collision golden
- request-body union stays `SetBody`

### integration

- OpenAI `CompoundFilter`
- OpenAI `/responses/{response_id}/input_items`

---

## Success criteria

This plan is complete when:

1. `CompoundFilter` generates as a recursive custom-tagged union, not `@JsonClassDiscriminator("type")`.
2. Native discriminated unions still render natively.
3. Duplicate discriminator values no longer force invalid native polymorphism.
4. `/responses/{response_id}/input_items` can be re-enabled.
5. Ambiguous schemas produce diagnostics instead of silent bogus subtype mappings.

---

## Recommended implementation order

1. Keep the current conservative native-validity gate.
2. Add explicit dispatch metadata to `Model.Union`.
3. Implement tagged custom rendering for disjoint multi-value cases.
4. Add collision subset dispatch.
5. Re-enable the excluded OpenAI path.

This order minimizes regressions and keeps every phase shippable.
