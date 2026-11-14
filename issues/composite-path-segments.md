# Path segments that mix template expressions with literal text (`{id}.json`, `{a}.{b}`, `v1.{format}`, `{name}:cancel`)

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** wrong output (silent) and compile, one root cause in typed
**Impact:** 303 of 1,547 documents have such a segment. 209 of them emit sources: 202 send the template text (`{id}`) on the wire in at least one operation, 9 of those compile cleanly (silent), 16 fail with syntax errors, 114 fail with `name contains illegal characters` (Google `{name}:verb`). The other 94 fail earlier for unrelated reasons and will hit this next. Providers: googleapis.com (171), amazonaws.com (80, `{arn}#tagKeys`), callfire, tomtom, healthcare.gov, gov.bc.ca, nytimes, gitea, twilio, jellyfin, discourse.
**Source spec valid:** yes for `{id}.json`, `{a}.{b}`, `v1.{format}`, `{name}:verb` (a template expression is a section of a path, not a whole segment). No for AWS `/tags/{arn}#tagKeys` (a fragment in a path key) and NASA `{id}{.format}` (RFC 6570 label operator, not OpenAPI).

## Symptom

Three shapes, three outcomes, no diagnostic in any of them (`diagnostics.json` is `[]`):

| Segment | Parsed as | Outcome |
|---|---|---|
| `{id}.{extension}`, `{a},{b}`, `{a}{b}`, `{o}-{d}` (starts with `{`, ends with `}`) | one `Parameter` named `id}.{extension` | does not compile: `unresolved reference 'Id}{extensionPath'`, `unresolved reference 'Get'`, `function declaration must have a name`, `modifier 'public' is not applicable to 'local class'`, `only one lambda expression is allowed…` (lufthansa) — all cascades of one line |
| `{id}.json`, `{id}.mp3`, `{name}:cancel`, `{arn}#tagKeys` | `Literal("{id}.json")` | compiles (or `name contains illegal characters: :` for `:`); request path is the text `hackathons/{id}.json`; the declared `id` path parameter is dropped from the API |
| `lists.{format}`, `v{version}`, `image{n}`, `regions={region}` | `Literal("lists.{format}")` | same: compiles, sends `lists.{format}` |

Census example of the silent case, hackathonwatch.com [2183fce1b47012e53a07], compile = pass:

```kotlin
public val id_Json: `{id}Json` = `{id}Json`(client)
…
public suspend operator fun invoke() {
  val response = client.get("hackathons/{id}.json")
```

## Minimal reproducer

`<scratchpad>/repro/composite-path-segment-multi.json`:

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},"paths":{
 "/media/{id}.{extension}":{"get":{"parameters":[
   {"name":"id","in":"path","required":true,"schema":{"type":"string"}},
   {"name":"extension","in":"path","required":true,"schema":{"type":"string"}}],
  "responses":{"200":{"description":"ok"}}}}}}
```

```
worker exit=0 phases: parse pass, typed pass, render pass, emit pass
diagnostics: []
compile exit=1
api/Media.kt:11:56: error: unresolved reference 'Id}{extensionPath'.
api/Media.kt:11:106: error: unresolved reference 'id'.
api/Media.kt:11:108: error: syntax error: Expecting ')'.
api/Media.kt:11:109: error: function declaration must have a name.
api/Media.kt:13:3: error: modifier 'public' is not applicable to 'local class'.
api/Media.kt:17:23: error: unresolved reference 'Get'.
```

```kotlin
public fun `id}{extension`(`id}{extension`: String): `Id}{extensionPath` = `Id}{extensionPath`(client, id}{extension)
…
      val response = client.get("media/$id}{extension")
```

`<scratchpad>/repro/composite-path-segment-silent.json` (paths `/hackathons/{id}.json` and `/lists.{format}`, each with its path parameter declared): all phases pass, `diagnostics: []`, `compile exit=0`, and

```kotlin
public val lists_format: `Lists{format}` = `Lists{format}`(client)
public val id_Json: `{id}Json` = `{id}Json`(client)
    public suspend operator fun invoke() {            // no `id` / `format` parameter anywhere
      val response = client.get("hackathons/{id}.json")
      val response = client.get("lists.{format}")
```

## What the spec says

- OAS 3.0.3 §4.7.8.2 Path Templating: "Path templating refers to the usage of template expressions, delimited by curly braces ({}), to mark a **section** of a URL path as replaceable using path parameters." Nothing restricts an expression to a whole segment.
- OAS 3.1.1 §4.8.2 / 3.2.0 make it explicit with ABNF: `path-segment = 1*( path-literal / template-expression )`, `template-expression = "{" template-expression-param-name "}"`. So `{id}.{ext}` and `v{version}` are valid; a name cannot contain `{`, `}`.
- Each template expression MUST correspond to a path parameter of the path item or operation; path parameters are always `required: true` (§4.7.12.1).
- Not valid: a path key with `#fragment` (AWS, 80 docs; converter artefact used to make keys unique) and `{.format}` (NASA; RFC 6570 label operator, not OpenAPI). These want a diagnostic, not a guess.

## Root cause

Traced. `parsePathSegments`, `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/PathSegment.kt:55-69`:

```kotlin
return relative.split("/").map { segment ->
    if (segment.startsWith("{") && segment.endsWith("}")) {
        val paramName = segment.removeSurrounding("{", "}")      // "{id}.{extension}" -> "id}.{extension"
        (pathParamTypes[paramName] ?: DefaultPathParameterType).toPathSegment(paramName)
    } else {
        PathSegment.Literal(segment)                              // "{id}.json", "lists.{format}" kept verbatim
    }
}
```

`PathSegment` (same file, lines 6-37) has no variant for a segment made of several parts, so nothing downstream can do better:

- `Route.pathLiteral`, `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ParameterSerialization.kt:443` writes a `Literal` name with `%L` as is, braces included (and `$` unescaped — the `$reset` / `$links` failures of another report come from this same line).
- `routeSegmentSimpleName` / `navigationMemberName`, `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/PathParamUtils.kt:7-27` turn the mangled name into `` `Id}{extensionPath` `` and `` `{id}Json` ``; the constructor call passes the parameter name with `%L`, unescaped, which is where the syntax errors start.
- Meanwhile `extractPathParameters` (`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/SchemaContext.kt:124-129`, regex `\{.*?\}`) does find `id` and `extension`, and `resolveParameters` keeps both as `Route.Input(input = Path)`. They match no segment, so the renderer neither stores them in a path class nor adds them to `invoke`: dropped silently.

Not a regression of ca63f057 or c6c9a76d: those changed the `split("/")` handling of empty segments and sibling naming; the whole-segment test predates them. c6c9a76d's `uniqueNames` does make more of these documents reach the compiler (it no longer merges `{id}.json` with a sibling).

## Correct final API

A segment is a sequence of literal text and parameters. Every template expression is a navigation argument, the literal text is not part of any name the user types beyond what is needed to tell siblings apart:

```kotlin
// /media/{id}.{extension}
api.media.id("42", extension = "mp3").get()          // GET media/42.mp3
// /hackathons/{id}.json   (literal suffix)
api.hackathons.idJson("42").get()                     // GET hackathons/42.json
// /lists.{format}         (literal prefix)
api.lists("json").get()                               // GET lists.json   (class ListsFormatPath)
// /v1/{name}:cancel       (Google custom verb)
api.v1.nameCancel("operations/7").post()              // POST v1/operations%2F7:cancel
```

Wire: each parameter value is expanded by its own `style`/`explode` and percent-encoded on its own (`encodeURLParameter`, as whole-segment parameters are since 4f5c259c); literal parts are written as they are. `{id}` and `{id}.json` under one parent are two nodes (`id(…)` and `idJson(…)`). A path key with `#` or an expression that is not a declared-name (`{.format}`, `{insight_id:}`) is reported through `GenerationDiagnostics` instead of generated.

## Fix slice

- typed: add `PathSegment.Composite(parts: List<Part>)` with `Part.Text(value)` / `Part.Param(name, model)` (or generalise `Parameter` to carry prefix/suffix text), and tokenise each segment with the 3.1.1 ABNF in `parsePathSegments`. `expandFiniteEnumPathSegmentAt` (`Route.kt:393`) and `toPathSegment` must decide what a fixed enum or a union means inside a composite (suggest: no expansion, plain parameter).
- typed: `buildTree` groups routes by the segment as written; composite segments with the same text and parameter names share a node.
- renderer: `routeSegmentSimpleName`, `navigationMemberName`, `accumulatedParams` (`ImplRenderer.kt:56`), `addConcreteNavigationMember` (`ImplRenderer.kt:113`) and `pathLiteral` for the new variant; a navigation function with one argument per parameter.
- renderer: pass path parameter names through KotlinPoet `%N` at the constructor call sites, so a name that is not an identifier can never produce a syntax error again.
- Diagnostics: an unbalanced brace, an empty or invalid expression name, a `#`/`?` in a path key, and a declared path parameter that matches no template expression.
- Open decisions: naming of the navigation member (`idJson` vs `id`), whether literal-only decoration such as `.json` is dropped from the name when there is no sibling clash, and what to do with AWS `#fragment` keys (they are distinct operations on the same path distinguished by a required query parameter; candidates: strip the fragment and keep operations apart by operationId, or report).

## Proof

- Typed: extend `typed/src/commonTest/kotlin/io/github/nomisrev/PathSegmentSpec.kt` (`parsePathSegments` table) with `{id}.{ext}`, `{id}.json`, `lists.{format}`, `{name}:cancel`, `{a}{b}`; today they yield `Parameter("id}.{ext")` / `Literal`.
- Golden `ClientSpec` case (`renderer/src/jvmTest/kotlin/io/github/nomisrev/render/ClientSpec.kt`) per shape.
- MockEngine runtime test asserting the request path: `media/42.mp3`, `hackathons/42.json`, `v1/operations%2F7:cancel`; today the silent shapes send `%7Bid%7D.json`/`{id}.json`.
- integration-tests: `github.json`, `youtrack.json` and `openai.yaml` contain no composite segment (checked every path key), so they catch nothing here.

## Affected documents

Syntax errors (16): callfire.com [42c37cdf1aa77725485b], gov.bc.ca:geocoder [492eb553b60242870f61], tomtom.com:maps [5956befdf26f27ee0ac8], parliament.uk:erskine-may [6168c0eb48e3c3cc79ce], nytimes.com:top_stories [626e9a026526a640cc46], nrel.gov:transportation-incentives-laws [6693faf1dd6edc3bfd0d], healthcare.gov [6ddd468eeb6ea1e22312], gov.bc.ca:geomark [913390254cc038bb6fc4], gov.bc.ca:bcgnws [97b5bfb6238033ef08db], clever-cloud.com [9f3fa3d55fd0df3d1e6a], gitea.io [c4634c91257a46d94614], hhs.gov [d0e8208f003b63efdd35], ptv.vic.gov.au [d377fe9c20580247b5ac], lufthansa.com:public [d8d1b22560b54b0aea65], digitalnz.org [edaa9b83d31f68929524], nasa.gov:asteroids neows [f2b3531b11ea82b3b2a7]. Five of these also carry `conflicting declarations` / illegal-character errors (tomtom, nytimes top_stories, clever-cloud, ptv, nasa) and geocoder also has the server-name bug.

Compile cleanly, wrong path on the wire (9): parliament.uk:bills [0e02acbb1339de3c6922], hackathonwatch.com [2183fce1b47012e53a07], prss.org [25554ffb64fd91c61d75], gov.bc.ca:router [2da1f9cf658928b79f75], nytimes.com:books_api [4b6e979b52eba4e66d79], highwaysengland.co.uk [65c112b616eacc6464f3], apis.guru [827271e7ab68acd68d72], shop-pro.jp [99e4209ce72e6734115f], worldtimeapi.org [d0e4659ae45daa29e2b7].

`name contains illegal characters: :` from `{name}:verb` literals (114, all googleapis.com, e.g. iamcredentials [0234d67be5e10080b0a3], tagmanager [01585979764a10bc658e], servicemanagement [05e6304d3f87768e910a]); 70 more emitted documents (62 AWS `#fragment`, adobe.com:aem, bitbucket.org, nytimes *.json) fail for other reasons and also send literal braces. Full per-document list: `<scratchpad>/cc/paths.json`, `<scratchpad>/cc/pathlists.json` (scripts `paths.py` next to them). Total: 303 with the construct, 209 emitted, 202 wrong on the wire.

Not this bug: rapidapi.com:idealspot-geodata [cf1304b961a7e2795d0d] has `/data/insights/{insight_id:}`, a whole-segment parameter whose name contains `:` (illegal-character cluster).
