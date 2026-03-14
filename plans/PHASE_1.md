# Phase 1 — Enums

Generate `enum class` from `Model.Enum`. First model type with full golden test coverage.

## Tasks

- [ ] Implement `Model.Enum.toTypeSpec(config: RenderConfig)` extension:
  - `@Serializable` annotation always
  - PascalCase enum entry names via `toEnumValueName()`
  - `@SerialName("raw_value")` when raw value ≠ PascalCase name
  - `@JsName("_Name")` when targeting JS and name starts with digit or is invalid JS identifier
  - Semicolon after last entry
- [ ] Handle inner type: string enums (default), integer enums
- [ ] Handle special values: `"*"` → `Star`, `"/"` → `Slash`, backtick-escaping for invalid identifiers
- [ ] Handle `null` values in enum list → mapped to `"null"` → `Null` entry
- [ ] Handle single-value enums (generates normally, just one entry)
- [ ] Implement `Model.Enum.toFileSpec(config: RenderConfig)` — wraps TypeSpec in FileSpec with correct package
- [ ] Wire into `generateModels()` — filter `Model.Enum` from `ApiTree.models`, generate one FileSpec per enum

## Golden Tests

- [ ] `enum/basic` — `Sort { ASC, DESC }` (values match PascalCase, no @SerialName needed)
- [ ] `enum/serialname` — `SortSerialName` with snake_case values requiring @SerialName
- [ ] `enum/singlevalue` — `Version { V1 }` single entry
- [ ] `enum/int` — `Priority` with integer inner type
- [ ] `enum/special-chars` — values with `*`, `/`, `+1`, `-1` etc.
- [ ] `enum/nullable` — nullable enum (if applicable)
- [ ] `enum/jsname` — enum with entries needing @JsName (test with JS target enabled)

## Files to Create/Modify

- **Create**: `renderer/.../EnumRenderer.kt` — `Model.Enum.toTypeSpec()` + `toFileSpec()`
- **Modify**: `renderer/.../Generate.kt` — wire enum generation into `generateModels()`
- **Create**: golden test resource files under `renderer/src/test/resources/kotlinTestData/enum/`
- **Modify**: `renderer/.../EnumSpec.kt` — add new test cases

## Key Decisions

- Enum entries always use trailing semicolon (KotlinPoet handles this)
- `@SerialName` omitted when PascalCase name equals raw value (e.g., `ASC` → `ASC`)
- Integer enums use the same `enum class` pattern — the inner type affects the serializer but not the Kotlin shape
