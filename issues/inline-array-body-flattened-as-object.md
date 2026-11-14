# A JSON body that is an inline array of inline objects is flattened as if it were one object

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile (and wrong output if it compiled)
**Impact:** 5 of 1,547 documents (interactivebrokers.com, vtex.local:Pricing-API, vtex.local:Marketplace-APIs, tvmaze.com, meilisearch.com). tvmaze also has 2 excluded errors.
**Source spec valid:** yes — `type: array` with inline `items` is a plain Schema Object (OAS 3.0.3 §4.7.24). (interactivebrokers declares it on a GET, which 3.0.3 §4.7.10 calls undefined semantics, but the failure does not depend on the method.)

## Symptom

```
Scrobble.kt:58:11: error: cannot infer type for type parameter 'T'. Specify it explicitly.
Scrobble.kt:58:23: error: unresolved reference 'Body'.
    setBody(Post.Body(episodeId = episodeId, markedAt = markedAt, type = type))
```

## Minimal reproducer

`/private/tmp/claude-501/-Users-simonvergauwen-Developer-OpenAPI-kt/a0df68b9-ccc6-4d18-981e-753c6b12368d/scratchpad/repro/inline-array-body-flattened.json`

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},
 "paths":{"/scrobble":{"post":{"operationId":"scrobble",
   "requestBody":{"required":true,"content":{"application/json":{"schema":{"type":"array","items":{"type":"object","properties":{
     "episode_id":{"type":"integer"},"marked_at":{"type":"integer"}}}}}}},
   "responses":{"204":{"description":"ok"}}}}}}
```

Observed (the only `invoke`; no `Body` class, no list overload):

```kotlin
public suspend operator fun invoke(episodeId: Long? = null, markedAt: Long? = null) {
  val response = client.post("scrobble") {
    contentType(ContentType.Application.Json)
    setBody(Post.Body(episodeId = episodeId, markedAt = markedAt))
  }
}
Scrobble.kt:22:9: error: cannot infer type for type parameter 'T'. Specify it explicitly.
Scrobble.kt:22:22: error: unresolved reference 'Body'.
```

## What the spec says

The body is a JSON array: `[{"episode_id":1,"marked_at":2}, ...]`. A single flattened object can never be the right request.

## Root cause

Traced. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererRequestBody.kt:200`:

```kotlin
val model = body?.type?.nestedOrNull() as? Model.Object
```

`nestedOrNull()` (`typed/.../transformers/Nested.kt:6-8`) looks through `Model.Collection` and `Model.Dictionary` to the element type. So for `List<InlineObject>` (or `Map<String, InlineObject>`) `flattenedBodyRendering` receives the element object, decides the body is a flattenable inline object and emits one parameter per item property plus `setBody(Body(...))`. The `Body` class is not emitted because the body type is a collection (`inlineBodyTypeSpec`, `ClientRendererRequestBody.kt:520-530`, renders `bodyModelOrNull()` itself). The same expression is at `:269` (`exceedsFlattenedBodyOverloadLimit`).

## Correct final API

```kotlin
public suspend operator fun invoke(body: List<Post.Body>)      // element type nested in the operation
@Serializable public data class Body(@SerialName("episode_id") val episodeId: Long? = null, @SerialName("marked_at") val markedAt: Long? = null)
```

Wire: `Content-Type: application/json`, body `[{"episode_id":1,"marked_at":2}]`. Open naming question: `Body` vs `Item`/`BodyItem` for the element class.

## Fix slice

- Renderer: flatten only when the body model itself is an inline `Model.Object` (`body.type as? Model.Object`, not through `nestedOrNull()`), at `:200` and `:269`.
- Make sure the non-flattened path emits the inline element type and types the parameter `List<...>` (the response side already has collection-item handling in `OperationInlineModelScope`; the request side has `collectionItemCandidateOrNull` only for `OverloadedBody`).

## Proof

- Failing test first: golden `ClientSpec` case of the reproducer and a `additionalProperties: {inline object}` twin; MockEngine test asserting the request body text is a JSON array.
- Integration tests: neither `github.json` nor `youtrack.json` has an operation whose only JSON body is an inline array of inline objects (checked by script; openai not checked, it compiles). Not covered.

## Affected documents

interactivebrokers.com [30288a85c1db53cb1aa5], vtex.local:Pricing-API [9b0acee859cd5b0b77e6], vtex.local:Marketplace-APIs [c397c11c6a9924ca55a0], tvmaze.com [f318db6f900215665dcf], meilisearch.com [f4ad07296f44e46a790c]. Total 5 (every `unresolved reference 'Body'` on a `setBody(<Method>.Body(...))` line).
