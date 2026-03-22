# ISSUE: anyOf Unions Use oneOf Deserialization Strategy

## Problem

The codebase treats `anyOf` and `oneOf` identically — both become `Model.Union` and both generate `attemptDeserialize` (try-each-in-order) serializers. This is correct for `oneOf` (exactly one type must match), but **wrong for `anyOf`** (multiple types may match a given payload).

With `anyOf`, `attemptDeserialize` picks the **first** type that doesn't throw, which may not be the **intended** type. This happens when object variants share required fields and differ only in optional fields — KotlinX Serialization defaults missing optional fields to `null`, so a superset variant silently swallows payloads intended for a subset variant.

### Scale

The GitHub OpenAPI spec contains:
- `oneOf`: 252 usages (current strategy is correct)
- `anyOf`: 27 usages (current strategy is broken)

All 27 `anyOf` usages are real multi-type unions (none are nullable shorthands).

---

## Concrete Example: Environment.protection_rules

### Simplified OpenAPI Schema

```json
{
  "protection_rules": {
    "type": "array",
    "items": {
      "anyOf": [
        {
          "type": "object",
          "required": ["id", "node_id", "type"],
          "properties": {
            "id": { "type": "integer" },
            "node_id": { "type": "string" },
            "type": { "type": "string", "example": "wait_timer" },
            "wait_timer": { "$ref": "#/components/schemas/wait-timer" }
          }
        },
        {
          "type": "object",
          "required": ["id", "node_id", "type"],
          "properties": {
            "id": { "type": "integer" },
            "node_id": { "type": "string" },
            "type": { "type": "string", "example": "required_reviewers" },
            "prevent_self_review": { "type": "boolean" },
            "reviewers": { "type": "array", "items": { ... } }
          }
        },
        {
          "type": "object",
          "required": ["id", "node_id", "type"],
          "properties": {
            "id": { "type": "integer" },
            "node_id": { "type": "string" },
            "type": { "type": "string", "example": "branch_policy" }
          }
        }
      ]
    }
  }
}
```

All three variants share the same required fields (`id`, `node_id`, `type`). They differ in optional fields (`wait_timer`, `prevent_self_review`, `reviewers`).

### Current Generated Code (Environment.kt)

```kotlin
@Serializable(with = ProtectionRules.Serializer::class)
public sealed interface ProtectionRules {

    @Serializable
    public data class IdAndNodeIdAndTypeAndWaitTimer(
        public val id: Long,
        @SerialName("node_id") public val nodeId: String,
        public val type: String,
        @SerialName("wait_timer") public val waitTimer: Long? = null,    // optional
    ) : ProtectionRules

    @Serializable
    public data class IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers(
        public val id: Long,
        @SerialName("node_id") public val nodeId: String,
        public val type: String,
        @SerialName("prevent_self_review") public val preventSelfReview: Boolean? = null, // optional
        public val reviewers: List<Reviewer>? = null,                                     // optional
    ) : ProtectionRules

    @Serializable
    public data class IdAndNodeIdAndType(
        public val id: Long,
        @SerialName("node_id") public val nodeId: String,
        public val type: String,
    ) : ProtectionRules

    public object Serializer : KSerializer<ProtectionRules> {
        override fun deserialize(decoder: Decoder): ProtectionRules {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder).json
            return json.attemptDeserialize(
                value,
                // Sorted by property count descending (more props = tried first)
                IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers::class to { ... }, // 5 props, tried FIRST
                IdAndNodeIdAndTypeAndWaitTimer::class to { ... },                     // 4 props, tried SECOND
                IdAndNodeIdAndType::class to { ... },                                 // 3 props, tried LAST
            )
        }
    }
}
```

### The Bug

Given this JSON payload for a `branch_policy` rule:

```json
{ "id": 3515, "node_id": "MDQ6R2F0ZTM1MTU=", "type": "branch_policy" }
```

**Expected:** Deserializes to `IdAndNodeIdAndType` (the branch_policy variant).

**Actual:** Deserializes to `IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers` because:
1. It's tried first (most properties)
2. `preventSelfReview` defaults to `null` (optional, missing in JSON → OK)
3. `reviewers` defaults to `null` (optional, missing in JSON → OK)
4. All required fields (`id`, `node_id`, `type`) are present → **deserialization succeeds**
5. `IdAndNodeIdAndTypeAndWaitTimer` and `IdAndNodeIdAndType` are never attempted

**Result:** Every protection rule payload deserializes as the reviewers variant, regardless of the actual rule type. The `waitTimer` and base variants are unreachable.

---

## Other Affected anyOf Unions

The same problem applies to any `anyOf` with overlapping object variants:

| Schema | anyOf Size | Issue |
|--------|-----------|-------|
| Environment.protection_rules | 3 objects | Superset swallows subset (shown above) |
| check-runs PATCH body | 2 objects | Overlapping status fields |
| code-scanning alerts PATCH body | 2 objects | Overlapping state fields |
| dependabot alerts PATCH body | 2 objects | Overlapping state fields |
| pages POST body | 2 objects | Overlapping source fields |
| pages PUT body | 5 objects | Multiple overlapping build configs |
| pulls requested_reviewers POST body | 2 objects | Overlapping reviewer fields |
| secret-scanning alerts PATCH body | 2 objects | Overlapping resolution fields |
| copilot content_exclusion items | 3 (string + 2 objects) | String vs object is fine; objects overlap |

Non-object `anyOf` unions (e.g., `simple-user | enterprise`, `simple-user | team`, `string | number | boolean`) are less affected because the types are structurally distinct — `attemptDeserialize` works correctly for those.

---

## Proposed Solution

### Step 1: Preserve anyOf vs oneOf Distinction in Model

Currently `SchemaTransformer.kt` converts both to `Model.Union` identically:

```kotlin
// SchemaTransformer.kt lines 78-84
schema.oneOf?.isNotEmpty() == true -> union(context, schema.oneOf!!)
schema.anyOf?.isNotEmpty() == true -> union(context, schema.anyOf!!)
```

Add a flag to `Model.Union` to track the source:

```kotlin
data class Union(
    ...
    val isAnyOf: Boolean = false,   // true when from anyOf, false when from oneOf
)
```

Set `isAnyOf = true` when transforming `anyOf` unions.

### Step 2: Use Key-Presence Discrimination for anyOf Object Unions

For `anyOf` unions where all cases are objects, generate a key-presence-based deserializer instead of `attemptDeserialize`:

```kotlin
override fun deserialize(decoder: Decoder): ProtectionRules {
    val value = decoder.decodeSerializableValue(JsonElement.serializer())
    val json = requireNotNull(decoder as? JsonDecoder).json
    val keys = value.jsonObject.keys
    return when {
        "wait_timer" in keys ->
            json.decodeFromJsonElement(IdAndNodeIdAndTypeAndWaitTimer.serializer(), value)
        "reviewers" in keys || "prevent_self_review" in keys ->
            json.decodeFromJsonElement(IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers.serializer(), value)
        else ->
            json.decodeFromJsonElement(IdAndNodeIdAndType.serializer(), value)
    }
}
```

**How to compute distinguishing keys:** For each object variant in the `anyOf`, find properties that are **unique to that variant** (not present in any other variant). Use those as discriminating keys. The variant with no unique keys becomes the `else` fallback.

For `protection_rules`:
- Variant 1 has unique key: `wait_timer`
- Variant 2 has unique keys: `prevent_self_review`, `reviewers`
- Variant 3 has no unique keys → fallback

### Step 3: Handle anyOf Unions Without Distinguishing Keys

When all object variants have the same property names (no unique keys), fall back to `attemptDeserialize` but with `coerceInputValues = false` and strict deserialization to prevent false positives.

Alternatively, if the variants have an implicit discriminator (like the `type` field in `protection_rules` whose example values `"wait_timer"`, `"required_reviewers"`, `"branch_policy"` map to each variant), use that as a hint. This is a stretch goal — the `example` field in the spec is not a formal discriminator.

### Step 4: Leave oneOf Unchanged

The existing `attemptDeserialize` strategy for `oneOf` is correct by definition — exactly one type must match. No changes needed.

---

## Implementation

### Files to Modify

| File | Change |
|------|--------|
| `typed/.../Model.kt` | Add `isAnyOf: Boolean` field to `Model.Union` |
| `typed/.../SchemaTransformer.kt` | Pass `isAnyOf = true` when resolving `anyOf` schemas |
| `typed/.../Union.kt` | Thread `isAnyOf` through the union construction |
| `renderer/.../UnionRenderer.kt` | In `buildDeserializeFunction()`: check `union.isAnyOf` and generate key-presence dispatch for object anyOf unions |

### Key Algorithm: Computing Distinguishing Keys

```
Input: List<ObjectVariant> where each has a set of property names
Output: Map<ObjectVariant, Set<String>> (unique keys per variant)

For each variant V:
    V.uniqueKeys = V.propertyNames - union(other variants' propertyNames)

If any variant has empty uniqueKeys:
    It becomes the else fallback (must be at most 1)
    If multiple have empty uniqueKeys → ambiguous, fall back to attemptDeserialize
```

---

## Acceptance Criteria

1. `Model.Union` carries `isAnyOf` flag distinguishing `anyOf` from `oneOf` origins.
2. `anyOf` object unions with distinguishing keys generate key-presence `when` blocks.
3. `anyOf` object unions without distinguishing keys fall back to `attemptDeserialize` (with a generation-time warning).
4. `oneOf` union deserialization is unchanged.
5. Non-object `anyOf` unions (primitive/ref mixtures) remain unchanged (they work correctly already).
6. Environment.protection_rules correctly dispatches to the right variant based on `wait_timer`/`reviewers` key presence.
7. All existing tests pass.
8. New tests cover anyOf key-presence dispatch for the protection_rules pattern.

---

## Dependencies

- None. Independent of other issues.
