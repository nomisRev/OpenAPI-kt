# ISSUE: Schemas Whose Properties Are All `readOnly` Generate as `data object` Instead of `data class`

## Problem

When a schema has at least one property in the spec but every property carries `readOnly: true`,
the `readOnly`-stripping logic in the typed layer removes all properties from the plain / Write
variant. The renderer then receives a `Model.Object` with zero rendered properties and falls into
the `allProperties.isEmpty()` branch in `ObjectRenderer.kt`:

```kotlin
// ObjectRenderer.kt ~line 79
val builder = when {
    allProperties.isEmpty() -> TypeSpec.objectBuilder(simpleName).addModifiers(KModifier.DATA)
    ...
}
```

This emits a `data object` — a Kotlin singleton. The result looks like:

```kotlin
// WRONG — generated for DateFormatDescriptor (all 4 properties are readOnly)
@Serializable
public data object DateFormatDescriptor
```

while the Read variant is correctly generated as:

```kotlin
@Serializable
public data class DateFormatDescriptorRead(
  public val id: String? = null,
  public val presentation: String? = null,
  public val pattern: String? = null,
  public val datePattern: String? = null,
  @SerialName("${'$'}type") public val type: String? = null,
)
```

## Why This Matters

A `data object` is a compile-time singleton. It cannot hold per-instance field values. Using it
for a schema that represents real JSON payloads breaks deserialization: every decoded instance
resolves to the single object, discarding all field data. It also breaks equality and hashing
semantics that callers may rely on.

The plain (non-suffixed) variant represents the schema in a **Write** context (request body). If
the spec itself is correct — all properties are truly read-only — then a zero-property request
body may be intentional, but it must still be a `data class` (with no constructor parameters) to
satisfy type-safe serialization contracts and to remain consistent with sealed-hierarchy subtypes.

## Affected Schemas (YouTrack spec)

Nine top-level schemas hit this path:

| Schema | All-readOnly fields |
|---|---|
| `DateFormatDescriptor` | `id`, `presentation`, `pattern`, `datePattern` |
| `DurationValue` | `id`, `minutes`, `presentation` |
| `FieldStyle` | `id`, `background`, `foreground` |
| `FieldType` | `id` |
| `PeriodFieldFormat` | `id` |
| `PeriodValue` | `id`, `minutes`, `presentation` |
| `SwimlaneValue` | `id`, `name` |
| `TextFieldValue` | `id`, `text`, `markdownText` |
| `TimeZoneDescriptor` | `id`, `presentation`, `offset` |

The same nine schemas produce empty `data object` Write variants (covered separately in
`ISSUE_DO_WRITE_VARIANT_ZERO_WRITABLE_FIELDS.md`).

Sealed-hierarchy subtypes hit the same branch when all their differentiating fields are
`readOnly`; see `ISSUE_DO_VALUE_CLASS_FROM_READONLY_STRIPPING.md` for the single-property case.

## Desired Outcome

`Model.Object` instances that originate from a schema that **had** properties but had them all
stripped by the Read/Write split must render as `data class` with zero constructor parameters,
not as `data object`:

```kotlin
// CORRECT — zero-property class, not a singleton object
@Serializable
public data class DateFormatDescriptor()
```

The renderer should distinguish "schema had no properties to begin with" (legitimate `data object`)
from "schema had all properties stripped" (must be `data class()`). One way to carry this signal
is a boolean flag on `Model.Object` (e.g. `hadPropertiesBeforeStripping`), set by the registry
when building context-specific variants.

## Affected Module

- `renderer` — `ObjectRenderer.kt` (`toTypeSpec`, the `allProperties.isEmpty()` branch)
- `typed` — wherever context-specific `Model.Object` variants are constructed after stripping
  `readOnly` / `writeOnly` properties

## Acceptance Criteria

1. A schema whose properties are all `readOnly` renders the plain / Write variant as
   `data class SchemaName()` (zero parameters), not `data object SchemaName`.
2. A schema that genuinely has no properties in the spec continues to render as `data object`.
3. The Read variant of any such schema is unaffected.
4. `./gradlew :renderer:jvmTest` passes.
5. `./gradlew :youtrack:generateOpenApi :youtrack:compileKotlin` passes in the integration tests.
