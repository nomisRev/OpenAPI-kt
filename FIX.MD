# Proposal: Represent `uniqueItems: true` as `List` + Validation (not `Set`)

## Background
The OpenAPI Array specification states that `uniqueItems: true` means “all items in the array must be unique.” It does not prescribe a language-level set type, and it does not imply a change of the collection’s type semantics. The core requirement is a validation constraint, not a type mapping.

Current typed transformation maps `uniqueItems: true` to `Model.Collection.Set` in `OpenAPITransformer.collection`. This changes type semantics by:
- Potentially reordering elements (sets do not guarantee order),
- Silently removing duplicates (when converted to a set),
- Hiding an important validation concern in the type rather than exposing it as a constraint.

This diverges from the spec’s intent and from typical codegen expectations: arrays remain arrays (Lists), and `uniqueItems` is enforced as a validation rule.

## Observed Behavior
- `OpenAPITransformer.collection` returns `Model.Collection.Set` when `schema.uniqueItems == true`.
- Constraints for collections (`Constraints.Collection`) currently only include `minItems` and `maxItems`, but no `uniqueItems` flag.
- Existing tests such as `ModelTest.uniqueItemsIsSet`, `ConstraintTest.set`, and parts of `UnionNameGenerationSpec` are written to expect `Set`.

## Desired Behavior
- Always represent OpenAPI arrays as `Model.Collection.List`, regardless of `uniqueItems`.
- Enforce `uniqueItems` via a validation constraint (carried in `Constraints.Collection`, used by generators/validators), not by changing the type.
- Preserve ordering and expressiveness of arrays, while still guaranteeing uniqueness at runtime/validation/generation layers.

## Proposed Changes

### 1. typed module changes
1. Extend `Constraints.Collection` to include a uniqueness flag:
   ```kotlin
   data class Collection(
     val minItems: Int,
     val maxItems: Int,
     val uniqueItems: Boolean // or Boolean? with default false
   ) : Constraints {
     companion object {
       operator fun invoke(schema: Schema): Collection? =
         if (schema.minItems != null || schema.maxItems != null || schema.uniqueItems == true)
           Collection(
             minItems = schema.minItems ?: 0,
             maxItems = schema.maxItems ?: Int.MAX_VALUE,
             uniqueItems = schema.uniqueItems == true
           )
         else null
     }
   }
   ```
   - Note: If you prefer a nullable `uniqueItems: Boolean?`, treat `null` as “not specified”.

2. Update `OpenAPITransformer.collection` to always return `Model.Collection.List` and attach the updated constraint:
   ```kotlin
   private fun Schema.collection(context: NamingContext): Collection {
     val items = requireNotNull(items?.resolve()) { "Array type requires items to be defined." }
     val inner = items.toModel(items.namedOr { context })
     val default = // unchanged logic
     return Collection.List(inner.value, default, description.get(), Constraints.Collection(this))
   }
   ```

3. Update any naming logic that derives names based on `uniqueItems` to consistently use `List` naming. For example, in union case contexts where the current code chooses between "List" and "Set", always choose "List".

### 2. Update tests in typed module
- Adjust tests that currently assert `Set` for `uniqueItems: true` to assert `List` instead, and verify that the `Constraints.Collection.uniqueItems == true` flag is present.
  - `ModelTest.uniqueItemsIsSet` -> `uniqueItemsIsListWithUniquenessConstraint`
  - `ConstraintTest.set` -> `list_with_uniqueItems_constraint`
  - `UnionNameGenerationSpec` cases that expect `Set` naming -> expect `List` naming uniformly.
- New tests (already added) in `ArraySpec.kt` define the desired behavior:
  - Arrays require `items`.
  - `uniqueItems: true` keeps `List` for primitives and for `oneOf` items.

### 3. generation and validation (follow-up)
- Wherever collection uniqueness was implicitly enforced by mapping to a `Set` in code generation, replace with explicit validation logic when `Constraints.Collection.uniqueItems == true`.
- Example check (pseudo-Kotlin runtime validation):
  ```kotlin
  fun <T> validateUnique(items: List<T>): Boolean = items.size == items.toSet().size
  ```
  - For complex items, rely on Kotlin data class equality or domain-specific equality as generated.
  - Ensure this validation is enforced at appropriate boundaries (deserialization, constructor/builders, or request construction), depending on your architectural choices.

### 4. Migration considerations
- This is a behavioral change for consumers who relied on `Set` typing. Migration will require switching parameter/field types from `Set<T>` to `List<T>` and ensuring uniqueness is not silently enforced by the type but explicitly validated.
- Benefits:
  - Preserves order of arrays, as typical in APIs.
  - Matches OpenAPI spec intent (validation, not type transformation).
  - Avoids surprising deduplication.

## Impacted Files
- typed/src/commonMain/kotlin/io/github/nomisrev/openapi/Constraints.kt
- typed/src/commonMain/kotlin/io/github/nomisrev/openapi/OpenAPITransformer.kt
- typed/src/commonMain/kotlin/io/github/nomisrev/openapi/OpenAPITransformer.toUnionCaseContext / naming logic (where applicable)
- typed/src/commonTest/** (update expectations from Set -> List + uniqueness constraint)
- generation/** (enforcement via validation rather than Set)

## Validation Plan
1. Update the code as described.
2. Run the updated test suite, including the new `ArraySpec.kt`. Ensure that all previous Set-based expectations are updated and pass as List + constraint.
3. Verify no regressions in code generation and example projects (e.g., `example/`).

## Conclusion
This change aligns the implementation with the OpenAPI specification for arrays: `uniqueItems` is a validation rule, not a type change. Representing unique arrays as `List` while carrying a uniqueness constraint preserves order, avoids silent deduplication, and provides a clear and portable contract for generators and validators.
