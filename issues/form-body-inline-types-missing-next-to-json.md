# A form body next to a JSON body references `Post.Body.<Enum>` types that are never generated

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 4 of 1,547 documents (clickmeter.com, microsoft.com:cognitiveservices-Training, remove.bg, smart-me.com). clickmeter also has 2 `conflicting declarations`; all four have other errors from other reports (empty enum name, `url` shadowing, response owner, renamed path class).
**Source spec valid:** yes — several media types for one request body, each with the same `$ref` schema (OAS 3.0.3 §4.7.13 Request Body Object, `content` map).

## Symptom

```
Removebg.kt:58:22: error: unresolved reference 'Body'.              channels: Post.Body.Channels? = null,
Removebg.kt:86:41: error: unresolved reference 'value' on receiver of type 'Any'.   append("channels", channels.value)
```

## Minimal reproducer

`/private/tmp/claude-501/-Users-simonvergauwen-Developer-OpenAPI-kt/a0df68b9-ccc6-4d18-981e-753c6b12368d/scratchpad/repro/form-body-ref-inline-types.json`

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},
 "paths":{"/removebg":{"post":{"requestBody":{"required":true,"content":{
   "application/x-www-form-urlencoded":{"schema":{"$ref":"#/components/schemas/RemoveBg"}},
   "application/json":{"schema":{"$ref":"#/components/schemas/RemoveBg"}}}},
   "responses":{"204":{"description":"ok"}}}}},
 "components":{"schemas":{"RemoveBg":{"type":"object","properties":{
   "channels":{"type":"string","enum":["rgba","alpha"]},"size":{"type":"string"}}}}}}
```

Observed:

```kotlin
public class Post internal constructor(private val client: HttpClient) {
  public suspend operator fun invoke(channels: Post.Body.Channels? = null, size: String? = null) { ... append("channels", channels.value) ... }
  public suspend operator fun invoke(body: RemoveBg) { ... }
  // no `object Body`
}
Removebg.kt:22:55: error: unresolved reference 'Body'.
Removebg.kt:27:41: error: unresolved reference 'value' on receiver of type 'Any'.
```

With only the form media type the same spec compiles: `public object Body { enum class Channels }` is emitted in `Post`.

## What the spec says

Each entry of `content` is an independent representation of the body; the form entry must be callable with the fields of `RemoveBg`.

## Root cause

Traced; two halves.

1. `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/RequestBody.kt:108` (`formUrlEncoded`): the form schema is always converted under `NamingContext.path(segments, method).nest(RouteBody)`, also when it is a `$ref`. So `RemoveBg.channels` is a second enum named `<Path>.Post.Body.Channels`, not the component's `RemoveBg.Channels`. The multipart twin does it the other way: `formDataToBody` (`RequestBody.kt:82-86`) names a `$ref` schema by the reference (remove.bg's multipart overload correctly says `RemoveBgMultipart.Channels`).
2. `renderer/.../ClientRendererRequestBody.kt:511-519` (`inlineBodyTypeSpec`): the `Body` namespace object for form/multipart nested types is emitted only when the form body is `defaultOrNull()`. `Route.Bodies.defaultOrNull` (`typed/.../routes/Route.kt:162-163`) prefers JSON/XML/octet-stream/text, so next to a JSON body nothing emits `Body`, while the form overload still references it (`ClientRendererOperations.kt:816`, `formData.type.toTypeName(config)`).

## Correct final API

One enum per schema, shared by both overloads:

```kotlin
public suspend operator fun invoke(channels: RemoveBg.Channels? = null, size: String? = null)   // form
public suspend operator fun invoke(body: RemoveBg)                                             // JSON
```

Wire: the form overload sends `channels=rgba&size=...` as `application/x-www-form-urlencoded`; JSON unchanged. For an inline (non-`$ref`) form schema next to another body, the nested types stay under the operation, in a namespace that does not collide with the JSON body's public `Body` class (cbe28917).

## Fix slice

- typed: in `formUrlEncoded`, name a `$ref` schema by its reference, as `formDataToBody` does. This fixes all 4 census documents (all are `$ref` schemas shared between JSON and form).
- renderer: emit the nested types of every form/multipart body of the operation, not just the default one. Open decision: where they live when the JSON body is an inline `Body` class (nest them in that class if structurally equal, or a `FormBody` namespace).
- Check `Read`/`Write` context suffixes when switching to the reference name (`SchemaContext.Write`).

## Proof

- Failing test first: golden `ClientSpec` case of the reproducer (JSON + form, same `$ref`, inline enum property), asserting `RemoveBg.Channels` in the form overload; a second case with inline schemas.
- MockEngine: form overload sends `channels=rgba`.
- Integration tests: `openai.yaml` has 3 operations with JSON + form bodies, `github.json` has form/multipart mentions; none has an inline-typed field in a non-default form body (the integration projects compile), so they do not cover this.

## Affected documents

clickmeter.com [23d8e065f3aa6785b5e0], microsoft.com:cognitiveservices-Training [39cdf9cd72fdf1d39084], remove.bg [4d455157dea43d5708dc], smart-me.com [e3426242c60b04dcddba]. Total 4 (every `unresolved reference 'Body'` on a parameter typed `<Method>.Body.<X>`).
