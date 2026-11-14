# A property and its inline type get the identical name (`val _0: _0`, `val context: context`)

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 5 of 1,547 documents fail to compile (vtex.local:Search-API, edrv.io, meraki.com, getpostman.com, alertersystem.com). At least 4 more have the construct and fail before compilation (mist.com, probely.com, box.com, vtex.local:Profile-System).
**Source spec valid:** yes — property names may start with a digit or a symbol.

## Symptom

```
api/Networks.kt:2026:24: error: conflicting declarations:
data class _0 : Any
            public val _0: _0? = null,
api/Networks.kt:2075: error: conflicting declarations:
val _0: Networks.NetworkIdPath.Clients.Provision.Post.PoliciesBySsid._0?
            public data class _0(
```

alertersystem.com shows the same with a lower-case class: `interface context : Any` against `val context: context?` (93 schemas, from the JSON-LD property `@context`).

## Minimal reproducer

`<scratchpad>/repro/property-vs-nested-class-name.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Nested", "version": "1" },
  "paths": {
    "/hours": { "get": { "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Hours" } } } } } } }
  },
  "components": { "schemas": {
    "Hours": {
      "type": "object",
      "properties": {
        "0": { "type": "object", "properties": { "open": { "type": "string" } } },
        "@context": { "type": "object", "properties": { "vocab": { "type": "string" } } }
      }
    }
  } }
}
```

Observed: all phases pass, no diagnostics, `compile exit=1`.

```kotlin
@Serializable
public data class Hours(
  @SerialName("0") public val _0: _0? = null,
  @SerialName("@context") public val context: context? = null,
) {
  @Serializable public data class _0(public val `open`: String? = null)
  @Serializable public data class context(public val vocab: String? = null)
}
```

```
model/Hours.kt:15:14: error: conflicting declarations:
model/Hours.kt:18:14: error: conflicting declarations:
model/Hours.kt:22:21: error: conflicting declarations:
model/Hours.kt:29:21: error: conflicting declarations:
```

Kotlin does not allow a member property and a nested class with the same name in one class.

## What the spec says

OAS 3.0.3 §4.7.24: `properties` keys are JSON object keys with no naming restriction. `"0"`–`"6"` for weekdays (edrv.io, meraki.com), UUID keys (getpostman.com), `@context` and `@id` (JSON-LD), `$template` (box.com) are all valid.

## Root cause

Traced. The two names are built by different functions that normally differ in the case of the first letter, and here don't:

- Property: `jsonName.toParamName()` (`renderer/.../NamingUtils.kt:39-49`): camel case, non-identifier characters replaced by `_` and trimmed, `_` prefix for a leading digit. `"0"` → `_0`, `"@context"` → `context`.
- Nested class: `NamingContext.ObjectProperty -> name.toPascalCase()` (`renderer/.../TypeMapping.kt:92-94`), then `sanitizeSimpleName` (`TypeMapping.kt:113-116`): characters outside `[A-Za-z0-9_]` removed, `_` prefix for a leading digit. `toPascalCase()` (`typed/.../StringUtils.kt:31-34`) upper-cases the first character of each word only if it is a lower-case letter. For `"0"` there is no letter, and for `"@context"` the first character is `@`, so nothing is capitalised: `_0` and `context`.

So the names coincide whenever the property name starts with a digit, or with a symbol that `splitToWords` doesn't treat as a boundary (`@`, `$`, `{`, `#`, …) followed by a lower-case letter. The second case also produces a class whose name starts with a lower-case letter, which is wrong on its own.

Arrays are affected the same way: edrv.io has `"0": { type: array, items: { inline object } }`, giving `val _0: List<_0>?`.

## Correct final API

The class name is always a capitalised identifier distinct from the property:

```kotlin
public data class Hours(
  @SerialName("0") public val _0: Day0? = null,          // or N0 / Item0, see open decision
  @SerialName("@context") public val context: Context? = null,
) {
  public data class Day0(...)
  public data class Context(...)
}
```

Wire behaviour: unchanged; `@SerialName` already carries the JSON key.

## Fix slice

- Renderer: in `sanitizeSimpleName` (or in `toSimpleName` for `ObjectProperty`), capitalise the first letter after sanitising, so `@context` → `Context`, `$template` → `Template`.
- For names with no leading letter (`0`, `2a9b8fa8-…`), choose a class prefix so the class differs from the property: `_0` for the property and for example `Property0` / `N0` for the class. Open decision: which prefix. It should be one rule shared with enum values and schema names that start with a digit.
- A guard for the general case belongs in the property-name allocator proposed in `issues/property-kotlin-name-collisions.md`: a property name must not equal the simple name of a type nested in the same class.
- Check `toParamName()` for property names consisting only of symbols; it falls back to `_`, and two such properties would collide (not seen in the corpus).

## Proof

- Failing test first: an `ObjectSpec` golden case with the reproducer, plus the array variant.
- Round trip: decode `{"0":{"open":"9"},"@context":{"vocab":"x"}}` and encode it back.
- Integration tests would not catch a regression: no inline-typed property in `github.json`, `openai.yaml` or `youtrack.json` has a name that both functions map to the same identifier (checked by script; `github.json` has `+1`/`-1` and `_links`, which don't collide).

## Affected documents

Observed: vtex.local:Search-API [0fe6612abbcb02a3305c] (`"0"`); edrv.io [61a95e88b315bda9a720] (`"0"`–`"6"`, arrays); meraki.com [91eefd8ca04f7cb31cd7] (`"0"`–`"14"`); getpostman.com [fd89481e9ab83eca42e2] (UUID keys); alertersystem.com [d1c61359c434f9603247] (`@context`).

Construct present, not reached: mist.com [5d2a9c76b41d4671c72b]; probely.com [e4fd363fe7102f41fa87]; box.com [4f89bc9851d389307c20] (`$template`); vtex.local:Profile-System [02204817aafb791f57d4] (`{fieldName}`).
