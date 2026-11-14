# One parameter holding an array of objects or a nested object fails the whole document (AWS query APIs)

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** render (`OpenAPI generation failed:` from `GenerationDiagnostics`)
**Impact:** 25 of 1,547 documents — 19 `amazonaws.com:*` query-protocol APIs (autoscaling, cloudformation, cloudsearch, docdb, ec2, elasticache, elasticbeanstalk, elasticloadbalancing, elasticloadbalancingv2, email, iam, monitoring, neptune, rds, redshift, sdb, sns, sqs, sts), box.com, gerermesaffaires.com, seldon.local:core, seldon.local:wrapper, telnyx.com, cloudmersive.com:ocr (header)
**Source spec valid:** mixed — the documents are schema-valid, but they use `style: form` / `style: simple` for values OpenAPI gives no serialization for, so no conforming client can send them

## Symptom

```
OpenAPI generation failed:
- GET /#Action=AddTags parameter Tags (in query, style form, explode true) has no supported serialization for its type, location, `style` and `explode`.
```

No source file is emitted for the document, although the refused parameters sit on a minority of its operations:
573 of 3,304 operations across the 19 AWS documents (5% in sns, 22% in ec2, 28% in docdb), 1 of 260 in box.com,
2 of 361 in telnyx.com, 1 of 19 in cloudmersive.com:ocr.

Refused shapes, counted from the failure messages against the source schemas (`allOf: [{$ref}, {description}]`
wrappers looked through; a document can appear in several rows):

| location / style | schema shape | docs | occurrences |
| --- | --- | --- | --- |
| query, form, explode true | array of objects | 19 (18 AWS + box.com) | 598 |
| query, form, explode true | object with an array property | 12 | 52 |
| query, form, explode true | object with an object property | 9 | 36 |
| query, form, explode true | object with array and object properties | 3 | 12 |
| query, form, explode true | map of objects (`additionalProperties: {object}`) | 2 (sns, sqs) | 3 |
| header, simple, explode false | object with array properties | 1 (cloudmersive.com:ocr) | 1 |

This is not a regression in the sense of lost working code. 35 of the 43 documents that now fail with
`OpenAPI generation failed:` emitted sources in the 2026-11-01 census, before f43c8ec9 "Serialize parameters by their
style and explode", but the emitted code was wrong: `exploration/build/preferred-census/results/05be807ef4fe351ae7c3/sources/.../api/#Action=AddTags.kt`
has `parameter("Tags", tags)` with `tags: List<Tag>`, which sends `Tags=[Tag(key=..., value=...)]` (Kotlin
`toString()`). The refusal replaced a silently wrong request.

## Minimal reproducer

`<scratchpad>/repro/query-nested-array-of-objects.json` (reduced from amazonaws.com:elasticloadbalancingv2 `GET /#Action=AddTags`):

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Repro", "version": "1" },
  "paths": {
    "/tags": {
      "get": {
        "operationId": "op",
        "parameters": [
          { "name": "Tags", "in": "query", "required": true,
            "schema": { "type": "array", "items": { "$ref": "#/components/schemas/Tag" } } }
        ],
        "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "type": "string" } } } } }
      }
    }
  },
  "components": { "schemas": { "Tag": { "type": "object", "required": ["Key"],
    "properties": { "Key": { "type": "string" }, "Value": { "type": "string" } } } } }
}
```

```
worker exit=1 phases: parse pass, typed pass, render fail
java.lang.IllegalArgumentException: OpenAPI generation failed:
- GET /tags parameter Tags (in query, style form, explode true) has no supported serialization for its type, location, `style` and `explode`.
	at io.github.nomisrev.openapi.GenerationDiagnosticsKt.throwOnErrors(GenerationDiagnostics.kt:31)
	at io.github.nomisrev.openapi.ClientRendererKt.generateClient(ClientRenderer.kt:9)
no sources emitted
```

Same result for `<scratchpad>/repro/query-nested-object.json` (object with an array property, reduced from
amazonaws.com:email `SendEmail.Destination`) and `<scratchpad>/repro/header-nested-object.json` (reduced from
cloudmersive.com:ocr `formTemplateDefinition`).

## What the spec says

- OAS 3.0.3 §4.7.12.2 "Style Values" defines `form`, `simple`, `spaceDelimited`, `pipeDelimited` for `primitive`,
  `array`, `object`, and the "Style Examples" table only shows an array of primitives and an object of primitives.
  The styles are RFC 6570 expansions, and RFC 6570 defines variables as a string, a list of strings or an associative
  array of strings (§2.3, §3.2.1): there is no expansion of a list of objects or of an object holding a list.
- OAS 3.1.1 §4.8.12.4 and Appendix C state it outright: the behaviour for nested objects and arrays is undefined
  for these styles; §4.8.12.3 recommends `content` (for example `application/json`) for complex values.
  (Section numbers and wording quoted from memory of the published text; check before pasting into an upstream issue.)
- So `Tags: array<Tag>` with `style: form` is a description no client can honour. Refusing to guess is right.

The AWS documents are machine conversions (`info.x-origin.converter: mermade/aws2openapi`) of the AWS Query
protocol. The real wire format is neither `form` nor JSON: it is `Tags.member.1.Key=k&Tags.member.1.Value=v`
(not verified against a live service; this is the documented awsQuery convention). The path `/#Action=AddTags` is
also a converter artefact: the fragment never reaches the server. These operations are not callable from the
document alone by any generator.

## Root cause

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/ParameterStyle.kt:60-98`
  (`Model.isSerializableParameter`): a `Model.Collection` is supported only when `inner` is a form scalar (line 76-78),
  a `Model.Object` only when every property is a form scalar (line 80-83), a `Model.Dictionary` only with scalar values
  (line 85-87). Result is stored in `Route.Input.isSupported` (`SchemaContext.kt:95`).
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/GenerationDiagnostics.kt:281-294`
  (`Route.parameterDiagnostics`) turns every unsupported parameter into an `Error`, unconditionally.
- `GenerationDiagnostics.kt:28-40` (`throwOnErrors`) fails the whole generation on the first `Error`.

Unlike an unsupported form body, which is downgraded to a `Warning` and skipped when another body variant exists
(`GenerationDiagnostics.kt:96-123`), an unsupported parameter has no "skip" path: there is no way to keep the other
operations of the document.

## Correct final API

1. The refusal itself stays: never send `toString()` or an invented layout for an undefined serialization.
2. The blast radius should be the operation, not the document. For the reproducer the client should contain every
   other operation, and for the refused one either
   - nothing, plus the diagnostic `GET /tags is not generated: parameter Tags (...) has no serialization OpenAPI
     defines`, or
   - (when the parameter is optional) still nothing: dropping an optional parameter silently changes the request, so
     the operation is omitted as a whole.
3. Whether the diagnostic is an `Error` (build fails) or a `Warning` (build passes, operation missing) is the open
   decision below. The existing precedent is the form body: `Error` when nothing else can be generated for the
   operation, and the Gradle task fails.

Wire behaviour for the supported neighbours is unchanged: `Tags=a&Tags=b` for an array of scalars, `Key=k&Value=v`
for an exploded object of scalars.

## Fix slice

- renderer: make the unsupported-parameter case filter the operation out of the tree (a sibling of
  `withSupportedRequestBodiesOnly`, `GenerationDiagnostics.kt:177-195`) so rendering never sees it.
- gradle-plugin / `RenderConfig`: an explicit switch, for example `unsupportedOperations = fail | omit`, defaulting
  to `fail`. With `omit`, the diagnostics become warnings and the 25 documents generate their remaining operations
  (2,731 of 3,304 for the AWS documents).
- Open decisions:
  - default of the switch (the project preference "no silent drops" is met either way: there is always a diagnostic);
  - whether to add an opt-in vendor serialization for awsQuery (`Name.member.N.Field`), in the spirit of option 2 in
    `issues/stripe-deepobject-arrays.md`. Not recommended without a real consumer: the converted documents also get
    the path (`/#Action=`), the POST body media type (`text/xml`) and SigV4 auth wrong.
  - the diagnostic text should name the reason ("array of objects has no `form` serialization") instead of the
    generic "its type, location, `style` and `explode`"; today the reader has to open the schema to learn which
    rule fired.

## Proof

- Failing test first (renderer `ClientSpec`-style golden): the reproducer plus a second, supported operation; assert
  the supported operation is generated and the diagnostics contain exactly one entry for `GET /tags`. Today the
  assertion on generated files fails because `generateClient` throws.
- typed `RouteSpec`: `isSupported == false` for array-of-objects, object-with-array, map-of-objects in query, and
  object-with-array in header (already true; pin it).
- integration-tests coverage: none. `github.json` has `oneOf[array, scalar]` query parameters but no nested
  object/array parameter; `openai.yaml` and `youtrack.json` have none. A regression here would not be caught.

## Affected documents

amazonaws.com:autoscaling [f73c85a9fb9ddabde98f], amazonaws.com:cloudformation [9689c64d3785465b4f4e],
amazonaws.com:ec2 [74b7f6f0f9ccdc9c4812], amazonaws.com:elasticloadbalancingv2 [05be807ef4fe351ae7c3],
amazonaws.com:email [0a6b0d1d5b6fd9f11fd0], amazonaws.com:rds [d562814f4b8e70bc53cf],
amazonaws.com:sqs [6911408785e3c80431fa], box.com [4f89bc9851d389307c20], seldon.local:core [3eb99610a055340ac5af],
cloudmersive.com:ocr [7cb0e4cd39fb92179359] — 25 in total (list in **Impact**). gerermesaffaires.com and telnyx.com
also need `issues/untyped-schema-parameters-and-form-fields-refused.md` fixed before they generate.
