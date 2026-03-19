# PHASE 6: Cutover and API Integration

## Objective

Make the new pipeline architecture the default and retire legacy execution paths.

## Scope

1. Switch top-level generation APIs to pipeline-backed implementation.
2. Preserve convenient API surface where practical.
3. Remove obsolete code paths.

## Non-Goals

1. No SPI stabilization in this phase.
2. No new backend targets.

## Work Items

1. Switch `OpenAPI.generate(config)` to call `GenerationPipeline`.
2. Keep convenience API entrypoints but delegate internally.
3. Add request-level transformer registration mechanism for callers.
4. Remove old rendering orchestration logic that bypasses pipeline phases.
5. Ensure gradle-plugin task remains unchanged from caller perspective.

## Suggested File Areas

1. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/Generate.kt`
2. Pipeline entrypoint files
3. Legacy orchestration files scheduled for removal

## Verification Commands

1. `./gradlew :renderer:jvmTest`
2. `./gradlew :typed:allTests`
3. `./gradlew :gradle-plugin:build`

## Exit Criteria

1. Pipeline is the default and only active generation orchestration path.
2. Public generation API remains usable.
3. Cross-module tests pass.

