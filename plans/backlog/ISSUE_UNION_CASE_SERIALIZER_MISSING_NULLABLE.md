# ISSUE: Union Case Serializers Missing `.nullable` for Nullable Cases

## Problem

When individual union cases have `nullable: true` in the OpenAPI spec, the generated serializers inside the union's custom `Serializer` object do **not** wrap the serializer with `.nullable`. This causes runtime serialization failures when the JSON value is `null` for a nullable case.

**Example OpenAPI:**
```json
"oneOf": [
  { "type": "string", "nullable": true },
  { "type": "integer", "nullable": true },
  { "type": "array", "nullable": true, "items": { "type": "string" } }
]
```

**Generated (incorrect):**
```kotlin
is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
```

**Expected:**
```kotlin
is CaseString -> encoder.encodeSerializableValue(String.serializer().nullable, value.value)
is CaseLong -> encoder.encodeSerializableValue(Long.serializer().nullable, value.value)
is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()).nullable, value.value)
```

## Root Cause

In `UnionRenderer.kt`, the `Model.serializerCode()` function (lines 725–753) generates serializer expressions but **never checks `isNullable`** on any model. Compare with `ObjectRenderer.kt` (lines 408–411) which correctly wraps with `.nullable`:

```kotlin
// ObjectRenderer.kt — correct
private fun Model.serializerCode(config: RenderConfig): CodeBlock {
    val nonNullable = nonNullableSerializerCode(config)
    return if (isNullable) CodeBlock.of("%L.%M", nonNullable, NullableMember) else nonNullable
}

// UnionRenderer.kt — missing nullability
private fun Model.serializerCode(...): CodeBlock = when (this) {
    is Model.Primitive.String -> CodeBlock.of("%T.%M()", kotlin.String::class, SerializerMember)
    // ... no .nullable wrapping anywhere
}
```

## Affected Code

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRenderer.kt` lines 725–753 (`Model.serializerCode`)
- This affects all three serializer generation sites in the union: `descriptor`, `deserialize`, and `serialize` functions.

## Fix

Add `.nullable` wrapping to the union's `serializerCode`, mirroring what `ObjectRenderer` does. Either:

1. **Refactor**: Extract the non-nullable serializer code, then wrap with `.nullable` when `case.model.isNullable`:
   ```kotlin
   private fun Model.serializerCode(...): CodeBlock {
       val nonNullable = nonNullableSerializerCode(...)
       return if (isNullable) CodeBlock.of("%L.%M", nonNullable, NullableMember) else nonNullable
   }
   ```

2. **Consolidate**: Consider sharing the `serializerCode` logic between `ObjectRenderer` and `UnionRenderer` to prevent future drift.

## Test Coverage Needed

- Union with all cases having `nullable: true`
- Union with mixed nullable and non-nullable cases
- Union with nullable collection cases (`List<String>?`)
- Union with nullable reference/object cases
