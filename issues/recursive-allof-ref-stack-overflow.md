# A recursive schema reached through an inline `allOf` overflows the stack

**Status:** fixed in `4da55e34`. 20 documents, not 18 (also atlassian.com:jira and dataflowkit.com), all through `resolveAllOf` transforming the merged copy in a fresh registry scope. A restating `allOf` is resolved as its `$ref` in the current scope; a structural one is a reference to its own nested type where it leads back to itself; `additionalProperties` of two members combine like properties. 19 compile and jira reaches `diagnostics` (re-checked with `--filter`). Side effect, decided: non-recursive restating `allOf`s use the top-level type instead of a nested copy (github, openai clients). Not fixed: cyclic `allOf` inheritance still overflows (no corpus document).
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** typed
**Impact:** 18 of 1,547 documents (all `amazonaws.com`: dynamodb, glue, sagemaker, ssm, wafv2, connect, athena, lex v2, ...)
**Source spec valid:** yes — recursive schemas and `allOf: [{$ref}, {description}]` are both ordinary

## Symptom

`StackOverflowError` with an empty message during `toApiTree`. The trace is the plugin chain repeating
`AllOfKt.resolveAllOf(AllOf.kt:58)` -> `Registry.toModel(Registry.kt:41)` -> `ObjectKt.properties(Object.kt:75)`.

Commit 18d5a14c added cycle detection to `Registry.peek` for *alias* cycles (`A: {$ref: B}`, `B: {$ref: A}`). This is
a different cycle: a data-type recursion whose hop goes through an inline `allOf`.

## Minimal reproducer

`<scratchpad>/repro/recursive-allof-ref.json` — the shape AWS uses for every property (`$ref` plus its own description):

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/node": { "get": { "responses": { "200": { "description": "ok",
    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Node" } } } } } } } },
  "components": { "schemas": {
    "Node": {
      "type": "object",
      "properties": {
        "child": { "allOf": [{ "$ref": "#/components/schemas/Node" }, { "description": "The child node." }] }
      }
    }
  } }
}
```

Observed:

```
worker exit=1 phases: parse pass, typed fail (java.lang.StackOverflowError, message "")
```

Variants, all in `<scratchpad>/repro/`:

| Spec | Shape | Result |
| --- | --- | --- |
| `recursive-plain-ref.json` | `child: {$ref: Node}` | passes, compiles |
| `recursive-allof-single.json` | `child: {allOf: [{$ref: Node}]}` | StackOverflowError |
| `recursive-allof-ref.json` | `child: {allOf: [{$ref: Node}, {description}]}` | StackOverflowError |
| `recursive-allof-list.json` | `children: {allOf: [{$ref: NodeList}, {description}]}`, `NodeList: {type: array, items: {$ref: Node}}` | StackOverflowError |
| `allof-ref-object-nonrecursive.json` | `owner: {allOf: [{$ref: Owner}, {description}]}` | passes, but see "Related wrong output" |

DynamoDB's real cycle is `AttributeValue.M -> allOf[$ref MapAttributeValue] -> additionalProperties $ref AttributeValue`.

## What the spec says

- OAS 3.0.3 §4.7.24 Schema Object: `allOf` is JSON Schema `allOf`; a member that only carries `description` adds an
  annotation and no constraint, so `{allOf: [{$ref: X}, {description}]}` validates exactly what X validates.
- OAS 3.0.3 §4.7.23 Reference Object: siblings of `$ref` are ignored in 3.0, which is why generators wrap the `$ref`
  in `allOf` to attach a per-property description. (3.1 allows `$ref` siblings.)

## Root cause (traced)

Two defects in `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/AllOf.kt:43-61`:

1. **The `$ref` member is inlined without being recorded.** `AllOf.kt:50` does
   `allOf.map { it.peek() }.reduce { acc, or -> acc.merge(or) }`. `peek` returns the target schema; the reference is
   never added to `ScopeImpl.expanding` (`Registry.kt:124-132`), which is the only thing that turns a revisit into
   `ResolvedSchema.Recursive` -> `Model.Reference` (`SchemaTransformerEngine.kt:62-69`). The property becomes an
   inline copy of `Node`, whose `child` is again an inline copy, forever.
2. **The merged schema is transformed in a fresh scope.** `AllOf.kt:58` runs
   `ReferenceOr.value(merged).toModel(name, context)` inside `with(scope.registry())`. With a `Registry` receiver the
   member `Registry.toModel` (`Registry.kt:39-44`) wins over the scope-aware top-level function
   (`Registry.kt:264-268`), and it starts from `scope()` = `ScopeImpl(null, emptySet())` (`Registry.kt:34`). The
   `expanding` set and the `$recursiveAnchor` are lost. The trace confirms it: `Registry.toModel(Registry.kt:41)`
   directly above `AllOfKt.resolveAllOf(AllOf.kt:58)`. This is why `recursive-allof-list.json` overflows although
   its back edge is a plain `$ref` that would otherwise be caught.

Only the `ResolvedSchema.Reference` branch (`AllOf.kt:55-56`) passes `scope` on.

Verified on the corpus by script: all 18 failing documents contain a `$ref` cycle with at least one hop through an
inline `allOf` member; among documents that reach this code, only 1 with such a cycle passes (bitbucket.org, cycle not
confirmed reachable from an operation) and 4 fail earlier for unrelated reasons.

## Correct final API

An `allOf` whose members are one `$ref` plus annotation-only schemas is a reference to that type:

```kotlin
@Serializable
public data class Node(
  /** The child node. */
  public val child: Node? = null,
)
```

Wire: `{"child": {"child": {}}}` round-trips. For DynamoDB: `AttributeValue.M: Map<String, AttributeValue>?`.

When the inline members add structure (`allOf: [{$ref: Node}, {properties: {extra: ...}}]`), the property is a new
inline type and must still terminate: properties that point back are `Model.Reference`s to the named component.

### Related wrong output (same code, not a crash)

`allof-ref-object-nonrecursive.json` generates a duplicate nested class instead of a reference:

```kotlin
public data class Node(
  public val owner: Owner? = null,                                             // Node.Owner, a structural copy
  public val owner2: io.github.nomisrev.openapi.generated.model.Owner? = null, // plain $ref: the real component
) { /** x */ public data class Owner(public val name: String? = null) }
```

So `Node.owner` and `Node.owner2` are different Kotlin types for one schema. 317 census documents contain an inline
`allOf` of one `$ref` plus annotation-only members (static count of the pattern; 237 of them currently generate). I
verified the duplicate only on the reduced spec, not per document.

## Fix slice

- typed, `AllOf.kt`: before merging, recognise "single `$ref` + members that only annotate" and return what the plain
  `$ref` returns, with the inline description/title/deprecated applied to the property. `Schema.restates(target)` in
  `DiscriminatedObject.kt:371-383` is already the predicate for "only annotates, constrains or restates".
- typed, `AllOf.kt:58`: call the scope-aware `toModel` so `expanding` and the anchor survive. Check the other
  `with(scope.registry()) { ... toModel(...) }` / `with(scope) { ReferenceOr.value(...).toModel(...) }` call sites
  for the same receiver trap (`RequestBody.kt` `formDataToBody` and `formUrlEncoded` use the Registry member on
  purpose at the root; nested transformer code must not).
- typed: when members are genuinely merged, record every `$ref` member as expanding while the merged schema is
  transformed, so a back edge resolves to a `Model.Reference`.
- Defence in depth: a depth guard in the engine that throws a named error with the schema path instead of
  `StackOverflowError` with an empty message.

## Proof

- Failing tests first, next to `typed/src/commonTest/.../registry/LocalSchemaAliasSpec.kt` (added by 18d5a14c): the
  four overflowing variants above must produce `Model.Reference` to `Node`.
- Golden `ClientSpec` case for `recursive-allof-ref.json` and for the non-recursive `owner` case (expects the
  top-level `Owner`, no nested class).
- Example round-trip: decode/encode a three-level `Node`.
- Integration tests would not catch this: `github.json` has 34 and `openai.yaml` 12 inline `allOf`s with a `$ref`
  member, none on a cycle; `youtrack.json` has none. They do exercise the duplicate-class output, unasserted.

## Affected documents

18 total. amazonaws.com:models.lex.v2 [0065ccfb7b4f60497f81], emr-containers [0d813bdad06ca71fac72],
dynamodb [1c65dd2f1d2889714cb7], connect [244c4004ce4090af20b6], fsx [2cce012b4dec43294893],
kendra [3273d383d1f67621dec5], streams.dynamodb [352e79435e928105fcc0], ssm [3de7c4924916aa0d419c],
wafv2 [441e49ba51836584e983], glue [9872adbdbf3cdd2daa2f]. Also timestream-query, rds-data, ce, athena, sagemaker,
organizations, runtime.lex.v2, elasticmapreduce.
