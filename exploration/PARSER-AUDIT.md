# Parser corpus audit — 2026-11-01

Ran the current parser against all **1,547 cached OpenAPI 3 preferred-version
documents** from the pinned API Guru census. **1,542 parsed successfully; five
failed.** All successful models serialized and reparsed with model equality.
Comparing original JSON with serialized JSON nevertheless exposed silent data
loss: model equality cannot detect information already discarded on first decode.

No production code was changed. Existing uncommitted changes were preserved.
Five small characterization tests were added in
[`CorpusRoundTripAuditTest`](../parser/src/commonTest/kotlin/io/github/nomisrev/openapi/parser/CorpusRoundTripAuditTest.kt).
These deliberately assert current broken behavior; replace them with preservation
assertions when fixing the corresponding bugs.

## Confirmed findings

### 1. Schema references discard OpenAPI 3.1 sibling constraints

`ReferenceOr.Serializer` turns every object containing `$ref` into a reference
holding only the URI, `readOnly`, and `writeOnly`. Other fields disappear.

- Codat Commerce, key `8b250766ea0dafe5910f`,
  `/components/schemas/TaxComponent/allOf/2`:
  `{"$ref":"#/components/schemas/ModifiedDates","required":["name"]}`
  becomes `{"$ref":"#/components/schemas/ModifiedDates"}`.
- Adyen Stored Value, key `04d7320a11e52e2b6de4`,
  `/components/schemas/StoredValueBalanceCheckRequest/properties/amount` loses
  its description beside `$ref`.

This can weaken validation constraints, not merely remove documentation.
The raw comparison found 1,188 omitted sibling fields across 30 OpenAPI 3.1
documents, including 966 descriptions and one `required`. This total includes
extensions and dialect-inapplicable keywords; it is not a count of independently
validated bugs. OpenAPI 3.0 reference siblings were excluded from this claim.
OpenAPI 3.1 Schema Objects follow JSON Schema, including keywords alongside
`$ref`: [Schema Object](https://spec.openapis.org/oas/v3.1.0.html#schema-object).

### 2. Enum values lose their JSON types; structured values fail decoding

`Schema.enum: List<String?>?` coerces primitive numbers/booleans into strings and
rejects arrays/objects. M-Bus (`mbus.local`, key `072ee2df8c320e3c79db`),
`/components/schemas/baudrate`, has `type: integer` and
`enum: [300,600,1200,2400,4800,9600]`. The output has string enum members,
contradicting its integer type. The raw scan found changed enum arrays in 105
documents (1,261 locations), including some invalid input type combinations.

All five parse failures are this same model limitation:

| Source | Key | Interpretation |
| --- | --- | --- |
| Docker Engine | `72153fdeeb7f32d0f9f5` | Object enum members on an object schema are rejected. |
| PandaScore | `a16b7327554a85f8e46f` | Object enum members on an object schema are rejected. |
| Apacta | `45032aa34518e6bdb425` | Array enum member on a string schema; source type mismatch. |
| Enode | `6b310704012b29b07d4b` | Object enum member on a string schema; source type mismatch. |
| BC Job Posting | `9f0fc369baad2493ee6a` | Array enum members on an integer schema; source type mismatch. |

The existing census fixtures already reduce these failures. Do not count all
five as valid-source parser regressions. The remaining previously reported
paths-extension failure and two boolean-property-schema failures now parse.

### 3. Defaults and examples become strings

`ExampleValue.Serializer` stores primitive content or stringified JSON and then
encodes it as a string. `DefaultValue` is an alias of this same model.

- Icons8, key `015d29f8eb70c9a9df7b`: the category operation's `amount` parameter
  has `{"type":"number","default":25}`; output changes the default to `"25"`.
- Change API, key `00810433e137014fc48b`,
  `/paths/~1api~1v1~1donations~1carbon_calculate/get/responses/200/content/application~1json/example`:
  an object with `amount: 18` becomes a JSON-encoded string.
- Arrays of primitives likewise become arrays of strings.

The raw scan found changed `default` values in 327 documents (20,407 locations)
and changed `example` values in 576 documents (37,220 locations). Counts are
keyword comparisons, not a complete schema-aware validation. The reduced cases
demonstrate the bug independently. OpenAPI permits typed defaults and arbitrary
example values: [Schema Object](https://spec.openapis.org/oas/v3.0.3.html#schema-object),
[Media Type Object](https://spec.openapis.org/oas/v3.0.3.html#media-type-object).

### 4. Root extensions are silently discarded

`OpenAPI` exposes an `extensions` map but uses its generated serializer instead
of `KSerializerWithExtensions`. Normal top-level `x-*` keys are ignored.
AWS Migration Hub, key `00059ba6ca9e7ae965de`, loses `x-hasEquivalentPaths: true`;
the decoded `extensions` map is empty. Root extension loss occurs in 429
successfully parsed documents. This is separate from the now-working Paths
extension serializer. Root extensions are allowed by the
[OpenAPI Object](https://spec.openapis.org/oas/v3.0.3.html#openapi-object).

## Execution and limits

The cached selection contains 2,529 preferred versions: 981 Swagger 2 inputs
were excluded, and Microsoft Graph beta was unavailable because its original
download exceeded the runner's 64 MiB limit. This audit did not retry downloads
or cover historical versions. Index SHA-256:
`dfac835d2d1f13dfdb82723d72be1acf567df53c1b710bf6d68e28b795696e66`.

The normal runner build was attempted with:

```sh
./gradlew :parser:jvmTest :exploration:prepareCorpus
```

The parser jar built successfully. At that point the typed build failed on an
unresolved `peek` in `pipeline/BooleanSchemas.kt:15`. The parser suite ran 449
tests, with one failure: `json discriminator with mapping deserializes` contains
a missing comma after `propertyName`. Neither issue was changed by this audit.

To isolate parsing from the typed build, a temporary Java driver invoked the
current parser jar directly over the cached inputs, in one JVM:

```sh
java -Xmx2g -cp "$(cat exploration/build/runner/runtime.txt)" \
  exploration/build/parser-audit/ParserAudit.java exploration/build/parser-audit
python3 exploration/build/parser-audit/compare.py
./gradlew :parser:jvmTest --tests '*CorpusRoundTripAuditTest'
```

The five focused characterization tests pass. Full evidence is retained under
`exploration/build/parser-audit/` (ignored): `ParserAudit.java`, `compare.py`,
`inputs.txt`, `outcomes.tsv`, `differences.json`, original `specs/`, and serialized
`roundtrip/` documents. `manifest.json` records revision, dirty status, parser
source/jar hashes, index hash and all input hashes. The diff script walks matching
object keys and array positions; omitted defaults, array changes and arbitrary
payload keys prevent treating it as a general semantic equivalence checker.

This was JSON/JVM parser coverage, not a rerun of typed transformation,
rendering, compilation, full YAML parity or independent source-spec validation.
