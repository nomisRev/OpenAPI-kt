# A discriminator without `mapping` on an `allOf` base generates no hierarchy

**Status:** open, not reproduced yet (found by reading the code and a corpus script, 2026-11-16)
**Stage:** typed (silently: no error)
**Impact:** about 11 bases in 8 documents (rough count: a base with `properties` and a `discriminator` without
`mapping`, no `oneOf`/`anyOf`, and component schemas that `allOf` it), e.g. `object` with 68 subtypes in
[5710cf787f18…], `report_request` (9) in [08b5ce21c4b3…], `DirectoryObject` (5) in [a0c6d8ff2d05…],
`channelCatalogMarketplaceProperty` and `channelCatalogMarketplaceSetting` (4 each) in [6d8bcb5b2ea8…]
**Source spec valid:** yes — the implicit mapping: the tag is the schema name of a schema that extends the base

## What happens (by reading)

`registry/Predicates.kt` `isObjectWithDiscriminator` requires a non-empty `mapping`, so `DiscriminatedObjectPlugin`
does not take the base, and `discriminatedSubtypeOrNull` finds no discriminated ancestor for its subtypes. The base is
then an ordinary object and each subtype an ordinary `allOf` merge: no sealed interface, and a response typed as the
base decodes every subtype as the base, dropping its own properties.

## Correct final API

The same hierarchy as with a mapping, every case under its schema name: `sealed interface Object` with `@SerialName`
of each subtype's component name. `e1cbf0e7` already adds the implicit cases next to an explicit mapping; the fix is to
accept an absent mapping in the predicate (with the same checks on the subtypes) and to treat it as empty.

## Proof

Reproduce first (typed spec and a golden of the reproducer), then the five documents above with `--filter`.
