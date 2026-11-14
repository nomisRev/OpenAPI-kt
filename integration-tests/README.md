# Integration tests

A separate Gradle build that `includeBuild("..")`s the generator. Each of `github`, `openai` and `youtrack` generates
a Ktor client from a spec in `../parser/src/commonTest/resources/specs/` into `src/main/kotlin`, compiles it, and runs
the example round-trip test below.

## Example round-trip

"It compiles" does not mean "it is right on the wire". OpenAPI specs carry their own test data: the `example` /
`examples` the API author wrote. The harness decodes each example with the generated model, re-encodes it, and compares
the two JSON trees. The spec is the oracle; there are no hand-written expectations.

```
./gradlew :openai:test                                  # round-trip one client
./gradlew :openai:test :youtrack:test :github:test      # all (github does not compile yet, see below)
./gradlew :openai:test -PupdateRoundTripBaseline=true   # accept the current findings as the new baseline
```

Output per project, under `<project>/build/reports/example-roundtrip/`: `<project>.md` (human readable: totals per
classification, findings grouped by failure signature) and `<project>.json` (machine readable).

### What is checked

Examples whose schema is a NAMED component schema, so the generated class is known:

- `example` / `examples[i]` on a component schema itself;
- media-type `example` / `examples.*.value` in responses and request bodies whose `schema` is a direct `$ref`;
- the same where the schema is `type: array` with `items` a direct `$ref` (the whole array is one example);
- an extension: `example` / `examples[i]` written on an inline property schema of a component schema
  (`/components/schemas/Foo/properties/bar/example`), decoded with the property serializer of the generated class.
  Most specs keep their examples here: without it OpenAI yields 10 examples and YouTrack none.

Everything else (inline schemas, non-JSON media types, `externalValue`) is counted under "skipped" in the report.
Each example is identified by the JSON Pointer of its value in the spec (`/paths/~1v1~1x/get/responses/200/content/application~1json/examples/foo/value`).

Each example is decoded with Ktor's `DefaultJson` (what the generated client's `ContentNegotiation { json() }` uses;
`ignoreUnknownKeys` is off) and re-encoded.

| classification | meaning |
| --- | --- |
| `ok` | re-encoded JSON equals the example |
| `decode-failed` | the generated serializer threw (exception class and first line of the message) |
| `lossy` | decoding worked, the re-encoded JSON differs (e.g. `$.rate: 300 -> "300" (number->string)`) |
| `no-class` | no generated class or serializer for the schema (e.g. a schema no operation uses) |
| `example-not-in-schema` | the only problem is example keys the schema does not declare |

Normalisations, and nothing else: key order; absent vs `null`; an added key whose value is a `default` the spec declares
for it; numerically equal numbers (`1` = `1.0`); date-time strings that denote the same instant (`...-04:00` = `...Z`,
which hides that the offset is not preserved). A number is never equal to a string.

Unknown keys are never ignored: the model rejects them. When the failure is the example's own fault, judged from the RAW
spec and not from the generator, the example is `example-not-in-schema` instead of `decode-failed`/`lossy`:

- the example has a key the schema does not declare (peeled off and retried, so it cannot hide a real failure deeper in it);
- the example omits a key the schema lists under `required`;
- the example has a JSON type the schema does not allow at that path (`null` for a non-nullable string, `20.04` for a
  string, an object where the schema is an array), or an enum value the schema does not list, or a malformed date-time;
- the schema is an array and the example is not.

A declared key the model dropped, or a type the schema does allow that the model rejects (`1000.0` for `integer`), stays
`decode-failed`. See `example-roundtrip/src/main/kotlin/.../JsonDiff.kt` and `RoundTrip.kt` for the exact rules.

### Baselines

`<project>/example-roundtrip-baseline.json` lists every known non-`ok` finding, sorted by id. The test fails when

- a finding appears that is not in the baseline (a regression, or a changed finding), or
- a baselined finding disappears (it was fixed, or the example is no longer collected),

so both show up as a diff. After reviewing, rewrite the baseline and commit it:

```
./gradlew :openai:test -PupdateRoundTripBaseline=true
```

### How it works

- `example-roundtrip/` is a small JVM library with the engine; the client projects add it as a `testImplementation`
  through `gradle/example-roundtrip.gradle.kts`, and each has a one-line `ExampleRoundTripTest`.
- The spec is read twice. Examples are read with kotlinx.serialization (JSON) or snakeyaml-engine (YAML) because the
  repo's parser keeps primitive examples as strings and kaml drops YAML quoting, which would erase the number-vs-string
  differences this harness looks for. The generator side (which class belongs to which schema) uses the repo's own
  `OpenAPI.fromJson/fromYaml`, `toApiTree()` and `NamingContext.toClassName()`, substituted from the included build.
- Because the harness links the generator's modules, all three projects use Kotlin 2.4.0 (the generator's metadata is
  2.4).
- Weakness: the renderer decides `FooRead` / `FooWrite` splits with an `internal` function. The harness computes the class
  name both ways with the real `toClassName` and takes the one that exists on the classpath.

### Status

- `openai`, `youtrack`: wired, baselines committed to the working tree.
- `github`: wired the same way, but the generated client does not compile (`issues/operation-parameter-shadows-path-parameter.md`),
  so its test cannot run until the generator is fixed. Its baseline was produced on a scratch copy whose three
  failing call sites were patched by hand; models are unaffected by that bug.
- `example-roundtrip/src/test` is a self-test of the harness against a hand-written integer enum; run it with
  `./gradlew :example-roundtrip:test`.
