# A server description or host that is not a Kotlin identifier renders an invalid server case

**Status:** fixed on branch worktree-agent-af3ec27e9e0840678 (based on openapi-3.2-streaming), with `server-case-names-collide.md`. `Server.toServerCaseName` is replaced by `List<Server>.serverNames()` (`renderer/.../ServerNames.kt`), computed once per server list for the type, the default argument and the diagnostics. Servers whose names clash are told apart by their URL: scheme-only pairs nest by scheme (`ApiServer.ApiExample.Https`), other differences append the URL words the others lack (`ApiLinodeV4`, `ApiLinodeV4beta`), identical servers get a number and a warning. Names are identifiers (non-alphanumerics are word breaks, `Server1Password` for a leading digit, `Default` for nothing, `CustomServer` for `Custom`). Golden `client/factory-server-names`, `ServerNamesTest`, `ServerNamesRuntimeTest`. The default argument is built from the case's `ClassName` (`%T`), so declaration and use cannot drift apart.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 6 of 1,547 documents: 4 through the unescaped default argument (apispot.io:whois, 1password.com:events, gov.bc.ca:geocoder, pdfblocks.com), 3 through a `:` in the class name (apispot.io:whois again, patrowl.local, airbyte.local:config)
**Source spec valid:** yes — `description` is free text, `http://localhost:5000` is a valid URL

## Symptom

Two symptoms of one unsanitised name:

1. The first server's name is written raw into the client's default argument, while the declaration is backtick-escaped by KotlinPoet:
   ```kotlin
   public constructor(server: GeocoderRESTAPIServer = GeocoderRESTAPIServer.Production(gated), block: …)   // unresolved reference 'Production', 'gated'
   public constructor(server: EventsAPIServer = EventsAPIServer.1Password, block: …)                        // primary constructor call expected, conflicting overloads, syntax errors
   public constructor(server: BulkWHOISAPIServer = BulkWHOISAPIServer.Localhost:5000, block: …)             // unresolved reference 'Localhost', Parameter name expected
   …
   public data object `Production(gated)` : GeocoderRESTAPIServer
   ```
2. Any server (first or not) whose name contains a character the JVM forbids in a class name fails at the declaration even though it is escaped: ``public data object `Localhost:5000` `` → `name contains illegal characters: :` (the other investigator's error family; noted here because the fix is the same function).

## Minimal reproducer

`<scratchpad>/repro/server-case-name-not-identifier.json`:

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},
 "servers":[{"description":"Production (gated)","url":"https://a.example.com"},
            {"url":"http://localhost:5000"},
            {"description":"1Password","url":"https://b.example.com"}],
 "paths":{"/ping":{"get":{"responses":{"200":{"description":"ok"}}}}}}
```

```
worker exit=0 phases: parse pass, typed pass, render pass, emit pass
diagnostics: []
compile exit=1
api/T.kt:18:48: error: unresolved reference 'Production'.
api/T.kt:18:59: error: unresolved reference 'gated'.
api/T.kt:26:48: error: unresolved reference 'Production'.
api/T.kt:26:59: error: unresolved reference 'gated'.
api/T.kt:46:22: error: name contains illegal characters: :.
```

```kotlin
18:  public constructor(server: TServer = TServer.Production(gated), block: HttpClientConfig<*>.() -> Unit) : this(HttpClient {
42:  public data object `Production(gated)` : TServer {
46:  public data object `Localhost:5000` : TServer {
50:  public data object `1Password` : TServer {
```

`<scratchpad>/repro/server-case-name-not-identifier-digit.json` (first server described "1Password") gives the 1password.com cascade: `conflicting overloads`, `primary constructor call expected`, `initializer type mismatch: expected 'TServer', actual 'Unit'`, `classifier 'interface TServer : Any' does not have a companion object`.

## What the spec says

OAS 3.0.3 §4.7.5 Server Object: `description` is "An optional string describing the host designated by the URL. CommonMark syntax MAY be used for rich text representation." It is prose, not a name; it can be a sentence ("Global endpoint (default)."), start with a digit, or be multi-line Markdown.

## Root cause

Traced, `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/FactoryRenderer.kt`:

- `Server.toServerCaseName` (lines 347-360) returns `description.toPascalCase()` — `toPascalCase` (`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/StringUtils.kt:31`) only splits words; `(`, `)`, `:`, `.` and a leading digit survive. The URL fallback splits on `/ . -` only, so a port stays (`Localhost:5000`).
- `buildSecondaryConstructors` (lines 62-70) builds the default with `CodeBlock.of("%T.%L", serverClassName, defaultCaseName)`: `%L` is a literal, so no backticks. That is symptom 1.
- `toServerCase` (line 247) `TypeSpec.objectBuilder(caseName)` escapes with backticks, which hides the problem at the declaration unless the character is JVM-illegal (`. ; [ ] / < > :`). That is symptom 2.

## Correct final API

A server case name is an ordinary PascalCase identifier, never backticked, derived from the description when it is short and name-like, else from the URL:

```kotlin
public sealed interface GeocoderRESTAPIServer {
  /** Production (gated) */
  public data object ProductionGated : GeocoderRESTAPIServer { override val url: String = "https://geocoder.api.gov.bc.ca/" }
  …
}
// http://localhost:5000  -> Localhost5000
// "1Password"            -> `_1Password` is ugly; prefer a letter-led form such as OnePassword / Server1Password (decision below)
public constructor(server: GeocoderRESTAPIServer = GeocoderRESTAPIServer.ProductionGated, …)
```

The original description goes into KDoc on the case, so nothing is lost. Wire behaviour unchanged.

## Fix slice

- renderer: sanitise in `toServerCaseName` (drop every character outside `[A-Za-z0-9_]` as a word break before PascalCasing; prefix when the result starts with a digit or is empty or a keyword), and compute names for the whole server list so uniqueness (`issues/server-case-names-collide.md`) is handled in the same pass.
- renderer: build the default argument from the nested `ClassName` (`%T` with `serverClassName.nestedClass(name)`) instead of `%L` text, so declaration and use site cannot drift apart again.
- Open decisions: prefix for a leading digit (`_1Password`, `Server1Password`); a cap on description length before falling back to the URL (descriptions can be whole sentences; the corpus has "Global endpoint (default)." → `GlobalEndpointDefault`); whether renaming existing valid names is acceptable (it is not needed: only names that are invalid today change).

## Proof

- Golden `ClientSpec` case in `renderer/src/jvmTest/resources/kotlinTestData/client/` (beside `factory-servers/`) with the three servers of the reproducer; assert no backticks in the server type and the default argument equals the declared name.
- Compile proof: the reproducer above must reach `compile exit=0`.
- integration-tests do not cover it: `github.json`, `youtrack.json`, `openai.yaml` each have a single variable-less server, so no server type is generated for any of them.

## Affected documents

Default argument unescaped (4): apispot.io:whois [34bd264da0b5af3913e7] (`Localhost:5000`), 1password.com:events [b381d2f40b144d4284da] (`1Password`), gov.bc.ca:geocoder [492eb553b60242870f61] (`Production(gated)`; also has composite path segments), pdfblocks.com [eaaf8efa58806a0c73b9] (`GlobalEndpoint(default)`; its only error). Illegal `:` in a case name (3): apispot.io:whois, patrowl.local [48ac00becfae8b6c7725] (`Localhost:5001Engines`; its only error family), airbyte.local:config [dc2e32d2103086b38b28] (`Localhost:8000Api`; its only error). Total 6 documents.
