# Plan: Phased Transformation Engine

> Source PRD: `PROPOSAL_TYPED_MODULE_REWRITE.md` + `TYPED_MODULE_ARCHITECTURE.md`

## Architectural Decisions

Durable decisions that apply across all phases:

- **Package layout**: All new code under `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/pipeline/`. Plugins in `pipeline/plugins/` sub-packages (`reference/`, `composite/`, `typed/`, `implicit/`, `fallback/`).
- **Core types**: `TransformResult` (sealed: `Handled(Model)` / `Pass`), `Phase` (enum: `REFERENCE`, `COMPOSITE`, `TYPED`, `IMPLICIT`, `FALLBACK`), `PluginKey` (value class wrapping `String`), `SchemaTransformerPlugin` (interface with `key`, `phase`, `transform`), `ModelInterceptor` (interface with `intercept`).
- **Engine pre-checks (not pluggable)**: (1) Nested reference short-circuit (`ResolvedSchema.Reference` with `resolveReference=false`), (2) Recursive reference guard (`ResolvedSchema.Recursive` → `Model.Reference`), (3) `type: null` validation error. These fire before any plugin.
- **`resolveReference` removal from plugin API**: Reference policy is engine-owned. Plugins never see `resolveReference`. The engine handles it in pre-checks.
- **Nested schema re-entry**: Plugins call `engine.transform(childSchema, context)` for nested schemas, not `toModel()`. This ensures custom plugins apply at all depths.
- **Builder API**: `SchemaTransformerEngine.build { defaults(); addFirst(...); addAfter(...); addBefore(...); replace(...); interceptor(...) }`. Ordering by `PluginKey`, not class type. Duplicate keys fail fast. `replace` fails if key absent.
- **Default engine**: `SchemaTransformerEngine.default()` — immutable, reusable, zero-config.
- **Helper functions stay as free functions**: `flattenNull`, `flattenedSingleNullableBranch`, `compositeShouldTakePrecedenceOverType`, `enumLikeValues` remain plain extension functions. No handler inheritance.
- **Parity testing**: Each phase adds engine-based tests alongside existing tests using the same `Expect<Schema, Model>` test data from `Values.kt`. Old dispatcher stays until Phase 10.
- **PluginKeys object**: A `PluginKeys` object holds all default key constants (e.g., `PluginKeys.Primitive`, `PluginKeys.Object`, `PluginKeys.AllOf`, etc.) as the stable public API for ordering.

---

## Phase 1: Engine Skeleton + Fallback Plugin

**User stories**: Engine core types, builder DSL, pre-checks, fallback handler, no-match error.

### What to build

The thinnest end-to-end tracer bullet. Define all core types (`TransformResult`, `Phase`, `PluginKey`, `SchemaTransformerPlugin`, `ModelInterceptor`, `SchemaTransformerEngine`). Implement the engine's `transform()` method with pre-checks (recursive guard, reference short-circuit, null-type error), then iterate through plugins in phase order returning the first `Handled` result. Implement builder DSL with `defaults()`, `addFirst`, `addAfter`, `addBefore`, `replace`, `interceptor`. Add `FallbackPlugin` (produces `FreeFormJson` for unmatched schemas — mirrors `SchemaTransformer.kt:200-208`). Add `NoTransformerFoundException` when chain exhausts without `Handled`. Add `PluginKeys` object with `PluginKeys.Fallback`.

### Acceptance criteria

- [ ] `SchemaTransformerEngine.default()` returns an engine with only the fallback plugin
- [ ] Engine pre-checks: `ResolvedSchema.Recursive` → `Model.Reference` without hitting any plugin
- [ ] Engine pre-checks: `ResolvedSchema.Reference` with `resolveReference=false` → `Model.Reference` without hitting any plugin
- [ ] Engine pre-checks: `type: null` schema → throws error
- [ ] `FallbackPlugin` produces `FreeFormJson` for `ResolvedSchema.Value`, `Model.Reference` for `Recursive`, wrapped `Object.value(FreeFormJson)` for `Reference` — matching `SchemaTransformer.kt:200-208` exactly
- [ ] Builder: `defaults()` populates fallback, duplicate `PluginKey` throws, `replace` on absent key throws
- [ ] Builder: `addFirst`, `addAfter`, `addBefore` produce correct ordering verified by test
- [ ] `ModelInterceptor` chain runs in order after plugin dispatch
- [ ] `NoTransformerFoundException` thrown when empty engine (no fallback) receives a schema
- [ ] All new code compiles on all KMP targets (JVM, macOS Arm64, JS)

---

## Phase 2: Primitive Plugin

**User stories**: Typed primitive handling via engine.

### What to build

A `PrimitivePlugin` in the `TYPED` phase that matches schemas with `type` in `{String, Integer, Number, Boolean}` and no `enumLikeValues`. Delegates to the existing `ResolvedSchema.primitive()` function in `Primitive.kt`. Add `PluginKeys.Primitive`. Wire into `defaults()`. Add engine-based parity tests using `Model.Primitive.String.all()`, `Model.Primitive.Int.all()`, etc. from `Values.kt`.

### Acceptance criteria

- [ ] `PrimitivePlugin` returns `Handled` for all primitive type schemas without enum values
- [ ] `PrimitivePlugin` returns `Pass` for schemas with `enumLikeValues`, composite schemas, object schemas
- [ ] Engine with `defaults()` + `PrimitivePlugin` produces identical output to old dispatcher for all `PrimitiveSpec` test cases
- [ ] Parity verified: String (all formats: binary→ByteArray, uuid→Uuid, date→Date, date-time→DateTime, default→String), Integer (int32→Int, default→Long), Number (float→Float, default→Double), Boolean
- [ ] Default values, constraints, nullability match exactly

---

## Phase 3: Enum Plugin

**User stories**: Typed enum handling + implicit enum (no `type` field).

### What to build

An `EnumPlugin` in the `TYPED` phase that matches schemas with `type` in `{String, Integer, Number, Boolean}` AND `enumLikeValues != null`. Delegates to `ResolvedSchema.toClosedEnum()` in `Enum.kt`. Add `PluginKeys.Enum`. 

An `ImplicitEnumPlugin` in the `IMPLICIT` phase that matches schemas without `type` but with `enumLikeValues != null`. Delegates to the same `toClosedEnum()`. Add `PluginKeys.ImplicitEnum`.

Both wired into `defaults()` at correct positions.

### Acceptance criteria

- [ ] `EnumPlugin` returns `Handled` for typed schemas with enum values
- [ ] `ImplicitEnumPlugin` returns `Handled` for typeless schemas with enum values (dispatcher line 108)
- [ ] Both return `Pass` for schemas without enum values
- [ ] Engine parity with `ClosedEnumSpec` test cases: Int enums, Long enums, String enums, Float enums, Double enums, Boolean enums
- [ ] Enum defaults and nullable enum handling match exactly
- [ ] Error cases (empty enum, invalid default) produce same exceptions

---

## Phase 4: Collection Plugin

**User stories**: Typed collection handling + implicit collection.

### What to build

A `CollectionPlugin` in the `TYPED` phase matching `type == Array`. Delegates to `ResolvedSchema.collection()` in `Collection.kt`. The nested `items` resolution must call `engine.transform()` instead of `toModel()` for nested schemas. Add `PluginKeys.Collection`.

An `ImplicitCollectionPlugin` in the `IMPLICIT` phase matching schemas without `type` but with `items != null` (dispatcher line 112). Delegates to the same `collection()`. Add `PluginKeys.ImplicitCollection`.

### Acceptance criteria

- [ ] `CollectionPlugin` returns `Handled` for `type: array` schemas
- [ ] `ImplicitCollectionPlugin` returns `Handled` for typeless schemas with `items`
- [ ] Both return `Pass` for non-collection schemas
- [ ] Engine parity with `CollectionSpec` test cases
- [ ] Nested item types resolved correctly through engine (not direct `toModel()`)
- [ ] Collection defaults (`[]`, null, single-value) match exactly
- [ ] Reference-wrapped collections produce correct `Model.Object` with `items` property

---

## Phase 5: Object Plugin

**User stories**: Typed object with/without properties, implicit object, implicit map.

### What to build

An `ObjectPlugin` in the `TYPED` phase:
- Matches `type == Object` with `properties?.isNotEmpty()` → delegates to `ResolvedSchema.toObject()` in `Object.kt`
- Matches `type == Object` without properties → delegates to `objectWithoutProperties()` in `SchemaTransformer.kt:140-145`

Add `PluginKeys.Object`.

An `ImplicitObjectPlugin` in the `IMPLICIT` phase:
- Matches typeless schemas with `properties?.isNotEmpty()` (dispatcher line 110) → delegates to `toObject()`
- Matches typeless schemas with `additionalProperties != null` (dispatcher line 111) → delegates to `objectWithoutProperties()`

Add `PluginKeys.ImplicitObject`.

Move `objectWithoutProperties`, `buildTypedAdditionalPropertiesModel`, `buildAllowedAdditionalPropertiesModel` from `SchemaTransformer.kt` to `Object.kt` or a new `ObjectHelpers.kt` so plugins can access them.

### Acceptance criteria

- [ ] `ObjectPlugin` returns `Handled` for `type: object` schemas (with and without properties)
- [ ] `ImplicitObjectPlugin` returns `Handled` for typeless schemas with properties or additionalProperties
- [ ] Engine parity with `ObjectSpec` test cases: properties, readOnly/writeOnly filtering, additionalProperties (typed, allowed true/false), value classes, scalar wrappers
- [ ] `hadPropertiesBeforeStripping` flag preserved correctly
- [ ] `objectWithoutProperties` correctly routes to `FreeFormJson`, typed additionalProperties map, or allowed additionalProperties model
- [ ] Nested property types resolved through engine

---

## Phase 6: TypeArray Plugin

**User stories**: OpenAPI 3.1 multi-type array handling.

### What to build

A `TypeArrayPlugin` in the `TYPED` phase matching `schema.type is Schema.Type.Array` (the multi-type syntax, e.g., `type: [string, null]`). Mirrors dispatcher lines 80-90: strips `Null` from type array, applies nullability, then delegates to `union()` for remaining types. Add `PluginKeys.TypeArray`.

### Acceptance criteria

- [ ] `TypeArrayPlugin` returns `Handled` for `Schema.Type.Array` schemas
- [ ] Returns `Pass` for `Schema.Type.Basic` schemas
- [ ] Null type in array correctly sets `isNullable = true` on the resulting model
- [ ] Multi-type arrays delegate to `union()` / `buildOneOf()` for remaining types
- [ ] Engine parity with `TypeArraySpec` test cases

---

## Phase 7: Composite allOf Plugin

**User stories**: allOf nullable flattening + allOf merge + composite precedence.

### What to build

A shared `CompositePrecedenceGuard` utility (extracted from `compositeShouldTakePrecedenceOverType()` in `SchemaTransformer.kt:117-138`). All composite plugins use this to decide whether composite takes precedence over type.

An `AllOfNullableFlattenPlugin` in the `COMPOSITE` phase matching `isAllOfNullableType()` when `compositeTakesPrecedence`. Handles nullable flattening (dispatcher lines 50-56): filters null branches, delegates single remaining branch to `flattenedSingleNullableBranch()`, otherwise delegates to `allOf()`. Add `PluginKeys.AllOfNullable`.

An `AllOfPlugin` in the `COMPOSITE` phase matching `schema.allOf != null` when `compositeTakesPrecedence`. Delegates to `allOf()` in `AllOf.kt` (dispatcher line 57). Add `PluginKeys.AllOf`.

### Acceptance criteria

- [ ] `CompositePrecedenceGuard` correctly returns true/false matching current `compositeShouldTakePrecedenceOverType()` for all edge cases
- [ ] `AllOfNullableFlattenPlugin` handles nullable allOf: strips null branch, marks result nullable, single-branch unwrap works
- [ ] `AllOfPlugin` handles non-nullable allOf: merge logic, discriminated subtype detection within allOf
- [ ] Both return `Pass` when composite does not take precedence (typed schema without structural composite)
- [ ] Engine parity with `AllOfSpec` test cases: nullable merging, single-branch unwrap, multi-schema merge, constraint merging

---

## Phase 8: Composite oneOf + anyOf Plugins

**User stories**: oneOf/anyOf nullable flattening, single-branch unwrap, union building.

### What to build

For oneOf:
- `OneOfNullableFlattenPlugin` in `COMPOSITE` phase matching `isOneOfNullableType()` when `compositeTakesPrecedence`. Nullable flattening with single-branch unwrap (dispatcher lines 59-65). Add `PluginKeys.OneOfNullable`.
- `OneOfSinglePlugin` in `COMPOSITE` phase matching `schema.oneOf?.size == 1` when `compositeTakesPrecedence`. Unwraps single-element oneOf (dispatcher line 66). Add `PluginKeys.OneOfSingle`.
- `OneOfPlugin` in `COMPOSITE` phase matching `schema.oneOf?.isNotEmpty()` when `compositeTakesPrecedence`. Delegates to `buildOneOf()` in `Union.kt` (dispatcher line 67). Add `PluginKeys.OneOf`.

For anyOf (identical structure):
- `AnyOfNullableFlattenPlugin` — `PluginKeys.AnyOfNullable`
- `AnyOfSinglePlugin` — `PluginKeys.AnyOfSingle`
- `AnyOfPlugin` — `PluginKeys.AnyOf`

All delegate to existing handler functions in `Union.kt`.

### Acceptance criteria

- [ ] Nullable flattening for oneOf/anyOf: strips null branches, marks nullable, single-branch unwrap
- [ ] Single-element oneOf/anyOf unwrap delegates to engine for the single branch
- [ ] Multi-element oneOf builds `Model.OneOf` with discriminator detection (native, tagged custom, structural)
- [ ] Multi-element anyOf builds `Model.AnyOf` with same discriminator logic
- [ ] Engine parity with `OneOfSpec`, `AnyOfSpec`, `UnionSpec` test cases
- [ ] Discriminator property stripping, property hoisting, case naming all preserved
- [ ] Open enum pattern, reference pattern naming fallbacks work correctly

---

## Phase 9: Reference Plugins

**User stories**: Discriminated subtype reference + discriminated object.

### What to build

A `DiscriminatedSubtypePlugin` in the `REFERENCE` phase matching `ResolvedSchema.Reference` where `schema.discriminatedSubtypeOrNull() != null`. Returns `Model.Reference` with the discriminated naming context (dispatcher lines 39-45). Add `PluginKeys.DiscriminatedSubtype`.

A `DiscriminatedObjectPlugin` in the `REFERENCE` phase matching `ResolvedSchema.Reference` where `isObjectWithDiscriminator()`. Delegates to `toDiscriminatedObject()` in `DiscriminatedObject.kt` (dispatcher line 49). Add `PluginKeys.DiscriminatedObject`.

Ordering matters: `DiscriminatedSubtypePlugin` must come before `DiscriminatedObjectPlugin` (matching dispatcher precedence: lines 39-45 before line 49).

### Acceptance criteria

- [ ] `DiscriminatedSubtypePlugin` returns `Handled(Model.Reference(...))` when schema has allOf pointing to a parent with discriminator mapping containing the reference name
- [ ] `DiscriminatedObjectPlugin` returns `Handled(Model.DiscriminatedObject(...))` for references with discriminator + mapping + properties
- [ ] Both return `Pass` for non-reference schemas and references without discriminator
- [ ] Ordering: subtype check fires before object check (if a ref matches both, subtype wins)
- [ ] Engine parity with `DiscriminatedObjectSpec` test cases: mapping, abstract properties, subtype properties, self-mapping default case
- [ ] Nested subtype property resolution goes through engine

---

## Phase 10: Switch Entry Point + Remove Old Dispatcher

**User stories**: Wire engine into `toApiTree()`, remove old code.

### What to build

1. Add `engine: SchemaTransformerEngine = SchemaTransformerEngine.default()` parameter to `OpenAPI.toApiTree()` in `ApiTree.kt:55-70`. Source-compatible for existing callers.
2. Route `Registry.toModel()` calls (in `Registry.kt:28-34`) through the engine instead of calling `ResolvedSchema.toModel(context, resolveReference)` directly.
3. Route all `ReferenceOr<Schema>.toModel()` nested calls from handler functions through the engine (the `context(engine: SchemaTransformerEngine)` receiver is already available from Phase 1).
4. Delete `SchemaTransformer.kt` (the old `ResolvedSchema.toModel()` dispatcher).
5. Move any remaining helpers (`objectWithoutProperties`, `flattenNull`, `flattenedSingleNullableBranch`) that were left in `SchemaTransformer.kt` to appropriate plugin files.
6. Remove any temporary parity scaffolding from earlier phases.

### Acceptance criteria

- [ ] `toApiTree()` accepts optional `engine` parameter, defaults to `SchemaTransformerEngine.default()`
- [ ] `toApiTree()` without engine argument produces identical output to current behavior (full regression)
- [ ] `toApiTree()` with custom engine (e.g., replaced plugin) works correctly
- [ ] `SchemaTransformer.kt` is deleted
- [ ] All existing tests in `typed/src/commonTest/` pass without modification (except imports if moved)
- [ ] `./gradlew :typed:allTests` passes on all KMP targets
- [ ] `./gradlew :renderer:jvmTest` passes (downstream consumer of `toApiTree()`)
- [ ] `./gradlew :gradle-plugin:build` passes
- [ ] No temporary parity scaffolding remains
