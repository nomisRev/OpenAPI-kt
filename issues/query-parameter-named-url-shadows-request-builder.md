# A parameter named `url` breaks every query parameter of its operation

**Status:** fixed in `273df770` (generated code reaches the URL through `this.url`). Census re-run 2026-11-14: no document fails on `encodedParameters` any more; appwrite.io, axesso.de and netatmo.net compile again.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 24 of 1,547 documents (webscraping.ai, googleapis.com pagespeedonline/blogger/indexing/factchecktools, nytimes.com timeswire/community, appwrite.io client/server, netatmo.net, axesso.de, flat.io, stackexchange.com, ...)
**Source spec valid:** yes — `url` is an ordinary parameter name (OAS 3.0.3 §4.7.12.1, Parameter Object `name`)

## Symptom

```
api/Addwebhook.kt:19:13: error: unresolved reference 'encodedParameters' on receiver of type 'String'.
```

The receiver type is the type of the operation's `url` parameter (`String`, `String?`, `Boolean`, `Boolean?` in the
census). Every query parameter of the operation fails, not only `url` itself, because each one is written through
`url.encodedParameters`.

Three of the four documents that compiled in the Oct 31 pilot and no longer do are this bug: appwrite.io:server
[047d861bfae43e496a4b], axesso.de [130777f0620a0184593b], netatmo.net [20878823151f83393f1f]. Old output
(`exploration/build/pilot-300/results/<key>/sources`) used `parameter("url", url)`; the new one uses
`url.encodedParameters.append("url", url.encodeURLParameter())`.

(The fourth, apptigent.com [04f32ea3ecb3622265a7], is not a generator regression: the generated enum entries that
contain `:` are identical in both runs. The pilot compiled with kotlin-compiler-embeddable 2.4.10, the census with
2.4.20; recompiling the unchanged Oct 31 `InputDateTimeConversion.kt` with the census compiler gives the same 168
`name contains illegal characters: :` errors. It belongs to the illegal-character-names cluster.)

## Minimal reproducer

`<scratchpad>/repro/query-parameter-named-url.json`:

```json
{
  "openapi": "3.0.3",
  "info": { "title": "url", "version": "1" },
  "paths": {
    "/addwebhook": {
      "get": {
        "parameters": [
          { "name": "url", "in": "query", "required": true, "schema": { "type": "string" } }
        ],
        "responses": { "200": { "description": "ok" } }
      }
    }
  }
}
```

Generated (`api/Addwebhook.kt`):

```kotlin
public suspend operator fun invoke(url: String) {
  val response = client.get("addwebhook") {
    url.encodedParameters.append("url", url.encodeURLParameter())
  }
  if (response.status.value != 200) throw ResponseException(response, "")
}
```

```
compile exit=1
api/Addwebhook.kt:19:13: error: unresolved reference 'encodedParameters' on receiver of type 'String'.
```

## What the spec says

OAS 3.0.3 §4.7.12.1: `name` is "the name of the parameter. Parameter names are case sensitive." Nothing reserves
`url`. The generator must keep the caller-facing name `url` (it is the documented name) and send `?url=...`.

## Root cause

`renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ParameterSerialization.kt:176`:

```kotlin
PairTarget.Query -> CodeBlock.of("url.encodedParameters.append(%L, %L)", name, value)
```

The statement is emitted inside the `client.get(...) { }` lambda, where `url` is meant to be
`HttpRequestBuilder.url`. A function parameter (or local) named `url` is lexically closer than the lambda's implicit
receiver, so `url` resolves to the parameter. Before f43c8ec9 query parameters were written with
`io.ktor.client.request.parameter(name, value)`, a top-level function, which a value parameter cannot shadow.

Verified: `git log -S'url.encodedParameters' -- renderer` returns only f43c8ec9; the 24 documents are exactly the ones
whose emitted `api/` sources declare an operation parameter `url` next to at least one query parameter
(`<scratchpad>/cc/shadow.py`). Five more census documents have a `url` parameter but no query parameter in that
operation (circuitsandbox.net, easypdfserver.com, mineskin.org, reverb.com, telegram.org) and compile.

The source of `url` is a query parameter in most documents; a flattened body field or a header named `url` in an
operation that also has query parameters fails the same way.

## Correct final API

Unchanged signature, the parameter stays `url`:

```kotlin
public suspend operator fun invoke(url: String) {
  val response = client.get("addwebhook") {
    this.url.encodedParameters.append("url", url.encodeURLParameter())
  }
  ...
}
```

Wire: `GET addwebhook?url=https%3A%2F%2Fexample.com%2Fa%20b` (RFC 6570 form expansion as f43c8ec9 introduced it).

## Fix slice

Renderer only. Options:

1. Qualify the builder: `this.url.encodedParameters.append(...)`. Query statements are emitted directly in the request
   lambda and inside `if (...) { }` / `forEach { }` / `when` blocks, none of which rebinds `this`; check the
   `addUnionCode` and map/object explode paths in `ParameterSerialization.kt` for a nested lambda with receiver
   (`apply`, `with`, `buildString`) before choosing this.
2. Emit an unshadowable helper, e.g. a generated `internal fun HttpRequestBuilder.encodedQuery(name: String, value: String)`
   in the serialization utils, called as a function (functions are not shadowed by value parameters).

The same class of bug exists for `client` (see `parameter-named-client-shadows-http-client.md`); a single pass over the
identifiers the renderer emits as bare names (`client`, `url`, `response`) would close both. `response` is safe today:
the parameter is only read inside the initializer of `val response`, before the local is in scope.

## Proof

- Failing test first: a `ClientSpec` golden case (`renderer/src/jvmTest/kotlin/io/github/nomisrev/render/ClientSpec.kt`,
  data under `renderer/src/jvmTest/kotlinTestData/client/`) with a required `url` query parameter plus a second query
  parameter, and a MockEngine runtime test asserting `request.url.encodedQuery == "url=https%3A%2F%2Fa.b%2Fc&x=1"`.
  Golden text alone would not have caught this; the generated file has to be compiled (the runtime tests do that).
- integration-tests: `github.json` and `youtrack.json` have no query parameter named `url` (scanned by camel-cased
  name); `openai.yaml` has no `in: query` parameter named `url` either. They do not catch this regression.

## Affected documents

24 total. 14 of them have no other compile error (every error line is this one): webscraping.ai [48d66909e879d2b3dbb8],
googleapis.com:pagespeedonline [dd53049854b2b24c04d0], googleapis.com:blogger [cb7d3b785fff7f94a712],
stackexchange.com [e2ba69a8c9bd05aada02], microsoft.com:cognitiveservices-Prediction [e965fc45ef3d84e1c6e7],
api2pdf.com [faf105092a4b5d9b3d29], neutrinoapi.net [2bef25b9e7e125f60188], appwrite.io:server [047d861bfae43e496a4b],
appwrite.io:client [fe9cb42d2117feb6d93d], axesso.de [130777f0620a0184593b], netatmo.net [20878823151f83393f1f],
apimatic.io [c9d71ae3768799d4b16b], interzoid.com:globalpageload [f1e7b5e40eb7c46ed9ff], nytimes.com:community [e0863e7dacc4c72d1852].

The other 10 also fail for another reason: flat.io [0c343c4e447589dc5b7e] (keyword path parameter `class`),
httpbin.org [860a832b571245275143] (TRACE), microsoft.com:cognitiveservices-Training [39cdf9cd72fdf1d39084]
(`Post.Body` qualifier), brainbi.net [7feff951ba2385533f01], googleapis.com:factchecktools [1c20b5437dc09f59d7f6],
googleapis.com:indexing [449aa66dc7d55f6dc8b7], api2cart.com [3f7eb592e4d4978df73c],
mermade.org.uk:openapi-converter [aedecc15966edc770e21], nytimes.com:timeswire [6c1a649e3e9fade651b4],
zeit.co [c88696f9879d15a8659a]. brainbi.net also has a query parameter with an empty name
(`keyword-path-parameter-unescaped-at-use-sites.md`); the last six carry `conflicting declarations` or
`name contains illegal characters` errors owned by other reports.
