# PHASE 3: Typed Transform Stage

## Objective

Create typed-model transformers so users can alter `ApiTree` semantics before rendering.

## Scope

1. Add typed transformer SPI.
2. Integrate typed transforms into pipeline execution.
3. Add built-in typed normalization passes as explicit components.

## Non-Goals

1. No emitter migration in this phase.
2. No public API cutover in this phase.

## Work Items

1. Add SPI contract:
   - `@ExperimentalOpenApiGenerationApi interface ApiTreeTransformer`
   - required members: `id`, `priority`
   - method: `suspend fun transform(input: ApiTree, ctx: TypedTransformContext): ApiTree`
2. Add `TypedTransformContext`:
   - diagnostics sink
   - shared metadata map
   - render config
3. Add typed transformer execution in `TYPED_TRANSFORM` phase.
4. Add built-in typed normalization passes:
   - route shape normalization for rendering
   - naming stabilization for deterministic emitted class names
5. Add typed invariants + diagnostics:
   - unique operation key checks per path node
   - invalid inline naming collisions

## Suggested File Areas

1. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/pipeline/typed/...`

## Verification Commands

1. `./gradlew :typed:allTests`
2. `./gradlew :renderer:jvmTest`

## Exit Criteria

1. Typed transformers execute and can mutate `ApiTree`.
2. Normalization passes are explicit and tested.
3. Existing generation scenarios remain green.

