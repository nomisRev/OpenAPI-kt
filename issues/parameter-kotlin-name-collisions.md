# Two parameters of one operation get the same Kotlin name (`maxResults` / `MaxResults`)

**Status:** fixed (renderer; `ParameterNameCollisionRuntimeTest`, `client/parameter-name-collisions`). Parameters that want one Kotlin name are told apart (symbols spelled: `<=` `>=` `<` `>` `[]` `!`; then the location; then the first free number from 2) and each renamed one is a warning. Still open: Ktor `Parameters` compare names ignoring case, so `maxResults` and `MaxResults` are both written as `maxResults` by `url.encodedParameters.append`.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 30 of 1,547 documents fail to compile (amazonaws.com 26, alertersystem.com, api2cart.com, gsmtasks.com, twilio.com). 39 documents have the construct; the other 9 fail before compilation.
**Source spec valid:** yes — parameters are unique by name and location, and names are case sensitive.

## Symptom

```
api/Assets.kt:46:7: error: conflicting declarations:
maxResults: Long? = ...
maxResults: String? = ...
```

Often followed by `argument type mismatch` or `encodeURLParameter ... receiver type mismatch`, because the body of the function resolves the name to one of the two parameters (kinesis-video-archived-media, spotify.com, alertersystem.com).

## Minimal reproducer

`<scratchpad>/repro/parameter-kotlin-name-collision.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Params", "version": "1" },
  "paths": {
    "/assets": {
      "get": {
        "parameters": [
          { "name": "maxResults", "in": "query", "schema": { "type": "integer" } },
          { "name": "MaxResults", "in": "query", "schema": { "type": "string" } }
        ],
        "responses": { "204": { "description": "ok" } }
      }
    }
  }
}
```

Observed: all phases pass, no diagnostics, `compile exit=1`.

```kotlin
public suspend operator fun invoke(maxResults: Long? = null, maxResults: String? = null) {
  val response = client.get("assets") {
    if (maxResults != null) {
      url.encodedParameters.append("maxResults", maxResults.toString())
    }
    if (maxResults != null) {
      url.encodedParameters.append("MaxResults", maxResults.encodeURLParameter())
    }
  }
```

```
api/Assets.kt:18:40: error: conflicting declarations:
api/Assets.kt:18:66: error: conflicting declarations:
```

## What the spec says

OAS 3.0.3 §4.7.10 Operation Object, `parameters`: "The list MUST NOT include duplicated parameters. A unique parameter is defined by a combination of a name and location." §4.7.12.1: "Parameter names are case sensitive." So `maxResults` and `MaxResults` in `query` are two parameters, as are `dataSegmentCode` and `dataSegmentCode[]`, `accessToken` and `access_token`, or `ValidUntilDate`, `ValidUntilDate<` and `ValidUntilDate>`. A query and a header parameter with the same name are also two parameters.

## Root cause

Traced. `Route.Input.toParameterSpec` in `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererOperations.kt:741` names each parameter `name.toParamName()` on its own. `toParamName()` (`NamingUtils.kt:39-49`) camel-cases through `splitToWords()` (`typed/.../StringUtils.kt:3-27`), which lowercases the first word and drops ` - _ . / [ * ]`, then replaces every other non-identifier character with `_` and trims it. Distinct wire names therefore meet:

| Wire names | Kotlin name |
| --- | --- |
| `maxResults`, `MaxResults` | `maxResults` |
| `accessToken`, `access_token` | `accessToken` |
| `dataSegmentCode`, `dataSegmentCode[]` | `dataSegmentCode` |
| `ValidUntilDate`, `ValidUntilDate<`, `ValidUntilDate>` | `validUntilDate` |

The four functions that assemble an operation's parameter list (`ClientRendererOperations.kt:414-453`, `555-600`, `632` onwards, `695-720`) add the specs in order and never compare names. The request code (`ImplRenderer.kt:880,916`) derives the same name again, so both `append` calls read one variable.

Sub-groups in the 30 observed documents (a document can be in both): names differing only by case, 24; names differing by punctuation or a suffix, 9. No document in the corpus has the same name in two locations (`query` + `header`) on one operation, but the code path is the same.

In 8 of the 30 documents, the same operations also have the collision described in `issues/flattened-body-field-vs-parameter-name.md`.

## Correct final API

Both parameters are present, each sent under its own wire name, with distinct Kotlin names. The first declared parameter keeps the natural name. Proposed naming for a later one, in order:

1. If the wire names differ in a way that can be spelled, spell it: `ValidUntilDate<` → `validUntilDateLt`, `ValidUntilDate>` → `validUntilDateGt`, `dataSegmentCode[]` → `dataSegmentCodeList`. This only needs a small table (`<`, `>`, `<=`, `>=`, `[]`, `!`).
2. If the location differs, suffix the location: `idHeader`.
3. Otherwise suffix from 2 in declaration order, as `uniqueNames()` does for path segments: `maxResults`, `maxResults2`, with a warning diagnostic that names both wire names.

```kotlin
/** @param maxResults query `maxResults`  @param maxResults2 query `MaxResults` */
public suspend operator fun invoke(maxResults: Long? = null, maxResults2: String? = null)
```

Wire behaviour: `?maxResults=10&MaxResults=abc` when both are set.

## Fix slice

- Renderer: compute the Kotlin names of an operation's inputs once per `Route` (a map from `Route.Input` to name) and use that map in `toParameterSpec` and in the serialization code in `ImplRenderer.kt` and `ParameterSerialization.kt`. Today both sides call `toParamName()` independently, which is what lets them drift.
- The same allocation must reserve the names used by the body arguments, `body`, `requestType`, `contentType` (already handled by `callerContentTypeParameterName`, `ClientRendererRequestBody.kt:133-137`, which is a model for this), the stored path parameters and `client`. See `issues/flattened-body-field-vs-parameter-name.md`, `issues/operation-parameter-shadows-path-parameter.md` and `issues/parameter-named-client-shadows-http-client.md`: all of them want one name allocator per operation.
- Diagnostics: a warning per renamed parameter in `GenerationDiagnostics.kt`, like `renamedPathDiagnostics`.
- Open decision: spelled suffixes (step 1) or numbers only. Numbers are deterministic and need no table; spelled names read better for the Twilio and `[]` cases.

## Proof

- Failing test first: a `ClientSpec` golden case with the reproducer.
- Runtime: a MockEngine test in the style of `ParameterStylesRuntimeTest` asserting the request URL contains both `maxResults=1` and `MaxResults=a`.
- Integration tests would not catch a regression: no operation in `github.json`, `openai.yaml` or `youtrack.json` has two parameters with one Kotlin name (checked by script).

## Affected documents

30 observed. alertersystem.com [d1c61359c434f9603247] (171 operations, `x` and `x[]`); api2cart.com [3f7eb592e4d4978df73c]; gsmtasks.com [daf353ec0ddccdfbd4fa]; twilio.com:twilio_numbers_v2 [85cbd463e9ca37c08528]; amazonaws.com:mediapackage-vod [032212ce36e243a35ad6]; amazonaws.com:appconfig [b5b9ca7b69b8751f7f82]; amazonaws.com:es [e21d6d0a9567559fece1]; amazonaws.com:codeartifact [f340048f5f1ef47c1869]; amazonaws.com:databrew [a3b4ec847f466335d537]; amazonaws.com:ebs [4f975e6ee34f0aeccb27].
