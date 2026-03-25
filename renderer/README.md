# OpenAPI Kotlin Renderer

JVM-only renderer that turns the transformed `ApiTree` / `Model` graph into KotlinPoet source files.

The main entry points live in [`Generate.kt`](./src/jvmMain/kotlin/io/github/nomisrev/openapi/Generate.kt):

- `OpenAPI.generate(...)`
- `OpenAPI.generateWithDiagnostics(...)`
- `ApiTree.render(...)`
- `ApiTree.generateClient(...)`

The renderer does not work directly on raw OpenAPI JSON shapes. It renders the normalized model graph produced by the parser + typed layers. That matters for a few cases:

- read/write splits can produce `Read` / `Write` model variants
- discriminated `allOf` inheritance arrives as `Model.DiscriminatedObject`
- named top-level collections are already normalized into a single-field wrapper model

## Rendering Pipeline

1. Parse OpenAPI into `ApiTree`.
2. Filter request-body variants that are unsupported for code generation.
3. Generate model files from `ApiTree.models`.
4. Generate client files from the route tree.
5. Emit shared serialization helpers when unions or `additionalProperties` serializers need them.

## Decision Tree

```text
OpenAPI schema (after normalization into renderer models)
├─ Normalized as `Model.Union` (`oneOf` / `anyOf`)?
│  ├─ Has discriminator?
│  │  └─ Render a sealed interface with nested cases
│  │     ├─ Inline object / enum / unit case -> nested subtype directly
│  │     └─ Primitive / reference / top-level object / collection / union case
│  │        -> nested wrapper value class around `value`
│  └─ No discriminator
│     ├─ Exactly `enum + string`?
│     │  └─ Render the "open enum" pattern:
│     │     sealed interface + enum subtype + raw string value class + custom serializer
│     └─ Otherwise
│        └─ Render a sealed interface + custom serializer
│           ├─ `anyOf` with object-only cases and unique keys
│           │  -> dispatch by key presence first
│           └─ fallback
│              -> try cases in deserialization-priority order
├─ Normalized as `Model.DiscriminatedObject`?
│  └─ Render a sealed interface for the base type
│     ├─ shared properties present on every subtype with the same rendered type
│     │  -> abstract properties on the interface
│     └─ each subtype
│        -> nested object renderer output (`data class`, `value class`, or `data object`)
├─ Normalized as `Model.Enum`?
│  └─ Render `enum class`
│     ├─ raw wire values that are not valid Kotlin identifiers
│     │  -> keep `@SerialName`
│     └─ if any wire value differs from the Kotlin entry name
│        -> add `value: String` to every enum entry
├─ Normalized as `Model.Object`?
│  └─ Render an object model
│     ├─ no remaining properties, and the schema never had properties stripped
│     │  -> `data object`
│     ├─ no remaining properties, but read/write filtering stripped them away
│     │  -> plain `class()`
│     ├─ exactly one property, no `additionalProperties`, and not a stripped variant
│     │  -> `@JvmInline value class`
│     └─ otherwise
│        -> `data class`
│     + `additionalProperties: true`
│       -> `additional: JsonObject?` + custom serializer
│     + `additionalProperties: <schema>`
│       -> `additional: Map<String, T>?` + custom serializer
├─ Normalized as `Model.Collection`?
│  ├─ Inline position (property / parameter / response body)
│  │  -> `List<T>`
│  │  -> `JsonArray` when `items` is free-form JSON
│  └─ Named top-level collection schema
│     -> already normalized into a single-field wrapper object
│     -> renders like a one-property object, usually `@JvmInline value class Name(val items: List<T>)`
└─ Leaf scalar / special model
   ├─ `string` + `format: date` -> `LocalDate`
   ├─ `string` + `format: date-time` -> `Instant`
   ├─ `string` + `format: uuid` -> `Uuid`
   ├─ `string` + binary/byte format -> `ByteArray`
   ├─ plain `string` -> `String`
   ├─ `integer` / `number` / `boolean` -> Kotlin primitive
   ├─ free-form schema `{}` -> `JsonElement`
   ├─ reference -> generated class name for that context
   └─ empty-body / explicit unit model -> `Unit`
```

## Object Rules

- Property names are converted to Kotlin parameter names. When the wire name changes, the renderer adds `@SerialName`.
- Optional properties render as nullable constructor parameters with `= null`.
- Required nullable properties stay nullable without a default, except when an OpenAPI default exists; then the constructor still defaults to `null` so callers can omit the field.
- Descriptions become KDoc.
- Nested inline object, enum, and union models are emitted as nested Kotlin types inside the owning class/interface.
- When the same component name exists in both read and write contexts, the renderer appends `Read` / `Write` to the generated class name.

## Union Deserialization Order

Non-discriminated unions are intentionally not tried in declaration order. The serializer prefers more specific shapes first:

1. Objects without `additionalProperties`, with higher-property-count objects first
2. Objects with typed `additionalProperties`
3. Objects with free-form `additionalProperties`
4. Discriminated objects
5. Nested unions
6. Enums
7. Collections
8. References
9. Primitive numeric / boolean / unit cases
10. `Uuid`, `LocalDate`, `Instant`, `ByteArray`
11. Plain `String`
12. Free-form JSON

The implementation lives in:

- [`UnionRenderer.kt`](./src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRenderer.kt)
- [`RenderedUnionCase.kt`](./src/jvmMain/kotlin/io/github/nomisrev/openapi/RenderedUnionCase.kt)
- [`UnionRendererSerialization.kt`](./src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRendererSerialization.kt)
- [`AnyOfUniqueKeyDispatch.kt`](./src/jvmMain/kotlin/io/github/nomisrev/openapi/AnyOfUniqueKeyDispatch.kt)

## Review Findings

### Resolved

#### Response collection item sharing only considered the first response model

Affected code:

- [`OperationInlineModelScope.kt`](./src/jvmMain/kotlin/io/github/nomisrev/openapi/OperationInlineModelScope.kt)
- [`ClientRendererResponses.kt`](./src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererResponses.kt)

What was fixed:

- Response collection item candidates are now collected from all response variants, not just the first preferred model.
- Response collection remapping now uses the collection item structural key, so distinct inline item schemas that originally shared the same source `ClassName` no longer overwrite each other.
- Direct `SeparateMethods` return types now pick the model for the requested content type instead of always using the first preferred response model.

Regression coverage:

- [`ClientSpec.kt`](./src/jvmTest/kotlin/io/github/nomisrev/render/ClientSpec.kt) now covers:
  - `client/response-inline-collection-items-multiple-statuses`
  - `client/response-inline-collection-items-separate-methods`

### Open

#### Flattened request-body overload limiting can overflow before the cap is checked

Affected code:

- [`ClientRendererRequestBody.kt`](./src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererRequestBody.kt)

Problem:

- The overload count is accumulated by multiplying option counts before checking the configured maximum.
- The accumulator is effectively computed with `Int` arithmetic even though the cap is expressed as `Long`.
- With enough required primitive-union fields, the multiplication can overflow before the `> maxOverloads` guard runs.

Observed failure mode:

- Large primitive-union combinations can wrap the count to `0` or a negative value.
- That bypasses the intended `32` overload limit.
- The renderer can then materialize an unexpectedly large Cartesian product of overloads.

Why it matters:

- This can turn a defensive limit into a generator-time blow-up.
- The failure mode is input-size dependent and may only appear on larger specs, which makes it easy to miss in normal tests.

## Key Files

- [`TypeMapping.kt`](./src/jvmMain/kotlin/io/github/nomisrev/openapi/TypeMapping.kt): leaf model -> Kotlin `TypeName`
- [`ObjectRenderer.kt`](./src/jvmMain/kotlin/io/github/nomisrev/openapi/ObjectRenderer.kt): object, value-class, additional-properties rendering
- [`EnumRenderer.kt`](./src/jvmMain/kotlin/io/github/nomisrev/openapi/EnumRenderer.kt): enum rendering
- [`DiscriminatedObjectRenderer.kt`](./src/jvmMain/kotlin/io/github/nomisrev/openapi/DiscriminatedObjectRenderer.kt): discriminated inheritance rendering
- [`ClientRendererInlineModels.kt`](./src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererInlineModels.kt) and [`OperationInlineModelScope.kt`](./src/jvmMain/kotlin/io/github/nomisrev/openapi/OperationInlineModelScope.kt): inline operation/body/response models
- [`SerializationUtilsRenderer.kt`](./src/jvmMain/kotlin/io/github/nomisrev/openapi/SerializationUtilsRenderer.kt): shared helpers for union dispatch and custom serializers
