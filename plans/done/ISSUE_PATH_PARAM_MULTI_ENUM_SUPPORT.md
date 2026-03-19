# ISSUE: Support Multi-Enum Path Parameter Unions

## Problem

Phase 5 moved path-parameter flattening decisions into the typed layer, but inline path unions with more than one enum
case still fail during route resolution with:

`Path parameter '<name>' uses a oneOf union with multiple enum cases, which is not supported yet.`

This preserves previous renderer behavior, but it is still a hard failure for otherwise valid OpenAPI specs.

## Why This Matters

1. Some APIs model path parameters as a union of several string enums.
2. Route construction currently aborts before rendering, so users cannot generate a client at all.
3. The typed layer now owns overload eligibility, so this limitation should also be addressed there instead of remaining
   as a permanent guardrail.

## Current Behavior

1. `parsePathSegments(...)` emits `PathSegment.OverloadedParameter` only for simple inline unions with at most one enum
   case.
2. More than one enum case throws an `IllegalArgumentException` during route construction.
3. The failure is covered by `RouteSpec` as an explicit unsupported case.

## Desired Outcome

Support inline path unions with multiple enum cases without failing route construction.

Acceptable implementations include:

1. Full support by generating overloads for each distinct enum case with stable inline type names.
2. A typed fallback that keeps the parameter unflattened instead of throwing, if full overload support is too invasive.
3. A typed diagnostic/leniency path, if the project wants to preserve strict mode and add a configurable downgrade path.

## Design Questions

1. How should multiple inline enum case types be named so nested generated types stay stable and readable?
   1_Answer. Use the same stable name generation as normal enum cases.
2. If two enum cases erase to the same Kotlin shape, should the typed layer deduplicate them or keep both and rely on
   renderer naming?
   2_Answer. Deduplicate
3. Should strict failure remain available as an opt-in validation mode after support is added?
   3_Answer. No strict mode needed.

## Suggested File Areas

1. `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/PathSegment.kt`
2. `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/ApiTree.kt`
3. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt`
4. `typed/src/commonTest/kotlin/io/github/nomisrev/PathSegmentSpec.kt`
5. `typed/src/commonTest/kotlin/io/github/nomisrev/RouteSpec.kt`
6. `renderer/src/jvmTest/kotlin/io/github/nomisrev/render/ClientSpec.kt`

## Acceptance Criteria

1. Specs with inline path `oneOf` unions containing multiple enum cases no longer fail during route resolution by
   default.
2. Generated navigation APIs remain deterministic and source-compatible for existing supported cases.
3. New typed and renderer tests cover at least one multi-enum path union case.
4. `./gradlew :typed:allTests` passes.
5. `./gradlew :renderer:jvmTest` passes.
