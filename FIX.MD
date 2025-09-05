# Proposal: Support readOnly and writeOnly in typed module

Issue: Support readOnly and writeOnly in OpenAPI Schema properties so that:
- readOnly properties are included in responses but excluded from requests;
- writeOnly properties are included in requests but excluded from responses.
- If a readOnly or writeOnly property is listed in `required`, the requirement only applies in its relevant scope (responses for readOnly; requests for writeOnly).

Reference: https://swagger.io/docs/specification/data-models/data-types/#readonly-writeonly

## Current state (observations)
- The parser module already models `Schema.readOnly: Boolean?` and `Schema.writeOnly: Boolean?` (parser/src/commonMain/kotlin/io/github/nomisrev/openapi/Schema.kt).
- The typed module builds domain models via `OpenAPITransformer` (typed/src/commonMain/kotlin/io/github/nomisrev/openapi/OpenAPITransformer.kt) and represents them as `Model` (typed/src/commonMain/kotlin/io/github/nomisrev/openapi/Model.kt).
- `Model.Object.Property` contains:
  - baseName, model, isRequired, isNullable, description.
  - It does NOT encode readOnly/writeOnly, and currently `isRequired` is computed from `schema.required.contains(name)` ignoring readOnly/writeOnly.
- Request/response shapes are built in:
  - Requests: `OpenAPITransformer.toRequestBody(...)` and `Operation.input(...)` (parameters)
  - Responses: `OpenAPITransformer.toResponses(...)`
- All model materialization goes through `Resolved<Schema>.toModel(context)`, which eventually calls `Schema.toObject(context, properties)` to build `Model.Object`.
- There is no concept of request vs response scope in the transformation, so readOnly/writeOnly are ignored.

## Goal
Introduce scope-aware transformation in typed so that the same underlying schemas can produce different shapes depending on usage:
- Request scope: filter out readOnly properties.
- Response scope: filter out writeOnly properties.
- Neutral/Common scope (components/models() traversal): keep all properties unchanged to avoid altering shared type introspection or unrelated code generation.
- Ensure `required` semantics apply only in the relevant scope.

## Design overview
We will implement scope-aware filtering inside the typed transformation (OpenAPITransformer), without changing the external structure of `Model`.

Key points:
- Introduce an internal enum TransformScope with values: Request, Response, Neutral.
- Add a defaulted scope parameter to the transformation pipeline (toModel and its internal helpers), defaulting to Neutral for existing call sites.
- When building `Model.Object` from `Schema`, filter properties depending on scope:
  - Request: drop properties where resolved property schema has `readOnly == true`.
  - Response: drop properties where resolved property schema has `writeOnly == true`.
  - Neutral: do not filter.
- `isRequired` remains computed based on the filtered set: `required.contains(name)` on the properties that remain after filtering. This naturally implements the spec rule that readOnly requirements apply in responses only and writeOnly requirements apply in requests only, since the out-of-scope properties are removed.
- Apply the same scope transitively to nested objects, arrays of objects, unions with object cases, and allOf mergers.
- Parameters (query/path/header/cookie) are inherently request scope. In practice they almost never carry readOnly/writeOnly at property-level (they’re usually primitives or arrays), but we will still pass Request scope consistently for correctness and future-proofing.

## Detailed changes (typed module)

Files primarily affected:
- typed/src/commonMain/kotlin/io/github/nomisrev/openapi/OpenAPITransformer.kt
- typed/src/commonMain/kotlin/io/github/nomisrev/openapi/Model.kt (no changes required)

1) Add internal scope enum in OpenAPITransformer.kt

```kotlin
private enum class TransformScope { Request, Response, Neutral }
```

2) Thread scope through the transformation API

Add an overload/defaulted parameter to the following functions (default = Neutral to preserve existing behavior):
- `Resolved<Schema>.toModel(context: NamingContext, scope: TransformScope = Neutral): Resolved<Model>`
- `Schema.type(context: NamingContext, scope: TransformScope = Neutral): Model`
- `Schema.toObject(context: NamingContext, scope: TransformScope = Neutral): Model`
- `Schema.toObject(context: NamingContext, properties: Map<String, ReferenceOr<Schema>>, scope: TransformScope = Neutral): Model`
- `allOf(schema: Schema, context: NamingContext, scope: TransformScope = Neutral): Model`
- `Schema.collection(context: NamingContext, scope: TransformScope = Neutral): Model.Collection`
- `Schema.toEnum(context: NamingContext, scope: TransformScope = Neutral): Model.Enum.Closed` (scope unused but passed through for consistency)
- `Schema.toOpenEnum(context: NamingContext, scope: TransformScope = Neutral): Model.Enum.Open` (scope unused)
- `Schema.toUnion(context: NamingContext, subtypes: List<ReferenceOr<Schema>>, scope: TransformScope = Neutral): Model.Union`
- `toUnionCaseContext(...)` can keep its signature; it doesn’t construct properties.
- `nestedModel(resolved: Resolved<Schema>, caseContext: NamingContext, scope: TransformScope = Neutral): Model?`

All internal calls that currently invoke these methods should pass the same scope value they received, to ensure deep/nested filtering is applied uniformly.

3) Property filtering in toObject

In `Schema.toObject(context, properties, scope)`:
- Build the property list by iterating over `properties.entries` but apply scope filtering:
  - If `scope == Request` and `resolvedProp.value.readOnly == true`, skip this property.
  - If `scope == Response` and `resolvedProp.value.writeOnly == true`, skip this property.
  - Otherwise include it.
- For included properties, compute:
  - `isRequired = required.contains(name)`
  - `isNullable = resolved.value.nullable ?: !required.contains(name)`
- The result is a `Model.Object` with only applicable properties included.

4) Requests and responses entry points set scope

- In `toRequestBody(...)` where JSON schema is turned into a `Route.Body.Json.Defined`, call `json.toModel(context, TransformScope.Request)`.
- In `toResponses(...)` where JSON schema is turned into a `Route.ReturnType`, call `resolved.toModel(context, TransformScope.Response)`.
- In `Operation.input(...)` (parameters), call `resolved.toModel(context, TransformScope.Request)`.

5) Multipart/form-data

Current multipart handling iterates the raw `resolved.value.properties!!` and constructs `Route.Body.Multipart.Value` by mapping each property directly, bypassing `Model.Object` construction. We should align it with the scope filtering:
- Minimal change option: Apply an inline filter there to skip readOnly properties when building form-data (request scope). Additionally, for each field’s `toModel(ctx(name))`, pass `TransformScope.Request`.
- Preferred consistent approach: First construct `val obj = resolved.toModel(contextForObject, TransformScope.Request).value as Model.Object` and then derive `FormData` entries from `obj.properties`, ensuring the same filtering and nested behavior applies.

Given we aim for minimal changes, the inline filter plus passing scope for the per-field `toModel` calls is acceptable. The consistent approach is slightly more refactor but less error-prone.

6) AllOf, OneOf, AnyOf, Arrays

- `allOf(...)` already merges properties and then delegates to `toObject(context, mergedProps)`. After adding scope to `toObject`, the filtering will be applied automatically.
- `oneOf` and `anyOf` produce unions. Since union cases often contain object models, passing scope to all recursive `toModel` calls ensures the filtering applies to union case objects as well.
- Arrays: when building the inner model via `items.resolve().toModel(...)`, pass the same scope so arrays of objects also get filtered.

7) Required semantics

- Because we filter excluded properties out of the property list before setting `isRequired`, the following naturally holds:
  - readOnly + required: required in Response only (it is removed from Request altogether).
  - writeOnly + required: required in Request only (it is removed from Response altogether).

8) Top-level readOnly/writeOnly

- The OpenAPI spec primarily uses readOnly/writeOnly for object properties. If a top-level schema used as a request or response were to be marked readOnly/writeOnly, we propose to treat those flags as no-ops for non-object shapes. We will document this explicitly, and optionally warn in a later iteration.

## Tests to add (typed module)
Add new tests in `typed/src/commonTest/kotlin/io/github/nomisrev/openapi/`:

1) Read-only property filtered from request
- Schema: object with id (integer, readOnly=true, required), username (string, required), password (string, writeOnly=true, required).
- Request shape (POST body): properties should be username, password; id excluded; username and password are required.
- Response shape (GET response): properties should be id, username; password excluded; id and username required (id because required + readOnly applies to response, username because required and not writeOnly).

2) Array of objects with readOnly/writeOnly in items
- Request: each item object should exclude readOnly properties.
- Response: each item object should exclude writeOnly properties.

3) oneOf/anyOf/union case containing objects with readOnly/writeOnly
- Ensure request scope excludes readOnly and response scope excludes writeOnly for object cases.

4) allOf merged objects
- Verify that merged properties respect the filtering and required semantics per scope.

5) Multipart/form-data
- For a multipart request schema with readOnly property, confirm the field is omitted in the constructed `Route.Body.Multipart.Value`.

6) Parameter schema
- Use a parameter with a (contrived) readOnly flag. Confirm that transformation passes Request scope and behavior is stable (no failure; filtering is a no-op for primitives).

## Backwards compatibility and impact
- `Model` API remains unchanged, preserving code generation behavior. The only difference is that the `Model` instances attached to Route request/response bodies will have properties filtered per scope, which is the intended correction.
- We add default parameters to internal transformer functions; external APIs `OpenAPI.routes()` and `OpenAPI.models()` remain unchanged.
- Component model collection via `OpenAPI.models()` will still use Neutral scope, keeping component-level models unchanged. This prevents accidental removal of properties in code generation for shared types unless they are used in a scoped context (request/response), which is exactly the goal.

## Implementation steps

1) Introduce `TransformScope` in OpenAPITransformer.kt.
2) Add scope parameters (defaulting to Neutral) to transformation methods and thread them through recursive calls.
3) Implement property filtering in `Schema.toObject(..., scope)`.
4) Update entry points to pass scope:
   - Requests: `toRequestBody`, `Operation.input` → Request
   - Responses: `toResponses` → Response
   - Multipart: add filtering or refactor to reuse `Model.Object` properties from scoped model.
5) Add tests described above in typed commonTest to validate behavior.
6) Run existing test suite to ensure no regressions.

## Acceptance criteria
- For any object schema used as a request body, the resulting `Model.Object` must not contain readOnly properties.
- For any object schema used as a response body, the resulting `Model.Object` must not contain writeOnly properties.
- Required semantics must apply in the correct scope only.
- Nested usages (arrays, unions, allOf) must reflect the same filtering behavior.
- Multipart request bodies must not include readOnly properties.

## Future considerations (out of scope for this change)
- Emitting compile-time annotations or metadata to indicate readOnly/writeOnly on generated types (we currently materialize the filtered shapes only, which is sufficient for correctness).
- Validation and diagnostics for suspicious use of readOnly/writeOnly at top-level schemas.
- Improved treatment of `oneOf` required semantics (not strictly related to this issue, but adjacent in the same code paths).

---

If this proposal is accepted, I will proceed with the implementation in typed following the steps above, and add the tests accordingly.
