# Preferred-version census — 2026-11-01

Commands below record historical runs using the former Python driver. For the
Kotlin driver, use `./gradlew :exploration:run --args="..."` with the same flags;
see [README.md](README.md) for current usage and tests.

Completed all 2,529 preferred-version selections against revision
`d703a35db7e8ae91ecae6646159c43622bb68598` with a dirty checkout and the
existing XML-name and response-range parser fixes. No production fixes were
made for this census. The worker was prepared before the run.
Index SHA-256: `dfac835d2d1f13dfdb82723d72be1acf567df53c1b710bf6d68e28b795696e66`.

```sh
./gradlew :exploration:prepareCorpus
python3 exploration/corpus.py --jobs 4 --timeout 60 --skip-compile --output exploration/build/preferred-census
./gradlew :parser:jvmTest
```

| Stage | Passed | Failed |
| --- | ---: | ---: |
| Download | 2,528 | 1 |
| Parse OpenAPI 3 | 1,539 | 8 |
| Typed transformation | 1,148 | 391 |
| Render | 1,138 | 10 |
| Emit unique filenames | 1,035 | 103 |

981 downloaded Swagger 2 documents were counted as unsupported. The selected
index metadata describes 1,548 OpenAPI 3 documents and 981 Swagger 2 documents;
the older pilot report's 1,521/1,008 dialect split was not reproduced by this
selection. Microsoft Graph beta (`microsoft.com:graph-beta`, key
`5a702cd464f95111a2b1`) exceeded the harness's 64 MiB download cap after three
attempts. This is a harness limit, not a parser failure. There were no worker
timeouts or infrastructure errors. All 1,547 workers skipped compilation;
1,035 completed emission. Render diagnostics total 140 errors and 133 warnings,
which can accompany successful emission and need separate investigation.

## New parse failures and reductions

All eight parse failures are outside the earlier 300-document selection.
They reduce to three families; the JSON exceptions below identify the failure,
not an assertion that each complete source document is valid.

| Family | Documents | Reduction / interpretation |
| --- | ---: | --- |
| Structured enum values decoded as strings | 5 | Docker Engine and PandaScore use object schemas with object enum members. Apacta and Enode declare string schemas containing an array/object member; BC Job Posting declares integer with array members. Those three source type mismatches are tracked separately. |
| Paths extension decoded as a Path Item | 1 | Apicurio Registry has `paths.x-codegen-contextRoot: /apis/registry/v2`. The decoder expects an object at this key. |
| Boolean property schema decoded as a Schema object | 2 | Codat Sync for Expenses and Commerce are OpenAPI 3.1.0 and define a property literally named `additionalProperties` whose schema is `false`. This is distinct from the object-level additional-properties keyword, which already supports booleans. |

Seven small fixture pairs cover all eight documents under
[`parser/src/commonTest/resources/specs/census`](../parser/src/commonTest/resources/specs/census/README.md).
The fixture README records each source URL, version, JSON Pointer, corpus key,
SHA-256 and reduction choices. The source enum schemas were moved to a single
component and shortened while retaining the offending value kind and declared
type. Both Codat documents share one reduced shape.

`CensusRegressionTest` checks the current failure category/message in JSON and
YAML. These are passing characterization tests of unresolved limitations;
replace them with successful decoding and value-preservation assertions when
fixing each family. All **442 parser JVM tests pass**, including four new test
methods exercising all seven pairs. The corpus itself exercised JSON only.

For the next parser fix, the Paths Object extension case is the narrowest
scope: OpenAPI [permits extensions on Paths Objects](https://spec.openapis.org/oas/v3.0.3.html#paths-object),
while the current plain map attempts to deserialize every value as `PathItem`.
A fix needs an explicit extension preservation policy and must keep extensions
out of route generation. Structured enums require preserving arbitrary JSON
values rather than coercing them into strings, with corresponding typed-layer
handling. Boolean schemas are allowed by the
[OpenAPI 3.1 Schema Object](https://spec.openapis.org/oas/v3.1.0.html#schema-object)
and require a schema-model decision. No parser fix is included here.

Full evidence is ignored under `exploration/build/preferred-census/`:
`manifest.json`, `index.json`, `results.jsonl`, `summary.json`, cached original
`specs/`, and per-document phase journals/stack traces/generated sources.
The console log is `exploration/build/preferred-census-run.log`. Compilation,
generated-client runtime behavior, JS/native support and independent full-spec
validation were outside this run.

---

# Path naming and embedded templates — corpus investigation — 2026-11-01

The earlier “path/title identifier fix” report overstated the result. Its
successful compile run is historical evidence about that attempted renderer
change, not proof that generated clients preserve path-template semantics. The
hash-based public names and related renderer/test changes have been reverted;
the cases below remain unresolved.

## Distinct corpus cases

- **Literal path punctuation creates illegal Kotlin identifiers.** Google
  Knowledge Graph Search (`googleapis.com:kgsearch`, key
  `01ee76d8f766aa7fe472`) has `/v1/entities:search`; the original compiler log
  reports `Entities:search` as an illegal class name. Tag Manager (key
  `01585979764a10bc658e`) has `/tagmanager/v2/accounts/containers:lookup` and
  `/tagmanager/v2/{parent}/destinations:link`; its log likewise reports
  `Containers:lookup` and `Destinations:link`. These are path-derived naming
  failures, not parser failures.
- **API titles can independently create illegal root class names.** Icons8
  (`icons8.com`, key `015d29f8eb70c9a9df7b`) has title
  `Use a [New Version](https://icons8.github.io/icons8-docs/) Instead` and no
  colon-bearing paths. The generated root name includes `https:`, and the
  compiler rejects it. This needs a title naming/fallback policy, separate
  from route naming.
- **Embedded path parameters are a semantic/template issue as well as a naming
  issue.** IAM Credentials (key `0234d67be5e10080b0a3`) declares required
  parameter `name` for `/v1/{name}:generateAccessToken`; Service Management
  (key `05e6304d3f87768e910a`) declares `resource` for
  `/v1/{resource}:getIamPolicy`; Tag Manager also uses `/tagmanager/v2/{path}:publish`.
  `PathSegment.kt` currently interprets these whole segments as literals rather
  than substituting the embedded parameter. A successful compile would not
  demonstrate that the argument is exposed or that the request path is correct.

## Naming collisions and limits of the corpus scan

A raw-path scan of the cached stable-hash 300-document sample counted 7,626 path
keys (191 OpenAPI 3 and 109 Swagger 2 documents). It found concrete route-shape
cases that deserve tests, but not additional confirmed compiler failures:

- Conjur 5.3.0 (key `121777971409f0c67574`) has siblings
  `/authn/{account}/login` and `/authn/{account}/{login}/authenticate`
  (`exploration/build/pilot-300/specs/121777971409f0c67574.json:963,1085`).
  A normalization that drops parameter/literal identity maps both relevant
  children to `Login`.
- Google Compute v1 (key `0164daed08129de21a3c`) has sibling parameters
  `/projects/{project}/global/images/{image}` and
  `/projects/{project}/global/images/{resource}/getIamPolicy`
  (`exploration/build/pilot-300/specs/0164daed08129de21a3c.json:17225,17583`);
  160 raw sibling groups in 50 documents have multiple parameter names.
  Parameter names cannot safely be discarded by a generic “Path” name.
- Xero Accounting 2.9.4 (key `02948b7a536d33b0ae70`) declares
  `/Contacts/{ContactID}` and `/Contacts/{ContactNumber}` with `GET`
  (`exploration/build/pilot-300/specs/02948b7a536d33b0ae70.json:4071,4690`).
  There are 22 such same-template/different-parameter-name groups in 8
  documents; these may be invalid OpenAPI path definitions rather than
  valid-spec renderer failures.
- VTEX Catalog 1.0 (key `182cfc323e411b36cdf6`) declares `POST` for both
  `/api/catalog/pvt/collection` and `/api/catalog/pvt/collection/`
  (`exploration/build/pilot-300/specs/182cfc323e411b36cdf6.json:1457,1609`).
  The typed path segment model discards empty segments, so trailing-slash
  identity needs an explicit decision; the cached record did not reach
  compilation.

The scan found no raw sibling literal pair differing only by `-` and `_`, and
no raw operation/child naming collision under the renderer's current HTTP
method naming. Amazon Macie 2 (key `1329c446be8d50b4de16`) has `POST
/custom-data-identifiers` and `POST /custom-data-identifiers/get`, a near-miss
with child `get` rather than a collision; its saved compiler error is duplicate
`UnknownStatusCode` declarations. These are bounded observations, not proof of
collision freedom: enum expansion, referenced path items, and typed-tree
identity merging are outside the raw sibling scan. The sample is the preferred-
version pilot, not a representative census of all API Guru documents.

The original per-document compiler logs are under `exploration/build/pilot/results/`;
the 300-document raw specs and outcomes are under `exploration/build/pilot-300/`.
The later attempted-fix run under `exploration/build/path-title-fix-300-sha256/`
records successful compilation for the five original representatives, but did
not verify parameter substitution, request URLs, or generated API stability.

---

# Response-code range fix verification — 2026-11-01

`Responses.responses` now uses one `Map<StatusCodeOrRange, ReferenceOr<Response>>`
for exact codes and OpenAPI response-code ranges (`1XX` through `5XX`). The
`StatusCodeOrRange.Code` and validated `StatusCodeOrRange.Range` variants keep an
exact response distinct from its encompassing range. JSON and YAML parsing
retain responses and references; JSON serialization round-trips codes, ranges,
defaults, and extensions. Typed transformation explicitly rejects range-bearing
operations with the range and operation path rather than silently generating
incomplete clients.

```sh
./gradlew :parser:jvmTest --rerun-tasks
./gradlew :typed:jvmTest --rerun-tasks
./gradlew :renderer:compileKotlinJvm
./gradlew :gradle-plugin:compileKotlin
python3 exploration/corpus.py --limit 300 --jobs 4 --timeout 60 --skip-compile --output exploration/build/response-ranges-300
```

Both JVM test tasks passed, including the range conversion guard test, and the
renderer and Gradle plugin compile tasks passed. The existing disabled
`ReturnSpec` tests for missing media schemas remain disabled; they are unrelated
to response ranges.

The rerun used the cached index and specs from `exploration/build/pilot-300`.
All 300 document keys and input hashes match the XML-name-fix baseline. Parse
passes increased from 189 to 191, and the only changed parse outcomes are
TomTom Search (`13051cf9256153b7c737`) and Adobe AEM
(`14d821f7967d48e181f6`), each changing from parse failure to parse success.
Both proceed to typed transformation and fail with the intentional unsupported
range diagnostic (`5XX`) and their operation paths. No parse regressions were
observed. Compilation was skipped; this verifies parser handling, not generated
client correctness or typed support for ranged responses.

The run summary and per-document phase logs are under
`exploration/build/response-ranges-300/`.
These corpus measurements are retained from the initial parser range fix; the
current unified-model refactor was validated with parser/typed tests and
downstream compilation rather than another corpus run.

---

# XML optional-name fix verification — 2026-11-01

Changed `Xml.name` to `String? = null` and added regressions for a schema's
`xml: {attribute: true}` in JSON/YAML and an empty XML object in both formats.
All 433 parser JVM tests passed.

Rebuilt the exploration worker and reran the frozen 300-document sample using
the cached index and inputs from `build/pilot-300`:

```sh
./gradlew :parser:jvmTest :exploration:prepareCorpus
python3 exploration/corpus.py --limit 300 --jobs 4 --timeout 60 --skip-compile --output exploration/build/xml-name-fix-300
```

All 300 input hashes match the baseline. Parse passes increased from 159 to 189;
all 30 XML-name failures now pass parsing, with no parse regressions. The two
remaining parse failures are the previously observed `5XX` response-code cases.
The same 109 Swagger 2 inputs remain unsupported. Of the 30 newly parsed inputs,
29 fail at typed transformation and one passes generation through emission.
Generated-client compilation was skipped; this verifies parsing and does not
establish end-to-end client correctness.

Results, phase logs and the baseline comparison are under
`build/xml-name-fix-300/`, including `results.jsonl` and `comparison.json`.

---

# Expanded API Guru pilot — 300 specs

Expanded on 2026-10-31 using the same frozen index, revision and default pipeline
as the original 60-spec run below. The sample includes 102 provider prefixes.
All original 60 input hashes and worker/compilation outcomes are unchanged.
No parser, typed or renderer fixes were applied.

```sh
./gradlew :exploration:prepareCorpus
python3 exploration/corpus.py --limit 300 --jobs 4 --timeout 60 --output exploration/build/pilot-300
```

All 300 documents downloaded (81,621,598 bytes). Actual document dialects are
191 OpenAPI 3 and 109 Swagger 2. The catalog incorrectly labels Billbee and
Netatmo as Swagger 2; Spotify's minor OpenAPI version also differs. The runner
uses the document's own version. Four inputs are OpenAPI 3.1.

| Stage | Original 60: passed / failed | Expanded 300: passed / failed |
| --- | ---: | ---: |
| Parse | 28 / 7 | 159 / 32 |
| Typed transformation | 22 / 6 | 139 / 20 |
| Render | 22 / 0 | 137 / 2 |
| Emit unique filenames | 21 / 1 | 124 / 13 |
| Compile JVM Kotlin + serialization | 8 / 13 | 50 / 74 |

Of 191 OpenAPI 3 documents, 50 compiled and 141 failed at a pipeline stage or
compilation. There were no timeouts or remaining download/infrastructure failures.
The additional 240 inputs account for 156 more OpenAPI 3 documents, 42 more
compilation passes, and 114 more failures. These counts do not establish source
validity or semantic correctness of generated clients.

There were 19 generation error diagnostics across four documents (NetLicensing,
Twilio Messaging, Studio and Conversations), plus 43 warnings for Telegram.
These all concern unsupported form-body variants/fields. All five documents
also failed compilation; none of the 50 compilation passes had diagnostics.
Counts describe individual diagnostics, not distinct bugs.

## New findings and more precise failure categories

Evidence lives under `build/pilot-300/results/<key>/` and inputs under
`build/pilot-300/specs/<key>.json`. Inspect `compile.log`, `failure.txt`, and the
`sources/` directory for the examples below. Findings are observations; suggested
causes that still need reduced regression fixtures are identified explicitly.

| Finding | Evidence / representative |
| --- | --- |
| Response ranges fail JSON parsing — **Parser fixed; typed generation unsupported** | TomTom Search (`13051cf9256153b7c737`) and Adobe AEM (`14d821f7967d48e181f6`) now parse `5XX`; typed transformation rejects these ranges explicitly. OpenAPI permits [response-code ranges](https://spec.openapis.org/oas/v3.0.3.html#responses-object). See the response-code range fix verification above. |
| Local schema aliases enter the unsupported remote-schema branch | BBC (`0805356d6bc5b6ebb665`) and papiNet Order Status (`0bf408c989cb6d5cdf61`). `Registry.ScopeImpl.peek` calls `remoteSchema` when a component is itself a reference, even when that reference begins with `#/components/schemas/`. |
| Request argument shadows the HTTP client | CDC Prime Data Hub (`0a0deda19c15c1509b2b`): generated `Reports.Post.invoke(client: String, ...)` calls `client.post(...)` on that String. Qualify the client property or allocate collision-free argument names. |
| Keyword escaping is inconsistent between declarations and use sites | Flat (`0c343c4e447589dc5b7e`): the `class` argument is backtick-escaped in its declaration but passed as bare `class` in `ClassPath(client, class)` and subsequent constructors. |
| Union cases receive duplicate class names | GitHub GHES 3.5 (`0862e2c0ef57a888873d`) generates three `CaseJsonElement` classes inside `Gists.GistIdPath.Patch.Files`. This is distinct from duplicate HTTP response names. Other GitHub versions also fail. Case naming needs a uniqueness check after transformation. |
| Server naming ignores differences that distinguish URLs | SportsData soccer stats (`107c18e1afc249df36d7`) emits two `AzureApi` objects for HTTP and HTTPS servers. Soccer projections and NBA/NFL play-by-play show the same compiler symptom. |
| Flattened body fields collide with operation parameters | AWS Kinesis archived media (`0d16276143e9b9d85fdb`), `GetImages.kt`, declares `maxResults` twice (`Long?` and `String?`) and `nextToken` twice. The body constructor also receives a mismatched type. This is distinct from duplicate response classes. |
| Empty title reaches KotlinPoet as an empty class name | GoDaddy OTE Orders (`1dac2cc33d9ba6b031b5`) and Aftermarket (`20760f59d4129ddf0787`) fail during render with `simpleNames must not contain empty items`. Orders has `info.title: ""`. A fallback name or explicit diagnostic is needed. |
| Form values need scalar conversion as well as null handling | NetLicensing (`08038dd53b0d319bee82`) passes a Boolean to an API expecting String; Twilio Studio (`14608c4cf7fae941e03b`) passes `ExecutionEnumStatus`. Nullable String handling alone will not resolve the form-generation failures. |

## Source-spec and unresolved cases

- Nexmo Reports (`08b5ce21c4b3cc70f5f6`) emits nonexistent `Status.None`, but
  the source itself declares default `none` outside its enum values. Treat this
  as invalid source/default validation work, not evidence that `none` should
  become an accepted enum value.
- VTEX Catalog (`182cfc323e411b36cdf6`) fails on content type `form-data`.
  This is a malformed media type; keep it separate from valid-spec regressions.
- Billbee (`0c0feff04882be4953be`) fails because a URL-encoded body is not
  represented as an object. This still needs source-shape reduction and review.
- Shared-path compatibility and recursive transformation failures still need
  reduced fixtures. Stack overflow now affects AWS Lex, EMR Containers and DynamoDB.

## Existing issues at larger scale

XML `name` accounts for 30 parse failures, and `5XX` for the other two.
Exact duplicate filenames affect 13 documents, including an API-file collision
in Apideck Customer Support, not only model collisions. Compiler logs report
conflicting declarations in 39 documents and illegal colon names in 22; the
colon-name group includes distinct path-derived and title-derived identifiers.
These compiler groups overlap and must not be summed as independent affected-
document counts.

## Harness correction and artifacts

Europeana's catalog URL contains the literal version `version unknown`. The
initial download failed before making a request. The downloader now percent-encodes
URL paths while preserving existing escapes. A regression test verifies raw
spaces, existing `%20`, and cache reuse:

```sh
python3 -m unittest discover -s exploration -p 'test_*.py'
```

Only Europeana was retried, successfully, with `--filter europeana.eu` into
`build/pilot-300-url-retry`. Its corrected result and cached input were merged
into the expanded run. `results-before-url-fix.jsonl` preserves the original
record, and `reruns.json` records the correction and retry manifest. The corrected
`results.jsonl` and `summary.json` are the authoritative totals above; the original
console log predates that single-record correction.

The original pilot is preserved under `build/pilot`. Full results, source hashes,
selection, generated code and compiler logs for this expansion are under
`build/pilot-300`. Generation and compilation ran with the same 60-second timeout
and four workers as the initial pilot. JSON/JVM/semantic coverage limitations
from the original report still apply.

---

# API Guru pilot — 2026-10-31

Executed against revision `3472e3357538fa3f957d54abc29fd4c2ec27346c` with an
uncommitted exploration harness. No parser/typed/renderer fixes were applied.

The downloaded API Guru index contains 2,529 APIs / 3,992 versions. Preferred
versions include 1,521 OpenAPI 3 documents and 1,008 Swagger 2 documents.
Index SHA-256: `dfac835d2d1f13dfdb82723d72be1acf567df53c1b710bf6d68e28b795696e66`.

```sh
./gradlew :exploration:prepareCorpus
python3 exploration/corpus.py --limit 60 --jobs 4 --timeout 60 --output exploration/build/pilot
```

This pilot ran 60 preferred versions in stable hash order, not the entire
catalog. All 60 downloaded (13,091,479 bytes total). Swagger 2 accounted for 25
and was excluded from the OpenAPI 3 pipeline. No timeouts occurred.

| Stage | Passed | Failed |
| --- | ---: | ---: |
| Parse | 28 | 7 |
| Typed transformation | 22 | 6 |
| Render | 22 | 0 |
| Emit unique filenames | 21 | 1 |
| Compile generated JVM Kotlin + serialization | 8 | 13 |

Thus 27 of 35 OpenAPI 3 inputs failed before or during compilation. None of the
successful render calls returned warnings/errors, including those whose emitted
code failed compilation. Passing compilation is not a semantic correctness claim.

## Findings to turn into regression tests

Keys below identify `build/pilot/specs/<key>.json` and
`build/pilot/results/<key>/` (full stack traces, source and compiler logs).
Source URLs, versions, SHA-256 hashes and phase results are in
`build/pilot/results.jsonl`; the index and selection are in `manifest.json`.

| Priority | Finding | Evidence / representative |
| --- | --- | --- |
| High | Parser requires optional XML `name` — **Fixed** | Seven API Setu documents failed with `MissingFieldException`. `apisetu.gov.in:hsbte`, key `02a240ddf8fff7ee5ff2`, contains `xml: {attribute: true}`. `parser/.../Xml.kt` now declares `name: String? = null`, with regression tests in `XmlTest.kt`. The official [XML example](https://spec.openapis.org/oas/v3.0.3.html#xml-object) explicitly uses this shape without `name`. See the XML optional-name fix verification above. |
| High | Literal path punctuation produces illegal Kotlin identifiers | Google Knowledge Graph Search (`01ee76d8f766aa7fe472`), `/v1/entities:search`, emits `Entities:search`; Tag Manager (`01585979764a10bc658e`) emits `Containers:lookup` and `Destinations:link`. Original compiler logs show illegal `:` class names. See the corpus investigation above. |
| High | API title produces an illegal root identifier | Icons8 (`015d29f8eb70c9a9df7b`) has a Markdown link in `info.title`; the generated root class includes the link URL's colon. It has no colon-bearing paths, so this is distinct from path segment naming. |
| High | Embedded path parameters are treated as literal segments | IAM Credentials (`0234d67be5e10080b0a3`), Service Management (`05e6304d3f87768e910a`), and Tag Manager (`01585979764a10bc658e`) use templates such as `/{name}:generateAccessToken`. Preserve and substitute the parameter and retain the literal suffix; successful compilation alone is insufficient. |
| Investigate | Route naming and path identity can be non-injective | Raw corpus examples include Conjur's literal/parameter `Login` normalization, Xero's same-template parameter-name pairs, and VTEX's trailing-slash pair. These are raw observations, not confirmed valid-spec compilation failures; see the collision scan above. |
| High | Distinct response codes collapse to duplicate response classes | Four AWS compile failures. AWSMigrationHub, key `00059ba6ca9e7ae965de`, has responses 480–488 and emits repeated `UnknownStatusCode` declarations. Also transcribe, mediapackage-vod, elasticloadbalancingv2. Preserve distinct numeric codes in naming. |
| High | Empty string enum value emits an empty backtick identifier | Two Xero compile failures. Payroll AU, key `0438fe542a6d52d592e5`, emits an empty identifier in `LeaveLineCalculationType.kt`. Accounting shows the same syntax failure. Generate a stable nonempty name while preserving the empty wire string. |
| High | Optional form fields are passed to non-null `Parameters.append` | Twilio Flex v2, key `058599c3b3b881575593`, emits `append("ChatFriendlyName", chatFriendlyName)` where the parameter is `String? = null`. Compiler rejects this and two other optional fields. Omit absent fields with a null check. |
| High | Recursive schema transformation overflows the stack | AWS Lex models v2, key `0065ccfb7b4f60497f81`. `failure.txt` repeats registry resolution, object transformation and `allOf` frames. Recursion tracking needs a reduced fixture; exact cycle not yet isolated. |
| High | Multiple model files have the same qualified name | Google contactcenterinsights, key `01e58fbb6bd61725a33d`. The harness detects three duplicate names before writing, including `GoogleCloudContactcenterinsightsV1AnalysisResult`. Normal writing could overwrite a model. Whether the duplicate bodies are identical still needs investigation. |
| High | Case-only schema names collide on this filesystem | Google compute, key `0164daed08129de21a3c`, defines both `HTTPHealthCheck` / `HttpHealthCheck` and HTTPS equivalents. On this macOS filesystem, emitted paths collide; compilation reports missing `HTTPHealthCheck` / `HTTPSHealthCheck`. Exact-name duplicate detection does not catch this. Recheck on a case-sensitive filesystem and choose portable naming. |
| Medium | Nested JSON Pointer schema reference is treated as a component name | VTEX Profile-System, key `02204817aafb791f57d4`, fails looking up `profile/properties/birthDate` among top-level component keys. Needs pointer traversal, not only component-name lookup. |
| Medium | Response media types without schemas abort transformation | change.local (`00810433e137014fc48b`), openaq.local and clubhouseapi.com. The [Media Type Object](https://spec.openapis.org/oas/v3.0.3.html#media-type-object) does not require `schema`; current `routes/Return.kt` does. Decide on an explicit untyped body representation. |
| Investigate | Shared path parameter compatibility rejects two String models | Twilio supersim v1, key `064f1b175c64fdf2b65e`. Both printed models are `String`, but constraints/metadata may differ. Compare the source definitions before deciding whether this is a bug or an intentional limitation. |

Compilation passed for Vonage Account, HERE Positioning, Extpose, Adyen Stored
Value, Appwrite Server, Interzoid Country Standard, Apptigent, and Pay1 Link.
These provide useful positive checks for the compiler classpath and plugin.

## Follow-up scope

First reduce the high-priority failures and add focused regressions. Run the
preferred-version census with `--skip-compile` for broader phase coverage, then
compile the successful emitters. Use `--all-versions` when historical coverage
is useful; repeated versions will otherwise overweight a handful of providers.

This run exercised JSON input, reachable schemas, the default transformation
pipeline, and JVM output. It did not validate all source specs independently,
check YAML parity, execute generated serializers or HTTP clients, test JS/native,
or establish whether generated APIs preserve all source semantics. The checked-in
report records observations; the full corpus and generated files remain ignored.
