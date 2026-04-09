# Phase 6: TypeArray Plugin

> Depends on: Phase 2 (Primitive Plugin), Phase 8 (oneOf — for union delegation)

## Goal

Add a `TypeArrayPlugin` in the `TYPED` phase that handles OpenAPI 3.1 multi-type arrays (e.g., `type: [string, null]`), delegating to the existing `union()` function for multi-type resolution.

---

## What to build

### TypeArrayPlugin (`pipeline/plugins/typed/TypeArrayPlugin.kt`)

**Phase**: `TYPED`
**Key**: `PluginKeys.TypeArray`

**Match condition** (mirrors `SchemaTransformer.kt:80`):
- `schema.type is Schema.Type.Array`

Note: `Schema.Type.Array` is the multi-type array syntax (e.g., `[string, null]`), NOT `Schema.Type.Basic.Array` which is a regular collection.

**Behavior** (mirrors `SchemaTransformer.kt:80-90`):

1. Check if `Null` is in the type array and `nullable != true` → copy schema with `nullable = true`
2. Build union from remaining types: `(type.types - Schema.Type.Basic.Null).map { ReferenceOr.value(Schema(type = it)) }`
3. Delegate to `ResolvedSchema.union(context, subtypes)` in `Union.kt:24-29`

The null-flattening logic must preserve the correct `ResolvedSchema` variant:
```kotlin
val resolved = if (type.types.contains(Null) && schema.nullable != true) {
    when (this) {
        is ResolvedSchema.Recursive -> copy(schema = schema.copy(nullable = true))
        is ResolvedSchema.Reference -> copy(schema = schema.copy(nullable = true))
        is ResolvedSchema.Value -> copy(schema = schema.copy(nullable = true))
    }
} else this
```

### PluginKeys update

Add `PluginKeys.TypeArray = PluginKey("type-array")`.

---

## Dispatcher branch mapping

| Dispatcher line | Condition | Plugin action |
|----------------|-----------|---------------|
| `SchemaTransformer.kt:80-90` | `type is Schema.Type.Array` | Strip null, apply nullable, delegate to `union()` |

### Handler delegation chain

`TypeArrayPlugin.transform()` → null-flatten → `ResolvedSchema.union(context, subtypes)` (`Union.kt:24`) → `buildOneOf(context, subtypes)` (`Union.kt:32-48`)

The union path goes through the full discriminator detection, case naming, and case building logic in `Union.kt`.

---

## Files to create

| File | Action |
|------|--------|
| `pipeline/plugins/typed/TypeArrayPlugin.kt` | **Create** |

## Files to modify

| File | Action |
|------|--------|
| `pipeline/PluginKeys.kt` | **Modify** — add `TypeArray` key |
| `pipeline/SchemaTransformerEngine.kt` | **Modify** — add to `defaults()` |

---

## Parity testing

Use existing test data from `TypeArraySpec.kt`:
- `type: [string, null]` → nullable string
- `type: [string, integer]` → union
- `type: [string, integer, null]` → nullable union
- `type: [object, null]` → nullable object
- Single-type array `type: [string]` → plain string (via single-element union unwrap)

---

## Acceptance criteria

- [ ] `TypeArrayPlugin` returns `Handled` for `Schema.Type.Array` schemas
- [ ] `TypeArrayPlugin` returns `Pass` for `Schema.Type.Basic.*` schemas
- [ ] Null type in array correctly sets `isNullable = true` on the `ResolvedSchema` before delegation
- [ ] Null type is stripped from the types list before building the union
- [ ] Multi-type arrays delegate to `union()` → `buildOneOf()` correctly
- [ ] Single non-null type in array produces a simple model (not wrapped in union)
- [ ] Engine parity with `TypeArraySpec` test cases
- [ ] All three `ResolvedSchema` variants (Value, Reference, Recursive) handle null-flattening correctly
