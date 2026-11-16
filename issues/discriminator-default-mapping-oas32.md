# OAS 3.2 `discriminator.defaultMapping` is not read

**Status:** open (feature; found 2026-11-16 while fixing `discriminator-mapping-without-base-entry`)
**Stage:** parse (the keyword is not modelled)
**Impact:** 0 corpus documents (none is 3.2); this branch targets OpenAPI 3.2
**Source spec valid:** yes

## What the spec says

OAS 3.0 and 3.1: when the tag "does not match an implicit or explicit mapping, no schema can be determined and
validation SHOULD fail". OAS 3.2 adds `defaultMapping` to the Discriminator Object: the schema to use when the
discriminating property is absent or its value is not mapped (and with it the property may be optional).

## What happens

The parser has no `defaultMapping` (no match in `parser/src/commonMain`). A tag that no case selects fails to decode,
which is right for 3.0/3.1 and for a 3.2 document without `defaultMapping`
(`DiscriminatorWithoutBaseRuntimeTest.aTagTheMappingDoesNotSelectFailsToDecode`).

## Decision needed

How the default case is generated, for discriminated objects and for `oneOf` unions alike: a case decoded for any
other tag (keeping the tag it was read with, so it re-encodes unchanged), and how that fits the deterministic union
deserializer design (`design-deterministic-union-deserializers`).
