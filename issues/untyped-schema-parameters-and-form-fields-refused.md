# Parameters and form fields with an untyped schema (`{}`, `items: {}`, `type: object`) are refused

**Status:** fixed for scalar, array and free-form object parameters and untyped form fields; open for a free-form object form field, an array of free-form objects, and `deepObject` (see below). An untyped value in a query, header, cookie or path parameter or a urlencoded form field is a `JsonPrimitive` sent as its `content` (`any: JsonPrimitive?`, `ids: List<JsonPrimitive>?`, `parameters: JsonPrimitive?`); a free-form object parameter is `Map<String, JsonPrimitive>` (`form` exploded: one `key=value` per entry; not exploded: `name=k1,v1,k2,v2`). A `JsonNull` for an optional parameter is left out like `null`; for a required one it is sent as the text `null`. Still refused: an array of free-form objects, a map of free-form objects, a free-form object path parameter, and a free-form object form field (an object form field has no map branch in `isFormUrlEncodable`; it is reported). Free-form JSON in bodies and properties stays `JsonElement`.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** render (`OpenAPI generation failed:`) for parameters; `Error` diagnostic for form bodies
**Impact:** 24 of 1,547 documents — 11 through parameters (crediwatch.com:covid19, gerermesaffaires.com, statsocial.com, wikipathways.org, rudder.example.local, ticketmaster.com:discovery, mastodon.local, openpolicy.local, salesloft.com, telnyx.com, twilio.com:twilio_lookups_v1), 12 `twilio.com:*` documents through form fields, plus gsmtasks.com (warnings only)
**Source spec valid:** yes — a schema without `type` is valid and means "any value"; it is under-specified, not wrong

## Symptom

Parameters (11 documents fail to render):

```
- GET /reports/custom/insert/ parameter ids (in query, style form, explode false) has no supported serialization for its type, location, `style` and `explode`.
```

Form fields (12 Twilio documents, `Error` diagnostic; the Gradle task fails on it):

```
POST /v1/Flows/{FlowSid}/Engagements (application/x-www-form-urlencoded) contains form fields [Parameters] that have no supported form serialization for their type and encoding (`style`, `explode`), and no alternative supported request body variant exists.
```

| where | schema | docs | occurrences |
| --- | --- | --- | --- |
| query, form, explode false | `{type: array, items: {}}` | 3 (statsocial.com, wikipathways.org, rudder.example.local) | 10 |
| query, form, explode true | `{type: array, items: {}}` | 1 (ticketmaster.com:discovery) | 4 |
| query, form, explode true | `{}` (no type) | 2 (crediwatch.com:covid19, gerermesaffaires.com) | 3 |
| query, form, explode true | `{type: object}` (free-form) | 4 (twilio lookups, openpolicy, salesloft, telnyx) | 14 |
| query, form, explode true | `{type: array, items: {type: object}}` | 1 (mastodon.local) | 1 |
| form field, no Encoding Object | `{}` with only a description | 12 (twilio) | 69 |
| form field, no Encoding Object | `{type: array, items: {}}` | 2 (twilio events, verify) | 2 |
| form field, no Encoding Object (warning, JSON alternative exists) | `{type: object}` / array of it | 1 (gsmtasks.com) | 22 |

The `deepObject` flavour of the free-form object is in `issues/deepobject-parameter-open-or-nested-object-refused.md`.

## Minimal reproducer

`<scratchpad>/repro/untyped-query.json`:

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/x": { "get": {
    "operationId": "op",
    "parameters": [
      { "name": "any", "in": "query", "schema": {} },
      { "name": "ids", "in": "query", "explode": false, "schema": { "type": "array", "items": {} } },
      { "name": "obj", "in": "query", "schema": { "type": "object" } }
    ],
    "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "type": "string" } } } } }
  } } }
}
```

```
worker exit=1 phases: parse pass, typed pass, render fail
- GET /x parameter any (in query, style form, explode true) has no supported serialization for its type, location, `style` and `explode`.
- GET /x parameter ids (in query, style form, explode false) has no supported serialization for its type, location, `style` and `explode`.
- GET /x parameter obj (in query, style form, explode true) has no supported serialization for its type, location, `style` and `explode`.
no sources emitted
```

`<scratchpad>/repro/untyped-form-field.json` (reduced from twilio.com:twilio_studio_v1 `CreateEngagementRequest`):

```json
"requestBody": { "content": { "application/x-www-form-urlencoded": { "schema": {
  "type": "object", "required": ["To"],
  "properties": { "To": { "type": "string" }, "Parameters": { "description": "A JSON string" } } } } } }
```

```
worker exit=0 phases: parse, typed, render, emit pass
diagnostics: [{"severity":"Error","message":"POST /x (application/x-www-form-urlencoded) contains form fields [Parameters] that have no supported form serialization for their type and encoding (`style`, `explode`), and no alternative supported request body variant exists."}]
compile exit=0
```

Note what the harness emitted for this one: `public suspend operator fun invoke(): String { val response = client.post("x") ...`
— the operation without any body. Through the Gradle plugin the `Error` aborts the build, so this code is never
written; see `issues/render-with-diagnostics-throws-on-parameter-errors.md` for why the harness and the plugin differ.

## What the spec says

- OAS 3.0.3 §4.7.24 Schema Object / JSON Schema: a schema with no `type` accepts any instance. `items: {}` is an
  array of anything; `type: object` without `properties` is any object.
- OAS 3.0.3 §4.7.12.2: `form` is defined for primitives, arrays and objects of primitives. An untyped parameter is
  therefore serializable exactly when the runtime value is one of those; the document does not say which.
- Twilio's descriptions say what is meant ("A JSON string we will add to your flow's context ... the JSON value must
  explicitly be passed as a string"), but only in prose.

## Root cause

- A schema with no type maps to `Model.FreeFormJson`. `Model.isFormScalar()` lists `Model.FreeFormJson` as not a
  scalar: `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/RequestBody.kt:274-303` (line 290).
- Parameters: `Model.isSerializableParameter`, `typed/.../routes/ParameterStyle.kt:73-97` — `FreeFormJson` matches no
  branch and hits `else -> false`; `Model.Collection` requires `inner.formModel()?.isFormScalar() == true` (line 78).
- Form fields: `Model.isFormUrlEncodable`, `RequestBody.kt:237-255` — `else -> false` (line 253) for `FreeFormJson`,
  and the collection branch has the same scalar-items requirement (line 242-244).
- Diagnostics: `renderer/.../GenerationDiagnostics.kt:281-294` (parameters, always `Error`) and `:93-123` (forms).

## Correct final API

The value space of an untyped schema is "any JSON value", and the wire is a string. The most accurate signature that
is still sendable without guessing:

```kotlin
// { "name": "any", "in": "query", "schema": {} }
any: JsonPrimitive? = null            // any=<content>; strings unquoted, numbers/booleans as literals
// { "type": "array", "items": {} }, explode false
ids: List<JsonPrimitive>? = null      // ids=a,b,3
// form field `Parameters: {}`
parameters: JsonPrimitive? = null     // Parameters=%7B%22name%22%3A%22Zeke%22%7D when the caller passes JsonPrimitive("{\"name\":\"Zeke\"}")
```

`JsonPrimitive` rather than `JsonElement` because `form` has no serialization for a nested value, and rather than
`String` because the schema also admits numbers and booleans. Using `String` everywhere is the simpler option and
loses nothing on the wire; it is an open decision, but it must be the same choice in all untyped positions
(query, header, cookie, form field, `deepObject` map values).

For `type: object` without properties under `form` + `explode: true` (twilio lookups `AddOnsData`, telnyx,
salesloft): the defined serialization is one `key=value` pair per entry, so `Map<String, JsonPrimitive>` (or
`Map<String, String>`) sent as top-level query pairs. For `explode: false`: `name=k1,v1,k2,v2`.

Array of free-form objects (mastodon `timeline`, gsmtasks `contact_addresses_data`) stays refused: nested, undefined.

## Fix slice

- typed: let `isFormScalar` (or a sibling `isFormValue`) accept `Model.FreeFormJson` when the source schema has no
  structural keyword; accept an untyped object as a scalar map. Both `isSerializableParameter` and
  `isFormUrlEncodable` share the helper, so parameters and form fields move together.
- renderer: `ParameterSerialization.kt` / `addFormFieldCode` (`ImplRenderer.kt`) need a `JsonPrimitive.content`
  branch.
- Open decisions: `JsonPrimitive` vs `String`; whether `Model.FreeFormJson` needs to distinguish "no type at all"
  from "type: object" (it has to, for the map case).

## Proof

- typed `RouteSpec`: `isSupported` / `unsupportedFields` for the four shapes (failing first).
- renderer golden for the Twilio form: `invoke(to: String, parameters: JsonPrimitive? = null)`.
- MockEngine runtime test: body equals `To=%2B1555&Parameters=%7B%22name%22%3A%22Zeke%22%7D`.
- integration-tests coverage: none of `github.json`, `openai.yaml`, `youtrack.json` has an untyped query parameter;
  `openai.yaml` has form fields that are `anyOf` including an untyped member (multipart, not urlencoded). No
  regression coverage.

## Affected documents

statsocial.com [4abefb5e2312ee81820c], wikipathways.org [8380abe99ff6a09411ba], ticketmaster.com:discovery [94c7421cd5897673cca2],
crediwatch.com:covid19 [47400977e7d7d92d2152], openpolicy.local [698ee6ef4aacf3f24943], salesloft.com [cb0b85a1223cdfcbe7ae],
twilio.com:twilio_lookups_v1 [2dab3e50739ed8e0c32c], twilio.com:twilio_studio_v1 [14608c4cf7fae941e03b],
twilio.com:twilio_preview [d0e7285c3c9fac8e646d], twilio.com:twilio_sync_v1 [9f32366870d3f1c5328a] — 24 in total.
The 12 Twilio form documents (autopilot_v1, events_v1, flex_v1, media_v1, notify_v1, numbers_v2, preview, studio_v1,
studio_v2, sync_v1, trusthub_v1, verify_v2) are recorded as pass/compile in `results.jsonl` because the harness does
not treat `Error` diagnostics as a failure.
