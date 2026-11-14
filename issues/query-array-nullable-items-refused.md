# A query array whose items are `nullable` fails the whole document

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** render (`OpenAPI generation failed:`)
**Impact:** 1 of 1,547 documents, 33 parameters on 12 of 158 operations (unicourt.com)
**Source spec valid:** yes — `nullable: true` on `items` is valid OpenAPI 3.0; what a null item means in a query string is not defined

## Symptom

```
OpenAPI generation failed:
- GET /pacerCaseLocator/caseSearch/allCourts parameter caseTypeArray (in query, style form, explode true) has no supported serialization for its type, location, `style` and `explode`.
```

## Minimal reproducer

`<scratchpad>/repro/query-array-nullable-items.json`:

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/x": { "get": {
    "operationId": "op",
    "parameters": [ { "name": "caseTypeArray", "in": "query",
      "schema": { "type": "array", "items": { "type": "string", "nullable": true } } } ],
    "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "type": "string" } } } } }
  } } }
}
```

```
worker exit=1 phases: parse pass, typed pass, render fail
- GET /x parameter caseTypeArray (in query, style form, explode true) has no supported serialization for its type, location, `style` and `explode`.
no sources emitted
```

Removing `"nullable": true` makes it pass.

## What the spec says

OAS 3.0.3 §4.7.12.2 defines `form` for an array of primitives (`color=blue&color=black`). RFC 6570 §2.3: "A variable
defined as a list value is considered undefined if the list contains zero members"; undefined *members* are not part
of the model — a list is a list of strings. There is no representation for a null member, so a client can only omit
it or send an empty value, and the two are different requests.

In unicourt.com the flag is almost certainly generator noise (every array parameter of the document has
`items.nullable: true`), not a statement that the server accepts null entries.

## Root cause

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/ParameterStyle.kt:76-78`:

```kotlin
model is Model.Collection ->
    style != Style.deepObject && !(delimited && explode) &&
        !model.inner.isNullable && model.inner.formModel()?.isFormScalar() == true
```

`!model.inner.isNullable` rejects the parameter; `GenerationDiagnostics.kt:281-294` makes it an `Error`. The same
condition exists for form fields (`RequestBody.kt:242-244`) and maps (`ParameterStyle.kt:87`).

## Correct final API

The nullable item adds nothing a client can send, so the accurate signature drops it at the parameter boundary and
says so:

```kotlin
public suspend operator fun invoke(caseTypeArray: List<String>? = null, ...)
// wire: caseTypeArray=a&caseTypeArray=b
```

plus a `Warning`: `GET /x parameter caseTypeArray: items are nullable, but a null item has no form serialization;
the parameter is generated with non-null items.` This keeps "no silent drops" (the narrowing is reported) without
failing 158 operations for it.

The alternative, `List<String?>` with null items skipped at runtime, is a silent drop at call time and is not
recommended.

## Fix slice

- typed: in `isSerializableParameter` / `isFormUrlEncodable`, accept a collection (and map) of nullable scalars, and
  record on `Route.Input` that item nullability was narrowed (so the renderer can type it non-null and the
  diagnostics can report it). Smallest version: strip `isNullable` from `inner` when building the `Route.Input` for
  a non-JSON location.
- renderer: new `Warning` in `GenerationDiagnostics.kt`; no serialization change.
- Open decision: Warning vs keeping the Error. Given the only occurrence in 1,547 documents is generator noise, the
  Warning is the pragmatic choice.

## Proof

- typed `RouteSpec`: array of nullable strings in query is supported and typed `List<String>` (failing first).
- renderer golden: signature and the warning text.
- integration-tests coverage: not checked in detail; a scan of `github.json`, `openai.yaml` and `youtrack.json`
  for non-scalar or unusual query parameter shapes found no array with nullable items, so no regression coverage.

## Affected documents

unicourt.com [cf72223edc58a6f71cd1] — 1 in total.
