# A form field that is a union of scalars (`anyOf [integer, string]`) is not generated, although the same query parameter is

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** render — `Warning: Skipping unsupported form overload` (an `Error` when the form is the only body)
**Impact:** 2 of 1,547 documents, 66 fields (telegram.org: `chat_id` and similar on 38 operations; gsmtasks.com: 28 `oneOf [Enum, BlankEnum]` fields). Both have JSON alternatives, so the documents still generate.
**Source spec valid:** yes

## Symptom

```
Warning: Skipping unsupported form overload for POST /sendMessage (application/x-www-form-urlencoded) because fields [chat_id] have no supported form serialization for their type and encoding (`style`, `explode`).
```

`chat_id` is `anyOf: [{type: integer}, {type: string}]`. In gsmtasks.com the shape is the Django REST "nullable
enum" idiom: `country_code: {oneOf: [{$ref: CountryCodeEnum}, {$ref: BlankEnum}]}`.

## Minimal reproducer

`<scratchpad>/repro/form-union-of-scalars.json`:

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/sendMessage": { "post": {
    "operationId": "op",
    "requestBody": { "required": true, "content": { "application/x-www-form-urlencoded": { "schema": {
      "type": "object", "required": ["chat_id", "text"],
      "properties": { "chat_id": { "anyOf": [ { "type": "integer" }, { "type": "string" } ] }, "text": { "type": "string" } } } } } },
    "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "type": "string" } } } } }
  } } }
}
```

```
worker exit=0 phases: parse, typed, render, emit pass
diagnostics: [{"severity":"Error","message":"POST /sendMessage (application/x-www-form-urlencoded) contains form fields [chat_id] that have no supported form serialization for their type and encoding (`style`, `explode`), and no alternative supported request body variant exists."}]
compile exit=0
```

(The harness does not fail on `Error` diagnostics; the Gradle task does.)

Control `<scratchpad>/repro/query-union-of-scalars-ok.json` — the same schema as a query parameter — generates:

```kotlin
when (chatId) {
  is ChatId.CaseLong -> { url.encodedParameters.append("chat_id", chatId.value.toString()) }
  is ChatId.CaseString -> { url.encodedParameters.append("chat_id", chatId.value.encodeURLParameter()) }
}
```

`<scratchpad>/repro/form-nullable-enum.json` (`oneOf [CC enum, Blank enum]`) reproduces the gsmtasks shape; it also
hits an unrelated compile error in the generated `Blank` enum (an enum whose only value is `""`), which is
`issues/enum-empty-identifier.md`, not this report.

## What the spec says

OAS 3.0.3 §4.7.15 Encoding Object: a form field is serialized like a query parameter of its `style`/`explode`
(default `form`, `explode: true`), and §4.7.12.2 defines `form` for primitives as the value itself. Every
alternative of `anyOf [integer, string]` is a primitive, so the field is `chat_id=<value>` whichever alternative the
value is. Nothing is undefined.

## Root cause

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/RequestBody.kt:237-255` (`isFormUrlEncodable`) has
branches for scalars, `Model.Collection` and `Model.Object` only; a `Model.Union` reaches `else -> false`
(line 253). The parameter rule, `ParameterStyle.kt:89-94`, does have a `Model.Union` branch
(`cases.all { it.model.isUnionParameterCase() && ... }`), which is why the query control passes. The two predicates
were written separately and diverged. For gsmtasks, even the parameter rule would refuse it, because enum cases are
excluded from `isUnionParameterCase` (`issues/parameter-union-with-enum-case-refused.md`).

## Correct final API

```kotlin
public suspend operator fun invoke(chatId: ChatId, text: String): String
// wire: chat_id=42&text=hi           (ChatId.CaseLong(42))
// wire: chat_id=%40channel&text=hi   (ChatId.CaseString("@channel"))
```

The same sealed union type the JSON overload uses, written as the value of the case it holds. For
`oneOf [CountryCodeEnum, BlankEnum]`: the enum's wire value (`country_code=EE` or `country_code=`).

## Fix slice

- typed: give `isFormUrlEncodable` the union branch, sharing one helper with `isSerializableParameter` so the two
  cannot drift again; include `Model.Enum` as a union case in that helper.
- renderer: `addFormFieldCode` (`ImplRenderer.kt`) needs the `when (value) { is CaseX -> append(...) }` shape that
  `ParameterSerialization.kt` already emits for query parameters.
- Open decision: none on the wire format. Naming of case classes follows `UnionRendererNaming`.

## Proof

- typed `RouteSpec`: `unsupportedFields` is empty for a form with an `anyOf [integer, string]` field (failing first).
- renderer golden + MockEngine runtime test for both cases.
- integration-tests coverage: `openai.yaml` has union-typed form fields, but in multipart bodies and with an
  untyped member; `github.json` and `youtrack.json` have none in urlencoded bodies. Not covered.

## Affected documents

telegram.org [19696a2d729d6669b650], gsmtasks.com [daf353ec0ddccdfbd4fa] — 2 in total, warnings only. Fixing this
alone does not restore the telegram form overloads that also carry object fields
(`issues/form-field-object-without-encoding-refused.md`).
