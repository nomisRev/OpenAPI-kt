# Parameters declared with `content: application/json` are refused unless the schema is a scalar

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** render (`OpenAPI generation failed:`)
**Impact:** 3 of 1,547 documents, 65 parameters (superset.apache.local:superset 60, rudder.example.local 4, apideck.com:file-storage 1 header)
**Source spec valid:** yes — `content` is the mechanism OpenAPI provides for complex parameter values

## Symptom

```
OpenAPI generation failed:
- GET /nodes parameter query (in query, content application/json) has no supported serialization for its type, location, `style` and `explode`.
- POST /file-storage/files parameter x-apideck-metadata (in header, content application/json) has no supported serialization for its type, location, `style` and `explode`.
```

The message is also misleading: a `content` parameter has no `style` or `explode`.

| location | schema under `application/json` | docs | occurrences |
| --- | --- | --- | --- |
| query | object with array (and map) properties | 2 (superset, rudder) | 45 |
| query | array of scalars | 1 (superset `q` on DELETE) | 15 |
| query | array of objects | 1 (rudder `where`) | 2 |
| query | object of scalars | 1 (superset) | 2 |
| header | object of scalars | 1 (apideck `x-apideck-metadata`) | 1 |

## Minimal reproducer

`<scratchpad>/repro/content-json-query.json` (reduced from superset `q`):

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/x": { "get": {
    "operationId": "op",
    "parameters": [ { "name": "q", "in": "query", "content": { "application/json": { "schema": {
      "type": "object",
      "properties": { "page": { "type": "integer" }, "columns": { "type": "array", "items": { "type": "string" } } } } } } } ],
    "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "type": "string" } } } } }
  } } }
}
```

```
worker exit=1 phases: parse pass, typed pass, render fail
- GET /x parameter q (in query, content application/json) has no supported serialization for its type, location, `style` and `explode`.
no sources emitted
```

Control `<scratchpad>/repro/content-json-scalar-ok.json` (same parameter, schema `{type: string}`) passes and generates

```kotlin
public suspend operator fun invoke(q: String? = null): String {
  val response = client.get("x") {
    if (q != null) {
      url.encodedParameters.append("q", q.encodeURLParameter())
    }
  }
```

## What the spec says

OAS 3.0.3 §4.7.12 Parameter Object, fixed fields: "`content` — A map containing the representations for the
parameter. The key is the media type and the value describes it. The map MUST only contain one entry." and "For more
complex scenarios, the `content` property can define the media type and schema of the parameter. A parameter MUST
contain either a `schema` property, or a `content` property, but not both." The spec's own example is a query
parameter `coordinates` with `content: application/json` and an object schema with `lat`/`long`.

So the value is the JSON text of the instance, percent-encoded for a query string, or placed as the header value.
This is fully defined for every JSON-serializable schema.

## Root cause

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/ParameterStyle.kt:67`:

```kotlin
if (hasContent) return model.isFormScalar()
```

The KDoc above it (line 52-53) states the limitation: "A parameter with `content` is serialized as its media type;
only a scalar, whose value is sent as is, is generated." The renderer has no JSON branch for parameters:
`renderer/.../ParameterSerialization.kt:452` treats a `content` parameter as `Style.simple`.

Related accuracy question in the supported case (observed in the control above, not traced further): a
`content: application/json` parameter with `schema: {type: string}` is sent as `q=abc`, but the JSON text of the
string `abc` is `"abc"` (`q=%22abc%22`). "Sent as is" is right for `text/plain` content, not for
`application/json`.

## Correct final API

```kotlin
// GET /x, parameter q with content application/json
public suspend operator fun invoke(q: Q? = null): String   // Q is the generated @Serializable data class

// wire: ?q=%7B%22page%22%3A1%2C%22columns%22%3A%5B%22a%22%5D%7D
url.encodedParameters.append("q", json.encodeToString(Q.serializer(), q).encodeURLParameter())
```

- Uses the client's configurable `Json` (project preference), not a private instance.
- Header (`x-apideck-metadata`): `header("x-apideck-metadata", json.encodeToString(...))`.
- Media types other than JSON (`text/plain` scalar stays as is; anything else the client cannot produce) keep an
  explicit diagnostic naming the media type.

## Fix slice

- typed: `isSerializableParameter(hasContent = true)` returns true when the media type is JSON
  (`application/json` or `+json`) for any model that has a serializer, and for `text/plain` scalars.
  `Route.Input.contentType` already carries the media type (`SchemaContext.kt:79-81`).
- renderer: a JSON branch in `addParameterPairsCode` / header code that encodes with the operation's `Json`; needs
  the `Json` instance in scope for operations that today only use `ContentNegotiation`.
- Diagnostic text: say "content application/json with a non-scalar schema is not generated yet" rather than
  mentioning `style` and `explode`.
- Open decision: JSON-quote string scalars under `application/json` content (accurate) or keep sending them bare
  (current behaviour, likely what most servers written against such documents expect).

## Proof

- typed `RouteSpec`: `isSupported == true` for object/array under `content: application/json` (failing first).
- renderer golden `ClientSpec` case for query and header.
- MockEngine runtime test asserting the exact encoded query string and that the client's configured `Json`
  (for example `encodeDefaults`, naming strategy) is the one used.
- integration-tests coverage: none. `github.json`, `openai.yaml` and `youtrack.json` contain no parameter with
  `content` (scanned: 0 occurrences).

## Affected documents

superset.apache.local:superset [ed90dc063025d3a8f7bf], rudder.example.local [619eecfc37efc6df2f78],
apideck.com:file-storage [283f449dd8e067e831d4] — 3 in total. rudder also has `array<any>` query parameters
(`issues/untyped-schema-parameters-and-form-fields-refused.md`); apideck:file-storage also has an open `deepObject`
parameter (`issues/deepobject-parameter-open-or-nested-object-refused.md`).
