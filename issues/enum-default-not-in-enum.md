# An enum default that is not one of the enum values renders a reference to a missing entry

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 1 of 1,547 documents (contract-p.fit); no excluded errors
**Source spec valid:** no — `default: "predicted"` with `enum: ["FULL", "SOURCE_FILES"]`. OAS 3.0.3 §4.7.24: the default "MUST conform to the defined type"; JSON Schema Validation (`default` keyword) recommends it validate against the schema. The question is the diagnostic.

## Symptom

```
Documents.kt:250:58: error: unresolved reference 'Predicted'.
    public suspend operator fun invoke(how: How? = How.Predicted): Response {
```

## Minimal reproducer

`/private/tmp/claude-501/-Users-simonvergauwen-Developer-OpenAPI-kt/a0df68b9-ccc6-4d18-981e-753c6b12368d/scratchpad/repro/enum-default-not-in-enum.json`

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},"paths":{"/documents":{"delete":{
  "parameters":[{"in":"query","name":"how","schema":{"type":"string","default":"predicted","enum":["FULL","SOURCE_FILES"]}}],
  "responses":{"204":{"description":"ok"}}}}}}
```

Observed: no diagnostics; `invoke(how: How? = How.Predicted)`; `Documents.kt:19:56: error: unresolved reference 'Predicted'.`

## What the spec says

The default has to be a valid instance of the schema. Here it is a string (type conforms) but not an enum member, so the document is wrong, not the generator's reading of it.

## Root cause

Traced. The default is turned into an entry name without checking membership: `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererOperations.kt:764` (`CodeBlock.of("%T.%L", enumClassName, toEnumValueName(d.value.wireValue()))`), likewise `ObjectRenderer.kt:565` for properties and `CollectionRenderer.kt:45` for list defaults. Neither the typed layer (`typed/.../transformers/Enum.kt`) nor `GenerationDiagnostics.kt` validates `default` against `values`.

## Correct final API

```kotlin
public suspend operator fun invoke(how: How? = null): Response
```

with a warning diagnostic: `DELETE /documents/{document_id}: parameter 'how' default "predicted" is not one of its enum values [FULL, SOURCE_FILES]; default ignored`. Not sent when null. No silent drop: the diagnostic names operation, parameter and value.

## Fix slice

- typed (preferred, one place): when building `Model.Enum`, drop a default that is not in `values` and record a diagnostic; or renderer: guard the three literal sites.
- Decision: warning vs error. Warning fits (the operation is fully usable).

## Proof

- Failing test first: golden case of the reproducer plus a diagnostics assertion; a property twin (`ObjectRenderer`).
- Integration tests: `github.json` has one enum whose default is not among its values (script) and still compiles, so that one is not rendered as a Kotlin default; not a regression guard for this path.

## Affected documents

contract-p.fit [80ed0134546eb5e81a98]. Total 1.
