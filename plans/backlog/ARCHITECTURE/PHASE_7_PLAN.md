# PHASE 7: Hardening and Documentation

## Objective

Harden the new architecture for maintainability, external transformer usage, and long-term evolution.

## Scope

1. Expand coverage for transformers, diagnostics, and edge cases.
2. Validate performance against Phase 0 baseline.
3. Document architecture and SPI usage.

## Non-Goals

1. No major semantic rewrites.
2. No SPI stabilization promise yet.

## Work Items

1. Add edge-case tests:
   - transformer ordering conflicts
   - invalid transformer outputs
   - deterministic output on repeated runs
2. Add diagnostics quality checks:
   - phase tags present
   - actionable messages for common failure modes
3. Add performance regression checks versus Phase 0.
4. Document:
   - pipeline phases and extension points
   - best practices for raw and typed transformers
   - limitations and known unsupported patterns
5. Produce migration notes for contributors working on renderer internals.

## Suggested File Areas

1. `renderer/src/jvmTest/...`
2. `renderer/README.md`
3. `plans/ARCHITECTURE/...`

## Verification Commands

1. `./gradlew :renderer:jvmTest`
2. `./gradlew :typed:allTests`

## Exit Criteria

1. Coverage is materially improved for extension points.
2. No unexpected performance regression beyond agreed threshold.
3. Architecture and usage docs are complete and reviewed.

