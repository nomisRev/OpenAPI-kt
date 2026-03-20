# ISSUE: `SetBody` / `OverloadedBody` Always Emit `ContentType.Application.Json`

## Problem

`addBodyCode()` in `ImplRenderer.kt` hardcoded `ContentType.Application.Json` for both `Route.Body.SetBody` and `Route.Body.OverloadedBody` regardless of the actual content type stored in the body model.

```kotlin
// Before fix — always application/json regardless of spec
code.addStatement("%M(%T.Application.Json)", ContentTypeFunMember, ContentTypeType)
```

`Route.Body.SetBody` and `Route.Body.OverloadedBody` both carry a `contentType: ContentType` field populated directly from the OpenAPI spec (`ContentType.parse(contentType)`), so the correct content type is already available at render time.

## Why This Matters

Specs that declare non-JSON request bodies (e.g. `application/octet-stream`, `text/plain`, `application/xml`) generate a client that sends the wrong `Content-Type` header. Servers doing strict content-type negotiation will reject these requests.

## Current Behavior (before fix)

Every `SetBody` / `OverloadedBody` operation emits:

```kotlin
contentType(ContentType.Application.Json)
setBody(body)
```

regardless of what the spec declares.

## Desired Outcome

The emitted content type matches the spec. Well-known Ktor named constants are used where possible so the generated code remains idiomatic; unknown types fall back to the `ContentType(contentType, subtype)` constructor.

```kotlin
// e.g. application/octet-stream
contentType(ContentType.Application.OctetStream)
setBody(body)

// e.g. application/vnd.custom+json (unknown named constant)
contentType(ContentType("application", "vnd.custom+json"))
setBody(body)
```

## Fix Applied

Added a private extension `ContentType.toContentTypeCodeBlock(): CodeBlock` in `ImplRenderer.kt` that maps well-known Ktor constants by value equality and falls back to the constructor form. Both `SetBody` and `OverloadedBody` branches now call this helper instead of hardcoding `.Application.Json`.

### Named constants mapped

`Application.Json`, `Application.OctetStream`, `Application.Xml`, `Application.FormUrlEncoded`,
`Application.Pdf`, `Application.Zip`, `Text.Plain`, `Text.Html`, `Text.Xml`, `Text.CSS`,
`Text.JavaScript`, `Image.PNG`, `Image.JPEG`, `Image.GIF`, `Image.SVG`.

## Affected Files

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt`

## Acceptance Criteria

1. A spec with an `application/octet-stream` request body generates `contentType(ContentType.Application.OctetStream)`.
2. A spec with an `application/json` body continues to generate `contentType(ContentType.Application.Json)`.
3. An unrecognised content type generates `contentType(ContentType("type", "subtype"))`.
4. `./gradlew :renderer:jvmTest` passes.
