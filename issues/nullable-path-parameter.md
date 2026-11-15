# A path parameter whose schema is `nullable: true` is generated as `String?` and does not compile

**Status:** fixed in `dfc4cf2d`: a path parameter declared nullable is generated as not nullable, with a warning.
Covered by the `/notes/{id}` case of golden `client/shared-path-parameter-types`.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 5 of 1,547 documents (gettyimages.com, nbg.gr, parliament.uk:writtenquestions, parliament.uk:treaties, vtex.local:Intelligent-Search-API); 2 more have the construct and fail earlier (microsoft.com:graph, vtex.local:Subscriptions-API-(v3))
**Source spec valid:** yes syntactically (`nullable` is a Schema Object keyword and allowed on any schema), but meaningless: a path parameter is always required and a URL segment has no representation of null. .NET/Swashbuckle emits it for every `string` route parameter.

## Symptom

```
api/V3.kt:453:67: error: only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'String?'.
    val response = client.post("v3/asset-licensing/${assetId.encodeURLParameter()}") {
```

and the public API exposes the nullability: `public fun assetId(assetId: String?): AssetIdPath`.

This is one of two causes behind the "nullable receiver in the path template" family. The other — an optional query/header/body parameter with the same Kotlin name shadowing the path property inside `invoke` (docusign, authentiq, interactivebrokers, warwick enterobase, aws apigateway) — is `issues/operation-parameter-shadows-path-parameter.md`.

## Minimal reproducer

`<scratchpad>/repro/nullable-path-parameter.json`:

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},"paths":{
 "/boards/{board_id}":{"get":{"parameters":[
   {"name":"board_id","in":"path","required":true,"schema":{"type":"string","nullable":true}}],
  "responses":{"200":{"description":"ok"}}}}}}
```

```
worker exit=0 phases: parse pass, typed pass, render pass, emit pass
diagnostics: []
compile exit=1
api/Boards.kt:25:52: error: only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'String?'.
```

```kotlin
public fun boardId(boardId: String?): BoardIdPath = BoardIdPath(client, boardId)
public class BoardIdPath internal constructor(private val client: HttpClient, private val boardId: String?) {
  …
      val response = client.get("boards/${boardId.encodeURLParameter()}")
```

## What the spec says

- OAS 3.0.3 §4.7.12.1 Parameter Object, `required`: "If the parameter location is "path", this property is REQUIRED and its value MUST be true."
- §4.7.8.2 Path Templating: "Each template expression in the path MUST correspond to a path parameter"; §4.7.12 style `simple` follows RFC 6570, where an undefined value expands to nothing — which would change the path to a different resource (`/boards/`), so "null" cannot be sent.
- §4.7.24 `nullable`: "A true value adds "null" to the allowed type". It is a property of the JSON value space; a path segment carries a string, so the keyword has no wire meaning here. (3.1: `type: ["string","null"]`, same reasoning.)

All five failing documents declare `required: true` together with `nullable: true`. No census document has a path parameter with `required` absent or false.

## Root cause

Traced.

- typed: `Parameter.toRouteInput` (`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/SchemaContext.kt:67-97`) converts the parameter schema with `schema.toModel(...)` for every location alike, so `nullable: true` becomes `isNullable = true` on the model; `toRoute` (`routes/Route.kt:96-99`) hands that model to `parsePathSegments`, and `PathSegment.Parameter.model` (`PathSegment.kt:16`) carries it.
- renderer: `addClientConstructorAndState` (`renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt:75-78`) and `addConcreteNavigationMember` (`ImplRenderer.kt:136-140`) use `model.publicInputTypeName(config)` → `String?`.
- renderer: `Route.pathLiteral` (`ParameterSerialization.kt:446-458`) → `expansionCode` → `ScalarValue.code` (`ParameterSerialization.kt:221-223`) emits `expr.encodeURLParameter()` assuming a non-null receiver.

Probably surfaced by 4f5c259c ("Percent-encode path parameter values as a path segment"): before it the template was `"$boardId"`, which compiles for `String?` and sends the text `null`. Not verified against an older build (none of the five documents is in the Oct 31 pilot). Non-string nullable path parameters (`integer`, as in microsoft.com:graph) still take the `$id` branch (`needsEncoding` false) and would compile and send `null` — same bug, silent.

## Correct final API

A path parameter is never nullable:

```kotlin
public fun boardId(boardId: String): BoardIdPath        // GET boards/<percent-encoded id>
```

with a `GenerationDiagnostics` warning per parameter ("path parameter `board_id` of GET /boards/{board_id} is declared nullable; a path segment cannot be null, generated as non-null"), so the spec oddity is not swallowed silently. If the path parameter is a `$ref` to a nullable component schema, only the navigation argument loses the `?`; the component type itself is untouched.

## Fix slice

- typed (preferred): strip nullability from the model of an `in: path` input in `toRouteInput`, where location is known, so `Route.Input.type`, `PathSegment.Parameter.model` and enum/union path expansion (`Route.kt:393`) all agree; record the fact for a diagnostic (e.g. on `Route.ignoredParameters`-style metadata or a flag on `Route.Input`).
- renderer: report it in `GenerationDiagnostics.kt`; no change to `pathLiteral` needed once the model is non-null.
- Open decision: warning vs silent normalisation. The project rule "no silent drops" argues for the warning; it is noisy for Swashbuckle specs (26 parameters in gettyimages), so one warning per document with a count is an option.

## Proof

- Typed test: a `nullable: true` path parameter yields a non-nullable `PathSegment.Parameter.model` and `Route.Input.type` (table in `typed/src/commonTest/kotlin/io/github/nomisrev/PathSegmentSpec.kt` or the route tests beside it).
- Golden `ClientSpec` case with the reproducer: `fun boardId(boardId: String)`; plus a nullable `integer` path parameter to pin the silent `null` variant.
- MockEngine test: `api.boards.boardId("a b").get()` requests `boards/a%20b`.
- integration-tests do not cover it: no path parameter in `github.json` or `youtrack.json` has `nullable: true` or a `null` type (scanned every operation and path-item parameter); `openai.yaml` path parameters are plain `type: string` (not exhaustively scanned, YAML).

## Affected documents

gettyimages.com [39b95f12bb81ffa34f51] (26 parameters; also 8 excluded-family errors), nbg.gr [e8f5388cdef68ed84896] (19; only error), parliament.uk:writtenquestions [8492c37fc8b42002d93b] (only error), parliament.uk:treaties [fda629d9611d7e4f910c] (only error), vtex.local:Intelligent-Search-API [c4eb78bf452660a971c9] (3; also 7 excluded-family errors). Total 5 compile failures; 3 would compile after the fix. Construct present but masked by an earlier typed failure: microsoft.com:graph [cb0f197c9832ed0801d0] (51 parameters, 2 of them integers), vtex.local:Subscriptions-API-(v3) [9d195a7990624dee9dc2].
