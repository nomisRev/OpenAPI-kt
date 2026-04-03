# `typed` Module Architecture & Transformation Logic

The `typed` module is responsible for converting raw OpenAPI `Schema` objects (from the `parser` module) into a strict, Kotlin-centric Intermediate Representation (`Model`). This process happens primarily through a functional, context-receiver-heavy architecture.

This document maps out the current state of schema parsing and IR generation to serve as a baseline for future refactoring efforts (e.g., introducing a Transformer Pipeline or Interceptor pattern).

## 1. Logical Branches (`SchemaTransformer.kt`)

The core engine is the `ResolvedSchema.toModel(...)` extension function. It acts as a centralized dispatcher using a large, precedence-based `when` expression to determine how a schema maps to the IR.

**The Dispatch Precedence Order:**
1. **Reference Guards:** Immediately handles `ResolvedSchema.Reference` (if `resolveReference = false` or if there's a discriminated subtype) and `ResolvedSchema.Recursive` to prevent infinite loops.
2. **Discriminated Objects:** Handles explicit inheritance-based polymorphism (OpenAPI `discriminator` on a `$ref`).
3. **Composite Structures (`allOf`, `oneOf`, `anyOf`):**
    * Only evaluated if `compositeTakesPrecedence` is true. This flag is `true` if no `type` is explicitly defined, OR if a helper function determines the composite block adds meaningful structure over the base type.
    * *Null Flattening:* First intercepts any composite where one branch is just `{ type: "null" }` (or similar nullability indicators). It discards the null branch and marks the resulting `Model` as `isNullable = true`.
    * *Single Item Unwrapping:* If a composite only has one valid branch after null-flattening, it delegates directly to that branch instead of generating a Union/AllOf wrapper.
4. **Base Types (`type: object`, `array`, `string`, etc.):**
    * *Array Type Tuple:* Handles OpenAPI 3.1 `type: [string, null]` arrays by translating them into unions (`anyOf`) or applying nullability.
    * *Enums:* Primitive types paired with `enum` arrays are routed to Enum generation.
    * *Objects:* Checks if `properties` or `additionalProperties` are present.
5. **Implicit Properties:** E.g., if `type` is missing but a `properties` map exists, it assumes it's an Object.
6. **Fallback:** Generates `Model.FreeFormJson` (representing an untyped JSON element) if the shape cannot be statically determined.

## 2. Recursive Guards

OpenAPI schemas can be infinitely recursive (e.g., a `TreeNode` schema that contains a `children` array of `TreeNode`). Eagerly evaluating every `$ref` would cause a `StackOverflowError`.

* **`ResolvedSchema.Recursive`:** The `Registry` tracks the resolution path. If it detects a cycle (visiting a reference it is already currently resolving), it wraps the schema in a `ResolvedSchema.Recursive`.
* **The Guard:** The transformer catches this immediately at the top of the `when` block:
  ```kotlin
  this is ResolvedSchema.Recursive -> Model.Reference(name, description(), isNullable, schema.title)
  ```
  Instead of expanding the schema further, it returns a `Model.Reference` pointing to the named type.
* **`resolveReference` Flag:** Controls whether top-level references are expanded (inlined into the IR) or kept as pointers. Nested references strictly default to `false` to avoid inlining shared types unnecessarily and to break reference cycles safely.

## 3. Recursive Typing & Information Preservation

When a recursion guard triggers, the goal is to emit a `Model.Reference` that holds enough context for the renderer to generate a valid Kotlin reference (e.g., `val node: TreeNode`).

* **What is preserved:** The `Model.Reference` retains:
  * The `NamingContext` (which holds the exact package/class name path).
  * `isNullable` state.
  * `description` and `title`.
* **What is deferred:** The actual structural `properties` or `cases`. The generator assumes that the concrete `Model.Object` or `Model.Union` for this reference has already been (or will be) generated as a separate top-level construct.

## 4. Depth of Inspection & Optimization

The module performs deep inspection on schemas to correctly map messy, flexible OpenAPI YAML into clean Kotlin type hierarchies.

* **`compositeTakesPrecedence` / `addsStructuralCompositeShape()`:**
  If a schema declares `type: object` *and* `allOf`, the code recursively inspects the `$ref` branches inside the `allOf`. If those branches contain properties, enums, or nested composites, it prioritizes the composite logic over just generating a generic object model.
* **Tag-Only Discriminator Inference (`Union.kt`):**
  If `oneOf` cases share a common literal string property (e.g., all have an `event` property, but case A has `event: "click"` and case B has `event: "hover"`), the transformer deeply inspects all branches using `flattenAllOfForUnionDiscriminator` to look through nested `allOf` blocks. It extracts this as a "Tagged Custom" or "Native Discriminator". This allows the renderer to create clean Kotlin sealed classes instead of generic JSON structures.
* **Property Hoisting / Stripping:**
  Once a discriminator is found, the transformer strips the redundant discriminator property from the subtype (preventing the need to define `override val type = "click"` in Kotlin if it can be implicitly serialized). If an object only has one property left after stripping, it attempts to "hoist" it (flattening the hierarchy).

## 5. Edge Cases Handled

* **Nullable Composites:** OpenAPI represents optional polymorphic fields as `oneOf: [ { type: "null" }, { $ref: "#/components/schemas/User" } ]`. The code explicitly flattens this into a nullable `Model.Reference`.
* **Open Enums:** Evaluated dynamically during name generation. If a `oneOf` has $N$ cases, where $N-1$ are generic `string` types and $1$ is a string `enum`, it identifies this as an "Open Enum" pattern and alters the name generation to support extensibility.
* **`hadPropertiesBeforeStripping`:** In OpenAPI, read/write contexts can strip properties (e.g., removing a `readOnly: true` `id` field during POST request generation). The `Model.Object` tracks this boolean so the renderer knows the difference between an explicitly empty object (renders as `data object`) and an object whose properties were stripped (must render as a `class` to preserve correct serialization semantics).
* **Scalar Wrappers:** Marks models where primitive references were wrapped in an object just to give them a name, allowing the parameter generator to unwrap them later at public boundaries (path/query parameters).

## 6. Name Generation (`NamingContext`)

Because inline OpenAPI schemas lack names (e.g., an anonymous `object` defined inline inside another `object`'s properties), the `NamingContext` class maintains an immutable, hierarchical trail of breadcrumbs to generate deterministic, readable Kotlin class names.

* **Structure:** It consists of a `Head` (the entry point: `Reference` or `Path`) and a list of `Nested` states (`ObjectProperty`, `UnionCase`, `RouteParam`, etc.).
* **`Union.kt` Name Generation Fallbacks (`unionCaseName`):**
  Generating names for anonymous `oneOf` branches is highly complex. The fallback logic cascades as follows:
  1. Explicit `title` property in the spec.
  2. `compositeUnionCaseName()` (inspects and derives names from sub-branches).
  3. Known discriminator/tag property names (e.g., the value of the `type` or `kind` field).
  4. **"Open Enum" Rule:** If it's an open enum, names the enum part `CaseEnum`.
  5. **"Else" Rule:** If $N-1$ cases are references and $1$ is inline, it names the inline one `CaseElse`.
  6. **Property Joining:** If it's an object with a few properties, it concatenates them (e.g., `AgeAndName`).
  7. **Primitive Name:** E.g., `CaseString`, `CaseInt`.
  8. **Absolute Fallback:** `Case0`, `Case1`, etc.