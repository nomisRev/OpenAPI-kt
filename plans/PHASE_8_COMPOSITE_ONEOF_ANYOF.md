# Phase 8: Composite oneOf + anyOf Plugins

> Depends on: Phase 7 (Composite allOf — for shared helpers)

## Goal

Add oneOf and anyOf handling: nullable flattening, single-branch unwrap, and union building — 6 plugins total in the `COMPOSITE` phase.

---

## What to build

### oneOf Plugins

#### OneOfNullableFlattenPlugin (`pipeline/plugins/composite/OneOfNullableFlattenPlugin.kt`)

**Phase**: `COMPOSITE`
**Key**: `PluginKeys.OneOfNullable`

**Match condition** (mirrors `SchemaTransformer.kt:59`):
- `compositeTakesPrecedence` is true
- AND `isOneOfNullableType()` (at least one oneOf branch is null)

**Behavior** (mirrors `SchemaTransformer.kt:59-65`):
1. `flattenNull(schema.oneOf!!)` — filter null branches, mark nullable
2. Single remaining → `flattenedSingleNullableBranch(branch, context)`
3. Multiple remaining → `buildOneOf(context, nonNullSchemas)`

#### OneOfSinglePlugin (`pipeline/plugins/composite/OneOfSinglePlugin.kt`)

**Phase**: `COMPOSITE`
**Key**: `PluginKeys.OneOfSingle`

**Match condition** (mirrors `SchemaTransformer.kt:66`):
- `compositeTakesPrecedence` is true
- AND `schema.oneOf?.size == 1`

**Behavior**: Unwraps the single element: `schema.oneOf!!.single().toModel(name, context)`.

#### OneOfPlugin (`pipeline/plugins/composite/OneOfPlugin.kt`)

**Phase**: `COMPOSITE`
**Key**: `PluginKeys.OneOf`

**Match condition** (mirrors `SchemaTransformer.kt:67`):
- `compositeTakesPrecedence` is true
- AND `schema.oneOf?.isNotEmpty() == true`

**Behavior**: Delegates to `ResolvedSchema.buildOneOf(context, schema.oneOf!!)` in `Union.kt:32-48`.

**Ordering**: `OneOfNullable` → `OneOfSingle` → `OneOf` (nullable check first, then single unwrap, then full union).

### anyOf Plugins (mirror oneOf structure)

#### AnyOfNullableFlattenPlugin (`pipeline/plugins/composite/AnyOfNullableFlattenPlugin.kt`)

**Phase**: `COMPOSITE`
**Key**: `PluginKeys.AnyOfNullable`

**Match condition** (mirrors `SchemaTransformer.kt:69`):
- `compositeTakesPrecedence` + `isAnyOfNullableType()`

**Behavior** (mirrors `SchemaTransformer.kt:69-75`):
Same pattern as oneOf nullable, but delegates multi-branch to `buildAnyOf()`.

#### AnyOfSinglePlugin (`pipeline/plugins/composite/AnyOfSinglePlugin.kt`)

**Phase**: `COMPOSITE`
**Key**: `PluginKeys.AnyOfSingle`

**Match condition** (mirrors `SchemaTransformer.kt:76`):
- `compositeTakesPrecedence` + `schema.anyOf?.size == 1`

**Behavior**: `schema.anyOf!!.single().toModel(name, context)`.

#### AnyOfPlugin (`pipeline/plugins/composite/AnyOfPlugin.kt`)

**Phase**: `COMPOSITE`
**Key**: `PluginKeys.AnyOf`

**Match condition** (mirrors `SchemaTransformer.kt:77`):
- `compositeTakesPrecedence` + `schema.anyOf?.isNotEmpty() == true`

**Behavior**: Delegates to `ResolvedSchema.buildAnyOf(context, schema.anyOf!!)` in `Union.kt:51-67`.

**Ordering**: `AnyOfNullable` → `AnyOfSingle` → `AnyOf`.

### PluginKeys update

Add: `OneOfNullable`, `OneOfSingle`, `OneOf`, `AnyOfNullable`, `AnyOfSingle`, `AnyOf`.

### Default ordering within COMPOSITE phase

Full COMPOSITE ordering after this phase:
1. `AllOfNullable`
2. `AllOf`
3. `OneOfNullable`
4. `OneOfSingle`
5. `OneOf`
6. `AnyOfNullable`
7. `AnyOfSingle`
8. `AnyOf`

This mirrors the dispatcher's branch order in `SchemaTransformer.kt:50-77`.

---

## Dispatcher branch mapping

| Dispatcher line | Condition | Plugin |
|----------------|-----------|--------|
| `SchemaTransformer.kt:59-65` | `compositeTakesPrecedence` + `isOneOfNullableType()` | `OneOfNullableFlattenPlugin` |
| `SchemaTransformer.kt:66` | `compositeTakesPrecedence` + `oneOf.size == 1` | `OneOfSinglePlugin` |
| `SchemaTransformer.kt:67` | `compositeTakesPrecedence` + `oneOf.isNotEmpty()` | `OneOfPlugin` |
| `SchemaTransformer.kt:69-75` | `compositeTakesPrecedence` + `isAnyOfNullableType()` | `AnyOfNullableFlattenPlugin` |
| `SchemaTransformer.kt:76` | `compositeTakesPrecedence` + `anyOf.size == 1` | `AnyOfSinglePlugin` |
| `SchemaTransformer.kt:77` | `compositeTakesPrecedence` + `anyOf.isNotEmpty()` | `AnyOfPlugin` |

### Handler delegation chain

**buildOneOf** (`Union.kt:32-48`):
1. `resolveUnionDiscriminator(subtypes)` — explicit discriminator or inferred tag-only
2. `classifyDispatch(discriminator)` → `Structural`, `NativeDiscriminator`, or `TaggedCustom`
3. `buildUnionCases(context, subtypes, discriminator, dispatch)` — resolves each case model
4. Returns `Model.OneOf(name, cases, default, description, title, dispatch, isNullable)`

**buildAnyOf** (`Union.kt:51-67`):
Identical structure, returns `Model.AnyOf`.

**Discriminator detection** (`Union.kt:77-131`):
- Explicit: `schema.discriminator?.propertyName` + mapping
- Inferred: `inferTagOnlyDiscriminatorOrNull()` — inspects flattened allOf properties for common literal string fields

**Case model building** (`Union.kt:134-200`):
- `NativeDiscriminator` → `toDiscriminatedUnionCaseModel()` — strips discriminator property, hoists single remaining
- `TaggedCustom` → `toTaggedCustomUnionCaseModel()` — flattens allOf
- `Structural` → `toModel(context, false)`

**Case naming** (`Union.kt:325-413`):
Complex fallback cascade: title → composite name → discriminator value → open enum → reference pattern → property joining → primitive name → index fallback.

---

## Files to create

| File | Action |
|------|--------|
| `pipeline/plugins/composite/OneOfNullableFlattenPlugin.kt` | **Create** |
| `pipeline/plugins/composite/OneOfSinglePlugin.kt` | **Create** |
| `pipeline/plugins/composite/OneOfPlugin.kt` | **Create** |
| `pipeline/plugins/composite/AnyOfNullableFlattenPlugin.kt` | **Create** |
| `pipeline/plugins/composite/AnyOfSinglePlugin.kt` | **Create** |
| `pipeline/plugins/composite/AnyOfPlugin.kt` | **Create** |

## Files to modify

| File | Action |
|------|--------|
| `pipeline/PluginKeys.kt` | **Modify** — add 6 keys |
| `pipeline/SchemaTransformerEngine.kt` | **Modify** — add 6 plugins to `defaults()` in correct order |

---

## Parity testing

Use existing test data from:
- `OneOfSpec.kt` — oneOf with references, inline types, discriminators
- `AnyOfSpec.kt` — anyOf variants
- `UnionSpec.kt` — discriminated unions (native, tagged custom, structural), case naming, open enums, property hoisting

Key scenarios:
- Nullable oneOf: `oneOf: [{ type: null }, { $ref: User }]` → nullable `Model.Reference`
- Single-element oneOf → unwrapped
- Multi-element oneOf → `Model.OneOf` with correct discriminator detection
- Native discriminator: explicit `discriminator.propertyName` + mapping
- Tagged custom: inferred from common literal string property
- Structural: no discriminator → content-based dispatch
- Discriminator property stripping and hoisting
- Open enum pattern: n-1 string types + 1 string enum → special naming
- Reference pattern: n-1 refs + 1 inline → `CaseElse` naming

---

## Acceptance criteria

- [ ] `OneOfNullableFlattenPlugin`: strips null branches, marks nullable, single-branch unwrap
- [ ] `OneOfSinglePlugin`: unwraps single-element oneOf
- [ ] `OneOfPlugin`: builds full `Model.OneOf` with discriminator detection
- [ ] `AnyOfNullableFlattenPlugin`: same as oneOf nullable but produces `Model.AnyOf`
- [ ] `AnyOfSinglePlugin`: unwraps single-element anyOf
- [ ] `AnyOfPlugin`: builds full `Model.AnyOf`
- [ ] All 6 plugins return `Pass` when `compositeTakesPrecedence` is false
- [ ] COMPOSITE ordering correct: allOf → oneOf → anyOf, each with nullable → single → full
- [ ] Engine parity with `OneOfSpec`, `AnyOfSpec`, `UnionSpec` test cases
- [ ] Discriminator detection: native, tagged custom, structural all work
- [ ] Discriminator property stripping and single-property hoisting preserved
- [ ] Case naming cascade: title → composite → discriminator → open enum → ref pattern → properties → primitive → index
- [ ] `flattenedSingleNullableBranch` preserves `ResolvedSchema` variant correctly
