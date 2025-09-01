# Proposal: Add proper discriminator support to typed module

This proposal outlines minimal changes to the `typed` module to correctly support OpenAPI `discriminator` for unions (oneOf/anyOf), in order to enable accurate code generation (e.g., using kotlinx.serialization `@JsonDiscriminator`).

## Current Behavior
- `parser` already parses `Schema.Discriminator(propertyName, mapping)`.
- `typed.OpenAPITransformer`:
  - Carries `discriminator` during `allOf` merge, but that information is not preserved in the produced `Model`.
  - Builds unions via `Schema.toUnion(...)` and determines union case names in `toUnionCaseContext`. Naming for inline object cases only looks for specific property keys `"event"` or `"type"` and uses their single enum value for naming.
  - There is no general support for arbitrary `discriminator.propertyName`.
- `typed.Model.Union` does not expose any discriminator data, making it impossible for the generation module to emit `@JsonDiscriminator` or to handle `mapping`.

## Desired Behavior
1. Given a union schema (`oneOf`/`anyOf`) with `discriminator.propertyName = X`, inline object cases that contain a property `X` with a single enum value should be named after that enum value (capitalized), consistent with current behavior for special-case `event`/`type`.
2. The typed model should expose discriminator metadata (propertyName, and mapping if provided) so code generation can:
   - Annotate the sealed interface/base with `@JsonClassDiscriminator(propertyName)` or equivalent (`kotlinx.serialization` -> `@JsonDiscriminator("X")`).
   - Annotate each subtype with the correct serial name, using either the enum value or the `discriminator.mapping` value.
3. Continue to gracefully fall back to the existing naming heuristics when discriminator is absent or cannot be applied.

## Proposed Minimal Changes

1. Extend the typed model to carry discriminator info:
   - In `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/Model.kt` adjust `Model.Union`:
     ```kotlin
     interface Model {
       // ...
       data class Union(
         val context: NamingContext,
         val cases: List<Case>,
         val default: String?,
         override val description: String?,
         val inline: List<Model>,
         val discriminator: Discriminator? // NEW
       ) : Model {
         data class Case(val context: NamingContext, val model: Model)
       }

       data class Discriminator(
         val propertyName: String,
         val mapping: Map<String, String>? // raw mapping values from OpenAPI (value -> $ref)
       )
     }
     ```
     - This mirrors `Schema.Discriminator` but attaches it to `Model.Union` for downstream use.

2. Thread the discriminator through the transformer:
   - In `OpenAPITransformer.toUnion(...)`, pass the top-level `schema.discriminator` into `Model.Union` as the new field.
   - Update the call sites that construct `Model.Union` (also in array-of-types case) to set `discriminator = null` where not applicable.

3. Use `discriminator.propertyName` for case naming:
   - Change `toUnionCaseContext(...)` to accept the preferred property name, e.g.:
     ```kotlin
     private fun toUnionCaseContext(
       context: NamingContext,
       case: Resolved<Schema>,
       index: Int,
       discriminatorProperty: String?
     ): NamingContext
     ```
   - If `discriminatorProperty != null` and `case` is an inline object (non-ref) with property `discriminatorProperty` whose schema is a string enum with single value `V`, use `Named(V.replaceFirstChar(Char::uppercaseChar))` nested within `context`.
   - Else, fallback to current special-cases for `event`/`type`.
   - Else, fallback to existing generic naming (`<Base>Case<idx>` etc.).

4. Preserve `allOf` behavior:
   - The existing merge logic already picks the first available `discriminator` into the merged schema. Ensure the merged schema’s discriminator is also considered if such a merged schema is used to form a union (no extra change likely needed once (2) and (3) are in place).

5. No changes to parser needed.

## Tests (already added, currently failing)
New tests in `typed/src/commonTest/kotlin/io/github/nomisrev/openapi/DiscriminatorSpec.kt`:
- `oneOf_inline_objects_use_discriminator_property_for_case_naming`
- `anyOf_inline_objects_use_discriminator_property_for_case_naming`

Both construct unions with `discriminator.propertyName = "objectType"` and inline object cases that contain `objectType` as a single-value enum. Expected behavior is that the union case naming uses the enum values (e.g., `Simple`, `Complex`) instead of falling back to `Case1`/`Case2`. These tests reflect the desired generalized behavior (not limited to `event`/`type`).

Given current implementation, these tests will fail because `toUnionCaseContext` only checks for the hardcoded keys `event`/`type` and does not consult `Schema.discriminator`.

## Edge Cases and Notes
- If a discriminator is present but the targeted property is missing on a case, or its schema is not a single-value enum, keep current fallback naming.
- Mapping:
  - `discriminator.mapping` maps discriminator values to schemas. Exposing `mapping` alongside `propertyName` in the typed model allows the codegen module to annotate subtypes with `@SerialName` based on mapped values. The typed module does not need to resolve or reorder union cases based on mapping; it only needs to expose the raw mapping.
- Backwards compatibility:
  - Adding a new optional field to `Model.Union` is source-compatible for existing tests/consumers if defaulted to `null`.

## Summary
Implementing the above minimal changes will:
- Generalize union case naming to any discriminator property name.
- Expose discriminator metadata to enable correct serialization annotations in code generation.
- Keep existing behaviors as fallback.

Once approved, I will implement these changes and ensure all tests, including the new discriminator tests, pass.
