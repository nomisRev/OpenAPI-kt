# The client class is named `info.title` verbatim: empty titles crash, titles with `:` do not compile

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** render (empty title) / compile (illegal characters)
**Impact:** 17 of 1,547 documents: 10 crash at render (ote-godaddy.com ×8, hubapi.com:auth, drchrono.com), 7 fail to compile (icons8.com, geodesystems.com, sportsdata.io:csgo-v3-scores, sportsdata.io:csgo-v3-stats, listennotes.com, etsi.local:MEC010-2_AppPkgMgmt, betfair.com). 172 more get a class name that only exists in back-ticks
**Source spec valid:** yes — `title` is a REQUIRED string, any string

## Symptom

- `info.title: ""` -> `IllegalArgumentException: simpleNames must not contain empty items: []` thrown from KotlinPoet,
  no diagnostic. All 10 documents of this family have exactly `"title": ""` (checked each). No other trigger exists
  in the corpus, but a title of only separators (`" "`, `"---"`) fails the same way (tried).
- A title containing `:` (also `.`-free forms of `/ ; < > [ ] \`) -> a file `ListenAPI:PodcastSearch.kt` and
  `error: name contains illegal characters: :`.
- Anything else is accepted as is: "API docs | logoraisr.com" becomes
  ``public class `APIDocs|logoraisrCom` `` in `APIDocs|logoraisrCom.kt`. That compiles on the JVM (15 such documents
  pass the census) but is not a name anyone can type comfortably, is illegal on Kotlin/JS and Native (the default
  `RenderConfig.targets` includes JS), and is an awkward file name. 172 documents have such a title
  (commas, parentheses, `&`, `|`, `'`, …), most from apisetu.gov.in.

## Minimal reproducer

`<scratchpad>/repro/client-name-empty-title.json`

```json
{ "openapi": "3.0.3", "info": { "title": "", "version": "1" },
  "paths": { "/ping": { "get": { "operationId": "g", "responses": { "204": { "description": "ok" } } } } } }
```
```
worker exit=1 phases: [parse pass, typed pass, render fail]
java.lang.IllegalArgumentException: simpleNames must not contain empty items: []
	at com.squareup.kotlinpoet.ClassName.<init>(ClassName.kt:58)
	at io.github.nomisrev.openapi.ClientRendererKt.generateClientFiles(ClientRenderer.kt:29)
```

`<scratchpad>/repro/client-name-title-illegal-characters.json` — same with `"title": "Listen API: Podcast Search"`

```
emit pass, compile exit=1
api/ListenAPI:PodcastSearch.kt:13:14: error: name contains illegal characters: :.
```

## What the spec says

OAS 3.0.3 Info Object: `title` — "string, REQUIRED. The title of the API."
(https://spec.openapis.org/oas/v3.0.3.html#info-object). It is prose for humans; an empty string satisfies the type.
icons8's title is a Markdown sentence: "Use a [New Version](https://icons8.github.io/icons8-docs/) Instead".

## Root cause (traced)

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/ApiTree.kt:66`: `name: String = info.title.toPascalCase()`.
`toPascalCase` (`typed/.../StringUtils.kt:31`) only splits on ` - _ . / [ * ]` and removes those; every other
character stays. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRenderer.kt:28-29` uses the result
unchecked as class and file name (`:45`), `renderer/.../ServerStrategy.kt:23-24` as `<name>Server`.
KotlinPoet back-ticks what is not an identifier; Kotlin/JVM rejects back-ticked names containing `. ; [ ] / < > : \`.

There is also no way for a user to choose the name: `renderer/.../Generate.kt:18` calls `toApiTree(preprocessor)`
without `name`, and `gradle-plugin/.../GenerateOpenApiDefinition.kt:41-45` has no such option.

## Correct final API

- A configurable client name (`clientName` on the Gradle extension / `RenderConfig`), which wins when set.
- Otherwise a name derived from the title that is always a plain Kotlin identifier: split on every non-identifier
  character, PascalCase the words, prefix `_` if it starts with a digit, escape keywords.
  "Listen API: Podcast Search, Directory, and Insights API" -> `ListenAPIPodcastSearchDirectoryAndInsightsAPI`,
  "CS:GO v3 Scores" -> `CSGOV3Scores`, "API docs | logoraisr.com" -> `APIDocsLogoraisrCom`.
- When nothing is left (empty title): an **error diagnostic** — "info.title is empty; set `clientName`" — or a fixed
  fallback (`Api`/`Client`). The project's "no silent drops" preference argues for the diagnostic plus the option.

## Fix slice

- typed/renderer: one `toClassNameIdentifier()` used for the root name (and reused by server cases and path
  literals, see `server-case-name-not-identifier.md`, `path-literal-segment-not-a-kotlin-identifier.md`).
- renderer: `GenerationDiagnostics` entry for an empty derived name instead of the KotlinPoet exception.
- gradle-plugin: `clientName` property passed through `generateWithDiagnostics` to `toApiTree(name = …)`.
- Open decisions: fallback name vs. hard error; whether cleaning up the 172 back-ticked names is acceptable as a
  rename for existing users (their titles are rare in hand-written specs; the three integration specs are unaffected).

## Proof

- Failing renderer tests: the two reproducers as golden `ClientSpec` cases (the first expects a diagnostic, the
  second a file `ListenAPIPodcastSearch.kt`).
- A property-style test: for arbitrary titles the derived name matches `[A-Za-z_][A-Za-z0-9_]*` or yields the
  diagnostic.
- integration-tests: titles "GitHub v3 REST API", "OpenAI API", "YouTrack REST API" are all plain; no coverage.

## Affected documents

Empty title (10): ote-godaddy.com:orders [1dac2cc33d9ba6b031b5], :aftermarket [20760f59d4129ddf0787], :subscriptions
[28d69af8eefa94f9ecee], :abuse [41c6326d3eb9f346c070], :domains [6986388ab1112edf6656], :countries
[73f2aa827c697aa025cc], :certificates [7845b09a227f45ed23d7], :agreements [ded085b1f35e36a3963b], hubapi.com:auth
[4a2ffe18230e08e0e88c], drchrono.com [b75c7eb289e5ee0396ff]. (ote-godaddy.com:shoppers and ipinfodb.com also have an
empty title but stop earlier / have no paths.)

Illegal characters (7): icons8.com [015d29f8eb70c9a9df7b], geodesystems.com [314b1e498b2676823819],
sportsdata.io:csgo-v3-scores [42ab1e5d51e2311b62a0], sportsdata.io:csgo-v3-stats [96deaf8dc87f122186fb],
listennotes.com [6d6fa8f2eaf506f947ce], etsi.local:MEC010-2_AppPkgMgmt [7fcdd86e785944491843], betfair.com
[bd8e65bcd59931177bfb]. Four more have such a title but fail earlier: zuora.com, gerermesaffaires.com, viator.com,
mastodon.local.
