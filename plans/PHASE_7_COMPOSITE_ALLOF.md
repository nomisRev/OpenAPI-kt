# Phase 7: Composite allOf Plugins

> Depends on: Phase 1 (Engine Skeleton)

## Goal

Add composite allOf handling: a shared `CompositePrecedenceGuard` utility, `AllOfNullableFlattenPlugin`, and `AllOfPlugin` — all in the `COMPOSITE` phase.

---

## What to build

### CompositePrecedenceGuard (`pipeline/plugins/composite/CompositePrecedenceGuard.kt`)

Shared utility extracted from `SchemaTransformer.kt:117-138`. Used by ALL composite plugins to decide if composite takes precedence over type.

```kotlin
internal fun Schema.compositeTakesPrecedence(): Boolean =
    type == null || compositeShouldTakePrecedenceOverType()
```

Where `compositeShouldTakePrecedenceOverType()` checks if any branch in allOf/oneOf/anyOf adds structural shape (has type, properties, additionalProperties, items, enum, nested composites, or format).

All composite plugins check this guard first and return `Pass` if it's false.

### AllOfNullableFlattenPlugin (`pipeline/plugins/composite/AllOfNullableFlattenPlugin.kt`)

**Phase**: `COMPOSITE`
**Key**: `PluginKeys.AllOfNullable`

**Match condition** (mirrors `SchemaTransformer.kt:50`):
- `compositeTakesPrecedence` is true
- AND `isAllOfNullableType()` (at least one allOf branch is `type: null` or `nullable: true` schema)

**Behavior** (mirrors `SchemaTransformer.kt:50-56`):
1. Call `flattenNull(schema.allOf!!)` — filters out null branches, marks result nullable
2. If single non-null branch remains → `flattenedSingleNullableBranch(branch, context)` — unwraps to the single branch's model
3. If multiple non-null branches → `allOf(context, nonNullSchemas)` — delegates to allOf merge

Uses existing helpers:
- `flattenNull()` (`SchemaTransformer.kt:220-234`)
- `flattenedSingleNullableBranch()` (`SchemaTransformer.kt:237-247`)

### AllOfPlugin (`pipeline/plugins/composite/AllOfPlugin.kt`)

**Phase**: `COMPOSITE`
**Key**: `PluginKeys.AllOf`

**Match condition** (mirrors `SchemaTransformer.kt:57`):
- `compositeTakesPrecedence` is true
- AND `schema.allOf != null`
- AND NOT nullable type (handled by `AllOfNullableFlattenPlugin`)

**Behavior**: Delegates to `ResolvedSchema.allOf(context, schema.allOf!!)` in `AllOf.kt:21-28`.

**Ordering**: `AllOfNullableFlattenPlugin` BEFORE `AllOfPlugin` (nullable check must fire first).

### PluginKeys update

Add `PluginKeys.AllOfNullable`, `PluginKeys.AllOf`.

### Move shared helpers

`flattenNull` and `flattenedSingleNullableBranch` are currently private in `SchemaTransformer.kt:219-247`. Make them `internal` so composite plugins can call them. Move to a shared location (e.g., `pipeline/plugins/composite/CompositeHelpers.kt` or keep in `SchemaTransformer.kt` with `internal` visibility for now — Phase 10 will clean up).

---

## Dispatcher branch mapping

| Dispatcher line | Condition | Plugin |
|----------------|-----------|--------|
| `SchemaTransformer.kt:50-56` | `compositeTakesPrecedence` + `isAllOfNullableType()` | `AllOfNullableFlattenPlugin` |
| `SchemaTransformer.kt:57` | `compositeTakesPrecedence` + `allOf != null` | `AllOfPlugin` |

### Handler delegation chain

**AllOf merge** (`AllOf.kt:21-34`):
- If `Reference` with 1-2 allOf branches → check for discriminated subtype first, then `resolveAllOf`
- `resolveAllOf` → peek all branches, reduce via `Schema.merge()`, then `toModel(name, context)`
- `Schema.merge()` is extensive: handles nested allOf, composite unions, enums, objects, primitives, constraints

**flattenNull** (`SchemaTransformer.kt:220-234`):
- Filters out null branches
- Calls `build(nonNullSchemas)` to get the model
- Sets `isNullable = true`, merges description and title

**flattenedSingleNullableBranch** (`SchemaTransformer.kt:237-247`):
- Reference branch → `toModel(name, context)`
- Value branch → preserve outer `ResolvedSchema` variant, replace inner schema, call `toModel(context, true)`

---

## Files to create

| File | Action |
|------|--------|
| `pipeline/plugins/composite/CompositePrecedenceGuard.kt` | **Create** |
| `pipeline/plugins/composite/AllOfNullableFlattenPlugin.kt` | **Create** |
| `pipeline/plugins/composite/AllOfPlugin.kt` | **Create** |

## Files to modify

| File | Action |
|------|--------|
| `transformers/SchemaTransformer.kt` | **Modify** — change `flattenNull`, `flattenedSingleNullableBranch`, `compositeShouldTakePrecedenceOverType` from `private` to `internal` |
| `pipeline/PluginKeys.kt` | **Modify** — add keys |
| `pipeline/SchemaTransformerEngine.kt` | **Modify** — add plugins to `defaults()`, ordered: `AllOfNullable` before `AllOf` |

---

## Parity testing

Use existing test data from `AllOfSpec.kt`:
- allOf with two object schemas → merged properties
- allOf with nullable branch → nullable flattened result
- allOf single non-null branch → unwrapped
- allOf with constraints → merged constraints (min→max, max→min)
- allOf with discriminated subtype detection (reference with parent discriminator mapping)
- allOf with nested allOf expansion
- allOf merging primitives, objects, FreeFormJson, collections

---

## Acceptance criteria

- [ ] `CompositePrecedenceGuard` correctly returns true/false matching `compositeShouldTakePrecedenceOverType()` for all edge cases
- [ ] `AllOfNullableFlattenPlugin` handles nullable allOf: strips null branches, marks result nullable
- [ ] `AllOfNullableFlattenPlugin` single-branch unwrap: single remaining branch → delegate to that branch's model
- [ ] `AllOfPlugin` handles non-nullable allOf merge
- [ ] Both return `Pass` when `compositeTakesPrecedence` is false
- [ ] Both return `Pass` when `allOf` is null
- [ ] `AllOfNullableFlattenPlugin` fires before `AllOfPlugin` in default ordering
- [ ] Engine parity with `AllOfSpec` test cases
- [ ] Constraint merging: number, text, object, collection constraints all merged correctly
- [ ] Discriminated subtype detection within allOf works
