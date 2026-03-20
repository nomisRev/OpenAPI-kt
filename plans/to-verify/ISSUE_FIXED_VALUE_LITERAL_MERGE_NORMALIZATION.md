# ISSUE: FixedValue and Literal Path Segments Are Not Merged When Names Differ Only In Separator

## Problem

`PathNodeBuilder.insert()` decides whether to reuse an existing child node by calling
`PathSegment?.sameIdentityAs(other)`. That comparison uses raw string equality on the segment name
and wire value.

OpenAPI enum values that appear as path parameter choices use underscore separators (e.g.
`"secret_scanning"` in the `security_product` enum). Literal path segments in a normal OpenAPI path
use hyphen separators (e.g. `"secret-scanning"` in `/orgs/{org}/secret-scanning/alerts`).

When `expandFiniteEnumPathSegments()` unrolls the route
`POST /orgs/{org}/{security_product}/{enablement}`, it produces
`POST /orgs/{org}/secret_scanning/enable_all` — a route with `FixedValue("secret_scanning")`. That
segment is never considered identical to the existing `Literal("secret-scanning")` from the other
routes, so the tree builder creates a second, separate `secret_scanning` node instead of merging the
two into one shared `secret-scanning` node.

The downstream renderer then emits a duplicate `SecretScanning` class containing only `enableAll`
and `disableAll`, completely disconnected from the `alerts` and `pattern-configurations` sibling
operations that live under the real `secret-scanning` literal node.

## Root Cause

Two functions in `ApiTree.kt` are affected.

### `sameIdentityAs`

```kotlin
this is PathSegment.FixedValue && other is PathSegment.Literal -> wireValue == other.name
this is PathSegment.Literal && other is PathSegment.FixedValue -> name == other.wireValue
this is PathSegment.Literal && other is PathSegment.Literal    -> name == other.name
this is PathSegment.FixedValue && other is PathSegment.FixedValue -> wireValue == other.wireValue
```

All four comparisons are raw equality checks. They return `false` for `"secret_scanning"` vs
`"secret-scanning"`.

### `normalizedForCompatibility`

```kotlin
is PathSegment.FixedValue -> PathSegment.Literal(wireValue)  // keeps raw underscore form
is PathSegment.Literal    -> this                            // keeps raw hyphen form
```

After normalization a `FixedValue("secret_scanning")` becomes `Literal("secret_scanning")` while a
`Literal("secret-scanning")` stays `Literal("secret-scanning")`. The compatibility check then
reports a conflict where none exists, and the two nodes are never merged.

## Fix Applied

A private helper `String.normalizeSegmentSeparators()` is introduced in `ApiTree.kt` to replace
hyphens with underscores before comparison. Both comparison sites are updated:

- `sameIdentityAs` — all four name-bearing branches call `.normalizeSegmentSeparators()` on both
  sides before comparing.
- `normalizedForCompatibility` — the `Literal` branch now also normalizes its name, so a
  `FixedValue("secret_scanning")` and a `Literal("secret-scanning")` both normalize to
  `Literal("secret_scanning")` and are considered compatible.

The `preferredRepresentation` logic already prefers `FixedValue` over `Literal` when both are
present, so the merged node retains the `FixedValue` form, keeping the original wire value intact
for URL construction.

## Impact

Without this fix the GitHub spec generates a stray top-level `SecretScanning` class whose operations
use the wrong URL (`/orgs/$org/secret_scanning/enable_all`) instead of being nested inside the
correct `secret-scanning` node. Any spec that shares this pattern — an enum-unrolled `FixedValue`
that names the same resource as an existing hyphenated `Literal` — is affected.

## Affected Files

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/ApiTree.kt` — bug and fix
- `typed/src/commonTest/kotlin/io/github/nomisrev/ApiTreeSpec.kt` — regression test added

## Acceptance Criteria

1. `FixedValue("secret_scanning")` and `Literal("secret-scanning")` are treated as the same node
   and merged into one `PathNode` with a `FixedValue` segment.
2. The merged node accumulates children from both the literal routes and the unrolled enum routes.
3. No spurious stray `SecretScanning` class is generated for the GitHub spec.
4. `./gradlew :typed:allTests` passes.
