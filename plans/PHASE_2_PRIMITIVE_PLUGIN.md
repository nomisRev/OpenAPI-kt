# Phase 2: Primitive Plugin

> Depends on: Phase 1 (Engine Skeleton)

## Goal

Add a `PrimitivePlugin` in the `TYPED` phase that handles all primitive type schemas, delegating to the existing `ResolvedSchema.primitive()` function.

---

## What to build

### PrimitivePlugin (`pipeline/plugins/typed/PrimitivePlugin.kt`)

**Phase**: `TYPED`
**Key**: `PluginKeys.Primitive`

**Match condition** (mirrors `SchemaTransformer.kt:99-102`):
- `schema.type` is one of `String`, `Integer`, `Number`, `Boolean`
- AND `schema.enumLikeValues() == null`

**Behavior**: Delegates to existing `ResolvedSchema.primitive()` in `transformers/Primitive.kt:12-24`.

Returns `TransformResult.Pass` for:
- Schemas with `enumLikeValues != null`
- Composite schemas (allOf/oneOf/anyOf)
- Object, Array, Null types
- Schemas without explicit `type`

### PluginKeys update

Add `PluginKeys.Primitive = PluginKey(primitive")`.

### Wire into `defaults()`

Add `PrimitivePlugin` to the engine's default plugin list in the `TYPED` phase.

---

## Dispatcher branch mapping

| Dispatcher line | Condition | Plugin action |
|----------------|-----------|---------------|
| `SchemaTransformer.kt:102` | `type in {Number, Boolean, Integer, String}` and no enum | `Handled(primitive())` |

### Handler delegation chain

`PrimitivePlugin.transform()` → `ResolvedSchema.primitive()` (`Primitive.kt:12`) → branches:
- `ResolvedSchema.Recursive` → `Model.Reference`
- `ResolvedSchema.Value` → `toPrimitive()` → type-specific `Model.Primitive.*`
- `ResolvedSchema.Reference` → `Model.Object.value(reference, toPrimitive(), isScalarWrapper=true)`

Type mapping (`Primitive.kt:29-92`):
- `Number` + `format=float` → `Model.Primitive.Float`
- `Number` + else → `Model.Primitive.Double`
- `Boolean` → `Model.Primitive.Boolean`
- `Integer` + `format=int32` → `Model.Primitive.Int`
- `Integer` + else → `Model.Primitive.Long`
- `String` + `format=binary` → `Model.ByteArray`
- `String` + `format=uuid` → `Model.Uuid`
- `String` + `format=date` → `Model.Date`
- `String` + `format=date-time` → `Model.DateTime`
- `String` + else → `Model.Primitive.String`

---

## Files to create

| File | Action |
|------|--------|
| `pipeline/plugins/typed/PrimitivePlugin.kt` | **Create** |

## Files to modify

| File | Action |
|------|--------|
| `pipeline/PluginKeys.kt` | **Modify** — add `Primitive` key |
| `pipeline/SchemaTransformerEngine.kt` | **Modify** — add `PrimitivePlugin` to `defaults()` |

---

## Parity testing

Use existing test data from `Values.kt`:
- `Model.Primitive.String.all()` — 49+ cases (nullable, formats, constraints, defaults)
- `Model.Primitive.Int.all()` — cases with number constraints
- `Model.Primitive.Long.all()` — similar to Int
- `Model.Primitive.Double.all()` — with number constraints
- `Model.Primitive.Float.all()` — float-specific
- `Model.Primitive.Boolean.all()` — boolean with defaults
- `Model.ByteArray.all()` — binary format strings
- `Model.Uuid.all()` — uuid format
- `Model.Date.all()` — date format
- `Model.DateTime.all()` — datetime format

Run each `Expect<Schema, Model>` pair through the engine and verify output matches expected `Model`.

Also verify error cases from `PrimitiveSpec.kt:39-79` produce the same exceptions.

---

## Acceptance criteria

- [ ] `PrimitivePlugin` returns `Handled` for all primitive type schemas without enum values
- [ ] `PrimitivePlugin` returns `Pass` for schemas with `enumLikeValues`
- [ ] `PrimitivePlugin` returns `Pass` for composite, object, array, null schemas
- [ ] `PrimitivePlugin` returns `Pass` for schemas without explicit `type`
- [ ] Engine with `PrimitivePlugin` + `FallbackPlugin` produces identical output to old dispatcher for all `PrimitiveSpec` test cases
- [ ] String formats: `binary→ByteArray`, `uuid→Uuid`, `date→Date`, `date-time→DateTime`, default→`String`
- [ ] Integer formats: `int32→Int`, default→`Long`
- [ ] Number formats: `float→Float`, default→`Double`
- [ ] Default values parsed correctly for each type
- [ ] Constraints (`minLength`, `maxLength`, `pattern`, `minimum`, `maximum`, `exclusiveMin/Max`, `multipleOf`) preserved
- [ ] Nullability propagated correctly
- [ ] `ResolvedSchema.Reference` wrapping produces `Model.Object.value` with `isScalarWrapper=true`
