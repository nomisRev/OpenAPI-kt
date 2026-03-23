# Phase 1: Infrastructure — ContentType Naming & Strategy Detection

## Goal

Create the foundational utilities that all later phases depend on: content type to Kotlin identifier conversion, response strategy detection, and error case classification.

## Deliverables

1. **New file: `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ContentTypeStrategy.kt`**
2. Unit tests for naming and strategy detection

## Tasks

### 1.1 Content Type → Identifier Conversion

Create `contentTypeToIdentifier(ct: ContentType): String` that converts a Ktor `ContentType` to a PascalCase Kotlin identifier.

**Algorithm:**
1. For well-known `application/*` types, strip the `application/` prefix:
   - `application/json` → `Json`
   - `application/xml` → `Xml`
   - `application/octet-stream` → `OctetStream`
2. For other types, include the primary type:
   - `text/plain` → `TextPlain`
   - `image/png` → `ImagePng`
3. Handle `+suffix`: the suffix replaces `application/` stripping:
   - `application/sarif+json` → `SarifJson`
   - `application/vnd.github.v3.star+json` → `VndGithubV3StarJson`
   - `application/vnd.github.object` → `VndGithubObject`
4. Special characters (`.`, `-`, `+`) → capitalize next letter, drop the separator
5. Strip parameters (e.g., `; charset=utf-8`)

Also create `contentTypeToMethodName(ct: ContentType): String` (camelCase variant).

**Collision handling:** accept a `Set<String>` of existing names, append numeric suffix if collision.

### 1.2 Response Strategy Detection

Create `Route.Returns.contentTypeStrategy(): ContentTypeStrategy`:

```kotlin
sealed interface ContentTypeStrategy {
    /** Single content type across all 2xx statuses — use current invoke() behavior */
    data object SingleContentType : ContentTypeStrategy

    /** Multiple success content types — generate separate named methods */
    data class SeparateMethods(
        val successContentTypes: List<ContentType>
    ) : ContentTypeStrategy
}
```

**Logic:**
```kotlin
fun Route.Returns.contentTypeStrategy(): ContentTypeStrategy {
    val successContentTypes = responses
        .filterKeys { it.value in 200..299 }
        .flatMap { (_, returnType) -> returnType.types.keys }
        .distinct()
    return if (successContentTypes.size <= 1) ContentTypeStrategy.SingleContentType
    else ContentTypeStrategy.SeparateMethods(successContentTypes)
}
```

### 1.3 Error Case Classification

Create `classifyErrorStatus(statusCode: HttpStatusCode, returnType: Route.ReturnType)`:

```kotlin
sealed interface ErrorCaseStrategy {
    /** No body for this status (204, 304) */
    data object NoContent : ErrorCaseStrategy

    /** Single content type → single error case name like "BadRequest" */
    data class SingleContentType(
        val contentType: ContentType,
        val model: Model
    ) : ErrorCaseStrategy

    /** Multiple content types → one case per CT, named like "JsonBadRequest", "ScimJsonBadRequest" */
    data class MultipleContentTypes(
        val variants: List<Pair<ContentType, Model>>
    ) : ErrorCaseStrategy
}
```

### 1.4 Request Body Clash Detection

Create `Route.Bodies.detectSignatureClashes(config: RenderConfig): RequestBodyStrategy`:

```kotlin
sealed interface RequestBodyStrategy {
    data class Single(val variant: Route.Bodies.Variant) : RequestBodyStrategy
    data class SeparateOverloads(val variants: List<Route.Bodies.Variant>) : RequestBodyStrategy
    data class ClashingWithEnum(
        val clashing: List<Route.Bodies.Variant>,  // Same signature → RequestType enum
        val unique: List<Route.Bodies.Variant>       // Different signature → separate overloads
    ) : RequestBodyStrategy
}
```

**Signature comparison** — `Route.Body.toKotlinSignature(config: RenderConfig): String`:
- `SetBody` / `OverloadedBody`: `type.toTypeName(config).toString()`
- `FormUrlEncoded`: sorted parameter types joined
- `Multipart.Value`: sorted parameter types joined
- `Multipart.Ref`: ref type name

## Key Code Locations

- `Route.Returns` definition: `typed/.../routes/Route.kt:202-209`
- `Route.Bodies` and `variants()`: `typed/.../routes/Route.kt:93-106`
- `Route.Body` sealed hierarchy: `typed/.../routes/Route.kt:133-192`
- `HttpStatusCode.toCaseName()`: `renderer/.../ClientRendererResponses.kt:28-31`
- `preferredModel()`: `renderer/.../ClientRendererResponses.kt:17-21`

## Acceptance Criteria

- [ ] `contentTypeToIdentifier` converts all examples from the naming table correctly
- [ ] `contentTypeStrategy()` returns `SingleContentType` for operations with 1 success CT
- [ ] `contentTypeStrategy()` returns `SeparateMethods` for operations with 2+ success CTs
- [ ] Error classification handles: no-content, single CT, multiple CTs
- [ ] Request body clash detection identifies same-signature variants
- [ ] Unit tests pass: `./gradlew :renderer:jvmTest`

## Verification

```bash
./gradlew :renderer:jvmTest
```

No existing behavior changes — this phase only adds new utilities.
