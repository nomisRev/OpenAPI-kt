# Root-level `x-*` extensions are dropped by the parser (`OpenAPI.extensions` is always empty)

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming); pinned by `CorpusRoundTripAuditTest`
**Stage:** parse (silent data loss; no effect on generated code today)
**Impact:** 430 of 1,547 documents have at least one root extension. Most frequent keys: `x-hasEquivalentPaths` 265, `x-maturity` 44, `x-tagGroups` 26, `x-groups` 25, `x-webhooks` 23, `x-hubspot-available-client-libraries` 9, `x-errors` 8, `x-speakeasy-retries` 7.
**Source spec valid:** yes

## Symptom

```kotlin
val api = OpenAPI.fromJson("""{"openapi":"3.0.0","info":{"title":"Audit","version":"1"},"paths":{},"x-hasEquivalentPaths":true}""")
api.extensions            // emptyMap()
api.toJson()              // no "x-hasEquivalentPaths"
```

Every other object that declares `extensions` (Info, Contact, License, Operation, Response, MediaType, Encoding,
Example, Link, Components, Paths) keeps its `x-*` keys; only the document root loses them. There is no warning.

How this reaches a generated client — verified, and currently it does not change the output:

- `typed/` and `renderer/` never read `OpenAPI.extensions` (no use of the root map; `x-webhooks` has 0 matches under
  `typed/src/commonMain` and `renderer/src/jvmMain`). The reproducer below generates the same files with and without
  its root extensions.
- What is lost is everything that would have to be built on the public model:
  - an `OpenApiPreprocessor` (`typed/.../OpenApiPreprocessor.kt:5`) receives an `OpenAPI` whose root extensions are
    already gone, so a user cannot key a preprocessing decision on them;
  - `x-webhooks` (23 documents, and 265 entries in the repository's own `github.json`) is the pre-3.1 way to
    describe webhooks; `issues/webhooks-and-unreferenced-schemas-generate-nothing.md` cannot offer it as an opt-in
    until the parser keeps it;
  - `x-speakeasy-retries` / `x-errors` style root configuration (retry policy, shared error shapes) is invisible to
    any future feature;
  - `OpenAPI.toJson()` / `toYaml()` are not faithful, which matters for anyone using the parser module on its own
    (`parser/` is a separate Kotlin Multiplatform module).

So this is a parser-fidelity bug with an enabling role, not a wrong-client bug. Priority follows from that.

## Minimal reproducer

`<scratchpad>/repro/root-extensions.json`:

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/ping": { "get": { "operationId": "op",
    "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "type": "string" } } } } } } } },
  "x-hasEquivalentPaths": true,
  "x-tagGroups": [ { "name": "g", "tags": ["a"] } ]
}
```

```
worker exit=0 phases: parse, typed, render, emit pass
diagnostics: []
compile exit=0        (api/Ping.kt, api/Repro.kt — identical to the output without the two extensions)
```

The loss itself is a pure round-trip loss, so the proof is the characterization test rather than generated code:
`CorpusRoundTripAuditTest."root extensions are currently discarded"`
(`parser/src/commonTest/kotlin/io/github/nomisrev/openapi/parser/CorpusRoundTripAuditTest.kt:46-51`) asserts
`assertEquals(emptyMap(), api.extensions)` and that `x-hasEquivalentPaths` is absent from `api.toJson()` for the
AWS Migration Hub root (amazonaws.com:AWSMigrationHub [00059ba6ca9e7ae965de]).

## What the spec says

OAS 3.0.3 §4.7.1 OpenAPI Object: "This object MAY be extended with Specification Extensions." §4.9 Specification
Extensions: "The extensions properties are implemented as patterned fields that are always prefixed by `x-`" and
"The value can be `null`, a primitive, an array or an object." Same in 3.1.0 §4.8.1 / §4.9.

## Root cause

`parser/src/commonMain/kotlin/io/github/nomisrev/openapi/parser/OpenAPI.kt:23-25`:

```kotlin
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class OpenAPI(
```

The class declares `public val extensions: Map<String, JsonElement> = emptyMap()` (`OpenAPI.kt:65`) but uses the
plugin-generated serializer, which only fills `extensions` from a literal JSON key named `extensions` and ignores
unknown `x-*` keys. The other objects opt into the shared mechanism, e.g. `Info.kt:109-115`:

```kotlin
internal object Serializer :
  KSerializerWithExtensions<Info>(generatedSerializer(), Info::extensions, { info, extensions -> info.copy(extensions = extensions) })
```

(`KSerializerWithExtensions.kt:18`). `OpenAPI` was simply never wired to it.

## Correct final API

- `OpenAPI.fromJson(...)` / `fromYaml(...)`: `extensions == mapOf("x-hasEquivalentPaths" to JsonPrimitive(true), ...)`.
- `toJson()` / `toYaml()` write the extensions back as top-level `x-*` keys, not under an `extensions` key.
- No change to generated clients by this fix alone.

## Fix slice

- parser only: annotate `OpenAPI` with `@Serializable(with = OpenAPI.Companion.Serializer::class)` +
  `@KeepGeneratedSerializer` and add the `KSerializerWithExtensions<OpenAPI>` object, exactly as `Info` does. Check
  the YAML path (`KSerializerWithExtensions` already supports `YamlInput` for the other types) and
  `toJsObject()` (`OpenAPI.kt:70`).
- Watch for one behavioural side effect: the generated serializer currently accepts a literal top-level
  `"extensions": {...}` key; with the wrapper that key is no longer a field. Not known to occur in the corpus (not
  checked).
- Open decision: none.

## Proof

- Replace the characterization test with a preservation assertion (non-empty `extensions`; key present after
  `toJson()` and after a YAML round trip).
- Corpus: the raw round-trip comparison in `exploration/PARSER-AUDIT.md` §4 should report 0 root-extension losses
  (it reported 429 documents on 2026-11-01; this census scan finds 430 documents with a root `x-*` key).
- integration-tests coverage: `github.json` has one root extension (`x-webhooks`) and `openai.yaml` has one; the
  integration projects only compile generated code, and generated code does not depend on root extensions, so they
  cannot detect this either way.

## Affected documents

amazonaws.com:AWSMigrationHub [00059ba6ca9e7ae965de] (`x-hasEquivalentPaths`) and 264 further documents with that
key, 44 with `x-maturity`, 26 with `x-tagGroups`, 23 with `x-webhooks`
— 430 documents in total.
