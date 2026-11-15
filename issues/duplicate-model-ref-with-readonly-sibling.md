# A `$ref` annotated `readOnly`/`writeOnly` makes its target model generate twice

**Status:** fixed in `040871b9` (typed part). The renderer guard against two files with one name is not added
yet; it belongs with `context-variants-of-one-schema-share-a-class-name.md`.

**Correction (2026-11-15):** the claims below about `integration-tests/youtrack` are wrong. All 115 of its annotated
references point at schemas that have `readOnly`/`writeOnly` properties of their own (`ActivityItem.author` ->
`User`, which has a read-only `id`), so use and definition agreed and it never had this duplicate. Its generated
client is unchanged by the fix. Only an annotated reference to a schema with one shape (Google's pattern) broke.

**Result:** `Scan` used in a request and a response is `ScanWrite` (no `status`) and `ScanRead` (with `status`), and
there is one `Status`. Proof: `typed/.../registry/AnnotatedReferenceSpec.kt`, golden `client/annotated-reference`,
`AnnotatedReferenceRuntimeTest`.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** emit (harness check); in the Gradle plugin the second file silently overwrites the first
**Impact:** 73 of 1,547 documents (66 of them fail for this reason only). 66 googleapis.com services, adyen.com (2), apache.org (2), apple.com:sirikit-cloud-media, bunq.com, influxdata.com
**Source spec valid:** yes — `readOnly` next to `$ref` is ignored in OAS 3.0 and meaningful in 3.1; either way it must not break generation

## Symptom

`IllegalStateException: Duplicate generated files: [….model.Status, …]`. The model list of the `ApiTree` holds the
same model twice (equal data classes, same `NamingContext`), so two `FileSpec`s with one name are rendered. In all
182 duplicated files of this group the two bodies are byte-identical.

The exception comes from the corpus harness (`exploration/src/main/kotlin/CorpusWorker.kt:46`) and from the renderer
test DSL (`renderer/src/jvmTest/kotlin/io/github/nomisrev/render/TestBalloonDsl.kt:93`). The Gradle plugin has no such
check: `gradle-plugin/src/main/kotlin/io/github/nomisrev/openapi/gradle/GenerateOpenApiDefinition.kt:58` writes every
file, the last one wins. With identical bodies that is harmless, which is why `integration-tests/youtrack` (115 such
references) builds today.

## Minimal reproducer

`<scratchpad>/repro/duplicate-model-ref-with-readonly.json`

```json
{
  "openapi": "3.0.0",
  "info": { "title": "Repro", "version": "1" },
  "paths": {
    "/scans": { "get": { "operationId": "getScan", "responses": { "200": { "description": "ok",
      "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Scan" } } } } } } }
  },
  "components": { "schemas": {
    "Scan": { "type": "object", "properties": {
      "status": { "$ref": "#/components/schemas/Status", "description": "Output only.", "readOnly": true } } },
    "Status": { "type": "object", "properties": { "code": { "type": "integer" } } }
  } }
}
```

```
worker exit=1 phases: [parse pass, typed pass, render pass, emit fail]
java.lang.IllegalStateException: Duplicate generated files: [io.github.nomisrev.openapi.generated.model.Status]
```

`ApiTree.models` for it (dumped with `<scratchpad>/naming/tree.sh`):

```
Object  NamingContext(head=Reference(name=Scan, context=Read), nested=[])     status -> Reference(Status, Read)
Object  NamingContext(head=Reference(name=Status, context=Null), nested=[])
Object  NamingContext(head=Reference(name=Status, context=Null), nested=[])
```

Only `readOnly: true` / `writeOnly: true` trigger it. `description`, `nullable`, `deprecated`, `title`, `example`,
`default` next to the `$ref` generate fine (each tried on the reproducer).

## What the spec says

- OAS 3.0.3, Reference Object: "This object cannot be extended with additional properties and any properties added
  SHALL be ignored." (https://spec.openapis.org/oas/v3.0.3.html#reference-object). Google's documents are 3.0.0, so
  strictly the `readOnly` is to be ignored.
- OAS 3.1.0, Schema Object: `$ref` is a JSON Schema 2020-12 keyword and siblings apply
  (https://spec.openapis.org/oas/v3.1.0.html#schema-object), so the property is read-only there.
- `readOnly` describes the *property*: "Relevant only for Schema `properties` definitions. Declares the property as
  read only" (3.0.3, Fixed Fields of Schema Object). It says nothing about the referenced schema.

The project already honours the sibling on purpose (`typed/.../registry/Predicates.kt:15-18`,
`typed/.../transformers/Object.kt:52` drops the property from the Write variant). That part is fine.

## Root cause (traced)

Two places disagree about the context of the same component:

1. Use site: `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/registry/Registry.kt:115`
   `val contextSpecific = readOnly == true || writeOnly == true || peeked.readOrWriteOnly()` — the annotation on the
   *reference* makes the *target* context-specific, so the property's model is `Reference(Status, Read)`.
2. Definition site: `Registry.kt:36` `NamingContext.Reference.toModel()` resolves a bare `ReferenceOr.schema(name)`,
   which has no annotation, so `Registry.kt:113-122` decides `Status` is not context-specific and names the model
   `Reference(Status, Null)`.

`typed/.../ApiTree.kt:59-63` (`topLevelModels`) then iterates to a fixed point over the *names it asked for plus the
names it got back*: `{Scan(Read), Status(Read)}` becomes `{Scan(Read), Status(Read), Status(Null)}`, and both
`Status(Read).toModel()` and `Status(Null).toModel()` return the same `Status(Null)` model. The list is never
deduplicated, and `renderer/.../Generate.kt:37` renders one file per list element.

## Correct final API

One `model/Status.kt`. `Scan` (Read variant) has `val status: Status? = null`; a Write variant of `Scan`, when one
is needed, has no `status`. No `StatusRead`. Wire behaviour is unchanged.

## Fix slice

- typed: in `Registry.kt:115` (and the twin at `:224`) stop using the reference's own `readOnly`/`writeOnly` to
  decide whether the *target* is context-specific; keep it for dropping the property (`Object.kt:52`) and for marking
  the *parent* context-specific (`readOrWriteOnlyProperties`, `Registry.kt:176-181`).
- typed: make `topLevelModels` return models distinct by `NamingContext`, and assert that a requested
  `NamingContext.Reference` and the returned model's context agree (or key the fixed point on the returned contexts).
- renderer: `renderWithDiagnostics` should refuse two `FileSpec`s with one qualified name (an error diagnostic naming
  both models), so the Gradle plugin can never overwrite a file silently. This guard also covers
  `context-variants-of-one-schema-share-a-class-name.md`, `schema-names-map-to-one-kotlin-class.md` and
  `client-root-class-collides-with-path-class.md`.
- Open decision: whether OAS 3.0 documents should ignore the sibling (spec) or keep honouring it (current, matches
  what Google means). Keeping it is the more accurate client.

## Proof

- Failing test first: a typed test on the reproducer asserting `toApiTree().models` has no two elements with the same
  `context` (today: two `Status(Null)`).
- Golden `ClientSpec` case with the reproducer: `renderGeneratedFiles` already throws
  "Renderer generated duplicate files: …Status.kt" today, so the golden case fails without any new assertion.
- integration-tests: `youtrack.json` has 115 `$ref` + `readOnly` references and builds only because the plugin
  overwrites identical files; it would not catch a regression. `github.json` has none, `openai.yaml` has no
  `readOnly` at all.

## What is hidden behind it

Re-emitting the 94 "Duplicate generated files" documents with last-write-wins (what the plugin does) and compiling:
19 compile, 62 then fail on `name contains illegal characters: :` (Google `{name}:verb` paths, see
`composite-path-segments.md`), 8 on `conflicting declarations`, 5 on other errors.

## Affected documents

73 total, e.g. googleapis.com:spanner [0764c943105742128979], googleapis.com:contactcenterinsights
[01e58fbb6bd61725a33d], googleapis.com:authorizedbuyersmarketplace [0bd8b3af20dfd2e25813], googleapis.com:apigee
[0d824bf26e43ed3039a3], googleapis.com:dataproc [0e654835e405a4eafbf7], adyen.com:BalancePlatformService
[e068cfcf1a7c525ef786], adyen.com:LegalEntityService [e25af6ae556371e67421], apache.org:airflow
[d2979a0aa336b2ffc358], apple.com:sirikit-cloud-media [72528bf58a657172e8f0], influxdata.com [0c204911aa4ad745ebae].
Per-document classification: `<scratchpad>/naming/dups/<key>.log` and `<scratchpad>/naming/trees/<key>.txt`.
