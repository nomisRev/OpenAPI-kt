# ISSUE: `Literal` Path Segments Missing Keyword Escape in Navigation Property Names

## Problem

`PathSegment.addConcreteNavigationMember()` in `ImplRenderer.kt` uses `name.toCamelCase()` for `Literal` segments but `name.toParamName()` for `FixedValue` segments. The two differ in one critical way: `toParamName()` wraps the result in backticks when it collides with a Kotlin keyword; `toCamelCase()` does not.

## Why This Matters

A path like `/object/items` produces a `Literal("object")` segment. `toCamelCase()` emits the property name `object`, which is an unescaped Kotlin keyword and causes a compilation error in the generated client. The same issue applies to any other keyword that appears as a literal path segment (`class`, `fun`, `val`, `var`, `for`, `in`, etc.).

## Current Behavior (before fix)

```kotlin
is PathSegment.Literal -> builder.addProperty(
    PropertySpec.builder(name.toCamelCase(), childClassName)   // no keyword guard
        ...
)
```

## Desired Outcome

```kotlin
is PathSegment.Literal -> builder.addProperty(
    PropertySpec.builder(name.toParamName(), childClassName)   // safe: backtick-wraps keywords
        ...
)
```

`toParamName()` calls `toCamelCase()` internally, so the camel-case transformation is preserved; only the missing safety layer is added.

## Fix Applied

`renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt:122`

Changed `name.toCamelCase()` → `name.toParamName()` in the `is PathSegment.Literal` branch of `addConcreteNavigationMember()`.

## Affected Files

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/NamingUtils.kt` (reference — no change needed)

## Acceptance Criteria

1. A spec with a literal path segment whose camel-case form is a Kotlin keyword generates a backtick-escaped property name.
2. Non-keyword literal segments are unaffected.
3. `./gradlew :renderer:jvmTest` passes.
