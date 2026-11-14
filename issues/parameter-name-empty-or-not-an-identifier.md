# Parameters with an empty name or a name containing `:` produce unusable argument names

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 2 of 1,547 documents (brainbi.net, rapidapi.com:idealspot-geodata)
**Source spec valid:** no for brainbi.net (a query parameter with `"name": ""` is meaningless); questionable for idealspot (`{insight_id:}` — `:` is not excluded by the OAS 3.0 text but is clearly a typo)

Related: `keyword-path-parameter-unescaped-at-use-sites.md` covers the unescaped *use* of `_` (brainbi.net) and of
keyword names. This report is about what the name should be in the first place: the missing diagnostic for an empty
name and the unsanitised `:`.

## Symptom

- brainbi.net: nine operations declare `{ "in": "query", "name": "", "schema": { "type": "string" } }`. The argument
  is named `_`, declared as `` `_` `` and used bare:
  `names _, __, ___, ... can only be used in back-ticks`. The request would add a query pair with an empty name.
- rapidapi.com:idealspot-geodata: path `/data/insights/{insight_id:}` with parameter `insight_id:`.
  `` public fun `insightId:`(`insightId:`: String): `InsightId:Path` = `InsightId:Path`(client, insightId:) `` ->
  `name contains illegal characters: :` plus syntax errors from the unescaped use.

## Minimal reproducer

`<scratchpad>/repro/parameter-empty-name.json`

```json
{ "openapi": "3.0.3", "info": { "title": "Repro", "version": "1" },
  "paths": { "/orders": { "get": { "operationId": "g",
    "parameters": [{ "name": "", "in": "query", "schema": { "type": "string" } }],
    "responses": { "204": { "description": "ok" } } } } } }
```
```
diagnostics: []
compile exit=1
api/Orders.kt:19:13: error: names _, __, ___, ... can only be used in back-ticks (`_`, `__`, `___`, ...).
api/Orders.kt:20:44: error: names _, __, ___, ... can only be used in back-ticks (`_`, `__`, `___`, ...).
```
```kotlin
public suspend operator fun invoke(`_`: String? = null) {
  val response = client.get("orders") {
    if (_ != null) {
      url.encodedParameters.append("", _.encodeURLParameter())
```

The `{insight_id:}` case was reproduced with a one-path spec (`<scratchpad>/t/colonparam.json`):
`api/Data.kt:17:16: error: name contains illegal characters: :`.

## What the spec says

OAS 3.0.3 Parameter Object: `name` — "REQUIRED. The name of the parameter. Parameter names are case sensitive."
(https://spec.openapis.org/oas/v3.0.3.html#parameter-object). An empty string satisfies "string" but names nothing;
for `in: path` the name must match a template expression, and OAS 3.2.0's ABNF
(`template-expression-param-name = 1*( %x00-7A / %x7C / %x7E-10FFFF )`, everything except `{` and `}`) does allow
`insight_id:`. So the second document is formally valid under 3.2 rules, the first is a broken export
(Postman-to-OpenAPI conversion of an empty query row).

## Root cause (traced)

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/NamingUtils.kt:39-50` `toParamName()`: an empty or
  all-symbol name becomes `_` (`.ifEmpty { "_" }`), which `isValidParamName()` accepts although `_` is reserved in
  Kotlin. The declaration is escaped by KotlinPoet, the uses in the body are emitted with `%L`
  (`renderer/.../ParameterSerialization.kt:162-163`, `expr`).
- Path parameters do not go through `toParamName()` at all but through `toCamelCase()`
  (`renderer/.../PathParamUtils.kt:24`, `ImplRenderer.kt:139`), which keeps `:`; the class name
  `InsightId:Path` comes from `PathParamUtils.kt:8`. The unescaped use is the `constructorArgs` defect described in
  `keyword-path-parameter-unescaped-at-use-sites.md`.
- No diagnostic exists for parameters whose name is empty (`renderer/.../GenerationDiagnostics.kt`).

## Correct final API

- Empty parameter name: a **warning diagnostic** and the parameter is not generated
  ("GET /orders: query parameter with an empty name ignored"), or an error for a required one. Sending `?=value`
  is not something a typed client should offer by accident.
- `insight_id:`: argument `insightId`, class `InsightIdPath`, URL `data/insights/${insightId.encodeURLParameter()}`.
  The identifier is sanitised; the template match stays on the raw name.

## Fix slice

- renderer: `toParamName()` must never return `_`/`__`…; fall back to `param`/`value` or (preferred) leave empty
  names to a diagnostic in the typed layer (`typed/.../routes/SchemaContext.kt:66`, `toRouteInput`).
- renderer: path parameter names through the same sanitiser as other parameters (`PathParamUtils.kt:8, 24`,
  `ImplRenderer.kt:139, 188, 1160`), references emitted with `%N`.
- Open decision: drop-with-warning vs. hard error for an empty name. Low priority: two documents, one of them invalid.

## Proof

- Failing golden `ClientSpec` cases for both specs; diagnostics test expecting the warning for the empty name.
- integration-tests: no empty or `:`-containing parameter names in `github.json`, `youtrack.json`; not seen in
  `openai.yaml`. No coverage needed beyond the goldens.

## Affected documents

brainbi.net [7feff951ba2385533f01] (also hits `query-parameter-named-url-shadows-request-builder.md`),
rapidapi.com:idealspot-geodata [cf1304b961a7e2795d0d].
