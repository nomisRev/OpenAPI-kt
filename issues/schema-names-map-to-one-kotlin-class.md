# Different component schemas that map to one Kotlin class name (or one file) overwrite each other

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** emit (same name) / compile (names differing only by case) / wrong output (Gradle plugin overwrites)
**Impact:** 5 of 1,547 documents observed (amazonaws.com:health, amazonaws.com:dataexchange, openbanking.org.uk:account-info-openapi, bunq.com, googleapis.com:compute); 24 documents declare schemas with one Kotlin name and 8 declare schemas differing only by case (most are hidden behind earlier failures or unused)
**Source spec valid:** yes — component keys only have to match `^[a-zA-Z0-9\.\-_]+$` and are case-sensitive

Related: `schema-names-differ-by-case.md` covers variant 2 below (googleapis.com:compute) on its own; the fix is the
same uniqueness pass, so it is kept here for the complete picture.

## Symptom

Three variants of one missing step, a uniqueness pass over top-level model class names:

1. Two component names normalise to the same class name -> `Duplicate generated files` in the harness, silent
   overwrite in the Gradle plugin. `OB_Rate1_1` and `OBRate1_1` -> `OBRate11`; `eventType` and `EventType` ->
   `EventType`; `__string` and `String` -> `String`. In openbanking and aws health the two bodies differ.
2. Two names differ only by case -> two files `HTTPHealthCheck.kt` / `HttpHealthCheck.kt`, one file on a
   case-insensitive file system (macOS, Windows): `unresolved reference 'HTTPHealthCheck'`.
3. A component is literally named like another component's variant: bunq.com has `Avatar` (rendered `AvatarRead` for
   its response shape) and a component `AvatarRead`. 23 duplicated files in bunq, 8 with different bodies.

## Minimal reproducer

`<scratchpad>/repro/model-names-collide-after-normalization.json` (two GET operations returning the two schemas)

```json
"components": { "schemas": {
  "OB_Rate1_1": { "type": "object", "properties": { "a": { "type": "string" } } },
  "OBRate1_1":  { "type": "object", "properties": { "b": { "type": "string" } } } } }
```
```
emit fail: Duplicate generated files: [io.github.nomisrev.openapi.generated.model.OBRate11]
```

`<scratchpad>/repro/model-names-collide-case-only.json`

```json
"components": { "schemas": {
  "HTTPHealthCheck": { "type": "object", "properties": { "a": { "type": "string" } } },
  "HttpHealthCheck": { "type": "object", "properties": { "b": { "type": "string" } } } } }
```
```
emit pass, compile exit=1   (sources/…/model/ contains only HTTPHealthCheck.kt)
api/R0.kt:3:51: error: unresolved reference 'HTTPHealthCheck'.
```

`<scratchpad>/repro/model-names-collide-read-suffix.json`

```json
"components": { "schemas": {
  "Avatar": { "type": "object", "properties": {
    "id": { "type": "string", "readOnly": true }, "secret": { "type": "string", "writeOnly": true } } },
  "AvatarRead": { "type": "object", "properties": { "b": { "type": "string" } } } } }
```
(`Avatar` used as request and response, `AvatarRead` as a response)
```
emit fail: Duplicate generated files: [io.github.nomisrev.openapi.generated.model.AvatarRead]
```

## What the spec says

OAS 3.0.3, Components Object: "All the fixed fields declared above are objects that MUST use keys that match the
regular expression: `^[a-zA-Z0-9\.\-_]+$`" (https://spec.openapis.org/oas/v3.0.3.html#components-object). Keys are
JSON object member names, so `HTTPHealthCheck` and `HttpHealthCheck`, or `OB_Rate1_1` and `OBRate1_1`, are distinct,
valid schemas.

## Root cause (traced)

`renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/TypeMapping.kt:52-65`: the class of a
`NamingContext.Reference` is `root.name.toPascalCase() + suffix`, then `sanitizeSimpleName` (`:113`) strips every
character outside `[A-Za-z0-9_]`. `toPascalCase` (`typed/.../StringUtils.kt:31`) drops `_`, `-`, `.` and upper-cases
word starts. The mapping is computed per reference, nothing checks it is injective.
`renderer/.../Generate.kt:35-38` renders one `FileSpec` per model, with no comparison of file names (and none ignoring
case). The `Read`/`Write` suffix (`TypeMapping.kt:55-61`) is appended without checking that `<name>Read` is free.

Path classes already solve the same problem: `renderer/.../PathParamUtils.kt:47` (`uniqueNames`) compares class names
ignoring case and suffixes later ones with `2`, `3`, ….

## Correct final API

Every component is its own class in its own file, with names that are unique ignoring case and deterministic from
the document order. Following the path-node rule, the first declared keeps the name and later ones get a number:

```kotlin
// components: OB_Rate1_1, OBRate1_1            // components: HTTPHealthCheck, HttpHealthCheck
data class OBRate11(val a: String? = null)      data class HTTPHealthCheck(...)
data class OBRate112(val b: String? = null)     data class HttpHealthCheck2(...)
```

All references (`Model.Reference`, discriminator mappings, union cases) resolve through the same table. Wire names are
untouched (`@SerialName` on properties, nothing on the class). A warning diagnostic names the renamed schema.

## Fix slice

- renderer: build the `NamingContext.Reference -> ClassName` table once in `renderWithDiagnostics` (next to
  `contextSpecificNames`, `Generate.kt:27`), resolve collisions there (case-insensitive), carry it in `RenderConfig`
  and make `NamingContext.toClassName` read from it. Include the context-variant suffixes in the table so
  `Avatar`/Read cannot take a name a component already has.
- Open decisions: numeric suffix vs. keeping more of the original spelling (`OB_Rate1_1` -> `OB_Rate1_1`); which of
  the two keeps the bare name (document order is deterministic; "exact PascalCase match keeps it" is friendlier);
  whether nested class names inside a model need the same pass (not seen in the corpus).

## Proof

- Failing golden `ClientSpec` cases for the three reproducers (`renderGeneratedFiles` throws on the first and third
  today; the second needs an assertion that file names are unique ignoring case, which `renderGeneratedFiles` should
  gain).
- A compile check of the generated sources for the case-only reproducer on macOS CI.
- integration-tests: `github.json`, `youtrack.json` have no colliding schema names (checked with the renderer's naming
  rules ported to a script); `openai.yaml` not seen to have any. They would not catch a regression.

## Affected documents

Observed (5): openbanking.org.uk:account-info-openapi [efaa2372c5579d0ae471] (`OBRate10`, `OBRate11`, bodies differ),
amazonaws.com:health [2e3b3e8fc077cc957c8f] (`EventType`, bodies differ), amazonaws.com:dataexchange
[85e584aa1672e9b37f18] (`String`), bunq.com [117382bb372cf1e58763] (`<X>Read`, 23 files),
googleapis.com:compute [0164daed08129de21a3c] (`HTTPHealthCheck`/`HttpHealthCheck`, `HTTPSHealthCheck`/`HttpsHealthCheck`).

Declared in the spec, not yet reached or not used (static count over `components.schemas` keys): 24 documents with an
exact class-name collision (snyk.io, docusign.net ×10 pairs, just-eat.co.uk, telnyx.com, sendgrid.com, rebilly.com,
bbc.com, daniweb.com, superset.apache.local, 11 AWS services with `String`/`__string`, `Boolean`/`boolean`, …) and 8
with case-only collisions (googleapis.com:compute, googleapis.com:serviceconsumermanagement, xtrf.eu, docusign.net,
probely.com, amazonaws.com:elastic-inference, :quicksight, :cloudformation).
