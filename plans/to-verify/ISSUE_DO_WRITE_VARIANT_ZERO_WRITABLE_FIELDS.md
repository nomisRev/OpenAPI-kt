# ISSUE: Write Variants for Schemas With Zero Writable Fields Generate as `data object`

## Problem

When a schema is used in both a Read and a Write context but **all** of its properties are
`readOnly: true`, the Write variant receives zero properties after stripping and falls into the
`allProperties.isEmpty()` branch in `ObjectRenderer.kt`, emitting a `data object`:

```kotlin
// WRONG — generated Write variant when all schema properties are readOnly
@Serializable
public data object DurationValueWrite

@Serializable
public data object GlobalSettingsWrite
```

This is the Write-variant counterpart of `ISSUE_DO_EMPTY_DATA_OBJECT_ALL_READONLY.md`.
That issue covers the plain (non-suffixed) variants; this issue covers the `*Write` variants
that are generated when the same schema is referenced in both a Read and a Write context.

## Why This Matters

A `data object` as a request-body type is semantically wrong:

- **Singleton serialization** — kotlinx.serialization serializes a `data object` as `{}` every
  time, which is coincidentally correct for an empty JSON object. However, a `data object` is a
  singleton: API clients cannot construct distinct "instances" of an empty request body, and
  future spec revisions that add a writable field would require a breaking API change (object →
  class) rather than a backwards-compatible parameter addition.
- **Inconsistent with the Read type** — `DurationValueRead` is a `data class` with `id`,
  `minutes`, and `presentation`. Callers expecting symmetry between Read and Write types will
  find a fundamental structural mismatch.
- **Type-safe construction** — standard API-client idioms build request bodies by instantiating
  a data class. A `data object` breaks this pattern: `DurationValueWrite()` does not compile;
  only `DurationValueWrite` (bare reference) is valid.

In practice, a Write type with no writable fields is a legitimate scenario — the caller simply
has nothing to supply. The correct shape is a zero-parameter `data class`, not a singleton object:

```kotlin
// CORRECT
@Serializable
public data class DurationValueWrite()
```

## Affected Schemas (YouTrack spec)

| Write variant | Reason all fields are absent |
|---|---|
| `DateFormatDescriptorWrite` | All 4 fields are `readOnly` |
| `DurationValueWrite` | All 3 fields are `readOnly` |
| `FieldStyleWrite` | All 3 fields are `readOnly` |
| `FieldTypeWrite` | `id` is `readOnly` |
| `PeriodFieldFormatWrite` | `id` is `readOnly` |
| `PeriodValueWrite` | All 3 fields are `readOnly` |
| `SwimlaneValueWrite` | Both `id` and `name` are `readOnly` |
| `TextFieldValueWrite` | All 3 fields are `readOnly` |
| `TimeZoneDescriptorWrite` | All 3 fields are `readOnly` |
| `GlobalSettingsWrite` | All top-level fields are nested objects with `readOnly` id |
| `NotificationSettingsWrite` | `id` is `readOnly`; `emailSettings` stripped |
| `ProjectColorWrite` | All fields are `readOnly` |

## Relationship to Other Issues

This issue shares a root cause with `ISSUE_DO_EMPTY_DATA_OBJECT_ALL_READONLY.md` (same
`allProperties.isEmpty()` branch, same renderer fix needed). Both issues can be resolved by the
same change: distinguish "no properties by spec design" from "no properties after stripping" and
emit `data class()` for the latter.

The fix proposed in `ISSUE_DO_EMPTY_DATA_OBJECT_ALL_READONLY.md` — carrying a flag on
`Model.Object` to indicate the schema had properties before stripping — would simultaneously fix
both the plain and the Write variants.

## Affected Module

- `renderer` — `ObjectRenderer.kt` (`toTypeSpec`, the `allProperties.isEmpty()` branch)
- `typed` — wherever context-specific `Model.Object` variants are constructed after stripping

## Acceptance Criteria

1. A schema used in a Write context where all properties are `readOnly` renders the Write variant
   as `data class SchemaNameWrite()` (zero parameters), not `data object SchemaNameWrite`.
2. A schema whose Write variant legitimately has no properties **in the spec itself** (no
   properties declared, not stripped) continues to render as `data object`.
3. `./gradlew :renderer:jvmTest` passes.
4. `./gradlew :youtrack:generateOpenApi :youtrack:compileKotlin` passes in the integration tests.
