# ISSUE: Dead Branches In expandFiniteEnumPathSegmentAt Allow Subtle Type-Coverage Gaps

## Problem

`Route.kt` contains `expandFiniteEnumPathSegmentAt(index: Int)`. The function begins with an
early-return guard:

```kotlin
if (segment is PathSegment.Literal || segment is PathSegment.FixedValue) return null
```

Immediately after, a `when` expression computes the model for the segment:

```kotlin
val model = pathInput?.type ?: when (segment) {
    is PathSegment.Parameter -> segment.model
    is PathSegment.OverloadedParameter -> segment.type
    is PathSegment.Literal,
    is PathSegment.FixedValue -> return null   // unreachable — already returned above
}
```

The two `Literal` and `FixedValue` branches in the `when` are structurally unreachable. The
compiler cannot prove this statically because the early-return guard operates on the outer variable
`segment` without a smart cast that would shrink the type before the `when`. The net result is:

1. The dead branches add noise that makes the actual logic harder to read.
2. They give a false sense of completeness. A reader might believe the `when` is exhaustive for all
   four sealed variants, when in practice the exhaustive coverage is split across two disjoint
   code paths with no single place that is provably complete.
3. If `PathSegment` gains a new variant in the future, the compiler will demand a new branch in the
   `when` — but that branch will also be unreachable unless the guard is updated in sync. The two
   sites can drift apart silently.

## Why This Matters

When a sealed hierarchy is handled with `when`, the ideal form either:

- Uses an exhaustive `when` over the sealed type so the compiler enforces coverage, or
- Uses a single early-return for the inapplicable variants, followed by a `when` that is
  structurally exhaustive over the remaining variants without an `else`.

Mixing both patterns (early return + redundant `when` branches) is a maintenance hazard. The `else`
escape hatch or redundant dead branches are the root cause of subtle bugs like the missing
`FixedValue`/`Literal` normalization (see `ISSUE_FIXED_VALUE_LITERAL_MERGE_NORMALIZATION.md`),
where a missing case in one function caused incorrect behavior that only surfaced at runtime.

## Fix Applied

The dead `Literal` and `FixedValue` branches were removed from the `when`. The `when` now covers
only `Parameter` and `OverloadedParameter`, which are the only variants reachable after the guard.
The Kotlin compiler infers an exhaustive `when` because both remaining sealed variants are covered:

```kotlin
val model = pathInput?.type ?: when (segment) {
    is PathSegment.Parameter -> segment.model
    is PathSegment.OverloadedParameter -> segment.type
}
```

## Suggested Follow-Up

The same pattern — an outer `if`/`is` guard followed by a `when` that re-lists the guarded variants
as dead branches — may appear elsewhere. A systematic search for `when` expressions over
`PathSegment` or other sealed types that contain provably unreachable branches is recommended.

## Affected Files

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/Route.kt` — fix applied

## Acceptance Criteria

1. The `when` expression in `expandFiniteEnumPathSegmentAt` contains no `Literal` or `FixedValue`
   branches.
2. The compiler enforces exhaustive coverage of `PathSegment` variants through the compiler's
   sealed-class exhaustiveness check, not through a silent `else` or redundant dead branches.
3. If `PathSegment` gains a new variant, the compiler reports an error at every non-exhaustive
   `when` site immediately.
4. `./gradlew :typed:allTests` passes.
