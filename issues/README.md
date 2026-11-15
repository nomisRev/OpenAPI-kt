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
| [operation-parameter-shadows-path-parameter](operation-parameter-shadows-path-parameter.md) | `68db4e01` | nothing; the GitHub integration client compiles again |
| [untyped-schema-parameters-and-form-fields-refused](untyped-schema-parameters-and-form-fields-refused.md) | `02be578d`, `3fb0ba0b` | a free-form object as a form field, arrays and maps of free-form objects, a free-form object path parameter |
| [composite-path-segments](composite-path-segments.md) | `f9555f8b`, `fd203506`, `eb97be6c` | Google resource names are sent with `%2F`; a name repeated in one segment (`{a}-{a}`); `?` in a path key is sent as written; `{insight_id:}` |
| [optional-nullable-property-cannot-send-explicit-null](optional-nullable-property-cannot-send-explicit-null.md) | `cae01e33`, `909e08bc` | a required nullable property is dropped under `explicitNulls = false` |
| [path-literal-segment-not-a-kotlin-identifier](path-literal-segment-not-a-kotlin-identifier.md) | `eae909af` | signed literals `+1` / `-1` share the name `_1` and are numbered |
| [parameter-kotlin-name-collisions](parameter-kotlin-name-collisions.md), [flattened-body-field-vs-parameter-name](flattened-body-field-vs-parameter-name.md), [parameter-named-client-shadows-http-client](parameter-named-client-shadows-http-client.md) | `8858c973` | query names that differ only by case are sent under one name (Ktor's parameters ignore case); a path parameter named `{client}` |
| [enum-values-with-one-kotlin-name-silently-dropped](enum-values-with-one-kotlin-name-silently-dropped.md), [enum-entry-name-illegal-characters](enum-entry-name-illegal-characters.md), [enum-empty-identifier](enum-empty-identifier.md) | `bf24fce5` | defaults on optional model properties are not covered by a golden |
| [dollar-in-path-literal-interpolated](dollar-in-path-literal-interpolated.md), [keyword-path-parameter-unescaped-at-use-sites](keyword-path-parameter-unescaped-at-use-sites.md) | `bb15dd58` | parameter names `_` / empty |
| [union-case-name-collisions](union-case-name-collisions.md) | `11ac049c` | a `required`-only branch next to `$ref` branches still becomes a free-form JSON case |
| [shared-error-case-inline-type-wrong-owner](shared-error-case-inline-type-wrong-owner.md) | `a8e070fa` | none known |
| [duplicate-model-ref-with-readonly-sibling](duplicate-model-ref-with-readonly-sibling.md) | `040871b9` | the renderer still has no guard against two files with one name |
| [shared-path-parameter-constraint-false-conflict](shared-path-parameter-constraint-false-conflict.md) | `4315b888` | constraints are not enforced anywhere yet; when they are, each operation checks its own declaration |
| [shared-path-parameter-different-types](shared-path-parameter-different-types.md), [nullable-path-parameter](nullable-path-parameter.md) | `dfc4cf2d`, `956639e0` | a type without a word of its own (inline enum, union) is numbered; an inline type below the `slash` node of a trailing slash is still referenced under the wrong class (magento.com, `Company.Slash.Post.Body`) |
| [response-media-type-without-schema](response-media-type-without-schema.md) | `3e55150c`, and the two commits after `e0a84a6c` | malformed media type keys on responses still abort the document (`malformed-media-type-key-crash`) |
| [unregistered-status-codes-share-one-response-case](unregistered-status-codes-share-one-response-case.md) | `0862fee5` | the multi-content-type and error-dispatch shapes are covered by a golden only |
| [server-case-names-collide](server-case-names-collide.md), [server-case-name-not-identifier](server-case-name-not-identifier.md), [server-empty-description-empty-case-name](server-empty-description-empty-case-name.md) | `796be767` | names over 200 characters are not shortened |
| [path-literal-segment-loses-navigation-property](path-literal-segment-loses-navigation-property.md) | `3b80f367` | a digit- or separator-only parameter next to a literal of that name (`{1}`, `1`) fails a new check instead of compiling |

A second census after these commits (`exploration/build/census-2026-11-14b/`, taken before `01f1fe46` and
`fde99b78`): 457 generate and compile (was 445), 541 fail to compile (was 555), 387 fail in typed (was 380), none
fails to parse (was 5). The typed count rose because the five documents that used to fail parsing now stop at
other reported issues, and because of two regressions that `01f1fe46` and `fde99b78` then fixed; the four
documents involved were re-checked individually and are back at their earlier stage or better.

A third census after `68db4e01` and `02be578d` (`exploration/build/census-2026-11-14c/`): 468 generate and compile,
538 fail to compile, 385 fail in typed, 99 fail at emit, 46 fail at render. Two of the 468 and one render failure
were re-checked individually: the run recorded them one stage too early because the generator was rebuilt while it
ran. No document stops earlier than in the second census.

A fourth census after `f9555f8b`, `cae01e33` and `909e08bc` (`exploration/build/census-2026-11-14d/`): 550 generate
and compile, 457 fail to compile, 385 fail in typed, 99 fail at emit, 45 fail at render. 82 documents newly compile,
none stops earlier than before. The one compile error that names the new `Optional` type (alertersystem.com) comes
from two properties, `@id` and `id`, sharing one Kotlin name (`property-kotlin-name-collisions`).

A sixth census after `0862fee5` and `796be767` (`exploration/build/census-2026-11-14f/`; the fifth, `…e`, after the
chained path navigation, matched the fourth): 759 generate and compile, 250 fail to compile, 385 fail in typed, 97
fail at emit, 45 fail at render. 209 documents newly compile, none stops earlier than before. The largest remaining
compile families: `name contains illegal characters: :` (119 documents, literal path segments such as
`entities:search`) and `conflicting declarations` (92).

A seventh census after `3e55150c` (`exploration/build/census-2026-11-14g/`): 932 generate and compile, 301 fail to
compile, 160 fail in typed, 97 fail at emit, 46 fail at render. 174 documents went from typed to compiling and 51
from typed to a compile failure. One document regressed (plaid.com, a request content key `examples` that is no media
type); it is fixed and compiles again when re-checked alone. The largest remaining failures before compile:
`Duplicate generated files` (95, emit), `Conflicting shared path parameter node` (65, typed), refused parameters
(36, render).

An eighth census after the five commits above (`exploration/build/census-2026-11-15a/`): 1,128 generate and compile,
108 fail to compile, 159 fail in typed, 95 fail at emit, 46 fail at render. 196 documents newly compile, none stops
earlier than before. Largest remaining groups: `Duplicate generated files` (95, emit), `Conflicting shared path
parameter node` (65, typed), an unresolved inline type under a per-media-type response class such as
`PdfResponse.BadRequest.ErrorDescription` (43 apisetu.gov.in documents, compile; reached since `3e55150c`), refused
parameters (36, render), unresolved `$ref` pointers (22, typed), recursive `allOf` (20, typed).

After `a8e070fa` the 43 apisetu.gov.in documents compile (re-checked with `--filter apisetu.gov.in`, the other 138
apisetu documents unchanged), which makes 1,171 by that count; no full census was run for it.

A ninth census after `a8e070fa` and `040871b9` (`exploration/build/census-2026-11-15c/`): 1,238 generate and
compile, 61 fail to compile, 159 fail in typed, 30 fail at emit, 46 fail at render. 110 documents newly compile (45
compile -> pass, 65 emit -> pass), none stops earlier than before. The 30 left at emit are the other three
duplicate-file causes: Read/Write variants under one class name, the root client class against a path class, and two
schemas with one Kotlin name.

A tenth census after `4315b888`, `dfc4cf2d` and `956639e0` (`exploration/build/census-2026-11-15e/`): 1,286 generate
and compile, 68 fail to compile, 94 fail in typed, 34 fail at emit, 52 fail at render. No document stops earlier than
before. Of the 65 documents that stopped on a conflicting path parameter, 43 compile; the other 22 now reach a later
failure (12 compile, 4 duplicate files, 6 refused parameters). 5 more compile through the nullable path parameter fix.

An eleventh census after `1be3a8ed` (`exploration/build/census-2026-11-15f/`): 1,292 generate and compile, 62 fail to
compile, 94 fail in typed, 34 fail at emit, 52 fail at render. 7 documents with an enum parameter inside a composite
segment newly compile (it is a step per value now). One stopped earlier: gitea.io, where the value `patch` of
`/pulls/{index}.{diffType}` got the name of the `PATCH` operation of its parent. `dadbcc06` names such a step with its
separator (`dotPatch`); re-checked with `--filter`: gitea.io and googleapis.com:contentwarehouse compile, which makes
1,294 by that count, and amazonaws.com:s3 gets past the clash to another error.

Not reported yet, seen in that census:

- Fixed in `5ff2e4f4` (decided 2026-11-15): a path step after a slash with the name of an operation of its parent
  (`/standards/get` next to `GET /standards`) is one member with that operation, `api.standards.get()` and
  `api.standards.get.post()`, neither renamed. amazonaws.com:securityhub, amazonaws.com:guardduty and tokenjay.app
  compile (re-checked with `--filter`), which makes 1,297 by that count. Not covered: the same clash on the root
  client class, and a type nested in the operation with the name of one nested in the step (left as two members).
- Fixed in `8272a0cc`: the root client class is named from a title with `:` (8 documents).
- A form body that is a `$ref` to a schema with inline enum fields refers to `Post.Body.<Enum>` (6 documents).
- discourse.local: a generated class name longer than a file name can be (`…And…And….class`, permission denied).

A twelfth census after `dadbcc06`, `5ff2e4f4` and `8272a0cc` (`exploration/build/census-2026-11-15g/`): 1,322 generate
and compile, 49 fail to compile, 94 fail in typed, 28 fail at emit, 41 fail at render. 30 documents newly compile, none
stops earlier than before: 13 from compile (the `:` root class names and the operation-named steps), 6 from emit (the
root class with the name of a path class, see `client-root-class-collides-with-path-class.md`), and 11 from render
(`simpleNames must not contain empty items`: documents without a title, whose root class is now `Api`).

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
| [optional-nullable-property-cannot-send-explicit-null](optional-nullable-property-cannot-send-explicit-null.md) | An optional, nullable body property sends `null` or nothing depending on the caller's `Json`; `PATCH` cannot tell "clear" from "unchanged". Needs a design decision | not counted; 37 GitHub operations |
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
`integration-tests/README.md`). It still sends no requests, so it cannot catch a wrong URL or parameter. On this branch all three compile and pass their round-trip tests.
The three specs contain few of the constructs above: GitHub has the shadowed path parameter, the
context-variant duplicates and a schema-less request media type; YouTrack has `$ref` + `readOnly`;
OpenAI has integer enums, 3.1 `$ref` siblings and webhooks.
