# Phase 5: (Optional) Back-port path param flattening to typed layer

## Goal

Move the path parameter union flattening decision from the renderer (`PathParamUtils.kt`) into the typed layer, for consistency with the new `OverloadedBody` approach.

## Motivation

After Phases 1-4, we have two different flattening patterns:
- **Request body**: Decided in the typed layer (`OverloadedBody` variant), renderer just maps to overloads
- **Path params**: Decided in the renderer (`isFlattenablePathUnion()` in `PathParamUtils.kt`), typed layer is unaware

This inconsistency means the renderer still contains domain logic about when to flatten. Moving path param flattening to the typed layer would:
1. Make the typed layer the single source of truth for "should this union be overloaded?"
2. Simplify the renderer to pure mechanical code generation
3. Allow future flattening decisions (e.g., query params) to follow the same pattern

## Status

**Completed on 2026-03-19**. The typed layer now emits `PathSegment.OverloadedParameter` for supported inline simple path unions, enforces the multiple-enum guard during route construction, and the renderer only consumes the typed variant.

## Sketch

### Typed layer

Introduce a new `PathSegment.Parameter` variant or add a `flattenedCases` field:

```kotlin
data class Parameter(
    val name: String,
    val model: Model,
    val flattenedCases: List<Model>?,  // non-null when union should be overloaded
)
```

Or introduce a parallel to `OverloadedBody`:
```kotlin
sealed interface PathSegment {
    data class Literal(val name: String) : PathSegment
    data class Parameter(val name: String, val model: Model) : PathSegment
    data class OverloadedParameter(val name: String, val cases: List<Model.Union.Case>) : PathSegment
}
```

### Renderer

Replace `isFlattenablePathUnion()` / `requireSupportedFlattenablePathUnion()` checks in `ClientRenderer.kt` and `ImplRenderer.kt` with simple pattern matching on the new `PathSegment` variant.

`PathParamUtils.kt` would be reduced to just the `routeSegmentSimpleName()` / `childInterfaceSimpleName()` utilities, or removed entirely.

## Risk

Low risk refactor since path param behavior is well-tested with golden files. Main concern is ensuring the typed layer has access to all information needed to make the flattenability decision (currently it's just "all cases are simple types + max 1 enum").

## Acceptance criteria

- [x] `PathParamUtils.isFlattenablePathUnion()` logic moved to typed layer
- [x] `requireSupportedFlattenablePathUnion()` guard moved to typed layer
- [x] Renderer only pattern-matches on typed variants, no domain logic
- [x] All existing path param golden file tests pass unchanged
- [x] `./gradlew :renderer:jvmTest` passes
