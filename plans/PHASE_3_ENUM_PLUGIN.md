# Phase 3: Enum Plugin

> Depends on: Phase 2 (Primitive Plugin)

## Goal

Add `EnumPlugin` (TYPED phase) and `ImplicitEnumPlugin` (IMPLICIT phase) for schemas with enum-like values, delegating to the existing `ResolvedSchema.toClosedEnum()`.

---

## What to build

### EnumPlugin (`pipeline/plugins/typed/EnumPlugin.kt`)

**Phase**: `TYPED`
**Key**: `PluginKeys.Enum`

**Match condition** (mirrors `SchemaTransformer.kt:95-98`):
- `schema.type` is one of `String`, `Number`, `Boolean`, `Integer`
- AND `schema.enumLikeValues() != null`

**Behavior**: Delegates to `ResolvedSchema.toClosedEnum(context, enumLikeValues)` in `transformers/Enum.kt:13-28`.

### ImplicitEnumPlugin (`pipeline/plugins/implicit/ImplicitEnumPlugin.kt`)

**Phase**: `IMPLICIT`
**Key**: `PluginKeys.ImplicitEnum`

**Match condition** (mirrors `SchemaTransformer.kt:108`):
- `schema.type == null`
- AND `schema.enumLikeValues() != null`

**Behavior**: Same delegation to `toClosedEnum(context, enumLikeValues)`.

### PluginKeys update

Add `PluginKeys.Enum = PluginKey("enum")` and `PluginKeys.ImplicitEnum = PluginKey("implicit-enum")`.

---

## Dispatcher branch mapping

| Dispatcher line | Condition | Plugin |
|----------------|-----------|--------|
| `SchemaTransformer.kt:95` | `type == String` + enum | `EnumPlugin` |
| `SchemaTransformer.kt:96` | `type == Number` + enum | `EnumPlugin` |
| `SchemaTransformer.kt:97` | `type == Boolean` + enum | `EnumPlugin` |
| `SchemaTransformer.kt:98` | `type == Integer` + enum | `EnumPlugin` |
| `SchemaTransformer.kt:108` | `type == null` + enum | `ImplicitEnumPlugin` |

### Handler delegation chain

`toClosedEnum()` (`Enum.kt:13-28`):
1. Validates enum is non-empty
2. Detects nullable enum values (`null` or `"null"`)
3. Strips enum values from schema, creates inner model via `ResolvedSchema.Value(...).toModel(context, false)` — this inner call resolves to the primitive type
4. Maps raw enum values to typed `Model.EnumValue.*` variants
5. Returns `Model.Enum(name, inner, enumValues, enumDefault, description, title, isNullable)`

**Note**: The inner `toModel()` call on line 19 of `Enum.kt` currently calls the old dispatcher. During this phase it continues to do so. Phase 10 rewires it through the engine.

---

## Files to create

| File | Action |
|------|--------|
| `pipeline/plugins/typed/EnumPlugin.kt` | **Create** |
| `pipeline/plugins/implicit/ImplicitEnumPlugin.kt` | **Create** |

## Files to modify

| File | Action |
|------|--------|
| `pipeline/PluginKeys.kt` | **Modify** — add `Enum`, `ImplicitEnum` keys |
| `pipeline/SchemaTransformerEngine.kt` | **Modify** — add both plugins to `defaults()` |

---

## Parity testing

Use existing test data from `ClosedEnumSpec.kt`:
- `Model.Enum.ints(name)` — integer enum values
- `Model.Enum.longs(name)` — long enum values
- `Model.Enum.strings(name)` — string enum values
- `Model.Enum.floats(name)` — float enum values
- `Model.Enum.doubles(name)` — double enum values
- `Model.Enum.booleans(name)` — boolean enum values

Also test:
- Nullable enums (enum containing `null` or `"null"`)
- Enum defaults (valid default, null default)
- Error: empty enum → `require` failure
- Error: null default on non-nullable enum → `require` failure

---

## Acceptance criteria

- [ ] `EnumPlugin` returns `Handled` for typed schemas with `enumLikeValues != null`
- [ ] `EnumPlugin` returns `Pass` for typed schemas without enum values
- [ ] `ImplicitEnumPlugin` returns `Handled` for typeless schemas with `enumLikeValues != null`
- [ ] `ImplicitEnumPlugin` returns `Pass` for typeless schemas without enum values
- [ ] Both return `Pass` when schema has composite fields (allOf/oneOf/anyOf)
- [ ] Engine parity with `ClosedEnumSpec` test cases for all enum types
- [ ] Enum defaults parsed and validated correctly
- [ ] Nullable enum detection works (both explicit `null` values and `"null"` strings)
- [ ] Inner primitive type correctly resolved (String enum → inner String, Int enum → inner Int, etc.)
- [ ] Error cases produce matching exceptions (empty enum, invalid default)
