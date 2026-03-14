# Phase 6 — Unions without Discriminator

Generate `sealed interface` with custom serializer using `attemptDeserialize` for unions without a discriminator.

## Tasks

- [ ] Generate `SerializationUtils.kt`:
  - `attemptDeserialize(json: JsonElement, vararg block: Pair<KClass<*>, (JsonElement) -> A>): A`
  - `UnionSerializationException` class
  - Only generated when at least one non-discriminated union exists
- [ ] Extend `Model.Union.toTypeSpec()` for non-discriminated unions:
  - `@Serializable(with = Union.Serializer::class)` annotation (instead of `@JsonClassDiscriminator`)
  - Same case rendering as Phase 5 (wrapped vs inlined)
- [ ] Generate custom `object Serializer : KSerializer<Union>` inside sealed interface:
  - `override val descriptor: SerialDescriptor`
  - `override fun serialize(encoder: Encoder, value: Union)` — dispatch on `value` type:
    - Wrapped cases: `encoder.encodeSerializableValue(T.serializer(), value.value)` — unwrap `.value`
    - Inlined cases: `encoder.encodeSerializableValue(CaseName.serializer(), value)` — direct
  - `override fun deserialize(decoder: Decoder): Union` — uses `attemptDeserialize`:
    - Decode `JsonElement` first
    - Try each case in specificity order, catching failures
- [ ] Deserialization order (most specific first):
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
- [ ] Open Enum Pattern:
  - Detect: exactly 2 cases, one `Enum` + one `String`
  - Generate specialized serializer: match known enum values first, fall back to string wrapper
- [ ] Handle `is` keyword for `when` branches in serialize (KotlinPoet `CodeBlock`)

## Golden Tests

- [ ] `union/nondiscriminated-basic` — union of primitives (String + Int)
- [ ] `union/nondiscriminated-objects` — union of inline objects
- [ ] `union/nondiscriminated-mixed` — mix of objects, primitives, references
- [ ] `union/nondiscriminated-serializer` — full serializer output verification
- [ ] `union/open-enum` — open enum pattern (Enum + String)
- [ ] `union/nondiscriminated-ordering` — verify deserialization order in serializer

## Files to Create/Modify

- **Modify**: `renderer/.../UnionRenderer.kt` — add non-discriminated serializer generation
- **Create**: `renderer/.../SerializationUtilsRenderer.kt` — generates `SerializationUtils.kt`
- **Modify**: `renderer/.../Generate.kt` — conditionally generate SerializationUtils.kt
- **Create**: golden test resource files under `renderer/src/test/resources/kotlinTestData/union/`
- **Modify**: `renderer/.../UnionSpec.kt` — add test cases

## Key Decisions

- `SerializationUtils.kt` is generated alongside model files, not as a separate module dependency
- The open enum serializer is a specialized path — not a generic union serializer
- Deserialization order is critical for correctness — wider types must be tried after narrower ones
- The serializer `when` block must handle all cases exhaustively
