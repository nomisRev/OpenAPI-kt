# A form-urlencoded body whose schema is not an object with properties aborts generation

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** typed
**Impact:** 8 of 1,547 documents (stripe, notion, billbee, spoonacular, libretranslate, envoice, agco-ats, ably)
**Source spec valid:** mixed — an empty object, a map and a `oneOf` of objects are valid; a top-level string, integer or array has no defined form serialization

## Symptom

```
IllegalArgumentException: Form URL encoded body must be an object. io.github.nomisrev.openapi.registry.Registry@64712be
```

The message names neither the operation nor the schema (`$this` is the `Registry`). The whole document fails.

## Minimal reproducer

`<scratchpad>/repro/form-urlencoded-<shape>.json`, all with this skeleton:

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/detect": { "post": {
    "requestBody": { "content": { "application/x-www-form-urlencoded": { "schema": SCHEMA } } },
    "responses": { "204": { "description": "ok" } }
  } } }
}
```

| File suffix | SCHEMA | Corpus example | Result |
| --- | --- | --- | --- |
| `empty-object` | `{"type":"object","properties":{},"additionalProperties":false}` | stripe (233 operations, e.g. `GET /v1/account`), notion `GET /v1/comments` | fails |
| `free-object` | `{"type":"object"}` | billbee `PATCH /api/v1/customers/addresses/{id}` | fails |
| `map` | `{"type":"object","additionalProperties":{"type":"string"}}` | (none seen as first failure) | fails |
| `oneof` | `{"oneOf":[{object a},{object b}]}` | ably `POST /push/channelSubscriptions` | fails |
| `array` | `{"type":"array","items":{"type":"integer"}}` | billbee, agco-ats (12 operations) | fails |
| `string` | `{"type":"string"}` | spoonacular `POST /food/detect`, libretranslate `POST /detect`; envoice uses `integer` | fails |

Observed for each:

```
worker exit=1 phases: parse pass, typed fail
java.lang.IllegalArgumentException: Form URL encoded body must be an object. io.github.nomisrev.openapi.registry.Registry@67389cb8
```

## What the spec says

- OAS 3.0.3 §4.7.13 Request Body Object / §4.7.14 Media Type Object, "Support for x-www-form-urlencoded Request Bodies": the body is the
  schema's properties serialized per RFC 1866 as name/value pairs. §4.7.15 Encoding Object: `encoding` keys are
  property names. The model is "an object whose properties are the fields".
- An object with no properties serializes to an empty body. `additionalProperties` are fields with free names. A
  `oneOf` of objects is one of several field sets. All valid.
- A top-level `string`, `integer` or `array` has no field name, so RFC 1866 gives it no serialization. The spec does
  not forbid the schema, it just leaves the wire format undefined: source defect.

## Root cause (traced)

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/RequestBody.kt:102-127`, `formUrlEncoded`:

```kotlin
val obj = with(scope) {
    requireNotNull(ReferenceOr.value(resolvedSchema).toModel(name, SchemaContext.Write) as? Model.Object) {
        "Form URL encoded body must be an object. $this"
    }
}
```

Every schema that does not model as `Model.Object` throws: `{type: object}` without properties (free-form JSON),
maps, unions, collections, primitives. `$this` inside `with(scope)` is the `Registry`, hence `Registry@...` in the
message. `Route.Body.FormUrlEncoded` (`routes/Route.kt`) can only hold a fixed parameter list.

For comparison, `formDataToBody` (`RequestBody.kt:76-99`) has a fallback (`Route.Body.Multipart.Ref`) for non-object
multipart schemas; the form-urlencoded path has none.

## Correct final API

| Schema | Generated signature | Wire |
| --- | --- | --- |
| object, no properties, `additionalProperties: false` | `suspend operator fun invoke()` — no body parameter | no body (or empty body with `Content-Type: application/x-www-form-urlencoded`; decide once) |
| `{type: object}` / map of scalars | `invoke(body: Map<String, String>)` (map value type from `additionalProperties`) | `k1=v1&k2=v2`, percent-encoded |
| object with properties **and** `additionalProperties` | named parameters plus `additionalProperties: Map<String, V>` | both sets of pairs |
| `oneOf` of objects | one overload per branch, like `Route.Body.OverloadedBody` does for JSON | that branch's fields |
| string / integer / array | no faithful form exists: diagnostic, not a guess | — |

For the last row follow the existing rule for unserializable form fields (`GenerationDiagnostics.kt`, see
`issues/stripe-deepobject-arrays.md`): an **error** naming `METHOD path` and the media type when the form is the
operation's only body, a **warning** with that overload skipped otherwise.

## Fix slice

- typed, `RequestBody.kt:102-127`: stop throwing. Map the shapes above to `Route.Body.FormUrlEncoded` (extend it with
  an optional additional-properties model, and allow an empty parameter list) or to a new "unsupported form body"
  marker that carries the reason and JSON pointer.
- renderer, `GenerationDiagnostics.kt` (`requestBodyDiagnostics`) and `ImplRenderer.kt` (`addFormFieldCode`): render
  the map case with `parameters { map.forEach(::append) }`, emit the diagnostics.
- Until then, at minimum fix the message: name `method path`, the content type and the schema type, not the Registry.
- Design decisions: empty form on `GET` (stripe, notion) — send nothing; whether nested values in a free-form map are
  allowed (they need an Encoding Object, so probably scalars only).
- Stripe note: `issues/stripe-deepobject-arrays.md` says Stripe "now fails with clear errors" from the renderer. On
  this branch Stripe never gets that far: it fails here first, on its 233 empty forms.

## Proof

- Failing tests first: typed test per shape asserting the `Route.Body` produced (or the marker); renderer
  `GenerationDiagnostics` test for the primitive/array cases.
- Golden `ClientSpec` for the empty-object and map cases; MockEngine test asserting the encoded body
  `a=1&b=x%20y` and the `Content-Type`.
- Integration tests would not catch a regression: `openai.yaml` has 3 form bodies, all objects with properties;
  `github.json` and `youtrack.json` have none. `parser/src/commonTest/resources/specs/stripe.json` contains the
  empty-object shape and can serve as a typed-layer fixture.

## Affected documents

8 total. stripe.com [bd6338ab48ec594bbcc7] (empty object), notion.com [370d102b94aef0e2b686] (empty object),
billbee.io [0c0feff04882be4953be] (free object, arrays), agco-ats.com [55f28aec858d4c1116aa] (arrays),
spoonacular.com [4a9c4965908d786c301b] (string), libretranslate.local [ce250cf9c3ade6565ab7] (string),
envoice.in [524930cb24b29fbfde7b] (integer), ably.io:platform [e603fb8c575432c493bb] (oneOf of objects).
