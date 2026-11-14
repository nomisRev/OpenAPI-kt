# Enum values with `:`, `<`, `>`, `;` or `\` produce entry names Kotlin rejects (`aws:kms`, `<=`)

**Status:** fixed on branch openapi-3.2-streaming. The entries of one enum are named together, `enumEntryNames` (`renderer/.../EnumEntryNames.kt`): a character that cannot be in a name is a word break (`aws:kms` is `AwsKms`), a value without letter or digit is spelled out (`<=` `LessThanOrEqual`, `""` `Empty`, `" "` `Blank`), values that still share a name spell their symbols (`last_activity`/`-last_activity` are `LastActivity`/`MinusLastActivity`, `aws:kms`/`aws-kms` are `AwsColonKms`/`AwsMinusKms`), the rest take the first free number from 2; a rename warns. Names that compiled and did not collide are unchanged. The declaration, `@SerialName`, the typed and open enum serializers, the path `when` and every default literal read the one table. Golden `enum/entry-names` and `client/enum-entry-names`, `EnumEntryNamesTest`, `EnumEntryNamesRoundTripTest`, `EnumEntryNamesRuntimeTest`, `EnumEntryNamesWarningsSpec`.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 26 of 1,547 documents (amazonaws.com:s3, :glacier, :macie2, :config, :accessanalyzer, apptigent.com, api2cart.com, nexmo.com:voice, salesforce.local:einstein, alertersystem.com, …); 53 documents declare such a value
**Source spec valid:** yes — `enum` values are arbitrary JSON values

Related, same function: `enum-empty-identifier.md` (values such as `""`, `"-"` that leave no name at all, 16
documents) and `enum-values-with-one-kotlin-name-silently-dropped.md` (values that collide). One naming function
should fix all three.

## Symptom

`"aws:kms"` -> `` `Aws:kms`("aws:kms") `` -> `error: name contains illegal characters: :`. Same for `<`, `<=`, `>`,
`>=` (api2cart.com), `audio/l16;rate=8000` (nexmo.com:voice), and `\`. Kotlin/JVM forbids `. ; [ ] / < > : \` in
back-ticked names; the first five of those cannot survive `toPascalCase`/the bracket stripping, the others do.

This is where the census family "name contains illegal characters" lands in generated `model/` files (16 documents)
and for inline enums in `api/` files (10 documents with `:`, 1 with `<`/`>`); the class-name half of that family is
`composite-path-segments.md`, `path-literal-segment-not-a-kotlin-identifier.md` and
`client-name-from-info-title-not-an-identifier.md`.

Anything else that is not an identifier is kept in back-ticks:
`` `GMTStandardTime(GMT)GreenwichMeanTime:Dublin,Edinburgh,Lisbon,London` ``, `` `1` ``, `` `A+` `` — JVM-only names.

**Compiler note (the apptigent "regression").** apptigent.com [04f32ea3ecb3622265a7] compiled on Oct 31 and fails
now in `model/InputDateTimeConversion.kt`. The generated enum entries are the same in both runs (diffed; only an
unrelated `@EncodeDefault` line changed). The Oct 31 `compile.log` names Kotlin 2.4.10, the census uses 2.4.20.
Compiling the unchanged Oct 31 sources with 2.4.20 gives the same 168 errors. A five-line sample compiled with 2.4.0
(the nearest older compiler in the Gradle cache) and with 2.4.20: 2.4.0 rejects `:` only in a *class* name, 2.4.20
also in *enum entry* names. So this is a stricter compiler, not a generator regression; these names were never safe.

## Minimal reproducer

`<scratchpad>/repro/enum-value-illegal-identifier-characters.json`

```json
{ "openapi": "3.0.3", "info": { "title": "Repro", "version": "1" },
  "paths": { "/items": { "get": { "operationId": "list",
    "parameters": [{ "name": "op", "in": "query", "schema": { "$ref": "#/components/schemas/Operator" } }],
    "responses": { "204": { "description": "ok" } } } } },
  "components": { "schemas": { "Operator": { "type": "string",
    "enum": ["eq", "<", "<=", ">", ">=", "aws:kms", "audio/l16;rate=8000", "a\\b"] } } } }
```
```
worker exit=0 phases: all pass, diagnostics: []
compile exit=1
model/Operator.kt:13:3: error: name contains illegal characters: <.
model/Operator.kt:15:3: error: name contains illegal characters: >.
model/Operator.kt:18:3: error: name contains illegal characters: :.
model/Operator.kt:20:3: error: name contains illegal characters: ;.
model/Operator.kt:22:3: error: name contains illegal characters: \.
```
```kotlin
public enum class Operator(public val `value`: String) {
  @SerialName("eq") Eq("eq"),
  `<`("<"), `<=`("<="), `>`(">"), `>=`(">="),
  @SerialName("aws:kms") `Aws:kms`("aws:kms"),
  @SerialName("audio/l16;rate=8000") `AudioL16;rate=8000`("audio/l16;rate=8000"),
  @SerialName("a\\b") `A\b`("a\\b"),
}
```

## What the spec says

OAS 3.0.3 Schema Object takes `enum` from JSON Schema Validation: elements "might be of any type, including null"
(https://spec.openapis.org/oas/v3.0.3.html#schema-object). Comparison operators, URNs
(`urn:ietf:params:oauth:grant-type:jwt-bearer`), media types and time-zone labels are all legitimate values.

## Root cause (traced)

`renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/NamingUtils.kt:83-98` `toEnumValueName(rawValue)`:
special-cases `*` and `/`, PascalCases, strips one leading `[` / trailing `]`, and otherwise returns
`` "`$sanitized`" `` — whatever characters are left. There is no character filter (compare `toParamName`, `:39-50`,
which replaces `[^A-Za-z0-9_]+`). Callers: `renderer/.../EnumRenderer.kt:68`, `OpenEnumParts.kt:98,112`,
`ObjectRenderer.kt:575`, `CollectionRenderer.kt:45`, `ClientRendererOperations.kt:764`, `ImplRenderer.kt:1249`,
`FactoryRenderer.kt:287,326`.

## Correct final API

Entry names are plain identifiers; the wire value lives in `value` and `@SerialName`, as it already does for
`Eq("eq")`:

```kotlin
public enum class Operator(public val `value`: String) {
  @SerialName("eq") Eq("eq"),
  @SerialName("<") LessThan("<"),
  @SerialName("<=") LessThanOrEqual("<="),
  @SerialName(">") GreaterThan(">"),
  @SerialName(">=") GreaterThanOrEqual(">="),
  @SerialName("aws:kms") AwsKms("aws:kms"),
  @SerialName("audio/l16;rate=8000") AudioL16Rate8000("audio/l16;rate=8000"),
}
```

Note today's `` `<`("<") `` has no `@SerialName` because the escaped name equals the value
(`EnumRenderer.kt:71`); once the name changes the annotation must be emitted. Nothing changes on the wire.

## Fix slice

- renderer: make `toEnumValueName` total over legal identifiers: split on every non-identifier character, spell out
  values without identifier characters (extend the `Star`/`Slash` table: `<` -> `LessThan`, `<=`, `>`, `>=`, `=`,
  `!=`, `+`, …), prefix `_` for a leading digit.
- It has to become a per-enum name table rather than a per-value function, because the stricter rule creates
  collisions (`aws:kms` vs `aws-kms`) — see `enum-values-with-one-kotlin-name-silently-dropped.md`.
- Open decisions: names for symbolic values; whether `"1"` stays `` `1` `` or becomes `_1` (the JS target needs the
  latter, `needsJsName`, `NamingUtils.kt:55`).

## Proof

- Failing golden `ClientSpec` case with the reproducer.
- kotlinx.serialization round-trip: `Json.decodeFromString<Operator>("\"<=\"")` and back; MockEngine test that the
  query parameter is sent as `op=%3C%3D`.
- A table test for the naming function over `<`, `a:b`, `a;b`, `a\b`, `[x]`, `-1`, `1`.
- integration-tests: `github.json` and `youtrack.json` have no enum value with `: ; < > \` (scanned), `openai.yaml`
  none found by text search. No coverage. Since the integration builds use the repository's Kotlin version, a
  compiler upgrade can surface this without any generator change, as 2.4.20 did here.

## Affected documents

26 total: amazonaws.com:s3 [1eed5a530ab5251aee4b], amazonaws.com:glacier [292bbe0bfc2432adc60e],
amazonaws.com:macie2 [1329c446be8d50b4de16], amazonaws.com:config [9ddb91cea4c82cbb12d5],
amazonaws.com:accessanalyzer [cfeb20130604c31291c2], apptigent.com [04f32ea3ecb3622265a7], api2cart.com
[3f7eb592e4d4978df73c] (`<`, `>`), nexmo.com:voice [d3f2245e2f1c7ba478c3] (`;`), salesforce.local:einstein
[46a078d8f8436e435150], alertersystem.com [d1c61359c434f9603247]. Full list: `<scratchpad>/naming/illegal-sub.json`
key `enum`.
