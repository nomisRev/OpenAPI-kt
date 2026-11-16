# An `allOf` of a closed object (`additionalProperties: false`) and an object with other properties is generated as the union of the properties, without a diagnostic

**Status:** open
**Confirmed:** found while fixing `fc10b47d` (2026-11-16), reproduced at HEAD `8405f814`; instrumented corpus pass 2026-11-16
**Stage:** typed
**Impact:** 4 of 1,547 documents merge a closed object with another object that declares properties the closed one does not (bbc.com, json2video.com, shipengine.com, tl-api.azurewebsites.net), counted with a temporary `System.err` print in a throwaway worktree over a full pass of the 3.x documents of the preferred census (not sampled). All 4 emit and compile. Three of them (bbc.com, json2video.com, shipengine.com) also merge a required property the closed member forbids. A separate variant, a closed object next to a typed `additionalProperties` schema, is wrong in the generated code (it gains an `additional` map) but no corpus document has it.
**Source spec valid:** yes, but read literally the schemas are unsatisfiable or narrower than their authors mean (see below). The common reading, an `allOf` as inheritance, is not what JSON Schema says.

## Symptom

`Schema.mergeObject` merges `properties` as a union and `additionalProperties` by `AdditionalProperties?.merge`, which keeps `false` when the other side is absent. The merged schema is therefore a closed object (`additionalProperties: false`) with properties that the member that closed it never declared. The two facts contradict each other: a closed member forbids every key it does not list, so none of the other member's properties can appear in an instance. The merge does not notice, report, or choose. The generated class has all the properties.

A second defect in the same function: `AdditionalProperties?.merge` (`DiscriminatedObject.kt:811-826`) returns the schema side whenever it meets `Allowed` (`is Allowed -> a`, `is PSchema -> b`) without reading the boolean, so `false` next to `{additionalProperties: {type: string}}` becomes the schema, and the generated class gains an `additional: Map<String, String>?`. Here closedness is actually lost.

## Minimal reproducers

All use `GET /things` returning `$ref: Thing`. Files: `<scratchpad>/merge-findings/repro/`.

**A. Required property the closed member does not declare** — `closed-open.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Api", "version": "0.0.1" },
  "paths": { "/things": { "get": { "operationId": "getThing",
    "responses": { "200": { "description": "OK", "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Thing" } } } } } } } },
  "components": { "schemas": {
    "Thing": { "allOf": [
      { "type": "object", "properties": { "a": { "type": "string" } }, "additionalProperties": false },
      { "type": "object", "properties": { "b": { "type": "string" } }, "required": ["b"] } ] }
  } }
}
```

Observed: all phases pass, `diagnostics.json` is `[]`, and

```kotlin
public data class Thing(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val a: String? = null,
  public val b: String,
)
```

No instance validates against the schema (every instance must have `b`, the first member forbids `b`).

**B. Optional property** — `closed-open-optional.json`: the same without `"required": ["b"]`. Observed: `a: String? = null, b: String? = null`, no diagnostic. The schema admits only instances without `b`.

**C. Closed next to a typed map (closedness really lost)** — `closed-and-map.json`

```json
"Thing": { "allOf": [
  { "type": "object", "properties": { "a": { "type": "string" } }, "additionalProperties": false },
  { "type": "object", "additionalProperties": { "type": "string" } } ] }
```

Observed: `Thing(a: String? = null, additional: Map<String, String>? = null)` with a custom `Serializer` that fills `additional` from every key that is not `a`. The schema admits exactly the instances whose only key is `a`.

## What the spec says

Quoted from memory, not re-read:

- JSON Schema 2020-12 Core §10.2.1.1 `allOf`: the instance must be valid against every subschema.
- Core §10.3.2.3 `additionalProperties`: it applies to the child values of instance names that match no name in `properties` (nor `patternProperties`) of the same schema object. It sees only adjacent keywords. Because of that, `allOf` does not extend a closed schema: `{properties: {a}, additionalProperties: false}` is invalid for `{"a": "x", "b": "y"}` whatever else the instance satisfies. Draft 2019-09 added `unevaluatedProperties` (Core §11.3) for the "extend and keep closed" use case.
- OAS 3.0.3 §4.7.24 Schema Object: JSON Schema keywords as of draft-wright-json-schema-00 (draft 5). `additionalProperties` is the same adjacent-keyword keyword. There is no `unevaluatedProperties` in 3.0. OAS 3.1 uses 2020-12 and has it.

So for A and B the literal reading is: `Thing` accepts only objects with keys in `{a}`. In A it accepts nothing at all.

Why the pattern is common anyway: the readable intent of `allOf: [Closed, {properties: {b}}]` is inheritance, "a Closed that also has b". Code generators for C# and Java emit `additionalProperties: false` on every class, and OAS 3.0 has no way to say "closed, but extendable". Of the corpus documents, tl-api.azurewebsites.net (an ASP.NET service) has `ApiException = allOf [Exception, {additionalProperties: false, properties: {errors, statusCode, ...}}]` with `Exception` itself closed. Every instance the server sends has keys of both. The literal intersection would allow only the keys that appear in both members (here none).

## Root cause (traced)

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/DiscriminatedObject.kt:794-806`, `Schema.mergeObject`:

```kotlin
val properties = properties.orEmpty().merge(other.properties.orEmpty()) { property, a, b -> combineProperty(property, a, b) }
return mergeCommon(other).mergeObjectConstraints(other).copy(
    properties = properties,
    additionalProperties = additionalProperties.merge(other.additionalProperties)
)
```

- `properties` is the union of both members' keys and is merged without looking at `additionalProperties`.
- `additionalProperties.merge(...)` (line 811) goes through `let(a, b)` (line 807): `false` next to `null` is `false`, and `true` next to `false` is `false` (`Allowed(a.value && b.value)`, correct). `PSchema` next to `Allowed` returns the `PSchema` for either boolean (line 815 `is Allowed -> a` inside the `PSchema` branch; line 825 `is PSchema -> b` inside the `Allowed` branch), which is right for `true` and wrong for `false` (C).
- Downstream, `Object.kt:86-92` models `additionalProperties: null` as `Allowed(false)`, the same as an explicit `false`. After the merge the typed layer cannot tell "closed" from "absent", and the properties of the other member are declared in an object that is closed.

## Correct final API

The decision is between three readings, for A and B:

1. **Forbid the other member's properties**: `Thing(a: String? = null)` for B, and for A (a required `b` the closed member forbids) a refusal as `ContradictoryDeclarations`. This is literal JSON Schema. It makes 3 of the 4 corpus documents (the three with required properties outside the closed member) fail, and it generates a client that cannot decode what the server sends in the fourth (tl-api sends `statusCode`). Rejected: it shrinks the supported set to be correct about a schema its author did not mean, and the generated client would be less accurate against real servers.
2. **Mark it a contradiction when required**: refuses 3 documents, same objection.
3. **Keep the union and say so** (recommended). The generated class is the one above (A: `a`, `b`; B: `a?`, `b?`), plus a warning per merged schema, once, for instance
   `allOf of Thing: the closed member (additionalProperties: false) declares only [a], but the other member declares [b]; read literally no instance has [b] (required here, so no instance validates at all). Generated as the union of the properties, the reading authors of such schemas intend.`

Runtime effect: none of this changes how the generated code decodes. With `Thing(a, b)` the default `Json` (`ignoreUnknownKeys = false`, which the integration-tests README also assumes) throws `SerializationException` for a key that is not a property, whether the schema says `additionalProperties: false` or says nothing: `Object.kt:88` maps both to `Allowed(false)`, so no `additional` property exists and the generated serializer is the plain one. `additionalProperties: true` (or a schema) renders `additional`. Hence `closed` changes nothing in the generated Kotlin; a decoded `{"a": "x", "b": "y"}` succeeds because `b` is declared. This follows from `Object.kt` and the emitted source for A, B and C; I did not run a decode in a compiled client. The Ktor `ContentNegotiation { json() }` default is also from memory.

For C the correct class is `Thing(a: String? = null)`: closed wins over a schema, `false` next to a `PSchema` stays `Allowed(false)`, no `additional`, no custom serializer. The value schema of the map member does still apply to `a` (the second member does not declare `a`), here already `string`.

When both members are closed (json2video, shipengine and tl-api, bbc partly), the literal intersection is the keys they share. The same recommendation applies: union plus warning.

## Fix slice

- typed, `DiscriminatedObject.kt:811-826`: `AdditionalProperties?.merge`: `Allowed(false)` against a `PSchema` is `Allowed(false)` in both orders. Minimal, no warnings needed: it is wrong output, not a judgement call.
- typed, `mergeObject`: when a side's `additionalProperties` is `Allowed(false)` and the other side's `properties` has keys not in this side's, record one warning through `ctx.registry().warn` naming the extra properties and which are required. Merge as today. Warnings are deduplicated by the registry, and the merge runs several times per document (instrumented: the same pair is reported up to 24 times in json2video).
- Do not change `Object.kt:88` here: the null versus false distinction in the typed layer belongs to the `additionalProperties` default question, not this report.

## Proof

Failing tests first (typed, `AllOfUntypedMemberSpec` helpers, plus renderer goldens):

- A and B: the merged model has `a`, `b`, and the registry's warnings name the closed member and `b`. A does not throw.
- C: `Model.Object.additionalProperties` is `Allowed(false)`; no `additional` in the golden (`renderSpec`, `client/allof-closed-and-map`), round-trip: `{"a": "x"}` decodes, `{"a": "x", "c": "y"}` throws with the default `Json`.
- Both closed: the union plus one warning.
- Negative: closed next to an object that declares only keys the closed member also declares gives no warning (the instrumented run found 4 documents with extra keys and none without).
- Integration tests: not checked against the instrumented run; add the tl-api `Exception`/`ApiException` shape as a typed test.

## Affected documents

4 of 1,547, all `compile: pass` in the census of `ccf7f728`:

- bbc.com [0805356d6bc5b6ebb665]: `broadcastTime` (closed `dateRange` plus an inline object with `additionalProperties: true` and `duration`), `deprecatedReferenceElement` (two closed members), required extras.
- json2video.com [2f1b95ebd3604ec737f5]: the `audio` family, a closed inline member next to `BaseElement` and `AudioElement`, both closed; up to 20 properties outside the closed member, required extras.
- shipengine.com [9c418ec409a7131ad64a]: `calculate_rates_response_body` (closed `partial_shipment` plus closed `rate_response`), required extras.
- tl-api.azurewebsites.net [0858c2aecb2233cc1204]: `ApiException`, closed `Exception` plus a closed inline member adding 7 properties, none required.

Reachable only through the `PSchema` variant (C): 0 documents.
