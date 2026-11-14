# A path parameter with different constraints per operation is rejected as a "conflicting shared path node"

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** typed
**Impact:** 31 of 1,547 documents (Twilio x13, AWS x9, VTEX x3, stream-io, oxforddictionaries, shutterstock, threatjammer, lgtm, godaddy)
**Source spec valid:** yes — each operation declares its own Parameter Object; nothing requires the schemas of one template variable to be identical across operations

## Symptom

`toApiTree` throws, and the two models it prints are identical:

```
IllegalArgumentException: Conflicting shared path parameter node '/items/{id}':
GET /items/{id} -> Parameter(name=id, model=String); GET /items/{id}/parts -> Parameter(name=id, model=String).
Shared path nodes must resolve to the same segment shape.
```

The whole document fails; nothing is generated.

## Minimal reproducer

`<scratchpad>/repro/shared-path-parameter-constraints.json`

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
        "parameters": [{ "name": "id", "in": "path", "required": true,
          "schema": { "type": "string", "minLength": 34, "maxLength": 34, "pattern": "^HA[0-9a-fA-F]{32}$" } }],
        "responses": { "204": { "description": "ok" } }
      }
    }
  }
}
```

Observed:

```
worker exit=1 phases: parse pass, typed fail
java.lang.IllegalArgumentException: Conflicting shared path parameter node '/items/{id}': GET /items/{id} -> Parameter(name=id, model=String); GET /items/{id}/parts -> Parameter(name=id, model=String). Shared path nodes must resolve to the same segment shape.
	at io.github.nomisrev.openapi.ApiTreeKt.requireCompatibleWith(ApiTree.kt:232)
	at io.github.nomisrev.openapi.ApiTreeKt.toNode(ApiTree.kt:163)
```

`shared-path-parameter-default.json` (second schema `{type: string, default: "x"}`) fails the same way.
`shared-path-parameter-ok.json` (both `{type: string}`) generates and compiles.

## What the spec says

- OAS 3.0.3 §4.7.9 Path Item / §4.7.10 Operation: parameters are declared per operation (or per path item); there is
  no document-wide definition of a template variable.
- §4.7.12 Parameter Object: `schema` is an ordinary Schema Object. `minLength`, `maxLength`, `pattern`, `default` are
  validation/annotation keywords; they do not change how a `simple`-style string is serialized into the path.

Twilio declares `{type: string}` on the list operation and `{type: string, minLength: 34, maxLength: 34, pattern: ...}`
on the item operation of the same resource. Both are valid.

## Root cause (traced)

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/ApiTree.kt:216-236` — `requireCompatibleWith` compares
  `normalizedForCompatibility()` of both segments with data-class equality.
- `ApiTree.kt:399-420` and `ApiTree.kt:484-503` — for primitives, `clearedCompatibilityMetadata()` clears only
  `description` and `title`. `Model.Primitive.String` (`Model.kt:142-148`) also carries `default` and
  `constraint: Constraints.Text?`; `Int`/`Long`/`Float`/`Double` carry `default` and `constraint: Constraints.Number?`;
  `Model.Collection` (`ApiTree.kt:423-427`) keeps `default` and `constraint` as well. Those fields take part in the
  equality check.
- `ApiTree.kt:520-551` — `compatibilityDescription()` prints only the Kotlin type, so the message shows `String` vs
  `String` and hides the field that differs.

Verified by sub-grouping the 31 documents on the keyword that differs between the two parameter schemas named in the
message: `minLength`/`maxLength`/`pattern` 27 (one of them, shutterstock, also `format` on a string that still models
as `String`), `default` 3 (VTEX), and stream-io 1 (`maxLength`, parameter declared through `content`).

The renderer does not read constraints at all (`ide search --query constraint --path renderer/src --ignore-case`
finds test code only), so the rejected difference has no effect on generated code.

## Correct final API

Exactly what `shared-path-parameter-ok.json` generates today: one node, one parameter of the common Kotlin type.

```kotlin
public class Items internal constructor(private val client: HttpClient) {
  public fun id(id: String): IdPath = IdPath(client, id)
  public class IdPath internal constructor(private val client: HttpClient, private val id: String) {
    public val `get`: Get = Get(client, id)          // GET items/{id}
    public val parts: Parts = Parts(client, id)      // GET items/{id}/parts
  }
}
```

Wire: `GET items/<id.encodeURLParameter()>` and `GET items/<...>/parts`, unchanged.

## Fix slice

- typed, `ApiTree.kt`: compare path segments on what decides the generated segment — Kotlin type, nullability, enum
  values, union cases — and not on `default`/`constraint`. Clear them in `clearedCompatibilityMetadata()` and in
  `Model.Collection.normalizedForCompatibility()`, or compare a dedicated "segment shape" projection.
- Decide which declaration the shared node keeps (`preferredRepresentation`, `ApiTree.kt:208-212`, keeps the first).
  If constraints are ever rendered (KDoc or `require`), the node must not apply one operation's constraint to another:
  drop constraints that are not common to all operations of the node.
- Make `compatibilityDescription()` print every field the comparison uses, so a real conflict is diagnosable.
- Same normalisation is used by `validatedConcreteRoutes` (`ApiTree.kt:286-293`); check whether the "Conflicting
  concrete route" check has the same false positive for duplicate descriptors.
- After this fix, some of these 31 documents will surface the real type conflicts of
  `issues/shared-path-parameter-different-types.md` on another node (only the first conflict is reported).

## Proof

- Failing test first: `typed/src/commonTest/kotlin/io/github/nomisrev/ApiTreeSpec.kt` already asserts the conflict
  message (line 136); add a case with `{type: string}` vs `{type: string, pattern, minLength, maxLength}` and one with
  `default`, expecting one `PathNode` with both operations.
- Golden `ClientSpec` case with the reproducer above, asserting the output shown.
- Integration tests would not catch a regression: no path node in `github.json`, `youtrack.json` or `openai.yaml` has
  operations whose path parameter schemas differ (checked by script).

## Affected documents

31 total. twilio.com:twilio_supersim_v1 [064f1b175c64fdf2b65e], twilio.com:twilio_proxy_v1 [148a7869d690b82e8997],
amazonaws.com:finspace-data [1721356ec75464868c02], twilio.com:twilio_ip_messaging_v1 [2612e84cb2c9741e233c],
oxforddictionaries.com [26b4c5f38b65e255f2a9], amazonaws.com:lex-models [31fb5699da2ecd54daab],
stream-io-api.com [3b1203aba1c94ee8c165], threatjammer.com [3b6cf8ab04117a4e2ad3],
vtex.local:Giftcard-API [4fbea38853bac72e7520], shutterstock.com [78f9beaa8179a6b4b749].
