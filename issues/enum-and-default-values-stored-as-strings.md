# Integer, number and boolean enums are generated as string-serialized Kotlin enums (enum and default values are parsed as strings)

**Status:** fixed in `5854454c`, `5d203102` (typed enum serializer), `1b89168f`, `f39eb298` (parser keeps JSON types; a string that reads as the declared type is accepted with a warning) and `87aac23f` (YAML scalars by the core schema). Still open: an enum with a `null` member next to typed members (`[1, 2, null]`) stays string-serialized; untyped enums mixing JSON types fall back to free-form JSON with a warning; YAML cannot tell `'1'` from `1` because the YAML library drops quoting, so an untyped YAML enum of quoted numbers becomes an integer enum; JSON `{type: string, default: 5}` is still accepted silently as text.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming); parser half pinned by `CorpusRoundTripAuditTest`
**Stage:** wrong output (parse loses the JSON type; the generated enum sends and expects JSON strings)
**Impact:** 103 of 1,547 documents declare an enum on a `type: integer`, `number` or `boolean` schema (1,208 locations). 28 of them currently pass every phase including compilation and ship the wrong wire format silently; the rest fail earlier for other reasons and will hit this once those are fixed.
**Source spec valid:** yes

## Symptom

For `{"type": "integer", "enum": [300, 600, 1200]}` the generated model is a plain `@Serializable enum class`, which
kotlinx.serialization encodes as a JSON **string** and refuses to decode from a JSON number:

```
encode: {"rate":"300","flag":"true","prio":"2","scale":"0.5"}
decode {"rate":300}   -> JsonDecodingException: Unexpected JSON token at offset 8: Expected quotation mark '"', but had '3' instead at path: $.rate
decode {"flag":true}  -> JsonDecodingException: Unexpected JSON token at offset 8: Expected quotation mark '"', but had 't' instead at path: $.flag
decode {"scale":0.5}  -> JsonDecodingException: Unexpected JSON token at offset 9: Expected quotation mark '"', but had '0' instead at path: $.scale
decode {"rate":"300"} -> Meter(rate=300, ...)
```

(Output of a small `main` compiled against the generated sources of the reproducer with the census runner's
classpath; default `Json`.) So a request body sends `"rate":"300"` where the API declares an integer, and every
response carrying the real value `300` throws. Query/path parameters are unaffected (their wire form is text).

Census breakdown of typed non-string enums (documents / locations; outcome of those documents today):

| schema | docs | locations | outcome of the documents |
| --- | --- | --- | --- |
| `type: integer` with numeric members | 73 | 901 | 26 compile-pass, 21 compile-fail, 18 typed-fail, 4 parse-fail, 3 emit-fail, 1 render-fail |
| `type: boolean` with boolean members | 31 | 251 | 3 compile-pass, 10 compile-fail, 10 typed-fail, 6 emit-fail, 2 render-fail |
| `type: number` with numeric members | 7 | 56 | 4 compile-fail, 2 typed-fail, 1 render-fail |

Union: 103 documents, 28 of which are `compile: pass`.

## Minimal reproducer

`<scratchpad>/repro/integer-enum.json` (the `Baudrate` schema is mbus.local `components/schemas/baudrate`, the one
`CorpusRoundTripAuditTest` cites):

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/meter": { "get": {
    "operationId": "op",
    "parameters": [
      { "name": "rate", "in": "query", "schema": { "$ref": "#/components/schemas/Baudrate" } },
      { "name": "limit", "in": "query", "schema": { "type": "integer", "default": 25 } },
      { "name": "ratio", "in": "query", "schema": { "type": "number", "default": 1.5 } },
      { "name": "on", "in": "query", "schema": { "type": "boolean", "default": true } } ],
    "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Meter" } } } } } } } },
  "components": { "schemas": {
    "Baudrate": { "type": "integer", "enum": [300, 600, 1200], "default": 300 },
    "Meter": { "type": "object", "properties": {
      "rate": { "$ref": "#/components/schemas/Baudrate" },
      "flag": { "type": "boolean", "enum": [true] },
      "prio": { "type": "integer", "enum": [1, 2], "default": 2 },
      "size": { "type": "integer", "default": 10 },
      "scale": { "type": "number", "enum": [0.5, 1.0, 1.5] },
      "mixed": { "enum": [1, "1", true, "true"] },
      "tags": { "type": "array", "items": { "type": "integer" }, "default": [1, 2] } } } } }
}
```

```
worker exit=0 phases: parse, typed, render, emit pass
diagnostics: []
compile exit=0
```

Generated (imports removed):

```kotlin
@Serializable
public enum class Baudrate { `300`, `600`, `1200`, }

public data class Meter(
  public val rate: Baudrate? = null,
  public val flag: Flag? = null,
  public val prio: Prio? = null,
  public val size: Long? = null,
  public val scale: Scale? = null,
  public val mixed: Mixed? = null,
  public val tags: List<Long>? = null,
) {
  @Serializable public enum class Flag(public val `value`: String) { @SerialName("true") True("true"), ; }
  @Serializable public enum class Mixed(public val `value`: String) { `1`("1"), @SerialName("true") True("true"), ; }
  @Serializable public enum class Prio { `1`, `2`, }
  @Serializable public enum class Scale(public val `value`: String) {
    @SerialName("0.5") `05`("0.5"), @SerialName("1.0") `10`("1.0"), @SerialName("1.5") `15`("1.5"), ; }
}

// api/Meter.kt
public suspend operator fun invoke(rate: Baudrate? = Baudrate.`300`, limit: Long? = 25L, ratio: Double? = 1.5, on: Boolean? = true)
```

Three separate observations in that output:

1. Typed enums serialize as strings (the bug of this report).
2. The untyped enum `[1, "1", true, "true"]` has four distinct JSON values and is generated with two constants:
   the number `1` and the string `"1"` are indistinguishable after parsing.
3. Scalar defaults survive (`25L`, `1.5`, `true`) because `typed` re-parses the stored text against the declared
   type. That re-parse is where the census errors `Default value X is not a Integer` / `Enum value X is not a
   Integer` come from (about 11 documents; owned by `issues/default-or-enum-value-not-of-declared-type.md` — cross-reference only, not counted here).

A second reproducer, `<scratchpad>/repro/default-null-string.json`, shows the same string model confusing JSON
`null` with the text `null`: a query parameter `{"type": "string", "default": "null"}` is generated as
`s: String? = null` (the default is dropped), identical to `{"type": "string", "nullable": true, "default": null}`.

## What the spec says

OAS 3.0.3 §4.7.24 / JSON Schema Validation §5.20: enum members "MAY be of any type"; an instance validates only if
it is *equal* to a member, and JSON equality is type-sensitive (`300` is not `"300"`). §4.7.24.1: "`default` ...
Unlike JSON Schema, the value MUST conform to the defined type for the Schema Object defined at the same level. For
example, if `type` is `string`, then `default` can be `"foo"` but cannot be `1`." So an integer enum is a set of
JSON numbers, and the wire value must be a JSON number.

## Root cause

Traced through all three layers:

1. parser — `parser/src/commonMain/kotlin/io/github/nomisrev/openapi/parser/Schema.kt:90`
   `val enum: List<String?>? = null`; `Schema.kt:70,75` `example: ExampleValue?`, `default: DefaultValue?` with
   `ExampleValue.Serializer` (`ExampleValue.kt:62-74`) storing `JsonPrimitive.content` for scalars
   (`is JsonPrimitive -> Single(json.content)`, which also turns `JsonNull` into the text `null`) and stringified
   JSON for objects. Pinned by `CorpusRoundTripAuditTest."numeric enum members currently become strings"`,
   `"numeric defaults currently become strings"`, `"object examples currently become JSON encoded strings"`
   (`parser/src/commonTest/.../CorpusRoundTripAuditTest.kt:18-37`).
2. typed — `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/Enum.kt:30-64` re-parses each
   string against the inner model and produces typed `Model.EnumValue.Int/Long/Float/Double/Boolean`, so the type
   information is recovered for well-typed enums (`Model.Enum.inner`, `Model.kt:464-472`). `Enum.kt:32` and `:67`
   treat the text `null` (any case) as JSON null.
3. renderer — `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/EnumRenderer.kt:36-37` always emits
   `TypeSpec.enumBuilder(...).addAnnotation(Serializable::class)`, with an optional `value: String` constructor
   (`:40-54`) and `@SerialName(rawValue)` (`:72`). `Model.Enum.inner` and the typed `EnumValue` are not consulted
   for serialization, so every enum uses kotlinx's default enum serializer, which reads and writes JSON strings.

Layer 3 is the cause of the wrong wire format; layer 1 is the cause of observation 2, of the `null` confusion, and of
the typed-layer re-parse errors.

## Correct final API

```kotlin
@Serializable(with = Baudrate.Serializer::class)
public enum class Baudrate(public val value: Int) {
  `300`(300), `600`(600), `1200`(1200);
  internal object Serializer : KSerializer<Baudrate> { /* encodeInt(value) / decodeInt() -> entry, unknown value -> SerializationException */ }
}
```

- Wire: `{"rate":300}` both ways; `{"flag":true}`; `{"scale":0.5}`.
- `value` has the declared Kotlin type (`Int`/`Long`/`Double`/`Boolean`), so parameters keep using `value.toString()`.
- A boolean enum with both members is just `Boolean`; with one member (`enum: [true]`) it is a constant the model
  can represent as the single-entry enum above or as `Boolean` with a diagnostic — open decision.
- An untyped enum of mixed JSON types keeps all distinct members; the Kotlin shape is a union of the primitive
  cases, or `JsonPrimitive` with a diagnostic. It must not merge `1` and `"1"`.
- A string default `"null"` stays the string `"null"`.

## Fix slice

- renderer (smallest fix for the wire bug, no parser change needed): in `EnumRenderer`, when `inner` is not a string
  model, emit the typed `value` property and a custom `KSerializer` (the project already generates a nested `Serializer` object
  for open enums, `OpenEnumParts.kt:84-125`; the same pattern applies). Entry naming for numeric members
  (`` `300` `` today) is unchanged.
- parser: `enum: List<JsonElement>?`, `default`/`example` as `JsonElement` (YAML via `JsonElementWithYamlSerializer`,
  as `const` already does at `Schema.kt:91`). Then `typed/.../Enum.kt` matches on the JSON type instead of
  re-parsing text, which removes the `null`/`"null"` and `1`/`"1"` ambiguities and turns "Enum value BASIC is not a
  Integer" into a precise, pointer-carrying diagnostic. Same parser change as
  `issues/enum-structured-members-fail-parse.md`.
- Open decisions: strict vs lenient decoding of an integer enum sent as a string by a sloppy server; representation
  of single-value boolean enums; whether mixed-type enums become a union.

## Proof

- Failing test first (renderer golden, `ClientSpec` style): the reduced `Baudrate` schema renders a typed enum with
  the custom serializer.
- Runtime test (kotlinx.serialization only, no HTTP): `Json.encodeToString(Meter(rate = Baudrate.`300`))` equals
  `{"rate":300}` and `{"rate":300}` decodes. Today both assertions fail as shown under **Symptom**.
- Parser: replace the three `CorpusRoundTripAuditTest` characterizations with preservation assertions
  (`[300,600]` stays `[300,600]`, `25` stays `25`, the object example stays an object).
- integration-tests coverage: `openai.yaml` has two integer enums
  (`FineTuneChatCompletionRequestAssistantMessage.weight: [0, 1]`, `RealtimeAudioFormats...rate: [24000]`) and
  `github.json` has 4 boolean enums, so the construct is present, but the integration projects only compile the
  generated code (`src/main`, no wire assertions were found for these types) and the wrong serializer compiles
  fine. They would not catch this bug or a regression of its fix.

## Affected documents

Currently passing every phase with a string-serialized typed enum, 28 in total: credas.co.uk:pi [483e663294cb0b58f3ae]
(55 locations; e.g. generated `model/CredasApiModelsDataCheckCheckRemark.kt` has an enum with constants `` `1` ``, `` `2` ``, ...),
signl4.com [8e317a61f72900d8e3a0], gov.bc.ca:router [2da1f9cf658928b79f75], bigredcloud.com [45fd1cd411fe627fc894],
netboxdemo.com [d4aff487a182c17d8090], increase.com [df0448ba21873977fda4] (`model/Error.kt`: `` `404` ``, `` `403` ``, ...),
dracoon.team [ac22555aeb890d5e2809], netbox.dev [c84a2ca73e4ca0877f5e], truesight.local [30405a5788de3fdbf6fe],
parliament.uk:members [38e45ea339df4260cbd6]. (mbus.local [072ee2df8c320e3c79db], the document the characterization
test cites, uses its `baudrate` enum only as a parameter, where the text form is correct.) Whether each location is in
a JSON body or only in a parameter was not classified; body occurrences are confirmed for credas and increase.
The full list is reproducible with the scan in
`<scratchpad>/c5/typed_enums.py` (walk every schema, select `enum` arrays whose members are JSON numbers/booleans under
`type: integer|number|boolean`): 103 documents, 1,208 locations. The raw parser comparison in
`exploration/PARSER-AUDIT.md` §2-3 additionally counts changed `default` values in 327 documents and changed
`example` values in 576 documents; those are round-trip losses (`toJson()` emits strings) and, for scalar defaults
on typed schemas, do not change the generated client.
