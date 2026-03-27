# Phase 1: Model + Transformation

## Context

The code generator currently treats union deserialization as binary: native `@JsonClassDiscriminator` or structural `attemptDeserialize(...)` fallback. This loses discriminator information for unions where native kotlinx polymorphism is invalid (multi-value enums, recursive refs, duplicate tags). We introduce a **TaggedCustom** tier that generates a custom serializer using tag-based dispatch, preserving discriminator routing even when native polymorphism fails.

This phase covers the model changes and transformation logic. Phase 2 (separate plan) covers renderer changes.

## Design Decisions

1. **Per-case tag values on `Case`** as `discriminatorValues: Set<String>` (replaces `discriminator: String?`)
2. **Two dispatch tiers** + structural:
   - `NativeDiscriminator(propertyName)` — strict 1:1 mapping, uses `@JsonClassDiscriminator`
   - `TaggedCustom(propertyName)` — custom serializer with `when(tag)` dispatch (handles multi-value, collisions, partial coverage)
   - `Structural` — no discriminator property exists at all
3. **`dispatch: UnionDispatch`** stored as a field on `Model.Union` (not computed)
4. **Else branch** in TaggedCustom: try only untagged cases (empty `discriminatorValues`); throw if all cases have tags
5. **Collision sub-dispatch** reuses `AnyOfUniqueKeyDispatchAnalysis` within collision groups
6. **`@SerialName`** only for `NativeDiscriminator` cases
7. **Schema-name fallback** valid per OpenAPI spec for both tiers (it IS the intended wire value per spec)
8. **Inferred discriminators** (no explicit `discriminator` in schema) feed into both tiers
9. **Strip discriminator property** only for `NativeDiscriminator`; TaggedCustom keeps it (case serializer receives full JSON)
10. **TaggedCustom case naming**: single discriminator value → use it; multi-value → ref schema name / generated name

---

## Model changes

**`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/Model.kt`**

Replace on `Model.Union`:
```kotlin
// OLD
val discriminator: String?

// NEW
val dispatch: UnionDispatch
```

Replace on `Model.Union.Case`:
```kotlin
// OLD
data class Case(val model: Model, val discriminator: String?)

// NEW
data class Case(val model: Model, val discriminatorValues: Set<String>)
```

Add new sealed interface (in Model.kt or as a separate file):
```kotlin
@Serializable
sealed interface UnionDispatch {
    @Serializable
    data object Structural : UnionDispatch

    @Serializable
    data class NativeDiscriminator(val propertyName: String) : UnionDispatch

    @Serializable
    data class TaggedCustom(val propertyName: String) : UnionDispatch
}
```

Update `Model.OneOf` and `Model.AnyOf` constructors: replace `discriminator: String?` parameter with `dispatch: UnionDispatch`.

---

## Transformation changes

**`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/Union.kt`**

### 1. Change `UnionDiscriminator` internal type to carry tag sets

```kotlin
private data class UnionDiscriminator(
    val propertyName: String,
    val caseValues: List<Set<String>>,  // was List<String?>
)
```

### 2. Change `discriminatorValueForSubtype` (line 520) to return `Set<String>`

Currently returns `String?`. Change to return `Set<String>`:
- Explicit mapping: collect ALL matching keys → `Set<String>` (change `firstOrNull` to filter all)
- Single-value enum: `setOf(value)`
- **Multi-value enum: `values.toSet()`** (was: return null — THIS IS THE KEY CHANGE at line 549)
- Schema-name fallback: `setOf(schemaName)` (except `"#"`)
- No info: `emptySet()`

### 3. Change `resolveUnionDiscriminator` (line 72)

- Always return `UnionDiscriminator` when a discriminator property name exists (explicit or inferred), even if not native-safe
- Remove the `takeIf(UnionDiscriminator::hasConcreteDistinctCaseValues)` filter — that's now a dispatch classification concern

### 4. Add dispatch classification after tag extraction

```kotlin
private fun classifyDispatch(discriminator: UnionDiscriminator?): UnionDispatch = when {
    discriminator == null -> UnionDispatch.Structural
    discriminator.caseValues.all { it.size == 1 }
        && discriminator.caseValues.map { it.single() }.distinct().size == discriminator.caseValues.size
        -> UnionDispatch.NativeDiscriminator(discriminator.propertyName)
    else -> UnionDispatch.TaggedCustom(discriminator.propertyName)
}
```

### 5. Update `buildUnionCases` (line 122)

Pass dispatch tier to control case normalization:
- `NativeDiscriminator`: flatten allOf → strip discriminator → hoist single property (current `toDiscriminatedUnionCaseModel`)
- `TaggedCustom`: flatten allOf only (don't strip, don't hoist) — OR use regular `toModel` path
- `Structural`: regular `toModel` path (current behavior)

Create `Case(model, discriminatorValues = tagSet)` instead of `Case(model, discriminatorValue)`.

### 6. Update case naming (line 131-134)

- NativeDiscriminator: single value → `NamingContext.UnionCase(value)` (current behavior)
- TaggedCustom with single value: `NamingContext.UnionCase(value)`
- TaggedCustom with multi-value: fallback to `name.unionCase(index, refOrSchema, ...)`
- Structural: fallback (current behavior)

### 7. Update `inferTagOnlyDiscriminatorOrNull` (line 91)

Currently returns only strict 1:1 inferred discriminators. Update to also return multi-value tag sets so inferred discriminators can feed into TaggedCustom tier.

### 8. Update `buildOneOf` and `buildAnyOf` (lines 29-62)

Pass `dispatch` instead of `discriminator?.propertyName` to the union constructors.

---

## Downstream consumers to update

- **`typed/.../routes/RequestBody.kt`** (line 125): `model.discriminator == null` → `model.dispatch is UnionDispatch.Structural`
- **`typed/.../transformers/DiscriminatedObject.kt`**: uses `schema.discriminator` (raw OpenAPI schema), not `Model.Union.discriminator` — no change needed
- **`typed/.../transformers/AllOf.kt`**: same — uses raw schema discriminator, not model field

---

## Typed tests

**`typed/src/commonTest/kotlin/io/github/nomisrev/transformers/UnionSpec.kt`**

Update existing assertions from `discriminator = "value"` to `discriminatorValues = setOf("value")` and `discriminator = "propertyName"` to `dispatch = NativeDiscriminator("propertyName")` or `TaggedCustom("propertyName")`.

Add new tests:
- Multi-value enum → TaggedCustom with full set
- Recursive `#` → TaggedCustom (not Structural, not Native)
- Duplicate single tag → TaggedCustom with collision
- Partial tag coverage → TaggedCustom with some empty sets
- Inferred discriminator with multi-value → TaggedCustom

---

## Verification

1. `./gradlew :typed:allTests` — model and transformation tests pass
2. Compile check: `./gradlew :renderer:compileKotlin` — renderer compiles against new model (may need stub changes)
3. `./gradlew build` — full build passes (renderer tests may need golden updates, acceptable in this phase)
