# Phase 7 — Discriminated Objects

Generate `sealed interface` with abstract properties from `allOf` + `discriminator` patterns.

## Tasks

- [ ] Implement `Model.DiscriminatedObject.toTypeSpec(config: RenderConfig)`:
  - `@Serializable` annotation
  - `@OptIn(ExperimentalSerializationApi::class)` annotation
  - `@JsonClassDiscriminator("discriminatorField")` annotation
  - `sealed interface` with abstract `val` properties for `abstractProperties`
- [ ] Abstract property rendering:
  - Each entry in `abstractProperties` → `val name: Type` on the sealed interface
  - Type via `Model.toTypeName(config)`
  - Only include properties where type is consistent across all subtypes (skip if types don't match — noted in MODEL_SPEC)
- [ ] Subtype rendering — reuses Object rendering:
  - Each entry in `subtypes: List<Object>` rendered as a subtype
  - `@SerialName("discriminatorValue")` on each subtype
  - Abstract properties → `override val name: Type`
  - Subtype-specific properties → `val name: Type`
  - Shape rules: data class / value class / data object (same as Phase 2)
- [ ] Subtype naming:
  - From `NamingContext.DiscriminatedObjectCase.discriminator` value
  - PascalCase conversion
- [ ] Wire into `generateModels()` — filter `Model.DiscriminatedObject` from models

## Golden Tests

- [ ] `discriminated/basic` — sealed interface with 2 subtypes, shared id property
- [ ] `discriminated/value-class-subtype` — subtype with single property → value class
- [ ] `discriminated/data-object-subtype` — subtype with no extra properties → data object (only overrides)
- [ ] `discriminated/multiple-abstract` — sealed interface with multiple abstract properties
- [ ] `discriminated/nested-properties` — subtypes with complex nested property types

## Files to Create/Modify

- **Create**: `renderer/.../DiscriminatedObjectRenderer.kt` — `Model.DiscriminatedObject.toTypeSpec()`
- **Modify**: `renderer/.../ObjectRenderer.kt` — support rendering as discriminated object subtype (override properties, parent interface)
- **Modify**: `renderer/.../Generate.kt` — wire discriminated object generation
- **Create**: golden test resource files under `renderer/src/test/resources/kotlinTestData/discriminated/`
- **Create**: `renderer/.../DiscriminatedObjectSpec.kt` — test suite

## Key Decisions

- Kotlinx.serialization handles discriminated object (de)serialization natively via `@JsonClassDiscriminator`
- Abstract properties that have inconsistent types across subtypes are excluded from the sealed interface
- Subtypes follow the same shape rules as regular Objects (Phase 2) but with `override` on inherited properties
- The discriminator property itself is NOT included as an abstract property — it's handled by `@JsonClassDiscriminator`
