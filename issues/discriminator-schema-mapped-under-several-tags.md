# A schema that its discriminator mapping selects under several tags crashes or is generated once per tag

**Status:** fixed in `73cb10c5` (option A, decided 2026-11-16). A schema the mapping selects under several tags is
one case, named by its schema (the base's own case stays `Default`), whose discriminator property is an enum of its
tags; `TaggedCaseSerializer` takes the set of tags, checks the property and leaves it in place, and the base selects the
case for each of them. A schema with a discriminator of its own that extends a discriminated base is a case of that
base. Fixed with it, as both kept sirikit's bases from being hierarchies: a mapped subtype that declares `type: object`
next to its `allOf` made `isObjectWithDiscriminator` refuse the whole base (sirikit's `Intent`,
`IntentResolutionResult`, `IntentResponse`, `Invocation`; rebilly.com's `Subscription`); and a base whose cases alone
are context-specific was named once for both contexts and generated twice under one name (`IntentResolutionResult`,
whose cases refer to `MediaItem`; proven by the `client/discriminated-base-context` golden and its runtime test, which
without the fix generate one `Result` for both directions and a third `Item` class whose `readOnly` `id` can be sent).
Re-checked with `--filter`: apple.com:sirikit-cloud-media compiles (60 files;
`Invocation` is a sealed interface of its 13 handler cases, each with a `method` enum of its tags); viator.com and
just-eat.co.uk still compile; twitter.com:current fails only on its 9 known errors; rebilly.com still stops on
`allof-property-two-refs-rejected`.
**Confirmed:** 2026-11-16, re-checking `discriminated-subtype-serializer-without-tag` (branch openapi-3.2-streaming)
**Stage:** typed
**Impact:** 1 of 1,547 documents: apple.com:sirikit-cloud-media [72528bf58a657172e8f0]. It got through typed in the 17th
census and failed to compile on unresolved subtype references (fixed by `c291f8a5`); since `e1cbf0e7` / `c291f8a5` it
reaches this case and stops in typed, earlier than before. Not a supported document lost.
**Source spec valid:** yes. A mapping is a map from tag to schema; nothing requires the schemas to be distinct.

## Symptom

```
java.lang.IllegalStateException: Expected exactly one self mapping for discriminated object
	at io.github.nomisrev.openapi.transformers.DiscriminatedObjectKt.simpleCase(DiscriminatedObject.kt:545)
	at io.github.nomisrev.openapi.transformers.DiscriminatedObjectKt.toDiscriminatedObject(DiscriminatedObject.kt:124)
```

## Shapes

sirikit describes a JSON-RPC style `method` with a schema per handler, each selected by several method names:

```json
"Invocation": {
  "properties": { "method": { "type": "string" }, "params": {}, "session": {} },
  "discriminator": { "propertyName": "method", "mapping": {
    "AddMediaIntentHandling.confirm": "#/components/schemas/AddMediaIntentHandlingInvocation",
    "AddMediaIntentHandling.handle":  "#/components/schemas/AddMediaIntentHandlingInvocation", ... } }
},
"AddMediaIntentHandlingInvocation": {
  "allOf": [{ "$ref": "#/components/schemas/Invocation" }],
  "properties": { "method": { "type": "string", "enum": ["AddMediaIntentHandling.confirm", "AddMediaIntentHandling.handle", ...] } },
  "discriminator": { "propertyName": "method", "mapping": { "AddMediaIntentHandling.confirm": "#/components/schemas/AddMediaIntentHandlingInvocation", ... } }
}
```

Checked with a typed test on a minimal document (`Pet` with `properties` and a `discriminator` on `type`):

| Mapping | Result |
| --- | --- |
| `{"pet": Pet, "animal": Pet}` (the base under two tags) | throws the error above: `simpleCase` takes the single self entry |
| `{"cat": Cat, "kitten": Cat}`, `Cat` is `allOf: [Pet]` | two cases, `cat` and `kitten`, the same schema generated twice; a `$ref` to `Cat` is the `cat` case, so a response typed `Cat` fails on a `kitten` body |

## Correct final API (to decide)

One case per schema, which accepts each of its tags and keeps the tag as a property, so a value is written with the
tag it was read with:

```kotlin
public data class AddMediaIntentHandlingInvocation(
  public val method: Method,   // the enum of its tags
  public val params: Params,
  ...
) : Invocation
```

Its serializer requires one of its tags instead of one tag (`TaggedCaseSerializer` takes a set and does not add the
tag, the property writes it), and the base selects it for each of them. The alternative, a case per tag, keeps one
tag per class but duplicates the schema and has no single type for a `$ref` to it.

## Proof

- typed: both shapes above, expecting one case with its tags.
- renderer golden and runtime test: decode each tag to the one case, encode it back with that tag.
- Re-check apple.com:sirikit-cloud-media with `--filter sirikit`.
