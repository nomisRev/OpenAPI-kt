# A binary body and a multipart body with one binary field give two `invoke(ByteArray)` overloads

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 1 of 1,547 documents (sakari.io); no excluded errors
**Source spec valid:** yes — two media types for one request body (OAS 3.0.3 §4.7.13)

## Symptom

```
V1.kt:830:33: error: platform declaration clash: The following declarations have the same JVM signature (invoke([BLkotlin/coroutines/Continuation;)Ljava/lang/Object;):
    public suspend operator fun invoke(body: ByteArray): Response      // application/octet-stream
    public suspend operator fun invoke(media: ByteArray? = null): Response   // multipart/form-data
```

## Minimal reproducer

`/private/tmp/claude-501/-Users-simonvergauwen-Developer-OpenAPI-kt/a0df68b9-ccc6-4d18-981e-753c6b12368d/scratchpad/repro/binary-and-multipart-body-jvm-clash.json`

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},"paths":{"/sharefile":{"post":{
  "requestBody":{"required":true,"content":{
    "application/octet-stream":{"schema":{"type":"string","format":"binary"}},
    "multipart/form-data":{"schema":{"type":"object","properties":{"media":{"type":"string","format":"binary"}}}}}},
  "responses":{"204":{"description":"ok"}}}}}}
```

Observed:

```
Sharefile.kt:21:29: error: platform declaration clash: ... (invoke([BLkotlin/coroutines/Continuation;)Ljava/lang/Object;)
21:    public suspend operator fun invoke(body: ByteArray) {
29:    public suspend operator fun invoke(media: ByteArray? = null) {
```

With `required` left out both are `ByteArray? = null` and the error is `conflicting overloads`.

## What the spec says

Both representations are valid ways to send the file; the client must offer both and let the caller choose.

## Root cause

Traced. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ContentTypeStrategy.kt:204-222` (`detectSignatureClashes`) groups bodies by `toKotlinSignature` (`:226-249`) and only names methods per content type when two strings are equal. The strings are not what Kotlin or the JVM compare: a `SetBody` is its type name (`kotlin.ByteArray`), a multipart/form body is its sorted `toKotlinSignaturePart`s, which include the field name and nullability. `ByteArray` vs `media:ByteArray?` look different, so `SeparateOverloads` is chosen, but overload resolution ignores parameter names and the JVM erases nullability. Any single-field form or multipart body whose field type equals the other body's type clashes the same way (`String` text body + one string form field).

## Correct final API

The existing clash strategy, one method per content type:

```kotlin
public suspend fun octetStream(body: ByteArray): Response
public suspend fun formData(media: ByteArray? = null): Response
```

Wire: the first sends the bytes as `application/octet-stream`, the second a `multipart/form-data` body with part `media`.

## Fix slice

- Renderer: compare JVM-level signatures in `toKotlinSignature`: positional parameter types only, nullability and names dropped (and operation parameters are common to all variants). Then `ClashingWithEnum` handles it.

## Proof

- Failing test first: golden `ClientSpec` case of the reproducer (required and optional), compiled; MockEngine test of both content types.
- Integration tests: `openai.yaml` has one `application/octet-stream` mention and compiles; no such pair in `github.json` / `youtrack.json` (script). Not covered.

## Affected documents

sakari.io [a59a5fb8faf3214473d7]. Total 1. (1password.com:events also reports `conflicting overloads`, but on its client constructors: a server-name problem owned by another report.)
