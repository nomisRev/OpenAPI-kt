# A discriminator whose mapping does not list the base schema itself crashes with NoSuchElementException

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** typed
**Impact:** 5 of 1,547 documents (viator, twitter, microsoft graph, rebilly, just-eat)
**Source spec valid:** yes — this is the inheritance example from the spec itself

## Symptom

```
java.util.NoSuchElementException: Collection contains no element matching the predicate.
	at io.github.nomisrev.openapi.transformers.DiscriminatedObjectKt.toDiscriminatedObject(DiscriminatedObject.kt:758)
	at io.github.nomisrev.openapi.pipeline.plugins.reference.DiscriminatedObjectPlugin.transform(DiscriminatedObjectPlugin.kt:27)
```

(Line 758 is past the end of the file: it is the inlined `single { }` of line 76.) The whole document fails.

## Minimal reproducer

`<scratchpad>/repro/discriminator-mapping-without-base.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/pets": { "get": { "responses": { "200": { "description": "ok", "content": { "application/json": {
    "schema": { "type": "array", "items": { "$ref": "#/components/schemas/Pet" } } } } } } } } },
  "components": { "schemas": {
    "Pet": {
      "type": "object",
      "required": ["petType"],
      "properties": { "petType": { "type": "string" }, "name": { "type": "string" } },
      "discriminator": { "propertyName": "petType",
        "mapping": { "cat": "#/components/schemas/Cat", "dog": "#/components/schemas/Dog" } }
    },
    "Cat": { "allOf": [{ "$ref": "#/components/schemas/Pet" },
      { "type": "object", "properties": { "huntingSkill": { "type": "string" } } }] },
    "Dog": { "allOf": [{ "$ref": "#/components/schemas/Pet" },
      { "type": "object", "properties": { "packSize": { "type": "integer" } } }] }
  } }
}
```

Observed:

```
worker exit=1 phases: parse pass, typed fail
java.util.NoSuchElementException: Collection contains no element matching the predicate.
	at io.github.nomisrev.openapi.transformers.DiscriminatedObjectKt.toDiscriminatedObject(DiscriminatedObject.kt:758)
```

`discriminator-mapping-with-base.json` adds `"pet": "#/components/schemas/Pet"` to the mapping and nothing else: it
generates `sealed interface Pet` with `Pet.Pet`, `Pet.Cat`, `Pet.Dog` and compiles.

## What the spec says

OAS 3.0.3 §4.7.25 Discriminator Object, and the "Models with Polymorphism Support" example of §4.7.24 Schema Object: `Pet` has a
discriminator, `Cat` and `Dog` are `allOf: [{$ref: Pet}, {...}]`, and the mapping (when present) lists the
*subtypes*. The base is not expected to map to itself. Listing it (as YouTrack does) is also legal and means "the
base is instantiable under this tag".

## Root cause (traced)

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/DiscriminatedObject.kt`:

- Line 76: `val parentClass = allOf.single { it.isSubtype(mapping) }` picks the subtype's parent member.
- Lines 401-402: `isSubtype(mapping)` is `this.ref in mapping.values`. A parent is only recognised if it is itself a
  mapping value. With `mapping = {cat: Cat, dog: Dog}`, `Cat.allOf = [$ref Pet, {...}]` has no member in the mapping,
  so `single` throws. The same predicate is used at line 151 for intermediate parents.
- Lines 404-411: `simpleCase` then demands `"Expected exactly one self mapping for discriminated object"`. So the
  self entry is required twice; line 76 just fails first.
- `registry/Predicates.kt:180-191` (`isObjectWithDiscriminator`) accepts the schema for this plugin without checking
  for a self entry, so the plugin takes a schema it cannot handle.

Verified by script over the census: exactly these 5 documents have a discriminator base (properties + mapping, all
mapped schemas `allOf` without `type`) whose subtypes' `allOf` has no member among the mapping values, and all 5 have
no self entry.

## Correct final API

Without a self entry the base is not instantiable under any tag; the subtypes are the only cases:

```kotlin
@JsonClassDiscriminator("petType")
@Serializable
public sealed interface Pet {
  public val name: String?

  @SerialName("cat") @Serializable
  public data class Cat(override val name: String? = null, public val huntingSkill: String? = null) : Pet

  @SerialName("dog") @Serializable
  public data class Dog(override val name: String? = null, public val packSize: Long? = null) : Pet
}
```

Wire: `{"petType":"cat","name":"Tom","huntingSkill":"lazy"}` decodes to `Pet.Cat`. A tag that is not in the mapping
(`"petType":"bird"`) is a decoding failure surfaced as a value, not a silently constructed base `Pet` — unless the
project wants an open "unknown subtype" case, which is a separate decision and should be the same one made for
`oneOf` unions.

## Fix slice

- typed, `DiscriminatedObject.kt:76` and `:151`: a subtype's parent is the `allOf` member that is the discriminated
  base (`reference`) or a schema in the mapping. Pass the base's `$ref` into `isSubtype`.
- typed, `DiscriminatedObject.kt:109-119` and `:404-411`: make the base case optional. `simpleCase` only when a self
  entry exists; otherwise `subtypes` are the mapped schemas only.
- Replace the remaining bare `single { }` / `!!` in this function (`schema.properties!!` line 67, `allOf.single`
  line 76) with errors that name the base, the subtype and the mapping key.
- Open question: OAS also allows the *implicit* mapping (no `mapping`, tag = schema name). `isObjectWithDiscriminator`
  requires a non-empty mapping, so those schemas take another path; not covered here.

## Proof

- Failing test first: typed test on the reproducer expecting `Model.DiscriminatedObject` with two subtypes and no
  base case.
- Golden `ClientSpec` with the output above; example round-trip for both tags and for an unknown tag.
- Integration tests would not catch a regression: `youtrack.json` has 20 discriminator bases and every one maps
  itself; `github.json` and `openai.yaml` have no base with `properties` + `mapping`.

## Affected documents

5 total. viator.com [44917e925b5be29d969b] (`SearchFreetextResponse`), twitter.com:current [4c9053fecb61e923cb65],
microsoft.com:graph [cb0f197c9832ed0801d0], rebilly.com [cbb9c24eff323ce81ded], just-eat.co.uk [f2467dc5e5257eff684a].
