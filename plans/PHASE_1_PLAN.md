# Phase 1: Remove renderer module

## Goal
Delete the `renderer/` module entirely. It will be rewritten from scratch to consume the new `ApiTree` structure. Removing it now unblocks all subsequent typed module changes without compilation errors from the renderer.

## Changes

### 1. Delete `renderer/` directory
Remove the entire `renderer/` module directory.

### 2. Remove renderer from Gradle build
- **`settings.gradle.kts`** — remove the `include(":renderer")` (or equivalent) line
- **`build.gradle.kts`** (root) — remove any renderer-specific configuration if present
- Any other build files referencing the renderer module

### 3. Remove renderer dependencies
- Any module that depends on `renderer` (e.g., integration tests) — remove those dependency declarations
- If integration tests depend on renderer for compilation, either remove those tests or stub them

## Verification
- Project compiles: `./gradlew :typed:build`
- Typed module tests pass: `./gradlew :typed:allTests`
