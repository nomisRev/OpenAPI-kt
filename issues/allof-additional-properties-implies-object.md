# `additionalProperties` next to an `allOf` of a non-object is treated as `type: object` and reported as a contradiction

**Status:** fixed in `fc10b47d`. A typeless `additionalProperties: true`, `false` or `{}` next to a type that does not permit objects is merged as an annotation, without a warning (the schema accepts the same values). The report's broader rule (`properties` and `items` vacuous too) was not taken: it contradicts the decisions pinned in `AllOfUntypedMemberSpec` (a `oneOf [array, string]` with untyped `items` is the array branch). Those, and a schema-valued `additionalProperties`, are still rejected, and the error now names the implying keyword. Side effect: `oneOf [string, integer]` next to `{additionalProperties: false}` keeps both branches, where `mergeCompositeUnion` used to drop both silently. Re-checked with `--filter`: linqr.app compiles. Not changed: a single-member `allOf` alias with a sibling `additionalProperties` ignores it silently, which is equally vacuous.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** typed
**Impact:** 1 of 1,547 documents (linqr.app)
**Source spec valid:** yes — `additionalProperties` constrains objects only and is vacuous for a string

## Symptom

```
io.github.nomisrev.openapi.transformers.ContradictoryDeclarations: No value is both of type string and of type object.
	at ...DiscriminatedObjectKt.mergeNonNull(DiscriminatedObject.kt:480)
	at ...DiscriminatedObjectKt.mergeEnum(DiscriminatedObject.kt:588)
	at ...UnionKt.flattenAllOfForUnionDiscriminator(Union.kt:299)
	at ...UnionKt.inferTagOnlyDiscriminatorOrNull(Union.kt:109)
	at ...UnionKt.buildAnyOf(Union.kt:56)
```

No schema name in the message. The whole document fails.

## Minimal reproducer

`<scratchpad>/repro/allof-vacuous-additional-properties.json` (`GET /thing` returns `$ref: Thing`)

```json
"Thing": { "type": "object", "properties": {
  "uri": { "anyOf": [{ "$ref": "#/components/schemas/Icon" }, { "type": "string", "format": "uri" }] } } },
"Icon":  { "allOf": [{ "$ref": "#/components/schemas/Icons" }], "additionalProperties": false,
           "description": "A predefined icon." },
"Icons": { "type": "string", "enum": ["icon://appstore", "icon://bitcoin"] }
```

Observed:

```
worker exit=1 phases: parse pass, typed fail
io.github.nomisrev.openapi.transformers.ContradictoryDeclarations: No value is both of type string and of type object.
```

This is what pydantic/FastAPI emits for an enum field with `extra = forbid`. linqr:
`EmbeddedImage.uri: anyOf [Icon, Storage, URL]`, `Icon = {allOf: [{$ref: Icons}], additionalProperties: false}`.

## What the spec says

JSON Schema (OAS 3.0.3 §4.7.24 Schema Object defers to it): `additionalProperties`, `properties`, `required` apply
only when the instance is an object; `items` only when it is an array. They never imply a type. `"icon://appstore"`
validates against `Icon`: it is one of `Icons`, and `additionalProperties: false` says nothing about a string.

## Root cause (traced)

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/Union.kt:294-301` —
  `flattenAllOfForUnionDiscriminator` merges the `allOf` members with the owner minus `allOf`:
  `allOfSchemas.map { ... } + copy(allOf = null)`. For `Icon` the second operand is
  `{additionalProperties: false, description}`.
- `DiscriminatedObject.kt:447-482` (`mergeNonNull`): the enum side goes through `mergeEnum` (line 571), which strips
  the enum and merges `{type: string}` with that operand. It is neither same-typed, nor `isObjectLike() &&
  other.isObjectLike()`, nor `isNonStructural()`, so line 477-478 applies `impliedType()` to both.
- `DiscriminatedObject.kt:490-494` and `:525-526` — `impliedType()` returns `object` because `isObjectLike()` is
  `type == Object || properties != null || additionalProperties != null`. Now it is "string vs object": throw.

The plain alias path does not hit this: a component `allOf: [{$ref}]` resolves through `singleBranch`
(`AllOf.kt:47`). It only fails where the owner's remaining keywords are merged: union discriminator inference
(`Union.kt:109`, also `:253`, `:259`, `:671`, `:709`, `:738`) and `expandAllOf` (`DiscriminatedObject.kt:507-509`).

Guess, not verified: `{allOf: [{$ref: StringSchema}, {additionalProperties: false}]}` (the keyword in a member
instead of on the owner) likely fails the same way via `AllOf.kt:50`.

## Correct final API

`Icon` is an alias of the string enum; the union has an enum case and a string case:

```kotlin
@Serializable public enum class Icons { @SerialName("icon://appstore") Appstore, @SerialName("icon://bitcoin") Bitcoin }

@Serializable
public data class Thing(public val uri: Uri? = null) {
  @Serializable(with = Uri.Serializer::class)
  public sealed interface Uri {
    @JvmInline public value class CaseIcon(public val value: Icons) : Uri     // exact names per existing union naming
    @JvmInline public value class CaseString(public val value: String) : Uri
  }
}
```

Wire: `{"uri":"icon://bitcoin"}` decodes to the enum case, `{"uri":"https://x"}` to the string case.

## Fix slice

- typed, `mergeNonNull` (`DiscriminatedObject.kt:447-482`): when exactly one side has an explicit `type` and the
  other has only keywords that do not apply to that type (`additionalProperties`/`properties`/`required` vs a
  non-object, `items` vs a non-array), those keywords are vacuous: merge as annotations (`mergeCommon`) instead of
  inferring a type. `impliedType()` stays for the case where *neither* side declares a type, which is what 0e374d2f
  was written for.
- Optional warning diagnostic: "`additionalProperties` has no effect on a string schema".
- Message: `ContradictoryDeclarations` thrown from the union path needs the schema name, like `resolveAllOf` adds.

## Proof

- Failing test first: typed test on the reproducer; unit test for `Schema.merge` of `{type: string, enum}` with
  `{additionalProperties: false}` returning the enum unchanged. Keep the existing 0e374d2f tests green.
- Golden `ClientSpec` for the union; example round-trip for both branches.
- Integration tests would not catch a regression: none of `github.json`, `youtrack.json`, `openai.yaml` fails here
  today, and I found no component combining a non-object `allOf` with `additionalProperties` in them (not
  exhaustively checked for nested inline schemas).

## Affected documents

1 total. linqr.app [d430be64190231793795] (`#/components/schemas/Icon`, used by `EmbeddedImage.uri`).
