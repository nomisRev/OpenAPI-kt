# A `trace` operation generates a call to `client.trace`, which Ktor does not have

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 1 of 1,547 documents (httpbin.org, 5 TRACE operations)
**Source spec valid:** yes — `trace` is a fixed field of the Path Item Object (OAS 3.0.3 §4.7.9.1)

## Symptom

```
api/Anything.kt:10:31: error: unresolved reference 'trace'.
api/Anything.kt:80:29: error: unresolved reference 'trace' on receiver of type 'HttpClient'.
```

## Minimal reproducer

`<scratchpad>/repro/trace-operation.json`:

```json
{
  "openapi": "3.0.3",
  "info": { "title": "trace", "version": "1" },
  "paths": {
    "/anything": {
      "trace": { "responses": { "200": { "description": "ok" } } }
    }
  }
}
```

```
compile exit=1
api/Anything.kt:5:31: error: unresolved reference 'trace'.
api/Anything.kt:16:29: error: unresolved reference 'trace' on receiver of type 'HttpClient'.
```

Line 5 is `import io.ktor.client.request.trace`, line 16 `val response = client.trace("anything")`.

## What the spec says

OAS 3.0.3 §4.7.9.1 lists `get`, `put`, `post`, `delete`, `options`, `head`, `patch`, `trace` on a Path Item. OAS 3.2.0
adds `query` and `additionalOperations` (any method token); if the typed layer of this branch models those, they hit the
same code path (not verified: no census document uses them).

## Root cause

The request function is the lower-cased method name, assumed to exist in `io.ktor.client.request`:

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt:323` `httpMethodName = method.value.lowercase()`,
  used at `:351-364` as `client.%L(%L)`;
- `ClientRenderer.kt:39-41` and `:49-51` `addImport("io.ktor.client.request", method)`.

Verified with `javap` on the census classpath (ktor-client-core 3.5.2): `io.ktor.client.request.BuildersKt` has
`get`, `post`, `put`, `delete`, `patch`, `head`, `options`, `query` and `request` for `(HttpClient, String, ...)`;
there is no `trace`. `io.ktor.http.HttpMethod.Companion` does have `Trace`.

## Correct final API

Navigation unchanged (`api.anything.trace()`), request through the generic builder:

```kotlin
val response = client.request("anything") {
  method = HttpMethod.Trace
}
```

Wire: `TRACE anything`.

## Fix slice

Renderer: map the seven methods Ktor has a builder for to it, and every other method to
`client.request(path) { method = HttpMethod("<TOKEN>") }` (or the `HttpMethod.Trace` constant), importing
`io.ktor.client.request.request` and `io.ktor.http.HttpMethod` instead of the method name. `needsRequestConfig`
(`ImplRenderer.kt:325`) must be true for these so the lambda is emitted. OAS 3.2 `query` already has a Ktor builder
(`client.query`); `additionalOperations` methods need the generic form.

## Proof

- Failing test first: `ClientSpec` golden case `trace-operation` and a MockEngine runtime test asserting
  `request.method == HttpMethod.Trace`.
- integration-tests: `github.json`, `youtrack.json` and `openai.yaml` have no `trace` operation; not covered.

## Affected documents

1 total: httpbin.org [860a832b571245275143] (`/anything`, `/delay/{delay}`, `/redirect-to`, `/status/{codes}`, ...);
it also has the `url` query parameter shadowing (`query-parameter-named-url-shadows-request-builder.md`).
