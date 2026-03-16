# Phase 5 — Unions with Discriminator

Generate `sealed interface` with `@JsonClassDiscriminator` for unions that have a discriminator property.

## Tasks

- [ ] Implement `Model.Union.toTypeSpec(config: RenderConfig)` for discriminated unions:
  - `@Serializable` annotation
  - `@OptIn(ExperimentalSerializationApi::class)` annotation
  - `@JsonClassDiscriminator("discriminatorField")` annotation
  - `sealed interface` with case subtypes
- [ ] Case rendering — determine wrapped vs inlined:
  - **Wrapped** (value class): references, primitives, collections, top-level objects/enums, nested unions, discriminated objects
    - `@Serializable @JvmInline value class CaseName(val value: T) : Union`
    - `@SerialName("discriminatorValue")` from `Case.discriminator`
  - **Inlined** (direct implementation): inline objects and inline enums (non-top-level)
    - Object: `data class Name(...) : Union` — uses Object rendering with parent sealed interface
    - Enum: `enum class Name : Union { ... }` — uses Enum rendering with parent sealed interface
    - data object: `data object Empty : Union` for Unit/empty
    - value class: `value class Name(val prop: T) : Union` for single-property inline objects
- [ ] Decision logic for wrapped vs inlined:
  - `ContextHolder` with `isTopLevel()` → wrapped
  - `Reference` → wrapped
  - `Primitive`, `Date`, `DateTime`, `Uuid`, `ByteArray`, `FreeFormJson` → wrapped
  - `Union`, `DiscriminatedObject` → wrapped
  - `Collection` → wrapped
  - Inline `Object` → inlined (renders as subtype)
  - Inline `Enum` → inlined (renders as subtype)
- [ ] Case naming:
  - Use `NamingContext.UnionCase.value` from the typed module
  - For wrapped references: `Case{TypeName}`
  - For primitives: `CaseString`, `CaseInt`, etc.
  - For Unit: `Empty`
- [ ] Wire into `generateModels()` — filter `Model.Union` with non-null `discriminator`

## Golden Tests

- [ ] `union/discriminated-basic` — simple discriminated union with object cases
- [ ] `union/discriminated-wrapped` — discriminated union with reference (value class) cases
- [ ] `union/discriminated-inlined` — discriminated union with inline object cases
- [ ] `union/discriminated-mixed` — mix of wrapped and inlined cases
- [ ] `union/discriminated-enum-case` — discriminated union with inline enum case
- [ ] `union/discriminated-value-class-case` — single-property inline object → value class subtype

## Files to Create/Modify

- **Create**: `renderer/.../UnionRenderer.kt` — `Model.Union.toTypeSpec()` + case rendering
- **Modify**: `renderer/.../Generate.kt` — wire union generation
- **Modify**: `renderer/.../ObjectRenderer.kt` — support `parentInterface` parameter for inlined cases
- **Modify**: `renderer/.../EnumRenderer.kt` — support `parentInterface` parameter for inlined cases
- **Create**: golden test resource files under `renderer/src/test/resources/kotlinTestData/union/`
- **Create**: `renderer/.../UnionSpec.kt` — test suite

## Key Decisions

- Custom KotlinX.serialization KSerializer needed
- The typed module determines case names via `NamingContext.UnionCase` — renderer reads them
- Wrapped cases use `@SerialName` with the discriminator value on the value class
- Inlined cases use `@SerialName` with the discriminator value on the data class/enum itself
