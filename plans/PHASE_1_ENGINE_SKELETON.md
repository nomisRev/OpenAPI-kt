# Phase 1: Engine Skeleton + Fallback Plugin

> Source: `PROPOSAL_TYPED_MODULE_REWRITE.md`, `TYPED_MODULE_ARCHITECTURE.md`

## Goal

Deliver the thinnest end-to-end tracer bullet: core types, engine with pre-checks, builder DSL, and a single `FallbackPlugin` — proving the framework compiles and runs on all KMP targets.

## Architectural Decisions (apply to all phases)

- **Package**: `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/pipeline/`
- **Plugins sub-packages**: `plugins/reference/`, `plugins/composite/`, `plugins/typed/`, `plugins/implicit/`, `plugins/fallback/`
- **Engine pre-checks (not pluggable)**: (1) Nested reference short-circuit, (2) Recursive reference guard, (3) `type: null` validation error. Fire before any plugin.
- **`resolveReference` removed from plugin API**: Reference policy is engine-owned.
- **Nested re-entry**: Plugins call `engine.transform()` for child schemas, not `toModel()`.
- **Builder**: `SchemaTransformerEngine.build { defaults(); addFirst(...); addAfter(...); addBefore(...); replace(...); interceptor(...) }`
- `PluginKey`**: Ordering helpers use keys, not raw classes. Duplicate keys fail fast. `replace` fails if key absent.
- **Default engine**: `SchemaTransformerEngine.default()` — immutable, reusable, zero-config.
- **Helpers stay as free functions**: `flattenNull`, `flattenedSingleNullableBranch`, `compositeShouldTakePrecedenceOverType`, `enumLikeValues`.

---

## What to build

### Core types (`pipeline/SchemaTransformerPlugin.kt`)

```kotlin
sealed interface TransformResult {
    data class Handled(val model: Model) : TransformResult
    data object Pass : TransformResult
}

enum class Phase {
    REFERENCE, COMPOSITE, TYPED, IMPLICIT, FALLBACK,
}

value class PluginKey(val name: String)

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

### Model interceptor (`pipeline/ModelInterceptor.kt`)

```kotlin
interface ModelInterceptor {
    context(ctx: Registry)
    suspend fun intercept(model: Model, context: SchemaContext): Model
}
```

### Plugin keys (`pipeline/PluginKeys.kt`)

Each key is typed to its plugin class. Start with `PluginKeys.Fallback`. Add keys in later phases.

```kotlin
object PluginKeys {
    val Fallback = PluginKey("fallback")
}
```

### Engine (`pipeline/SchemaTransformerEngine.kt`)

`transform(schema: ResolvedSchema, context: SchemaContext, resolveReference: Boolean): Model`

Engine pre-checks (before plugin chain):
1. `ResolvedSchema.Reference` with `resolveReference=false` → `Model.Reference(discriminatedSubtypeOrNull(...) ?: name, description(), isNullable, schema.title)` — mirrors `SchemaTransformer.kt:31-37`
2. `ResolvedSchema.Recursive` → `Model.Reference(name, description(), isNullable, schema.title)` — mirrors `SchemaTransformer.kt:47`
3. `schema.type == Schema.Type.Basic.Null` → `error(...)` — mirrors `SchemaTransformer.kt:104-105`

Plugin dispatch: iterate plugins in phase order, return first `Handled`. If chain exhausts → throw `NoTransformerFoundException`.

Interceptor chain: fold over interceptors after dispatch.

### Builder DSL

```kotlin
class SchemaTransformerEngineBuilder {
    fun defaults()  // Installs all default plugins + fallback
    fun addFirst(phase: Phase, plugin: SchemaTransformerPlugin)
    fun addAfter(key: PluginKey, plugin: SchemaTransformerPlugin)
    fun addBefore(key: PluginKey, plugin: SchemaTransformerPlugin)
    fun replace(key: PluginKey, plugin: SchemaTransformerPlugin)
    fun interceptor(interceptor: ModelInterceptor)
}

fun SchemaTransformerEngine.Companion.build(
    block: SchemaTransformerEngineBuilder.() -> Unit
): SchemaTransformerEngine

fun SchemaTransformerEngine.Companion.default(): SchemaTransformerEngine
```

Builder rules:
- `build {}` starts empty unless `defaults()` called
- Duplicate `PluginKey` throws `IllegalArgumentException`
- `replace(...)` on absent key throws `IllegalArgumentException`
- `addAfter`/`addBefore` on absent key throws `IllegalArgumentException`

### Fallback plugin (`pipeline/plugins/fallback/FallbackPlugin.kt`)

Matches any schema that reaches it. Produces output matching `SchemaTransformer.kt:200-208`:

```kotlin
// ResolvedSchema.Value → FreeFormJson(description(), Constraints.Object(schema), isNullable, schema.title)
// ResolvedSchema.Recursive → Model.Reference(name, description(), isNullable, schema.title)
// ResolvedSchema.Reference → Object.value(reference, FreeFormJson(...), title = schema.title)
```

Always returns `TransformResult.Handled`.

---

## Files to create

| File | Action |
|------|--------|
| `pipeline/SchemaTransformerPlugin.kt` | **Create** — `TransformResult`, `Phase`, `PluginKey`, `SchemaTransformerPlugin` |
| `pipeline/ModelInterceptor.kt` | **Create** — `ModelInterceptor` interface |
| `pipeline/PluginKeys.kt` | **Create** — `object PluginKeys { val Fallback = PluginKey("fallback") }` |
| `pipeline/SchemaTransformerEngine.kt` | **Create** — Engine class, builder, `default()`, `build {}` |
| `pipeline/plugins/fallback/FallbackPlugin.kt` | **Create** — Fallback handler |

## Files to modify

None. Old dispatcher stays untouched.

---

## Acceptance criteria

- [ ] `SchemaTransformerEngine.default()` returns an engine with only the fallback plugin
- [ ] Engine pre-check: `ResolvedSchema.Recursive` → `Model.Reference` without hitting any plugin
- [ ] Engine pre-check: `ResolvedSchema.Reference` with `resolveReference=false` → `Model.Reference` without hitting any plugin
- [ ] Engine pre-check: `type: null` schema → throws error with message matching `SchemaTransformer.kt:104-105`
- [ ] `FallbackPlugin` produces `FreeFormJson` for `ResolvedSchema.Value`
- [ ] `FallbackPlugin` produces `Model.Reference` for `ResolvedSchema.Recursive`
- [ ] `FallbackPlugin` produces wrapped `Object.value(FreeFormJson)` for `ResolvedSchema.Reference`
- [ ] Builder: `defaults()` populates fallback
- [ ] Builder: duplicate `PluginKey` throws
- [ ] Builder: `replace` on absent key throws
- [ ] Builder: `addFirst`, `addAfter`, `addBefore` produce correct ordering (verified by test inspecting plugin order)
- [ ] `ModelInterceptor` chain runs in registration order after plugin dispatch
- [ ] `NoTransformerFoundException` thrown when empty engine receives a schema
- [ ] All new code compiles on all KMP targets: `./gradlew :typed:allTests`
