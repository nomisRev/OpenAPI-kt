# ISSUE: Resolve Conflicting Path Parameter Shapes In Shared Path Nodes

## Problem

`ApiTree.buildTree()` merges parameter nodes by path parameter name only. After Phase 5, that means a shared node can be built from either:

1. `PathSegment.Parameter`
2. `PathSegment.OverloadedParameter`

When two routes share the same path parameter name but resolve to different typed segment shapes, the tree keeps whichever segment instance is inserted first.
Navigation generation then depends on route insertion order instead of an explicit merge rule.

## Why This Matters

1. The generated client shape for a shared path node should be deterministic from schema semantics, not from path iteration order.
2. A mixed `Parameter`/`OverloadedParameter` node can under-generate or over-generate navigation overloads for one of the routes on that node.
3. This became easier to hit after introducing `PathSegment.OverloadedParameter`.

## Current Behavior

1. `PathSegment?.matches(...)` treats `Parameter` and `OverloadedParameter` with the same name as the same tree node.
2. `PathNodeBuilder` stores the first segment instance it sees for that node.
3. No explicit compatibility check or merge strategy exists for differing parameter models on the same shared node.

## Desired Outcome

Make shared path-node parameter semantics explicit and deterministic.

Reasonable solutions include:
1. Normalize all matching parameter nodes to a single merged segment shape before tree construction.
2. Detect incompatible parameter definitions and fail with a targeted error message.
3. Carry per-operation path parameter metadata separately if a shared node cannot safely own one canonical segment shape.

## Design Questions

1. What counts as compatible when two routes share the same parameter name but have different models?
2. If one route is flattenable and another is not, should the merged node expose the superset API, the intersection, or fail?
3. Should this validation happen during route resolution or during tree building?

## Suggested File Areas

1. `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/ApiTree.kt`
2. `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/PathSegment.kt`
3. `typed/src/commonTest/kotlin/io/github/nomisrev/ApiTreeSpec.kt`
4. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt`
5. `renderer/src/jvmTest/kotlin/io/github/nomisrev/render/ClientSpec.kt`

## Acceptance Criteria

1. Shared path nodes with the same parameter name no longer depend on insertion order for their segment shape.
2. Mixed `Parameter` / `OverloadedParameter` scenarios are either merged deterministically or rejected with a targeted failure.
3. Tests cover at least one shared-node conflict case across multiple operations or sibling routes.
4. `./gradlew :typed:allTests` passes.
5. `./gradlew :renderer:jvmTest` passes.
