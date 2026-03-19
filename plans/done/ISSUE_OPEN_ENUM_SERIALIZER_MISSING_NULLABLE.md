# ISSUE: Open Enum Union Serializer Ignores Nullability

## Problem

The open enum pattern (union of one enum + one string) generates a custom serializer via `buildOpenEnumSerializer` that uses `encoder.encodeString()` / `decoder.decodeString()` directly. When the open enum union or its cases are nullable, there is no null handling:

1. The serializer descriptor uses `String.serializer().descriptor` — not `.nullable.descriptor` even if nullable
2. `serialize` uses `encoder.encodeString(...)` which cannot encode `null`
3. `deserialize` uses `decoder.decodeString()` which cannot decode `null`

**Example OpenAPI:**
```json
{
  "nullable": true,
  "oneOf": [
    { "type": "string", "enum": ["active", "inactive"] },
    { "type": "string" }
  ]
}
```

## Root Cause

`buildOpenEnumSerializer` in `UnionRenderer.kt` (line 493) hardcodes `String.serializer()` for the descriptor and uses `encodeString`/`decodeString` directly without any null check or nullable serializer wrapping.

The open enum path is a completely separate code path from the generic union serializer, so any fix to the generic path won't help here.

## Affected Code

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRenderer.kt`:
  - `buildOpenEnumSerializer()` line 493
  - Descriptor: line 535
  - Serialize: lines 503–514
  - Deserialize: lines 516–528

## Fix

When the union `isNullable`:
1. Use `String.serializer().nullable.descriptor` for the descriptor
2. In `serialize`: check for null before encoding, use `encoder.encodeNull()` or similar
3. In `deserialize`: attempt to decode null first, return null if present

Alternatively, keep the serializer non-null and rely on `.nullable` wrapping at usage sites (consistent with the approach for non-discriminated unions).

## Test Coverage Needed

- Nullable open enum union — serialize and deserialize null
- Non-nullable open enum — verify null throws
- Open enum with nullable individual cases
