# Phase 2: Render interface overloads in `ClientRenderer.kt`

## Goal

When `Route.Body.OverloadedBody` is the default body, generate **multiple `invoke` overloads** (one per union case) in the client interface instead of a single `invoke(body: Body)` with a sealed interface.

## Motivation

The typed layer (Phase 1) now produces `OverloadedBody` with individual cases. The renderer needs to translate each case into a separate function signature. This eliminates the sealed `Body` interface and its custom serializer, giving callers direct, type-safe overloads.

## Files to modify

### `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRenderer.kt`

#### 1. `Route.toOperationTypeSpec()` (line ~291)

Currently generates a single `invoke` via `toInvokeFunSpec()`. When `OverloadedBody` is detected:

- **Do NOT** generate `inlineBodyTypeSpec` (no sealed `Body` interface)
- **Do** emit inline nested types from each union case (objects, enums) as nested types in the operation interface
- **Do** generate N `invoke` functions, one per case

```kotlin
// Pseudocode for the new branch
val overloadedBody = body?.defaultOrNull() as? Route.Body.OverloadedBody
if (overloadedBody != null) {
    // Emit inline types from each case
    for (case in overloadedBody.cases) {
        case.model.toInlineOperationTypeSpecOrNull(config, methodClassName, /* case name */)
            ?.let(builder::addType)
    }
    // Emit N invoke overloads
    val emittedTypes = mutableSetOf<TypeName>()
    for (case in overloadedBody.cases) {
        val caseTypeName = case.model.toTypeName(config)
        if (!emittedTypes.add(caseTypeName)) continue  // deduplicate
        builder.addFunction(toInvokeFunSpecForCase(case, ...))
    }
} else {
    // Existing single-invoke logic
}
```

#### 2. New helper: `toInvokeFunSpecForCase()`

Generates a single overload for one union case:

```kotlin
private fun Route.toInvokeFunSpecForCase(
    caseModel: Model,
    config: RenderConfig,
    interfaceClassName: ClassName,
    methodClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
): FunSpec {
    val builder = FunSpec.builder("invoke")
        .addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND, KModifier.OPERATOR)

    // Same parameter ordering as existing: required params, body, optional params
    // ... add non-path params (same as current logic) ...

    // Body parameter — always named "body", type is the case's resolved type
    val bodyTypeName = caseModel.toTypeName(config)
    builder.addParameter(ParameterSpec.builder("body", bodyTypeName).build())

    // ... add optional params ...

    builder.returns(invokeReturnType(methodClassName))
    return builder.build()
}
```

#### 3. `Route.Bodies.inlineBodyTypeSpec()` (line ~648)

When body is `OverloadedBody`, return `null` — no sealed `Body` interface should be generated.

#### 4. `hasInlineNonDiscriminatedBodyUnion()` (line ~218)

Update to return `false` when the body is `OverloadedBody` (since we're flattening, we don't need `AttemptDeserialize` for the body). The check currently looks for `SetBody` with a `Model.Union` — `OverloadedBody` is a different variant, so this may already work correctly, but verify.

#### 5. Inline nested type emission

Each union case's model may itself contain inline types (e.g., an object case has an inline enum property). These need to be emitted as nested types in the operation interface. Use the existing `toInlineOperationTypeSpecOrNull()` for each case model.

For naming: union cases already have their own naming context from the typed layer. Use those names directly.

## Type deduplication

If two union cases resolve to the same Kotlin type (e.g., both are `List<String>`), emit only one overload. Track emitted `TypeName`s in a `MutableSet` and skip duplicates.

## Test approach

**Step 1**: Create golden files for the `overloaded-body-simple` test from Phase 1. Expected output:

```kotlin
public interface Pets {
  public val post: Post

  public interface Post {
    // Inline object type from case 1
    @Serializable
    public data class NameAndTag(
      public val name: String,
      public val tag: String? = null,
    )

    public suspend operator fun invoke(body: NameAndTag): Response
    public suspend operator fun invoke(body: String): Response

    public data class Response(public val value: String)
  }
}
```

**Step 2**: Add a more complex test — the GitHub labels example (5 cases with nested inline objects and collections). Add golden files.

**Step 3**: Run `./gradlew :renderer:jvmTest` — both tests should pass.

## Acceptance criteria

- [ ] `OverloadedBody` cases produce N `invoke` overloads in the interface
- [ ] No sealed `Body` interface is generated for overloaded bodies
- [ ] Inline nested types from cases are emitted in the operation interface
- [ ] Duplicate Kotlin types are deduplicated (only one overload per unique type)
- [ ] `hasInlineNonDiscriminatedBodyUnion()` returns `false` for overloaded bodies
- [ ] Golden files for simple 2-case test pass
- [ ] Golden files for complex 5-case (GitHub labels) test pass
- [ ] `./gradlew :renderer:jvmTest` passes
