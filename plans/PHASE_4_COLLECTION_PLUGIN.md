# Phase 4: Collection Plugin

> Depends on: Phase 2 (Primitive Plugin)

## Goal

Add `CollectionPlugin` (TYPED phase) and `ImplicitCollectionPlugin` (IMPLICIT phase) for array schemas, delegating to the existing `ResolvedSchema.collection()`.

---

## What to build

### CollectionPlugin (`pipeline/plugins/typed/CollectionPlugin.kt`)

**Phase**: `TYPED`
**Key**: `PluginKeys.Collection`

**Match condition** (mirrors `SchemaTransformer.kt:94`):
- `schema.type == Schema.Type.Basic.Array`

**Behavior**: Delegates to `ResolvedSchema.collection(context)` in `transformers/Collection.kt:15-65`.

### ImplicitCollectionPlugin (`pipeline/plugins/implicit/ImplicitCollectionPlugin.kt`)

**Phase**: `IMPLICIT`
**Key**: `PluginKeys.ImplicitCollection`

**Match condition** (mirrors `SchemaTransformer.kt:112`):
- `schema.type == null`
- AND `schema.items != null`
- AND `schema.properties.isNullOrEmpty()`
- AND `schema.additionalProperties == null`
- AND `schema.enumLikeValues() == null`

The extra guards prevent matching schemas that should be handled by implicit object/enum plugins.

**Behavior**: Same delegation to `collection(context)`.

### PluginKeys update

Add `PluginKeys.Collection` and `PluginKeys.ImplicitCollection`.

---

## Dispatcher branch mapping

| Dispatcher line | Condition | Plugin |
|----------------|-----------|--------|
| `SchemaTransformer.kt:94` | `type == Array` | `CollectionPlugin` |
| `SchemaTransformer.kt:112` | `type == null` + `items != null` | `ImplicitCollectionPlugin` |

### Handler delegation chain

`collection()` (`Collection.kt:15-65`) branches by `ResolvedSchema` variant:
- `Recursive` → `Model.Reference`
- `Value` → `toCollection()` → `Model.Collection(inner, default, description, constraint, isNullable, title)`
- `Reference` → wraps in `Model.Object` with single `items` property containing `Model.Collection`

The `inner` model is resolved via `schema.items?.toModel(name, context)`. This call currently goes through the old dispatcher. Phase 10 rewires it.

`wrapIfNeeded()` (`Collection.kt:20-40`): adjusts naming context for inline models nested inside reference-wrapped collections.

`collectionDefault()` (`Collection.kt:119-134`): parses `schema.default` to `Model.Default.Value(list)`, `Model.Default.Null`, or `null`.

---

## Files to create

| File | Action |
|------|--------|
| `pipeline/plugins/typed/CollectionPlugin.kt` | **Create** |
| `pipeline/plugins/implicit/ImplicitCollectionPlugin.kt` | **Create** |

## Files to modify

| File | Action |
|------|--------|
| `pipeline/PluginKeys.kt` | **Modify** — add keys |
| `pipeline/SchemaTransformerEngine.kt` | **Modify** — add both to `defaults()` |

---

## Parity testing

Use existing test data from `CollectionSpec.kt`:
- Array of primitives (string, int, etc.)
- Array of references
- Array of objects (inline)
- Array with constraints (`minItems`, `maxItems`, `uniqueItems`)
- Array with defaults (`[]`, null, single-value)
- Nested arrays (array of arrays)
- Reference-wrapped arrays → `Model.Object` with `items` property

---

## Acceptance criteria

- [ ] `CollectionPlugin` returns `Handled` for `type: array` schemas
- [ ] `CollectionPlugin` returns `Pass` for non-array typed schemas
- [ ] `ImplicitCollectionPlugin` returns `Handled` for typeless schemas with `items`
- [ ] `ImplicitCollectionPlugin` returns `Pass` when `type` is present
- [ ] `ImplicitCollectionPlugin` returns `Pass` when `properties` or `additionalProperties` present (defers to object)
- [ ] Engine parity with `CollectionSpec` test cases
- [ ] Nested item types resolved correctly
- [ ] Collection defaults match: `[]`, null, single-value
- [ ] Constraints preserved: `minItems`, `maxItems`, `uniqueItems`
- [ ] Reference-wrapped collections produce `Model.Object` with `items` property and correct context nesting
- [ ] `wrapIfNeeded` correctly adjusts naming context for inline enum/object/union items
