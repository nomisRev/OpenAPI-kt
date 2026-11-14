# A response media type without `schema` aborts the whole document

**Status:** fixed on branch openapi-3.2-streaming (typed + renderer, golden `client/media-type-without-schema`); census re-run pending
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** typed (responses); wrong output / silent drop (request bodies)
**Impact:** 243 of 1,547 documents fail on it (179 apisetu.gov.in, 64 from 55 other providers). 7 more contain the construct but fail earlier on something else (250 total). 6 documents have the same construct in a request body, where it is dropped silently.
**Source spec valid:** yes — `schema` is optional on the Media Type Object; 4 of the 243 documents additionally have invalid media type keys (see "Invalid neighbours").

## Symptom

`toApiTree` throws for the first response whose `content` entry has neither `schema` nor `itemSchema`, and no code is generated for the document:

```
java.lang.IllegalArgumentException: Response without MediaType(schema=null, example=pdf bytes, examples={}, ...) schema for GET /certificate (application/pdf). Response(description=...)
	at io.github.nomisrev.openapi.routes.ReturnKt.toReturnType(Return.kt:47)
	at io.github.nomisrev.openapi.routes.ReturnKt.resolveReturns(Return.kt:29)
```

Sub-groups of the 243 failing documents (counted from the specs, a document can be in more than one row):

| What the schema-less media type carries | Documents |
|---|---|
| `example` only | 193 |
| `examples` only | 44 |
| nothing at all (`"text/plain": {}`) | 10 |

| Media type of the schema-less entry | Documents |
|---|---|
| binary / other only (`application/pdf` 180, `image/png`, `image/gif`, `*/*`, `application/x-protobuf`, ...) | 183 |
| JSON only (`application/json`, `+json`) | 34 |
| text only (`text/plain`, `text/csv`, `text/html`, `application/xml`) | 11 |
| mixed JSON + text / binary | 10 |
| keys that are not media types at all (`token`, `name`, `data`, ...), alone or next to JSON | 5 |

The apisetu.gov.in documents all have the same shape: `"application/pdf": {"example": "Response body contains contents of the certificate in PDF format."}`.

## Minimal reproducer

Three specs, one per sub-group, in the scratchpad `repro/` directory:
`media-type-without-schema-pdf.json`, `media-type-without-schema-json-example.json`, `media-type-without-schema-empty.json`.

```json
{
  "openapi": "3.0.3",
  "info": { "title": "t", "version": "1" },
  "paths": {
    "/certificate": {
      "get": {
        "operationId": "certificate",
        "responses": {
          "200": {
            "description": "The certificate as a PDF",
            "content": { "application/pdf": { "example": "pdf bytes" } }
          }
        }
      }
    }
  }
}
```

Observed (`repro.sh`):

```
worker exit=1 phases: [{"phase":"parse","status":"pass"},{"phase":"typed","status":"fail","exception":"java.lang.IllegalArgumentException","message":"Response without MediaType(schema=null, example=pdf bytes, examples={}, encoding={}, itemSchema=null, itemEncoding=null, prefixEncoding=[], extensions={}) schema for GET /certificate (application/pdf). ...
	at io.github.nomisrev.openapi.routes.ReturnKt.toReturnType(Return.kt:47)
no sources emitted
```

The other two fail the same way, with `examples={one=Value(...)}` for `"application/json": {"examples": {"one": {"value": {"id": 1}}}}` and with every field empty for `"text/plain": {}`.

Request side (`media-type-without-schema-request.json`): an optional body `"application/pdf": {"example": "pdf bytes"}` and a required body with `"application/pdf": {}` next to `"application/json": {"schema": {"type": "string"}}`. This passes every phase with `diagnostics: []` and compiles, but the output is wrong:

```kotlin
// POST /upload: the declared PDF body is gone, the operation cannot send one
public suspend operator fun invoke() {
  val response = client.post("upload")

// POST /upload-required: only the JSON alternative exists
public suspend operator fun invoke(body: String) {
  val response = client.post("upload-required") {
    contentType(ContentType.Application.Json)
    setBody(JsonPrimitive(body))
```

## What the spec says

- OAS 3.0.3 §4.7.14 Media Type Object (https://spec.openapis.org/oas/v3.0.3.html#media-type-object): `schema`, `example`, `examples` and `encoding` are all optional fixed fields; none is REQUIRED. A Media Type Object of `{}` is valid.
- OAS 3.1.0 §4.8.14.3 Considerations for File Uploads (https://spec.openapis.org/oas/v3.1.0.html#considerations-for-file-uploads) makes the empty object the recommended form for binary content: "a binary file of any type" is written `content: application/octet-stream: {}`, and "a PNG image as a binary file" as `content: image/png: {}`. So for 3.1 documents this is the canonical way to describe a download, not an authoring gap.
- A missing schema puts no constraint on the content. It is equivalent to the empty schema `{}`, with the media type alone saying how the bytes are to be read.

## Root cause

Traced.

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/Return.kt:46-50` — `Response.toReturnType` iterates over every `content` entry and `require`s `mediaType.schema != null || mediaType.itemSchema != null`. The first schema-less entry anywhere in the document aborts `toApiTree`.
- Lines 52-56 would otherwise skip such an entry (`mediaType.schema?.let { ... }` inside `mapNotNull`), which would be the silent drop the `require` is guarding against. So removing the `require` alone is not a fix.
- Request bodies take the other branch of the same decision: `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/RequestBody.kt:38` has `val schema = mediaType.schema ?: return@mapNotNull null`, and line 51 returns `null` for the whole body when it is not required and no entry has a schema. Only a required body with no schema in any content type fails (lines 52-56). Nothing is reported.

Related behaviour that the fix must not copy (verified by `repro/c3-empty-schema.json`, generated code read, not run against a server): an explicit empty schema under a non-JSON media type, `"application/pdf": {"schema": {}}` or `"text/plain": {"schema": {}}`, generates `value: JsonElement` read with `response.body()`. A PDF or plain text body cannot be decoded as `JsonElement`, so "treat missing as `{}`" would turn 183 typed failures into clients that fail at runtime. 4 census documents have a type-less schema under a non-JSON response media type today.

## Correct final API

A media type declared without `schema` (and without `itemSchema`) gets its type from the media type alone. `example` and `examples` never influence it. One classification (`ContentType.mediaTypeKind()` in `typed`) serves responses and request bodies.

Responses:

| Declared media type, no schema | Generated value type |
|---|---|
| `application/json`, `*+json` | `JsonElement` (what `schema: {}` gives) |
| `text/*` | `String` (read as text, like `{type: string}` under `text/plain`) |
| anything else (`application/pdf`, `image/*`, `application/octet-stream`, `*/*`, XML) | Ktor's `io.ktor.client.statement.HttpResponse` |

`ByteArray` is only for a spec that says bytes: a declared `{type: string, format: binary}` keeps generating `ByteArray`. Where the spec says nothing, the caller gets the `HttpResponse` and chooses `body<ByteArray>()`, `bodyAsChannel()`, the headers, the real content type.

Typed status dispatch stays and the response object is passed through unread:

```kotlin
public suspend operator fun invoke(): HttpResponse {
  val response = client.get("certificate") {
    header(HttpHeaders.Accept, ContentType.Application.Pdf)
  }
  if (response.status.value != 200) throw ResponseException(response, "")
  return response
}
```

In a sealed response the success case holds it (`data class Ok(val response: HttpResponse) : Response`) and the declared error cases are unchanged. `Accept` carries the declared media type. A schema-less entry next to a typed one for the same status follows the existing multi-content-type rendering: one method per success media type (`json()`, `pdf()`), each returning its own type.

Request bodies:

- Schema-less JSON is a `body: JsonElement`, schema-less `text/*` a `body: String`, both sent with the declared `Content-Type`.
- Any other schema-less media type has no body type. The function takes a request builder block and the caller sets the body; the declared media type is set before the block, so the block can override it:

```kotlin
public suspend operator fun invoke(block: HttpRequestBuilder.() -> Unit) {
  val response = client.post("upload") {
    contentType(ContentType.Application.Pdf)
    block()
  }
  ...
}
```

  The block is required when the body is required and defaults to `{}` when it is optional (alone). Next to a typed alternative it is one more overload; when the body is optional it has no default there, so `invoke()` stays unambiguous. A body declared under a range (`image/*`) or under several such media types takes a `contentType: ContentType` argument before the block, validated like the existing declared-binary range upload.
- No media type is dropped any more. An `itemSchema`-only request body is still not modelled (streaming request bodies are out of scope).

## Fix slice

- typed, `Return.kt`: replace the `require` with a model derived from the content type when both `schema` and `itemSchema` are absent. The existing `types` map then gets one entry per declared media type and the renderer needs no new case (it already renders `ByteArray`, `String` and `JsonElement` responses, see the output of `repro/c3-binary-ok.json`).
- typed, `RequestBody.kt:38`: same derivation instead of `return@mapNotNull null`; lines 50-57 then only apply to `content: {}`.
- Open decisions:
  1. The JSON / text / binary classification should be one function shared by both sides, and ideally the same one that decides how `schema: {}` under a non-JSON media type is read, so that case is fixed with it.
  2. `*/*` without schema: `ByteArray` is the only honest type.
  3. A schema-less entry next to a typed one for another media type (10 documents) follows the existing multi-content-type rendering; nothing special.
- Not part of this fix: `Bad Content-Type format` failures, see below.

### Invalid neighbours

5 of the 243 documents (truora.com [0cb5532798f3e3394c08], osf.io [467c923b8416ac55398c], vmware.local:vrni [7a84acec33ee8d68a45a], zoom.us [f1f4ca5024e6431d6985], biapi.pro [fa452a26d77a1838b8ad]) have `content` keys that are not media types (`token`, `name`, `data`, `new` in zoom.us, `PDF` in truora.com, ...). Those are invalid (§4.7.14: the key is "a media type or media type range"). Once this issue is fixed they will reach `ContentType.parse` and fail as `BadContentTypeFormatException`, the family that vtex.local:Catalog-API [182cfc323e411b36cdf6] and isendpro.com [3333e066d4d57810d89d] are already in. They need a diagnostic naming the operation and key, which belongs to that family's report.

## Proof

Failing tests to write first:

1. typed: a `ReturnSpec`-style test next to `typed/src/commonTest/kotlin/io/github/nomisrev/openapi/routes/ResponseRangeSpec.kt` asserting `route.returns.responses[200].types` for the three reproducers (PDF with `example`, JSON with `examples`, empty `text/plain`). Today each throws at `Return.kt:47`.
2. typed: the request reproducer, asserting `route.body.types` contains `application/pdf`. Today it is `null` / JSON only.
3. renderer: a golden `ClientSpec` case (`client/media-type-without-schema`) covering the three response shapes and the request body.
4. runtime: a MockEngine test that serves `%PDF-1.4` bytes with `Content-Type: application/pdf` and asserts the returned `ByteArray`, serves `text/plain` and asserts the `String`, and asserts the request sends `Content-Type: application/pdf` with the given bytes.

Integration-tests coverage: none for responses. `github.json`, `youtrack.json` and `openai.yaml` contain no schema-less response media type, so they did not and would not catch a regression. `github.json` does contain the request-side case once (`GET /repos/{owner}/{repo}/contents/{path}`, `requestBody.content["application/json"]` with only `examples`), which is dropped silently today; that operation's signature will change with the fix.

## Affected documents

243 fail in typed with this message. First ten by sub-group:

- binary: apisetu.gov.in:hsbte [02a240ddf8fff7ee5ff2], apisetu.gov.in:scvtup [02db6158488c7d3d1665] (179 apisetu.gov.in documents in total), openaq.local [03210f808ca660b94018], ritekit.com [2e1feb4899db4391da92], clicksend.com [5b592b3f51711e95143d], elevenlabs.io [f2f35f8ff239cd3b13ef]
- JSON with example(s): change.local [00810433e137014fc48b], clubhouseapi.com [067f257511eb65c95ca7], snyk.io [1ca3b421f63392f7058f], dev.to [2b4773342329f0660e88], zuora.com [2c001941fbc8ed312c21]
- text: taxrates.io [53f4eabecf49717e5dbb], wolframalpha.com [89a3fc2630b7aa2b249c], mercure.local [d8405b7f83ca6fa4464b]
- empty object: xtrf.eu [43758eab66c18ac44ad2], atlassian.com:jira [ae2b609d3d04e763ffcb], microcks.local [c2e53ef7acd78f1fd5d1]

Masked by an earlier failure (7): vtex.local:Catalog-API [182cfc323e411b36cdf6], isendpro.com [3333e066d4d57810d89d], digitalocean.com [f98034cdf05682c61474], just-eat.co.uk [f2467dc5e5257eff684a], docker.com:engine [72153fdeeb7f32d0f9f5], opensuse.org:obs [2b172818deef551c832c], whatsapp.local [835a1c4e625565ee61ee].

Request body dropped silently (6): okta.local [35e50757b86acb09f331], plaid.com [245c1b438f1a896cb80d], brainbi.net [7feff951ba2385533f01], getpostman.com [fd89481e9ab83eca42e2], meilisearch.com [f4ad07296f44e46a790c], nowpayments.io [e2cd747f146de12b282c].

Regenerate the lists with `python3 <scratchpad>/c3/mt.py` (writes `<scratchpad>/c3/mt.json`).
