# ISSUE: Unroll Finite Enum Path Segments Into Fixed Path Nodes

## Problem

Closed enums on path parameters currently behave like dynamic navigation inputs even when they really describe a finite set
of concrete subpaths.

Example:

`/orgs/{org}/{security_product}/{enablement}`

In the GitHub spec, both `security_product` and `enablement` are closed string enums. Semantically that is not one dynamic
path shape, but a small finite set of concrete paths.

Today we still model those segments as path parameters and try to navigate through them dynamically. That creates three
problems:

1. The generated API is less ergonomic than the path semantics justify.
2. Direct enum path parameters still fall through the generic `PathSegment.Parameter` path, which is the wrong abstraction.
3. Path enum support is split across special cases (`OverloadedParameter`) instead of being treated as path structure.

## Why This Matters

1. A finite enum on a path is really a fixed branch in the path tree.
2. Unrolling those branches removes the need to generate path-only enum navigation types.
3. It eliminates path-encoding logic for closed enum values by baking the wire value directly into the generated path.
4. It should simplify or remove some existing shared-node conflicts around `Parameter` vs `OverloadedParameter`.
5. It applies equally to referenced and inline enums.

## Current Behavior

1. Route resolution builds `Route.Input` for every parameter, including path params.
2. `parsePathSegments(...)` only special-cases `Model.Union`; direct `Model.Enum` path params become plain
   `PathSegment.Parameter`.
3. Flattenable path unions become `PathSegment.OverloadedParameter`, and enum cases inside those unions are rendered as
   overloads with generated enum types.
4. Shared path nodes can currently conflict when one route resolves to `Parameter` and another to `OverloadedParameter`,
   or when overloaded enum case order differs.

## Desired Outcome

Finite enum-valued path choices should become concrete navigation branches instead of runtime arguments.

Target API shape:

1. Direct enum path param:
   `api.orgs.org("acme").dependencyGraph().enableAll().post(...)`
2. Inline enum path param:
   Same behavior as referenced enums. Inline origin should not matter.
3. Mixed flattenable union path param, for example `oneOf[int, enum("queued", "in-progress")]`:
   `api.workflows.workflowId(123).runs.get()`
   `api.workflows.queued().runs.get()`
   `api.workflows.inProgress().runs.get()`

This treats enum choices as fixed path structure while preserving a dynamic branch for any non-enum remainder.

## Proposed Strategy

### 1. Add a route-expansion phase before tree construction

After `toRoute(...)` resolves parameter types, expand finite enum-valued path segments into concrete route variants before
calling `buildTree(...)`.

This is the key design shift:

1. Do not model closed path enums primarily as argument types.
2. Model them as concrete path variants.

### 2. Expand direct closed enum path params

For a path segment whose model is `Model.Enum`:

1. Duplicate the route once per enum value.
2. Replace the path segment with a fixed-value segment for that wire value.
3. Remove that path param from the expanded route's remaining dynamic path inputs.

For the GitHub example, `security_product` produces 7 branches and `enablement` produces 2 branches, so the route expands
to 14 concrete variants.

### 3. Expand enum cases inside flattenable path unions

For a path segment whose model is `Model.Union`:

1. Split enum cases out into concrete fixed-value routes.
2. Keep one residual dynamic route if non-enum cases remain.
3. Rebuild the residual segment shape from the remaining cases:
   - one remaining model -> plain `PathSegment.Parameter`
   - multiple remaining flattenable cases -> `PathSegment.OverloadedParameter`
   - no remaining cases -> no dynamic route

This keeps useful dynamic navigation while still unrolling all finite enum choices.

### 4. Introduce a fixed-value path segment kind

Add a dedicated path-segment representation for enum-derived fixed branches instead of reusing plain `Literal` directly.

It should carry:

1. The raw wire value to place in the URL.
2. The source parameter name.
3. Enough metadata for renderer decisions, especially if we want zero-arg functions instead of literal properties.

Recommended shape:

1. `PathSegment.FixedValue(...)` or similar.

This keeps enum-derived branches explicit in the typed model and avoids losing origin information during expansion.

### 5. Render fixed enum branches as zero-arg functions

If we want the API shape from this issue, fixed enum branches should render as zero-arg navigation functions:

1. `dependencyGraph()` instead of a generated enum argument.
2. `enableAll()` instead of `enablement(Enablement.EnableAll)`.

This is slightly different from ordinary literal path segments, which currently render as properties. That difference is
intentional:

1. Ordinary literals represent fixed resources in the spec.
2. Enum-derived fixed branches represent a former path choice and should remain choice-like in the generated API.

If the team wants lower implementation churn, reusing normal literal-property rendering is the fallback, but it will not
match the target call shape for this issue.

### 6. Revisit model reachability for path enums

Fully unrolled enum path params should no longer cause top-level or inline enum model generation.

Only enum models that remain reachable through:

1. query/header/cookie params
2. request bodies
3. response bodies
4. residual non-unrolled unions

should still contribute generated model files.

### 7. Validate concrete-path collisions after expansion

Route expansion can collapse a parameterized path onto an explicit literal path or onto another unrolled branch.

We should add a targeted validation step for duplicate concrete `(method, path)` routes after expansion:

1. Merge only when they are semantically identical.
2. Otherwise fail with a deterministic error.

This avoids silent last-write-wins behavior during tree construction.

## Design Questions

1. Should enum-derived branches render as properties or zero-arg functions?
   1_Answer. Zero-arg functions.
2. Should mixed unions keep a dynamic branch after enum values are unrolled?
   2_Answer. Mixed unions like oneOf [ String | enum ] (open enums) or [ A | enum ] need to keep a dynamic branch.
3. Should this apply only to direct enums, or also to enum cases inside flattenable path unions?
   3_Answer. Also to enum cases inside flattenable path unions.
4. Should we cap cross-product expansion when a route has several enum path params?
   4_Answer. No hard cap for the first implementation. Add clear tests and diagnostics instead of a silent threshold.

## Suggested File Areas

1. `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/Route.kt`
2. `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/PathSegment.kt`
3. `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/ApiTree.kt`
4. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRenderer.kt`
5. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt`
6. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/PathParamUtils.kt`
7. `typed/src/commonTest/kotlin/io/github/nomisrev/RouteSpec.kt`
8. `typed/src/commonTest/kotlin/io/github/nomisrev/ApiTreeSpec.kt`
9. `renderer/src/jvmTest/kotlin/io/github/nomisrev/render/ClientSpec.kt`

## Acceptance Criteria

1. Direct closed enum path params are expanded into fixed navigation branches and no longer require enum arguments.
2. Inline closed enum path params behave the same way as referenced enums.
3. Flattenable path unions with enum cases expose both:
   - fixed enum-derived zero-arg branches
   - a residual dynamic branch for non-enum cases, when present
4. Shared-node conflicts caused only by enum-case ordering or by enum-vs-parameter path shape mismatches are removed or
   replaced by deterministic concrete-path behavior.
5. Fully unrolled path enums do not generate unused enum model files.
6. Tests cover:
   - direct enum path params
   - inline enum path params
   - mixed union path params
   - multiple enum path params on one route
   - collision between explicit literals and unrolled enum literals
7. `./gradlew :typed:allTests` passes.
8. `./gradlew :renderer:jvmTest` passes.
