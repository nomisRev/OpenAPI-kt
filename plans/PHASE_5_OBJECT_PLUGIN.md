# Phase 5: Object Plugin

> Depends on: Phase 2 (Primitive Plugin)

## Goal

Add `ObjectPlugin` (TYPED phase) and `ImplicitObjectPlugin` (IMPLICIT phase) for object schemas, including `additionalProperties` handling.

---

## What to build

### ObjectPlugin (`pipeline/plugins/typed/ObjectPlugin.kt`)

**Phase**: `TYPED`
**Key**: `PluginKeys.Object`

**Match condition**: `schema.type == Schema.Type.Basic.Object`

Two sub-branches (mirrors `SchemaTransformer.kt:92-93`):
1. `properties?.isNotEmpty() == true` → delegate to `ResolvedSchema.toObject(context, properties)` in `Object.kt:18-41`
2. Otherwise → delegate to `objectWithoutProperties(context)` in `SchemaTransformer.kt:140-145`

### ImplicitObjectPlugin (`pipeline/plugins/implicit/ImplicitObjectPlugin.kt`)

**Phase**: `IMPLICIT`
**Key**: `PluginKeys.ImplicitObject`

**Match condition**: `schema.type == null` AND one of:
- `schema.properties?.isNotEmpty() == true` (mirrors `SchemaTransformer.kt:110`)
- `schema.additionalProperties != null` (mirrors `SchemaTransformer.kt:111`)

AND `schema.enumLikeValues() == null` (so enum plugin gets priority).

Two sub-branches:
1. `properties?.isNotEmpty()` → delegate to `toObject(context, properties)`
2. `additionalProperties != null` → delegate to `objectWithoutProperties(context)`

### Move `objectWithoutProperties` to shared location

Currently private in `SchemaTransformer.kt:140-197`. Move to `transformers/Object.kt` (or a new `ObjectHelpers.kt`) so both the old dispatcher and plugins can access it.

Functions to move:
- `objectWithoutProperties()` (`SchemaTransformer.kt:140-145`)
- `buildTypedAdditionalPropertiesModel()` (`SchemaTransformer.kt:148-184`)
- `buildAllowedAdditionalPropertiesModel()` (`SchemaTransformer.kt:187-197`)

These three are private helpers that only depend on `Registry.Scope`, `ResolvedSchema`, and `SchemaContext`. Moving them to `Object.kt` keeps the object-related logic together.

### PluginKeys update

Add `PluginKeys.Object` and `PluginKeys.ImplicitObject`.

---

## Dispatcher branch mapping

| Dispatcher line | Condition | Plugin |
|----------------|-----------|--------|
| `SchemaTransformer.kt:92` | `type == Object` + properties | `ObjectPlugin` → `toObject()` |
| `SchemaTransformer.kt:93` | `type == Object` + no properties | `ObjectPlugin` → `objectWithoutProperties()` |
| `SchemaTransformer.kt:110` | `type == null` + properties | `ImplicitObjectPlugin` → `toObject()` |
| `SchemaTransformer.kt:111` | `type == null` + additionalProperties | `ImplicitObjectPlugin` → `objectWithoutProperties()` |

### Handler delegation chain

**`toObject()`** (`Object.kt:18-41`):
1. Filters properties by `SchemaContext` (strips readOnly/writeOnly)
2. Resolves `additionalProperties` model
3. Computes `hadPropertiesBeforeStripping` flag
4. Returns `Model.Object`

**`objectWithoutProperties()`** (`SchemaTransformer.kt:140-145`):
- `additionalProperties == null` → `fallback()` (delegates to FallbackPlugin in engine)
- `additionalProperties is PSchema` → `buildTypedAdditionalPropertiesModel()` → `Model.Object` (Reference/Recursive) or `Model.Collection` (Value)
- `additionalProperties is Allowed(true)` → `fallback()`
- `additionalProperties is Allowed(false)` → `buildAllowedAdditionalPropertiesModel()` → `Model.Object` or `Model.Primitive.Unit`

**Note**: `objectWithoutProperties` calls `fallback()` in some branches. In the plugin world, the plugin should return `Pass` for these cases so the FallbackPlugin handles them. Alternatively, the plugin can call `engine.transform()` with a stripped schema that the fallback will catch.

---

## Files to create

| File | Action |
|------|--------|
| `pipeline/plugins/typed/ObjectPlugin.kt` | **Create** |
| `pipeline/plugins/implicit/ImplicitObjectPlugin.kt` | **Create** |

## Files to modify

| File | Action |
|------|--------|
| `transformers/Object.kt` | **Modify** — move `objectWithoutProperties`, `buildTypedAdditionalPropertiesModel`, `buildAllowedAdditionalPropertiesModel` from `SchemaTransformer.kt` (change visibility from private to internal) |
| `transformers/SchemaTransformer.kt` | **Modify** — remove moved functions, call them from `Object.kt` instead |
| `pipeline/PluginKeys.kt` | **Modify** — add keys |
| `pipeline/SchemaTransformerEngine.kt` | **Modify** — add both plugins to `defaults()` |

---

## Parity testing

Use existing test data from `ObjectSpec.kt`:
- Object with properties (various types)
- readOnly/writeOnly property filtering by `SchemaContext`
- `hadPropertiesBeforeStripping` flag
- `additionalProperties: true` → FreeFormJson
- `additionalProperties: false` → `Allowed(false)`
- `additionalProperties: { type: string }` → typed map
- Value classes / scalar wrappers
- Empty object → fallback
- Nested object properties resolved correctly

---

## Acceptance criteria

- [ ] `ObjectPlugin` returns `Handled` for `type: object` schemas with properties
- [ ] `ObjectPlugin` returns `Handled` for `type: object` schemas without properties but with `additionalProperties`
- [ ] `ObjectPlugin` returns `Pass` for non-object typed schemas
- [ ] `ImplicitObjectPlugin` returns `Handled` for typeless schemas with properties
- [ ] `ImplicitObjectPlugin` returns `Handled` for typeless schemas with `additionalProperties`
- [ ] `ImplicitObjectPlugin` returns `Pass` when `type` is present or enum values exist
- [ ] Engine parity with `ObjectSpec` test cases
- [ ] readOnly/writeOnly filtering works correctly per `SchemaContext`
- [ ] `hadPropertiesBeforeStripping` flag set when properties were filtered
- [ ] `additionalProperties` correctly resolved: typed schema → `Schema`, allowed → `Allowed`, null → `Allowed(false)`
- [ ] `objectWithoutProperties` branches: `PSchema` → typed map, `Allowed(true)` → fallback, `Allowed(false)` → empty object or Unit
- [ ] Nested property models resolved correctly
