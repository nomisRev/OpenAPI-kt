# ISSUE: Duplicated `serializerCode` Between ObjectRenderer and UnionRenderer

## Problem

There are two independent `Model.serializerCode()` implementations:

1. **`ObjectRenderer.kt` lines 408–439**: Correctly handles `.nullable` wrapping via `nonNullableSerializerCode()` + conditional `.nullable`.
2. **`UnionRenderer.kt` lines 725–753**: Duplicate implementation that **lacks** `.nullable` handling entirely.

These two implementations are almost identical in their type-to-serializer mapping but diverge on:
- Nullability handling (ObjectRenderer has it, UnionRenderer doesn't)
- Parameter lists (UnionRenderer takes extra args for className remapping)

This duplication is the root cause of the nullability bug — the ObjectRenderer was correctly updated but the UnionRenderer copy was not.

## Why This Matters

1. Any future serializer-related fix applied to one copy may be missed in the other
2. The UnionRenderer version also handles `externalTypeNames` remapping which ObjectRenderer doesn't, suggesting they may diverge further
3. This is a maintenance hazard that will keep producing bugs

## Affected Code

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ObjectRenderer.kt` lines 408–439
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRenderer.kt` lines 725–753

## Fix

Extract a shared `Model.nonNullableSerializerCode(config, originalClassName?, className?, externalTypeNames?)` function that both renderers can use, with the nullable wrapping applied consistently. The className remapping parameters can be optional (defaulting to null/no remapping for ObjectRenderer's use case).
