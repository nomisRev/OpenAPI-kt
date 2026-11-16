# `allOf` members that declare one property with two different `$ref`s are rejected although the intersection exists

**Status:** fixed in `7e2f5586`. Two unrelated `$ref`s on one property or array items are merged: one schema under two names (equal but for annotations) is the later `$ref`, a merge equal to one of them is that `$ref` (an enum and its subset is the subset), anything else is an inline type with a warning. Unions, discriminated schemas and two recursive schemas are still rejected, with their names. A discriminated subtype redeclaring an inherited `$ref` with an unrelated one both accept is now the intersection, not an override. Re-checked with `--filter`: beezup.com compiles; nexmo.com:reports reaches compile and stops on `"default": "none"` outside its enum (`enum-default-not-in-enum`). Corrections to this report: before 29766c84 the second `$ref` was silently kept (beezup lost properties), and a closed object merged with an open one is not rejected, it becomes the union of properties and loses `additionalProperties: false` (not fixed here). Merging two unions drops failing branches without a diagnostic (`mergeCompositeUnion`), not fixed here either.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** typed
**Impact:** 2 of 1,547 documents (nexmo.com:reports, beezup.com); both generated code before 29766c84
**Source spec valid:** yes — in both documents a value satisfying both declarations exists

## Symptom

```
IllegalArgumentException: allOf members of 'Thing' are incompatible: allOf members declare property 'product'
incompatibly: Product and ProductVoice do not extend one another, so a value of both is an intersection of two named
types that no generated type represents.
```

The claim "no generated type represents" is false for both corpus cases. The whole document fails.

## Minimal reproducers

Both use a `GET /thing` returning `$ref: Thing`.

**A. Two `$ref`s to equivalent schemas** (nexmo) — `<scratchpad>/repro/allof-property-equivalent-refs.json`

```json
"Thing": { "allOf": [{ "$ref": "#/components/schemas/Base" },
  { "type": "object", "properties": { "product": { "$ref": "#/components/schemas/ProductVoice" } } }] },
"Base": { "type": "object", "properties": { "product": { "$ref": "#/components/schemas/Product" } } },
"Product":      { "type": "string", "enum": ["SMS", "VOICE-CALL"], "example": "SMS" },
"ProductVoice": { "type": "string", "enum": ["SMS", "VOICE-CALL"], "example": "VOICE-CALL" }
```

Observed: `typed fail ... Product and ProductVoice do not extend one another, so a value of both is an intersection of two named types that no generated type represents.`

In nexmo, `product` and `product_voice` are the same nine-value string enum; only `example` differs.

**B. Two `$ref`s to open object schemas** (beezup) — `<scratchpad>/repro/allof-property-two-object-refs.json`

```json
"Thing": { "allOf": [{ "$ref": "#/components/schemas/Column" },
  { "type": "object", "properties": { "links": { "$ref": "#/components/schemas/DetectedLinks" } } }] },
"Column": { "type": "object", "properties": { "id": { "type": "string" }, "links": { "$ref": "#/components/schemas/ColumnLinks" } } },
"ColumnLinks":   { "type": "object", "properties": { "rename": { "type": "string" } } },
"DetectedLinks": { "type": "object", "properties": { "configure": { "type": "string" }, "ignore": { "type": "string" } } }
```

Observed: `typed fail ... ColumnLinks and DetectedLinks do not extend one another, so a value of both is an intersection of two named types that no generated type represents.`

## What the spec says

OAS 3.0.3 §4.7.24 Schema Object: `allOf` is JSON Schema `allOf` — the instance must validate against every member.
A property declared by two members must validate against both declarations, i.e. against
`allOf: [{$ref: A}, {$ref: B}]`.

- A: both targets accept exactly `"SMS" | "VOICE-CALL"`. `example` is an annotation. The intersection is that enum.
- B: neither object is closed (`additionalProperties` absent), so `{"rename": "x", "configure": "y"}` validates
  against both. The intersection is the object with the union of the properties. It is the same merge the generator
  already performs for a top-level `allOf: [{$ref: ColumnLinks}, {$ref: DetectedLinks}]`.

## Root cause (traced)

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/DiscriminatedObject.kt:319-339`,
`ReferenceOr.Reference.combine(other: ReferenceOr.Reference)`:

```kotlin
val [specific, general] = when {
    target == otherTarget || extends(other) -> this to other
    other.extends(this) -> other to this
    else -> throw ContradictoryDeclarations("... do not extend one another ...")
}
```

- `target == otherTarget` is data-class equality on `Schema`, annotations included, so A fails on `example`.
- Anything else that is not an `allOf` ancestor relation (`extends`, lines 350-357) throws without attempting the
  structural merge that `ReferenceOr.Reference.combine(value: Schema)` (lines 297-305) performs through
  `mergeTarget` -> `Schema.merge` when one side is inline. So `{$ref: A}` + inline copy of B merges, but
  `{$ref: A}` + `{$ref: B}` is rejected.

The doc comment at lines 265-267 states this as intent ("rejected rather than resolved by picking one"). The intent
is right about not *picking* one; it overlooks that merging is available. It is thrown as `ContradictoryDeclarations`,
the class documented (lines 244-249) as "no value satisfies both", which is not true here.

## Correct final API

A — the two enums are one value set; reference either component (prefer the more specific member's `$ref`, here the
inline member's `ProductVoice`), no new type:

```kotlin
@Serializable public data class Thing(public val product: ProductVoice? = null)
```

B — an inline merged object, as any property-level `allOf` of two objects:

```kotlin
@Serializable
public data class Thing(public val id: String? = null, public val links: Links? = null) {
  @Serializable
  public data class Links(
    public val rename: String? = null,
    public val configure: String? = null,
    public val ignore: String? = null,
  )
}
```

Wire: `{"links":{"rename":"a","configure":"b"}}` round-trips. A real contradiction between the two targets (string vs
object, disjoint enums, one closed object) still fails, with `Schema.merge`'s specific reason.

## Fix slice

- typed, `DiscriminatedObject.kt:319-339`: when neither `$ref` extends the other, return
  `ReferenceOr.value(mergeTarget(target, otherTarget))` instead of throwing; let `Schema.merge` decide whether a value
  exists. Guard recursion (the targets may reference each other).
- Keep the `$ref` (no inline type) when the merge result is equivalent to one of the targets ignoring annotations
  (`description`, `title`, `example(s)`, `deprecated`, `externalDocs`, `default`): covers A and avoids a duplicate
  enum class.
- Design decision for A when value sets are equal: which name wins. Suggest the later/inline member, matching the
  override direction used for discriminated subtypes in ee85b3fb.
- Unrelated cleanup: this merge code lives in `DiscriminatedObject.kt` though it serves every `allOf`.

## Proof

- Failing tests first: typed tests on both reproducers (expected models above), plus a negative test that
  `{$ref: StringEnum}` + `{$ref: ObjectSchema}` still fails with the type message.
- Golden `ClientSpec` for B; example round-trip for B with properties of both parents.
- Integration tests would not catch a regression: `github.json` (13), `youtrack.json` (43) and `openai.yaml` (4)
  have `allOf`s whose members redeclare a property, and all pass today, so none has two unrelated `$ref`s on one
  property.

## Affected documents

2 total. nexmo.com:reports [08b5ce21c4b3cc70f5f6] (`product` vs `product_voice`, in
`#/components/responses/json_record_response/.../oneOf/2/allOf`), beezup.com [6d8bcb5b2ea80deb9c98]
(`detectedCatalogColumn.links`: `catalogColumnLinks` vs `detectedCatalogColumnLinks`).
