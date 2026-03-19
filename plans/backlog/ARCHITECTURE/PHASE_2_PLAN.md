# PHASE 2: Raw Transform Stage

## Objective

Create first-class raw-spec transformers so users can patch malformed OpenAPI documents before typed resolution.

## Scope

1. Add raw transformer SPI.
2. Add deterministic registration and ordering.
3. Extract raw repair behavior into explicit built-in transformers.

## Non-Goals

1. No typed transformer SPI yet.
2. No render-plan IR yet.

## Work Items

1. Add SPI contract:
   - `@ExperimentalOpenApiGenerationApi interface OpenApiTransformer`
   - required members: `id`, `priority`
   - method: `suspend fun transform(input: OpenAPI, ctx: RawTransformContext): OpenAPI`
2. Add `RawTransformContext`:
   - diagnostics sink
   - shared metadata map
   - render config view (read-only)
3. Add raw transformer execution in pipeline phase `RAW_TRANSFORM`.
4. Add built-in raw transformers for known bad-spec cases:
   - missing path parameter declaration repair
   - defensive schema normalization needed before typed conversion
5. Add ordering guarantees:
   - built-ins first by default
   - then user transformers by `(priority, id)`
6. Add failure semantics:
   - strict mode: error diagnostic fails generation
   - lenient mode: warning + continue when safe

## Suggested File Areas

1. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/pipeline/raw/...`
2. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/pipeline/core/...`

## Verification Commands

1. `./gradlew :renderer:jvmTest`

## Exit Criteria

1. Raw transformers can be registered and executed.
2. Built-in repair transformers are tested.
3. Baseline valid specs remain unaffected.

