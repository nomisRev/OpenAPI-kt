# Two properties of one schema get the same Kotlin name (`caller-name` / `caller_name`)

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 3 of 1,547 documents fail to compile (trello.com, nexmo.com:number-insight, alertersystem.com). 6 documents have the construct: meshery.local and mist.com fail before compilation, and zeit.co has it in a schema but shows no such error (not investigated).
**Source spec valid:** yes — property names are arbitrary, case-sensitive JSON object keys.

## Symptom

```
model/NiResponseXmlStandard.kt:50:16: error: conflicting declarations:
val callerName: String?
model/NiResponseXmlStandard.kt:46: error: serializable class has a duplicate serial name for property 'caller-name', either in the class itself or in its supertypes.
model/NiResponseXmlStandard.kt:74: error: conflicting declarations:
enum class CallerType : Enum<NiResponseXmlStandard.CallerIdentity.CallerType>
```

The census family "serializable class has a duplicate serial name" (3 documents) is this same bug, not a second one.

## Minimal reproducer

`<scratchpad>/repro/property-kotlin-name-collision.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Props", "version": "1" },
  "paths": {
    "/caller": { "get": { "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Caller" } } } } } } }
  },
  "components": { "schemas": {
    "Caller": {
      "type": "object",
      "properties": {
        "caller-name": { "type": "string" },
        "caller_name": { "type": "string" }
      }
    }
  } }
}
```

Observed: all phases pass, no diagnostics, `compile exit=1`.

```kotlin
@Serializable
public data class Caller(
  @SerialName("caller-name")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val callerName: String? = null,
  @SerialName("caller-name")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val callerName: String? = null,
)
```

```
model/Caller.kt:11:1: error: serializable class has a duplicate serial name for property 'caller-name', either in the class itself or in its supertypes.
model/Caller.kt:15:14: error: conflicting declarations:
model/Caller.kt:18:14: error: conflicting declarations:
```

Note that the second property has lost its wire name: both carry `@SerialName("caller-name")`, and `caller_name` appears nowhere.

## What the spec says

OAS 3.0.3 §4.7.24 Schema Object: `properties` follows JSON Schema, a map from property name to schema. JSON object keys are arbitrary strings and are compared exactly, so `caller-name` and `caller_name`, `prefs/background` and `prefs_background`, `@id` and `id`, `x-axis` and `xAxis`, `_version` and `version` are all pairs of distinct properties.

## Root cause

Traced. `renderProperty` in `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ObjectRenderer.kt:262` names each property `jsonName.toParamName()` independently. `toParamName()` (`NamingUtils.kt:39-49`) camel-cases and strips punctuation, so the pairs above meet. The caller (`ObjectRenderer.kt:74`) maps over the properties without comparing the results. `DiscriminatedObjectRenderer.kt:102` names properties the same way.

The duplicate `@SerialName` is a consequence, not a separate bug: `renderProperty` puts the annotation on the constructor `ParameterSpec` and returns a `PropertySpec` with `initializer(paramName)`. KotlinPoet merges a property with the constructor parameter of the same name when it prints the class, so both properties are printed with the first parameter's annotations. (Inferred from the output; I did not read KotlinPoet's source.)

Inline types collide too. A property with an inline enum or object gets a nested class named `name.toPascalCase()` (`NamingContext.Nested.toSimpleName`, `TypeMapping.kt:92-102`). `caller-type` and `caller_type` both declare an inline enum in nexmo, so `enum class CallerType` is emitted twice.

## Correct final API

Both properties exist, each with its own wire name:

```kotlin
@Serializable
public data class Caller(
  @SerialName("caller-name") public val callerName: String? = null,
  @SerialName("caller_name") public val callerName2: String? = null,
)
```

Naming rule, matching what `uniqueNames()` does for path segments (`PathParamUtils.kt:47`): the first declared property keeps the name; a later one whose name is taken gets the first free number from 2, and a warning diagnostic names the schema and both wire names. An alternative for the second name is to keep more of the wire name (`caller_name` as a backticked or snake-case identifier); numbers are simpler and deterministic. Nested inline types follow their property (`CallerType`, `CallerType2`).

Wire behaviour: both keys are read and written independently.

## Fix slice

- Renderer: allocate property names per object before rendering (a map from JSON name to Kotlin name), and pass it to `renderProperty`, to the discriminated-object renderer, and to every place that reads a property by name: `ParameterSerialization.kt:93,137,284,288,327` and the flattened-body constructor call in `ClientRendererRequestBody.kt:340` all call `toParamName()` on the JSON name again.
- Typed or renderer: nested inline class names need the same treatment, so the name must be decided where `NamingContext.ObjectProperty` is turned into a class name (`TypeMapping.kt:92-102`), or the typed layer must hand out unique contexts.
- The allocator must also avoid a property name equal to a nested class name, see `issues/property-name-equals-nested-class-name.md`.
- Open decision: numeric suffix versus a name derived from the punctuation (`callerName` / `caller_name`).

## Proof

- Failing test first: an `ObjectSpec` golden case with the reproducer.
- Round trip: decode `{"caller-name":"a","caller_name":"b"}`, assert both values, encode and compare.
- Integration tests would not catch a regression: no schema in `github.json`, `openai.yaml` or `youtrack.json` has two properties with one Kotlin name (checked by script).

## Affected documents

Observed: trello.com [2b9f6fbebdeb82370e5f] (`prefs/x` and `prefs_x`, 8 pairs in `boards`); nexmo.com:number-insight [3101ace5c3989adb58e6] (`caller-name`/`caller_name`, `caller-type`/`caller_type`, `last-name`/`last_name`); alertersystem.com [d1c61359c434f9603247] (`@id` and `id`, 93 schemas).

Construct present, not reached: meshery.local [273f272bf8b411fe785a] (`x-axis`/`xAxis`); mist.com [5d2a9c76b41d4671c72b] (`_version`/`version`, `_id`/`id`); zeit.co [c88696f9879d15a8659a] (`created-by`/`created_by`).
