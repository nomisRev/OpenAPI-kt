# Server variable enums: values without letters collapse to an empty name, and a default outside the enum is referenced as a constant

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile (plus one silent drop)
**Impact:** 2 of 1,547 documents (amazonaws.com:s3, vtex.local:VTEX_TEMPLATE); 1,078 server objects in the census use variable enums and go through this code
**Source spec valid:** mixed — s3 (`enum: [".", "-"]`) is valid; vtex (`default: "{environment}"` not in `enum`) violates a SHOULD in 3.0 and a MUST in 3.1

## Symptom

amazonaws.com:s3 [1eed5a530ab5251aee4b], server `http://s3{dash-or-dot}{region}.amazonaws.com` with `dash-or-dot: {enum: [".", "-"], default: "."}`:

```kotlin
public val dashOrDot: DashOrDot = DashOrDot.``,     // syntax error: Expecting an element
public enum class DashOrDot(public val `value`: String) {
  ``("-"),                                          // "." is gone: both values got the name `` and one replaced the other
  ;
}
```

vtex.local:VTEX_TEMPLATE [9b403a0344823228bea7], `environment: {enum: [vtexcommercestable, myvtex], default: "{environment}"}`:

```kotlin
public val environment: Environment = Environment.`{environment}`,   // unresolved reference '{environment}'
public enum class Environment(…) { Vtexcommercestable("vtexcommercestable"), Myvtex("myvtex"), ; }
```

## Minimal reproducer

`<scratchpad>/repro/server-variable-enum-symbols.json`:

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},
 "servers":[{"url":"https://s3{sep}{region}.example.com","variables":{
   "sep":{"enum":[".","-"],"default":"."},"region":{"enum":["us","eu"],"default":"us"}}}],
 "paths":{"/ping":{"get":{"responses":{"200":{"description":"ok"}}}}}}
```

```
worker exit=0 phases: parse pass, typed pass, render pass, emit pass
diagnostics: []
compile exit=1
api/T.kt:19:33: error: unresolved reference 'url' on receiver of type 'TServer'.
api/T.kt:40:19: error: unresolved reference 'Sep'.
api/T.kt:40:29: error: syntax error: Expecting an element.
api/T.kt:40:30: error: syntax error: Expecting comma or ')'.
api/T.kt:41:22: error: unresolved reference 'Region'.
api/T.kt:42:3: error: function declaration must have a name.
```

Generated: `public val sep: Sep = Sep.``,` and an `enum class Sep` whose only constant is ```` ``("-") ````.

`<scratchpad>/repro/server-variable-enum-default.json` (`"env":{"enum":["stable","beta"],"default":"{env}"}`):

```
compile exit=1
api/T.kt:40:29: error: unresolved reference '{env}'.
```

## What the spec says

OAS 3.0.3 §4.7.6 Server Variable Object: `enum` — "An enumeration of string values to be used if the substitution options are from a limited set"; `default` — REQUIRED, "the value … SHALL be sent if an alternate value is not supplied"; "Note this behavior is different than the Schema Object's treatment of default values". 3.0.3 adds that the default "SHOULD exist in the enum's values"; OAS 3.1.0 §4.8.6 tightens it: "If the enum is defined, the value MUST exist in the enum's values." Enum values are arbitrary strings; `.` and `-` are legitimate (they are URL punctuation in s3).

## Root cause

Traced, `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/FactoryRenderer.kt`:

- `buildVariableEnum` (lines 308-325) names each constant `toEnumValueName(enumValue)`. `toEnumValueName` (`renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/NamingUtils.kt:83-98`) special-cases only `*` and `/`; for any other value without a letter or digit `toPascalCase()` is `""`, and the fallback branch returns ```"`$sanitized`"``` = an empty backticked name. Two such values get the same name; `TypeSpec.Builder.addEnumConstant` keeps one entry per name, so `.` is dropped without a diagnostic.
- `buildServerCaseWithVariables` (lines 281-287) writes the default as `"%T.%L", enumClassName, toEnumValueName(variable.default)` without checking that `variable.default` is one of `variable.enum`, so a default outside the enum names a constant that does not exist.

The same empty-name fallback in `toEnumValueName` is what produces ```` `` ```` constants for schema enums containing `""` (xero, fec.gov, clickmeter, …; another investigator's report) — shared helper, separate call site.

## Correct final API

```kotlin
public data class TServer(
  public val sep: Sep = Sep.Dot,
  public val region: Region = Region.Us,
) {
  public val url: String get() = "https://s3${sep.value}${region.value}.example.com"
  public enum class Sep(public val `value`: String) { Dot("."), Dash("-") }
}
```

Every enum value is a constant with a valid, distinct name (punctuation spelled out, then a positional fallback such as `Value1` if two still collide); none is dropped. For a default that is not in the enum: a `GenerationDiagnostics` warning naming the server and variable, and the parameter is generated without a default (`environment: Environment`, caller must choose) — the declared default is not a legal value, so inventing an enum constant for it or falling back to the first value would both be guesses. Wire: the URL is the template with each variable's `value` substituted, unchanged.

## Fix slice

- renderer: give enum-constant naming a total, injective mapping for a list of raw values (names for common punctuation, uniqueness pass), used by `buildVariableEnum`; coordinate with the schema-enum empty-name report so both call sites share it.
- renderer: in `buildServerCaseWithVariables`, look the default up in the enum; if absent, no default argument + diagnostic. `buildSecondaryConstructors` (lines 62-70) then cannot use `Case()` as the client's default server when a variable has no default — it needs either no default server or the first server that is fully defaulted.
- Open decisions: 3.0 says SHOULD — is the missing default a warning (generate, no default) or an error? Names for punctuation (`Dot`/`Dash` vs `Period`/`Hyphen`).
- Related, by reading the code only (no corpus document hits it): `buildServerUrlTemplate` (lines 327-340) builds the template by string replacement into a raw `"…"` literal, so a `$` or `"` in a server URL, or a variable whose camel-cased name is a keyword, would also break; worth covering in the same change.

## Proof

- Golden `ClientSpec` cases beside `renderer/src/jvmTest/resources/kotlinTestData/client/factory-servers-variable-default/`: enum `[".", "-"]`; default outside the enum (assert diagnostic and no default).
- Runtime assertion: `TServer(sep = Sep.Dash).url == "https://s3-us.example.com"` and `TServer().url == "https://s3.us.example.com"`.
- integration-tests do not cover it: `github.json`, `youtrack.json`, `openai.yaml` have one server each with no variables.

## Affected documents

amazonaws.com:s3 [1eed5a530ab5251aee4b] (also fails on duplicate server names, composite `#` path keys and 25 `conflicting declarations`), vtex.local:VTEX_TEMPLATE [9b403a0344823228bea7] (its only error). Total 2. Spec scan for both conditions over all census documents: `<scratchpad>/cc/servers.py` and the inline scan recorded in the session; no other document has an enum value without a letter/digit or a default outside its enum.
