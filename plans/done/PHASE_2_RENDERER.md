# Phase 2: Renderer

## Context

Phase 1 introduced `UnionDispatch` (NativeDiscriminator / TaggedCustom / Structural) on the model and updated the transformation to compute tag sets. This phase implements TaggedCustom rendering — generating custom serializers that use discriminator-tag-based `when(tag)` dispatch with collision sub-dispatch and unique-key optimization.

---

## Renderer changes

### `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRenderer.kt`

Change the main dispatch:
```kotlin
// OLD
return if (discriminator != null) toDiscriminatedTypeSpec(...)
else toNonDiscriminatedTypeSpec(...)

// NEW
return when (dispatch) {
    is UnionDispatch.NativeDiscriminator -> toNativeDiscriminatedTypeSpec(...)
    is UnionDispatch.TaggedCustom -> toTaggedCustomTypeSpec(...)
    UnionDispatch.Structural -> toStructuralTypeSpec(...)
}
```

- `toNativeDiscriminatedTypeSpec` = current `toDiscriminatedTypeSpec` (unchanged)
- `toStructuralTypeSpec` = current `toNonDiscriminatedTypeSpec` (unchanged)
- `toTaggedCustomTypeSpec` = **NEW** — similar to non-discriminated but with tag-based serializer

### `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRendererSerialization.kt`

Add TaggedCustom deserializer generation. The generated code shape:

```kotlin
override fun deserialize(decoder: Decoder): Union {
    val value = decoder.decodeSerializableValue(JsonElement.serializer())
    val json = requireNotNull(decoder as? JsonDecoder).json
    val tag = value.jsonObject["<propertyName>"]?.jsonPrimitive?.content

    return when (tag) {
        // Single-case tag groups: decode directly
        "v1", "v2" -> CaseA(json.decodeFromJsonElement(TypeA.serializer(), value))
        "v3" -> CaseB(json.decodeFromJsonElement(TypeB.serializer(), value))

        // Collision group (multiple cases share a tag): sub-dispatch
        "v4" -> {
            val keys = value.jsonObject.keys
            when {
                "uniqueKey" in keys -> CaseC(json.decodeFromJsonElement(TypeC.serializer(), value))
                else -> json.attemptDeserialize(value,
                    TypeD::class to { ... },
                    TypeE::class to { ... },
                )
            }
        }

        // Untagged cases (else branch):
        else -> json.attemptDeserialize(value,
            UntaggedCase::class to { ... },
        )
        // OR if no untagged cases:
        else -> throw SerializationException("Unknown tag: $tag for <UnionName>")
    }
}
```

Serialize side: same as current non-discriminated (`when(value) { is CaseA -> encode(...) }`).

### `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRendererNaming.kt`

- `serialNameOrNull` (line 39): only called for NativeDiscriminator now. Update to use `discriminatorValues.singleOrNull()`.
- For TaggedCustom, `@SerialName` is not added so this function isn't called.

### `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/RenderedUnionCase.kt`

TaggedCustom cases use the same rendering path as non-discriminated cases (wrapped/inlined logic). The discriminator property is NOT stripped, so case objects retain it.

### `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererInlineModels.kt` (line 19)

`inline.model.discriminator == null` → `inline.model.dispatch is UnionDispatch.Structural || inline.model.dispatch is UnionDispatch.TaggedCustom` (both need custom serializers and thus may need AttemptDeserialize utilities).

### `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/AnyOfUniqueKeyDispatch.kt`

Generalize to also work on collision subsets (currently only works on entire AnyOf unions). Extract the analysis logic so it can be called on a subset of cases within a collision group.

---

## Renderer tests

### `renderer/src/jvmTest/kotlin/io/github/nomisrev/render/UnionSpec.kt`

Update existing tests that reference the old `discriminator` field.

Add new golden test cases:
- `discriminated-tagged-custom-multivalue/` — CompoundFilter-like union with multi-value enums
- `discriminated-tagged-custom-collision/` — two cases sharing a tag value
- `discriminated-tagged-custom-partial/` — some cases tagged, some not

Update existing goldens:
- `discriminated-recursive-ref-fallback/` — should now use TaggedCustom instead of Structural
- `discriminated-enum-case/` — verify behavior stays correct

### Golden test files

Under `renderer/src/jvmTest/resources/kotlinTestData/union/`:
- New directories for tagged custom scenarios
- Update `CompoundFilter.kt` golden to use tag-based `when` dispatch instead of `attemptDeserialize`

---

## Verification

1. `./gradlew :renderer:jvmTest` — renderer and golden tests pass
2. `./gradlew build` — full build passes
3. Manually inspect generated `CompoundFilter` serializer: should use `when(tag)` dispatch with `"eq"`, `"ne"`, etc. mapping to ComparisonFilter and `"and"`, `"or"` mapping to CompoundFilter
4. Existing native discriminator tests (e.g., `discriminated-basic`) produce identical output
5. Existing non-discriminated tests (e.g., `all-primitives`, `references`) produce identical output
