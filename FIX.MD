# Proposal: Implement AnyValue (Empty Schema `{}`) as Free-Form JSON

This proposal describes minimal changes to support OpenAPI empty Schema (`{}`) as an "Any value" type across the typed transformation, aligned with the specification described in the issue.

## Background
OpenAPI defines that a schema without a `type` matches any data type. The shorthand `{}` is equivalent to a union across primitive types, arrays (with items of any type), and objects. In our typed model, we already represent free-form JSON values with `Model.FreeFormJson`, which maps to `kotlinx.serialization.json.JsonElement` in code generation. This is semantically appropriate for an "Any value".

Currently, transforming an empty schema (`Schema()` with all fields null/defaults) results in a `NotImplementedError` from `OpenAPITransformer.type`, which breaks the correct support for `AnyValue` and places where `{}` is used (e.g., array items `{}`, body schemas `{}`, properties referencing `{}`).

A new test suite `AnyValueSpec` has been added under `typed` and currently fails, confirming the gap:
- Top-level component `{}` should become `Model.FreeFormJson` (with description propagated)
- Array `items: {}` should become `Collection.List(inner = FreeFormJson)`
- As an object property, `nullable: true` must be respected on the property

## Goals
- Map empty schema `{}` to `Model.FreeFormJson` in the typed transformer.
- Ensure description is propagated when present.
- Allow `{}` to be used anywhere a schema is accepted (components, properties, array items, request/response bodies).
- Keep changes minimal and non-invasive.

## Proposed Changes
1. Open file `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/OpenAPITransformer.kt`.
2. In `private tailrec fun Schema.type(context: NamingContext): Model`, handle `type == null` with the following cases:
   - If `properties != null || additionalProperties != null` → existing behavior (`toObject(context)`) stays.
   - Else if `items != null` → existing behavior (`collection(context)`) stays.
   - Else (i.e., truly empty schema `{}`) → return `Model.FreeFormJson(description.get(), null)` instead of `TODO(...)`.

   Concretely, replace the current fallback branch:
   else -> TODO("Schema: $this not yet supported. Please report to issue tracker.")
   with:
   else -> Model.FreeFormJson(description.get(), null)

3. With this change:
   - Top-level component `{}` transforms successfully into `Model.FreeFormJson`.
   - Array `items: {}` transforms with inner as `Model.FreeFormJson` because `collection(context)` calls `items.resolve().toModel(...)`.
   - Request/response bodies with an explicit `{}` schema will now resolve to `Model.FreeFormJson`.
   - Object properties referencing `{}` become `Model.FreeFormJson`, and existing code already sets `isNullable` from `resolved.value.nullable`.

4. Update tests that currently assert the `NotImplementedError` for empty schema:
   - `typed/src/commonTest/kotlin/io/github/nomisrev/openapi/ModelTest.kt` test `emptySchema` should be updated from expecting `NotImplementedError` to expecting `Model.FreeFormJson(description = null, constraint = null)`.

## Non-Goals / Alternatives
- Implementing `{}` as a large `Model.Union` over all primitive types, arrays and objects is more complex and provides little additional value for generation compared to `JsonElement`. `Model.FreeFormJson` is already used elsewhere to represent free-form JSON, and generation maps it to `JsonElement`, which faithfully represents arbitrary JSON values.
- Supporting JSON Schema `null` type directly (`Type.Basic.Null`) is out-of-scope here. OpenAPI 3.0 uses `nullable: true`, which is already handled for properties. If needed in future, `null` union handling can be considered separately.

## Risk Assessment
- Low. The change only affects the fallback branch when no type/properties/items are set. Previously it crashed with `TODO`; now it returns a valid model. Existing behaviors for object-ish and array-ish schemas with missing `type` remain unchanged.
- Code generation already supports `Model.FreeFormJson` → `JsonElement`, so no generator changes are required.

## Test Plan
- The added `AnyValueSpec` includes:
  - Top-level component `{}` → `Model.FreeFormJson` (with description propagation)
  - Array `items: {}` → `Collection.List(inner = FreeFormJson)`
  - Property referencing `{}` with `nullable: true` → property.isNullable = true, type `FreeFormJson`
- Update `ModelTest.emptySchema` expectation accordingly.
- Run full test suite for `typed` and `generation` modules to ensure no regressions.

## Summary
By mapping empty schemas `{}` to `Model.FreeFormJson`, we align with OpenAPI semantics for "Any value" while keeping implementation minimal and leveraging existing generation capabilities (JsonElement). This resolves the failing tests and adds robust support for AnyValue across all positions (components, items, properties, bodies).
