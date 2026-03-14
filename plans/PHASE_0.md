# Phase 0 — Scaffolding

Set up the renderer foundation: config, naming utilities, type mapping, and the generation pipeline.

## Tasks

- [ ] Remove `ApiModel` class from `Route.kt`
- [ ] Move `models: List<Model>` and `servers: List<Server>` into `ApiTree`
- [ ] Move top-level model resolution logic (`topLevelModels()`, `topLevelNames()` on routes) into `ApiTree` construction
- [ ] Create `RenderConfig` data class:
  - `modelPackage: String` — package for component schema types
  - `apiPackage: String` — package for client interfaces
  - `targets: Set<KmpTarget>` — KMP platform targets (JVM, JS)
- [ ] Create `KmpTarget` enum: `JVM`, `JS`
- [ ] Create naming utilities in renderer module:
  - `String.toParamName()` — JSON name → camelCase Kotlin param name
  - `String.toCamelCase()` — general camelCase conversion
  - `toEnumValueName(rawValue: String)` — raw enum value → PascalCase entry name
  - `String.stringValue()` — dollar-aware string literal for `@SerialName`
  - `String.escapeKotlinString()` — escape special chars in string literals
  - `String.isValidClassname()` — identifier regex check
  - `String.isValidParamName()` — identifier + keyword check
  - `KOTLIN_KEYWORDS` set (27 words)
  - `String.sanitize()` — package path segment escaping
- [ ] Create `Model.toTypeName(config: RenderConfig)` extension — maps any `Model` to a KotlinPoet `TypeName`:
  - Primitives → `INT`, `LONG`, `FLOAT`, `DOUBLE`, `BOOLEAN`, `STRING`, `UNIT`
  - `Uuid` → `kotlin.uuid.Uuid`
  - `Date` → `kotlinx.datetime.LocalDate`
  - `DateTime` → `kotlinx.datetime.LocalDateTime`
  - `ByteArray` → `BYTE_ARRAY`
  - `FreeFormJson` → `kotlinx.serialization.json.JsonElement`
  - `Collection` → `List<inner.toTypeName()>` (or `JsonArray` when inner is FreeFormJson)
  - `Reference` → resolve via `NamingContext` → `ClassName`
  - `Object`, `Enum`, `Union`, `DiscriminatedObject` → resolve via `NamingContext` → `ClassName`
  - Nullable types → `TypeName.copy(nullable = true)`
- [ ] Create `NamingContext.toClassName(config: RenderConfig)` — resolves naming context to KotlinPoet `ClassName`:
  - `Head.Reference` → `ClassName(modelPackage, name.toPascalCase())` (+ nested path for nested contexts)
  - `Head.Path` → `ClassName(apiPackage, ...)` with Request/Response suffix
- [ ] Wire up the generation pipeline:
  - `OpenAPI.generate(config: RenderConfig)` → calls `toApiModel()` equivalent → `ApiTree.render(config)`
  - `ApiTree.render(config)` → calls `generateModels(config)` + `generateClient(config)` → `List<FileSpec>`
  - `generateModels()` and `generateClient()` as separate public functions on `ApiTree`
- [ ] Update test DSL (`TestBalloonDsl.kt`) to pass `RenderConfig` with test-appropriate packages

## Files to Create/Modify

- **Modify**: `typed/.../routes/Route.kt` — remove `ApiModel`, move model resolution
- **Modify**: `typed/.../ApiTree.kt` — add `models: List<Model>`, `servers: List<Server>`
- **Modify**: `renderer/.../Generate.kt` — implement pipeline
- **Create**: `renderer/.../RenderConfig.kt` — config + KmpTarget
- **Create**: `renderer/.../NamingUtils.kt` — naming utilities
- **Create**: `renderer/.../TypeMapping.kt` — Model → TypeName mapping
- **Modify**: `renderer/.../TestBalloonDsl.kt` — pass config

## Golden Tests

None for this phase — scaffolding only. Existing enum golden tests should still compile (may need package updates).
