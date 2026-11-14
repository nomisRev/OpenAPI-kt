# Open issues

Index of the reports in this directory, from the corpus census of 2026-11-14 on branch
`openapi-3.2-streaming` (after commit `025aef78`). Each report has a reduced reproducer, the spec
reference, the traced root cause, the generated API we want, and how to prove the fix.

## Census result

1,547 OpenAPI 3 documents (API Guru preferred versions, index pinned on 2026-11-01), run through
parse → typed → render → emit → compile. Evidence is under `exploration/build/census-2026-11-14/`
(ignored by Git).

| Outcome | Documents |
| --- | ---: |
| Generates and compiles | 445 |
| Generates, fails to compile | 555 |
| Fails in typed transformation | 380 |
| Fails at emit | 98 |
| Fails at render | 53 |
| Passes every phase, emits no files | 11 |
| Fails to parse | 5 |

Read these numbers with three caveats:

- **"Compiles" is not "correct".** At least 12 of the 445 contain operations whose body was removed
  after an error diagnostic the harness does not check, and several reports below describe clients
  that compile and send the wrong request.
- **One failure per document.** The census records the first failure only, so fixing a family
  exposes the next one. All 209 `UnknownStatusCode` documents also fail on server names.
- **"Duplicate generated files" is a harness check.** The Gradle plugin has no such check: it writes
  every file and the last one wins.

## Fixed since the census

| Report | Commits | Left open |
| --- | --- | --- |
| [query-parameter-named-url-shadows-request-builder](query-parameter-named-url-shadows-request-builder.md) | `273df770` | nothing |
| [enum-and-default-values-stored-as-strings](enum-and-default-values-stored-as-strings.md) | `5854454c`, `5d203102`, `1b89168f`, `f39eb298`, `87aac23f`, `01f1fe46`, `fde99b78` | enums with a `null` member next to typed members; YAML quoting is not visible to the parser |
| [enum-structured-members-fail-parse](enum-structured-members-fail-parse.md) | `1b89168f` | nothing |
| [default-or-enum-value-not-of-declared-type](default-or-enum-value-not-of-declared-type.md) | `f39eb298` (partly) | values that cannot be read as the declared type still abort the document (12) |

A second census after these commits (`exploration/build/census-2026-11-14b/`, taken before `01f1fe46` and
`fde99b78`): 457 generate and compile (was 445), 541 fail to compile (was 555), 387 fail in typed (was 380), none
fails to parse (was 5). The typed count rose because the five documents that used to fail parsing now stop at
other reported issues, and because of two regressions that `01f1fe46` and `fde99b78` then fixed; the four
documents involved were re-checked individually and are back at their earlier stage or better.

The tables below still carry the counts of the first census.

## Suggested order

Ordered by: silent wrong output first, then regressions from this branch, then documents unblocked.
Counts are documents affected; see each report for how they were counted.

### 1. Clients that compile and are wrong

| Report | What goes wrong | Docs |
| --- | --- | ---: |
| [enum-and-default-values-stored-as-strings](enum-and-default-values-stored-as-strings.md) | Integer/number/boolean enums encode as `"300"` and reject `300` | 103 have it, 28 pass today |
| [composite-path-segments](composite-path-segments.md) | `{id}.json`, `{a}.{b}`, `{name}:verb` treated as literals; braces sent on the wire | 303 have it |
| [operation-parameter-shadows-path-parameter](operation-parameter-shadows-path-parameter.md) | A body/query field named like a path parameter replaces it in the path (GitHub integration client) | 14 |
| [enum-values-with-one-kotlin-name-silently-dropped](enum-values-with-one-kotlin-name-silently-dropped.md) | `last_activity` / `-last_activity` collapse to one entry | 31 have it |
| [path-literal-segment-loses-navigation-property](path-literal-segment-loses-navigation-property.md) | Operations under a segment named `client` are unreachable | 13 |
| [context-variants-of-one-schema-share-a-class-name](context-variants-of-one-schema-share-a-class-name.md) | Read/Write variants overwrite each other | 18 |
| [duplicate-model-ref-with-readonly-sibling](duplicate-model-ref-with-readonly-sibling.md) | `$ref` + `readOnly` generates the target twice (YouTrack has 115) | 73 |
| [client-root-class-collides-with-path-class](client-root-class-collides-with-path-class.md) | Path class overwrites the client class | 9 |
| [schema-names-map-to-one-kotlin-class](schema-names-map-to-one-kotlin-class.md), [schema-names-differ-by-case](schema-names-differ-by-case.md) | Two schemas, one file | 5 |
| [dollar-in-path-literal-interpolated](dollar-in-path-literal-interpolated.md) | `/members/$count` becomes a string template | 3 fail, 5 blocked earlier |
| [ref-sibling-keywords-discarded-oas31](ref-sibling-keywords-discarded-oas31.md) | `required`, `description`, `default` beside `$ref` are lost | 30 of 36 OAS 3.1 docs |
| [webhooks-and-unreferenced-schemas-generate-nothing](webhooks-and-unreferenced-schemas-generate-nothing.md) | Webhook-only documents emit nothing, without a diagnostic | 11 |
| [render-with-diagnostics-throws-on-parameter-errors](render-with-diagnostics-throws-on-parameter-errors.md) | Parameter errors throw, form errors are returned; operations lose their body | 57 |

### 2. Regressions from this branch

| Report | Introduced by | Docs |
| --- | --- | ---: |
| [query-parameter-named-url-shadows-request-builder](query-parameter-named-url-shadows-request-builder.md) | `f43c8ec9` | 24 |
| [allof-property-two-refs-rejected](allof-property-two-refs-rejected.md) | `29766c84` | 2 |
| [allof-additional-properties-implies-object](allof-additional-properties-implies-object.md) | `0e374d2f` | 1 |

### 3. Largest blockers

| Report | Stage | Docs |
| --- | --- | ---: |
| [server-case-names-collide](server-case-names-collide.md) | compile | 269 |
| [response-media-type-without-schema](response-media-type-without-schema.md) | typed | 243 |
| [unregistered-status-codes-share-one-response-case](unregistered-status-codes-share-one-response-case.md) | compile | 209 |
| [path-literal-segment-not-a-kotlin-identifier](path-literal-segment-not-a-kotlin-identifier.md) | compile | ~93 |
| [flattened-body-field-vs-parameter-name](flattened-body-field-vs-parameter-name.md) | compile | 44 |
| [shared-path-parameter-constraint-false-conflict](shared-path-parameter-constraint-false-conflict.md) | typed | 31 |
| [parameter-kotlin-name-collisions](parameter-kotlin-name-collisions.md) | compile | 30 |
| [ref-json-pointer-not-resolved](ref-json-pointer-not-resolved.md) | typed | 26 |
| [shared-path-parameter-different-types](shared-path-parameter-different-types.md) | typed | 26 |
| [enum-entry-name-illegal-characters](enum-entry-name-illegal-characters.md) | compile | 26 |
| [query-parameter-nested-objects-refused](query-parameter-nested-objects-refused.md) | render | 25 |
| [untyped-schema-parameters-and-form-fields-refused](untyped-schema-parameters-and-form-fields-refused.md) | render | 24 |
| [recursive-allof-ref-stack-overflow](recursive-allof-ref-stack-overflow.md) | typed | 18 |
| [client-name-from-info-title-not-an-identifier](client-name-from-info-title-not-an-identifier.md) | render / compile | 17 |
| [union-case-name-collisions](union-case-name-collisions.md) | compile | 17 |
| [enum-empty-identifier](enum-empty-identifier.md) | compile | 16 |

### 4. Smaller generator bugs (valid specs)

- Typed:
  [discriminator-mapping-without-base-entry](discriminator-mapping-without-base-entry.md) (5),
  [form-urlencoded-non-object-body](form-urlencoded-non-object-body.md) (8, includes Stripe),
  [null-type-schema-crash](null-type-schema-crash.md) (1),
  [remote-references-not-supported](remote-references-not-supported.md) (0 in the corpus).
- Parse:
  [enum-structured-members-fail-parse](enum-structured-members-fail-parse.md) (5),
  [root-extensions-dropped](root-extensions-dropped.md) (430, no effect on generated code today).
- Refused parameters and form fields:
  [deepobject-parameter-open-or-nested-object-refused](deepobject-parameter-open-or-nested-object-refused.md) (7),
  [parameter-content-json-refused](parameter-content-json-refused.md) (3),
  [parameter-union-with-enum-case-refused](parameter-union-with-enum-case-refused.md) (1),
  [query-array-nullable-items-refused](query-array-nullable-items-refused.md) (1),
  [form-field-object-without-encoding-refused](form-field-object-without-encoding-refused.md) (8),
  [form-field-union-of-scalars-refused](form-field-union-of-scalars-refused.md) (2),
  [stripe-deepobject-arrays](stripe-deepobject-arrays.md) (1, by design).
- Names and scoping:
  [route-inline-type-owner-class-mismatch](route-inline-type-owner-class-mismatch.md) (6),
  [server-case-name-not-identifier](server-case-name-not-identifier.md) (6),
  [property-name-equals-nested-class-name](property-name-equals-nested-class-name.md) (5),
  [keyword-path-parameter-unescaped-at-use-sites](keyword-path-parameter-unescaped-at-use-sites.md) (4),
  [parameter-named-client-shadows-http-client](parameter-named-client-shadows-http-client.md) (3),
  [property-kotlin-name-collisions](property-kotlin-name-collisions.md) (3),
  [path-segment-named-like-http-method](path-segment-named-like-http-method.md) (3),
  [shared-response-case-wrong-owner](shared-response-case-wrong-owner.md) (3),
  [server-empty-description-empty-case-name](server-empty-description-empty-case-name.md) (2),
  [server-variable-enum-names](server-variable-enum-names.md) (2),
  [inline-enum-list-default-qualifier](inline-enum-list-default-qualifier.md) (2),
  [inline-union-jsonobject-import](inline-union-jsonobject-import.md) (1).
- Bodies:
  [inline-array-body-flattened-as-object](inline-array-body-flattened-as-object.md) (5),
  [form-body-inline-types-missing-next-to-json](form-body-inline-types-missing-next-to-json.md) (4),
  [binary-and-multipart-body-jvm-clash](binary-and-multipart-body-jvm-clash.md) (1),
  [multipart-field-required-nullable](multipart-field-required-nullable.md) (1),
  [trace-operation-has-no-ktor-function](trace-operation-has-no-ktor-function.md) (1).

### 5. Invalid or questionable source specs (what diagnostic should we give?)

[default-or-enum-value-not-of-declared-type](default-or-enum-value-not-of-declared-type.md) (11),
[malformed-media-type-key-crash](malformed-media-type-key-crash.md) (6),
[nullable-path-parameter](nullable-path-parameter.md) (5),
[allof-property-contradictory-types](allof-property-contradictory-types.md) (2),
[form-field-binary-refused](form-field-binary-refused.md) (2),
[parameter-name-empty-or-not-an-identifier](parameter-name-empty-or-not-an-identifier.md) (2),
[enum-default-not-in-enum](enum-default-not-in-enum.md) (1).

## Checked and not an issue

- Response-code ranges (`4XX`, `5XX`) generate correct dispatch and are covered by tests. The note in
  `exploration/FINDINGS.md` that the typed layer rejects them is stale.
- Ignoring `Accept`, `Content-Type` and `Authorization` header parameters, and renaming a path whose
  Kotlin name is taken (`Xml2`), behave as intended on the examples checked.
- apptigent.com stopped compiling because Kotlin 2.4.20 rejects `:` in enum entry names that 2.4.10
  accepted; the generated source is unchanged. Covered by `enum-entry-name-illegal-characters`.

## Integration tests

`integration-tests/` (GitHub, OpenAI, YouTrack) generates and compiles each client and, since `8d373468`,
round-trips the specs' own examples through the generated models against a committed baseline (see
`integration-tests/README.md`). It still sends no requests, so it cannot catch a wrong URL or parameter. On this branch OpenAI and YouTrack compile and GitHub does not
(`operation-parameter-shadows-path-parameter`, plus one stale line in the hand-written `Smoke.kt`).
The three specs contain few of the constructs above: GitHub has the shadowed path parameter, the
context-variant duplicates and a schema-less request media type; YouTrack has `$ref` + `readOnly`;
OpenAI has integer enums, 3.1 `$ref` siblings and webhooks.
