# Phase 4: Handle edge cases & update existing tests

## Goal

Handle optional bodies, `$ref` unions, serialization utils cleanup, and update all existing oneOf requestBody golden file tests.

## Edge cases

### 1. Optional body (`required: false`)

When the requestBody is optional, generating N overloads all with `body: CaseType? = null` creates Kotlin overload resolution ambiguity.

**Solution**: Generate N overloads (body is **required** in each) **plus one extra overload with no body parameter**:

```kotlin
// Interface
public interface Put {
    // Per-case overloads (body required in each)
    public suspend operator fun invoke(body: LabelsStrings): Response
    public suspend operator fun invoke(body: List<String>): Response
    public suspend operator fun invoke(body: String): Response
    // No-body overload
    public suspend operator fun invoke(): Response
}
```

Implementation for the no-body overload:
```kotlin
override suspend operator fun invoke(): Response {
    val value: ... = client.put("/path").body()
    return Response(value)
}
```

The no-body overload skips the request config block for body (`setBody`/`contentType`) but still includes non-path parameter configuration.

#### Files affected
- `ClientRenderer.kt` — In the overloaded body branch of `toOperationTypeSpec()`, when `body.required == false` (from `Route.Bodies.required`), emit an additional `invoke()` with no body parameter.
- `ImplRenderer.kt` — Same: when `!bodies.required`, emit an additional override with no body setup code.

### 2. `$ref` body that resolves to a union

A request body with `"schema": { "$ref": "#/components/schemas/MyUnion" }` resolves to a `Model.Union` whose `context.head` is `NamingContext.Reference` (not `NamingContext.Path`).

The Phase 1 condition already handles this:
```kotlin
model.context.head is NamingContext.Path  // only inline unions
```

`$ref` unions will continue to produce `SetBody` with the union type, keeping the sealed interface pattern. **No additional changes needed**, but add a test to verify this behavior is preserved.

### 3. SerializationUtils cleanup

When a body is flattened into overloads, we no longer need:
- The `Body.Serializer` custom serializer
- The `AttemptDeserialize` utility (if it was only needed for this body)

The existing `hasInlineNonDiscriminatedBodyUnion()` check in `ClientRenderer.kt` (line ~218) looks for `SetBody` with a `Model.Union`:
```kotlin
val setBody = route.body?.defaultOrNull() as? Route.Body.SetBody ?: return@any false
```

Since `OverloadedBody` is not `SetBody`, this check naturally returns `false` for overloaded bodies. **Verify this is correct** — if `OverloadedBody` extends or is recognized by `defaultOrNull()`, the cast to `SetBody` will fail and correctly skip it.

However, if the `OverloadedBody` is the **only** reason serialization utils were generated, we should confirm the `AttemptDeserialize.kt` file is no longer emitted for the test spec.

### 4. `Route.nested` set

The `Bodies?.nested()` function in `Route.kt` (line ~177) collects inline models for code generation. Add `OverloadedBody`:

```kotlin
is Body.OverloadedBody -> body.cases.mapNotNull { it.model.nestedOrNull() }
```

This ensures inline objects/enums from union cases are still discovered and generated.

## Existing tests to update

### `inline-oneof-request-body-nested-path`

**Spec**: `POST /user/codespaces` with `required: true`, 2 oneOf cases (RepositoryIdAndRef object, PullRequest wrapper object).

**Current golden output**: Sealed `Body` interface with `RepositoryIdAndRef` and `PullRequest` cases, custom `Serializer`, `AttemptDeserialize.kt`.

**New expected output**: Two invoke overloads, no sealed interface, no `AttemptDeserialize.kt`:
```kotlin
public interface Post {
    public suspend operator fun invoke(body: RepositoryIdAndRef): Response
    public suspend operator fun invoke(body: PullRequest): Response

    @Serializable
    public data class RepositoryIdAndRef(...)

    @Serializable
    public data class PullRequest(...)  // or value class wrapping inner object

    public data class Response(public val value: String)
}
```

**Golden files to update**: `User.kt` (main change), `AttemptDeserialize.kt` (should be deleted/not generated).

### `inline-oneof-request-body-collection-inline-items`

**Spec**: `PUT /repos/{owner}/{repo}/issues/{issue_number}/labels` with `required: false`, 5 oneOf cases.

**Current golden output**: Does not exist yet (test was added but golden files not generated).

**New expected output**: 5 invoke overloads (body required in each) + 1 no-body overload. Inline types for the object/array cases:
```kotlin
public interface Put {
    // 5 case overloads + 1 no-body
    public suspend operator fun invoke(body: LabelsStrings): Response
    public suspend operator fun invoke(body: List<String>): Response
    public suspend operator fun invoke(body: LabelsNames): Response
    public suspend operator fun invoke(body: List<Name>): Response
    public suspend operator fun invoke(body: String): Response
    public suspend operator fun invoke(): Response  // no-body (optional)

    // Inline types
    @Serializable public data class LabelsStrings(...)
    @Serializable public data class Name(...)
    @Serializable public data class LabelsNames(...)

    public data class Response(public val value: List<...>)
}
```

**Golden files to create**: New directory `client/inline-oneof-request-body-collection-inline-items/`.

## Test plan

1. Update golden files for `inline-oneof-request-body-nested-path` (delete `AttemptDeserialize.kt`)
2. Generate golden files for `inline-oneof-request-body-collection-inline-items`
3. Add a test for `$ref` body union to verify it's NOT flattened (keeps sealed interface)
4. Run `./gradlew :renderer:jvmTest` — all tests pass
5. Run `./gradlew :typed:allTests` — no regressions

## Acceptance criteria

- [ ] Optional bodies generate N overloads + no-body overload
- [ ] `$ref` union bodies are NOT flattened (sealed interface preserved)
- [ ] `AttemptDeserialize` not generated when only overloaded bodies use non-discriminated unions
- [ ] `Route.nested` correctly includes inline models from `OverloadedBody` cases
- [ ] `inline-oneof-request-body-nested-path` golden files updated (overloads, no sealed interface)
- [ ] `inline-oneof-request-body-collection-inline-items` golden files created (5 overloads + no-body)
- [ ] All existing tests still pass
- [ ] `./gradlew :renderer:jvmTest` passes
- [ ] `./gradlew :typed:allTests` passes
