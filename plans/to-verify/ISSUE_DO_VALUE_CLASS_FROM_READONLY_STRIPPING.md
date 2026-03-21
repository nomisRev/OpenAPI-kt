# ISSUE: `readOnly` Stripping Produces `@JvmInline value class` for Multi-Field Schemas

## Problem

When a schema has multiple properties but the `readOnly`-stripping logic removes all but one,
the renderer receives a `Model.Object` with exactly one rendered property. The single-property
branch in `ObjectRenderer.kt` unconditionally promotes this to a `@JvmInline value class`:

```kotlin
// ObjectRenderer.kt ~line 80-93
allProperties.size == 1 && additionalProperty == null -> {
    TypeSpec.classBuilder(simpleName)
        .addModifiers(KModifier.VALUE)
        .primaryConstructor(...)
        .apply { if (KmpTarget.JVM in config.targets) addAnnotation(JvmInline::class) }
}
```

The result, for a schema like `LocaleDescriptor` (has `id`, `locale`, `language`, `community`,
`name` — all but `name` are `readOnly`) looks like:

```kotlin
// WRONG — generated as a value class wrapping a single surviving field
@JvmInline
@Serializable
public value class LocaleDescriptor(
  public val name: String? = null,
)
```

while the Read variant correctly captures all five fields:

```kotlin
@Serializable
public data class LocaleDescriptorRead(
  public val id: String? = null,
  public val locale: String? = null,
  public val language: String? = null,
  public val community: Boolean? = null,
  public val name: String? = null,
  @SerialName("${'$'}type") public val type: String? = null,
)
```

## Why This Matters

A `@JvmInline value class` is semantically and behaviourally different from a `data class`:

- **Runtime erasure** — the wrapper type is erased at the JVM level; the wrapped value is used
  directly. Polymorphic kotlinx.serialization serializers that inspect the runtime type will
  fail or produce incorrect output.
- **Single-field restriction** — value classes cannot be used as a polymorphic base or subtype
  in a sealed interface without an explicit custom serializer.
- **Equals / hashCode** — value class equality is structural over the single wrapped value,
  erasing identity that the original multi-field schema may have carried.
- **Misleading API** — callers see a type that appears to consist of only one field, when the
  intent was to represent a writable subset of a richer schema.

The `value class` optimisation only makes sense for schemas that are **single-field by design**
in the spec, not schemas that became single-field as a side-effect of stripping.

## Affected Schemas (YouTrack spec)

| Schema | Surviving write field | Total fields in spec |
|---|---|---|
| `LocaleDescriptor` | `name` | 5 (`id`, `locale`, `language`, `community`, `name`) |
| `LocaleDescriptorWrite` | `name` | same |
| `NotificationSettings` | `emailSettings` | 2 (`id`, `emailSettings`) |
| `TimeTrackingUserProfile` | `periodFormat` | 2 (`id`, `periodFormat`) |
| `TimeTrackingUserProfileWrite` | `periodFormat` | same |

Sealed-hierarchy subtypes are also affected:

| Sealed subtype | Surviving write field | Read fields |
|---|---|---|
| `User.Default` | `profiles` | 12+ (login, fullName, email, …) |
| `User.VcsUnresolvedUser` | `profiles` | same |
| `User.Me` | `profiles` | same |

## Desired Outcome

A `Model.Object` that had more than one property in the spec before stripping must render as a
`data class`, not a `value class`, even when only one property survives:

```kotlin
// CORRECT — data class with one constructor parameter
@Serializable
public data class LocaleDescriptor(
  public val name: String? = null,
)
```

The `value class` optimisation in the renderer should only be applied when the schema had exactly
one property **in the spec itself** — i.e. before any Read/Write context stripping occurred.
One way to carry this signal is a flag on `Model.Object` set by the registry during construction,
analogous to the signal proposed in `ISSUE_DO_EMPTY_DATA_OBJECT_ALL_READONLY.md`.

## Affected Module

- `renderer` — `ObjectRenderer.kt` (`toTypeSpec`, the `allProperties.size == 1` branch)
- `typed` — wherever context-specific `Model.Object` variants are constructed after stripping

## Acceptance Criteria

1. A schema whose original spec has more than one property renders the stripped variant as a
   `data class`, not a `@JvmInline value class`, even if stripping leaves exactly one field.
2. A schema that has exactly one property in the spec (no stripping involved) still renders as
   a `value class` (the existing behaviour is correct for that case).
3. Sealed subtypes that become single-field after stripping are also `data class`, not
   `value class`.
4. `./gradlew :renderer:jvmTest` passes.
5. `./gradlew :youtrack:generateOpenApi :youtrack:compileKotlin` passes in the integration tests.
