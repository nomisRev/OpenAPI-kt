# Phase 4 — Collections

Generate `List<T>`, `JsonArray`, and value class wrappers for top-level named collections.

## Tasks

- [x] Collection type mapping (already partially in Phase 0 `toTypeName`):
  - `Collection` with typed inner → `List<inner.toTypeName()>`
  - `Collection` with `FreeFormJson` inner → `JsonArray`
- [x] Top-level named collection (component schema with `type: array`):
  - Generate `value class` wrapper: `@Serializable @JvmInline value class Tags(val items: List<String>)`
  - Property name is always `items`
  - `@JvmInline` when JVM target
- [x] Serializer behavior for top-level collections:
  - Use compiler-generated serializer from `@Serializable` value classes
  - Do not generate custom serializers for simple collections
- [x] Nullable collections: `List<T>?` when `isNullable`
- [x] Default values for collections: `Default.Value(list)` → generate list literal, `Default.Null` → `= null`

## Golden Tests

- [x] `collection/basic` — top-level `Tags` value class wrapping `List<String>`
- [x] `collection/complex-inner` — collection of objects: `List<Pet>`
- [x] `collection/freeform` — collection with FreeFormJson inner → `JsonArray`
- [x] `collection/nullable` — nullable collection

## Files to Create/Modify

- **Create**: `renderer/.../CollectionRenderer.kt` — top-level collection → value class FileSpec
- **Modify**: `renderer/.../Generate.kt` — wire collection generation (filter top-level `Collection` models that have a NamingContext)
- **Create**: golden test resource files under `renderer/src/test/resources/kotlinTestData/collection/`
- **Create**: `renderer/.../CollectionSpec.kt` — test suite

## Key Decisions

- Inline collections (as object properties, return types, etc.) don't generate standalone types — they're just `List<T>` in the parent
- Only top-level named collections (from component schemas) get the value class wrapper
- The `items` property name is hardcoded — not derived from the schema
