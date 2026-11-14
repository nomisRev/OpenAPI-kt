# A `content` key that is not a media type crashes with Ktor's BadContentTypeFormatException

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** typed
**Impact:** 6 of 1,547 documents (vtex Catalog, vtex Promotions, isendpro, loket.nl, whatsapp, apple app-store-connect)
**Source spec valid:** no — in all 6 the key is not a media type; the question is which diagnostic to give

## Symptom

```
io.ktor.http.BadContentTypeFormatException: Bad Content-Type format: gzip
	at io.ktor.http.ContentType$Companion.parse(ContentTypes.kt:144)
	at io.github.nomisrev.openapi.routes.ReturnKt.toReturnType(Return.kt:54)
```

A Ktor exception escapes `toApiTree`. It names neither the operation nor whether the key is on a request or a
response. One bad key fails the whole document.

## Minimal reproducer

`<scratchpad>/repro/malformed-media-type-response.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/reports": { "get": { "responses": { "200": { "description": "ok",
    "content": { "gzip": { "schema": { "type": "string", "format": "binary" } } } } } } } }
}
```

Observed:

```
worker exit=1 phases: parse pass, typed fail
io.ktor.http.BadContentTypeFormatException: Bad Content-Type format: gzip
	at io.ktor.http.ContentType$Companion.parse(ContentTypes.kt:144)
	at io.github.nomisrev.openapi.routes.ReturnKt.toReturnType(Return.kt:54)
```

`<scratchpad>/repro/malformed-media-type-request.json` — a request body with `"form-data"` next to a valid
`"application/json"`:

```
io.ktor.http.BadContentTypeFormatException: Bad Content-Type format: form-data
	at io.github.nomisrev.openapi.routes.RequestBodyKt.toBodies(RequestBody.kt:39)
```

The valid JSON body does not save the operation.

## What the spec says

OAS 3.0.3 §4.7.13 Request Body Object and §4.7.17 Response Object, `content`: "The key is a media type or media type
range" (RFC 7231 §3.1.1.1: `type "/" subtype *( OWS ";" OWS parameter )`). None of these keys has a `/`:

| Document | Where | Key | Probably meant |
| --- | --- | --- | --- |
| vtex.local:Catalog-API [182cfc323e411b36cdf6] | request | `form-data` (also `xml` on a response) | `multipart/form-data`, `application/xml` |
| loket.nl [3c2fbbbed59d2e29a55f] | request, 52 places | `applicationjson;version=2018-01-01` | `application/json;version=2018-01-01` |
| whatsapp.local [835a1c4e625565ee61ee] | request | `codecs=opus` (sibling of `audio/ogg`) | `audio/ogg; codecs=opus`, split by a converter |
| isendpro.com [3333e066d4d57810d89d] | response | `file`, `etat`, `exemple1` (7) | example names placed under `content` |
| apple.com:app-store-connect [a29b842821ae0032bd46] | response | `gzip` | `application/a-gzip` (Apple's header) |
| vtex.local:Promotions- [f36133494e28a3b79c91] | response | `Promotion`, `Tax` | example names placed under `content` |

## Root cause (traced)

`ContentType.parse(key)` is called unguarded on the raw map key at four places:

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/RequestBody.kt:39` (request bodies)
- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/Return.kt:54` (response schemas) and `:60` (streams)
- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/SchemaContext.kt:80`

The parser accepts any string as a `content` key (`Map<String, MediaType>`), so typed is the first place the key is
interpreted, and it has no error handling there.

Not reproduced, same code area: `RequestBody.kt:91` parses `encoding.<name>.contentType` with `ContentType.parse`,
but the Encoding Object allows a comma-separated list there ("image/png, image/jpeg"). I did not test what Ktor
returns for a list; worth a test when touching this.

## Correct final API

No guessing what the author meant. The entry cannot be sent or matched, so it is not generated, and that is reported:

- The operation has other usable content (vtex Catalog request with JSON next to `form-data`; isendpro `200` with
  `application/json` next to `file`): generate from the valid entries, **warning**
  `POST /files request body: content key 'form-data' is not a media type (expected type/subtype); ignored.`
- The malformed key is the only content of a request body or of a response (apple `gzip`, loket): an **error**
  diagnostic for that operation, consistent with the form-field rule in `GenerationDiagnostics.kt` ("error when it is
  the operation's only request body, warning with the overload skipped otherwise"). For a response the lenient
  alternative is to keep the status code and return the raw `HttpResponse`/bytes with a warning; pick one rule.

Either way the rest of the document generates, and the message names method, path, request/response + status code
and the key.

## Fix slice

- typed: one helper `parseMediaTypeOrNull(key)` (catching `BadContentTypeFormatException`) used at the four call
  sites; entries that fail are collected on `Route.Bodies` / `Route.ReturnType` (e.g. `ignoredContent: List<String>`)
  instead of thrown.
- renderer, `GenerationDiagnostics.kt`: turn them into warnings/errors as above.
- Optional, in the parser or an `OpenApiPreprocessor`: validate `content` keys once so the same message is available
  without running typed.
- Decision: no auto-repair (`applicationjson` -> `application/json`). If repair is wanted, it belongs in a
  user-supplied preprocessor, not in the default pipeline.

## Proof

- Failing tests first: typed test for both reproducers asserting no exception and the recorded ignored key;
  renderer test asserting the diagnostic text and severity (only-content vs one-of-several).
- Golden `ClientSpec` for `malformed-media-type-request.json`: only the JSON overload is generated.
- Integration tests would not catch a regression: every request/response `content` key in `github.json`,
  `youtrack.json` and `openai.yaml` is a well-formed media type (checked by script).

## Affected documents

6 total, all in the table above.
