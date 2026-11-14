# A schema with `type: "null"` crashes with "Null should always be resolved ... Please report this bug"

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** typed
**Impact:** 1 of 1,547 documents (vercel.com)
**Source spec valid:** mixed — `{type: "null"}` alone is valid OAS 3.1; `{type: "null", oneOf: [...non-null...]}` is legal syntax that no value satisfies

## Symptom

```
IllegalStateException: Null  should always be resolved to result in nullable types. Please report this bug.
{"oneOf":[{"type":"string"},{"type":"number"}],"type":"null"}
	at io.github.nomisrev.openapi.pipeline.SchemaTransformerEngine.transformNormalized(SchemaTransformerEngine.kt:71)
```

## Minimal reproducer

`<scratchpad>/repro/null-type-with-oneof.json` (vercel's shape, 17 places such as `aliasAssigned`, `gitRepo`)

```json
{
  "openapi": "3.1.0",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/thing": { "get": { "responses": { "200": { "description": "ok", "content": { "application/json": {
    "schema": { "type": "object", "properties": {
      "value": { "type": "null", "oneOf": [{ "type": "string" }, { "type": "number" }] },
      "id": { "type": "string" } } } } } } } } } }
}
```

Observed:

```
worker exit=1 phases: parse pass, typed fail
java.lang.IllegalStateException: Null  should always be resolved to result in nullable types. Please report this bug. {"oneOf":[{"type":"string"},{"type":"number"}],"type":"null"}
```

Variants of the `value` schema (files `null-type-*.json`, `null-enum-only.json` in `<scratchpad>/repro/`):

| `value` schema | Result |
| --- | --- |
| `{"type":"null","oneOf":[{string},{number}]}` | crash (above) |
| `{"type":"null"}` (vercel has it once: `joinedFrom.ssoUserId`) | same crash, message ends `{"type":"null"}` |
| `{"type":"array","items":{"type":"null"}}` | same crash |
| `{"oneOf":[{string},{number},{"type":"null"}]}` | passes (nullable union) |
| `{"type":["null"]}` | passes, but generates a `sealed interface Value` with no cases (nothing can be decoded) |
| `{"enum":[null]}` | passes, but generates `enum class Value { @SerialName("null") Null("null") }` — the string `"null"`, not JSON `null` |

The last two are wrong output found on the way, not census failures; the last one is the parser's enum
stringification (`exploration/PARSER-AUDIT.md` §2).

## What the spec says

- OAS 3.1.0 §4.8.24 Schema Object uses JSON Schema 2020-12: `type: "null"` is a type like any other; the only valid
  instance is `null`. As a property schema it means "always null when present".
- `type` and `oneOf` are both applied. `{type: "null", oneOf: [{type: string}, {type: number}]}` requires `null` and
  exactly one of string/number: unsatisfiable. Vercel's generator emits it for "string or number or null"; the valid
  spelling is the fourth row.

## Root cause (traced)

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/pipeline/SchemaTransformerEngine.kt:70-72`:

```kotlin
if (schema.schema.type == Schema.Type.Basic.Null) {
    error("Null  should always be resolved to result in nullable types. Please report this bug. ${schema.schema}")
}
```

The engine assumes `null` only appears where something else removes it first: as a `oneOf`/`anyOf` branch
(`OneOfNullableFlattenPlugin`, `AnyOfNullableFlattenPlugin`, `AllOfNullableFlattenPlugin`) or in a type array
(`flattenSingleTypeArray`, `pipeline/plugins/typed/TypeArrayPlugin.kt:23-34`, which only rewrites arrays with exactly
one non-null type). A scalar `type: "null"` on a property, an array item or next to a composite reaches the
pre-check. There is no model for "the null type": `Model` has no such case and `Primitive.kt:33` also rejects it.

## Correct final API

`{type: "null"}`: a property whose only value is `null`.

```kotlin
public val ssoUserId: JsonNull? = null   // kotlinx.serialization.json.JsonNull; absent or null
```

A dedicated `Model.Null` rendered as `JsonNull` (or `Nothing?` with a custom serializer) is a design decision; it
must decode `null`, reject anything else, and encode `null`.

`{type: "null", oneOf: [string, number]}`: design decision.

- Strict: unsatisfiable, so an error diagnostic for the schema (and the operations that need it).
- Recommended: read `type: "null"` beside `oneOf`/`anyOf` as an extra null branch — the same meaning 3.0's
  `nullable: true` beside `oneOf` already has in this code base (`SchemaTransformerEngine.kt:75-79`) — and warn:

```kotlin
public val value: Value? = null          // sealed interface Value { CaseString, CaseDouble }
// warning: .../properties/value: type "null" next to oneOf accepts no value as written; generated as a nullable union.
```

Wire: `"a"`, `1.5` and `null` all decode.

## Fix slice

- typed: add the null model (`Model.kt`) and a plugin for scalar `type: "null"`; remove the pre-check or turn it into
  a named error with the schema's naming context.
- typed: normalise `{type: "null", oneOf|anyOf: [...]}` in the engine next to `flattenSingleTypeArray`
  (`SchemaTransformerEngine.kt:46`) to `{oneOf: [..., {type: "null"}]}` and record the warning.
- renderer: render the null model; `GenerationDiagnostics.kt` for the warning.
- Separate, smaller: `type: ["null"]` should be the same null model, not a sealed interface without cases.

## Proof

- Failing tests first: typed tests for the three crashing rows.
- Golden `ClientSpec` for `{type: "null"}` and for the vercel shape; example round-trip with `null`, a string and a
  number.
- Integration tests would not catch a regression: `openai.yaml` (3.1.0) has 391 `type: "null"` schemas, every one a
  bare `oneOf`/`anyOf` branch (checked by script); `github.json` and `youtrack.json` are 3.0 and have none.

## Affected documents

1 total. vercel.com [f9815cb31c0e7dc128b3] (17 schemas with `type: "null"` + `oneOf`, 1 bare `type: "null"` property,
7 as union branches).
