# Enum values that map to the same Kotlin entry name are silently dropped (`last_activity` / `-last_activity`)

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** wrong output (compiles; values missing from the generated enum)
**Impact:** 31 of 1,547 documents declare such an enum; 2 of them pass the whole census with values missing (docker.com:hub, walletobjects.googleapis.com:pay-passes), 7 reach compile and fail for other reasons, the rest stop earlier
**Source spec valid:** yes

## Symptom

No error, no diagnostic. When two distinct enum values produce the same entry name, the generated enum contains only
the last one:

- docker.com:hub [70fb2dd593b905673a55], `ordering` query parameter `["last_activity", "-last_activity", "digest",
  "-digest"]`: `api/V2.kt:538` has `LastActivity("-last_activity")` and no entry for `last_activity`. A user cannot
  request ascending order.
- walletobjects.googleapis.com:pay-passes [821baed5a686cefae254], `Barcode.type` has both `EAN_13` and `EAN13`,
  `PDF_417` and `PDF417` (legacy aliases): `model/Barcode.kt:65` has `EAN13("EAN13")` only. A response carrying
  `"type": "EAN_13"` has no entry to decode into (not run; follows from the generated enum).

## Minimal reproducer

`<scratchpad>/repro/enum-values-collide-silently-dropped.json` — a query parameter of type

```json
"Operator": { "type": "string", "enum": ["a-b", "a_b", "A B"] }
```
```
worker exit=0 phases: all pass, diagnostics: []
compile exit=0
```
```kotlin
@Serializable
public enum class Operator(public val `value`: String) {
  @SerialName("A B")
  AB("A B"),
  ;
}
```

Also `["NONE", "", "-"]`: the `""` entry disappears behind `-` (both are named ``` `` ```, see
`enum-entry-name-illegal-characters.md`).

## What the spec says

JSON Schema Validation (referenced by OAS 3.0.3 Schema Object, `enum`): "Elements in the array SHOULD be unique", and
they are — `a-b` and `a_b` are different strings. An instance validates only if it equals one of them, so every
listed value must be sendable and receivable.

## Root cause (traced)

`renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/EnumRenderer.kt:66-88`: each value is named independently
with `toEnumValueName(rawValue)` (`renderer/.../NamingUtils.kt:83`, PascalCase, which removes `-`, `_`, `.`, spaces)
and added with `addEnumConstant(entryName, …)`. KotlinPoet keeps enum constants in a map keyed by name, so a second
constant with the same name replaces the first without complaint. No uniqueness check exists in the renderer or in
the typed `Model.Enum` (`values` are the raw wire values). The same independent naming is used where entries are
*referenced* (`ObjectRenderer.kt:575`, `CollectionRenderer.kt:45`, `ClientRendererOperations.kt:764`,
`OpenEnumParts.kt:98,112`, `ImplRenderer.kt:1249`), so a default value of `last_activity` would also resolve to the
`-last_activity` entry.

`toParamName` already knows about the sign problem for properties (`NamingUtils.kt:40-43`: "`+1`/`-1` … would
collapse both to the same identifier"); enum entries have no equivalent.

## Correct final API

One entry per declared value, names unique within the enum, deterministic:

```kotlin
public enum class Ordering(public val `value`: String) {
  @SerialName("last_activity") LastActivity("last_activity"),
  @SerialName("-last_activity") LastActivityDescending("-last_activity"),   // or MinusLastActivity / LastActivity2
  …
}
public enum class Type(…) { @SerialName("EAN_13") EAN_13("EAN_13"), @SerialName("EAN13") EAN13("EAN13"), … }
```

Preference order for disambiguation: (1) keep the raw value when it is already a valid identifier and unique
(`EAN_13`, `EAN13`), (2) spell out a leading sign (`-x` -> `MinusX`, `+x` -> `PlusX`), (3) numeric suffix by
document order. Every value round-trips on the wire. If a collision ever cannot be resolved, an **error diagnostic**,
never a drop.

## Fix slice

- renderer: compute entry names per `Model.Enum` in one function returning `Map<wireValue, entryName>` that is
  injective; use it at the declaration (`EnumRenderer.kt:66-88`, `OpenEnumParts.kt`) and at every reference listed
  above. Share it with the path-level `FixedValue`/overloaded enum naming if those have the same gap.
- renderer: guard in `toTypeSpec` — `check(names.distinct().size == values.size)` so a future naming change cannot
  drop values silently again.
- Do it in one change with `enum-entry-name-illegal-characters.md` (same function).
- Open decision: the disambiguation scheme above.

## Proof

- Failing golden `ClientSpec` case with the reproducer (expects three entries, gets one).
- A renderer property test: for any list of distinct strings, the generated enum has as many entries as values and
  `entries.map { it.value }` equals the input.
- kotlinx.serialization round-trip for `EAN_13` and `EAN13`; MockEngine test that `ordering=last_activity` and
  `ordering=-last_activity` are both sendable.
- integration-tests: no enum in `github.json` or `youtrack.json` has colliding names (checked with the naming rule
  ported to a script); `openai.yaml` not checked structurally. No coverage.

## Affected documents

31 total by static scan of every `enum` array (string values, names computed with the renderer's rule; includes
enums on unused schemas). Verified in generated output: docker.com:hub [70fb2dd593b905673a55],
walletobjects.googleapis.com:pay-passes [821baed5a686cefae254]. By stage: compile-pass 2, compile-fail 7, emit 7,
typed 10, render 4, parse 1.
