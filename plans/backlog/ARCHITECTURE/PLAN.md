# Renderer V2 Architecture Plan

## Goal

Rebuild the renderer into a scalable compiler-style pipeline with explicit extension points for spec repair and semantic customization.

Target flow:
1. Parse spec into `OpenAPI`.
2. Run raw spec transformers (`OpenAPI -> OpenAPI`).
3. Build typed model (`ApiTree`).
4. Run typed transformers (`ApiTree -> ApiTree`).
5. Build canonical render plan IR.
6. Emit KotlinPoet files.
7. Return files + diagnostics.

## Why This Refactor

The current renderer has high coupling and duplicated logic across client interface and client implementation generation. Custom user fixups for bad specs are not first-class. This plan introduces clear architecture boundaries, deterministic extension points, and phase-level diagnostics.

## Architecture Decisions

1. Hook level: dual-stage transformers (raw and typed).
2. Migration mode: big-bang architecture cutover.
3. SPI stability: experimental in first releases.
4. Backend scope: Kotlin-only, no multi-language IR abstraction.

## Public API Target

1. `GenerationPipeline.generate(openApi: OpenAPI, request: GenerationRequest): GenerationResult`
2. `@ExperimentalOpenApiGenerationApi interface OpenApiTransformer`
3. `@ExperimentalOpenApiGenerationApi interface ApiTreeTransformer`
4. `data class GenerationResult(files: List<FileSpec>, diagnostics: List<Diagnostic>)`
5. Existing `OpenAPI.generate(config)` delegates to the new pipeline internally.

## Phase Index

| Phase | Name | Main Output | Depends On |
|---|---|---|---|
| 0 | Baseline and Guardrails | Stable behavior baseline and regression harness | None |
| 1 | Pipeline Core | Orchestrator, phase engine, diagnostics model | 0 |
| 2 | Raw Transform Stage | Raw transformer SPI and built-in raw repairs | 1 |
| 3 | Typed Transform Stage | Typed transformer SPI and typed normalization pass execution | 2 |
| 4 | Render Plan IR | Canonical plan model for operations/models/files | 3 |
| 5 | Emitter Refactor | Plan-driven emitters and duplication removal | 4 |
| 6 | Cutover and API Integration | Pipeline as default generation path | 5 |
| 7 | Hardening and Documentation | Coverage expansion, perf validation, usage docs | 6 |

## Cross-Phase Invariants

1. Output order must remain deterministic.
2. All phases emit typed diagnostics with phase metadata.
3. Transformer execution order is deterministic by `(priority, id)`.
4. Renderer core stays host-agnostic and does not depend on Gradle APIs.
5. Existing generation scenarios must continue to compile unless explicitly changed by approved spec update.

## Delivery Checklist

1. Every phase has tests and explicit exit criteria.
2. No phase starts before previous phase exit criteria are met.
3. No legacy code removal before equivalent plan-driven path is verified.
4. All new extension contracts are marked experimental.

