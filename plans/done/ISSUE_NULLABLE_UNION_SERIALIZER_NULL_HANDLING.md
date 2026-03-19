# ISSUE: Nullable Union (`Union?`) Serializer Doesn't Handle `null` JSON Values

## Problem

When a `Model.Union` has `isNullable = true` (from `nullable: true` on the oneOf/anyOf schema itself), the generated `KSerializer<Union>` does not handle `null` JSON input. The `deserialize` function always tries to decode a `JsonElement` and attempts case matching — it will throw on `null` instead of returning `null`.

Similarly, the `serialize` function has `when(value)` with exhaustive case matching but no handling for the possibility that the caller needs to serialize `null`.

**Example OpenAPI:**
```json
{
  "nullable": true,
  "oneOf": [
    { "type": "string" },
    { "type": "integer" }
  ]
}
```

**Generated serializer type:** `KSerializer<Union>` — but usage sites need `KSerializer<Union?>`.

When this union is used as a property type, `ObjectRenderer` correctly generates `Union?` for the property type. But the serializer annotation `@Serializable(with = Union.Serializer::class)` on the sealed interface doesn't account for nullability — the `Serializer` object implements `KSerializer<Union>`, not `KSerializer<Union?>`.

## Root Cause

The `buildSerializerObject` in `UnionRenderer.kt` (line 278) always creates `KSerializer<Union>` regardless of `isNullable`. There's no null-check at the top of `deserialize` and no null-encoding path in `serialize`.

When a property holds `Union?`, kotlinx.serialization needs the serializer to be wrapped with `.nullable` at the call site (e.g., `Union.Serializer.nullable`). The ObjectRenderer handles this for properties via `serializerCode()`, but the union's own `@Serializable(with = ...)` annotation points to the non-nullable serializer.

## Affected Code

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRenderer.kt`:
  - `buildSerializerObject()` line 278 — `KSerializer<Union>` should handle null when union is nullable
  - `buildDeserializeFunction()` line 334 — no null check before attempting case deserialization
  - `buildSerializeFunction()` line 383 — no null encoding path

## Fix Options

1. **Rely on `.nullable` wrapper at usage sites** (simpler): Keep `Serializer` as `KSerializer<Union>` (non-null). Ensure all usage sites (object properties, parameters, response types) wrap with `.nullable` when the model is nullable. This is how kotlinx.serialization idiomatically works.

2. **Generate null-aware serializer** (more complex): When `isNullable`, generate the serializer to handle null directly:
   ```kotlin
   override fun deserialize(decoder: Decoder): Union? {
       val element = decoder.decodeSerializableValue(JsonElement.serializer())
       if (element is JsonNull) return null
       // ... case matching
   }
   ```

Option 1 is preferred as it follows kotlinx.serialization conventions, but requires fixing all serializer reference sites (covered by ISSUE_UNION_CASE_SERIALIZER_MISSING_NULLABLE).

## Test Coverage Needed

- Nullable union as object property — deserialize `null` JSON value
- Nullable union as object property — serialize `null` Kotlin value
- Non-nullable union — verify `null` input throws appropriately
- Nullable union as standalone response type
