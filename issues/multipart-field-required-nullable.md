# A required, nullable multipart field is appended without a null check

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 1 of 1,547 documents (nordigen.com); no excluded errors (it also has the `Slash` owner-class error)
**Source spec valid:** yes — `required` plus `nullable: true` is legal (OAS 3.0.3 §4.7.24)

## Symptom

```
Api.kt:922:34: error: argument type mismatch: actual type is 'String?', but 'String' was expected.
    append("redirect", redirect)
```

## Minimal reproducer

`/private/tmp/claude-501/-Users-simonvergauwen-Developer-OpenAPI-kt/a0df68b9-ccc6-4d18-981e-753c6b12368d/scratchpad/repro/multipart-field-required-nullable.json`

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},
 "paths":{"/payments":{"post":{
   "requestBody":{"required":true,"content":{"multipart/form-data":{"schema":{"$ref":"#/components/schemas/Payment"}}}},
   "responses":{"204":{"description":"ok"}}}}},
 "components":{"schemas":{"Payment":{"type":"object","required":["redirect"],"properties":{
   "redirect":{"type":"string","nullable":true}}}}}}
```

Observed:

```kotlin
public suspend operator fun invoke(redirect: String?) {
  val response = client.post("payments") {
    setBody(MultiPartFormDataContent(formData {
      append("redirect", redirect)
    }))
Payments.kt:22:30: error: argument type mismatch: actual type is 'String?', but 'String' was expected.
```

The same schema as `application/x-www-form-urlencoded` compiles (`if (redirect != null) { append(...) }`).

## What the spec says

The property must be present and may be `null`. Multipart has no representation of `null` (OAS 3.0.3 §4.7.14.1 only maps values to parts), so "present but null" cannot be sent faithfully.

## Root cause

Traced. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt:1113`: the multipart writer guards with `if (!formData.isRequired)` only. The form-urlencoded writer uses `mayBeNull = formData.type.isNullable || !formData.isRequired` (`ImplRenderer.kt:1096`), and parameters use `mayBeNull` at `:925`.

## Correct final API

`invoke(redirect: String?)` stays (required: no default). On the wire a `null` omits the part, the same as the form-urlencoded writer does today. Open decision: omit vs an empty part; either way the choice should be the same for form and multipart and stated in KDoc.

## Fix slice

- Renderer, one line: use `formData.type.isNullable || !formData.isRequired` at `ImplRenderer.kt:1113`.

## Proof

- Failing test first: golden case of the reproducer; MockEngine test that `null` sends no `redirect` part and a value sends one.
- Integration tests: no required nullable multipart property in `github.json` / `youtrack.json` (script); openai (23 multipart mentions) compiles. Not covered.

## Affected documents

nordigen.com [e7bb45654e2703c425cc]. Total 1 (only `argument type mismatch` on an `append("...")` line in the census).
