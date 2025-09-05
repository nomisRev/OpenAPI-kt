# Proposal: Support OpenAPI String formats in typed module

Author: Junie (JetBrains)
Date: 2025-09-01
Status: Draft proposal for review (no code changes implemented)

## Motivation
OpenAPI allows `type: string` to be refined by `format` and `pattern`, and to be constrained by `minLength`/`maxLength`. Today, the typed module:
- Collects `minLength`/`maxLength`/`pattern` via `Constraints.Text` and generates runtime checks (good)
- Special-cases `format: binary` -> `Model.OctetStream` (good)
- Ignores other built-in and widely-used formats such as `date`, `date-time`, `password`, `byte`, and open values like `email`, `uuid`, `uri`, `hostname`, `ipv4`, `ipv6` (missing)

This proposal defines how to represent string formats in the typed model, how to map them in code generation, and how to stay backward compatible while enabling users to opt into stronger typing and/or validation.

## Current State (Summary)
- parser:
  - `Schema.format: String?`
  - `Format.kt` has constants including `byte`, `binary`, `date`, `datetime`, `password` (note: `datetime` vs OpenAPI `date-time` mismatch).
- typed:
  - `OpenAPITransformer` maps string+binary to `Model.OctetStream`, otherwise to `Model.Primitive.String` with `Constraints.Text`.
  - No model surface for `format` information beyond the `binary` special-case.
- generation:
  - Maps `Model.Primitive.String` to Kotlin `String`.
  - Enforces `Constraints.Text` (length and pattern) via generated `require` checks.
  - No serializers or mappings for `date`, `date-time`, `byte`, etc.

## Goals
- Represent string `format` in the typed model so downstream tooling can act upon it.
- Provide sensible, opt-in mappings from OpenAPI formats to Kotlin types and/or validations.
- Keep backward compatibility by default (i.e., unchanged output and behavior unless opted in).
- Support open/custom formats without breaking tools that don’t recognize them.

## Non-Goals
- Enforcing full RFC-level validation for all formats by default across all targets.
- Changing default behavior for existing users without an explicit opt-in.

## Design Overview
1. Capture `format` in typed Model for `type: string` (beyond `binary`).
2. Provide an extensible set of recognized formats in typed (sealed class) with an open/custom fallback.
3. Update transformation to populate this new field and to continue special-casing `binary` to `Model.OctetStream`.
4. In generation, allow optional mapping from these formats to Kotlin types and/or validations.
5. Maintain defaults: no change in generated types unless the user enables the new mapping/validation options.

## Detailed Design

### 1) New typed representation for string formats
Add a new sealed type in `typed`:

```text
sealed interface StringFormat {
  // Built-in OpenAPI formats (plus common de-facto ones)
  data object Date : StringFormat           // "date"
  data object DateTime : StringFormat       // "date-time" (and alias "datetime")
  data object Password : StringFormat       // "password"
  data object ByteBase64 : StringFormat     // "byte"
  data object Binary : StringFormat         // "binary" (still mapped to OctetStream)

  // Common non-standard but widely used
  data object Email : StringFormat          // "email"
  data object Uuid : StringFormat           // "uuid"
  data object Uri : StringFormat            // "uri"
  data object Hostname : StringFormat       // "hostname"
  data object Ipv4 : StringFormat           // "ipv4"
  data object Ipv6 : StringFormat           // "ipv6"

  // Fallback for any unknown or custom format
  data class Open(val value: String) : StringFormat
}
```

Extend `Model.Primitive.String` to carry `format: StringFormat?`:

```text
data class Model.Primitive.String(
  val default: kotlin.String?,
  override val description: kotlin.String?,
  val constraint: Constraints.Text?,
  val format: StringFormat?,            // NEW
) : Model.Primitive
```

Rationale:
- Minimal structural change: we keep using `Model.Primitive.String` for non-binary strings and annotate it with format info.
- `Model.OctetStream` remains the representation for `binary` payloads (as today).
- Open/custom formats are preserved.

### 2) Transformation logic (typed/OpenAPITransformer)
Update the `Type.Basic.String` branch:

- Keep existing `format == "binary"` -> `Model.OctetStream` behavior.
- Else, construct `Model.Primitive.String` with `format` parsed from `Schema.format`:
  - Recognize both `"date-time"` and the current alias `"datetime"` (backward compatibility with current `Format.kt`).
  - Map known values to `StringFormat` entries; otherwise, use `StringFormat.Open(value)`.
  - Continue to supply `Constraints.Text(schema)`.

Pseudocode snippet:

```text
val fmt = when (format?.lowercase()) {
  "binary" -> return Model.OctetStream(description.get())
  "byte" -> StringFormat.ByteBase64
  "date" -> StringFormat.Date
  "date-time", "datetime" -> StringFormat.DateTime
  "password" -> StringFormat.Password
  "email" -> StringFormat.Email
  "uuid" -> StringFormat.Uuid
  "uri" -> StringFormat.Uri
  "hostname" -> StringFormat.Hostname
  "ipv4" -> StringFormat.Ipv4
  "ipv6" -> StringFormat.Ipv6
  null -> null
  else -> StringFormat.Open(format)
}
Model.Primitive.String(default(...), description.get(), Constraints.Text(this), fmt)
```

Note: No behavioral change by default downstream; we only record the format.

### 3) Parser format constants
- Align `parser/Format.kt` with the spec by changing `datetime` -> `date-time`.
- Provide a deprecated alias `const val datetime = "datetime"` for backward compatibility in code that references it directly (if any), and accept both spellings in transformer as above. (Implementation detail; we can also just accept both in transformer without adding alias if preferred.)

### 4) Generation: opt-in mappings and validations
Introduce new optional behaviors controlled via `GenerationConfig` (and plugin DSL):

Config additions (names tentative):

```text
data class GenerationConfig(
  ...,
  val mapStringFormats: Boolean = false,          // default: off (BC)
  val dateType: DateType = DateType.KotlinxLocalDate,
  val dateTimeType: DateTimeType = DateTimeType.KotlinxInstant,
  val uuidType: UuidType = UuidType.String,
  val enableFormatValidation: Boolean = false,    // generate require checks for known formats
)

enum class DateType { KotlinxLocalDate, String }
enum class DateTimeType { KotlinxInstant, KotlinxLocalDateTime, String }
enum class UuidType { String, KotlinxUuid /* or benasher44 Uuid */ }
```

Type mapping rules when `mapStringFormats = true`:
- `StringFormat.Date` ->
  - `kotlinx.datetime.LocalDate` if `dateType = KotlinxLocalDate`
  - `String` if `dateType = String`
- `StringFormat.DateTime` ->
  - `kotlinx.datetime.Instant` if `dateTimeType = KotlinxInstant` (default)
  - `kotlinx.datetime.LocalDateTime` if selected
  - `String` if selected
- `StringFormat.ByteBase64` -> `ByteArray` with custom JSON string Base64 serializer (see below)
- `StringFormat.Password` -> `String` (UI hint only)
- `StringFormat.Email`, `Uuid`, `Uri`, `Hostname`, `Ipv4`, `Ipv6` -> by default remain `String`. Optionally:
  - `Uuid` can map to a multiplatform UUID type if we agree on a dependency (see Open Questions).
- `StringFormat.Open` -> `String`

Validation rules when `enableFormatValidation = true` (applies only if the property is a `String` at generation time):
- `Email` -> regex check (simple RFC5322-compatible subset)
- `Uuid` -> regex check for canonical 8-4-4-4-12 hex
- `Uri` -> simple parse check (optional; may be omitted for strict multiplatform compatibility)
- `Hostname` -> RFC 1123-like regex
- `Ipv4`/`Ipv6` -> regex checks
- For `Date`/`DateTime` left as `String`: attempt parse with `kotlinx.datetime` in a try-catch, or regex for RFC 3339; opt-in due to size/target concerns.

Note: `minLength`, `maxLength`, and `pattern` constraints currently generate runtime checks. When we map to non-String types (e.g., `LocalDate`), length/pattern no longer apply; proposed behavior: drop those text constraints for such properties and, optionally, emit a code comment indicating they were ignored due to type mapping.

#### Base64 serializer for `byte`
OpenAPI `format: byte` is a base64-encoded string. We should generate/ship a serializer:

```text
@Serializable(with = ByteArrayBase64Serializer::class)
val bytes: ByteArray
```

Implement `ByteArrayBase64Serializer` in `generation` Predef (so it’s available to generated code) using `kotlinx.serialization` and a Base64 implementation (Kotlin/JVM has `java.util.Base64`, but for multiplatform we can use `okio.ByteString` which is already a dependency via `okio`).

- Encode: `okio.ByteString.of(*value).base64()`
- Decode: `ByteString.decodeBase64(string)?.toByteArray()` with error handling.

### 5) Dependencies
If `mapStringFormats = true`:
- `kotlinx-datetime` (multiplatform) for `LocalDate`, `Instant`, `LocalDateTime` and parsing. Add as a dependency where needed (runtime of generated code or referenced types), or generate code that expects the user to have it (preferred: add to generator’s emitted module or document requirement).
- For UUID mapping (if we choose to support a non-String type), we should agree on a multiplatform library. Common choices:
  - `com.benasher44:uuid` (widely used, KMP-ready)
  - If we prefer String by default, we can avoid this dependency initially.
- No additional dependencies for validation if we use regex.

### 6) Backward compatibility
- Default behavior unchanged: `mapStringFormats = false` and `enableFormatValidation = false` keep current output (Strings, except `binary`).
- `binary` behavior unchanged.
- Accept both `"date-time"` and `"datetime"` during transformation; no breaking change for existing specs using the non-standard `datetime`.

### 7) Tests
Add tests without changing existing ones:

Typed tests:
- Map `type: string, format: binary` -> `Model.OctetStream` (existing) + ensure others stay `Primitive.String(format = …)`
- Populate `format` for: `date`, `date-time`/`datetime`, `password`, `byte`, `email`, `uuid`, `uri`, `hostname`, `ipv4`, `ipv6`, and unknown format -> `Open(value)`

Generation tests:
- With default config, `StringFormat.*` still becomes `String` (no changes); existing tests continue to pass.
- With `mapStringFormats = true`:
  - `date` -> `LocalDate`, `date-time` -> `Instant` types appear in generated code and compile.
  - `byte` -> `ByteArray` with `ByteArrayBase64Serializer` referenced; serializer code compiles.
- With `enableFormatValidation = true` and String types: generated `require` checks compile and behave as expected for valid/invalid examples.

### 8) Migration & Documentation
- Document new `GenerationConfig` fields and the plugin DSL additions (e.g., `formatMappings { ... }` or simple booleans).
- Mention that length/pattern are ignored if a string is mapped to a non-String type.
- Provide examples in README.

## Alternatives Considered
- Introducing new `Model.Primitive` subclasses for each formatted string (e.g., `LocalDate`, `Instant`, `Uuid`):
  - Heavier change to the model and generator; less flexible for open/custom formats.
  - The proposed `format` attribute is simpler and more extensible.
- Always mapping `date-time` to `String` with regex validation only:
  - Avoids new dependencies but misses type safety that many users desire.

## Open Questions for Review
1. For `date-time`, do we prefer `Instant` (default) or `LocalDateTime` by default when mapping?
2. Do we want to support a UUID type mapping in v1 of this feature, and if so, which library (e.g., `com.benasher44:uuid`)?
3. Should we offer URI mapping to a type (e.g., Ktor’s `Url`) or keep it as `String` for multiplatform safety?
4. Are we comfortable adding `kotlinx-datetime` as a hard dependency for generated code when format mapping is enabled, or should it be user-provided?
5. Should we deprecate the `parser/Format.kt` constant `datetime` in favor of `date-time` or simply accept both without deprecation noise?

## Implementation Steps (once approved)
1. typed module:
   - Add `StringFormat` sealed interface and extend `Model.Primitive.String` with `format: StringFormat?`.
   - Update `OpenAPITransformer` to populate format and keep existing `binary` handling.
   - Add tests covering formats.
2. parser module:
   - Optionally adjust `Format.kt` to align with `date-time` and keep alias acceptance.
3. generation module:
   - Extend `GenerationConfig` with new options.
   - Update `OpenAPIContext`/plugin to pass options.
   - Update `Model.toTypeName` to consider format when `mapStringFormats = true`.
   - Add `ByteArrayBase64Serializer` to `Predef.kt` and integrate when needed.
   - Add conditional validation codegen when `enableFormatValidation = true`.
   - Add tests for type mapping and validation generation.
4. plugin module:
   - Extend DSL to set new `GenerationConfig` flags.
   - Tests for plugin wiring (optional).

## Acceptance Criteria
- With defaults, no observable changes for current users and tests stay green.
- When enabled, `date`/`date-time`/`byte` map to desired Kotlin types with working serializers, and tests cover these.
- Unknown formats are preserved as open values and do not break generation.
- Documentation updated.
