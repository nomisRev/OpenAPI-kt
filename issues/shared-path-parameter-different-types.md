# Operations that type one path variable differently abort generation

**Status:** fixed in `dfc4cf2d` with candidate 1 below (decided 2026-11-15: an operation must not be reachable with a
type it does not declare). `id(id: String): IdStringPath` and `id(id: Uuid): IdUuidPath` are overloads, the classes
are named with the type whatever the order of the document, each with a warning. A group with a type that has no
word of its own (inline enum, union), with two equal words, or with more than one list, is numbered instead. A
nullable path parameter is generated as not nullable, with a warning. Proof: `ApiTreeSpec`, golden
`client/shared-path-parameter-types`, `SharedPathParameterRuntimeTest`.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** typed
**Impact:** 26 of 1,547 documents (magento, hetzner.cloud, sendgrid, jellyfin, 1password connect, vimeo, files.com, discourse, appcenter.ms, ...)
**Source spec valid:** yes (sloppy but legal) — a Parameter Object belongs to one operation; two operations may type `{id}` differently

## Symptom

```
IllegalArgumentException: Conflicting shared path parameter node '/items/{id}':
GET /items/{id} -> Parameter(name=id, model=String); GET /items/{id}/parts -> Parameter(name=id, model=Long).
Shared path nodes must resolve to the same segment shape.
```

Unlike `issues/shared-path-parameter-constraint-false-conflict.md`, the Kotlin types really differ here. The whole
document fails.

Pairs seen in the census (first conflict per document): `Long`/`String` 9, `String`/`Uuid` 6, `Int`/`String` 6,
`Int`/`Long` 2, `Double`/`String` 1, `Int`/`List<Int>` 1, `String`/`String?` 1.

## Minimal reproducer

`<scratchpad>/repro/shared-path-parameter-types.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": {
    "/items/{id}": {
      "get": {
        "parameters": [{ "name": "id", "in": "path", "required": true, "schema": { "type": "string" } }],
        "responses": { "204": { "description": "ok" } }
      }
    },
    "/items/{id}/parts": {
      "get": {
        "parameters": [{ "name": "id", "in": "path", "required": true, "schema": { "type": "integer", "format": "int64" } }],
        "responses": { "204": { "description": "ok" } }
      }
    }
  }
}
```

Observed:

```
worker exit=1 phases: parse pass, typed fail
java.lang.IllegalArgumentException: Conflicting shared path parameter node '/items/{id}': GET /items/{id} -> Parameter(name=id, model=String); GET /items/{id}/parts -> Parameter(name=id, model=Long). Shared path nodes must resolve to the same segment shape.
	at io.github.nomisrev.openapi.ApiTreeKt.requireCompatibleWith(ApiTree.kt:232)
```

## What the spec says

OAS 3.0.3 §4.7.9/§4.7.10/§4.7.12: parameters are per operation. The "Path Templating" section only requires that each
template expression corresponds to a path parameter of that operation. Nothing ties `/items/{id}` to
`/items/{id}/parts`; the tree of shared prefixes is our construction, not the spec's.

For the `String`/`String?` pair (vtex Subscriptions): §4.7.12 (fixed fields, `required`) says a path parameter is always `required: true`, and
`simple` style has no representation of `null`, so `nullable: true` on a path parameter schema is meaningless.

## Root cause (traced)

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/ApiTree.kt:145-156` — `childNodes` groups placements by
  `spellingKey()`, which for a parameter is `"parameter:$name"` (`ApiTree.kt:193-198`): the type is not part of the
  key, so every operation below `/items/{id}` lands on one node.
- `ApiTree.kt:158-177` — `toNode` folds the cluster through `requireCompatibleWith`, which throws on the first
  mismatch (`ApiTree.kt:216-236`).

The generated node class takes the parameter once in its constructor (`IdPath(client, id: String)`), so the current
tree shape has room for only one type per node. That is the limitation; the rejection is how it is enforced.

## Correct final API

Each operation must take the type its own Parameter Object declares. One shape that keeps that:

```kotlin
public class Items internal constructor(private val client: HttpClient) {
  public fun id(id: String): IdPath = IdPath(client, id)        // operations that declare {id} as string
  public fun id(id: Long): IdLongPath = IdLongPath(client, id)  // operations that declare {id} as int64

  public class IdPath(...)     { public val `get`: Get }                 // GET items/{id}
  public class IdLongPath(...) { public val parts: Parts }               // GET items/{id}/parts
}
```

Wire: unchanged per operation, `items/${id.encodeURLParameter()}` resp. `items/${id}/parts`.

## Fix slice

Open design decision, three candidates:

1. **Split the node by parameter type** (above). typed: make the model's shape part of the clustering in
   `childNodes`, so each distinct shape is its own `PathNode`. renderer: sibling nodes with one member name become
   overloads; the class names need a suffix (`uniqueNames` in the renderer already suffixes colliding names and
   reports it through `renamedPathDiagnostics`, `GenerationDiagnostics.kt:67-78`). Overloads are only legal when the
   Kotlin parameter types differ, which holds for every pair above except `String`/`String?`.
2. **Reuse `PathSegment.OverloadedParameter`** (`PathSegment.kt`, added in 05ed7615 for shared `oneOf` parameters):
   one node whose parameter is a union of the declared types. Less accurate: `id("abc").parts.get()` compiles although
   that operation declares an integer.
3. **Keep one node, do not share the parameter**: each operation takes its own path arguments. Largest change.

Separately, whichever is chosen:

- `String` vs `String?`: drop `nullable` on path parameter schemas in typed (with a warning diagnostic) before
  comparing.
- `Int` vs `Long` (`format: int32` on one operation only): still two types on the wire contract; treat like any other
  pair, do not widen silently.
- If the decision is to keep rejecting, report it as a `GenerationDiagnostic` error that names both operations and
  JSON pointers instead of an exception from `toApiTree`, and consider failing only the affected subtree.

## Proof

- Failing test first in `typed/src/commonTest/kotlin/io/github/nomisrev/ApiTreeSpec.kt` (it currently asserts the
  rejection at line 136; that expectation changes).
- Golden `ClientSpec` case for the reproducer; MockEngine test that `id("a").get()` requests `items/a` and
  `id(1L).parts.get()` requests `items/1/parts`.
- Integration tests would not catch a regression: `github.json`, `youtrack.json` and `openai.yaml` have no path node
  whose operations type the variable differently (checked by script).

## Affected documents

26 total (pair = the two types in the first reported conflict). magento.com [34738c96ee9fb71d2a3d] Long/String,
hetzner.cloud [e04b10a9461aeccdaa6f] Long/String, sendgrid.com [ab515774052dd0aacda8] String/Long,
jellyfin.local [38ec17f7a8237fcfe9c2] String/Uuid, 1password.local:connect [8d29f9970d265c75c5b8] String/Uuid,
files.com [61a925651597f03ea9bc] Int/String, vimeo.com [315034d21e1f8b798d05] String/Double,
exavault.com [9815a18cb30fa855f0d5] Int/Long, daniweb.com [84ee5d4190c7b3a8d82a] List<Int>/Int,
vtex.local:Subscriptions-API-(v3) [9d195a7990624dee9dc2] String?/String.
