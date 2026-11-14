# A `default` or `enum` value that does not match the declared `type` aborts generation

**Status:** partly addressed in `f39eb298`: a string that reads as the declared type (`"7"`, `"true"`) is accepted with a warning (510 such values in the census), and rejections now name the schema location. Still open: values that cannot be read as the declared type (`inf`, `BASIC`, `current`) abort the whole document — 12 documents in the 2026-11-14 re-run.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** typed
**Impact:** 11 of 1,547 documents — 8 by `default` (openai 1.2.0, graphhopper, motaword, gov.bc.ca news, botify, paypi, vtex x2), 3 by `enum` (googleapis cloudidentity, visualstudio, probely)
**Source spec valid:** no — all 11 are OAS 3.0.x and each offending value contradicts its schema's `type`

## Symptom

```
IllegalArgumentException: Default value inf is not a Integer.
IllegalArgumentException: Default value  is not a Integer.
IllegalArgumentException: Default value 1 is not a Boolean.
IllegalArgumentException: Enum value BASIC is not a Integer.
```

No operation, schema name or JSON pointer. The whole document fails on one annotation.

## Minimal reproducer

`<scratchpad>/repro/default-not-of-type-inf.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/posts": { "post": {
    "parameters": [{ "name": "count", "in": "query", "schema": { "type": "integer" } }],
    "requestBody": { "content": { "application/json": { "schema": { "type": "object",
      "properties": { "max_tokens": { "type": "integer", "default": "inf" } } } } } },
    "responses": { "204": { "description": "ok" } }
  } } }
}
```

Observed: `worker exit=1 ... typed fail ... java.lang.IllegalArgumentException: Default value inf is not a Integer.`

Same skeleton, other values (files in `<scratchpad>/repro/`):

| File | Schema | Result |
| --- | --- | --- |
| `default-not-of-type-empty.json` | `{type: integer, format: int32, default: ""}` | `Default value  is not a Integer.` |
| `default-not-of-type-boolean.json` | `{type: boolean, default: 1}` | `Default value 1 is not a Boolean.` |
| `default-not-of-type-int64-overflow.json` | `{type: integer, format: int64, default: 9223372036854776000}` | `Default value 9223372036854776000 is not a Integer.` |
| `enum-not-of-type.json` | `{type: integer, enum: ["BASIC", "FULL"]}` | `Enum value BASIC is not a Integer.` |
| `default-wrong-json-type-accepted.json` | `{type: integer, default: "7"}` and `{type: string, default: 5}` | **passes**: generates `count: Long? = 7L` |
| `default-valid.json` | `{type: integer, format: int32, default: 10}` | passes: `count: Int? = 10` |

## What the spec says

- OAS 3.0.3 §4.7.24 Schema Object, `default`: "Unlike JSON Schema, the value MUST conform to the defined type for the
  Schema Object defined at the same level. For example, if `type` is `string`, then `default` can be `"foo"` but
  cannot be `1`." All eight `default` documents violate this.
- `enum` with `type`: JSON Schema applies both, so `{type: integer, enum: ["BASIC","FULL"]}` accepts no value. Legal
  syntax, unsatisfiable schema. Google's real API takes the strings; visualstudio's values are `"0 (Unknown)"` labels.
- OAS 3.1 drops the MUST: `default` is a JSON Schema annotation there. A 3.1 document with a mismatched default is
  valid, so "reject" cannot be the general answer.

What each source actually wrote:

| Document | Pointer (shortened) | Declaration |
| --- | --- | --- |
| openai.com [9c5067bebc6dd5736296] | `CreateChatCompletionRequest/properties/max_tokens` | `{type: integer, default: "inf"}` |
| graphhopper.com [f099a6c66320e9be8695] | `Service/properties/max_time_in_vehicle` (+3) | `{type: integer, format: int64, default: 9223372036854776000}` (2^63 after a double round-trip; Long.MAX_VALUE + 1) |
| motaword.com [7ab666d20e8d3f3df9ea] | query parameters (6) | `{type: boolean, default: 1}` / `default: 0` |
| gov.bc.ca:news [604ac133531b6f2d6348] | query parameters (4) | `{type: integer, format: int32, default: ""}` |
| vtex.local:Price-Simulations [bfd09d72d08e3acf1da1] | response property `id` (3) | `{type: integer, default: ""}` |
| vtex.local:Customer-Credit-API [e2ab914b8f4db862046f] | `creditLimit` (3) | `{type: integer, default: "100.0"}` |
| botify.com [bfe154ad4acd41bea54b] | `CsvExportStatus/properties/area` | `{type: integer, format: int64, default: "current"}` |
| paypi.dev [dae0e5926d82a364d7cd] | response property `message` (2) | `{type: boolean, default: "Verification email sent"}` |
| googleapis.com:cloudidentity [5fbb1d65026411989b34] | query parameter `view` | `{type: integer, enum: ["BASIC","FULL"]}` |
| visualstudio.com [b4364fa718a3655c5950] | 17 schemas, e.g. `BillableOwnerType` | `{type: integer, format: int32, enum: ["0 (User)","1 (Organization)"]}` |
| probely.com [e4fd363fe7102f41fa87] | `Activity/properties/change` | `{type: integer, enum: ["created","comment","found","changed"]}` |

## Root cause (traced)

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/Primitive.kt:94-106` — `default(label, onSingle)`
  does `requireNotNull(onSingle(default.value)) { "Default value ${default.value} is not a $label." }` with
  `String::toIntOrNull` / `toLongOrNull` / `toBooleanStrictOrNull` (lines 38-71).
- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/Enum.kt:30-64` — `toEnumValue` does the same
  per enum value; `enumDefault` (lines 66-73) routes an enum's default through it too.

Both treat an inconsistent annotation as a fatal error and have no schema location to report.

### Relation to the parser storing defaults and enums as strings (`exploration/PARSER-AUDIT.md` §2-3)

That data loss is **not** the root cause of these 11 failures: every one is wrong in the source JSON, checked
value by value (table above). It is the reason the check is unreliable in the other direction:

- typed only sees strings (`ExampleValue.Single(value: String)`, `Schema.enum: List<String?>`), so it cannot tell
  `default: "7"` from `default: 7`. `default-wrong-json-type-accepted.json` shows a string default on an integer
  being accepted and rendered as `7L`. vtex Customer-Credit has `{type: integer, default: "0"}` right next to the
  failing `"100.0"`; the first passes.
- `default: null` and `default: "null"` are indistinguishable (`Primitive.kt:100` compares the text with `"null"`).
- `9223372036854776000` reaches typed as a string that `toLongOrNull` rejects; with typed JSON values the model could
  say "integer literal outside int64".

So fixing the parser makes this check exact; it does not make these documents pass.

## Correct final API

`default` is an annotation: it never changes what is valid on the wire. A default that is not a value of the type
cannot be a Kotlin default argument, so there is none, and the user is told:

```kotlin
// {type: integer, default: "inf"}
public val maxTokens: Long? = null
// warning: #/components/schemas/CreateChatCompletionRequest/properties/max_tokens: default "inf" is not an
//          integer; the default is ignored.
```

Wire: the property is omitted unless set — the server applies its own default.

For `enum` on a contradicting `type` no value satisfies the schema, so something must give. Recommended: trust the
`enum` (the literal values are what goes on the wire), type the enum by its values, and warn:

```kotlin
// {type: integer, enum: ["BASIC", "FULL"]}
@Serializable public enum class View { @SerialName("BASIC") BASIC, @SerialName("FULL") FULL }
// warning: ...parameters/3/schema: enum values are strings but type is integer; generated as a string enum.
```

Alternative (stricter): an error diagnostic scoped to the operations that use the schema. Either way, not a
document-wide exception.

## Fix slice

- parser (prerequisite for an exact check, tracked in PARSER-AUDIT): keep `default`/`enum`/`const` as `JsonElement`.
- typed: carry "declared default rejected, reason, pointer" on the model (as `Model.DiscriminatedObject.PropertyOverride`
  does for overrides) instead of throwing in `Primitive.kt:102` and `Enum.kt:35-59`.
- renderer, `GenerationDiagnostics.kt`: one warning per rejected default / retyped enum.
- Design decisions: (1) enum vs type: trust enum (recommended) or error; (2) int64 default out of range (graphhopper):
  ignore with warning, do not clamp; (3) boolean `default: 0/1` (motaword): ignore with warning, do not coerce —
  coercion is a guess; (4) whether 3.0 documents get an error and 3.1 a warning, or both a warning.
- The messages need the JSON pointer whatever is decided ("not a Integer" -> "not an integer").

## Proof

- Failing tests first: typed tests for each row of the reproducer table; a renderer test asserting the warning text.
- Golden `ClientSpec`: the `max_tokens` property without default; the string enum for `view`.
- Parser test (separate bug): `{type: integer, default: "7"}` must stay a JSON string through decode/encode.
- Integration tests would not catch a regression: `openai.yaml` in the repo is a newer OpenAI spec (3.1.0) without
  `default: inf`; scripted scan of `github.json`, `youtrack.json`, `openai.yaml` finds no integer/boolean schema with
  a mismatched default and no integer enum with non-integer values.

## Affected documents

11 total, all listed in the table above.
