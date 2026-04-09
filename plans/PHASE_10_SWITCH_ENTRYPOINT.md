# Phase 10: Switch Entry Point + Remove Old Dispatcher

> Depends on: All previous phases (1-9)

## Goal

Wire the engine into `toApiTree()`, route all `toModel()` calls through the engine, delete the old dispatcher, and clean up temporary scaffolding.

---

## What to build

### 1. Add `engine` parameter to `toApiTree()`

**File**: `ApiTree.kt:55-70`

```kotlin
suspend fun OpenAPI.toApiTree(
    name: String = info.title.toPascalCase(),
    preprocessor: OpenApiPreprocessor = OpenApiPreprocessor.None,
    engine: SchemaTransformerEngine = SchemaTransformerEngine.default(),
): ApiTree
```

Source-compatible for existing callers due to default parameter.

### 2. Route `Registry.toModel()` through the engine

**File**: `registry/Registry.kt:28-34`

Currently:
```kotlin
suspend fun NamingContext.Reference.toModel(): Model =
    ReferenceOr.schema(name).toModel(NamingContext(this, emptyList()), context)

suspend fun ReferenceOr<Schema>.toModel(name: NamingContext, context: SchemaContext): Model =
    with(ScopeImpl(null, emptySet())) {
        resolve(name, context) { it.toModel(context, true) }
    }
```

Change the inner `it.toModel(context, true)` to call `engine.transform(it, context, resolveReference = true)`. The engine must be available in scope — either passed through `Registry` constructor or via context receiver.

### 3. Route nested `toModel()` calls in handlers through the engine

Several handler functions call `ReferenceOr<Schema>.toModel(name, context)` or `ResolvedSchema.toModel(context, resolveReference)` for nested schemas:

| File | Line | Call |
|------|------|------|
| `Enum.kt` | 19 | `ResolvedSchema.Value(...).toModel(context, false)` |
| `Object.kt` | 60 | `propSchema.toModel(context, false)` |
| `Object.kt` | 72 | `ap.value.toModel(name, context)` |
| `Collection.kt` | 42, 69 | `schema.items?.toModel(name, context)` |
| `Union.kt` | 179-197 | `it.toModel(context, false/true)` in case building |
| `AllOf.kt` | 34 | `ReferenceOr.value(...).toModel(name, context)` |
| `DiscriminatedObject.kt` | 87 | `.toObject(context, properties)` |
| `SchemaTransformer.kt` | 161, 177 | `ap.value.toModel(name, context)` in `buildTypedAdditionalPropertiesModel` |

All `toModel()` calls must route through the engine. The engine context receiver (`context(engine: SchemaTransformerEngine)`) makes this available.

For `ReferenceOr<Schema>.toModel(name, context)`: this goes through `Registry.toModel()` which already routes to the engine (step 2).

For `ResolvedSchema.toModel(context, resolveReference)`: replace with `engine.transform(this, context, resolveReference)`.

### 4. Delete `SchemaTransformer.kt`

After all calls are rewired, delete `transformers/SchemaTransformer.kt` entirely. This removes:
- `ResolvedSchema.toModel(context, resolveReference)` — the old dispatcher
- `compositeShouldTakePrecedenceOverType()` — moved to `CompositePrecedenceGuard`
- `objectWithoutProperties()`, `buildTypedAdditionalPropertiesModel()`, `buildAllowedAdditionalPropertiesModel()` — moved to `Object.kt` in Phase 5
- `flattenNull()`, `flattenedSingleNullableBranch()` — moved to composite helpers in Phase 7
- `fallback()` — replaced by `FallbackPlugin`

### 5. Clean up temporary scaffolding

- Remove any `internal` visibility overrides added for parity testing
- Remove any temporary test harnesses that compared old vs new output
- Ensure all imports updated across the codebase

---

## Files to modify

| File | Action |
|------|--------|
| `ApiTree.kt` | **Modify** — add `engine` parameter to `toApiTree()`, pass to `Registry` |
| `registry/Registry.kt` | **Modify** — accept engine, route `toModel()` through it |
| `transformers/Enum.kt` | **Modify** — route nested `toModel()` through engine |
| `transformers/Object.kt` | **Modify** — route nested `toModel()` through engine |
| `transformers/Collection.kt` | **Modify** — route nested `toModel()` through engine |
| `transformers/Union.kt` | **Modify** — route nested `toModel()` through engine |
| `transformers/AllOf.kt` | **Modify** — route nested `toModel()` through engine |
| `transformers/DiscriminatedObject.kt` | **Modify** — route nested `toModel()` through engine |

## Files to delete

| File | Action |
|------|--------|
| `transformers/SchemaTransformer.kt` | **Delete** |

---

## Risk: Engine threading

The engine must be available wherever `toModel()` is called. Options:
1. **Context receiver**: `context(engine: SchemaTransformerEngine)` on all handler functions — clean but requires updating every signature
2. **Registry field**: Store engine in `Registry`, access via `Registry.Scope` — minimal signature changes
3. **ThreadLocal/CoroutineContext**: Store in coroutine context — no signature changes but implicit

Recommendation: Option 2 (Registry field) is least invasive. `Registry` already holds the OpenAPI spec and scope state. Adding the engine there is natural.

---

## Verification

### Full regression

```bash
./gradlew :typed:allTests       # All KMP targets
./gradlew :renderer:jvmTest     # Downstream consumer
./gradlew :gradle-plugin:build  # Plugin integration
```

### Specific checks

- `toApiTree()` without engine argument → identical output to pre-rewrite
- `toApiTree()` with custom engine (e.g., replaced plugin) → custom behavior applies
- No references to `SchemaTransformer.toModel()` remain in codebase
- No temporary parity scaffolding remains
- All test data from all previous phases still passes through the engine

---

## Acceptance criteria

- [ ] `toApiTree()` accepts optional `engine` parameter, defaults to `SchemaTransformerEngine.default()`
- [ ] `toApiTree()` without engine argument produces identical output to pre-rewrite behavior
- [ ] `toApiTree()` with custom engine applies custom plugin behavior at all depths
- [ ] `SchemaTransformer.kt` is deleted
- [ ] No calls to old `ResolvedSchema.toModel(context, resolveReference)` remain
- [ ] All nested `toModel()` calls in handlers route through engine
- [ ] `./gradlew :typed:allTests` passes on JVM, macOS Arm64, JS
- [ ] `./gradlew :renderer:jvmTest` passes
- [ ] `./gradlew :gradle-plugin:build` passes
- [ ] No temporary parity scaffolding or `internal` visibility hacks remain
- [ ] Existing test infrastructure (`VerifyAll.kt`, `Values.kt`, etc.) unchanged or minimally updated (imports only)
