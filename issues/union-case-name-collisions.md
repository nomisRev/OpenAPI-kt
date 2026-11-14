# Union cases of one `oneOf`/`anyOf` get the same class name

**Status:** fixed on openapi-3.2-streaming: branches Kotlin cannot tell apart are one case, identical objects one case, different shapes with one name are named by enum value or position with a warning (`separateCases` in `Union.kt`); `required`-only `oneOf`/`anyOf` branches are a constraint on the parent object with a warning (`hasOnlyConstraintBranches`, `Object.kt`); the renderer numbers any remaining collision from 2 with a warning (`caseSimpleNames`). Covered by `UnionCaseNameCollisionsSpec` (typed), `UnionSpec` golden `union/name-collisions`, `UnionCaseNameCollisionsRoundTripTest` and `UnionCaseNameCollisionsWarningsSpec`. Not yet re-run against the census corpus.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 17 of 1,547 documents fail to compile (github.com GHES 2.18–3.7 and github.ae: 14, webflow.com, journy.io, nexmo.com:messages-olympus).
**Source spec valid:** yes for the construct. Two of the corpus shapes are questionable schemas (see below), but all are legal OpenAPI.

## Symptom

```
api/Gists.kt:437:28: error: conflicting declarations:
class CaseJsonElement : Gists.GistIdPath.Patch.Files
class CaseJsonElement : Gists.GistIdPath.Patch.Files
```

Three shapes, one cause:

| Shape | Example | Duplicate |
| --- | --- | --- |
| Two primitive branches with one Kotlin type | webflow `Log.transitionId`: two `string` branches with different `pattern`; journy.io `metadata`: `string` and `string` with `format: datetime` | `CaseString` ×2 |
| Branches that carry only constraints | GHES `PATCH /gists/{gist_id}` `files`: `anyOf: [{required:[content]}, {required:[filename]}, {type: object, maxProperties: 0}]` | `CaseJsonElement` ×3 |
| Inline object branches with the same property names | nexmo messages-olympus 202 response: five `oneOf` objects each with only `message_uuid`; webflow `PatchTransistionExecutionId`: two objects with only `status` | `MessageUuid` ×5, `Status` ×2 |

## Minimal reproducer

`<scratchpad>/repro/union-case-name-collision.json` (primitive and object shapes):

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Unions", "version": "1" },
  "paths": {
    "/log": { "get": { "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Log" } } } } } } }
  },
  "components": { "schemas": {
    "Log": {
      "type": "object",
      "properties": {
        "transitionId": { "anyOf": [
          { "type": "string", "pattern": "^las:transition:[a-f0-9]{32}$" },
          { "type": "string", "pattern": "^las:transition:commons-[0-9A-Za-z-]+$" }
        ] },
        "result": { "oneOf": [
          { "type": "object", "properties": { "status": { "type": "string", "enum": ["succeeded"] } } },
          { "type": "object", "properties": { "status": { "type": "string", "enum": ["failed", "rejected"] } } }
        ] }
      }
    }
  } }
}
```

Observed: all phases pass, no diagnostics, `compile exit=1`.

```kotlin
public sealed interface Result {
  public data class Status(public val status: Status? = null) : Result { public enum class Status { Succeeded } }
  public data class Status(public val status: Status? = null) : Result { public enum class Status { Failed, Rejected } }
}
public sealed interface TransitionId {
  public value class CaseString(public val `value`: String) : TransitionId
  public value class CaseString(public val `value`: String) : TransitionId
}
```

```
model/Log.kt:34:23: error: conflicting declarations:
model/Log.kt:50:23: error: conflicting declarations:
model/Log.kt:100:24: error: conflicting declarations:
model/Log.kt:106:24: error: conflicting declarations:
```

Two more reduced specs, same outcome: `<scratchpad>/repro/union-case-name-collision-constraint-branches.json` (the gists shape, `CaseJsonElement` ×3 at `api/Gists.kt:51,57,63`) and `<scratchpad>/repro/union-case-name-collision-primitives.json` (the journy.io shape, `CaseString` ×2 at `api/Track.kt:55,67`). The generated serializer also lists the same class three times (`CaseJsonElement::class to { ... }`), so decoding has no way to choose even if the names were fixed by hand.

## What the spec says

OAS 3.0.3 §4.7.24 Schema Object takes `oneOf` and `anyOf` from JSON Schema: an array of schemas, with no requirement that the branches differ in type or have names.

- `anyOf` with two `string` branches is a plain "matches either pattern" constraint. Valid and meaningful.
- `oneOf` with `string` and `string`+`format` (journy.io) is valid; since `format` is an annotation, a value can match both branches, which makes the `oneOf` unsatisfiable for those values. A spec smell, not a reason to fail.
- `anyOf` whose branches have only `required` (GHES gists) is the JSON Schema idiom for "at least one of these properties". It is a constraint on the surrounding object, which declares `type: object` and `properties` itself. It does not describe alternatives of different shape.

## Root cause

Traced: case names are computed per branch and never compared with the sibling branches, in two layers.

- Typed layer, `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/Union.kt:444-453`: the case context is `title`, a discriminator value, or `schema.unionCaseName(index)` (`Union.kt:472-505`). For an object that is the property names joined with `And` (`objectUnionCaseName`, `Union.kt:534-546`): `MessageUuid`, `Status`. Identical property sets give identical names. The `index` is only used as a last resort.
- Renderer, `Model.Union.Case.caseSimpleName` in `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRendererNaming.kt:8-46`: for a primitive, date, UUID, byte array or free-form case the name is a constant per Kotlin type (`CaseString`, `CaseJsonElement`, …). The typed-layer name is only read for models that hold a context (`unionCaseValueOrNull`, line 59), so the name `CaseDatetime` that the typed layer computes for journy.io's second branch is discarded.
- `collidingSingleDiscriminatorTags` (`UnionRendererNaming.kt:48-53`) handles the one collision the renderer knows about, duplicate discriminator tags. There is no general pass.

Traced (fixed): where the typed layer decides that the gists `anyOf` of `required`-only branches is a union of three free-form JSON values, dropping the object's own `content` and `filename` properties. `github.json` in the test fixtures has `oneOf: [{required: [...]}, {required: [...]}]` on several request bodies and compiles, so that shape is handled somewhere when it sits directly on a body schema; the gists shape (three branches, one of them `{type: object, maxProperties: 0}`, under `additionalProperties`) is not. Answer: `compositeTakesPrecedence` (`CompositePrecedenceGuard.kt`) lets a composite win only when the schema has no `type` or a branch adds structure (a `$ref`, a `type`, properties, ...). With `type: object` and branches of only `required` nothing adds structure, so `ObjectPlugin` builds the object. The gists schema has a trailing `{type: object, maxProperties: 0}` branch, whose `type` counts as structure, and `files` values are `nullable` objects; so the composite won. Without a `type` on the parent the composite always won.

## Correct final API

1. Branches that Kotlin can't tell apart at runtime should not be separate cases. Two `string` branches differing by `pattern`, `format` or length are one case; the constraint stays in KDoc (or in validation if that exists for the type):

   ```kotlin
   public val transitionId: String? = null   // anyOf of two string patterns: a String
   ```

   A union that keeps other cases keeps one `CaseString`.

2. `anyOf`/`oneOf` branches with only `required` (and no type, properties or `$ref`) are a constraint on the parent object. The type is the parent object with its declared properties; the gists `files` value is

   ```kotlin
   public data class Files(val content: String? = null, val filename: String? = null)   // nullable as a whole
   ```

   A diagnostic (info or warning) should say that "at least one of" is not enforced by the type.

3. Inline object branches that are different shapes with the same property names stay separate cases with distinct names: by `title` when present, else by a distinguishing single-value enum property (`Succeeded` / `FailedOrRejected`, the `enumLikeValues` naming the code already uses for string enums), else by position (`Status1`, `Status2`) with a warning. Branches that are structurally identical (nexmo's five `message_uuid` objects, which differ only in `description` and `x-tab-id`) collapse into one case.

Wire behaviour: unchanged for encoding. Decoding must be deterministic, which rule 1 and the collapse in rule 3 provide; today the serializer tries the same type several times.

## Fix slice

- Typed (`Union.kt`): after building cases, merge cases with equal models, and make sibling case names unique (suffix by distinguishing enum values, then index). This is where `unionCaseName(index)` already has the index.
- Typed: treat `required`-only branches as constraints. Find first why the direct-on-body form already works (untraced, see above) and extend that rule to nested schemas and to a trailing `{type: object, maxProperties: 0}` branch.
- Renderer (`UnionRendererNaming.kt`): `caseSimpleName` must not return a constant for two cases of one union. With the typed merge in place, a `check` that the names of a union's cases are distinct turns any remaining collision into a diagnostic instead of uncompilable code.
- Open decision: merge same-typed primitive branches (proposed) or keep them as `CaseString`, `CaseString2`. Keeping them gives the user a choice the wire can't express and the decoder can't make.

## Proof

- Failing tests first: `UnionSpec` golden cases for each of the three reproducers.
- Round trip: a test in the style of `UnionTokenKindDispatchRoundTripTest` decoding `"las:transition:commons-x"` and `{"status":"failed"}` and encoding them back.
- Integration tests would not catch a regression. `github.json` (current api.github.com) no longer has the gists `anyOf`; the GHES documents in the corpus do. A scan of `github.json` and `openai.yaml` finds unions with several inline branches of one shape (13 and 8), and both compile today, so their cases are already told apart by `title`, enum values or discriminators; none has two plain primitive branches of one type.

## Affected documents

17 observed. github.com:ghes-3.5 [0862e2c0ef57a888873d]; github.com:github.ae [10031a5c39c3169ba896]; github.com:ghes-3.3 [10fc85d83872612df7cf]; github.com:ghes-3.7 [1917015bbb7fff9a41f2]; github.com:ghes-2.21 [20845585fea531a6de8d]; github.com:ghes-2.22 [474691433ae0c1272a1b]; journy.io [8f7d98bc61a23209b9da]; webflow.com [bc2a5f7f98f1dc65804b] (both the primitive and the object shape); nexmo.com:messages-olympus [9072255cb991c6d55be2]; github.com:ghes-3.1 [c13a53ffa0d964ed4101]. The other 7 are GHES 2.18, 2.19, 2.20, 3.0, 3.2, 3.4 and 3.6.
