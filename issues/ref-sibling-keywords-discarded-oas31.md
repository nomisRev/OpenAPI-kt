# Keywords next to a `$ref` are discarded in OpenAPI 3.1 documents (`required`, `description`, `default`, `deprecated`, ...)

**Status:** fixed in `c4ef6f6f`, for OpenAPI 3.0 and 3.1 documents alike (decided 2026-11-16: 3.0 says to ignore these
keywords, but authors write them to be read; every change they make in a 3.0 document is reported). Keywords that shape
the value (`required`, `properties`, `items`, `additionalProperties`, `enum`, composites) are merged into the referenced
schema; a property becomes an inline class. `nullable: true` and `type: [..., "null"]` make that use nullable, with a
warning (strictly, 2020-12 reads them as non-null). A contradicting `type` or shape is ignored with a warning.
Not done: `description`, `title`, `deprecated` and `default` are kept by the parser but change nothing, since no
property gets KDoc or `@Deprecated` yet (a separate feature); `toYaml()` still writes a reference without its keywords.
The "Correct final API" below overstated what was possible: property KDoc did not exist for inline properties either.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming); parser half pinned by `CorpusRoundTripAuditTest`
**Stage:** wrong output (parse drops the keywords; the generated model is weaker than the schema)
**Impact:** 30 of the 36 OpenAPI 3.1 documents in the census have keywords beside a schema `$ref` (1,275 locations): `description` 1,040 in 30 documents, `x-addedInVersion` 182 in 11, `nullable` 23 in 5, `readOnly` 9 in 3 (kept), `deprecated` 6 in 3, `minLength`/`maxLength` 4 each in 1, `required` 1, `properties` 1, `type` 1.
**Source spec valid:** yes for 3.1 — siblings of `$ref` are evaluated; for 3.0 they must be ignored, which is what happens

## Symptom

In a 3.1 document, `{"$ref": "#/components/schemas/Dates", "required": ["name"]}` is read as
`{"$ref": "#/components/schemas/Dates"}`. Everything after the parser sees a bare reference, so:

- a `required` sibling does not make the property required: the generated property is nullable with a default;
- a `description` sibling (the way Adyen, Codat and OpenAI document a property whose type is a component) never
  reaches the generated KDoc;
- `default`, `deprecated`, `title`, `minLength`, ... are lost the same way.

No diagnostic is produced.

## Minimal reproducer

`<scratchpad>/repro/ref-siblings-31.json` (the `TaxComponent` shape is codat.io:commerce
`components/schemas/TaxComponent/allOf/2`; the `amount` shape is adyen `StoredValueBalanceCheckRequest/properties/amount`):

```json
{
  "openapi": "3.1.0",
  "info": { "title": "Repro", "version": "1" },
  "paths": {
    "/tax": { "get": { "operationId": "op", "responses": { "200": { "description": "ok", "content": { "application/json": {
      "schema": { "$ref": "#/components/schemas/TaxComponent" } } } } } } },
    "/check": { "post": { "operationId": "op",
      "requestBody": { "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Request" } } } },
      "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "type": "string" } } } } } } }
  },
  "components": { "schemas": {
    "Dates": { "type": "object", "properties": { "modifiedDate": { "type": "string" }, "name": { "type": "string" } } },
    "Amount": { "type": "object", "properties": { "value": { "type": "integer" } } },
    "TaxComponent": { "allOf": [
      { "type": "object", "properties": { "rate": { "type": "number" } } },
      { "$ref": "#/components/schemas/Dates", "required": ["name"] } ] },
    "Request": { "type": "object", "required": ["amount"], "properties": {
      "amount":   { "$ref": "#/components/schemas/Amount", "description": "The amount to check." },
      "previous": { "$ref": "#/components/schemas/Amount", "description": "Previous amount", "deprecated": true, "default": { "value": 0 } },
      "maybe":    { "$ref": "#/components/schemas/Amount", "type": ["object", "null"] } } }
  } }
}
```

```
worker exit=0 phases: parse, typed, render, emit pass
diagnostics: []
compile exit=0
```

Generated (imports removed):

```kotlin
public data class TaxComponent(
  public val rate: Double? = null,
  public val modifiedDate: String? = null,
  public val name: String? = null,          // schema says required
)

public data class Request(
  public val amount: Amount,                // no KDoc "The amount to check."
  public val previous: Amount? = null,      // no @Deprecated, no KDoc, default {"value":0} gone
  public val maybe: Amount? = null,
)
```

The parser half is pinned by `CorpusRoundTripAuditTest."OpenAPI 3_1 schema reference siblings are currently discarded"`
(`parser/src/commonTest/kotlin/io/github/nomisrev/openapi/parser/CorpusRoundTripAuditTest.kt:39-44`): the round trip
of `{"$ref":"#/components/schemas/Name","required":["name"]}` is `{"$ref":"#/components/schemas/Name"}`.

## What the spec says

- OAS 3.0.3 §4.7.23 Reference Object: "This object cannot be extended with additional properties and any properties
  added SHALL be ignored." Dropping siblings is correct for 3.0.x documents.
- OAS 3.1.0 §4.8.24 Schema Object: the Schema Object "is a superset of the JSON Schema Specification Draft 2020-12",
  and in 2020-12 `$ref` is an ordinary applicator keyword: the instance must satisfy the referenced schema **and**
  every sibling keyword. §4.8.23 Reference Object (the non-schema one) additionally allows `summary` and
  `description` siblings that override the target's. So `{$ref: Dates, required: [name]}` means
  `allOf: [{$ref: Dates}, {required: [name]}]`.
- `nullable` beside a `$ref` in a 3.1 document (23 locations, all codat.io) is not a 3.1 keyword; ignoring it is
  correct, though a diagnostic would help (`type: [..., "null"]` or `anyOf` with `{type: null}` is the 3.1 form).

## Root cause

`parser/src/commonMain/kotlin/io/github/nomisrev/openapi/parser/ReferenceOr.kt:72-93` (JSON) and `:97-110` (YAML):
any object containing `$ref` (or `$recursiveRef`) is decoded as

```kotlin
Reference(ref = ..., readOnly = ..., writeOnly = ...)
```

`ReferenceOr.Reference` (`ReferenceOr.kt:33-38`) has no room for other keywords, and the decision is independent of
the document's `openapi` version. `readOnly`/`writeOnly` were special-cased earlier (youtrack.json has 202
`readOnly` siblings), which shows the need but not the general mechanism. Nothing downstream can recover the
siblings: `typed` only ever sees `ReferenceOr.Reference`.

## Correct final API

For 3.1 documents the reproducer generates:

```kotlin
public data class TaxComponent(
  public val rate: Double? = null,
  public val modifiedDate: String? = null,
  public val name: String,                  // required by the sibling
)

public data class Request(
  /** The amount to check. */
  public val amount: Amount,
  /** Previous amount */
  @Deprecated("...")                        // however `deprecated` is rendered for inline properties today
  public val previous: Amount? = null,
  public val maybe: Amount? = null,         // type [object, null] sibling: nullable
)
```

Wire behaviour: `TaxComponent` without `name` fails to decode (as the schema demands) instead of yielding `null`.
For 3.0 documents nothing changes, except optionally a `Warning` when a semantic keyword (`required`, `type`,
`properties`, constraints) sits next to a `$ref` and is ignored per §4.7.23.

## Fix slice

- parser: make the reference carry its siblings. Two shapes:
  1. `ReferenceOr.Reference(ref, siblings: Schema?)` (generic `T?` for non-schema references, where only
     `summary`/`description` apply), decoded by running the data serializer on the object minus `$ref`; or
  2. decode `{$ref, ...}` in a Schema position as a `Schema` with a `ref` field (the 2020-12 model).
  Shape 1 is the smaller change and keeps every existing `is ReferenceOr.Reference` match working.
- typed: when a schema reference has semantic siblings and the document is 3.1+, treat it as
  `allOf [ref, siblings]` (the `allOf` merge already exists in `transformers/AllOf.kt`); annotation-only siblings
  (`description`, `title`, `deprecated`, `default`, `example`) override the target's annotations at the use site
  without creating a new model.
- Version gate: `OpenAPI.openapi` starts with `3.0` → ignore siblings (optionally warn).
- Open decisions: which siblings count as annotation-only (no new type) versus structural (new merged type and
  therefore a new generated class name); whether 3.0 documents that clearly rely on siblings (very common for
  `description`) get the lenient 3.1 treatment for annotations.

## Proof

- Parser: replace the characterization test with a preservation assertion (round trip keeps `required`).
- typed test: `TaxComponent.name` is required for `openapi: 3.1.0` and optional for `3.0.3` with the same schemas.
- Renderer golden for KDoc/`@Deprecated` on a referenced property.
- Runtime: decoding `{"rate":1.0}` as `TaxComponent` fails with a missing-field error.
- integration-tests coverage: `openai.yaml` is 3.1.0 and has `$ref` siblings — `description` 120, `title` 11,
  `default` 5 (e.g. `RealtimeResponse...voice: {$ref: VoiceIdsShared, default: alloy}`), `nullable` 4, `type` 1 —
  so the fix changes its generated output (KDoc, defaults) and it would compile-check the result; nothing asserts
  the semantics. `github.json` (3.0.3) has none; `youtrack.json` (3.0.1) has only `readOnly` siblings, the case
  already handled.

## Affected documents

Semantic losses (constraint keywords): codat.io:commerce [8b250766ea0dafe5910f] (`required`), codat.io:sync-for-commerce
(`properties`), codat.io:assess (`type`), adyen.com:ManagementService (`minLength`/`maxLength`, 4 each).
Documentation losses (`description`, `deprecated`): all 30, e.g. adyen.com Stored Value [04d7320a11e52e2b6de4].
30 documents in total; counts are from a scan of the census specs for objects with a `#/components/schemas/` `$ref`
and at least one other key, restricted to documents whose `openapi` starts with `3.1` (the earlier raw comparison in
`exploration/PARSER-AUDIT.md` §1 reported 1,188 omitted fields with a slightly different filter).
