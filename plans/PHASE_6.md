# Phase 6 — Unions without Discriminator

Generate `sealed interface` with custom serializer using `attemptDeserialize` for unions without a discriminator.

## Tasks

- [x] Generate `SerializationUtils.kt`:
  - `attemptDeserialize(json: JsonElement, vararg block: Pair<KClass<*>, (JsonElement) -> A>): A`
  - `UnionSerializationException` class
  - Only generated when at least one non-discriminated union exists
- [x] Extend `Model.Union.toTypeSpec()` for non-discriminated unions:
  - `@Serializable(with = Union.Serializer::class)` annotation (instead of `@JsonClassDiscriminator`)
  - Same case rendering as Phase 5 (wrapped vs inlined)
- [x] Generate custom `object Serializer : KSerializer<Union>` inside sealed interface:
  - `override val descriptor: SerialDescriptor`
  - `override fun serialize(encoder: Encoder, value: Union)` — dispatch on `value` type:
    - Wrapped cases: `encoder.encodeSerializableValue(T.serializer(), value.value)` — unwrap `.value`
    - Inlined cases: `encoder.encodeSerializableValue(CaseName.serializer(), value)` — direct
  - `override fun deserialize(decoder: Decoder): Union` — uses `attemptDeserialize`:
    - Decode `JsonElement` first
    - Try each case in specificity order, catching failures
- [x] Deserialization order (most specific first):
  1. Objects without additionalProperties (more properties → higher priority)
  2. Objects with typed additionalProperties schema
  3. Objects with additionalProperties allowed
  4. DiscriminatedObjects
  5. Nested Unions
  6. Enums (before String)
  7. Collections
  8. References
  9. Primitives: Int → Long → Float → Double → Boolean → Unit
  10. String-like: Uuid → Date → DateTime → ByteArray
  11. String (swallows other string types)
  12. FreeFormJson / JsonElement (last resort)
- [x] Open Enum Pattern:
  - Detect: exactly 2 cases, one `Enum` + one `String`
  - Generate specialized serializer: match known enum values first, fall back to string wrapper
- [x] Descriptor uses declaration order, deserialize uses priority order
- [x] Inlined cases use nested type's own serializer (not the model's serializer)
- [x] Package name sanitization for SerializationUtils matches model files

## Golden Tests

- [x] `union/all-primitives` — union of all typed primitives (String, Int, Float, Double, Boolean, Date, DateTime, Binary, Uuid)
- [x] `union/enum-and-primitive` — open enum pattern (Enum + String)
- [x] `union/collection-and-primitive` — collection + primitive
- [x] `union/inlined-object-and-primitives` — inlined object + primitives with deserialization ordering
- [x] `union/references` — union of referenced types (Person, Company)
- [x] `union/overlapping-objects` — overlapping objects (more-specific first)
- [x] `union/additional-properties-last` — strict object + object with additionalProperties
- [x] `union/discriminated-primitive` — non-discriminated union (String + ref, was misclassified as discriminated)

## Known Limitations

- `{}` (empty/any-type schema) in union cases hits `TODO("Nested complex union case?")` in the typed module
- FreeFormJson (`JsonElement`) as a union case is not yet supported for the same reason

## Files Modified

- `renderer/.../UnionRenderer.kt` — non-discriminated serializer generation, descriptor ordering, type reference fixes
- `renderer/.../SerializationUtilsRenderer.kt` — generates `AttemptDeserialize.kt` with package sanitization
- `renderer/.../Generate.kt` — conditionally generates SerializationUtils
- `renderer/src/jvmTest/.../UnionSpec.kt` — non-discriminated union test cases
- `renderer/src/jvmTest/.../TestBalloonDsl.kt` — per-test package isolation
- Golden test files under `renderer/src/jvmTest/resources/kotlinTestData/union/`
