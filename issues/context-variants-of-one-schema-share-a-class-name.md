# Read, Write and "Null" variants of one schema are rendered under one class name

**Status:** fixed in `300d9589`. A schema has two shapes when it, or anything it refers to, has `readOnly` or
`writeOnly`, and the context of a use (Read in a response, Write in a request) reaches every schema beneath it, through
inline schemas and `$ref`s. Before, an inline wrapper made a `Null` variant with every property (googleapis.com:people,
retail), and a `$ref` without flags of its own forced `Write` on what it held, so a response could decode into the
request shape (`Summary { owner: PersonWrite }`, not seen by the census: no duplicate file, no compile error). The
renderer suffixes `Read`/`Write` only when the variants differ, renders equal ones as one file, and reports an error
when two different models would still be one file. Proof: `ContextFlowSpec` (typed), `ContextVariantsSpec` with goldens
`client/context-equal-variants`, `client/context-variants`, `client/context-variants-both`, `client/context-retail`,
`ContextVariantsRuntimeTest`; the golden `object/plain-model-no-read-suffix` now reads `IssueKey.project` as
`IssueFolderRead`. Re-checked with `--filter`: of the 28 documents that failed on duplicate files in the twelfth census,
21 compile; 5 now stop on the new error, all two component schemas with one Kotlin name
(`schema-names-map-to-one-kotlin-class`: `_metadata`/`metadata` in sendgrid.com, `EventType`/`eventType`,
`OBRate1_0`/`OB_Rate1_0`, `Endpoint-get-users`/`Endpoint-get-users--`, and bunq.com's component `AvatarRead` next to
the `Read` variant of `Avatar`); apple.com:sirikit-cloud-media and influxdata.com reach compile and fail on errors the
duplicate hid (a discriminator base that is not rendered sealed; an unresolved `Post.Body`). The integration-tests
clients do not change.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** emit (harness check) / wrong output (the Gradle plugin overwrites one variant with the other)
**Impact:** 18 of 1,547 documents fail at emit (github.com ×6, apideck.com ×4, asana.com, apache.org ×2, adyen.com:LegalEntityService, googleapis.com:people, googleapis.com:retail, intellifi.nl, influxdata.com). In 2 of them the two files differ.
**Source spec valid:** yes

## Symptom

`IllegalStateException: Duplicate generated files: [….model.AlertCreatedAt, …]`. The typed layer produced two or three
models for one component (`Reference(name, Read)`, `Reference(name, Write)`, `Reference(name, Null)`) and the renderer
gave them all the same class name.

- 86 duplicated files are byte-identical (the variants do not differ): harmless under the plugin's last-write-wins,
  fatal in the harness and in `renderGeneratedFiles` (`renderer/src/jvmTest/.../TestBalloonDsl.kt:93`).
- 4 duplicated files differ: googleapis.com:people (`PersonMetadata`, `RelationshipInterest`, `RelationshipStatus`:
  one variant has `sources: List<SourceRead>`, the other `List<Source>`) and googleapis.com:retail
  (`GoogleCloudRetailV2alphaCatalogAttribute`: one variant lacks `inUse` and `type`). With the Gradle plugin whichever
  is rendered last silently replaces the other.

## Minimal reproducer

### A. Read and Write variants that are equal (github `alert-created-at`)

`<scratchpad>/repro/duplicate-model-read-write-variants.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": {
    "/alerts": { "get": { "operationId": "getAlert", "responses": { "200": { "description": "ok",
      "content": { "application/json": { "schema": { "$ref": "#/components/schemas/alert" } } } } } } },
    "/summaries": { "get": { "operationId": "getSummary", "responses": { "200": { "description": "ok",
      "content": { "application/json": { "schema": { "$ref": "#/components/schemas/summary" } } } } } } }
  },
  "components": { "schemas": {
    "alert": { "type": "object", "properties": {
      "id": { "type": "integer", "readOnly": true },
      "created_at": { "$ref": "#/components/schemas/alert-created-at" } } },
    "summary": { "type": "object", "properties": {
      "created_at": { "$ref": "#/components/schemas/alert-created-at" } } },
    "alert-created-at": { "type": "string", "format": "date-time", "readOnly": true }
  } }
}
```

```
emit fail: Duplicate generated files: [io.github.nomisrev.openapi.generated.model.AlertCreatedAt]
models: alert(Read), alert-created-at(Read), summary(Null), alert-created-at(Write)   <- two equal wrappers
```

### B. A "Null" variant that differs from the Read variant (googleapis.com:people)

`<scratchpad>/repro/duplicate-model-null-and-read-variants-differ.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": {
    "/people/{id}": { "get": { "operationId": "getPerson",
      "parameters": [{ "name": "id", "in": "path", "required": true, "schema": { "type": "string" } }],
      "responses": { "200": { "description": "ok",
        "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Person" } } } } } } },
    "/people": { "get": { "operationId": "listPeople", "responses": { "200": { "description": "ok",
      "content": { "application/json": { "schema": { "type": "object",
        "properties": { "person": { "$ref": "#/components/schemas/Person" } } } } } } } } }
  },
  "components": { "schemas": {
    "Person": { "type": "object", "properties": {
      "id": { "type": "string", "readOnly": true },
      "password": { "type": "string", "writeOnly": true } } }
  } }
}
```

```
emit fail: Duplicate generated files: [io.github.nomisrev.openapi.generated.model.Person]
models: Person(Read) {id}            Person(Null) {id, password}
diff of the two Person.kt:  >   public val password: String? = null,
```

The `Person(Null)` variant is used by a *response* and carries the `writeOnly` `password`.

## What the spec says

OAS 3.0.3 Schema Object fixed fields: `readOnly` — the property "MAY be sent as part of a response but SHOULD NOT be
sent as part of the request"; `writeOnly` — "MAY be sent as part of a request but SHOULD NOT be sent as part of the
response" (https://spec.openapis.org/oas/v3.0.3.html#fixed-fields-20). A schema therefore has at most two shapes, the
request one and the response one. There is no third.

## Root cause (traced)

1. Naming: `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/TypeMapping.kt:55` adds the `Read`/`Write` suffix
   only when the name is in `config.contextSpecificNames`.
   `renderer/.../ContextSpecificNameResolution.kt:34` puts a name there only when it has *both* a Read and a Write
   variant *and* they differ. So equal Read/Write variants (case A) and any pairing with a Null variant (case B) get
   the bare name. `renderer/.../Generate.kt:37` then renders one `FileSpec` per variant.
2. The Null variant of a context-specific schema should not exist. It comes from
   `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/registry/Registry.kt:137`:
   for an inline schema without read/write-only content, `nextContext = SchemaContext.Null` is passed as the *third*
   constructor argument of `ScopeImpl` (`:140`, `:145`), which is `forceContext`. A reference resolved below that
   scope then takes `forceContext ?: context` = `Null` at `Registry.kt:116`, although the target is context-specific.
   The inline response wrapper of case B is exactly that. A `Null` model keeps every property (`Object.kt:56`).
3. Case A's two variants come from a non-context-specific parent forcing `Write` on its children
   (`Registry.kt:121`, `:241`) while a context-specific parent passes `Read`.

## Correct final API

- Variants that are structurally equal are one class: `model/AlertCreatedAt.kt`, referenced by both `Alert` and
  `Summary`.
- Variants that differ get distinct names from one rule that covers all three contexts: `Person` (request shape) and
  `PersonRead` (response shape) today's convention, never two files named `Person`.
- A response never exposes a `writeOnly` property and a request never a `readOnly` one: `GET /people` returns
  `Response(person: PersonRead?)` with no `password`.

## Fix slice

- typed: `Registry.kt:137` must not force `Null` onto children; an inline schema should pass the caller's context
  through (`forceContext ?: context`), so a context-specific target under an inline wrapper resolves as Read in a
  response and Write in a request. After that `SchemaContext.Null` only ever names schemas that are not
  context-specific.
- renderer: compute the class name of a `NamingContext.Reference` from the set of *distinct* variants of that name
  (compare with `contextComparisonKey`): one variant or all equal -> bare name and a single file; otherwise suffix.
  `contextSpecificNames()` has to consider Null and the one-sided cases (Read + Null, Write + Null).
- renderer: deduplicate equal variants before `toFileSpecOrNull`, and fail with a diagnostic when two different
  models still map to one file (shared guard, see `duplicate-model-ref-with-readonly-sibling.md`).
- Open decision: when only a Read variant and a neutral one exist, is the name `Person`/`PersonRead` or does the
  neutral one disappear entirely once step 1 is fixed? Tracing suggests it disappears.

## Proof

- Failing typed test: reproducer B, assert no model has `SchemaContext.Null` for a schema with `readOnly`/`writeOnly`
  properties, and that the response model of `GET /people` has no `password`.
- Golden `ClientSpec` cases for A and B (`renderGeneratedFiles` throws on the duplicate today).
- MockEngine runtime test for B: decode `{"person":{"id":"1"}}`, assert the response type has no `password` member.
- integration-tests: `github.json` contains case A (7 schemas: `alert-created-at`, `alert-dismissed-at`,
  `alert-fixed-at`, `alert-html-url`, `alert-number`, …). The integration build passes only because the plugin
  overwrites identical files, so it does not catch this; a duplicate-file guard in the renderer would turn it into a
  build failure immediately.

## Affected documents

18 total at emit: github.com [57637e78e1593bb439a5], github.com:api.github.com [416b644d75497a6c18ea],
github.com:ghec [9fc70bc85a2a2cb5abd3], github.com:ghes-3.8 [305305f56edbbee243c6], apideck.com:hris
[0c426f60c46eac0ab87e], apideck.com:pos [566893e9715a9221de26], asana.com [b2b8fb38f6a5a2af6dd1], apache.org:airflow
[d2979a0aa336b2ffc358], googleapis.com:people [60cb8ef3e45eb0027876] (bodies differ), googleapis.com:retail
[21198c2a9341c6a21baf] (bodies differ). 6 of the 18 also have the `$ref` + `readOnly` duplicate.
