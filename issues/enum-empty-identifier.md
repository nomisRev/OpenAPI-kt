# An enum value with no letters or digits (`""`, `"."`, `"-"`) becomes the empty identifier ` `` `

**Status:** fixed on branch openapi-3.2-streaming. The entries of one enum are named together, `enumEntryNames` (`renderer/.../EnumEntryNames.kt`): a character that cannot be in a name is a word break (`aws:kms` is `AwsKms`), a value without letter or digit is spelled out (`<=` `LessThanOrEqual`, `""` `Empty`, `" "` `Blank`), values that still share a name spell their symbols (`last_activity`/`-last_activity` are `LastActivity`/`MinusLastActivity`, `aws:kms`/`aws-kms` are `AwsColonKms`/`AwsMinusKms`), the rest take the first free number from 2; a rename warns. Names that compiled and did not collide are unchanged. The declaration, `@SerialName`, the typed and open enum serializers, the path `when` and every default literal read the one table. Golden `enum/entry-names` and `client/enum-entry-names`, `EnumEntryNamesTest`, `EnumEntryNamesRoundTripTest`, `EnumEntryNamesRuntimeTest`, `EnumEntryNamesWarningsSpec`.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 16 of 1,547 documents (xero x3, fec.gov, clickmeter, slack, readme.io, dodo.ac, storecove, thebluealliance, ideal-postcodes, billingo, clever, gsmtasks, tomtom:maps, amazonaws:s3). 12 have no excluded errors; s3, clickmeter, tomtom and gsmtasks also carry `conflicting declarations` / illegal-character errors and would still fail.
**Source spec valid:** yes — `enum` may hold any value, `""` included (OAS 3.0.3 §4.7.24 Schema Object, JSON Schema Validation §6.1.2).

## Symptom

```
Pets.kt:34:8: error: syntax error: Expecting ';' after the last enum entry or '}' to close enum class body.
Pets.kt:35:7: error: syntax error: Expecting member declaration.   (cascade, dozens per enum)
```

14 documents have `""` in an enum (the "no filter" / "not set" option). amazonaws.com:s3 has `enum: [".", "-"]`.

## Minimal reproducer

`/private/tmp/claude-501/-Users-simonvergauwen-Developer-OpenAPI-kt/a0df68b9-ccc6-4d18-981e-753c6b12368d/scratchpad/repro/enum-empty-string-value.json`

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},
 "paths":{"/pets":{"get":{"operationId":"listPets",
   "parameters":[{"name":"filter","in":"query","schema":{"type":"string","enum":["","spiders","uniques"]}}],
   "responses":{"200":{"description":"ok","content":{"application/json":{"schema":{"$ref":"#/components/schemas/Pet"}}}}}}}},
 "components":{"schemas":{"Pet":{"type":"object","properties":{
   "kind":{"type":"string","enum":["BANK","NONE",""]},
   "sep":{"type":"string","enum":[".","-"]}}}}}}
```

Observed (parse, typed, render, emit pass; no diagnostics; kotlinc fails):

```kotlin
public enum class Filter(public val `value`: String) {
  ``(""),                       // empty identifier
  @SerialName("spiders") Spiders("spiders"), ...
}
public enum class Kind { BANK, NONE, ``, }          // and no @SerialName("") on it
public enum class Sep(public val `value`: String) {
  @SerialName("-") ``("-"),     // "." is gone: silently dropped
  ;
}
```

## What the spec says

`enum` values are arbitrary JSON values; an empty string is a legal member and is what the server expects on the wire. Nothing lets a generator drop or merge members.

## Root cause

Traced.

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/NamingUtils.kt:83-99` `toEnumValueName`: `rawValue.toPascalCase()` is `""` for a value with no word characters; neither branch is a valid class name, so it falls to `else -> "`$sanitized`"`, i.e. the two backticks around nothing. Only `"*"` and `"/"` are special-cased.
- `EnumRenderer.kt:88` `addEnumConstant(entryName, ...)`: KotlinPoet keeps enum constants in a map by name, so two values with one name (`"."` and `"-"`, both `""`) overwrite each other. That is the silent drop in `Sep`. Not checked beyond this case: any two values with one Kotlin name (`"a-b"` / `"a_b"`) likely merge the same way.
- `EnumRenderer.kt:71`: `@SerialName` is added only when `rawValue != entryName.unescapeBackticks()`; for `""` both sides are `""`, so a renamed entry would lose its serial name unless this comparison is against the final name.

## Correct final API

Every enum value is an entry with a stable, valid name and its exact wire value:

```kotlin
@Serializable
public enum class Filter(public val value: String) {
  @SerialName("") Empty(""),
  @SerialName("spiders") Spiders("spiders"),
  @SerialName("uniques") Uniques("uniques"),
}
```

`Filter.Empty` sends `filter=`, and `""` in a response decodes to it. Symbol-only values get a spelled-out name (`"."` -> `Dot`, `"-"` -> `Minus`, like the existing `Star`/`Slash`), and two values with one Kotlin name are disambiguated (suffix, as `uniqueNames()` does for path nodes), never merged.

## Fix slice

- Renderer only: `toEnumValueName` must never return an empty name (`Empty` for `""`, `Blank`/`Space` for whitespace, a symbol table for punctuation, fallback `Value<N>`), and the enum renderer must name all entries of one enum together so collisions are resolved (`Empty` already taken by a real `"Empty"` value -> suffix).
- `needsValueProperty()` (`EnumRenderer.kt:21-25`) and the `@SerialName` test must compare against the final name.
- The same function names default-value literals (`ClientRendererOperations.kt:764`, `CollectionRenderer.kt:45`, `ObjectRenderer.kt:565`), so those must use the same per-enum mapping.
- Open decision: the name for `""` (`Empty`?) and whether a collision emits a diagnostic.

## Proof

- Failing test first: golden `ClientSpec`/model case for `enum: ["", "a"]` and `enum: [".", "-"]`, asserting three resp. two entries with `@SerialName`.
- Runtime: kotlinx.serialization round trip of `""`; MockEngine test that the query is `filter=`.
- Integration tests: `github.json` and `youtrack.json` contain no enum value without a word character (checked by script); `openai.yaml` has no `- ''` enum line (grep). None would catch a regression.
- Latent: 29 more corpus documents have such enum values but fail earlier (stripe has 601 `""` members, drchrono, here.com:tracking, telnyx, influxdata, ...); they hit this once their earlier failure is fixed.

## Affected documents

xero.com:xero_accounting [02948b7a536d33b0ae70], xero.com:xero-payroll-au [0438fe542a6d52d592e5], fec.gov [173c74ca98f307784e41], amazonaws.com:s3 [1eed5a530ab5251aee4b], clickmeter.com [23d8e065f3aa6785b5e0], slack.com [4b4bd2539a8ae61e458d], readme.io [507a0842870237f15b6b], dodo.ac [5824a94a2b214c7fe86f], tomtom.com:maps [5956befdf26f27ee0ac8], xero.com:xero_bankfeeds [7d7fc429660036a5a399], plus storecove.com [86816f8e1b1852f8f3f0], thebluealliance.com [9520c7e346aab0c40301], ideal-postcodes.co.uk [a27ed2379216e27c832b], billingo.hu [bb999a1c3073e40b9c73], clever.com [d6c3096af77864426cbd], gsmtasks.com [daf353ec0ddccdfbd4fa]. Total 16 (found by scanning all emitted sources for an entry named ` `` `).
