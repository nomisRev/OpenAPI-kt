# Phase 9: Reference Plugins

> Depends on: Phase 1 (Engine Skeleton)

## Goal

Add `DiscriminatedSubtypePlugin` and `DiscriminatedObjectPlugin` in the `REFERENCE` phase for handling discriminated reference schemas.

---

## What to build

### DiscriminatedSubtypePlugin (`pipeline/plugins/reference/DiscriminatedSubtypePlugin.kt`)

**Phase**: `REFERENCE`
**Key**: `PluginKeys.DiscriminatedSubtype`

**Match condition** (mirrors `SchemaTransformer.kt:39-45`):
- `this is ResolvedSchema.Reference`
- AND `schema.discriminatedSubtypeOrNull(context, reference.name) != null`

**Behavior**: Returns `Model.Reference` with the discriminated naming context:
```kotlin
Model.Reference(
    schema.discriminatedSubtypeOrNull(context, reference.name)!!,
    description(),
    isNullable,
    schema.title
)
```

Uses `discriminatedSubtypeOrNull()` from `DiscriminatedObject.kt:25-47` which:
1. Checks for non-empty `allOf`
2. Finds single reference in allOf
3. Recursively checks if that reference's schema has a `discriminator.mapping` containing the current reference name
4. Returns a `NamingContext` pointing to the parent's discriminated object case

### DiscriminatedObjectPlugin (`pipeline/plugins/reference/DiscriminatedObjectPlugin.kt`)

**Phase**: `REFERENCE`
**Key**: `PluginKeys.DiscriminatedObject`

**Match condition** (mirrors `SchemaTransformer.kt:49`):
- `this is ResolvedSchema.Reference`
- AND `isObjectWithDiscriminator()` (from `Predicates.kt:59-72`)

**Behavior**: Delegates to `ResolvedSchema.toDiscriminatedObject(context)` in `DiscriminatedObject.kt:51-99`.

### Ordering

`DiscriminatedSubtypePlugin` MUST come BEFORE `DiscriminatedObjectPlugin`. This mirrors the dispatcher precedence where lines 39-45 fire before line 49. If a reference matches both (a discriminated subtype that is also an object with discriminator), the subtype check wins.

### PluginKeys update

Add `PluginKeys.DiscriminatedSubtype` and `PluginKeys.DiscriminatedObject`.

---

## Dispatcher branch mapping

| Dispatcher line | Condition | Plugin |
|----------------|-----------|--------|
| `SchemaTransformer.kt:39-45` | `Reference` + `discriminatedSubtypeOrNull != null` | `DiscriminatedSubtypePlugin` |
| `SchemaTransformer.kt:49` | `Reference` + `isObjectWithDiscriminator()` | `DiscriminatedObjectPlugin` |

### Handler delegation chain

**discriminatedSubtypeOrNull** (`DiscriminatedObject.kt:25-47`):
1. Check `allOf` non-empty
2. Extract single reference from allOf
3. Peek the referenced schema
4. If it has 1-2 allOf branches → recurse
5. If its `discriminator.mapping` contains `#/components/schemas/$name` → return naming context
6. Otherwise → null

**isObjectWithDiscriminator** (`Predicates.kt:59-83`):
1. Schema has `properties`
2. `discriminator.mapping` is non-empty
3. All mapping values either self-reference OR point to schemas whose allOf references back

**toDiscriminatedObject** (`DiscriminatedObject.kt:51-99`):
1. Require discriminator + mapping + Reference
2. Filter out self-mapping from mapping
3. Compute abstract properties (exclude discriminator field and FreeFormJson)
4. For each mapped subtype:
   - Peek allOf, find parent class, collect super-type properties recursively
   - Merge super + subtype properties (excluding discriminator field)
   - Build `Model.Object` via `toObject()`
5. Build self-mapping simple case
6. Return `Model.DiscriminatedObject(name, abstractProperties, subtypes, description, title, discriminator, isNullable)`

---

## Files to create

| File | Action |
|------|--------|
| `pipeline/plugins/reference/DiscriminatedSubtypePlugin.kt` | **Create** |
| `pipeline/plugins/reference/DiscriminatedObjectPlugin.kt` | **Create** |

## Files to modify

| File | Action |
|------|--------|
| `pipeline/PluginKeys.kt` | **Modify** — add keys |
| `pipeline/SchemaTransformerEngine.kt` | **Modify** — add both to `defaults()`, `DiscriminatedSubtype` before `DiscriminatedObject` |

---

## Parity testing

Use existing test data from `DiscriminatedObjectSpec.kt`:
- Object with discriminator + mapping → `Model.DiscriminatedObject`
- Discriminated subtype reference (allOf pointing to parent with discriminator) → `Model.Reference` with discriminated naming
- Self-mapping → simple case with default/mapped name
- Multi-level inheritance (subtype → intermediate parent → top parent)
- Abstract properties extracted correctly (excludes discriminator field)
- Subtype properties merged from allOf branches + super-type properties

Also from `AllOfSpec.kt`:
- allOf with single reference to discriminated parent → `Model.Reference` (subtype detection within allOf handler)

---

## Acceptance criteria

- [ ] `DiscriminatedSubtypePlugin` returns `Handled(Model.Reference(...))` when allOf references a parent with discriminator mapping
- [ ] `DiscriminatedSubtypePlugin` returns `Pass` for non-Reference schemas
- [ ] `DiscriminatedSubtypePlugin` returns `Pass` for references without allOf or without discriminator mapping
- [ ] `DiscriminatedObjectPlugin` returns `Handled(Model.DiscriminatedObject(...))` for references with properties + discriminator + mapping
- [ ] `DiscriminatedObjectPlugin` returns `Pass` for non-Reference schemas
- [ ] `DiscriminatedObjectPlugin` returns `Pass` for references without discriminator
- [ ] Ordering: subtype check fires before object check in default engine
- [ ] Engine parity with `DiscriminatedObjectSpec` test cases
- [ ] Abstract properties exclude discriminator field and FreeFormJson properties
- [ ] Self-mapping produces simple case with correct naming (default name if self-mapped, mapped name otherwise)
- [ ] Multi-level inheritance: super-type properties collected recursively from intermediate parents
- [ ] Subtype properties correctly merged and filtered by `SchemaContext`
