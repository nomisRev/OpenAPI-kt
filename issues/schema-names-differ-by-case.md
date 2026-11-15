# Two schemas whose names differ only by case overwrite each other on a case-insensitive file system

**Status:** fixed in `6faa2a6d`. Class names are assigned once per schema and per `Read`/`Write` shape, compared
ignoring case: schemas in declaration order, then the shapes; a taken name is numbered from 2 with a Warning naming
both schemas (`Metadata2`, `HttpHealthCheck2`, `AvatarRead2`, a component literally named `AvatarRead` keeping its
name); schemas with one name and one shape (`__string`, `String`) stay one class without a diagnostic; wire names
(`@SerialName`, discriminator values) are unchanged. The duplicate-file checks of the renderer, `renderGeneratedFiles`
and the corpus worker ignore case. Proof: `SchemaNameCollisionsSpec`, goldens `client/schema-names-same`,
`client/schema-names-case`, `client/schema-names-suffix`, `client/schema-names-equal`, `SchemaNameCollisionsRuntimeTest`.
Re-checked with `--filter`: amazonaws.com:health, openbanking.org.uk:account-info-openapi, daniweb.com, bunq.com,
googleapis.com:compute (compiled on macOS) and amazonaws.com:dataexchange compile; sendgrid.com gets past render and
fails to compile on an unresolved `Post.Body` (as influxdata.com). Left open: a discriminator without a mapping whose
`oneOf` lists two schemas with one Kotlin name renders both subtypes as one nested class (the subtype names come from
the typed layer); a numbered name that ends in a digit reads oddly (`OBRate1_0` is `OBRate102`); the schema declared
first keeps the name even when the later one is already PascalCase (`eventType` keeps `EventType`).
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** emit (seen at compile)
**Impact:** 1 of 1,547 documents confirmed (googleapis.com:compute; no excluded errors, 4 errors in total). 7 more emitted documents have such schema pairs but show no unresolved reference from it (they fail on other errors).
**Source spec valid:** yes — component keys are case-sensitive (OAS 3.0.3 §4.7.7 Components Object)

## Symptom

```
model/HealthCheck.kt:29:31: error: unresolved reference 'HTTPHealthCheck'.
model/HealthCheck.kt:29:31: error: serializer was not found for type '??? (Unresolved qualified name: HTTPHealthCheck)?'
model/HealthCheck.kt:31:32: error: unresolved reference 'HTTPSHealthCheck'.
```

compute declares `HTTPHealthCheck` and `HttpHealthCheck` (and `HTTPSHealthCheck` / `HttpsHealthCheck`). One file `HTTPHealthCheck.kt` is on disk and it contains `data class HttpHealthCheck`.

## Minimal reproducer

`/private/tmp/claude-501/-Users-simonvergauwen-Developer-OpenAPI-kt/a0df68b9-ccc6-4d18-981e-753c6b12368d/scratchpad/repro/schema-names-differ-by-case.json`

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},
 "paths":{"/hc":{"get":{"responses":{"200":{"description":"ok","content":{"application/json":{"schema":{"$ref":"#/components/schemas/HealthCheck"}}}}}}}},
 "components":{"schemas":{
  "HealthCheck":{"type":"object","properties":{"a":{"$ref":"#/components/schemas/HTTPHealthCheck"},"b":{"$ref":"#/components/schemas/HttpHealthCheck"}}},
  "HTTPHealthCheck":{"type":"object","properties":{"port":{"type":"integer"}}},
  "HttpHealthCheck":{"type":"object","properties":{"host":{"type":"string"}}}}}}
```

Observed on macOS (APFS, case-insensitive): no diagnostics; `model/` holds `HealthCheck.kt` and `HTTPHealthCheck.kt` (content: `class HttpHealthCheck`); `model/HealthCheck.kt:12:17: error: unresolved reference 'HTTPHealthCheck'.`

Environment-dependent: on a case-sensitive file system the two source files coexist and this compiles. Not verified there. The class files `HTTPHealthCheck.class` / `HttpHealthCheck.class` would still collide for any consumer who unpacks or builds on macOS or Windows.

## What the spec says

`HTTPHealthCheck` and `HttpHealthCheck` are two distinct schemas.

## Root cause

Partly traced. Model files are named after the class, one top-level class per schema (`Model.Enum.toFileSpec` `EnumRenderer.kt:93-98` and its object/union twins), and nothing compares model names ignoring case. Path classes already do: `uniqueNames()` (`renderer/.../PathParamUtils.kt:47-70`) compares class names with `lowercase()` for exactly this reason. The harness check for duplicate files (`exploration/src/main/kotlin/CorpusWorker.kt:48`) compares exact names, so it does not flag it either. Not traced: where the plugin writes files and whether it could detect the overwrite.

## Correct final API

Both classes exist under names that differ by more than case, deterministically, with a diagnostic naming the rename:

```kotlin
public data class HTTPHealthCheck(...)      // first declared keeps its name
public data class HttpHealthCheck2(...)     // warning: schema 'HttpHealthCheck' renamed, collides with 'HTTPHealthCheck' ignoring case
```

Serial names and wire format are unaffected.

## Fix slice

- Renderer (or typed naming): a model-name uniqueness pass that ignores case, same rule as `uniqueNames()` (first keeps the name, later ones get the first free number), applied where `NamingContext.Reference` becomes a `ClassName` (`TypeMapping.kt:52-64`) so every reference follows.
- Open decision: suffix vs keeping both in one file. A suffix also fixes the class-file collision.
- Overlap: exact-name collisions after PascalCase (`Boolean`/`boolean`, `V1Beta1X`/`V1beta1X`) are the `conflicting declarations` cluster; one pass should cover both.

## Proof

- Failing test first: renderer test asserting the generated `FileSpec` names are unique ignoring case for the reproducer; golden files for both classes.
- Integration tests: no schema names differing only by case in `github.json` / `youtrack.json` (script). Not covered.

## Affected documents

googleapis.com:compute [0164daed08129de21a3c]. Total 1 confirmed. Pairs present without this error: amazonaws.com:elastic-inference [21c7b94aea37e168cae7], alexaforbusiness [2de6ed6b992b3531c9eb], storagegateway [9901e9c113969eadc758], signer [d7348556d586787ca720], frauddetector [e6befd8e32abdd7a6a1c], googleapis.com:serviceconsumermanagement [6d0ff65dcef19640c7b5], docusign.net [ac5d9fe0b91a84bec43a].
