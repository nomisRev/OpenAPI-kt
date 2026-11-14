# A `format: binary` field in an `application/x-www-form-urlencoded` body fails generation

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** render — `Error` diagnostic (`... and no alternative supported request body variant exists.`)
**Impact:** 2 of 1,547 documents, 4 operations (digitallocker.gov.in:authpartner `hmac`; mineskin.org `file`)
**Source spec valid:** no in substance — binary data has no urlencoded form; both documents mean something else (a hex string; a multipart upload)

## Symptom

```
Error: POST /oauth2/1/pull/doctype (application/x-www-form-urlencoded) contains form fields [hmac] that have no supported form serialization for their type and encoding (`style`, `explode`), and no alternative supported request body variant exists.
```

- digitallocker: `hmac: {type: string, format: binary}` described as "SHA-256 encrypted value of the client secret,
  clientid and ts" — a hex/base64 text digest, mis-declared as `binary`.
- mineskin: `POST /generate/upload` declares `file: {type: string, format: binary}` inside
  `application/x-www-form-urlencoded`; the real API takes `multipart/form-data`.

Both documents are recorded as passing render in `results.jsonl` because the harness ignores `Error` diagnostics
(`issues/render-with-diagnostics-throws-on-parameter-errors.md`); the Gradle task fails on them. In the harness
output the operation is generated without a request body at all.

## Minimal reproducer

`<scratchpad>/repro/form-binary-field.json`:

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/upload": { "post": {
    "operationId": "op",
    "requestBody": { "required": true, "content": { "application/x-www-form-urlencoded": { "schema": {
      "type": "object",
      "properties": { "clientid": { "type": "string" }, "hmac": { "type": "string", "format": "binary" } } } } } },
    "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "type": "string" } } } } }
  } } }
}
```

```
worker exit=0 phases: parse, typed, render, emit pass
diagnostics: [{"severity":"Error","message":"POST /upload (application/x-www-form-urlencoded) contains form fields [hmac] that have no supported form serialization for their type and encoding (`style`, `explode`), and no alternative supported request body variant exists."}]
compile exit=0
```

Control `<scratchpad>/repro/form-byte-field.json` (`format: byte`, i.e. base64 text) produces no diagnostic.

## What the spec says

OAS 3.0.3 §4.7.15.1: the default Encoding `contentType` for `string` with `format: binary` is
`application/octet-stream`, and §4.7.14.4 says urlencoded content is "stringified per RFC1866". RFC 1866 §8.2.1
defines the encoding for form field *text*; octets outside a character encoding have no agreed representation
(raw percent-encoded bytes are possible but no server framework decodes them back to bytes by default). OpenAPI
points file uploads at `multipart/form-data` (§4.7.14.3 "Considerations for File Uploads"). So the refusal is
correct; the question is only the diagnostic.

## Root cause

`format: binary` becomes `Model.ByteArray`, which `Model.isFormScalar()` lists as not a scalar
(`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/RequestBody.kt:286`), so `isFormUrlEncodable`
(`RequestBody.kt:237-255`) returns false and the field is added to `unsupportedFields` (`RequestBody.kt:118-124`).
`GenerationDiagnostics.kt:93-123` emits the generic message.

## Correct final API

No generated form overload for such a field: keep the `Error`. The diagnostic should carry the reason and the fix a
spec author or a preprocessor can apply:

```
POST /upload (application/x-www-form-urlencoded): field hmac is `format: binary`; binary data cannot be sent in an
urlencoded form. Declare the body as multipart/form-data, or the field as a plain string / `format: byte`.
```

Sending percent-encoded raw bytes is technically possible (`hmac=%DE%AD...`) but would be a guess neither affected
API accepts; not recommended.

## Fix slice

- renderer only: make the form-field diagnostic reason-specific. `Route.Body.FormUrlEncoded.unsupportedFields` is a
  `List<String>` of names (`Route.kt:231-243`); it would need to carry a reason (`Map<String, Reason>` or a small
  sealed type: `Binary`, `NestedObject`, `ObjectWithoutEncoding`, `DeepObjectArray`, `Union`, `Untyped`) so every
  form report in this cluster gets an actionable message instead of the shared sentence.
- Open decision: none on behaviour.

## Proof

- typed `RouteSpec`: a `format: binary` field is in `unsupportedFields` with reason `Binary` (new assertion).
- renderer test on the diagnostic text.
- integration-tests coverage: not applicable (a refusal); none of the three specs has a binary urlencoded field.

## Affected documents

digitallocker.gov.in:authpartner [34c0f611ffd66319216b], mineskin.org [9e13e0004c27574a8062] — 2 in total.
