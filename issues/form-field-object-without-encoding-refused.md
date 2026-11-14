# Form fields that are objects (or arrays of objects) without an Encoding Object are not generated

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** render — `Warning: Skipping unsupported form overload` (an `Error` when the form is the only body)
**Impact:** 8 of 1,547 documents, 102 refused fields (telegram.org, clickmeter.com, gsmtasks.com, letmc.com:diary, letmc.com:maintenance, microsoft.com:cognitiveservices-Training, nordigen.com, smart-me.com). All 8 have a JSON alternative, so the documents still generate.
**Source spec valid:** yes

## Symptom

```
Warning: Skipping unsupported form overload for POST /restrictChatMember (application/x-www-form-urlencoded) because fields [chat_id, permissions] have no supported form serialization for their type and encoding (`style`, `explode`).
```

The `application/x-www-form-urlencoded` overload is dropped and only the JSON (and multipart) overloads remain.
Nothing is silently wrong on the wire; the loss is that a declared request representation is not generated, and the
same shape with no alternative body is an `Error` that fails the Gradle task.

Field shapes behind the warnings (no document in the census has an Encoding Object for these fields):

| field schema | docs | occurrences |
| --- | --- | --- |
| array of objects | 7 | 36 |
| object of scalars | 5 | 17 |
| object with nested array / object / union properties | 4 | 46 |
| array of a union | 1 (telegram `results`) | 2 |
| array of arrays | 1 (nordigen `access_scope`) | 1 |

(`chat_id` in the message above is a different root cause, `issues/form-field-union-of-scalars-refused.md`;
free-form fields are in `issues/untyped-schema-parameters-and-form-fields-refused.md`; `style: deepObject` on arrays
is `issues/stripe-deepobject-arrays.md`.)

## Minimal reproducer

`<scratchpad>/repro/form-object-default-json-alt.json` (reduced from telegram.org `restrictChatMember`):

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/restrictChatMember": { "post": {
    "operationId": "op",
    "requestBody": { "required": true, "content": {
      "application/x-www-form-urlencoded": { "schema": { "type": "object", "required": ["user_id", "permissions"],
        "properties": { "user_id": { "type": "integer" }, "permissions": { "$ref": "#/components/schemas/ChatPermissions" } } } },
      "application/json": { "schema": { "type": "object", "required": ["user_id", "permissions"],
        "properties": { "user_id": { "type": "integer" }, "permissions": { "$ref": "#/components/schemas/ChatPermissions" } } } } } },
    "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "type": "string" } } } } }
  } } },
  "components": { "schemas": { "ChatPermissions": { "type": "object",
    "properties": { "can_send_messages": { "type": "boolean" }, "can_pin": { "type": "boolean" } } } } }
}
```

```
worker exit=0 phases: parse, typed, render, emit pass
diagnostics: [{"severity":"Warning","message":"Skipping unsupported form overload for POST /restrictChatMember (application/x-www-form-urlencoded) because fields [permissions] have no supported form serialization for their type and encoding (`style`, `explode`)."}]
compile exit=0
```

Generated: only `invoke(userId: Long, permissions: ChatPermissions)` with `contentType(ContentType.Application.Json)`.

`<scratchpad>/repro/form-object-default-json.json` (same without the JSON alternative) reports the `Error`
"... and no alternative supported request body variant exists." `<scratchpad>/repro/form-array-of-objects.json`
does the same for an array of objects.

## What the spec says

Two readings exist, and the code already picked one:

- OAS 3.0.3 §4.7.14.4 (Media Type Object, "Support for x-www-form-urlencoded Request Bodies"): "When passing complex
  objects in the `application/x-www-form-urlencoded` content type, the default serialization strategy of such
  properties is described in the Encoding Object's `style` property as `form`." Read literally: an object field
  without an Encoding Object is `form` + `explode: true`, i.e. flattened into `can_send_messages=true&can_pin=false`.
- OAS 3.0.3 §4.7.15.1 (Encoding Object `contentType`): "Default value depends on the property type: ... for `object` —
  `application/json`; for `array` — the default is defined based on the inner type." OAS 3.1.1 §4.8.15 resolves the
  conflict: when none of `style`, `explode`, `allowReserved` is present, `contentType` (default or explicit)
  governs, so an object field is the JSON text of the object.

`Model.isFormUrlEncodable` follows the 3.1.1 reading (KDoc, `RequestBody.kt:226-235`: "It needs an explicit `style`
or `explode`: without one, its default is its `contentType`, `application/json`, a JSON string the client does not
send."). Under that reading the serialization is fully defined; it just is not implemented. It is also what the
affected APIs want: telegram.org's own description of `shipping_options` reads "A JSON-serialized array of
available shipping options" (verified in the census spec); object fields such as `permissions` are `$ref`s without
such a note.

## Root cause

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/RequestBody.kt:237-255`:

```kotlin
is Model.Object ->
    encoding != null && (encoding.style != null || encoding.explode != null) && ...
```

An object without an explicit `style`/`explode` returns false by design (line 246-250). An array of objects fails
the scalar-items check (line 242-244). The field name lands in `Route.Body.FormUrlEncoded.unsupportedFields`
(`RequestBody.kt:118-124`), and `GenerationDiagnostics.kt:93-123` reports the overload and
`withSupportedRequestBodiesOnly` (`:177-195`) removes it.

## Correct final API

```kotlin
// form overload, next to the JSON one
public suspend operator fun invoke(userId: Long, permissions: ChatPermissions): String
// wire: user_id=42&permissions=%7B%22can_send_messages%22%3Atrue%7D
append("permissions", json.encodeToString(ChatPermissions.serializer(), permissions))
```

- Object field, no `style`/`explode`: one field whose value is the JSON text, encoded with the client's configured
  `Json`. An explicit Encoding `contentType` that is not JSON stays refused with a diagnostic naming it.
- Array of objects, no Encoding Object: the 3.x text ("default is defined based on the inner type") gives each item
  the content type `application/json`, and an array under the default `form`/`explode: true` is one field per item:
  `shipping_options=%7B...%7D&shipping_options=%7B...%7D`. Telegram instead expects a single JSON array. This is the
  one genuinely ambiguous sub-case: keep it refused (with a diagnostic that says the array-of-objects default is
  ambiguous) unless an Encoding Object says `contentType: application/json` for the field, in which case the whole
  array is one JSON value.
- When both JSON and form overloads would be generated with identical Kotlin signatures, the existing
  content-type overload selection applies; this report does not change it.

## Fix slice

- typed: `isFormUrlEncodable` returns true for an object (any serializable model) when no `style`/`explode` is
  given and the effective `contentType` is JSON; `Route.Body.FormUrlEncoded` needs to record per field that it is
  JSON-encoded (today it carries only `styles: Map<String, FormStyle>`; a `FormStyle.Json` or a parallel set).
- renderer: `addFormFieldCode` (`ImplRenderer.kt`) gets the JSON branch; needs the client's `Json` in scope.
- Open decisions: the array-of-objects default; whether OpenAPI 3.0 documents should follow the 3.0.3 literal reading
  (flatten) instead. Recommendation: one rule for all versions, the 3.1.1 one, because 3.1.1 is a clarification and
  flattening silently changes field names.

## Proof

- typed `RouteSpec` (there are existing cases around `unsupportedFields`, lines 754-906): an object field without
  encoding is supported and marked JSON (failing first).
- renderer golden `ClientSpec` and a MockEngine runtime test asserting the exact form body bytes.
- integration-tests coverage: `github.json` and `youtrack.json` have no urlencoded bodies with object fields;
  `openai.yaml` has form fields that are unions (multipart). No regression coverage for this construct.

## Affected documents

telegram.org [19696a2d729d6669b650], clickmeter.com [23d8e065f3aa6785b5e0], gsmtasks.com [daf353ec0ddccdfbd4fa],
letmc.com:diary [bcbf6672edf71691adf7], letmc.com:maintenance [e824d2c8b51d05f3913d],
microsoft.com:cognitiveservices-Training [39cdf9cd72fdf1d39084], nordigen.com [e7bb45654e2703c425cc],
smart-me.com [e3426242c60b04dcddba] — 8 in total, all warnings.
