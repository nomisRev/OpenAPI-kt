# Proposal: Phased Transformation Engine for `typed`

## Status

Draft.

This proposal replaces the current `ResolvedSchema.toModel(...)` dispatcher in `SchemaTransformer.kt` with a phased engine that keeps default behavior intact while making dispatch explicit, testable, and extensible.

## Problem

The current `typed` transformation flow is centered around one precedence-driven `when` block. That has two concrete problems:

1. **Order is fragile.** Adding a new branch is risky because correctness depends on relative position.
2. **Extension is impossible.** Users cannot hook custom schema handling or post-process generated `Model` values without forking core code.

The rewrite should solve those problems without changing the `Model` IR or breaking current call sites.

## Goals

1. Preserve current default transformation semantics.
2. Make dispatch order explicit and locally testable.
3. Allow controlled extension at two levels:
   - schema dispatch (`ResolvedSchema -> Model`)
   - model post-processing (`Model -> Model`)
4. Centralize recursion and reference safety in one place.
5. Keep the zero-config path simple for existing callers.

## Non-Goals

1. Reworking `Model`, `NamingContext`, or renderer behavior as part of this rewrite.
2. Adding runtime plugin discovery or classpath scanning.
3. Refactoring unrelated modules.
4. Promising that every default handler class becomes part of a stable public ABI.

## Hard Constraints

These rules stay outside plugin code and remain engine-owned:

1. **Nested reference short-circuit.** When nested resolution should not inline a `$ref`, the engine returns `Model.Reference` directly.
2. **Recursive reference guard.** `ResolvedSchema.Recursive` always becomes `Model.Reference`.
3. **Deterministic ordering.** The default engine is immutable and produces the same result for the same input.
4. **Recursive sub-schema dispatch.** Nested schemas re-enter the same engine, so custom handlers apply consistently at all depths.
5. **Fail fast on bad configuration.** Misconfigured pipelines throw instead of silently degrading.

This removes `resolveReference: Boolean` from handler APIs. Reference policy belongs to the engine, not to individual handlers.

---

## Architecture

The rewrite has two phases with different responsibilities.

| Phase | Concern | Extension Model |
|-------|---------|-----------------|
| **Phase 1** | `ResolvedSchema -> Model` dispatch | Ordered schema handlers |
| **Phase 2** | `Model -> Model` post-processing | Ordered interceptors |

The split matters. Dispatch decides **what kind of model** a schema becomes. Interception adjusts an already-built model without competing with core precedence rules.

### Phase 1: Schema Handlers

```kotlin
sealed interface TransformResult {
    data class Handled(val model: Model) : TransformResult
    data object Pass : TransformResult
}

enum class Phase {
    REFERENCE,
    COMPOSITE,
    TYPED,
    IMPLICIT,
    FALLBACK,
}

@JvmInline
value class PluginKey(val value: String)

interface SchemaTransformerPlugin {
    val key: PluginKey
    val phase: Phase

    context(ctx: Registry.Scope, engine: SchemaTransformerEngine)
    suspend fun transform(
        schema: ResolvedSchema,
        context: SchemaContext,
        next: suspend (ResolvedSchema, SchemaContext) -> Model,
    ): TransformResult
}
```

Notes:

- `Pass` means “this handler does not own the schema”.
- `next(...)` enables wrapper behavior for the few handlers that genuinely need it.
- Stable ordering should rely on `PluginKey`, not concrete implementation classes.

That last point is important. The original draft exposed default handler classes to support `addBefore<ObjectPlugin>(...)`. That leaks internal structure into the public API too early. Stable keys are cheaper to support long-term.

### Engine-Owned Pre-Checks

Before the handler chain runs, the engine performs the structural checks that should never be user-overridden:

1. Nested reference short-circuit.
2. Recursive reference guard.
3. Validation of impossible states that should still fail loudly, such as an unresolved bare `type: null`.

Handlers never receive those cases.

### Recursive Sub-Schema Resolution

Handlers that need to resolve nested schemas call back into the engine:

```kotlin
context(ctx: Registry.Scope, engine: SchemaTransformerEngine)
suspend fun transform(
    schema: ResolvedSchema,
    context: SchemaContext,
    next: suspend (ResolvedSchema, SchemaContext) -> Model,
): TransformResult {
    val propertyModel = engine.transform(propertySchema, context)
    return TransformResult.Handled(/* ... */)
}
```

This keeps behavior consistent for nested objects, unions, collections, and custom user handlers.

### Default Handler Layout

The implementation should stay close to the current branch structure, but the proposal should not hard-code an exact handler count. The previous draft claimed “~21 plugins” while the table actually described 22. That is noise. What matters is stable precedence.

Recommended default ordering:

#### `REFERENCE`

- discriminated subtype reference
- discriminated object reference

#### `COMPOSITE`

- composite precedence guard
- `allOf` nullable flattening
- `allOf`
- `oneOf` nullable flattening
- `oneOf` single-branch unwrap
- `oneOf`
- `anyOf` nullable flattening
- `anyOf` single-branch unwrap
- `anyOf`

#### `TYPED`

- OpenAPI 3.1 multi-type array handling
- explicit object with properties
- explicit object without properties
- collection
- typed enum
- primitive

#### `IMPLICIT`

- enum without explicit `type`
- object inferred from `properties`
- map inferred from `additionalProperties`
- collection inferred from `items`

#### `FALLBACK`

- `FreeFormJson`

### No-Match Behavior

If the chain exhausts without a handler returning `Handled`, the engine throws `NoTransformerFoundException`.

The default configuration should always end with the fallback handler, so a throw indicates user misconfiguration or an internal bug.

### Shared Helpers

Keep existing cross-cutting helpers as plain functions or extensions:

- `flattenNull`
- `flattenedSingleNullableBranch`
- `compositeShouldTakePrecedenceOverType`
- `enumLikeValues`

Do not introduce a deep inheritance tree for handlers.

---

## Phase 2: Model Interceptors

Interceptors run after schema dispatch has produced a `Model`.

```kotlin
interface ModelInterceptor {
    context(ctx: Registry)
    suspend fun intercept(model: Model, context: SchemaContext): Model
}
```

Applied in order:

```kotlin
val intercepted = interceptors.fold(model) { acc, interceptor ->
    interceptor.intercept(acc, context)
}
```

Rules:

1. Interceptors do not participate in schema dispatch.
2. Interceptors are sequential and explicit.
3. Interceptors should be used for policy changes, not for re-implementing core dispatch.

Example:

```kotlin
class MakeUpdateModelOptionalInterceptor : ModelInterceptor {
    context(ctx: Registry)
    override suspend fun intercept(model: Model, context: SchemaContext): Model {
        if (model is Model.Object && model.name.head is NamingContext.Reference) {
            val ref = model.name.head as NamingContext.Reference
            if (ref.name == "UpdateUserRequest") {
                val optionalProps = model.properties.mapValues { it.value.copy(isRequired = false) }
                return model.copy(properties = optionalProps)
            }
        }
        return model
    }
}
```

---

## Public Configuration API

The public API should make the common case obvious and the advanced case possible.

### Default Engine

```kotlin
val engine = SchemaTransformerEngine.default()
```

- immutable
- reusable
- zero-config

### Custom Engine

```kotlin
val engine = SchemaTransformerEngine.build {
    defaults()

    addFirst(Phase.COMPOSITE, MyCustomCompositePlugin())
    addAfter(PluginKeys.Object, MyObjectPostProcessor())
    addBefore(PluginKeys.Primitive, MyCustomTypePlugin())
    replace(PluginKeys.Enum, MyEnumOverride())

    interceptor(MakeUpdateModelOptionalInterceptor())
}
```

Recommended builder rules:

1. `build {}` starts empty unless `defaults()` is called.
2. Duplicate `PluginKey` values fail fast.
3. `replace(...)` fails if the target key does not exist.
4. Ordering helpers operate on keys, not classes.

This gives a stable extension surface without making every built-in handler type public API.

### Wiring into `toApiTree()`

```kotlin
suspend fun OpenAPI.toApiTree(
    name: String,
    preprocessor: OpenApiPreprocessor = OpenApiPreprocessor.Identity,
    engine: SchemaTransformerEngine = SchemaTransformerEngine.default(),
): ApiTree
```

This is source-compatible for existing callers.

---

## Package Layout

All new code should live under:

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/pipeline/`

Suggested structure:

```text
pipeline/
├── SchemaTransformerEngine.kt
├── SchemaTransformerPlugin.kt
├── ModelInterceptor.kt
├── PluginKeys.kt
└── plugins/
    ├── reference/
    ├── composite/
    ├── typed/
    ├── implicit/
    └── fallback/
```

Do not freeze file-per-handler layout in the proposal. That is an implementation detail.

The old `transformers/SchemaTransformer.kt` stays in place until parity is proven.

---

## Migration Plan

Keep the migration small and reversible.

### PR 1 — Engine Skeleton

- add `Phase`
- add `PluginKey`
- add `TransformResult`
- add `SchemaTransformerPlugin`
- add `ModelInterceptor`
- add `SchemaTransformerEngine`
- add focused engine tests

No behavior change yet.

### PR 2 — Extract Default Handlers

- mechanically extract current branches into ordered handlers
- keep helper functions where they are unless relocation is obviously cleaner
- add focused tests per extracted handler where coverage is currently weak

Still no public wiring change.

### PR 3 — Parity Suite

- add a temporary parity test suite in `typed` tests, preferably `jvmTest`
- run old dispatcher and new engine against real specs and existing targeted fixtures
- diff failures through `ModelJson.encodeToString()` or equivalent readable output

Do not create a new module unless test isolation proves necessary.

### PR 4 — Switch Entry Point

- add `engine` to `toApiTree()`
- route `toModel()` calls through the engine
- remove the old dispatcher once parity passes
- delete temporary parity-only scaffolding

---

## Testing Strategy

### Engine Tests

- phase ordering
- `next(...)` behavior
- no-match failure
- builder operations: `defaults`, `addBefore`, `addAfter`, `replace`
- duplicate key rejection

### Handler Tests

- keep them focused on the branch each handler owns
- reuse existing typed test infrastructure where possible
- add helper harnesses only where they reduce duplication

### Parity Tests

- use real-world specs already available in repo tests/resources
- compare old and new output for the same schema inputs
- keep this suite temporary

Prefer targeted validation. Do not jump to broad repo-wide builds unless the rewrite crosses module boundaries.

---

## Risks and Mitigations

| Risk | Why it matters | Mitigation |
|------|----------------|------------|
| Public API too wide | Exposing handler classes locks internal design | Use stable `PluginKey` values as public anchors |
| Ordering regressions | Same logic, wrong order, different output | Preserve phase order and add parity coverage |
| Overuse of interceptors | Post-processing can become a second dispatcher | Keep interceptors strictly post-model and document that limit |
| Migration drag | Two pipelines can diverge during rollout | Keep parity suite temporary and remove old path quickly after switch |

---

## Final Recommendation

Proceed with the rewrite, but keep the contract smaller than the original draft suggested.

The important decisions are:

1. engine-owned recursion/reference policy
2. explicit phases
3. stable public ordering via `PluginKey`
4. temporary parity testing before deleting the old dispatcher

That gives the `typed` module a controlled extension surface without turning internal branch structure into a long-term maintenance burden.
