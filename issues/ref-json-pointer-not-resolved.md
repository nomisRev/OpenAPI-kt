# `$ref` is resolved by component name, not as a JSON Pointer

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** typed
**Impact:** 26 of 1,547 documents fail on it (21 as `Schema <pointer> not found in [...]`, 5 as `Reference #/... does not point to a component in #/components/<section>/`); 1 more (enode.io) contains such references but fails in parse first. Every census document that has a `$ref` other than `#/components/<section>/<name>` fails.
**Source spec valid:** mixed — 25 valid (any JSON Pointer into the document is a legal reference target), 1 invalid (rumble.run points a schema `$ref` at a Response Object).

## Symptom

Two messages, one cause:

```
java.lang.IllegalStateException: Schema User/properties/id not found in [User].
	at io.github.nomisrev.openapi.registry.Registry$ScopeImpl.peek(Registry.kt:95)

java.lang.IllegalArgumentException: Reference #/paths/~1users/get/parameters/0 does not point to a component in #/components/parameters/.
	at io.github.nomisrev.openapi.registry.ComponentReferenceKt.resolveComponent(ComponentReference.kt:26)
```

The first message is misleading on top of being a failure: the "schema name" is the reference with its first 21 characters cut off, whatever they were (`d%7D/get/responses/200/content/application~1json/schema`, `s/StatusMessage`).

Pointer shapes found in the 27 documents (a document can use several):

| Target of the `$ref` | Documents | Examples |
|---|---|---|
| A1. inside a component schema: `#/components/schemas/X/properties/p`, `.../items`, `.../allOf/0`, `.../oneOf/0` | 12 | vtex.local:Profile-System, cpy.re:peertube, linode.com, nexmo.com:conversation, twinehealth.com |
| A2. inside a component schema under `definitions`: `#/components/schemas/X/definitions/y` | 7 | all codat.io documents |
| A3. a schema inside another component: `#/components/parameters/X/schema`, `#/components/responses/X/content/application~1json/schema` | 5 | codat.io:assess, nexmo.com:subaccounts |
| B1. a schema inside `paths`: `#/paths/~1a/get/responses/200/content/application~1json/schema[/properties/p]` | 11 | fulfillment.com, eos.local, exlibrisgroup.com:tasklists, digitalocean.com |
| B2. a Response Object inside `paths`: `#/paths/~1a/get/responses/401` | 6 | opensuse.org:obs, brex.io, rev.ai |
| B3. a Parameter Object inside `paths`: `#/paths/~1a/get/parameters/0`, `#/paths/~1a/parameters/0` | 7 | codat.io:sync-for-expenses, surevoip.co.uk |
| B4. headers, links, a whole path item inside `paths` | 2 | digitalocean.com, surevoip.co.uk |
| percent-encoded tokens (`%7B` for `{`, `%20`) in any of the above | 12 | conjur.local, rev.ai, opensuse.org:obs |
| X. schema `$ref` to `#/components/responses/Name` (wrong section) | 2 | rumble.run (fails on it), rev.ai |

No reference in the corpus is dangling: all of them resolve when read as RFC 6901 pointers with percent-decoding.

## Minimal reproducer

Four specs in the scratchpad `repro/` directory.

`ref-json-pointer-schema-property.json` (A1):

```json
{
  "openapi": "3.0.3",
  "info": { "title": "t", "version": "1" },
  "paths": {
    "/users/{id}": {
      "get": {
        "operationId": "getUser",
        "parameters": [
          { "name": "id", "in": "path", "required": true, "schema": { "$ref": "#/components/schemas/User/properties/id" } }
        ],
        "responses": {
          "200": { "description": "ok", "content": { "application/json": { "schema": { "$ref": "#/components/schemas/User" } } } }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "User": {
        "type": "object",
        "properties": { "id": { "type": "integer", "format": "int64" }, "name": { "type": "string" } }
      }
    }
  }
}
```

```
worker exit=1 phases: [{"phase":"parse","status":"pass"},{"phase":"typed","status":"fail","exception":"java.lang.IllegalStateException","message":"Schema User/properties/id not found in [User]."}]
	at io.github.nomisrev.openapi.registry.Registry$ScopeImpl.peek(Registry.kt:95)
	at io.github.nomisrev.openapi.registry.Registry$ScopeImpl.resolve(Registry.kt:114)
no sources emitted
```

`ref-json-pointer-schema-in-paths.json` (B1, with a percent-encoded `{id}`): `GET /me` declares
`"schema": { "$ref": "#/paths/~1users~1%7Bid%7D/get/responses/200/content/application~1json/schema" }`.

```
"message":"Schema d%7D/get/responses/200/content/application~1json/schema not found in []."
	at io.github.nomisrev.openapi.registry.Registry$ScopeImpl.peek(Registry.kt:95)
```

`ref-json-pointer-response-in-paths.json` (B2 + B3): `GET /groups` declares
`"parameters": [ { "$ref": "#/paths/~1users/get/parameters/0" } ]` and `"401": { "$ref": "#/paths/~1users/get/responses/401" }`.

```
"message":"Reference #/paths/~1users/get/parameters/0 does not point to a component in #/components/parameters/."
	at io.github.nomisrev.openapi.registry.ComponentReferenceKt.resolveComponent(ComponentReference.kt:26)
	at io.github.nomisrev.openapi.routes.SchemaContextKt.resolve(SchemaContext.kt:124)
	at io.github.nomisrev.openapi.routes.SchemaContextKt.withMissingPathParameters(SchemaContext.kt:108)
```

`ref-schema-to-response-component.json` (X, the rumble.run shape): a schema `$ref` of `#/components/responses/StatusMessage`, where both `components.responses.StatusMessage` and `components.schemas.StatusMessage` exist.

```
"message":"Schema s/StatusMessage not found in [StatusMessage]."
```

## What the spec says

- OAS 3.0.3 §4.7.23 Reference Object (https://spec.openapis.org/oas/v3.0.3.html#reference-object): "The Reference Object is defined by JSON Reference and follows the same structure, behavior and rules." A JSON Reference's fragment is a JSON Pointer (RFC 6901) into the document; nothing restricts it to `#/components/...`. The Components Object only says its entries are reusable, not that they are the only targets. OAS 3.1.0 §4.8.23 says the same with `$ref` as a URI.
- RFC 6901 §6 (https://www.rfc-editor.org/rfc/rfc6901#section-6): in the URI fragment representation a pointer is percent-encoded, so `%7B` must be decoded to `{` before `~1` → `/` and `~0` → `~` are applied. `#/paths/~1users~1%7Bid%7D/get` addresses the path `/users/{id}`.
- OAS 3.1.0 §4.8.24 Schema Object: schemas are JSON Schema 2020-12, where `$ref` may point to any subschema. `definitions` is not a 2020-12 keyword (`$defs` is), but an unknown keyword's value is still addressable by pointer, so the codat.io references are resolvable. They are all in 3.1.0 documents.
- X (rumble.run): the `$ref` sits where a Schema Object is expected and its target is a Response Object (`{"description": "Summary of operation results"}`). A reference must resolve to the type expected at its location, so this is an authoring error; `components.schemas.StatusMessage` exists and is clearly what was meant. I did not find a sentence in 3.0.3 that states the type rule in so many words; treat "invalid" as the reading of §4.7.23 plus the Components Object's typed maps, not as a quoted rule.

Where the non-component pointers come from is a guess: the `#/paths/...` targets look like the output of a bundler that inlines the first occurrence of an external or repeated object and points later occurrences at it. Hand-written specs produce A1 (linode.com, peertube).

## Root cause

Traced. There is no pointer resolution anywhere in `typed/`; every reference is turned into a map key by string surgery.

Schemas:

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/registry/Registry.kt:247` — `fun String.schemaName() = drop("#/components/schemas/".length)`. It drops 21 characters without checking the prefix.
- `Registry.kt:90-95` — `peek` looks that string up in `openAPI.components.schemas` and fails with `Schema $name not found`.
- `Registry.kt:122` — `NamingContext.Reference(ref.schemaName(), ...)`: the same string becomes the generated type's name, so naming also assumes one path segment.
- The same assumption is repeated at `Registry.kt:222`, `routes/RequestBody.kt:84`, `transformers/Union.kt:153,376,388,513,614-617`, `transformers/AllOf.kt:94-96`, `transformers/DiscriminatedObject.kt:48-50,65,88,150,204,409`, `registry/Predicates.kt:184`.

Other objects:

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/registry/ComponentReference.kt:25-26` — `resolveComponent` requires the prefix `#/components/$section/` and then looks the remainder up in that section's map. Callers: `routes/Return.kt:69` (responses), `routes/SchemaContext.kt:124` (parameters), `routes/RequestBody.kt:206` (request bodies), `routes/Route.kt:65` (path items).

What makes the fix more than a lookup:

- The typed layer only has the decoded `OpenAPI` model. The parser decodes with `ignoreUnknownKeys = true` (`parser/src/commonMain/kotlin/io/github/nomisrev/openapi/parser/OpenAPI.kt:83`) and `Schema` has no `$defs` / `definitions` field (searched `parser/src/commonMain`: no match), so the targets of A2 no longer exist after parsing.
- A pointer target has no component name, and the name currently is the identity of the generated type.

## Correct final API

A reference is transparent: the generated code is the same as if the target had been a named component, and the same Kotlin type is used at the target's own location and at every reference to it.

For reproducer A1, `#/components/schemas/User/properties/id` is `User.id`:

```kotlin
// GET /users/{id}
public suspend operator fun invoke(id: Long): User
```

and for an object-valued target (linode's `#/components/schemas/EntityTransfer/properties/entities`) the type is the one already generated for that property of `EntityTransfer` (its nested class), not a second copy named after the referencing site.

For B1, `GET /me` returns the same type as `GET /users/{id}` (the inline response class generated for the target operation). For B2/B3, `GET /groups` gets the `limit` query parameter and the same `401` case type as `GET /users`. Wire behaviour is identical to the inlined document.

For X, no guessing (`components.schemas.StatusMessage` must not be substituted silently). Fail with a diagnostic that names both ends: `Schema reference #/components/responses/StatusMessage at #/paths/~1tags/post/responses/200/content/application~1json/schema points to a Response Object, not a schema.`

The same applies to a pointer that does not resolve: a diagnostic with the reference and the location of the `$ref`, never a truncated name next to a dump of every component key.

## Fix slice

- parser: give the typed layer something a pointer can be evaluated against. Two options:
  1. keep the raw `JsonObject` of the document on `OpenAPI` (or hand it to `Registry`), evaluate RFC 6901 pointers (with percent-decoding) on it and decode the target with the serializer of the expected type (`Schema`, `Response`, `Parameter`, `RequestBody`, `PathItem`, `Header`). Covers A2 and every other keyword the model does not keep.
  2. walk the typed model along the pointer. No second representation, but needs a hand-written step per keyword and cannot reach `definitions` / unknown keywords without also modelling `$defs`.
  Option 1 is the smaller and more complete change; it is an open decision.
- typed, `ComponentReference.kt`: `resolveComponent` becomes "resolve pointer, expect type A", keeping the alias-chain and cycle handling. `#/components/<section>/<name>` stays the fast path.
- typed, `Registry.kt`: `peek(ref)` resolves any local pointer. Naming is the open design decision: derive the `NamingContext` from the target location (`#/components/schemas/User/properties/id` → `Reference(User)` nested with the property; `#/paths/.../responses/200/content/.../schema` → the path/method response context used at `Return.kt:45`) so that target and references share one generated type. The `schemaName()` call sites listed above need a "is this a plain component reference" check instead of a blind `drop`.
- typed: recursion. `Registry.kt:124` detects recursive schemas by `NamingContext`; a pointer into a schema that is currently being expanded (nexmo `#/components/schemas/channel/properties/from/oneOf/0`) must be recognised the same way.
- Diagnostics for X and for unresolvable pointers.
- enode.io [6b310704012b29b07d4b] will only show up here once its parse failure (another cluster) is fixed.

## Proof

Failing tests to write first:

1. typed: one test per reproducer (A1, B1 with `%7B`, B2+B3) asserting that the route resolves and that the model at the reference equals the model at the target. Today they throw at `Registry.kt:95` / `ComponentReference.kt:26`.
2. typed: an A2 test (`"$ref": "#/components/schemas/X/definitions/y"` in a 3.1.0 document) to pin the parser decision.
3. typed: X asserts the diagnostic text.
4. renderer: a golden `ClientSpec` case (`client/ref-json-pointer`) showing that no duplicate type is generated for an object-valued target, and that it compiles.
5. Corpus: the 26 keys below should leave these two families.

Integration-tests coverage: none. `github.json` (9,514 references) and `youtrack.json` (743) contain only `#/components/<section>/<name>` references; `openai.yaml` likewise (its multi-line `$ref: >-` entries are plain component references). They would not catch a regression.

## Investigation 2026-11-16 (at `5cee6c05`, not fixed)

Re-verified: all reproducers still fail at typed; 27 documents now (enode.io reaches typed since the parse fixes).
More sites than listed above assume `#/components/<section>/<name>`: `schemaName()` callers in Registry.kt, AllOf.kt,
Union.kt, Predicates.kt, DiscriminatedObject.kt; literal `#/components/schemas/` prefixes in Union.kt,
DiscriminatedObject.kt, Webhooks.kt; `substringAfterLast('/')` in Webhooks.kt and StreamResolution.kt. No component
name is percent-decoded or `~`-unescaped. A schema's `$ref` name is also its identity (`Reference.toModel()` rebuilds
from `ReferenceOr.schema(name)`), so pointers cannot be resolved in `peek` alone.

Prototyped (in a throwaway worktree, saved as `scratchpad/ref-pointer/PointerHoisting.kt` and `prototype.diff`):
hoist every local pointer that is not `#/components/<section>/<name>` into a component in the parser, before decoding
(the target and every `$ref` to it become one component, as a bundler would), RFC 6901 decoding, section from the
`$ref`'s location, data subtrees (`example`, `default`, `enum`, `const`, `x-*`) skipped except under `properties` and
`responses`. 12 of 17 checked documents reach emit; conjur, peertube and dnd5eapi stop at other errors; surevoip needs
a `pathItems` kind; rumble.run is a schema `$ref` to a Response (should be an error naming both ends). The disk filled
during the run, so the other 10 documents and every compile step were not checked.

Decisions needed before implementing:
1. Hoist every target, or only those that generate a type (objects, enums, unions), inlining primitives (avoids
   `UserId`-style value classes). Investigator: the latter.
2. Hoist in the parser (covers `definitions`, YAML; `toJson()` no longer equals the input) or keep the raw JSON on
   `OpenAPI` and resolve in typed. Investigator: the parser.
3. Share one type between the pointer's target and its uses (renames today's nested classes, only in documents that
   fail today) or generate the target twice. Investigator: share.
4. Names: PascalCased pointer tokens with a deterministic numeric suffix and a diagnostic per rename
   (`TransferEntities`, `UsersIdGetResponses200…` for path targets), or operationId-based names for path targets.
5. A schema `$ref` to a Response/Parameter (rumble.run): fail with a message naming both ends, or accept with a
   warning when a same-named schema exists.

## Affected documents

Fail in typed (26):

- `Schema ... not found` (21): vtex.local:Profile-System [02204817aafb791f57d4], conjur.local [121777971409f0c67574], fulfillment.com [13c7bc831088130e0e25], rumble.run [233407eceea9cf4868b3] (X, invalid), cpy.re:peertube [2f4c975ba6843b78308f], linode.com [329eee6b9a8b6aca780c], codat.io:assess [67ef826e05de75ea7c48], nexmo.com:application [69c5cada7486c86eb181], codat.io:bank-feeds [6dc860d09b652f00052a], nexmo.com:conversation [6e56f719e51b22864c82], eos.local [729640d5239b477d7c2e], nexmo.com:subaccounts [88555501d92c2529273b], codat.io:commerce [8b250766ea0dafe5910f], dnd5eapi.co [a2e0c95949a21ce9db43], nexmo.com:application.v2 [ab5d5c773c2c9da8fe21], surevoip.co.uk [b65f0d02f305613a4e37], codat.io:accounting [b9bd8fafae2703b4c19c], codat.io:banking [bbad480f593ce1fc2ba5], exlibrisgroup.com:tasklists [c1f147d2631964b75216], twinehealth.com [df937794d077822c747f], digitalocean.com [f98034cdf05682c61474]
- `Reference ... does not point to a component` (5): opensuse.org:obs [2b172818deef551c832c], brex.io [3c0f694afc91bc4ac5f6], rev.ai [a2633525beb721d497b9] (responses); codat.io:sync-for-expenses [de45456e96ccc209edac], codat.io:sync-for-commerce [f2241f987ecc68b32e34] (parameters)

Masked by a parse failure (1): enode.io [6b310704012b29b07d4b].

Most of these documents use several pointer shapes, so fixing only one of the two code paths moves a document from one message to the other. Regenerate the classification with `python3 <scratchpad>/c3/refs.py` (writes `<scratchpad>/c3/refs.json`).
