# With one method per response content type, shared error cases reference their nested types under `JsonResponse`

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 3 of 1,547 documents (digitallocker.gov.in:authpartner, nexmo.com:account, remove.bg). None has excluded errors.
**Source spec valid:** yes — several media types on a success response, inline or `oneOf` schemas on error responses (OAS 3.0.3 §4.7.16 Response Object).

## Symptom

```
Oauth2.kt:620:32: error: annotation argument must be a compile-time constant.     @Serializable(with = PdfResponse.InternalServerError.Serializer::class)
Oauth2.kt:620:44: error: unresolved reference 'InternalServerError'.
Oauth2.kt:647:20: error: object '...InternalServerError.Serializer' is not abstract and does not implement abstract member
... 'serialize' overrides nothing / cannot infer type for type parameter 'A'
Removebg.kt:428:31: error: serializer was not found for type '??? (Unresolved qualified name: JsonResponse.BadRequest.Errors)'
```

## Minimal reproducer

`/private/tmp/claude-501/-Users-simonvergauwen-Developer-OpenAPI-kt/a0df68b9-ccc6-4d18-981e-753c6b12368d/scratchpad/repro/separate-method-response-nested-union.json` and `...-nested.json`. The second:

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},"paths":{"/file":{"get":{"responses":{
  "200":{"description":"ok","content":{
    "application/json":{"schema":{"type":"object","properties":{"id":{"type":"string"}}}},
    "image/png":{"schema":{"type":"string","format":"binary"}}}},
  "400":{"description":"err","content":{"application/json":{"schema":{"type":"object","properties":{
    "errors":{"type":"array","items":{"type":"object","properties":{"code":{"type":"string"}}}}}}}}}}}}}}
```

Observed:

```kotlin
public class Get {
  public suspend fun json(): JsonResponse
  public suspend fun imagePng(): ImagePngResponse
  public sealed interface JsonResponse { data class Ok(...) }
  public sealed interface ImagePngResponse { data class Ok(...) }
  public data class BadRequest(                                  // declared in Get
    public val errors: List<JsonResponse.BadRequest.Errors>? = null,   // referenced in Get.JsonResponse
  ) : JsonResponse, ImagePngResponse { public data class Errors(...) }
}
File.kt:67:31: error: serializer was not found for type '??? (Unresolved qualified name: JsonResponse.BadRequest.Errors)'
File.kt:67:44: error: unresolved reference 'BadRequest'.
```

The `union` variant (500 is `oneOf: [A, B]`) gives `@Serializable(with = JsonResponse.InternalServerError.Serializer::class)` on `Get.InternalServerError`: `annotation argument must be a compile-time constant` and the serializer cascade.

## What the spec says

Nothing unusual; the shape is ours. A case shared by all per-content-type response interfaces is declared once on the operation class.

## Root cause

Traced. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererResponses.kt:707-737` (`buildSharedResponseCaseTypeSpec`): for an inline model it calls

```kotlin
buildInlineResponseCaseTypeSpec(model, config, responseClassName = firstSuperinterface as ClassName, caseName, ...)   // :723-729
```

`buildInlineResponseCaseTypeSpec` (`:520`, `ownerClassName = responseClassName` at `:530`, `classNameOverride = ownerClassName.nestedClass(nameOverride)` at `:563`) builds the case as `responseClassName.nestedClass(caseName)`, i.e. `Get.JsonResponse.BadRequest`, and every self-reference (nested property types, the union's `Serializer`, its `when` branches) uses that name. The caller then adds the spec to the operation class (`specs +=` at `:108-152`), where its real name is `Get.BadRequest`. The non-inline branch below (`:739`) correctly uses `methodClassName.nestedClass(caseName)`. Both `ErrorCaseStrategy.SingleContentType` and `MultipleContentTypes` (nexmo's `JsonUnauthorized`/`XmlUnauthorized`) go through this function.

## Correct final API

```kotlin
public data class BadRequest(public val errors: List<BadRequest.Errors>? = null) : JsonResponse, ImagePngResponse {
  @Serializable public data class Errors(public val code: String? = null)
}
@Serializable(with = InternalServerError.Serializer::class)
public sealed interface InternalServerError : JsonResponse, ImagePngResponse { ... }
```

Runtime: a 400 from either `json()` or `imagePng()` decodes into `Get.BadRequest`.

## Fix slice

- Renderer: `buildInlineResponseCaseTypeSpec` needs the owner class and the superinterface separately; pass `methodClassName` as owner and `superinterfaces` for `addSuperinterface`. One function, both strategies.

## Proof

- Failing tests first: two golden `ClientSpec` cases from the reproducers (inline object with a nested inline type; `oneOf` union), compiled; MockEngine test decoding a 400 through both methods.
- Integration tests: `github.json` has 3 operations with several success media types and 1 inline non-success schema among them; it compiles, so that one has no nested inline type or union. Not covered.

## Affected documents

digitallocker.gov.in:authpartner [34c0f611ffd66319216b], nexmo.com:account [ac021f5a6a590e834e7b], remove.bg [4d455157dea43d5708dc]. Total 3 (every unresolved reference written as `<X>Response.<Case>`).
