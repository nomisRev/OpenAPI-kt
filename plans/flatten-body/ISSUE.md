# ISSUE: JVM Signature Clash For Flattened Collection Overloads

## Summary

Flattening inline `oneOf` request bodies into Kotlin overloads works for most case combinations, but it breaks on JVM when two collection cases differ only by generic argument.

The concrete reproducer is the GitHub labels endpoint, which wants both of these overloads:

```kotlin
suspend operator fun invoke(body: List<String>): Response
suspend operator fun invoke(body: List<Name>): Response
```

These are distinct in Kotlin source, but both erase to the same JVM signature:

```kotlin
invoke(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
```

## Reproducer

The failing golden is:

- `renderer/src/jvmTest/resources/kotlinTestData/client/inline-oneof-request-body-collection-inline-items/Repos.kt`

The current compile failure is:

```text
Platform declaration clash: The following declarations have the same JVM signature
(invoke(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;):
    suspend fun invoke(body: List<String>, ...)
    suspend fun invoke(body: List<Name>, ...)
```

## Current State

- The earlier workaround that deduplicated overloads by JVM-erased signature has been reverted.
- That workaround kept the generated sources buildable, but it silently removed `invoke(body: List<Name>)` from the public API.
- We do not want to keep that workaround because it changes the flattened API shape instead of solving the underlying problem.

## Requirements For The Fix

- Preserve the intended source-level API shape for flattened bodies.
- Do not silently drop one overload just because the JVM cannot distinguish the erased signatures.
- Keep the body-flattening ergonomics that motivated this refactor.
- Keep interface and implementation generation aligned.
- Keep the shared inline domain-model deduplication that now exists for `Name`.

## Open Question

We need a different encoding for this specific class of overloads. Possible directions include changing callable shape, introducing a disambiguating wrapper or alternate entrypoint, or using a target-aware strategy, but the actual fix is still to be designed.

## Next Step

Resolve this issue before marking Phase 4 complete or expecting `:renderer:jvmTest` to pass again.
