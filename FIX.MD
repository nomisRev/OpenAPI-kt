# Proposal: Properly implement Maps (OpenAPI `additionalProperties`)

This proposal addresses failing tests in the new typed module test suite (MapsTest.kt) and aligns behavior with the OpenAPI specification for dictionaries/maps.

## Summary of Failing Behavior
- For schemas with `type: object` and `additionalProperties` being a schema (primitive or `$ref`), the transformer currently returns `Model.FreeFormJson`.
- Expected behavior per OpenAPI spec: a string-keyed dictionary should be represented as a Map type with value type derived from `additionalProperties`.

Example:
```yaml
# Expected: Map<String, String>
type: object
additionalProperties:
  type: string
```
Currently becomes `Model.FreeFormJson`, but should become `Model.Collection.Map(Primitive.String, ...)`.

The new tests in `typed/src/commonTest/kotlin/io/github/nomisrev/openapi/MapsTest.kt` that would fail:
- "dictionary with primitive value additionalProperties string"
- "dictionary with $ref value"

Free-form object tests for `additionalProperties: true` and `{}` already match current behavior and are OK.

## Proposed Minimal Changes
Focus: Implement typed dictionary mapping in the typed transformer without invasive changes to the model hierarchy.

Files to update: `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/OpenAPITransformer.kt`

1) Implement Map generation when `additionalProperties` is a schema and no fixed properties exist

- In `private fun Schema.toObject(context: NamingContext): Model`:
  - Case: `properties == null` and `additionalProperties is AdditionalProperties.PSchema`:
    - Resolve the inner schema: `val innerResolved = props.value.resolve()`
    - Compute a naming context for the inner similarly to arrays (`collection(context)`):
      - `val innerCtx = innerResolved.namedOr { context }`
      - `val innerModel = innerResolved.toModel(innerCtx).value`
    - Return `Model.Collection.Map(inner = innerModel, description = description.get(), constraint = null)`.
  - Keep existing behavior for:
    - `additionalProperties is Allowed`:
      - `true` -> `Model.FreeFormJson(...)`
      - `false` -> throw `IllegalStateException` (as today)
  - Case: `properties != null` stays as-is for now (see Future Enhancements).

2) Support implicit objects with only `additionalProperties`

- In `private tailrec fun Schema.type(context: NamingContext): Model`, when `type == null` and `properties != null || additionalProperties != null`, we already call `toObject(context)`. With change (1), this will correctly produce a Map if only `additionalProperties` is defined.

3) (Optional, but small) Handle `allOf` edge-case

- In `private fun allOf(schema: Schema, context: NamingContext): Model`, if all subschemas are object-like and after merging we have:
  - `mergedProps.isEmpty()` and we detect `additionalProperties is PSchema` (from any subschema), return a Map similar to (1) instead of `FreeFormJson`.
- This keeps behavior consistent when a dictionary is specified via composition.

No changes required in `Model.kt` – `Model.Collection.Map` already exists.

## Non-goals / Deferred
- Objects that have both fixed properties AND `additionalProperties` (mixed shape: fixed keys + dictionary). Representing this faithfully would need a new model shape (e.g. `Model.Object` plus an additional map payload) or extending `Model.Object` to carry an optional `additionalProperties` field. This is out-of-scope for the minimal change and can be handled in a follow-up PR.
- Applying `minProperties`/`maxProperties` constraints to Map. Current `Model.Collection.Map` uses `Constraints.Collection?`; object cardinality constraints are not wired for maps. We keep `constraint = null` for now, consistent with current design.

## Pseudocode Patch
In `Schema.toObject(context)`:
```kotlin
when {
  properties != null -> toObject(context, properties!!)
  additionalProperties != null -> when (val ap = additionalProperties!!) {
    is AdditionalProperties.PSchema -> {
      val innerResolved = ap.value.resolve()
      val innerCtx = innerResolved.namedOr { context }
      val innerModel = innerResolved.toModel(innerCtx).value
      Model.Collection.Map(innerModel, description.get(), /* constraint = */ null)
    }
    is Allowed -> if (ap.value) Model.FreeFormJson(description.get(), Constraints.Object(this))
                  else error("No additional properties allowed on object without properties. $this")
  }
  else -> Model.FreeFormJson(description.get(), Constraints.Object(this))
}
```

In `allOf(...)` (optional):
- After resolving/merging, if `mergedProps.isEmpty()` and any subschema had `additionalProperties is PSchema`, resolve its inner and return `Model.Collection.Map` instead of `FreeFormJson`.

## Impact Assessment
- Code generation already understands `Model.Collection.Map` (see `generation/src/test/.../DefaultsAndTypeNameTest.kt` expecting `Map<String, Int>`), so adding map emission in typed transformer should integrate smoothly with generation.
- No breaking API changes to public models/interfaces; only behavior becomes correct for dictionary schemas.

## Tests
The new tests in `MapsTest.kt` cover:
- free-form objects (true and `{}`) -> FreeFormJson (already pass)
- dictionaries with primitive values -> Map<String, String>
- dictionaries with `$ref` values -> Map<String, Message> and ensuring the `Message` model is present

After implementing changes, these tests should pass.

## Follow-up Enhancements (separate PR)
- Support objects with both fixed keys and `additionalProperties` by extending model to represent both the fixed fields and the dynamic map.
- Apply `minProperties`/`maxProperties` constraints to maps (extend constraints model appropriately).
- Improve naming context for inline object-valued maps (e.g., `MessagesValue`).
