# `style: deepObject` query parameters are refused unless the object is closed and flat

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** render (`OpenAPI generation failed:`)
**Impact:** 7 of 1,547 documents, 74 parameters (apideck.com:accounting, :file-storage, :issue-tracking, :webhook; here.com:tracking; impala.travel:hotels; redhat.com:catalog_inventory)
**Source spec valid:** mixed — free-form and `additionalProperties: true` objects are valid and have a defined `deepObject` form when their values are scalars; nested objects/arrays are undefined by OpenAPI

## Symptom

```
OpenAPI generation failed:
- GET /file-storage/files parameter pass_through (in query, style deepObject, explode true) has no supported serialization for its type, location, `style` and `explode`.
```

Four different schema shapes are behind the same message:

| shape of the `deepObject` schema | docs | occurrences | example |
| --- | --- | --- | --- |
| free-form object: `{type: object}` | 3 (redhat.com:catalog_inventory, impala.travel:hotels, here.com:tracking) | 57 | redhat `filter`, `sort_by`; here `labels` (example `{"group":"group1","priority":"high"}`) |
| scalar properties plus `additionalProperties: true` | 2 (apideck.com:accounting, :file-storage) | 14 | `pass_through` |
| object with a nested object or array property | 2 (apideck.com:webhook `filter`, apideck.com:issue-tracking `filter`) | 2 | webhook `filter.service: {id}` (nested object); issue-tracking `filter.status: array<string>` |
| `anyOf [object, enum string]` | 1 (here.com:tracking `floor`) | 1 | `floor[id]=DM_1234` or `floor=null` |

(here.com's 6 `labels` occurrences are reported under `/labels/v4/device`, `/labels/v4/geofence`, ...: the path
template is `/labels/v4/{resourceType}` with an enum path parameter, which the generator expands into fixed
segments.)

## Minimal reproducer

`<scratchpad>/repro/deepobject-freeform.json`:

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/x": { "get": {
    "operationId": "op",
    "parameters": [ { "name": "filter", "in": "query", "style": "deepObject", "explode": true,
                      "schema": { "type": "object" } } ],
    "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "type": "string" } } } } }
  } } }
}
```

```
worker exit=1 phases: parse pass, typed pass, render fail
- GET /x parameter filter (in query, style deepObject, explode true) has no supported serialization for its type, location, `style` and `explode`.
no sources emitted
```

Also failing with the same message: `<scratchpad>/repro/deepobject-additional-true.json`
(`{type: object, additionalProperties: true, properties: {search: {type: string}}}`) and
`<scratchpad>/repro/deepobject-nested.json` (a property `ids: array<string>`).

Control, `<scratchpad>/repro/deepobject-flat-ok.json` (closed object of scalars, and `additionalProperties: {type: string}`):
passes and generates

```kotlin
public suspend operator fun invoke(filter: Filter? = null, m: Map<String, String>? = null): String {
  val response = client.get("x") {
    if (filter != null) {
      if (filter.since != null) {
        url.encodedParameters.append("filter%5Bsince%5D", filter.since.encodeURLParameter())
      }
    }
    m?.forEach { url.encodedParameters.append("m%5B${it.key.encodeURLParameter()}%5D", it.value.encodeURLParameter()) }
  }
```

## What the spec says

OAS 3.0.3 §4.7.12.2 Style Values: `deepObject` — type `object`, in `query` — "Provides a simple way of rendering
nested objects using form parameters." The Style Examples table shows `color[R]=100&color[G]=200&color[B]=150`.
OAS 3.1.1 narrows the wording to objects with scalar properties and says the representation of array or object
properties is not defined. So:

- free-form object / `additionalProperties: true` whose runtime values are scalars: defined (`name[key]=value`);
  the schema just does not name the keys. Refusing these is stricter than the specification.
- nested object or array property: undefined; refusing is correct. `name[a][b]=v` and `name[a][]=v` are qs/Rails
  conventions, not OpenAPI (whether apideck's servers expect them was not checked).
- `anyOf [object, string]`: each alternative has a defined serialization on its own (`floor[id]=..` / `floor=null`).

## Root cause

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/ParameterStyle.kt:66-98`:

- line 66: `formModel()` returns the model; a schema-less object is `Model.FreeFormJson`, which falls to
  `else -> false` (line 96).
- line 80-83: a `Model.Object` is accepted only with `additionalProperties == AdditionalProperties.False` and all
  properties scalar, so `additionalProperties: true` and nested properties are both rejected by the same branch.
- line 89-94: a `Model.Union` is accepted only when every case `isUnionParameterCase()` (line 104-117: primitives and
  collections, not objects, not enums), so `anyOf [object, enum]` is rejected.
- A typed map (`Model.Dictionary` of scalars, line 85-87) is accepted, which is why `additionalProperties: {type: string}`
  works while `additionalProperties: true` and `{type: object}` do not.

## Correct final API

- Free-form object (`{type: object}`), values unknown: the honest Kotlin type is the same as a typed map whose value
  type is "a scalar". Proposal: `filter: Map<String, JsonPrimitive>? = null`, serialized as
  `filter[key]=<content>` (strings unquoted, numbers and booleans as their literals), skipping `JsonNull`.
  `Map<String, String>` is the simpler alternative and is what the wire carries anyway; pick one for all untyped
  positions (see `issues/untyped-schema-parameters-and-form-fields-refused.md`).
- Declared scalar properties plus `additionalProperties: true`: the data class the generator already produces for
  such a schema (declared properties plus the additional-properties map), serialized as one `name[prop]=value` per
  declared property and one `name[key]=value` per additional entry.
- Nested object/array property: stay refused (undefined), with a diagnostic that says so. A vendor opt-in
  (`deepObjectNesting = "brackets"`) is the same design question as `issues/stripe-deepobject-arrays.md`.
- `anyOf [object-of-scalars, scalar]`: a sealed union parameter; the object case is written as `name[prop]=value`,
  the scalar case as `name=value`.

## Fix slice

- typed: extend `isSerializableParameter` for `style == deepObject` (and for `form`, see the untyped report) to
  accept `Model.FreeFormJson` when the schema is an object, and `Model.Object` with open additional properties when
  declared properties are scalars; add `Model.Object`-of-scalars and `Model.Enum` to the union cases.
- renderer: `ParameterSerialization.kt` deepObject branch needs the free-form map and "declared + additional"
  variants (it already has the per-property and typed-map variants shown above).
- Open decisions: `JsonPrimitive` vs `String` values for free-form; whether nested deepObject is ever generated.
- Separate concern: one refused parameter fails the whole document (`issues/query-parameter-nested-objects-refused.md`
  discusses operation-level omission).

## Proof

- typed `RouteSpec`: `isSupported` for the four shapes (failing first for free-form and `additionalProperties: true`).
- renderer golden: generated signature and `url.encodedParameters.append("filter%5B...")` calls.
- MockEngine runtime test: `labels = mapOf("group" to "group1", "priority" to "high")` produces
  `?labels%5Bgroup%5D=group1&labels%5Bpriority%5D=high`.
- integration-tests coverage: `openai.yaml` has one `deepObject` parameter, a typed map of scalars (the supported
  case). None of the three specs has a free-form, open or nested `deepObject` parameter, so no regression coverage.

## Affected documents

apideck.com:accounting [dbe5a9ed4fb96502849c], apideck.com:file-storage [283f449dd8e067e831d4],
apideck.com:issue-tracking [cc06d832d257acadf4ac], apideck.com:webhook [91a593abb536239603e3],
here.com:tracking [bb102d0ca827a9fb1bf0], impala.travel:hotels [a0add2aa2d1a83a4eb60],
redhat.com:catalog_inventory [456aa92b9a6ef71704b4] — 7 in total. apideck.com:file-storage also needs
`issues/parameter-content-json-refused.md`; here.com:tracking also needs `issues/parameter-union-with-enum-case-refused.md`.
