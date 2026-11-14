# A `$` in a literal path segment is written as a Kotlin string template

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile, and wrong output when the name after `$` happens to resolve
**Impact:** 3 of 1,547 documents compile-fail on it (amazonaws.com:greengrass, windows.net:graphrbac, ideal-postcodes.co.uk); 5 more with such paths fail earlier (microsoft.com:graph with 1,574 `$count`/`$ref` paths, googleapis.com:healthcare, amazonaws.com:lex-models, amazonaws.com:iot-jobs-data, loket.nl)
**Source spec valid:** yes — `$` is a sub-delimiter allowed in a URI path segment (RFC 3986 §3.3); OData and FHIR use `$count`, `$ref`, `$everything`

## Symptom

```
api/Greengrass.kt:939:106: error: outer class 'class Deployments : Any' of non-inner class cannot be used as receiver.
      val response = client.post("greengrass/groups/${groupId.encodeURLParameter()}/deployments/$reset") {
```

`$reset` is read as a reference to the sibling navigation property `reset` of the enclosing path class. When nothing
named `reset` is in scope the error is `unresolved reference`.

## Minimal reproducer

`<scratchpad>/repro/dollar-in-path-literal.json`:

```json
{
  "openapi": "3.0.3",
  "info": { "title": "dollar", "version": "1" },
  "paths": {
    "/groups/{groupId}/deployments/$reset": {
      "post": {
        "parameters": [
          { "name": "groupId", "in": "path", "required": true, "schema": { "type": "string" } }
        ],
        "responses": { "200": { "description": "ok" } }
      }
    },
    "/groups/{groupId}/members/$count": {
      "get": {
        "parameters": [
          { "name": "groupId", "in": "path", "required": true, "schema": { "type": "string" } },
          { "name": "count", "in": "query", "schema": { "type": "boolean" } }
        ],
        "responses": { "200": { "description": "ok" } }
      }
    }
  }
}
```

```
compile exit=1
api/Groups.kt:41:93: error: outer class 'class Deployments : Any' of non-inner class cannot be used as receiver.
```

Only the first path is reported. The second compiles and is wrong:

```kotlin
public suspend operator fun invoke(count: Boolean? = null) {
  val response = client.get("groups/${groupId.encodeURLParameter()}/members/$count") {
```

`api.groups.groupId("g").members.count.get(count = true)` requests `groups/g/members/true` instead of
`groups/g/members/$count`. With OData (`/users/$count` plus the `$count` query option, named `count` after
sanitising) this silent case is the likely one.

## What the spec says

OAS 3.0.3 §4.7.8 (Paths Object): the field name is a relative path "appended (no relative URL resolution) to the
expanded URL from the Server Object's url field". Only `{name}` is a template expression (§4.7.8.2 Path Templating);
every other character is literal.

## Root cause

`renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ParameterSerialization.kt:443`, in `Route.pathLiteral`:

```kotlin
is PathSegment.Literal -> CodeBlock.of("%L", segment.name.replace(' ', '·'))
```

The literal is placed unescaped between the quotes built at `:462` (`CodeBlock.of("\"%L\"", ...)`). The escaping helper
exists in the same file (`templateText(text)` at `:533`, which escapes `\`, `"` and `$`) and is applied to
`PathSegment.FixedValue` two lines above (`:441`) but not to `Literal`. A `"` or `\` in a path (loket.nl has
`.../dossier"`) breaks the string the same way.

Not a regression: before 4f5c259c the literal was also written raw (`is PathSegment.Literal -> segment.name` in the
removed `pathLiteral`).

## Correct final API

```kotlin
val response = client.post("groups/${groupId.encodeURLParameter()}/deployments/\$reset")
```

Wire: `POST groups/g/deployments/$reset`. The literal is sent byte for byte as the document writes it (no
percent-encoding of `$`; the path is the author's already-encoded text). Navigation names stay as today
(`deployments.reset`, `members.count`).

## Fix slice

Renderer, one line: `CodeBlock.of("%L", templateText(segment.name).replace(' ', '·'))` at
`ParameterSerialization.kt:443`. Check the two other places that print a literal segment into Kotlin source for the same
gap: `GenerationDiagnostics.kt:79` (`PathSegment.templateText()`, which looks like diagnostics text only; not traced).

## Proof

- Failing tests first: a `ClientSpec` golden case with `/groups/{groupId}/deployments/$reset` and
  `/groups/{groupId}/members/$count` (+ `count` query parameter), and a MockEngine runtime test asserting
  `request.url.encodedPath` ends with `/members/$count` — the silent case is only visible at runtime.
- integration-tests: no path in `github.json` or `youtrack.json` contains `$`, `"` or `\`; `openai.yaml` has none
  either. Not covered.

## Affected documents

Compile-fail (3):

- windows.net:graphrbac [a0c6d8ff2d054156a9e7] — `/{tenantID}/applications/{applicationObjectId}/$links/owners`; all 8 errors are this.
- amazonaws.com:greengrass [7491ff4d57202b0f7552] — `.../deployments/$reset`, `.../{BulkDeploymentId}/$stop`; 2 errors, plus 32 `conflicting declarations`.
- ideal-postcodes.co.uk [a27ed2379216e27c832b] — `/places/${place}` (a typo in the source for `{place}`; the literal should still be sent as written, with a warning that `place` has no template expression); also empty-string enum entries.

Blocked earlier, will hit this next (5): microsoft.com:graph [cb0f197c9832ed0801d0] (typed), googleapis.com:healthcare
[a0b29d3dc89d909ed647] (emit), amazonaws.com:lex-models [31fb5699da2ecd54daab] (typed), amazonaws.com:iot-jobs-data
[e0671c74acf3ce1b6e50] (typed), loket.nl [3c2fbbbed59d2e29a55f] (`"` in a path, typed).
