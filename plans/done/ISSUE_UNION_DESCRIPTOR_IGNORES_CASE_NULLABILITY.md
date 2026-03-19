# RESOLVED: Union SerialDescriptor Respects Case-Level Nullability

## Outcome

Union case descriptors now stay aligned with the serializer nullability used by the union serializer itself. Nullable cases emit nullable element descriptors.

**Generated (correct):**
```kotlin
override val descriptor: SerialDescriptor =
    buildSerialDescriptor("...", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().nullable.descriptor)
    }
```

## Implementation

`buildDescriptorProperty` in `UnionRenderer.kt` still builds descriptor elements from `caseSerializerCode(...).descriptor`, but the serializer code path it calls is now nullability-aware:

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRenderer.kt`
  - `buildDescriptorProperty()` uses `caseSerializerCode(...).descriptor`
  - `caseSerializerCode()` delegates wrapped cases to `case.model.serializerCode(...)`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ObjectRenderer.kt`
  - `Model.serializerCode(...)` wraps `nonNullableSerializerCode(...)` with `.nullable` when `isNullable` is true

That shared serializer generation keeps the descriptor, deserialize, and serialize paths consistent.

## Coverage

The renderer suite already covers this with the nullable union golden:

- `renderer/src/jvmTest/kotlin/io/github/nomisrev/render/UnionSpec.kt`
  - `modelTest(..., "union/nullable-cases")`
- `renderer/src/jvmTest/resources/kotlinTestData/union/nullable-cases/Union.kt`
  - `CaseString` uses `String.serializer().nullable.descriptor`
  - `CaseStrings` uses `ListSerializer(String.serializer()).nullable.descriptor`

## Validation

Verified with:

```bash
./gradlew :renderer:jvmTest --tests io.github.nomisrev.render.UnionSpec --rerun-tasks
```
