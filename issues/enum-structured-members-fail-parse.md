# An `enum` member that is an array or an object makes the whole document fail to parse

**Status:** fixed in `1b89168f`: structured members parse and round-trip in JSON and YAML, and the typed layer generates the underlying type with a warning. Census re-run 2026-11-14: no document fails to parse; the five affected documents now stop at other reported issues.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming); pinned by `CensusRegressionTest`
**Stage:** parse
**Impact:** 5 of 1,547 documents, 6 schema locations (docker.com:engine, pandascore.co, enode.io, apacta.com, gov.bc.ca:jobposting). These are all 5 parse failures of the census.
**Source spec valid:** mixed — an enum member of any JSON type is valid JSON Schema; in 2 documents the members match the declared `type: object` (docker, pandascore), in 3 they contradict the declared type (authoring mistakes)

## Symptom

```
kotlinx.serialization.json.JsonDecodingException: Expected JsonPrimitive, but had JsonObject as the serialized body of string at path: $.0
JSON input: {}
```

(or `had JsonArray`). The exception has no JSON pointer to the schema (`$.0` is the index inside the enum array), so
the user cannot tell which of several hundred schemas is at fault. Nothing is generated for the document.

| document | location | declared type | enum |
| --- | --- | --- | --- |
| docker.com:engine | `components/schemas/ContainerConfig/properties/ExposedPorts/additionalProperties` (and `Volumes`) | object | `[{}]` |
| pandascore.co | `components/schemas/CurrentVideogame` | object | `[{"id":1,"name":"LoL","slug":"league-of-legends"}, ...]` |
| enode.io | `.../charge-state` response `isChargingReasons/items` | string | `[{"override": true}, "DEFAULT"]` |
| apacta.com | `/expenses` GET parameter 11 schema | string | `["all", "none", ["project1","project2","project3"]]` |
| gov.bc.ca:jobposting | `/jobs` POST body `jobTypes` | integer | `[[1], [2]]` |

## Minimal reproducer

`<scratchpad>/repro/enum-object-member.json` (reduced from docker.com:engine):

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": { "/containers": { "get": { "operationId": "op", "responses": { "200": { "description": "ok",
    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/ContainerConfig" } } } } } } } },
  "components": { "schemas": { "ContainerConfig": { "type": "object", "properties": {
    "ExposedPorts": { "type": "object",
      "additionalProperties": { "type": "object", "enum": [ {} ], "default": {} } } } } } }
}
```

```
worker exit=1 phases: [{"phase":"parse","status":"fail","exception":"kotlinx.serialization.json.JsonDecodingException","message":"Expected JsonPrimitive, but had JsonObject as the serialized body of string at path: $.0\nJSON input: {}"}]
no sources emitted
```

`<scratchpad>/repro/enum-array-member.json` (reduced from apacta.com, a string enum with one array member) fails
with `Expected JsonPrimitive, but had JsonArray ... at path: $.2`.

The reduced fixtures also exist in the repository: `parser/src/commonTest/resources/specs/census/{docker-object-enum,
pandascore-object-enum,enode-object-enum,apacta-array-enum,jobposting-array-enum}.{json,yaml}`, asserted to be
rejected by `CensusRegressionTest."array enum values are currently rejected"` and
`"object enum values are currently rejected"`
(`parser/src/commonTest/kotlin/io/github/nomisrev/openapi/parser/CensusRegressionTest.kt:23-35`). YAML fails the same
way ("Expected a string, but got a list" / "got a map").

## What the spec says

OAS 3.0.3 §4.7.24 Schema Object takes `enum` from JSON Schema Validation (draft Wright-00) §5.20: "The value of this
keyword MUST be an array. This array SHOULD have at least one element. Elements in the array SHOULD be unique.
Elements in the array MAY be of any type, including null." JSON Schema 2020-12 (OAS 3.1) §6.1.2 says the same. So:

- docker `{type: object, enum: [{}]}` is valid and meaningful: "exactly the empty object" (Docker's
  `"ExposedPorts": {"80/tcp": {}}` idiom).
- pandascore's object enum is valid: one of a closed list of object values.
- A member that contradicts `type` (enode, apacta, jobposting) is still a valid schema document; that member simply
  can never validate. The parser has no ground to reject the document.

## Root cause

`parser/src/commonMain/kotlin/io/github/nomisrev/openapi/parser/Schema.kt:89-90`:

```kotlin
// inner type nullable to allow explicit "null" value for nullable enums
val enum: List<String?>? = null,
```

The generated serializer decodes each member with the `String` serializer, which accepts any JSON primitive
(numbers and booleans become their text, see `issues/enum-and-default-values-stored-as-strings.md`) and throws on
arrays and objects. The failure aborts `OpenAPI.fromJson` / `fromYaml`.

## Correct final API

Parsing never fails on a valid `enum`. In the generated client:

- docker: `ExposedPorts: Map<String, JsonObject>?` — the value type is the declared object type; an enum whose only
  member is `{}` adds no type information the Kotlin model can carry, so it is dropped with a diagnostic
  (`Warning: enum of object values at #/components/schemas/ContainerConfig/properties/ExposedPorts/additionalProperties
  is not generated as a Kotlin enum; the declared type is used.`). Wire: `{"ExposedPorts": {"80/tcp": {}}}`.
- pandascore `CurrentVideogame`: generated as the object type its `properties` describe (or free-form JSON when it
  has none), same diagnostic. A sealed set of constants is possible but not required for a correct client.
- Type-contradicting members (enode, apacta, jobposting): the enum is generated from the members that match the
  declared type (`DEFAULT`; `all`, `none`), and each dropped member is reported:
  `Warning: enum member ["project1","project2","project3"] at <pointer> is not a string and is ignored.` For
  jobposting, where no member matches `integer`, the property falls back to the declared type (`Long`) with the
  same warning (the author almost certainly meant `type: array, items: {enum: [1, 2]}`; not ours to guess).

## Fix slice

- parser: `Schema.enum: List<JsonElement>?` (using the existing `JsonElementWithYamlSerializer`, as `const` on the
  next line already does). This is the same model change `issues/enum-and-default-values-stored-as-strings.md`
  needs; do them together.
- typed: `toClosedEnum` (`typed/.../transformers/Enum.kt:13-28`) partitions members into those matching the inner
  type and the rest; structured members are never enum constants. Needs a way to surface a warning from `typed`
  (today diagnostics are produced only in the renderer from the finished `ApiTree`).
- Parser error reporting, independent of the above: decoding errors should carry the JSON pointer of the schema.
- Open decisions: whether a pandascore-style object enum should become constants; whether a contradictory member
  is a warning (recommended) or an error.

## Proof

- Flip the two `CensusRegressionTest` cases from `rejects(...)` to value assertions (the file's header asks for
  exactly that).
- typed test per reduced fixture: resulting model (map of free-form objects; enum with only the matching members).
- End to end: the five census documents get past `parse` (docker then needs the rest of the pipeline to hold).
- integration-tests coverage: none of `github.json`, `openai.yaml`, `youtrack.json` contains an array or object enum
  member (scanned: 0), so they neither fail today nor guard the fix.

## Affected documents

docker.com:engine [72153fdeeb7f32d0f9f5], pandascore.co [a16b7327554a85f8e46f], enode.io [6b310704012b29b07d4b],
apacta.com [45032aa34518e6bdb425], gov.bc.ca:jobposting [9f0fc369baad2493ee6a] — 5 in total. A scan of all 1,547
source documents for array/object enum members finds exactly these 5 (6 locations), so every occurrence in the
corpus is a parse failure.
