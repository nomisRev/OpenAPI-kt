# Phase 3 — Objects with Additional Properties

Extend Object rendering to handle `additionalProperties` with custom serializer generation.

## Tasks

- [x] Extend `Model.Object.toTypeSpec()` for additionalProperties:
  - `additionalProperties: true` → add `val additional: JsonObject? = null` property
  - `additionalProperties: { schema }` → add `val additional: Map<String, T>? = null` property
- [x] Generate custom `object Serializer : KSerializer<Foo>` inside class body:
  - Add `@KeepGeneratedSerializer` annotation on the class
  - Add `@Serializable(with = Foo.Serializer::class)` annotation
  - Add `@OptIn(ExperimentalSerializationApi::class)` annotation
  - Serializer implements `KSerializer<Foo>` with:
    - `descriptor` from generated serializer
    - `serialize()`: encode known properties, merge additional properties into JSON object
    - `deserialize()`: extract known property keys, treat remaining as additional properties
- [x] Handle interaction with value class (1 prop + additionalProperties → data class, not value class)
- [x] Handle nested types inside additionalProperties schema

## Golden Tests

- [x] `object/additional-boolean` — `additionalProperties: true` → `JsonObject?`
- [x] `object/additional-schema` — `additionalProperties: { type: string }` → `Map<String, String>?`
- [x] `object/additional-complex` — additionalProperties with complex inner schema
- [x] `object/additional-serializer` — full custom serializer output verification

## Files to Create/Modify

- **Modify**: `renderer/.../ObjectRenderer.kt` — extend for additionalProperties + serializer generation
- **Create**: golden test resource files under `renderer/src/test/resources/kotlinTestData/object/`
- **Modify**: `renderer/.../ObjectSpec.kt` — add test cases

## Key Decisions

- The custom serializer pattern follows MODEL_SPEC exactly: `KeepGeneratedSerializer` + `Serializer` companion object
- Additional properties are always optional (`? = null`) since they represent "extra" fields
- When `additionalProperties` is present, a single-property object becomes `data class` (not `value class`) because it needs the additional property too
