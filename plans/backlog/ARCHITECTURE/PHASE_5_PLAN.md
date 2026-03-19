# PHASE 5: Emitter Refactor (Plan-Driven)

## Objective

Refactor rendering so all KotlinPoet emission is driven by `RenderPlan`, not by ad-hoc direct traversal.

## Scope

1. Migrate client interface emitter to plan-driven generation.
2. Migrate client impl emitter to plan-driven generation.
3. Migrate model/factory/utility emission to explicit emitter modules.

## Non-Goals

1. No new feature semantics beyond approved drift policy.
2. No Gradle API work.

## Work Items

1. Add emitter modules:
   - `ModelEmitter`
   - `ClientInterfaceEmitter`
   - `ClientImplementationEmitter`
   - `FactoryEmitter`
   - `SerializationUtilityEmitter`
2. Replace duplicated logic paths by consuming `OperationPlan`.
3. Ensure interface and impl generation both consume the same signature and response plans.
4. Move serialization utility trigger conditions to `UtilityPlan`.
5. Decommission legacy helpers only after parity tests pass.

## Suggested File Areas

1. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/pipeline/emit/...`
2. Existing renderer files that will be replaced or slimmed

## Verification Commands

1. `./gradlew :renderer:jvmTest`

## Exit Criteria

1. Interface and implementation emitters are plan-driven.
2. Duplicated operation logic is removed.
3. Golden tests are green or deviations are explicitly approved.

