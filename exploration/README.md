# API Guru corpus exploration

Opt-in exploration; ordinary builds/tests do not download or run the corpus.
Requires Java and the repository's Gradle toolchain. The Kotlin driver uses Ktor
for downloads and KotlinX Serialization for JSON.

See [FINDINGS.md](FINDINGS.md) for the completed preferred-version census and 60-/300-spec pilots,
phase counts, and representative failures.

```sh
./gradlew :exploration:run --args="--limit 60 --jobs 2"
# Expanded sample, including compilation:
./gradlew :exploration:run --args="--limit 300 --jobs 4 --timeout 60 --output exploration/build/pilot-300"
# Preferred version of every API (default):
./gradlew :exploration:run --args="--jobs 2"
# Every historical version:
./gradlew :exploration:run --args="--all-versions --jobs 2"
# Focus on a provider/spec:
./gradlew :exploration:run --args="--filter change.local"
```

Each invocation builds the driver and worker, then runs fresh transformations
against cached documents. Use a new `--output` directory
for a fresh index snapshot or to preserve another run's evidence. Keep selection
flags identical for a repeat run. `--fetch-only` downloads without launching
worker JVMs; `--skip-compile` enables a faster generation census.

Selection defaults to preferred versions, ordered by a stable hash of API ID and
version, so `--limit` does not simply select an alphabetical block of AWS specs.
This is reproducible sampling, not stratification by provider or feature.
Swagger 2 is downloaded and counted as unsupported; no conversion is performed.
The original JSON documents are passed directly to the parser.

Phases:

1. Download: three attempts, timeouts, 64 MiB cap, explicit user agent.
2. Parse: `OpenAPI.fromJson`.
3. Typed: `OpenAPI.toApiTree` with default preprocessing and title-derived names.
4. Render: `renderWithDiagnostics` for JVM, preserving errors and warnings.
5. Emit: reject duplicate output filenames before writing KotlinPoet files.
6. Compile: Kotlin compiler and serialization plugin matching the repository,
   with Ktor, serialization, and datetime dependencies. Generated code is never
   executed and no API endpoints are called.

Each document gets an isolated JVM with a 1 GiB heap. Generation and compilation
each have a `--timeout` (120 seconds by default). A worker crash or timeout does
not stop the corpus. Keep `--jobs` low enough for available RAM. Only reachable
schemas participate in `toApiTree`; this does not check every unused component.
JVM compilation does not establish JS/native compatibility or wire correctness.
Error diagnostics can accompany successfully emitted/compiled code and must be
triaged separately. Fetch/infrastructure failures are not parser defects.

Artifacts under `exploration/build/corpus` (ignored by Git):

- `index.json`, `manifest.json`: pinned directory snapshot, selection, revision,
  dirty-worktree flag, command, and index hash.
- `specs/<key>.json`: unmodified cached inputs; content hashes in results.
- `results.jsonl`: flushed result per completion, including phase, exception,
  diagnostic, compilation outcome, and source identity.
- `results/<key>/`: phase journal, full stack trace, worker/compiler logs,
  generated sources, compiler arguments, and compiled classes.
- `summary.json`: phase counts and exception/message groups with example IDs.

Timeouts retain `active-phase.txt` to identify the last entered phase. Grouping
is heuristic; similar failures can have different messages. Inspect compiler
logs to distinguish harness/dependency issues from invalid generated code.
Interrupted runs retain completed JSONL records; they are not automatically
resumed. Cached documents are reused, but results are recomputed.

For semantic bugs, reduce a corpus example to a local fixture, then add a
focused regression covering the typed model or generated serialization/HTTP
behavior. Compilation alone cannot detect lost constraints, incorrect wire
names, wrong parameter encoding, or silently dropped operations. Corpus failures
are evidence to investigate, not proof that the source spec is valid.

Run the offline driver regression tests with `./gradlew :exploration:test`.
