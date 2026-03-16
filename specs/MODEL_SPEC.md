# Model Specification

How OpenAPI schemas translate to Kotlin model types, including nullability, defaults, annotations, and serialization.

## Type Mapping

### Primitives

| OpenAPI `type` | OpenAPI `format` | Kotlin Type | Serializer |
|---|---|---|---|
| `string` | _(none)_ | `String` | `String.serializer()` |
| `string` | `uuid` | `kotlin.uuid.Uuid` | `Uuid.serializer()` |
| `string` | `date` | `kotlinx.datetime.LocalDate` | `LocalDate.serializer()` |
| `string` | `date-time` | `kotlinx.datetime.LocalDateTime` | `LocalDateTime.serializer()` |
| `string` | `binary` | `ByteArray` | `ByteArraySerializer()` |
| `integer` | `int32` | `Int` | `Int.serializer()` |
| `integer` | `int64` / _(none)_ | `Long` | `Long.serializer()` |
| `number` | `float` | `Float` | `Float.serializer()` |
| `number` | `double` / _(none)_ | `Double` | `Double.serializer()` |
| `boolean` | — | `Boolean` | `Boolean.serializer()` |
| _(null / empty)_ | — | `Unit` | `Unit.serializer()` |

### Special Types

| OpenAPI Pattern | Kotlin Type | Serializer |
|---|---|---|
| `{}` (empty schema) | `JsonElement` | `JsonElement.serializer()` |
| `type: array, items: {}` | `JsonArray` | `JsonArray.serializer()` |
| `type: object` with no properties + `additionalProperties: true` | `JsonObject` | `JsonObject.serializer()` |
| `type: object` with no properties + `additionalProperties: { schema }` | Resolves to the schema type | Schema's serializer |

### Complex Types

| OpenAPI Pattern | Kotlin Type | Section |
|---|---|---|
| `type: object` | `data class` / `value class` / `data object` | [Object](#object) |
| `type: array` | `List<T>` | [Collection](#collection) |
| `enum` | `enum class` | [Enum](#enum) |
| `oneOf` / `anyOf` | `sealed interface` | [Union](#union) |
| `allOf` + `discriminator` | `sealed interface` with inheritance | [Discriminated Object](#discriminated-object) |
| `$ref` | `Model.Reference` (resolved to target type) | [Reference](#reference) |

---

## Object

Generated from `type: object` schemas. The shape depends on property count and `additionalProperties`.

### Shape Rules

| Properties | additionalProperties | Generated As |
|---|---|---|
| 0 | `false` / absent | `data object Name` |
| 1 | `false` / absent | `value class Name(val prop: T)` |
| 1 | `true` or `{ schema }` | `data class Name(val prop: T, val additional: ...)` |
| 2+ | any | `data class Name(...)` |

### Nullability

A property type is nullable (`T?`) when **either**:
- The property schema has `nullable: true`, **OR**
- The property is **not** listed in `required`

```kotlin
// required + not nullable → T
val name: String

// not required → T? = null
val email: String? = null

// required + nullable + no default → T? (no default!)
val tag: String?

// required + nullable + has default → T? = null
val label: String? = null
```

The key distinction: `required + nullable` without a default gets **no implicit `= null`**, forcing the caller to explicitly provide the value.

### Default Values

When `!isRequired || isNullable` the property gets `= null`.

When `isRequired && hasDefault` the `@Required` annotation is added. This tells kotlinx.serialization to always include the field during serialization even though a default exists.

Default values are tracked in the `Model` via `Default<A>`:
- `Default.Value(value)` — an explicit default from the schema
- `Default.Null` — explicit `null` default (only for nullable schemas)

Types that support defaults: `String`, `Int`, `Long`, `Float`, `Double`, `Boolean`, `Enum`, `Collection`.
Types that do **not** support defaults: `Object`, `Union`, `Reference`, `Uuid`, `Date`, `DateTime`, `ByteArray`, `FreeFormJson`, `DiscriminatedObject`.

### Annotations

```kotlin
@Serializable                         // Always present
data class Foo(
    @SerialName("json_name")          // When JSON name ≠ Kotlin param name
    val jsonName: String,

    @Required                         // When isRequired && hasDefault
    val status: String,
)
```

When the object has `additionalProperties`, extra annotations are added:

```kotlin
@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = Foo.Serializer::class)
data class Foo(...)
```

### Additional Properties

| `additionalProperties` | Generated Property |
|---|---|
| `false` / absent | _(none)_ |
| `true` | `val additional: JsonObject? = null` |
| `{ schema }` | `val additional: Map<String, T>? = null` |

When present, a custom `object Serializer : KSerializer<Foo>` is generated inside the class body. It:
- Serializes known properties by name, then merges additional properties into the JSON object
- Deserializes by extracting known property keys, treating remaining keys as additional properties

### Inline / Nested Types

Properties whose schemas are inline (not `$ref`) generate nested classes inside the parent:

```kotlin
@Serializable
data class Foo(val nested: Nested) {
    @Serializable
    data class Nested(val value: String)
}
```

This includes inline objects from properties and from `additionalProperties: { schema }`.

---

## Collection

Generated from `type: array` schemas.

### Rendering

- Inline: `List<InnerType>`
- If `inner` is `FreeFormJson`: `JsonArray` (not `List<JsonElement>`)
- Top-level named collection (component schema): wrapped in a `value class`

```kotlin
@Serializable
@JvmInline
value class Tags(val items: List<String>)
```

The `items` property name is always `items` for top-level collection wrappers.

### Serializer

- `ListSerializer(inner.serializer())` for typed items
- `JsonArray.serializer()` when items are free-form

---

## Enum

Generated from schemas with `enum` values.

### Shape

```kotlin
@Serializable
enum class Status {
    Active,
    @SerialName("in-progress") InProgress,
    @SerialName("DONE") Done;
}
```

### Value Naming

Raw enum values are converted to PascalCase. `@SerialName` is added only when the raw value differs from the PascalCase name.

Special mappings:
- `"*"` → `Star`
- `"/"` → `Slash`
- Invalid identifiers are backtick-escaped: `` `+1` ``
- On JS targets, invalid JS names get an additional `@JsName("_Name")` annotation

### Inner Type

Enums track their `inner` model type (usually `Model.Primitive.String`), which is used to determine the serializer for open-enum patterns.

---

## Union

Generated from `oneOf` / `anyOf` schemas. Produces a `sealed interface` with case subtypes.

### With Discriminator

When the schema has a `discriminator` property:

```kotlin
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
sealed interface Event {
    @SerialName("click")
    @Serializable
    data class Click(val x: Int, val y: Int) : Event

    @SerialName("key")
    @Serializable
    data class Key(val code: String) : Event
}
```

Kotlinx.serialization handles deserialization natively via `@JsonClassDiscriminator`.

### Without Discriminator

A custom `object Serializer : KSerializer<Union>` is generated. It uses `attemptDeserialize` which tries each case in priority order, catching failures and falling through.

```kotlin
@Serializable(with = Union.Serializer::class)
sealed interface Union {
    @Serializable @JvmInline
    value class CaseString(val value: String) : Union

    @Serializable @JvmInline
    value class CaseInt(val value: Int) : Union

    object Serializer : KSerializer<Union> { ... }
}
```

### Case Naming

Cases are named using the following priority:
1. **Discriminator value** (if present)
2. **Inline object**: named from `NamingContext.UnionCase` (derived from property names, joined with "And", max 90 chars)
3. **Top-level reference**: `Case{TypeName}` (e.g., `CaseUser`)
4. **Primitive/special type**: `CaseString`, `CaseInt`, `CaseLong`, `CaseFloat`, `CaseDouble`, `CaseBoolean`, `CaseUuid`, `CaseDate`, `CaseDateTime`, `CaseBinary`, `CaseJsonElement`
5. **Unit**: `Empty` (rendered as `data object Empty`)
6. **Collection**: pluralizes inner name (e.g., `CaseStrings`, `CaseUsersList`)

### Case Rendering

There are two fundamentally different rendering strategies: **wrapped** (value class wrapper) and **inlined** (directly implements the sealed interface).

#### Wrapped cases (value class)

Primitives, special types, references, collections, top-level objects/enums, nested unions, and discriminated objects are wrapped in a `value class`:

```kotlin
@Serializable @JvmInline
value class CaseString(val value: String) : Union
```

The wrapper holds a `val value: T` and the serializer unwraps/wraps during (de)serialization:
- **serialize**: `encoder.encodeSerializableValue(String.serializer(), value.value)` — accesses `.value`
- **deserialize**: `CaseString(decodeFromJsonElement(String.serializer(), it))` — wraps in constructor

#### Inlined cases (direct interface implementation)

(non-top-level, defined within the union schema) are rendered directly as subtypes of the sealed interface — they implement it without a value class wrapper. This is the key structural difference.

**Inline object** 

```kotlin
// Object implements Union directly, no value class wrapper
@Serializable
data class AgeAndName(val age: Int, val name: String) : Union
```

**Inline enum**

```kotlin
// Enum implements Union directly, no value class wrapper
@Serializable
enum class AscOrDesc : Union {
    @SerialName("asc") Asc, @SerialName("desc") Desc;
}
```

Nested inline Union & DiscriminatedObject subtypes cannot be supported??

**Empty inline object** (0 properties):

```kotlin
@Serializable
data object Empty : Union
```

**Single-property inline object** (no additionalProperties):

```kotlin
@Serializable @JvmInline
value class WithProp(val prop: String) : Union
```

#### Serialization difference

The inlined vs wrapped distinction affects the custom serializer:

| | Serialize | Deserialize |
|---|---|---|
| **Wrapped** | `encoder.encodeSerializableValue(T.serializer(), value.value)` | `CaseX(decodeFromJsonElement(T.serializer(), it))` |
| **Inlined** | `encoder.encodeSerializableValue(CaseName.serializer(), value)` | `decodeFromJsonElement(CaseName.serializer(), it)` |

Inlined cases use their own generated serializer directly (no unwrapping). Wrapped cases unwrap `.value` for serialization and wrap the result in the constructor for deserialization.

#### Decision logic

```
Case.render() =
  if model is ContextHolder AND isTopLevel  → valueClass()    // wrapped: top-level ref
  if model is Primitive.Unit                → data object Empty
  if model is Reference                     → valueClass()    // wrapped
  if model is Primitive/Date/DateTime/...   → valueClass()    // wrapped
  if model is Union/DiscriminatedObject     → valueClass()    // wrapped
  if model is Collection                    → valueClass()    // wrapped
  if model is Object                        → Object.render(parentClass = union)  // INLINED
  if model is Enum                          → Enum.render(parentClass = union)    // INLINED
```

The critical check is `isTopLevel()` — a `ContextHolder` (Object/Enum/Union/DiscriminatedObject) whose context head is a top-level `Reference` gets wrapped in a value class. Only inline (non-top-level) objects and enums are rendered directly as union subtypes.

### Deserialization Order

Cases are ordered by specificity to prevent wider types from swallowing narrower ones:

1. Objects without additionalProperties (more properties → higher priority)
2. Objects with typed additionalProperties schema
3. Objects with additionalProperties allowed
4. DiscriminatedObjects
5. Nested Unions
6. Enums (before String to avoid being swallowed)
7. Collections
8. References
9. Primitives: `Int` → `Long` → `Float` → `Double` → `Boolean` → `Unit`
10. String-like: `Uuid` → `Date` → `DateTime` → `ByteArray`
11. `String` (swallows other string-representable types)
12. `FreeFormJson` / `JsonElement` (last resort — swallows everything)

### Open Enum Pattern

When a union has exactly two cases — one `Enum` and one `String` — it generates an "open enum" serializer that matches known values to enum entries and falls back to the string wrapper for unknown values.

---

## Discriminated Object

Generated from `allOf` + `discriminator` patterns where a base schema defines common (abstract) properties and subtypes extend via `allOf`.

```kotlin
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
sealed interface User {
    val id: Long                       // abstract property (from base schema)
    // If abstract property from base schema but types don't match, typically JsonElement with more specific downstream types.
    // This doesn't typecheck so exclude it as an abstract property.

    @SerialName("anonymous")
    @Serializable
    @JvmInline
    value class Anonymous(override val id: Long) : User

    @SerialName("registered")
    @Serializable
    data class Registered(
        override val id: Long,         // overrides abstract property
        val email: String,             // subtype-specific property
    ) : User
}
```

### Structure

- **Abstract properties**: declared as `val` in the sealed interface — shared across all subtypes
- **Subtypes**: each rendered as an Object (data class / value class) that `override`s abstract properties and adds its own
- **Discriminator**: `@JsonClassDiscriminator` on the sealed interface, `@SerialName` on each subtype with the discriminator mapping value
- Subtypes follow the same shape rules as [Object](#object) (data class / value class / data object)

---

## Reference

`Model.Reference` represents a `$ref` pointing to a named component schema. During rendering, references resolve to the target type's `TypeName`.

- Component schemas render under `{package}.model`
- Path/route-level schemas render under `{package}.api`

---

## Naming

All naming flows through `splitToWords()` → `toPascalCase()` / `toCamelCase()`.

### Parameter Names (`toParamName`)

Converts JSON property names to Kotlin parameter names (camelCase).

```kotlin
fun String.toParamName(): String {
    val sanitised = replace("$", "")      // Strip all $ characters
    val camelCase = if (negativeNumberRegex.matches(sanitised)) sanitised  // Preserve -1, -2.5, etc.
    else sanitised.toCamelCase()
    if (camelCase.isValidParamName()) camelCase else "`$camelCase`"        // Backtick-escape if needed
}
```

Edge cases:
- `$type` → `type` (`$`-prefixed property)
- `$foo_bar` → `fooBar` (`$` stripped, then camelCased)
- `-1` → `` `-1` `` (negative number preserved as-is, backtick-escaped)
- `when` → `` `when` `` (Kotlin keyword, backtick-escaped)
- `some_name` → `someName` (standard snake_case conversion)

`@SerialName("original_name")` is emitted when `paramName != baseName`.

### Enum Value Names (`toEnumValueName`)

Converts raw enum string values to Kotlin enum entry names (PascalCase).

```kotlin
fun toEnumValueName(rawToName: String): String {
    if (rawToName == "*") return "Star"        // Special: wildcard
    if (rawToName == "/") return "Slash"        // Special: slash
    val pascalCase = if (negativeNumberRegex.matches(rawToName)) rawToName  // Preserve -1, -2.5
                     else rawToName.toPascalCase()
    return if (pascalCase.isValidClassname()) pascalCase
    else {
        val sanitise = pascalCase
            .run { if (startsWith("[")) drop(1) else this }    // Strip leading [
            .run { if (endsWith("]")) dropLast(1) else this }  // Strip trailing ]
        if (sanitise.isValidClassname()) sanitise else "`$sanitise`"
    }
}
```

Edge cases:
- `"*"` → `Star`
- `"/"` → `Slash`
- `"-1"` → `` `-1` `` (negative number, backtick-escaped)
- `"in-progress"` → `InProgress` (hyphen is a word boundary)
- `"[something]"` → `Something` (brackets stripped)
- `"+1"` → `` `+1` `` (invalid identifier, backtick-escaped)
- `null` values in enum → mapped to string `"null"` → `Null`

On JS targets, enum names starting with backtick+digit (e.g., `` `1xx` ``) also get `@JsName("_1xx")`.

### String Literal Escaping (`stringValue`)

Used for `@SerialName` values and `@JsonClassDiscriminator` values. Handles strings containing `$` by using Kotlin raw-string dollar syntax:

```kotlin
fun String.stringValue(): String {
  var max = 0; var count = 0
  for (c in this) if (c != '$' || count++ <= max) Unit else { max = count }
  val dollars = if (count > 0) "$".repeat(count + 1) else ""
  return "$dollars\"$this\""
}
```

- `"foo"` → `"foo"` (plain string)
- `"$type"` → `$$"$type"` (dollar-string to avoid Kotlin interpolation)

### Kotlin Keyword Escaping

The full keyword set (27 words) that triggers backtick-escaping:

`as`, `break`, `class`, `continue`, `do`, `else`, `false`, `for`, `fun`, `if`, `in`, `interface`, `is`, `null`, `object`, `package`, `return`, `super`, `this`, `throw`, `true`, `try`, `typealias`, `typeof`, `val`, `var`, `when`, `while`

**Parameter names**: case-insensitive keyword check (`isValidParamName`).
**Class names**: only checked against identifier regex, not keywords (`isValidClassname`).
**Package paths**: `sanitize()` escapes each segment independently with backticks.

### Identifier Validation

```kotlin
private val classNameRegex = Regex("^[a-zA-Z_][a-zA-Z\\d_]*$")

fun String.isValidClassname(): Boolean = classNameRegex.matches(this)
fun String.isValidParamName(): Boolean = classNameRegex.matches(this) &&
    KOTLIN_KEYWORDS.none { it.equals(this, ignoreCase = true) }
```

### Union Case Naming (typed module — `Union.kt`)

Union case names are determined in the **typed** module during schema transformation, not in the renderer. The logic has two paths:

**Primary path** (`NamingContext.unionCase` at line 62):

| Priority | Condition | Name |
|---|---|---|
| 1 | Single subtype | `"Case"` |
| 2 | Has discriminator mapping | discriminator value |
| 3 | Has `type`/`event`/`$type` property with single-value enum | that enum value |
| 4 | Is open-enum case (n-1 string + 1 string enum) | joined enum values with "Or" (max 90 chars), fallback `"CaseEnum"` |
| 5 | Is the non-reference case when n-1 cases are references | `"Else"` |
| 6 | Fallback | `schema.unionCaseName(index)` |

**`unionCaseName` fallback** (line 153):

| Schema Type | Name |
|---|---|
| `object` | Property names joined with "And" (max 90 chars), e.g., `"AgeAndName"` |
| `object` (name too long) | Named index: `"One"`, `"Two"`, ..., `"Sixteen"`, then `"Case{index}"` |
| `integer` (int32) | `"CaseInt"` |
| `integer` (other) | `"CaseLong"` |
| `number` (float) | `"CaseFloat"` |
| `number` (other) | `"CaseDouble"` |
| `boolean` | `"CaseBoolean"` |
| `string` + enum | Values joined with "Or" (max 90 chars), e.g., `"AscOrDesc"` |
| `string` (binary) | `"CaseBinary"` |
| `string` (uuid) | `"CaseUuid"` |
| `string` (date) | `"CaseDate"` |
| `string` (date-time) | `"CaseDateTime"` |
| `string` (other format) | `"Case{Format}"` (PascalCased) |
| `string` (no format) | `"CaseString"` |
| `array` | `"CaseArray"` |
| `null` | `"CaseNull"` |
| Type.Array (union types) | Types joined with "Or" prefix "Case", e.g., `"CaseStringOrInt"` |
| `null` type | Named index fallback |

**Collection handling**: When a union case is an array, the naming recurses into `items` — if items are a `$ref`, the reference name is used directly.

### Union Case Naming (renderer module — `UnionRenderer.kt`)

The renderer has a **separate** naming path (`unionClassName`) that maps `Model` types to display names. This must stay consistent with the typed module's `NamingContext.UnionCase` values:

| Model Type | Renderer Name |
|---|---|
| `Primitive.String` | `CaseString` |
| `Primitive.Int` | `CaseInt` |
| `Primitive.Long` | `CaseLong` |
| `Primitive.Float` | `CaseFloat` |
| `Primitive.Double` | `CaseDouble` |
| `Primitive.Boolean` | `CaseBoolean` |
| `Primitive.Unit` | `Empty` |
| `ByteArray` | `CaseBinary` |
| `Date` | `CaseDate` |
| `DateTime` | `CaseDateTime` |
| `Uuid` | `CaseUuid` |
| `FreeFormJson` | `CaseJsonElement` |
| `Reference` | `Case{TypeName}` |
| `DiscriminatedObject` | `Case{TypeName}` |
| `Union` (nested) | `Case{TypeName}` |
| Top-level `Object` | `Case{TypeName}` |
| Top-level `Enum` | `Case{TypeName}` |
| Inline `Object`/`Enum` | Read from `NamingContext.UnionCase.value` |
| `Collection` | Pluralize inner name (append `s`) or append `List` if already ends with `s` (recursive for nested lists) |

**Collection pluralization logic**: `CaseString` → `CaseStrings`, `CaseStrings` → `CaseStringsList`, `CaseStringsListList`, etc.

### HTTP Status Code Names

Used for sealed return type cases:

```kotlin
private fun HttpStatusCode.sealedCaseName(): String =
    description.split(" ").
```

- `200 OK` → `Ok`
- `204 No Content` → `NoContent` (hardcoded)
- `404 Not Found` → `NotFound`
- `500 Internal Server Error` → `InternalServerError`

### Server Variable Naming

Server descriptions are converted to enum case names:
- Trailing "server" word is stripped: `"Production Server"` → `"Production"`
- Empty/blank results return `null` (server gets no named case)
- Enum names for server variables are deduplicated with numeric suffixes (`Variable2`, `Variable3`, etc.)

### Kotlin String Escaping (`escapeKotlinString`)

Used in server URL template rendering:

| Character | Escaped As |
|---|---|
| `\` | `\\` |
| `"` | `\"` |
| `$` | `\$` |
| newline | `\n` |
| carriage return | `\r` |
| tab | `\t` |

### Class Names

- Component schema names → PascalCase under `{package}.model`
- Path-level inline schemas → PascalCase under `{package}.api`
- Nested schemas → PascalCase, nested inside parent class
- Read/Write context suffixes: `{Name}Request` / `{Name}Response` appended for schemas used in request/response context

### Array Syntax Removal

`dropArraySyntax()` strips `[n]` patterns from names: `"items[0]"` → `"items"`. Used during path/parameter processing.

---

## Platform Annotations

| Annotation | Condition |
|---|---|
| `@JvmInline` | Value classes, only when targeting JVM (`ctx.jvm`) |
| `@JsName("_Name")` | Enum values with invalid JS identifiers, only when targeting JS (`ctx.js`) |
| `@OptIn(ExperimentalUuidApi::class)` | Any class containing a `Uuid` type (directly or nested) |
| `@OptIn(ExperimentalSerializationApi::class)` | Classes using `@JsonClassDiscriminator`, `@KeepGeneratedSerializer`, or `buildSerialDescriptor` |

---

## Import Generation

Imports are tracked by the `Renderer` and emitted per file. Only used imports appear. Key import groups:

| Usage | Imports |
|---|---|
| All models | `kotlinx.serialization.Serializable` |
| SerialName needed | `kotlinx.serialization.SerialName` |
| Required annotation | `kotlinx.serialization.Required` |
| Uuid | `kotlin.uuid.Uuid`, `kotlin.uuid.ExperimentalUuidApi` |
| Date/DateTime | `kotlinx.datetime.LocalDate` / `LocalDateTime` |
| FreeFormJson | `kotlinx.serialization.json.JsonElement` |
| Collections | `kotlinx.serialization.json.JsonArray`, `kotlinx.serialization.builtins.ListSerializer` |
| Additional properties | `kotlinx.serialization.json.JsonObject`, `kotlinx.serialization.json.buildJsonObject` |
| Custom serializers | `KSerializer`, `SerialDescriptor`, `Encoder`, `Decoder`, `JsonDecoder`, `JsonEncoder` |
| Discriminated types | `kotlinx.serialization.json.JsonClassDiscriminator` |
| Value classes (JVM) | `kotlin.jvm.JvmInline` |
