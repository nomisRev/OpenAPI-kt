# Design: deterministic union deserializers

**Status:** proposal, for discussion (2026-11-16). Nothing implemented.
**Scope:** how a generated `oneOf`/`anyOf` serializer picks the case for an incoming JSON value. Not about how `typed`
builds the cases (that is `allof-union-merge-drops-branches-silently.md`, fixed in `cfba4c52`).

## Today

Every union gets a `UnionDispatch` (`typed/.../Model.kt:9`):

- `NativeDiscriminator` / `TaggedCustom`: a declared `discriminator` (or an inferred tag property). Deterministic: the
  tag's value selects the case.
- `Structural`: everything else. The renderer (`UnionRendererSerialization.kt:124-240`) then
  1. dispatches on the JSON kind of the value (`UnionTokenKindDispatch.kt`: string, number, boolean, null, array,
     object), so a case is only tried for the kinds its schema permits;
  2. for an `anyOf` of objects whose cases each have a property no other case has, dispatches on that property
     (`AnyOfUniqueKeyDispatch.kt`, at most one case without such a key);
  3. otherwise calls `json.attemptDeserialize(value, case1, case2, ...)`: decodes the cases in a fixed priority order
     (objects with few properties first, `String` and free-form JSON last, `UnionRendererSerialization.kt:33-53`) and
     returns the first that does not throw `IllegalArgumentException`.

Step 3 is what the maintainer called "guessing". Its problems:

- **It depends on what the decoder happens to reject.** Which case wins depends on `Json` settings
  (`ignoreUnknownKeys`, `isLenient`, `coerceInputValues`), on kotlinx.serialization's checks (an enum rejects an
  unknown value, a `String` accepts any string), and on the priority table, not on the schema. linqr.app's
  `uri: anyOf [Icon, string]` is correct only because the enum case is tried first and throws for other strings.
- **It cannot tell "no case matches" from "this case is malformed".** The error lists every case's exception; a
  value meant for case 2 with one wrong field is reported as matching nothing.
- **`oneOf` is not enforced.** A value that two cases accept silently becomes the first one.
- **Cost and control flow by exception.** Every miss is a decode plus a thrown exception, also on hot response paths.

## Corpus

Static classification of every `oneOf`/`anyOf` with two or more non-null branches in the 1,547 documents
(`$ref`s resolved, `allOf` members merged shallowly; script in the session scratchpad, `scripts/unions.py`). Each
union is put in the cheapest bucket that tells all its cases apart:

| Distinguished by | Unions | Documents | Example |
| --- | ---: | ---: | --- |
| JSON kind alone | 3,424 | 76 | stripe.com (736), microsoft.com:graph |
| A required key only one case has | 3,890 | 67 | microsoft.com:graph (3,208), github.com (47) |
| A property key only one case declares | 256 | 65 | |
| A property with a distinct `const` / single-value `enum` per case (implicit tag) | 178 | 45 | |
| A declared `discriminator` | 103 | 23 | |
| Enum membership (disjoint string/number enums, at most one plain) | 240 | 8 | linqr.app `Icon` vs `string` |
| String format (`date-time` vs `date` vs plain) | 39 | 7 | |
| A branch without type, or itself a union | 171 | 35 | |
| **Ambiguous by these rules** | **375** | **70** | ljaero.com:dflight (24), rebilly.com (17) |

The ambiguous ones, by the JSON kind their cases share: object 217, array 60 (github.com: `oneOf` of two arrays of
different items), array and object 40, number 34 (`integer` vs `number`), string 23, number and string 1.

So about 95% of the unions can be decided by checks on the JSON tree that never decode a case speculatively.

The "ambiguous" bucket above only compares the cases' top-level kinds and keys. Most of it is decided one or two
levels deeper in the same tree, so the analysis must recurse:

| Ambiguous cases share | Unions | What separates them |
| --- | ---: | --- |
| Objects with identical property names | 232 | mostly the kind or item kind of a shared property (github.com's 422 `oneOf [validation-error, validation-error-simple]`: `errors` is an array of objects vs of strings); 17 the enum values of a shared property; 4 have the same shape |
| Arrays with the same item kind | 53 | the items' own properties, recursively |
| Arrays with different item kinds | 45 | the first element's kind |
| `integer` vs `number` | 32 | whether the number is integral |
| One object's properties a subset of another's | 24 | presence of an extra key |
| Strings with nothing to compare | 24 | nothing |

What remains is not a deserializer problem: values the schema admits in both cases (an empty `errors` array, the
number `5` for `integer` vs `number`, `{message}` when one case only adds optional keys, identical shapes). Both
readings are valid per the spec (and such a value violates a `oneOf`), so they need a rule, not more inspection:
open decision 1.

## Proposal

Replace step 3 with a decision tree computed at generation time and emitted as plain code. Decoding then happens
exactly once, with the chosen case's serializer.

1. **Discriminant analysis** (new, in `typed` or the renderer, over `Model.Union.cases`). For each union, find a
   predicate per case on the `JsonElement`, in this order of preference, recursing per JSON kind:
   - JSON kind (as today);
   - declared or implicit tag: a property whose `const`/single `enum` value differs per case;
   - presence of a required key no other case has; then of a declared property no other case has;
   - enum membership for primitives (`value.content in setOf(...)`), with at most one non-enum case as the else;
   - number shape: an integer case vs a number case (`value.longOrNull != null`), only meaningful when the integer
     case is listed first or the number case excludes integers — otherwise ambiguous;
   - string format: `date`, `date-time`, `uuid` parse checks, plain string as the else;
   - array: the JSON kind (and tag, recursively) of the first element; an empty array is the first array case.
   The result is either `Deterministic(tree)` or `Ambiguous(cases, reason)` listing the cases no predicate separates.
2. **Code generation.** The renderer turns the tree into a `when` over the `JsonElement` and calls exactly one case
   serializer. A value that no branch of the tree admits throws `UnionSerializationException` naming the predicate
   that failed ("expected one of the keys [radius, side]"), and a malformed value for the chosen case surfaces that
   case's own error.
3. **Ambiguous unions** need a policy (open decision 1). They are 4% of unions; the analysis reports each one as a
   generation warning naming the overlapping cases, so specs can be fixed upstream.
4. **`attemptDeserialize` stays** only for ambiguous unions (if decision 1 keeps a fallback) and free-form branches.

What this does not change: the encoding side (the case already knows its serializer), the case classes and names, and
the `TaggedCustom`/`NativeDiscriminator` paths.

### Example (linqr.app)

```kotlin
// today
value is JsonPrimitive && value.isString -> json.attemptDeserialize(value,
    CaseIcon::class to { CaseIcon(decodeFromJsonElement(Icon.serializer(), it)) },
    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) })

// proposed
value is JsonPrimitive && value.isString -> when (value.content) {
    "icon://appstore", "icon://bitcoin" -> CaseIcon(json.decodeFromJsonElement(Icon.serializer(), value))
    else -> CaseString(value.content)
}
```

### Example (objects told apart by a required key)

```kotlin
value is JsonObject -> when {
    "radius" in value -> RadiusAndColor(json.decodeFromJsonElement(RadiusAndColor.serializer(), value))
    "side" in value -> SideAndColor(json.decodeFromJsonElement(SideAndColor.serializer(), value))
    else -> throw UnionSerializationException(value, "expected one of the keys [radius, side]")
}
```

## Interactions and risks

- **Required-key dispatch trusts the server to send required keys.** A response missing a required key fails today
  too (the case's own decoder rejects it); with dispatch it fails with a clearer message, or picks another case that
  does not require it. That is a behaviour change only for invalid responses.
- **Unknown keys.** Today a case that rejects an unknown key (default `Json`) makes `attemptDeserialize` try the next
  case. With dispatch the chosen case's error is reported. More correct, but a client that relied on the fallback
  would now fail; worth a release note.
- **Nullable and `null` branches** are already a separate JSON kind.
- **Recursive unions** (`A: oneOf [string, array of A]`): the predicates only look one level deep, so no recursion.
- **Regression surface:** the committed github and openai clients change in every structural union (generated code
  only; the cases do not change). The runtime tests per golden are the check.

## Open decisions

1. **Values two cases both admit** (after the recursive search; empty arrays, integral numbers, subsets, identical
   shapes):
   - (a) the most specific case that admits it (the integer case, the case without extra keys), warning at generation
     (deterministic and explainable; recommended);
   - (b) declaration order, warning (deterministic, but depends on how the spec lists them);
   - (c) keep `attemptDeserialize` for these unions (no client gets worse, but keeps the guessing);
   - (d) refuse to generate the union (strict; shrinks supported specs, against the project principle).
2. **`oneOf` exclusivity**: when two cases both admit a value (only possible for ambiguous unions), fail the decode
   (spec-correct for `oneOf`) or take the first (lenient). Recommended: lenient with the generation warning, since
   generated specs overlap by accident far more often than servers send ambiguous values.
3. **Where the analysis lives.** In `typed` it is testable without rendering and can feed diagnostics; the renderer
   only needs the tree. Recommended: `typed`, as a field on `Model.Union` next to `dispatch`.
4. **Formats.** Whether `date-time` vs `string` is a dispatch predicate (needs a parse attempt, which is again "try
   and catch", but local and cheap) or ambiguous. Recommended: a predicate, using the same parser the case uses.

## Slices

1. Analysis only: compute the tree, report ambiguous unions as warnings, no generated-code change. Corpus run to
   confirm the numbers above against the real models (the static script approximates `allOf` merging).
2. Objects: tag, required key, declared key. Covers most of microsoft.com:graph and github.com.
3. Primitives: enum membership, integer vs number, formats.
4. Arrays by first-element kind.
5. Remove `attemptDeserialize` from every union the tree decides; keep it per decision 1.

Each slice: failing runtime tests first against a golden per predicate (including a malformed value for the chosen
case and a value no case admits), regenerate the integration-tests clients, census.
