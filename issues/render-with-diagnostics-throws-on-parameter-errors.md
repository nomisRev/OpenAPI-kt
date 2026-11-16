# `renderWithDiagnostics` throws for parameter errors but returns form-body errors, so warnings are lost and the census miscounts

**Status:** fixed in `84ab0bcd`. `renderWithDiagnostics` and `generateClientWithDiagnostics` return every diagnostic
once and never throw for one; an operation with an `Error` of its own (unsupported parameter, a body without a sendable
variant, required or optional, the 32-overload limit) is not rendered, and path classes left empty are pruned. Skipping
a variant next to a sendable one stays a warning. The census worker fails such a document in a new `diagnostics`
phase (after render, before emit). Re-checked on the census of `7bf24f56`: the 41 render failures now fail there with
their warnings; only 5 documents still had a body-less operation (not 14: the untyped-field fix took the Twilio ones),
salesloft.com, apacta.com, fungenerators.com:barcode, digitallocker.gov.in:authpartner and mineskin.org, and they are
now refused. Not covered by a test: the charset and wildcard errors, which no corpus document has. Kept open:
whether an `Error` can be made non-fatal in the plugin; supporting the refused shapes is in the parameter and form-field
reports.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** render (API behaviour) / harness artefact
**Impact:** 57 of 1,547 documents are rejected by the Gradle task for an `Error` diagnostic; the census shows only 43 of them as failures. The other 14 (12 `twilio.com:*`, digitallocker.gov.in:authpartner, mineskin.org) are counted as emitted — 12 of them as `compile: pass`.
**Source spec valid:** n/a — this is about our API and the harness, not a spec construct

## Symptom

Two documents with the same kind of problem ("something has no supported serialization, generation must stop")
behave differently through `ApiTree.renderWithDiagnostics`:

1. Unsupported **parameter**: the call throws `IllegalArgumentException: OpenAPI generation failed: ...`. The caller
   never receives the `GenerationResult`, so every `Warning` of the document (ignored headers, renamed paths,
   skipped form overloads, property overrides) is lost.
2. Unsupported **form body** with no alternative: the call returns normally with an `Error` in `diagnostics` and
   with files in which the operation has silently lost its request body.

Consequences:

- Gradle plugin (`gradle-plugin/.../GenerateOpenApiDefinition.kt:49-57`): for case 1 the exception escapes
  `generateWithDiagnostics` before the logging loop, so the user sees the exception but none of the warnings. For
  case 2 it logs everything and then `throwOnErrors()` fails the task. Both fail, with different output.
- Census harness (`exploration/src/main/kotlin/CorpusWorker.kt:39-44`): calls `renderWithDiagnostics` and writes
  the files without checking for `Error` diagnostics. Case 1 is recorded as a render failure (43 documents); case 2
  as a pass. The 445 "pass" total includes 12 documents the Gradle task would reject, and their emitted sources
  contain body-less operations.

## Minimal reproducer

Case 1, `<scratchpad>/repro/query-nested-array-of-objects.json`:

```
worker exit=1 phases: parse pass, typed pass, render fail
java.lang.IllegalArgumentException: OpenAPI generation failed:
- GET /tags parameter Tags (in query, style form, explode true) has no supported serialization for its type, location, `style` and `explode`.
	at io.github.nomisrev.openapi.GenerationDiagnosticsKt.throwOnErrors(GenerationDiagnostics.kt:31)
	at io.github.nomisrev.openapi.GenerationDiagnosticsKt.throwOnErrors(GenerationDiagnostics.kt:15)
	at io.github.nomisrev.openapi.ClientRendererKt.generateClient(ClientRenderer.kt:9)
```

Case 2, `<scratchpad>/repro/untyped-form-field.json` (a required-body POST whose form has one unsupported field):

```
worker exit=0 phases: parse, typed, render, emit pass
diagnostics: [{"severity":"Error","message":"POST /x (application/x-www-form-urlencoded) contains form fields [Parameters] that have no supported form serialization for their type and encoding (`style`, `explode`), and no alternative supported request body variant exists."}]
compile exit=0
```

and the emitted `api/X.kt`:

```kotlin
public suspend operator fun invoke(): String {
  val response = client.post("x")
```

## What the spec says

Nothing; this is the contract of our own API. The name `renderWithDiagnostics` and the `GenerationResult` type
(`files` + `diagnostics`) promise that diagnostics are returned and the caller decides (`throwOnErrors`).

## Root cause

`renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/Generate.kt:24-32`:

```kotlin
fun ApiTree.renderWithDiagnostics(config: RenderConfig): GenerationResult {
    val diagnostics = generationDiagnostics()
    ...
    val filteredTree = withSupportedRequestBodiesOnly()
    return GenerationResult(
        files = filteredTree.generateModels(enrichedConfig) + filteredTree.generateClient(enrichedConfig),
```

`generateClient` is the throwing variant (`ClientRenderer.kt:7-10`: `generateClientWithDiagnostics(config).also(GenerationResult::throwOnErrors).files`).
It recomputes `generationDiagnostics()` on `filteredTree` (`ClientRenderer.kt:12-13`). On the filtered tree the
form-body errors are gone, because `withSupportedRequestBodiesOnly` removed the offending bodies
(`GenerationDiagnostics.kt:177-195`), but parameter errors are still there
(`GenerationDiagnostics.kt:281-294`), so it throws for exactly that class.

The body-less operation in case 2 is `Route.withSupportedRequestBodiesOnly` returning `body = null` when no variant
is supported (`GenerationDiagnostics.kt:188-195`).

## Correct final API

- `renderWithDiagnostics` / `generateWithDiagnostics` never throw for a diagnosable condition; they return every
  diagnostic of the document once.
- When the result contains an `Error`, the affected operation is not rendered at all (no body-less variant of an
  operation whose body is required), so a caller that chooses to continue past errors gets a client that is
  smaller, never one that sends a different request than declared.
- `render` / `generate` / the Gradle task keep failing on errors, after logging all diagnostics.

## Fix slice

- renderer: in `renderWithDiagnostics`, call the non-throwing `generateClientFiles` path
  (or `generateClientWithDiagnostics(...).files`) instead of `generateClient`; compute diagnostics once.
- renderer: operations with an `Error` diagnostic (unsupported parameter, unsupported only-body) are filtered out
  of the tree before rendering — the same mechanism `issues/query-parameter-nested-objects-refused.md` asks for.
- exploration harness: `CorpusWorker.kt` should record a `render` failure (or a distinct `diagnostic-error` status)
  when `result.diagnostics` contains an `Error`, so the census matches what a user of the plugin gets.
- Open decision: whether `Error` should remain a hard failure in the plugin or become configurable
  (see the nested-parameter report).

## Proof

- Failing test first (renderer): `apiTree.renderWithDiagnostics(config)` on the case 1 reproducer does not throw
  and returns one `Error` plus the files of the other operations.
- Renderer test for case 2: no generated `invoke()` without the body; either the operation is absent or generation
  fails.
- Harness: rerun the 14 documents below; they should move from pass/compile to the refused bucket.
- integration-tests coverage: none of the three specs produces an `Error` diagnostic, so nothing there exercises
  this path.

## Affected documents

Returned-but-ignored errors (case 2): twilio.com:twilio_autopilot_v1 [41cc229da986f817563a],
twilio_events_v1 [24906f856d2931e35fe6], twilio_flex_v1 [6654764630bfdad7be8d], twilio_media_v1 [68fe30612d35216f7f03],
twilio_notify_v1 [38b611612a912b0901f7], twilio_numbers_v2 [85cbd463e9ca37c08528], twilio_preview [d0e7285c3c9fac8e646d],
twilio_studio_v1 [14608c4cf7fae941e03b], twilio_studio_v2 [27168fdaa55d00c80a53], twilio_sync_v1 [9f32366870d3f1c5328a],
plus twilio_trusthub_v1, twilio_verify_v2, digitallocker.gov.in:authpartner, mineskin.org — 14 in total
(12 `compile: pass`, 2 `compile: fail` for unrelated reasons). Thrown errors (case 1): the 43 render failures listed
across the parameter reports in this directory.
