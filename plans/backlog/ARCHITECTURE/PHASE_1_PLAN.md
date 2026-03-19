# PHASE 1: Pipeline Core

## Objective

Introduce the generation pipeline skeleton and diagnostics infrastructure without semantic behavior change.

## Scope

1. Define the phase model and orchestrator.
2. Define request/result envelopes.
3. Route existing generation path through the new shell.

## Non-Goals

1. No custom transformers yet.
2. No emitter refactor yet.

## Work Items

1. Add core pipeline types under renderer:
   - `GenerationRequest`
   - `GenerationResult`
   - `Diagnostic`
   - `DiagnosticSeverity`
   - `GenerationPhase`
   - `PipelineContext`
2. Add orchestrator:
   - `GenerationPipeline`
   - fixed phase sequence: `RAW_TRANSFORM`, `TO_TYPED`, `TYPED_TRANSFORM`, `PLAN`, `EMIT`
3. Add deterministic execution contract:
   - stable file ordering
   - stable diagnostic ordering
4. Add adapter that invokes current generation internals from within phase steps.
5. Keep public API behavior via delegation:
   - `OpenAPI.generate(config)` forwards into the pipeline.

## Suggested File Areas

1. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/pipeline/...`
2. Existing `Generate.kt` entrypoint delegation

## Verification Commands

1. `./gradlew :renderer:jvmTest`

## Exit Criteria

1. Existing tests pass unchanged.
2. Pipeline phases are observable in diagnostics/debug logs.
3. Public API remains functional.

