# Phase 2 — Objects (Basic)

Generate `data class`, `value class`, and `data object` from `Model.Object` without `additionalProperties`.

## Tasks

- [ ] Implement `Model.Object.toTypeSpec(config: RenderConfig)` extension:
  - **data object**: 0 properties, `additionalProperties: false`
  - **value class**: 1 property, `additionalProperties: false` — `@JvmInline` when JVM target
  - **data class**: 2+ properties, or 1 property with additionalProperties
- [ ] Property rendering:
  - JSON name → `toParamName()` for Kotlin parameter name
  - `@SerialName("json_name")` when param name ≠ JSON name
  - Type via `Model.toTypeName(config)`
- [ ] Nullability rules:
  - `required + not nullable` → `T`
  - `not required` → `T? = null`
  - `required + nullable + no default` → `T?` (no `= null`!)
  - `required + nullable + has default` → `T? = null`
- [ ] Default values:
  - `!isRequired || isNullable` → `= null`
  - `isRequired && hasDefault` → `@Required` annotation + literal default value
  - Support defaults for: String, Int, Long, Float, Double, Boolean, Enum, Collection
- [ ] Annotations:
  - `@Serializable` always
  - `@SerialName` on properties when needed
  - `@Required` when `isRequired && hasDefault`
  - `@OptIn(ExperimentalUuidApi::class)` when any property uses Uuid
- [ ] Nested inline types:
  - Properties with inline schemas (non-`$ref`) → generate nested TypeSpec inside parent
  - Recursively render nested Objects, Enums inside the parent class body
- [ ] Description as KDoc on the class (when present)
- [ ] Wire into `generateModels()` — filter `Model.Object` (without additionalProperties) from models

## Golden Tests

- [ ] `object/dataclass` — standard multi-property data class
- [ ] `object/valueclass` — single-property value class with @JvmInline
- [ ] `object/dataobject` — zero-property data object
- [ ] `object/nullability` — all 4 nullability combinations
- [ ] `object/defaults` — properties with default values + @Required
- [ ] `object/serialname` — properties requiring @SerialName
- [ ] `object/nested` — object with inline nested object/enum properties
- [ ] `object/description` — object with KDoc from description

## Files to Create/Modify

- **Create**: `renderer/.../ObjectRenderer.kt` — `Model.Object.toTypeSpec()` + `toFileSpec()`
- **Modify**: `renderer/.../Generate.kt` — wire object generation
- **Create**: golden test resource files under `renderer/src/test/resources/kotlinTestData/object/`
- **Create**: `renderer/.../ObjectSpec.kt` — test suite

## Key Decisions

- Property order matches declaration order in the OpenAPI schema (preserved by `Map` iteration order)
- Nested types are rendered as inner classes/enums inside the parent data class
- `@Required` is from `kotlinx.serialization` — indicates field must always be serialized even though it has a default
