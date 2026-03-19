M# ISSUE: Union Case Wrapper Value Types Ignore Inner Model Nullability

## Problem

When a union case wraps a nullable model (e.g., `{ "type": "string", "nullable": true }`), the generated value class wrapper holds a **non-nullable** value type. This is because `renderWrappedTypeSpec` uses `model.toTypeName(config)` which respects `isNullable`, but the union transformation pipeline may strip or ignore case-level nullability.

**Example OpenAPI:**
```json
"oneOf": [
  { "type": "string", "nullable": true },
  { "type": "integer" }
]
```

**Current generated:**
```kotlin
@JvmInline
value class CaseString(val value: String) : Union  // String is non-nullable
```

**Expected:**
```kotlin
@JvmInline
value class CaseString(val value: String?) : Union  // String? since the case is nullable
```

## Root Cause

In `Union.kt` line 46, when creating union cases, the individual case models may carry `isNullable = true` from the OpenAPI spec. The `renderWrappedTypeSpec` function in `UnionRenderer.kt` (line 609) calls `model.toTypeName(config)` which does apply `.copy(nullable = isNullable)` via `TypeMapping.kt:47`.

However, there's a design question: should individual case value types be nullable, or should the **union itself** be nullable (i.e., `Union?` rather than making each case's inner value nullable)?

## Design Consideration

There are two valid interpretations:
1. **Case-level nullability**: Each case's inner `value` is nullable (`String?`). The serializer for that case uses `.nullable`.
2. **Union-level nullability**: The union itself is nullable (`Union?`), and `null` is represented as a top-level null rather than a case wrapping null.

The current code inconsistently handles this — `toTypeName` would make the value nullable, but the serializer doesn't use `.nullable`.

## Affected Code

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRenderer.kt` line 609 (`renderWrappedTypeSpec`)
- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/Union.kt` line 46 (case model nullability)

## Fix

Decide on the correct semantic:
- If case-level: ensure both the value type AND serializer reflect nullability (this issue + ISSUE_UNION_CASE_SERIALIZER_MISSING_NULLABLE)
- If union-level: strip `isNullable` from individual case models during transformation and set it on the `Model.Union` instead. Then handle `null` at the union serializer level (return `null` before attempting case deserialization).

## Test Coverage Needed

- Union where individual cases have `nullable: true`
- Verify value class inner type matches serializer nullability
- Roundtrip serialization of `null` values through nullable union cases
