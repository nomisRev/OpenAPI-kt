# PHASE 0: Baseline and Guardrails

## Objective

Freeze current behavior before architecture rewrite so regressions are measurable and explicit.

## Scope

1. Capture current generation behavior with stable tests.
2. Add targeted regression tests for known fragile behavior.
3. Define drift policy for the big-bang cutover.

## Non-Goals

1. No architecture rewrite in this phase.
2. No new extension APIs in this phase.

## Work Items

1. Establish a baseline matrix of representative specs:
   - model-heavy (`object`, `enum`, `union`, discriminated object)
   - client-heavy (deep path tree, multipart/form/urlencoded body, mixed responses)
2. Add focused regression tests for:
   - parameter ordering and required/optional split
   - body placement and optionality
   - response wrapper behavior (single vs sealed multi-response)
   - inline parameter sharing behavior
3. Define output drift policy:
   - allowed: internal organization and non-semantic formatting drift
   - blocked: API-signature drift unless explicitly approved
4. Add baseline generation-time measurement for representative specs.

## Suggested File Areas

1. `renderer/src/jvmTest/kotlin/...`
2. `renderer/src/jvmTest/resources/kotlinTestData/...`

## Verification Commands

1. `./gradlew :renderer:jvmTest`

## Exit Criteria

1. All baseline tests are green.
2. Fragile behavior has dedicated regression coverage.
3. Drift policy is documented and referenced by later phases.

