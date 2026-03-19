# Flatten oneOf requestBody unions into function overloads

## Problem

When a `requestBody` has a `oneOf` schema, the current codegen produces a **sealed interface** (e.g., `Body`) with wrapper cases and a single `invoke(body: Body)` function. This forces users to manually wrap values in union case constructors — a pattern that is unidiomatic in Kotlin and especially tedious for endpoints with many cases.

For example, GitHub's "set labels" endpoint has 5 oneOf cases:
```json
"oneOf": [
  { "type": "object", "properties": { "labels": { "items": { "type": "string" } } } },
  { "type": "array", "items": { "type": "string" } },
  { "type": "object", "properties": { "labels": { "items": { "type": "object", "properties": { "name": { "type": "string" } } } } } },
  { "type": "array", "items": { "type": "object", "properties": { "name": { "type": "string" } } } },
  { "type": "string" }
]
```

**Current output** (sealed interface):
```kotlin
suspend operator fun invoke(body: Body): Response

@Serializable(with = Body.Serializer::class)
sealed interface Body {
    data class CaseLabelsStrings(...) : Body
    @JvmInline value class CaseStrings(...) : Body
    data class CaseLabelsNames(...) : Body
    @JvmInline value class CaseNames(...) : Body
    @JvmInline value class CaseString(...) : Body
    object Serializer : KSerializer<Body> { ... }
}
```

**Desired output** (overloads):
```kotlin
suspend operator fun invoke(body: LabelsStrings): Response
suspend operator fun invoke(body: List<String>): Response
suspend operator fun invoke(body: LabelsNames): Response
suspend operator fun invoke(body: List<Name>): Response
suspend operator fun invoke(body: String): Response
```

## Design decisions

1. **Always flatten** — regardless of case complexity (not just simple/primitive types).
2. **Typed layer owns the decision** — introduce a new `Route.Body.OverloadedBody` variant in the typed layer rather than pushing flattening logic into the renderer. This is cleaner than the existing path param approach (`PathParamUtils.kt`) which is renderer-only.
3. **Only inline oneOf** — `$ref` bodies that resolve to a named union component keep the sealed interface (the union type has a meaningful name and is reusable).
4. **Optional body handling** — for `required: false`, generate N overloads (body required in each) **plus one extra overload with no body parameter** to represent the "send no body" case. This avoids Kotlin overload resolution ambiguity from `= null` defaults.
5. **Deduplication** — if two union cases resolve to the same Kotlin type, emit only one overload.

## Phases

| Phase | Description | Key files |
|-------|-------------|-----------|
| [Phase 1](./phase_1_plan.md) | Introduce `Route.Body.OverloadedBody` in the typed layer | `Route.kt`, `RequestBody.kt` |
| [Phase 2](./phase_2_plan.md) | Render interface overloads in `ClientRenderer.kt` | `ClientRenderer.kt` |
| [Phase 3](./phase_3_plan.md) | Render implementation overloads in `ImplRenderer.kt` | `ImplRenderer.kt` |
| [Phase 4](./phase_4_plan.md) | Edge cases, optional bodies, update existing tests | Multiple |
| [Phase 5](./phase_5_plan.md) | (Optional) Back-port path param flattening to typed layer | `PathParamUtils.kt`, `Route.kt` |

## Status

- [x] Phase 1 completed on 2026-03-19.
  The typed layer now produces `Route.Body.OverloadedBody` for inline non-discriminated request-body unions, `Route.Bodies.defaultOrNull()` recognizes it, route nesting/top-level model discovery account for it, and typed tests cover inline, `$ref`, and discriminated cases.
- [x] Phase 2 completed on 2026-03-19.
  `ClientRenderer.kt` now flattens `OverloadedBody` into interface overloads, emits inline case types directly in the operation interface, suppresses the nested sealed `Body` wrapper, and stops generating `AttemptDeserialize` support for flattened request bodies.
- [x] Phase 3 completed on 2026-03-19.
  `ImplRenderer.kt` now emits matching overload implementations for flattened request bodies and keeps `setBody(body)` on the concrete overload parameter type.
- [ ] Phase 4 pending.
  Blocked by the JVM overload conflict documented in [ISSUE.md](./ISSUE.md).
- [ ] Phase 5 deferred.

## Notes

- Phase 4 still owns the remaining optional-body/no-body overload shape and broader edge-case cleanup, but the renderer now produces coherent interface+impl overloads for required and nullable-body flattened unions.
- The temporary workaround that dropped `invoke(body: List<Name>)` to avoid the JVM clash with `invoke(body: List<String>)` has been reverted because it changes the generated API surface. The branch is intentionally back in the conflicting state until a better fix lands.
- The concrete blocker is the GitHub labels example: flattened collection overloads are distinct in Kotlin source but erase to the same JVM signature. See [ISSUE.md](./ISSUE.md) for the reproducer and constraints.

## Key files reference

| File | Role |
|------|------|
| `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/Route.kt` | Route model with `Body` sealed interface |
| `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/RequestBody.kt` | Resolves OpenAPI requestBody to `Route.Body` |
| `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRenderer.kt` | Generates client interface (invoke signatures) |
| `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt` | Generates Ktor implementation classes |
| `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/PathParamUtils.kt` | Existing path param flattening (reference pattern) |
| `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/InlineParameterScope.kt` | Inline parameter model deduplication |
| `renderer/src/jvmTest/kotlin/io/github/nomisrev/render/ClientSpec.kt` | Golden file test definitions |
| `renderer/src/jvmTest/resources/kotlinTestData/client/` | Golden file outputs |

## Verification

1. `./gradlew :typed:allTests` — typed layer changes don't break parsing
2. `./gradlew :renderer:compileTestKotlinJvm` — currently fails with a platform declaration clash for `List<String>` vs `List<Name>` overloads
3. `./gradlew :renderer:jvmTest` — blocked until [ISSUE.md](./ISSUE.md) is resolved
4. `UPDATE_GOLDEN=true ./gradlew :renderer:jvmTest` — use only after the overload conflict has a proper fix
5. Manually inspect generated code for the GitHub labels example to confirm overloads remain source-level complete once the issue is resolved
