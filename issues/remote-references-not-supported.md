# A `$ref` to another document throws `NotImplementedError`

**Status:** open, not hit by the census
**Confirmed:** by reproducer on 2026-11-14 (branch openapi-3.2-streaming); 0 census documents
**Stage:** typed
**Impact:** 0 of 1,547 documents. The corpus is API Guru, which publishes every specification as one bundled file, so it cannot measure this. Multi-file specifications are common in source repositories.
**Source spec valid:** yes — a `$ref` may be any URI reference, including a relative file or an absolute URL.

## Symptom

Any `$ref` that does not start with `#` ends the typed phase with a `kotlin.NotImplementedError` (an `Error`, not an `Exception`, so `catch (e: Exception)` in a caller does not see it):

```
kotlin.NotImplementedError: An operation is not implemented: Remote schemas ./models.json#/components/schemas/Pet not supported yet.
	at io.github.nomisrev.openapi.registry.Registry.remoteSchema(Registry.kt:30)
	at io.github.nomisrev.openapi.registry.Registry$ScopeImpl.peek(Registry.kt:90)

kotlin.NotImplementedError: An operation is not implemented: Remote responses reference https://example.com/common.json#/components/responses/Pets not supported yet.
	at io.github.nomisrev.openapi.registry.ComponentReferenceKt.resolveComponent(ComponentReference.kt:24)
	at io.github.nomisrev.openapi.routes.ReturnKt.resolve(Return.kt:69)
```

Census check: every `$ref` value of all 565 fetched OpenAPI 3 documents was classified (`<scratchpad>/c3/refs.py`, references inside `example`/`examples`/extensions excluded). None is non-local, and no failure message in `results.jsonl` contains "not supported yet".

## Minimal reproducer

Scratchpad `repro/remote-ref-schema.json`:

```json
{
  "openapi": "3.0.3",
  "info": { "title": "t", "version": "1" },
  "paths": {
    "/pets": {
      "get": {
        "operationId": "pets",
        "responses": {
          "200": { "description": "ok", "content": { "application/json": { "schema": { "$ref": "./models.json#/components/schemas/Pet" } } } }
        }
      }
    }
  }
}
```

```
worker exit=1 phases: [{"phase":"parse","status":"pass"},{"phase":"typed","status":"fail","exception":"kotlin.NotImplementedError","message":"An operation is not implemented: Remote schemas ./models.json#/components/schemas/Pet not supported yet."}]
no sources emitted
```

`repro/remote-ref-response.json` is the same with `"200": { "$ref": "https://example.com/common.json#/components/responses/Pets" }` and fails at `ComponentReference.kt:24`.

## What the spec says

- OAS 3.0.3 §4.7.23 Reference Object and §4.6 Relative References in URLs (https://spec.openapis.org/oas/v3.0.3.html#relative-references-in-urls): the reference string is a JSON Reference; relative references are resolved against the URL of the referring document. The spec's own examples include `"$ref": "Pet.json"` and `"$ref": "definitions.json#/Pet"`.
- OAS 3.1.0 §4.8.23: `$ref` is "in the form of a URI", resolved per RFC 3986 against the document's base URI.

## Root cause

Traced.

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/registry/Registry.kt:29-30` — `remoteSchema` is `TODO("Remote schemas $url not supported yet.")`, called from `peek` at line 90 for every schema reference not starting with `#`.
- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/registry/ComponentReference.kt:24` — the same `TODO` for responses, parameters, request bodies and path items.
- `Registry.kt:23` creates a Ktor `HttpClient()` per registry (closed in `close()`) that nothing uses; it is a leftover of the intended implementation.
- Structural gap: `toApiTree` receives a decoded `OpenAPI` value with no base URI, so a relative reference like `./models.json` cannot be resolved even in principle. The Gradle plugin knows the spec file's location, the typed layer does not.

## Correct final API

Same as for a local reference: the generated client is identical to the one generated from the bundled document. A schema referenced from `models.json#/components/schemas/Pet` generates one `Pet` type, shared by all references to it; a name clash between two documents' `Pet` must be an error or be disambiguated deterministically, never merged silently.

Until that exists, the failure should be a normal diagnostic: `External reference ./models.json#/components/schemas/Pet at #/paths/~1pets/get/responses/200/content/application~1json/schema is not supported; bundle the specification into one file first.`

## Fix slice

- Smallest step: replace both `TODO`s with an `IllegalArgumentException` / diagnostic carrying the reference and its location, and remove the unused `HttpClient` (and with it the Ktor client dependency of `typed`, if nothing else needs it).
- Full support, open design decisions:
  1. Where resolution happens. A loader in front of the parser that bundles all documents into one (rewriting external references to local pointers) keeps `typed/` single-document. It depends on `issues/ref-json-pointer-not-resolved.md`, because a bundler produces exactly those non-component pointers.
  2. Base URI: `specFile` in the Gradle plugin gives it for files; a spec loaded from a string needs an explicit one.
  3. Whether the build may fetch `http(s)` references at all (reproducibility, offline builds), or only files relative to the spec.
  4. Type naming for schemas that come from another document.

## Proof

1. typed: a test that a non-local schema reference and a non-local response reference fail with the diagnostic above and not with `NotImplementedError`. Fails today.
2. For full support: a two-file fixture (`api.json` + `models.json`) in `parser/src/commonTest/resources/specs/`, a golden `ClientSpec` case asserting the output equals that of the bundled single file, and a Gradle plugin functional test with a relative reference.

Integration-tests coverage: none; `github.json`, `youtrack.json` and `openai.yaml` are single files without external references.

## Affected documents

None in the census.
