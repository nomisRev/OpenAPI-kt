# A `oneOf`/`anyOf` query parameter is refused when one of its cases is an enum

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** render (`OpenAPI generation failed:`)
**Impact:** 1 of 1,547 documents, 9 parameters (here.com:tracking); the same gap makes form fields fail in telegram.org and gsmtasks.com, see `issues/form-field-union-of-scalars-refused.md`
**Source spec valid:** yes

## Symptom

```
OpenAPI generation failed:
- GET /bulkjobs/v4/deviceUploads parameter status (in query, style form, explode true) has no supported serialization for its type, location, `style` and `explode`.
```

The parameter is "one status or a list of statuses":

```json
{ "in": "query", "name": "status", "schema": { "oneOf": [
  { "type": "string", "enum": ["pending", "ongoing", "completed", "failed", "cancelled", "acknowledged"] },
  { "type": "array", "items": { "type": "string", "enum": ["pending", "ongoing", "completed", "failed", "cancelled", "acknowledged"] } }
] } }
```

## Minimal reproducer

`<scratchpad>/repro/query-union-enum-or-array.json`:

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/x": { "get": {
    "operationId": "op",
    "parameters": [ { "name": "status", "in": "query", "schema": { "oneOf": [
      { "type": "string", "enum": ["pending", "failed"] },
      { "type": "array", "items": { "type": "string", "enum": ["pending", "failed"] } } ] } } ],
    "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "type": "string" } } } } }
  } } }
}
```

```
worker exit=1 phases: parse pass, typed pass, render fail
- GET /x parameter status (in query, style form, explode true) has no supported serialization for its type, location, `style` and `explode`.
no sources emitted
```

Also fails: `<scratchpad>/repro/query-union-enum-or-string.json` (`oneOf [enum string, integer]`).

Controls that pass: `<scratchpad>/repro/query-union-array-or-scalar.json` (`oneOf [string, array<string>]`, the shape
`github.json` uses 11 times) and `<scratchpad>/repro/query-union-of-scalars-ok.json` (`anyOf [integer, string]`),
which generates

```kotlin
when (chatId) {
  is ChatId.CaseLong -> { url.encodedParameters.append("chat_id", chatId.value.toString()) }
  is ChatId.CaseString -> { url.encodedParameters.append("chat_id", chatId.value.encodeURLParameter()) }
}
```

So the only difference between supported and refused is `enum` on a case.

## What the spec says

OAS 3.0.3 §4.7.12.2: a primitive is serialized as its value and an array of primitives as repeated names under
`form`/`explode: true`. An enum is a constrained primitive; a union whose every alternative has a defined
serialization is serialized as the alternative it holds. Nothing is undefined here.

## Root cause

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/ParameterStyle.kt:89-94` accepts a union only when
every case satisfies `isUnionParameterCase()`, and that function (line 104-117) lists `Model.Collection`,
`Model.Date`, `Model.DateTime`, `Model.Uuid` and the `Model.Primitive.*` types but not `Model.Enum`
(falls to `else -> false`). The KDoc says why the list is narrow: these are the cases "the client wraps in a value
class of its own (`CaseString(value)`, `CaseStrings(value)`), so a parameter holding it is sent as its `value`".
An enum case is rendered differently (not a `CaseX(value)` wrapper around a primitive), and the parameter renderer
has no code path for it. A collection case whose items are enums is accepted by `isUnionParameterCase` but then goes
through `isSerializableParameter`, where `Model.Enum.isFormScalar()` is true, so the array case is not the problem.

## Correct final API

```kotlin
public suspend operator fun invoke(status: Status? = null): ...

// sealed union as generated today for bodies: a case holding the enum, a case holding the list
// wire, enum case:  status=pending
// wire, list case:  status=pending&status=failed
when (status) {
  is Status.CaseStatusEnum -> url.encodedParameters.append("status", status.value.value.encodeURLParameter())
  is Status.CaseStatusEnums -> status.value.forEach { url.encodedParameters.append("status", it.value.encodeURLParameter()) }
}
```

(Case class names are illustrative; use whatever `UnionRendererNaming` produces.) The enum's wire value must come
from its `value` property / `@SerialName`, not `name`, as `EnumRenderer.needsValueProperty` already arranges for
plain enum parameters.

## Fix slice

- typed: add `is Model.Enum -> true` to `isUnionParameterCase` (`ParameterStyle.kt:104`), with the path restriction
  that already applies to enums (`style == simple`, line 74).
- renderer: `ParameterSerialization.kt` union branch must emit the enum wire value for an enum case.
- The same helper should be reused for form fields, which have no union support at all
  (`issues/form-field-union-of-scalars-refused.md`).
- here.com:tracking also has `floor`: `anyOf [object, enum]` under `deepObject`, and free-form `labels`; it needs
  `issues/deepobject-parameter-open-or-nested-object-refused.md` too before the document generates.

## Proof

- typed `RouteSpec`: `isSupported == true` for `oneOf [enum, array<enum>]` and `oneOf [enum, integer]` in query
  (failing first).
- renderer golden + MockEngine runtime test for both cases of the union, including an enum value whose Kotlin name
  differs from its wire value.
- integration-tests coverage: `github.json` has 11 `oneOf [array, scalar]` query parameters and 8 `oneOf` path
  parameters, none with an enum case (they pass today), so the supported neighbour is covered at compile level and
  this gap is not. `openai.yaml` / `youtrack.json`: no such parameter.

## Affected documents

here.com:tracking [bb102d0ca827a9fb1bf0] — 1 in total (9 occurrences: `status`, `eventSource`, `eventType`, ...).
