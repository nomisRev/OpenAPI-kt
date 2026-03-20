# ISSUE: mergeTextConstraints Copies `pattern` From Self Instead of `other`

## Problem

`Schema.mergeTextConstraints()` in `DiscriminatedObject.kt` contained a self-copy bug on the `pattern` field:

```kotlin
pattern = pattern ?: pattern  // always resolves to `this.pattern`
```

The right-hand operand should be `other.pattern` so that the result carries the pattern from the other schema when `this.pattern` is absent.

## Why This Matters

`mergeTextConstraints` is called when merging two `Schema` objects during discriminated-object composition. If `this` has no pattern constraint but `other` does, the merged schema silently drops the constraint. This can produce a wider type than intended and cause downstream validation to accept strings that the spec rejects.

## Current Behavior (before fix)

```kotlin
fun Schema.mergeTextConstraints(other: Schema): Schema =
    copy(
        minLength = let(minLength, other.minLength, ::maxOf),
        maxLength = let(maxLength, other.maxLength, ::minOf),
        pattern = pattern ?: pattern  // BUG: ignores other.pattern
    )
```

## Desired Outcome

```kotlin
pattern = pattern ?: other.pattern
```

The merged schema carries the most restrictive pattern available. Full intersection of multiple regex patterns is non-trivial (left as a TODO comment).

## Fix Applied

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/DiscriminatedObject.kt:244`

Changed:
```kotlin
pattern = pattern ?: pattern // TODO
```
To:
```kotlin
pattern = pattern ?: other.pattern // TODO: intersection of multiple patterns is non-trivial
```

## Acceptance Criteria

1. `mergeTextConstraints` propagates `other.pattern` when `this.pattern` is null.
2. `./gradlew :typed:allTests` passes.
