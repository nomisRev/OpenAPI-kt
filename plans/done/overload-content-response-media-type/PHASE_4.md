# Phase 4: Request Body Signature Clash Detection

## Goal

When multiple request body content types produce the same Kotlin method signature, generate a `RequestType` enum to disambiguate. Fix compilation errors from clashing overloads.

## Deliverables

1. Modified `ClientRendererOperations.kt` — clash detection in variant loop
2. Modified `ClientRendererRequestBody.kt` — `RequestType` enum TypeSpec generation
3. Tests for clash detection and enum generation

## Tasks

### 4.1 Integrate Clash Detection into Variant Loop

**File:** `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererOperations.kt`

**Current code (line 147-162):**
```kotlin
val bodyVariants = context.route.body?.variants().orEmpty()
if (bodyVariants.size > 1) {
    bodyVariants.forEach { variant ->
        addFunction(
            context.route.toInvokeFunSpec(
                // ...
                selectedBody = variant.body,
            )
        )
    }
    return
}
```

**Change:** Before generating overloads, run `detectSignatureClashes()` from Phase 1:

```kotlin
val bodyVariants = context.route.body?.variants().orEmpty()
if (bodyVariants.size > 1) {
    val strategy = context.route.body!!.detectSignatureClashes(context.config)
    when (strategy) {
        is RequestBodyStrategy.SeparateOverloads -> {
            // Current behavior — no clash, separate invoke() per variant
            strategy.variants.forEach { variant ->
                addFunction(context.route.toInvokeFunSpec(..., selectedBody = variant.body))
            }
        }
        is RequestBodyStrategy.ClashingWithEnum -> {
            // Generate RequestType enum + single invoke with enum param
            addType(buildRequestTypeEnum(strategy.clashing))
            addFunction(context.route.toInvokeFunSpecWithRequestType(...))
            // Also generate overloads for non-clashing variants
            strategy.unique.forEach { variant ->
                addFunction(context.route.toInvokeFunSpec(..., selectedBody = variant.body))
            }
        }
    }
    return
}
```

### 4.2 Generate RequestType Enum

**File:** `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererRequestBody.kt`

Add function to generate:
```kotlin
enum class RequestType(val contentType: ContentType) {
    TextPlain(ContentType.Text.Plain),
    TextXMarkdown(ContentType("text", "x-markdown"))
}
```

**Implementation:**
```kotlin
fun buildRequestTypeEnum(clashingVariants: List<Route.Bodies.Variant>): TypeSpec {
    val builder = TypeSpec.enumBuilder("RequestType")
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("contentType", ContentTypeMember)
                .build()
        )
        .addProperty(
            PropertySpec.builder("contentType", ContentTypeMember)
                .initializer("contentType")
                .build()
        )

    for (variant in clashingVariants) {
        val enumName = contentTypeToIdentifier(variant.contentType)
        val ctCodeBlock = variant.contentType.toContentTypeCodeBlock()
        builder.addEnumConstant(enumName, TypeSpec.anonymousClassBuilder()
            .addSuperclassConstructorParameter("%L", ctCodeBlock)
            .build()
        )
    }

    return builder.build()
}
```

### 4.3 Generate invoke() with RequestType Parameter

Create `toInvokeFunSpecWithRequestType()` — similar to `toInvokeFunSpec()` but:
1. Adds `requestType: RequestType` as a required parameter (no default)
2. In the implementation body, uses `contentType(requestType.contentType)` instead of hardcoded content type
3. Uses the body type from any of the clashing variants (they're all the same type)

**Parameter ordering:** `requestType` goes after the body parameter:
```kotlin
suspend operator fun invoke(body: String, requestType: RequestType): Response
```

### 4.4 Compose with Multi-CT Response Methods

When an operation has BOTH request body clashes AND multiple response content types:
- Each response method (`json()`, `xml()`) accepts the `requestType` parameter
- The `RequestType` enum is independent of the response strategy

```kotlin
suspend fun json(body: String, requestType: RequestType): JsonResponse = ...
suspend fun xml(body: String, requestType: RequestType): XmlResponse = ...
```

## Key Code Locations

- Variant loop: `ClientRendererOperations.kt:146-162`
- `toContentTypeCodeBlock()`: `ImplRenderer.kt:434-459`
- `Route.Bodies.variants()`: `typed/.../routes/Route.kt:104-106`
- `Route.Body.SetBody.type`: `typed/.../routes/Route.kt:141-146`
- Existing `@JvmName` pattern: `ClientRendererRequestBody.kt:404-428`

## Real-World Cases

From spec analysis:
- **GitHub** `/markdown/raw` (POST): `text/plain` + `text/x-markdown`, both `String` → RequestType enum
- **Cloudflare** `/accounts/.../workers/{worker_id}` (PATCH): `application/json` + `application/merge-patch+json` → potentially same schema

## Acceptance Criteria

- [ ] Operations with different body types → separate overloads (current behavior preserved)
- [ ] Operations with same body type across CTs → `RequestType` enum generated
- [ ] `RequestType` parameter is required (no default value)
- [ ] Mixed cases (some clash, some don't) → enum for clashing + overloads for unique
- [ ] Generated code compiles without signature clashes
- [ ] `./gradlew :renderer:jvmTest` passes

## Verification

```bash
./gradlew :renderer:jvmTest
```

Write a test with `text/plain` + `text/x-markdown` both mapping to `String`. Verify `RequestType` enum is generated and `invoke()` has the enum parameter.
