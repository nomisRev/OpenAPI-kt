# An inline object property of a discriminated base is typed as the base's class but generated inside its case

**Status:** open, latent: no corpus document reaches it since `73cb10c5`
**Confirmed:** 2026-11-16, re-checking `discriminator-schema-mapped-under-several-tags` (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 0 today. Seen in apple.com:sirikit-cloud-media [72528bf58a657172e8f0] while its handler schemas were bases of
their own (6 compile errors, in 2 files); since `73cb10c5` they are cases of `Invocation` and the document compiles.
Not counted over the corpus; the shape is a base that lists itself in its mapping and declares an inline object
property.
**Source spec valid:** yes

## Symptom

```
model/AddMediaIntentHandlingInvocation.kt:16:55: error: unresolved reference 'Params'.
model/AddMediaIntentHandlingInvocation.kt:24:26: error: serializer was not found for type '??? (Unresolved qualified name: AddMediaIntentHandlingInvocation.Params)'.
```

`AddMediaIntentHandlingInvocation` declares `params: {type: object, properties: {intent: ...}}` and maps itself (under
four tags). The base's abstract property and its case's override are typed `AddMediaIntentHandlingInvocation.Params`,
but the class is generated nested in the case, `AddMediaIntentHandlingInvocation.Default.Params`:

```kotlin
public sealed interface AddMediaIntentHandlingInvocation {
  public val params: AddMediaIntentHandlingInvocation.Params      // no such class
  public data class Default(
    public val method: Method,
    override val params: AddMediaIntentHandlingInvocation.Params,
  ) : AddMediaIntentHandlingInvocation {
    @Serializable public data class Params(public val intent: AddMediaIntent)   // generated here
    ...
```

## Root cause (traced, not reduced)

typed `DiscriminatedObject.kt` `selfCase` reuses the base's `abstractProperties`, whose inline models are named under
the base (`AddMediaIntentHandlingInvocation.params`). The renderer renders a case's inline models nested in the case,
and renders none in the sealed interface, so the class lands in `Default`. The golden
`discriminated/nested-properties` has inline properties on subtypes only.

## Fix slice (to propose)

Render the inline models of the base's abstract properties nested in the sealed interface, where their names point,
and not again inside the case; or name them under the case. The first keeps one class shared by every case that
inherits the property.

## Proof

- Golden: a base with an inline object property, mapping itself and a subtype; it must compile.
- Re-check apple.com:sirikit-cloud-media with `--filter sirikit`.
