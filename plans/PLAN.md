# Renderer Implementation Plan

Generate Kotlin source code from the typed OpenAPI model using KotlinPoet. Covers model types (enums, objects, unions, discriminated objects, collections) and a Ktor-based HTTP client with path-based navigation.

## Design Decisions

| # | Decision | Choice |
|---|----------|--------|
| 1 | Entry point | `ApiTree.render()` extension, `OpenAPI.generate()` convenience wrapper |
| 2 | `ApiModel` | Remove — replaced by `ApiTree` with `models: List<Model>` |
| 3 | Model vs client gen | Separate `generateModels()` and `generateClient()` functions |
| 4 | Package config | Fully configurable — caller passes model and api packages separately |
| 5 | Platform targeting | KMP from start — `Set<KmpTarget>` param controls @JvmInline / @JsName |
| 6 | Custom serializers | Generate from start (unions without discriminator + additionalProperties) |
| 7 | Model file organization | One file per top-level type, nested types inside parent |
| 8 | Runtime utilities | Shared generated `SerializationUtils.kt` (attemptDeserialize, UnionSerializationException) |
| 9 | Naming utilities | In renderer module (splitToWords/toPascalCase stay in typed) |
| 10 | Imports | KotlinPoet automatic import management |
| 11 | Renderer architecture | Extension functions on Model subtypes (e.g., `Model.Enum.toTypeSpec()`) |
| 12 | Name resolution | On-the-fly NamingContext → ClassName resolution |
| 13 | Test strategy | Full golden output including serializers |
| 14 | Client scope | Both interface + Ktor implementation from start |
| 15 | Response types | Nested in client interfaces, part of client gen |
| 16 | Inline enums | Nested inside client interfaces |
| 17 | Interface + impl files | Same file |
| 18 | Read/Write context | Always suffix route-level schemas: `{Name}Request` / `{Name}Response` |
| 19 | Nested inline unions | Attempt during implementation, fall back to wrapping |

## Phases

| Phase | Name | Plan | Status |
|-------|------|------|--------|
| 0 | Scaffolding | [PHASE_0.md](PHASE_0.md) | DONE |
| 1 | Enums | [PHASE_1.md](PHASE_1.md) | DONE |
| 2 | Objects (basic) | [PHASE_2.md](PHASE_2.md) | DONE |
| 3 | Objects (additionalProperties) | [PHASE_3.md](PHASE_3.md) | DONE |
| 4 | Collections | [PHASE_4.md](PHASE_4.md) | DONE |
| 5 | Unions (discriminated) | [PHASE_5.md](PHASE_5.md) | DONE |
| 6 | Unions (non-discriminated) | [PHASE_6.md](PHASE_6.md) | DONE |
| 7 | Discriminated Objects | [PHASE_7.md](PHASE_7.md) | DONE |
| 8 | Client: Interface Tree + Navigation | [PHASE_8.md](PHASE_8.md) | DONE |
| 9 | Client: Operations + Request Bodies | [PHASE_9.md](PHASE_9.md) | DONE |
| 10 | Client: Response Handling | [PHASE_10.md](PHASE_10.md) | DONE |
| 11 | Client: Ktor Impl + Server + Files | [PHASE_11.md](PHASE_11.md) | TODO |

## Specs

- [MODEL_SPEC.md](MODEL_SPEC.md) — How OpenAPI schemas map to Kotlin types
- [CLIENT_SPEC.md](CLIENT_SPEC.md) — How the generated Ktor client should look
