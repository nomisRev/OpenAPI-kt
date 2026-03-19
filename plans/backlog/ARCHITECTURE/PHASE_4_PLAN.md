# PHASE 4: Render Plan IR

## Objective

Introduce canonical render planning IR so all emitters consume the same precomputed plan and shared rules.

## Scope

1. Add render planning model.
2. Implement planners that compute operation/model/file plans.
3. Encode previously duplicated rules once.

## Non-Goals

1. No full emitter rewrite yet.
2. No public cutover yet.

## Work Items

1. Add planning IR models:
   - `RenderPlan`
   - `ModelPlan`
   - `ClientPlan`
   - `PathPlan`
   - `OperationPlan`
   - `ParameterPlan`
   - `BodyPlan`
   - `ResponsePlan`
   - `UtilityPlan`
2. Implement planner components:
   - `PathNavigationPlanner`
   - `OperationSignaturePlanner`
   - `ResponsePlanner`
   - `InlineTypePlanner`
3. Centralize rules now duplicated in multiple renderers:
   - required/optional parameter ordering
   - body parameter insertion and optionality
   - response wrapper selection (unit/single/sealed)
   - route inline body/response type strategy
4. Add planner diagnostics on invalid states and ambiguity.

## Suggested File Areas

1. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/pipeline/plan/...`

## Verification Commands

1. `./gradlew :renderer:jvmTest`

## Exit Criteria

1. A complete `RenderPlan` can be built for existing feature coverage.
2. Planner rule tests pass.
3. Plan objects are deterministic for identical input.

