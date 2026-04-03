---
name: kotlin-builder-dsl
description: Write or update Kotlin builder DSLs for existing immutable data types, ADTs, and configuration objects. Use when Codex needs to add an AWS SDK Kotlin-style `Builder` plus `invoke { ... }` entry point, keep the original type shape intact, and optimize end-user ergonomics with convenience methods for collections, maps, nested ADTs, combined collection+nested-builder cases, defaults, and required fields.
---

# Kotlin Builder DSL

## Goal

Add a builder DSL to an existing instantiable Kotlin type.

Prefer the Amazon Kotlin SDK shape:

- Add a nested `Builder`.
- Add `companion object { operator fun invoke(block: Builder.() -> Unit): Type = ... }`.
- Keep the model immutable.
- Optimize the call site for human use, not generator convenience.

## Workflow

1. Inspect the target type before writing code.
   - Find the real construction path: primary constructor, secondary constructor, factory, or companion function.
   - Reuse existing defaults and invariants.
   - Do not change semantics just to make the DSL easier to write.

2. Add the DSL only to a concrete type.
   - Prefer concrete data classes and concrete config classes.
   - For sealed hierarchies, add DSLs to the instantiable leaf types unless the user explicitly wants a root-level factory DSL.
   - Do not invent a builder for an abstract root that still cannot be instantiated.

3. Add the entry point.
   - Prefer:
     ```kotlin
     public companion object {
         public operator fun invoke(block: Builder.() -> Unit): Schema =
             Builder().apply(block).build()
     }
     ```
   - If a companion object already exists, extend it.
   - If the surrounding codebase already uses a different entry point, match local style, but keep the lambda builder shape if the user asked for a DSL.

4. Add the nested `Builder`.
   - Name it `Builder` unless the surrounding type already uses a stronger convention.
   - Keep scalar properties as `var`.
   - Required scalar with no default: make it nullable in the builder and validate in `build()`.
   - Scalar with an existing default: copy that default into the builder.
   - Non-null collection properties: initialize to `emptyList()`, `emptySet()`, or `emptyMap()`.
   - Nullable collection properties: initialize to `null`.
   - Nested ADTs: store the built type, then add a nested lambda helper if that nested type also has a DSL.
   - Collections of ADTs: combine collection helpers and nested builder helpers instead of choosing only one.

5. Add end-user convenience methods.
   - Always keep the plural property for bulk replacement.
   - Add singular helpers for append-style usage.
   - For list-like and set-like collections, also add a multi-add overload using `first, vararg other`.
   - Prefer the best call-site UX over purely mechanical generation.

6. Add `build()`.
   - Construct the existing target type.
   - Use `requireNotNull` for required builder fields with clear messages.
   - Preserve existing validation.
   - Snapshot mutable backing state before returning if the builder internally used mutable collections.

7. Add focused tests when the target project has tests.
   - Test the happy path DSL.
   - Test required-field failure.
   - Test collection convenience methods.
   - Test nested DSL helpers when added.

## UX Rules

### Scalars

- Keep simple scalar members as direct builder properties.
- Do not add trivial wrappers like `name(value)` for every `String`.
- Let users write `name = "schema"` unless the local DSL style strongly prefers setter functions.

### Lists

For `val scopes: List<String>`, expose both:

```kotlin
public var scopes: List<String> = emptyList()

public fun scope(value: String) {
    scopes = scopes + value
}

public fun scope(first: String, vararg other: String) {
    scopes = scopes + first + other
}
```

Rules:

- Singular helper name should normally be the singular form of the property.
- If singularization is awkward or wrong, use an explicit verb such as `addScope`.
- For multiple values, prefer `fun scope(first: String, vararg other: String)` over a single `vararg` parameter.
- Do not expose `MutableList` on the public DSL surface unless the local style already does that.

### Sets

For `val tags: Set<String>`, expose both:

```kotlin
public var tags: Set<String> = emptySet()

public fun tag(value: String) {
    tags = tags + value
}

public fun tag(first: String, vararg other: String) {
    tags = tags + first + other
}
```

Rules:

- Preserve set semantics.
- Use singular helper names when natural.
- For multiple values, prefer `fun tag(first: String, vararg other: String)` over a single `vararg` parameter.
- Fall back to `addTag`, `addRole`, and similar names when the singular form is unclear.

### Maps

For `val attributes: Map<String, String>`, expose both:

```kotlin
public var attributes: Map<String, String> = emptyMap()

public fun attribute(key: String, value: String) {
    attributes = attributes + (key to value)
}
```

Rules:

- Prefer a singular entry helper derived from the property name.
- If the singular form is unclear, use `putAttribute`, `putMetadata`, or another explicit name.
- Keep the plural property for full replacement.
- Do not force users to build one-off maps at the call site for common cases.

### Nullable collections

For nullable collections, preserve nullability and still support ergonomic append helpers:

```kotlin
public var scopes: List<String>? = null

public fun scope(value: String) {
    scopes = (scopes ?: emptyList()) + value
}

public fun scope(first: String, vararg other: String) {
    scopes = (scopes ?: emptyList()) + first + other
}
```

Do not silently change a nullable model field into a non-null one.

### Nested ADTs

If a property type already has a builder DSL, add a nested lambda helper:

```kotlin
public var retry: RetryPolicy? = null

public fun retry(block: RetryPolicy.Builder.() -> Unit) {
    retry = RetryPolicy(block)
}
```

Rules:

- Prefer nested lambdas for nested config-like types.
- Do not add nested DSL helpers for arbitrary third-party types with no matching DSL.
- Do not duplicate multiple competing ways to set the same value unless the local API already does that.

### Collections of nested ADTs

If a collection element type already has a builder DSL, combine both features.

For `val endpoints: List<Endpoint>`, prefer:

```kotlin
public var endpoints: List<Endpoint> = emptyList()

public fun endpoint(value: Endpoint) {
    endpoints = endpoints + value
}

public fun endpoint(first: Endpoint, vararg other: Endpoint) {
    endpoints = endpoints + first + other
}

public fun endpoint(block: Endpoint.Builder.() -> Unit) {
    endpoints = endpoints + Endpoint(block)
}
```

Rules:

- Do not make the caller choose between collection ergonomics and nested-builder ergonomics.
- Keep the plural property for full replacement.
- Add the single-value helper.
- Add the `first, vararg other` overload for bulk appends.
- Add the nested builder overload when the element type supports it.
- Apply the same rule to `Set<T>` collections of DSL-enabled ADTs.

## Required Field Rules

For required fields without defaults, use nullable builder state plus explicit validation:

```kotlin
public var name: String? = null

public fun build(): Schema =
    Schema(
        name = requireNotNull(name) { "Missing value for name" },
        attributes = attributes,
        scopes = scopes,
    )
```

Rules:

- Match existing validation messages when the type already has them.
- Keep messages precise.
- Do not default required fields to fake placeholders.

## Recommended Shape

Use this as the default pattern when the surrounding code does not strongly suggest otherwise:

```kotlin
public data class Schema(
    val name: String,
    val attributes: Map<String, String> = emptyMap(),
    val scopes: List<String> = emptyList(),
) {
    public companion object {
        public operator fun invoke(block: Builder.() -> Unit): Schema =
            Builder().apply(block).build()
    }

    public class Builder {
        public var name: String? = null
        public var attributes: Map<String, String> = emptyMap()
        public var scopes: List<String> = emptyList()

        public fun attribute(key: String, value: String) {
            attributes = attributes + (key to value)
        }

        public fun scope(value: String) {
            scopes = scopes + value
        }

        public fun scope(first: String, vararg other: String) {
            scopes = scopes + first + other
        }

        public fun build(): Schema =
            Schema(
                name = requireNotNull(name) { "Missing value for name" },
                attributes = attributes,
                scopes = scopes,
            )
    }
}
```

This should enable:

```kotlin
val schema = Schema {
    name = "user"
    attribute("version", "v1")
    scope("read", "write")
}
```

## Do Not Do This

- Do not rewrite the model into a mutable type.
- Do not remove existing constructors or factories unless the user asked for that.
- Do not expose mutable collection implementation types without a good local reason.
- Do not generate broken singular names just because the property is plural.
- Do not skip the collection helper just because the element type also has a nested DSL.
- Do not add helper methods that make the API larger without improving the call site.
- Do not change nullability, defaults, or validation semantics to fit the DSL.

## Decision Rules For Naming

Use these naming rules in order:

1. Prefer the natural singular form.
   - `scopes` -> `scope`
   - `attributes` -> `attribute`
   - `tags` -> `tag`

2. If the singular form is ambiguous or ugly, use an explicit verb.
   - `children` -> `addChild`
   - `metadata` -> `putMetadata`
   - `entries` -> `entry` only if that reads naturally in the target API

3. If the project already has a naming convention, follow it instead of inventing a new one.

## Final Check

Before finishing:

- Verify the DSL can build the existing type without changing its public meaning.
- Verify collection-heavy fields have ergonomic helpers.
- Verify list/set helpers support both single and multi-add usage.
- Verify collections of nested ADTs support both direct values and nested builder blocks.
- Verify required fields still fail loudly.
- Verify the call site reads cleanly for a human.
- Prefer a smaller API with better names over a larger API with mechanical names.
