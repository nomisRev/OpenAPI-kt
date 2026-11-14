# A literal path segment becomes a class name verbatim: `entities:search` does not compile, `-` has no name at all

**Status:** fixed (branch openapi-3.2-streaming). A `:` splits a literal segment into a chain like the text of `{name}:cancel` (`PathSegment.toSteps`): `/v1/entities:search` is `api.v1.entities.search.get()`, request path unchanged, and `entities` is shared with `/v1/entities`; next to `/v1/entities/search` the step is `colonSearch`. Only `:` splits (`secret-scanning`, `a.b`, `lists.json` stay one step). A leading `:` joins the first step to its parent, a trailing one trails the last, a segment of colons only stays one step. Names of literal and fixed-value steps treat every character that is not a letter or digit as a word break (`a@b` and `a=b` are `AB`; a collision is numbered with a warning), and a segment without a letter or digit is spelled with the `SeparatorWords` (`-` is `dash`, `...` is `dotDotDot`; added: apostrophe, parens, percent, brackets, braces, question, less, greater, caret, quote, backslash, backtick, space; any other character is `u<hex>`). Still open: `$` in a literal is still written unescaped in the request string (`dollar-in-path-literal-interpolated.md`); `#`/`?` keys.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile (`:` in a class name) / emit (empty class name)
**Impact:** about 93 of 1,547 documents observed: ~91 fail to compile on a literal segment with `:` (googleapis.com custom verbs such as `/v1/entities:search`, adobe.com:aem, clever-cloud.com, google.com), 2 crash at emit on a segment `-` (googleapis.com:firebaseappcheck, amazonaws.com:codeguruprofiler). 128 documents contain such a segment; 212 more get class names that only exist in back-ticks
**Source spec valid:** yes for `:`, `-`, `$`, `~`, `@`, `=`, `,` (all `pchar`); no for `#…` and `?…` inside a path key (AWS/APIs.guru artefact)

## Symptom

- `/v1/entities:search` -> ``public class `Entities:search` `` -> `error: name contains illegal characters: :`.
  Kotlin/JVM forbids `. ; [ ] / < > : \` in back-ticked names; `:` is the only one that occurs in path literals of the
  corpus (127 documents), `;` once.
- `/apps/-/config` -> the class for `-` is named `""` -> `NoSuchElementException: Char sequence is empty.` when
  KotlinPoet writes the file. Same for a segment of only `_`, `*`, `.`, `[`, `]` (`_`, `__`, `*`, `...` tried).
- Everything else is accepted in back-ticks: AWS `/#X-Amz-Target=AWSMigrationHub.ListCreatedArtifacts` becomes file
  `#XAmzTarget=AWSMigrationHubListCreatedArtifacts.kt` with a class of that name. It compiles on the JVM only (Kotlin/JS
  and Native reject such names; `RenderConfig.targets` defaults to JVM + JS) and is unusable from an IDE.

The request URL is right in these cases (`client.get("v1/entities:search")`); this is purely a naming failure.
Segments that contain a `{parameter}` are a different bug with wrong URLs: `composite-path-segments.md`.
Of the 167 documents whose api files have a class name with `:`, 114 have the embedded-parameter form, 94 a plain
literal (3 of those from server descriptions), 7 the client name (`client-name-from-info-title-not-an-identifier.md`).
Related literal-segment reports by other investigators: `dollar-in-path-literal-interpolated.md`,
`path-literal-segment-loses-navigation-property.md`, `path-segment-named-like-http-method.md`.

## Minimal reproducer

`<scratchpad>/repro/path-literal-segment-illegal-characters.json`

```json
{ "openapi": "3.0.0", "info": { "title": "Repro", "version": "1" },
  "paths": { "/v1/entities:search": { "get": { "operationId": "search",
    "responses": { "204": { "description": "ok" } } } } } }
```
```
compile exit=1
api/V1.kt:12:16: error: name contains illegal characters: :.
```
```kotlin
public class V1 internal constructor(private val client: HttpClient) {
  public val entities_search: `Entities:search` = `Entities:search`(client)
  public class `Entities:search` internal constructor(private val client: HttpClient) { … client.get("v1/entities:search") … }
}
```

`<scratchpad>/repro/path-segment-without-identifier-chars.json` — path `/apps/-/config`

```
worker exit=1 phases: [parse pass, typed pass, render pass, emit fail]
java.util.NoSuchElementException: Char sequence is empty.
	at com.squareup.kotlinpoet.UtilKt.escapeIfNotJavaIdentifier(Util.kt:357)
```
(diagnosed with `<scratchpad>/naming/EmitDiag.kt`: the failing type is `V1.ParentPath.Apps.` + `""`.)

## What the spec says

OAS 3.2.0 Path Templating ABNF (https://spec.openapis.org/oas/v3.2.0.html#path-templating):
`path-literal = 1*pchar`, `pchar = unreserved / pct-encoded / sub-delims / ":" / "@"`. So `:`, `@`, `-`, `.`, `_`, `~`,
`$`, `=`, `,`, `;`, `!`, `*`, `'`, `(`, `)`, `&`, `+` are all legal in a literal segment; `#` and `?` are not. OAS 3.0.3
only says the key "MUST begin with a forward slash" and is appended to the server URL. The `-` segment is Google's
wildcard collection id (`/v1/{parent}/apps/-/appAttestConfig:batchGet`).

For the AWS documents: the key `/#X-Amz-Target=Service.Operation` stands for `POST /` with a required
`X-Amz-Target` header (declared as a header parameter with a one-value enum). The generated call is
`client.post("#X-Amz-Target=…")`; a fragment is normally not sent, so this probably requests the base URL, but that
was not verified against a MockEngine. 137 documents have keys starting with `/#` and 105 more a `…#param` suffix
(242 in total, none compiles today, for other reasons).

## Root cause (traced)

`renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/PathParamUtils.kt:7-12` `routeSegmentSimpleName()`:
`is PathSegment.Literal -> navigationName.toPascalCase()`. `toPascalCase` (`typed/.../StringUtils.kt:3-4, 31`)
removes only ` - _ . / [ * ]`. Nothing sanitises the rest, and only the *empty* segment has a fallback name
(`navigationName`, `PathParamUtils.kt:19-20`, gives `slash`); a segment that becomes empty after PascalCasing does
not. The member name goes through `toParamName()` (`renderer/.../NamingUtils.kt:39`), which does sanitise
(`entities_search`) and special-cases a leading `-`, so only the class side is broken. The same string is the file
name for first-level nodes (`renderer/.../ClientRenderer.kt:33-37`).

## Correct final API

Class and member names are plain identifiers derived from the segment; the URL keeps the segment verbatim.

```kotlin
client.v1.entitiesSearch.get()                 // GET v1/entities:search      class V1.EntitiesSearch
client.v1.parent(p).apps.dash.appAttestConfigBatchGet.get()   // …/apps/-/appAttestConfig:batchGet
```

- Split on every non-identifier character, PascalCase/camelCase the words (`entities:search` -> `EntitiesSearch` /
  `entitiesSearch`; today's member is `entities_search`).
- A segment with no identifier characters gets a spelled-out name, like the existing `slash` for the empty segment
  and `Star`/`Slash` for enum values: `-` -> `dash`, `*` -> `star`, `_` -> `underscore`, otherwise `segment`.
- Collisions created by the stricter rule (`a:b` vs `a-b` vs `aB`) are already resolved by `uniqueNames`
  (`PathParamUtils.kt:47`).

## Fix slice

- renderer: one identifier sanitiser for class names (shared with the client name and server cases) applied in
  `routeSegmentSimpleName` for `Literal` and `FixedValue`, plus the non-identifier fallback table; make
  `navigationMemberName` agree with it.
- Open decisions: `entities_search` (current member) vs `entitiesSearch`; whether to special-case the Google custom
  verb (`entities:search` -> `entities.search()`); what to do with `#…`/`?…` keys — strip the fragment from the name
  *and* the URL with a warning, or reject with a diagnostic. Stripping changes the node a route lands on (AWS has
  hundreds of operations on `/`), so it belongs with the AWS work, not here.

## Proof

- Failing golden `ClientSpec` cases for both reproducers; a naming unit test table next to
  `PathSegmentSpec`/the `uniqueNames` tests (`entities:search`, `-`, `*`, `_`, `a;b`, `$batch`, `~`, `@me`).
- MockEngine runtime test: the requested path is still `v1/entities:search` and `apps/-/config`.
- integration-tests: `github.json`, `youtrack.json`, `openai.yaml` have no literal segment with `:` or without
  identifier characters (all paths checked). No coverage.

## Affected documents

Literal `:` (94 documents have a non-parameter class name with `:` in api files): googleapis.com:kgsearch
[01ee76d8f766aa7fe472], googleapis.com:tagmanager [01585979764a10bc658e], googleapis.com:servicemanagement
[05e6304d3f87768e910a], googleapis.com:acceleratedmobilepageurl [d33ce4fc96acf7cf854a], googleapis.com:admin
[bb482d4e73a640ea90b8], googleapis.com:admob [982d7b8272ec3bf4d15a], googleapis.com:alertcenter
[5fba4969c64edb1b6667], adobe.com:aem [14d821f7967d48e181f6], clever-cloud.com [9f3fa3d55fd0df3d1e6a], google.com
[93e4055f164a1a7fd347]. Static: 128 documents have a literal segment with `:` or `;` (39 stop at emit, 2 at typed).
Empty name (2): googleapis.com:firebaseappcheck [9edf32b827ff0bc7c1a3], amazonaws.com:codeguruprofiler
[9fc7baa006356c746d8f]; one more document has such a segment but fails at typed.
