# An `allOf` that merges a union with another schema drops failing branches without a diagnostic, and an all-failing union becomes free-form JSON

**Status:** fixed in `cfba4c52`. A branch with no value in common with the other members is dropped with a warning; a branch whose intersection exists but is not generated (`UngeneratedIntersection`, split from `ContradictoryDeclarations`) fails the document instead of being dropped; no branch left fails like an allOf without a union (decided: fail, not `JsonElement`); every other failure, cancellation included, propagates. The five documents that reach the merge stop where they did (re-checked with `--filter`). Not changed: the warning names no owning schema.
**Confirmed:** found while fixing `fc10b47d` (2026-11-16), reproduced at HEAD `8405f814`; instrumented corpus pass 2026-11-16
**Stage:** typed
**Impact:** 0 of 1,547 documents at HEAD drop a branch (counted with a temporary stderr print in a throwaway worktree, full pass over every 3.x document of the preferred census, not sampled; see Affected documents). 5 documents reach `mergeCompositeUnion` and all their branches merge. Before `fc10b47d` the pattern lost both branches of a union in linqr.app's `Icon`-style `allOf`. A lower bound: the 92 documents that stop in the typed phase at HEAD may reach the path later. The defect is latent and silent: the output compiles and is wrong.
**Source spec valid:** yes — every reproducer below is a valid OAS 3.0.3 document. The all-branches-fail one is vacuous (no value satisfies it) but valid.

## Symptom

`mergeCompositeUnion` merges each branch of a `oneOf`/`anyOf` with the other `allOf` member (or branch with branch when both are unions) inside `runCatching { ... }.getOrNull()`. A branch whose merge throws is removed from the result. Nothing is recorded: no warning, no diagnostic, no error.

- Some branches fail: the union keeps the survivors. Zero survivors left it a union of nothing; one survivor collapses to that branch's schema (the `oneOf` disappears).
- Every branch fails: `mergeCompositeUnion` returns `Schema(description = description ?: other.description)`. An empty schema with a description is free-form JSON, so the property is generated as `JsonElement`. The merge that was supposed to prove "no value is valid" reads as "any value is valid" instead, and the document passes with no diagnostic. The same merge without a union around it (`allOf: [{type: string}, {type: boolean}]`) fails the document with `ContradictoryDeclarations`.
- The `runCatching` catches `Throwable`, not just the merge's own errors. That swallows more than contradictions (below), including `CancellationException` and `StackOverflowError`.

## Minimal reproducers

All use `GET /things` returning `$ref: Thing`, run through `exploration.CorpusWorkerKt` in an instrumented worktree. Files: `<scratchpad>/merge-findings/repro/`.

**A. Every branch fails** — `union-allfail.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Api", "version": "0.0.1" },
  "paths": { "/things": { "get": { "operationId": "getThing",
    "responses": { "200": { "description": "OK", "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Thing" } } } } } } } },
  "components": { "schemas": {
    "Thing": { "type": "object", "properties": { "v": { "description": "A value", "allOf": [ { "oneOf": [ { "type": "string" }, { "type": "integer" } ] }, { "type": "boolean" } ] } } }
  } }
}
```

Observed: phases `parse, typed, render, diagnostics, emit` all pass, `diagnostics.json` is `[]`, branch failures `No value is both of type string and of type boolean.` and `... integer and ... boolean.` were thrown and discarded, and `Thing.kt` is

```kotlin
public data class Thing(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val v: JsonElement? = null,
)
```

**B. Some branches fail, correctly** — `union-partial.json`: same document with `"v": { "allOf": [ { "oneOf": [ {type: string}, {type: integer}, {type: object, properties: {a}} ] }, { "type": "string", "maxLength": 3 } ] }`.

Observed: two branches fail with `No value is both of type integer and of type string.` and `... object and ... string.`, no diagnostic, `v: String?`. Dropping them is right (below), but nobody is told.

**C. A branch that does admit values is dropped** — `union-valid-branch-dropped.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Api", "version": "0.0.1" },
  "paths": { "/things": { "get": { "operationId": "getThing",
    "responses": { "200": { "description": "OK", "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Thing" } } } } } } } },
  "components": { "schemas": {
    "Thing": { "allOf": [
      { "oneOf": [
        { "type": "object", "properties": { "p": { "$ref": "#/components/schemas/Cat" } } },
        { "type": "object", "properties": { "q": { "type": "string" } } } ] },
      { "type": "object", "properties": { "p": { "$ref": "#/components/schemas/Pet" } } } ] },
    "Cat": { "oneOf": [ { "type": "string", "enum": ["tom"] }, { "type": "integer" } ] },
    "Pet": { "oneOf": [ { "type": "string" }, { "type": "integer" }, { "type": "boolean" } ] }
  } }
}
```

Observed: the first branch throws `ContradictoryDeclarations: allOf members declare property 'p' incompatibly: Cat and Pet do not extend one another, and one of them is a union (oneOf/anyOf) or has a discriminator; their intersection is not generated.` It is swallowed, and `Thing` becomes the second branch only:

```kotlin
public data class Thing(
  public val q: String? = null,
  public val p: Pet? = null,
)
```

Both `Cat` and `Pet` admit `"tom"` and `5`, so the first branch admits `{"p": "tom"}`. The identical shape without the outer union, `allOf: [{object, p: Cat}, {object, p: Pet}]` (`union-no-outer-union.json`), fails the document with that same message. So the union turns "refuse, because it cannot be generated" into "generate something narrower and say nothing".

## What the spec says

Quoted from memory, not re-read:

- JSON Schema 2020-12 Core §10.2.1.1 `allOf`: an instance is valid only if it is valid against every subschema. `oneOf` (§10.2.1.3) is valid against exactly one, `anyOf` (§10.2.1.2) against at least one. OAS 3.0.3 §4.7.24 Schema Object defines `allOf`/`oneOf`/`anyOf` with the same meaning.
- Distribution: `allOf: [oneOf: [A, B], C]` is `oneOf: [allOf: [A, C], allOf: [B, C]]` when A and B are mutually exclusive, and `anyOf` likewise. A branch `A` with no value in common with `C` makes `allOf: [A, C]` admit nothing, so that branch contributes no instance. Dropping it is correct.
- When every branch has an empty intersection, the whole schema admits no value. That is a contradiction in the same sense as `allOf: [{type: string}, {type: boolean}]`. It is not "any value".

So the semantics are right for a true contradiction. Three things are wrong: it is silent, an empty result becomes free-form JSON, and branches that are not contradictions are dropped too.

## Root cause (traced)

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/DiscriminatedObject.kt:647-686`, `Schema.mergeCompositeUnion`:

- 654, 660, 665: `runCatching { ...merge(...) }.getOrNull()?.let { ReferenceOr.value(it) }` for union-with-union, union-with-schema and schema-with-union.
- 671: `if (mergedBranches.isEmpty()) return Schema(description = description ?: other.description)`.
- 672: `if (mergedBranches.size == 1) return mergedBranches.single().peek()`.

What is swallowed, by origin:

| Thrown | Where | True contradiction? |
| --- | --- | --- |
| `ContradictoryDeclarations` "No value is both of type X and of type Y" | 564, 571 | yes |
| `ContradictoryDeclarations` "Enum intersection is empty" | 696 | yes |
| `ContradictoryDeclarations` "No value has both format ..." | 742 | yes (formats strictly are annotations, but the merge treats them as exclusive) |
| `ContradictoryDeclarations` "... one of them is a union ... their intersection is not generated" | 365 | no: values exist, the type cannot be expressed |
| `ContradictoryDeclarations` "... refer to themselves ... a recursive type that is not generated" | 373 | no: same |
| `IllegalArgumentException` "Cannot infer the type of ..." | 584 (`impliedType`) | no: an internal gap |
| `IllegalStateException` "Expected at least one enum" (`error(...)`) | 713 | no: a bug |
| `StackOverflowError` from recursive `$ref`s (see `recursive-allof-ref-stack-overflow.md`) | any | no |
| `CancellationException` | any suspension point in `peek()`/`merge` | no |

The doc comment on `ContradictoryDeclarations` (lines 244-249) says it covers both "no value satisfies both" and "whose intersection no generated type expresses". That second reading is what makes C wrong: lines 365 and 373 are not contradictions, and `mergeCompositeUnion` cannot tell them apart from the others because it catches everything.

Contrast: `Registry.kt:180-186` (`target.merge(shape)`) catches `IllegalArgumentException` and calls `warn("... is ignored: ${e.message}")`. `AllOf.kt:50-52` rethrows with the schema name. The union path is the only caller that discards the reason.

### The `CancellationException` concern

`mergeCompositeUnion` is a `suspend` function and `merge`/`peek` are suspend. `runCatching` also catches `CancellationException`. If the coroutine is cancelled while a branch merges (the Gradle task or a test is cancelled, a surrounding `withTimeout` fires), the exception is treated as "this branch failed" and the loop goes on. The caller then continues with a collapsed schema, and the cancellation only surfaces at some later suspension point, or never if none comes. The IDE flags the three `runCatching` lines for this reason. It is a correctness problem in its own right, independent of the diagnostic: the fix is not to catch cancellation. Catching `Throwable` also hides `StackOverflowError`/`OutOfMemoryError` as a "branch dropped".

## Correct final API

No Kotlin shape changes for a correct merge; the fixes are in what is reported and what is refused.

- **A true contradiction in some branches** (B): the union keeps the survivors, as today (`v: String?`), and the document has a warning once per distinct reason, for instance
  `allOf of a oneOf with {type: string, maxLength: 3}: branch integer is dropped (no value is both of type integer and of type string); branch object is dropped (no value is both of type object and of type string).`
  Nothing is generated for the dropped branch, so the maintainer's "anything not generated gets a diagnostic" applies.
- **A branch with a real intersection that cannot be generated** (C): not dropped. The merge fails like the same `allOf` without the union:
  `allOf members of 'Thing' are incompatible: ... Cat and Pet do not extend one another, and one of them is a union ...` (or, once the intersection of two unions is implemented, the intersection). A generated client that silently accepts fewer values than the spec is worse than a refusal.
- **Every branch contradicts** (A): the merge throws `ContradictoryDeclarations` that lists each branch's reason (`allOf members of 'Thing.v' are incompatible: no branch of the oneOf has a value in common with the other member: string (No value is both of type string and of type boolean); integer (...)`). That is what the same `allOf` without a union does, and a surrounding union merge (a union branch that is itself an `allOf` of a union) can then treat it as a failing branch.

Open decision: for case A, an error (above, consistent, shrinks the supported set by the documents that contain such a schema, 0 today) or a warning plus `JsonElement` (accepts the document; keeps today's type but reports it; decodes whatever the server sends). Both give a diagnostic; the second fits "strictness must not shrink supported specs" better, the first fits "correctness over convenience" and the existing non-union behaviour.
- **Cancellation**: `CancellationException` and other `Error`s propagate.

## Fix slice

- typed, `DiscriminatedObject.kt`: split the class. A `NoCommonValue` (or a flag on `ContradictoryDeclarations`) for 564, 571, 696, 742. The "not generated" throws at 365 and 373 become a separate `UnsupportedIntersection` that `Schema.merge` callers still report the same way.
- `mergeCompositeUnion`: replace the three `runCatching` with `try { ... } catch (e: NoCommonValue)`; collect `(branch description, e.message)`; propagate everything else. Warn through `ctx.registry().warn(...)` (a set, so the many repeated merges of one `allOf` are reported once; the census instrumented run showed each merge runs twice per document).
- Empty result: throw `NoCommonValue` with the collected reasons, unless the decision above picks warning + free-form.
- Describe a branch by its `$ref` name or its type, not by `toString()` of the schema.
- Do not touch the `mergedBranches.size == 1` collapse, but report the dropped sibling.

## Proof

Failing tests first, in `typed/src/commonTest/kotlin/io/github/nomisrev/transformers/AllOfUntypedMemberSpec.kt` style (`merged(...)` / `rejected(...)`, which already pin `oneOf [string, integer]` next to `{additionalProperties: false}` keeping both branches, `fc10b47d`):

- `oneOf [string, integer, object]` with `{type: string}` is `Model.Primitive.String` and the registry has a warning naming `integer` and `object`.
- `oneOf [string, integer]` with `{type: boolean}` is rejected, with a message naming both branches (or a warning + `FreeFormJson` if that is the decision).
- Reproducer C is rejected with the type message, the same message as `allOf: [{p: Cat}, {p: Pet}]`.
- A cancelled coroutine merging a union rethrows `CancellationException` (`runTest { ... cancel() }`).
- Golden `renderSpec` for B with the warning in `diagnostics`.
- Integration tests would not catch a regression: the generated clients are unchanged by this slice, no corpus document is affected today.

## Affected documents

Counted at HEAD with a temporary `System.err` print in `mergeCompositeUnion` (entry, per-branch failure class and message, summary) over all 1,547 3.x documents of `exploration/build/preferred-census/specs` (full pass, 8 parallel workers).

- Documents with a failing branch: 0.
- Documents that reach `mergeCompositeUnion` (a union merged with another schema in an `allOf`), all branches merged successfully: 5. influxdata.com [0c204911aa4ad745ebae], box.com [4f89bc9851d389307c20], wiremock.org:admin [69ebc25f44cdc0ee5887], intellifi.nl [9eecdb7930e11de8e1a4], graphhopper.com [f099a6c66320e9be8695].
- Before `fc10b47d`: linqr.app (both branches of `oneOf [string, integer]` next to `{additionalProperties: false}` were dropped, per that commit's message; not re-run).
- Documents that fail in the typed phase and may reach the path afterwards: 92 (census of `ccf7f728`).
