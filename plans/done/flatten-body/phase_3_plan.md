# Phase 3: Render implementation overloads in `ImplRenderer.kt`

## Goal

Generate Ktor implementation code for each body overload — multiple `override suspend operator fun invoke(body: CaseType)` methods in the anonymous implementation class.

## Motivation

Phase 2 generates the interface signatures. The implementation class must provide matching overrides. Each overload makes the same HTTP call with the same path/params, but sends the case-specific type as the request body.

## Files to modify

### `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt`

#### 1. `Route.toImplOperationPropertySpec()` (line ~330)

Currently creates an anonymous class with a single `invoke` function:
```kotlin
val anonymousImpl = TypeSpec.anonymousClassBuilder()
    .addSuperinterface(methodClassName)
    .addFunction(toImplInvokeFunSpec(...))
    .build()
```

When the body is `OverloadedBody`, switch to generating **multiple functions**:
```kotlin
val overloadedBody = body?.defaultOrNull() as? Route.Body.OverloadedBody
if (overloadedBody != null) {
    val anonymousImpl = TypeSpec.anonymousClassBuilder()
        .addSuperinterface(methodClassName)
    val emittedTypes = mutableSetOf<TypeName>()
    for (case in overloadedBody.cases) {
        val caseTypeName = case.model.toTypeName(config)
        if (!emittedTypes.add(caseTypeName)) continue
        anonymousImpl.addFunction(
            toImplInvokeFunSpecForCase(case.model, method, config, ...)
        )
    }
    anonymousImpl.build()
} else {
    // Existing single-function pattern
}
```

#### 2. New helper: `Route.toImplInvokeFunSpecForCase()`

Generates one override implementation for a specific case:

```kotlin
private fun Route.toImplInvokeFunSpecForCase(
    caseModel: Model,
    method: HttpMethod,
    config: RenderConfig,
    interfaceClassName: ClassName,
    methodClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
): FunSpec {
    val builder = FunSpec.builder("invoke")
        .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND, KModifier.OPERATOR)

    // Same non-path params as existing (no defaults — Kotlin disallows on override)
    // ... add required params, body param, optional params ...

    val bodyTypeName = caseModel.toTypeName(config)
    builder.addParameter(ParameterSpec.builder("body", bodyTypeName).build())

    builder.returns(invokeReturnType(methodClassName))

    // Build body: same HTTP call, same params, setBody(body) with case type
    builder.addCode(buildOperationBody(method, config, methodClassName))

    return builder.build()
}
```

#### 3. `addBodyCode()` (line ~573)

Currently handles `SetBody`, `FormUrlEncoded`, `Multipart`. Add a branch for `OverloadedBody`:

```kotlin
is Route.Body.OverloadedBody -> {
    // Each overload sends body directly (the parameter type IS the concrete case type)
    code.addStatement("%M(%T.Application.Json)", ContentTypeFunMember, ContentTypeType)
    code.addStatement("%M(body)", SetBodyMember)
}
```

This is actually the same as the existing `SetBody` required path. The key difference is that `body` in each overload is already the concrete type — no sealed interface wrapper.

#### 4. `Route.Bodies.usesNestedBodyType()` (line ~734)

When body is `OverloadedBody`, return `false` — we don't use a nested `Body` type wrapper.

#### 5. `Route.Bodies.toImplInvokeParameterSpecs()` (line ~689)

Add a branch for `OverloadedBody`. However, since each overload has its own case type, this method may need to be restructured. The approach depends on whether we generate N separate `FunSpec`s (each with its own body param) or try to reuse the existing single-function codepath.

**Recommended approach**: For overloaded bodies, don't use `toImplInvokeParameterSpecs()` at all — each overload builds its own parameter list directly in `toImplInvokeFunSpecForCase()`.

## Implementation pattern

The generated impl for a required overloaded body will look like:

```kotlin
override val post: Pets.Post = object : Pets.Post {
    override suspend operator fun invoke(body: Pets.Post.NameAndTag): Pets.Post.Response {
        val value: String = client.post("/pets") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
        return Pets.Post.Response(value)
    }

    override suspend operator fun invoke(body: String): Pets.Post.Response {
        val value: String = client.post("/pets") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
        return Pets.Post.Response(value)
    }
}
```

Each overload has identical HTTP logic — only the parameter type differs. The Ktor `setBody()` uses the parameter's static type for serialization, so each case is serialized correctly.

## Test approach

**Step 1**: Update golden files from Phase 2 to include the full implementation code (not just the interface).

**Step 2**: Verify that the generated impl compiles by checking:
- Parameter types match the interface signatures
- No default values on override parameters
- `setBody(body)` is called with the correct type

**Step 3**: Run `./gradlew :renderer:jvmTest` — all golden file tests pass.

## Acceptance criteria

- [ ] Anonymous impl class generates N `invoke` overrides for `OverloadedBody`
- [ ] Each override sends `setBody(body)` with `contentType(Application.Json)`
- [ ] `usesNestedBodyType()` returns `false` for `OverloadedBody`
- [ ] No `AttemptDeserialize` serialization utils generated for overloaded bodies
- [ ] Golden files include full impl code and pass
- [ ] `./gradlew :renderer:jvmTest` passes
