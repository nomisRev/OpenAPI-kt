# An `allOf` refinement that retypes an inherited property fails the whole document

**Status:** open — the rejection is correct by JSON Schema; the blast radius and the missing remedy are the issue
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** typed
**Impact:** 2 of 1,547 documents (personio.de:personnel, sinao.app). Per the census brief personio generated code before 29766c84; I did not rerun an older build for either
**Source spec valid:** no — taken literally the property admits no value; the author meant "override"

## Symptom

```
IllegalArgumentException: allOf members of 'AttendancePeriodsResponse' are incompatible: allOf members declare
property 'data' incompatibly: No value is both of type object and of type array.
```

Accurate message, but it is an exception from `toApiTree`: one schema stops generation of every operation.

## Minimal reproducer

`<scratchpad>/repro/allof-property-contradictory-types.json` (`GET /thing` returns `$ref: Thing`)

```json
"Thing": { "allOf": [{ "$ref": "#/components/schemas/Response" },
  { "type": "object", "properties": { "data": { "type": "array", "items": { "type": "string" } } } }] },
"Response": { "type": "object", "required": ["success", "data"],
  "properties": { "success": { "type": "boolean" }, "data": { "type": "object" } } }
```

Observed:

```
worker exit=1 phases: parse pass, typed fail
java.lang.IllegalArgumentException: allOf members of 'Thing' are incompatible: allOf members declare property 'data' incompatibly: No value is both of type object and of type array.
```

Corpus shapes:

- personio: `Response.data: {type: object}` is a placeholder; every `*Response` is
  `allOf: [{$ref: Response}, {properties: {data: {type: array, items: ...}}}]`.
- sinao: `SalesDocumentContract.discount: {type: object, properties: {amount, name, percent, value}}`;
  `RecurringInvoice = allOf: [{$ref: SalesDocumentContract}, {properties: {discount: {type: integer}}}]`.

## What the spec says

OAS 3.0.3 §4.7.24 Schema Object, "Composition and Inheritance (Polymorphism)": `allOf` composes, it does not override.
The instance must validate against every member, so `data` must be an object **and** an array. `data` is `required`
in personio's base, so no instance of `AttendancePeriodsResponse` is valid. The document is self-contradictory;
rejecting it is the spec-accurate reading and matches the project rule (accuracy over convenience).

The real APIs return the refined type (personio's `data` is an array). The authors use `allOf` as subclassing.

## Was the new rejection a regression?

No, for both. The message was introduced by 29766c84 (`git log -S`). Before it, a contradiction was not detected,
so one declaration ended up in the output (personio compiled, per the census brief; which declaration won I did not
check). That output was a guess. What is missing is (a) containment and (b) a decision on the override idiom.

The project already made that decision once: ee85b3fb lets a **discriminated** subtype override a property it
redeclares contradictorily, with a warning (`overrideProperty`, `DiscriminatedObject.kt:216-237`; its doc comment:
"Outside a discriminated hierarchy such a contradiction is still rejected"). personio and sinao are the same idiom
without a discriminator.

## Root cause (traced)

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/DiscriminatedObject.kt:447-482` —
  `Schema.mergeNonNull` throws `ContradictoryDeclarations("No value is both of type ...")` (line 480-481).
- `DiscriminatedObject.kt:270-280` — `combineProperty` wraps it with the property name.
- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/AllOf.kt:49-53` — `resolveAllOf` wraps it
  with the schema name and rethrows as `IllegalArgumentException`; nothing above catches it, so it leaves
  `toApiTree`.

## Correct final API

Design decision; two defensible answers.

**Option 1 — treat `[$ref base, inline refinement]` as an override, with a warning** (same rule as ee85b3fb):

```kotlin
@Serializable
public data class Thing(
  public val success: Boolean,
  public val `data`: List<String>,   // the refinement's declaration; required comes from the base
)
// warning: Thing overrides property 'data' it inherits from Response: it declares {"type":"array",...}, which
//          contradicts the inherited {"type":"object"} (No value is both of type object and of type array).
//          Thing's declaration is used.
```

Wire: `{"success":true,"data":["a"]}` decodes. Only when the shape is unambiguous: exactly one side is a `$ref`
member (the parent) and the other an inline member or the schema's own keywords. Two inline members, or two `$ref`s,
that contradict stay an error — there is no "more specific" side.

**Option 2 — keep rejecting, but as a scoped diagnostic.** The schema becomes an error diagnostic that names the
schema, the property and both JSON pointers; operations that use it are not generated (error, per the existing
"only body" rule), everything else is.

Recommendation: option 1. It matches what these APIs send, reuses a rule the project has already accepted for the
identical idiom, and stays honest through the warning. If option 2 is chosen, the user's remedy is an
`OpenApiPreprocessor` that fixes the base schema.

## Fix slice

- typed, `AllOf.kt:43-61` + `DiscriminatedObject.kt:270-280`: when merging a `$ref` member with inline members,
  route property conflicts through `overrideProperty` and record a `PropertyOverride`. Today that record lives on
  `Model.DiscriminatedObject.overriddenProperties`; `Model.Object` needs the same field, or the overrides move to a
  registry-level list.
- renderer, `GenerationDiagnostics.kt:46-63` (`propertyOverrideDiagnostics`): include overrides from plain objects.
- Whichever option: the exception must not escape `toApiTree` for a single unusable schema.
- Open: `required` on an overridden property (personio's base requires `data`): keep required.

## Proof

- Failing test first: typed test on the reproducer (expects `data: List<String>` + one override record, or the
  scoped error under option 2). A second test with two contradicting inline members must still fail.
- Golden `ClientSpec` and a `GenerationDiagnostics` test for the warning text; example round-trip.
- Integration tests: `youtrack.json` covers the discriminated variant (`ActivityItem.added`), which is why ee85b3fb
  exists. None of the three integration specs has the non-discriminated variant, so they would not catch this.

## Affected documents

2 total. personio.de:personnel [2b743ccb791458becf0e] (`AttendancePeriodsResponse.data`: object vs array),
sinao.app [41279b7cf3df92ea8619] (`RecurringInvoice.discount`: object vs integer).
