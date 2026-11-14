# An optional, nullable property cannot say "send null" separately from "leave it out"

**Status:** fixed on branch openapi-3.2-streaming with option 1 (generated tri-state `Optional<T>`: `Absent`, `Null`, `Value`).
Proven by `OptionalPropertyEncodingTest`, `OptionalNullableVariantsTest` and `OptionalNullableWireRuntimeTest` under Ktor's
`DefaultJson`, `Json {}` and `Json { explicitNulls = false }`. Still open: the `integration-tests` regeneration, and a
separate finding: under `Json { explicitNulls = false }` a required nullable property set to `null` is not written either
(`T?` without `@EncodeDefault`/tri-state), which the spec does not allow.
**Confirmed:** 2026-11-14 (branch openapi-3.2-streaming), by `OptionalPropertyEncodingTest` and the golden `object/optional-encoding`
**Stage:** wrong output (silent), depends on the caller's `Json` configuration
**Impact:** not counted on the corpus. In `github.json`: 76 optional and nullable top-level request-body properties in 37 operations, 18 of them `PATCH`
**Source spec valid:** yes

## Symptom

A property that is not in `required` and is nullable has three states on the wire: absent, `null`, a value. The
generated property has two:

```kotlin
public val optionalNullable: String? = null
```

What an unset property sends is decided by the `Json` the caller installed, not by the caller's intent
(`OptionalPropertyEncodingTest.optionalNullablePropertyFollowsJsonConfiguration`, a characterization test):

| `Json` | `optionalNullable = null` sends |
| --- | --- |
| Ktor `ContentNegotiation { json() }` (`encodeDefaults = true`, `explicitNulls = true`) | `"optionalNullable": null` |
| `Json {}` (`encodeDefaults = false`) | key absent |

Neither configuration can express both. With Ktor's default every unset optional nullable property is sent as
`null`; with `Json {}` or `explicitNulls = false` an explicit null can never be sent.

The same holds for a flattened inline body, where the property is an `invoke` parameter
(`invoke(description: String? = null)` builds `Body(description = description)`).

## Why it matters

For `PATCH`, `null` and absent usually mean different things: "clear this field" and "leave it unchanged".
Example from `github.json`: `PATCH /enterprises/{enterprise}/teams/{team_slug}` has optional, nullable `description`
and `name`. With Ktor's default `Json`, a call that only sets `name` also sends `"description": null`. Whether a
given server reads that as "clear" has not been tested here; the spec permits it.

## What the spec says

- JSON Schema: `required` governs presence, `type` (or OAS 3.0 `nullable`) governs the value. They are independent,
  so absent and `null` are different instances.
- RFC 7396 (JSON Merge Patch) gives `null` the meaning "remove", absent the meaning "unchanged".

## What is already right

The other three combinations do not depend on the `Json` configuration (same test class):

| Property | Generated | Wire when unset |
| --- | --- | --- |
| optional, not nullable | `@EncodeDefault(NEVER) val x: T? = null` | always absent |
| required, nullable | `val x: T?` (or `@Required val x: T? = null`) | always `null` |
| required, not nullable | `val x: T` | n/a |

## Root cause

`T? = null` is used for both "optional" and "nullable", so for a property that is both, Kotlin `null` has to stand
for two wire states. The property gets no `@EncodeDefault`, so kotlinx.serialization falls back to the configuration.

## Correct final API (decided: option 1)

The untyped-parameter fix (`3fb0ba0b`) settled the same question for parameters: Kotlin `null` is "not sent", and a
distinct value (`JsonNull`) is "the value null". A typed property needs an equivalent third state. Candidates:

1. **A tri-state type for optional and nullable properties only**, for example a small generated or runtime
   `sealed interface Optional<out T>` with `Absent`, `Null` and `Value(T)`, defaulting to `Absent`, with a serializer
   that omits the key for `Absent`. Exact, independent of `Json`; changes the type of these properties, also when the
   same model is read from a response.
2. **Always omit `null`** (`@EncodeDefault(NEVER)` like the optional, not nullable case). Independent of `Json`,
   keeps `T?`, but an explicit `null` becomes impossible to send.
3. **Keep today's behaviour** and document that `explicitNulls` decides.

Option 1 is the only one that is correct for `PATCH`. Its cost is the type change on the read side, where the
distinction is rarely interesting; limiting it to request models is possible where a model is request-only (inline
bodies, write variants), not where one schema is shared.

## Fix slice

Depends on the decision. For option 1: typed marks a property as optional and nullable (it already knows both
facts); the renderer emits the tri-state type and its serializer for those properties, and the flattened `invoke`
parameter takes the same type.

## Proof

- Replace the characterization test by a contract: for both `Json` configurations, unset sends no key, explicit null
  sends `null`, a value sends the value; decoding absent, `null` and a value round-trips.
- MockEngine runtime test for a `PATCH` with a flattened body, asserting the body text for the three states.
- integration-tests: `github.json` has the construct in 37 operations; the example round-trip harness would cover the
  decode side.
