# ISSUE: Union SerialDescriptor Ignores Case-Level Nullability

## Problem

The `buildDescriptorProperty` in `UnionRenderer.kt` builds element descriptors for each union case using the same `caseSerializerCode` that generates non-nullable serializers. When a case is nullable, the descriptor should reflect this (using the nullable serializer's descriptor), but it currently doesn't.

**Generated (incorrect):**
```kotlin
override val descriptor: SerialDescriptor =
    buildSerialDescriptor("...", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)  // should be String.serializer().nullable.descriptor
    }
```

## Root Cause

`buildDescriptorProperty` at `UnionRenderer.kt:296` calls `caseSerializerCode(...)` which delegates to `Model.serializerCode(...)` (line 725). Since `serializerCode` never applies `.nullable`, the descriptor elements are always non-nullable.

While descriptor mismatches may not always cause runtime errors (kotlinx.serialization is somewhat lenient), they can cause:
- Incorrect schema generation if the descriptor is inspected
- Misleading debug output
- Potential issues with format-specific encoders that rely on descriptor nullability

## Affected Code

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRenderer.kt`:
  - `buildDescriptorProperty()` line 296
  - `caseSerializerCode()` line 713
  - `Model.serializerCode()` line 725

## Fix

This is automatically fixed by fixing ISSUE_UNION_CASE_SERIALIZER_MISSING_NULLABLE, since the descriptor uses `caseSerializerCode(...).descriptor` and that code will include `.nullable` when appropriate.

## Test Coverage Needed

- Verify descriptor elements match actual serializer nullability
- This is implicitly covered by fixing the serializer code generation
