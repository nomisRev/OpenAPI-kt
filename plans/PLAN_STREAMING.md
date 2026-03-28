# Add First-Class Sequential Streaming Support

## Summary

- OpenAPI 3.2 adds real support for streaming media via sequential media types, including `text/event-stream`, with `itemSchema` for per-item decoding.
- That does not directly solve OpenAI generation in this repo today because:
  - OpenAI's current published spec is still `openapi: 3.1.0` and uses `schema` under `text/event-stream`, not `itemSchema`.
  - This repo currently collapses responses to a single body model and generated clients always decode with `.body()`.
- Implement a generic sequential-response pipeline for OAS 3.2, plus a narrow compatibility path for legacy OpenAI-style SSE specs.

## Key Changes

- Parser/model layer:
  - Extend `MediaType` to parse OAS 3.2 fields: `itemSchema`, `prefixEncoding`, and `itemEncoding`.
  - Extend `Schema` parsing to retain `contentMediaType` and `contentSchema` metadata.
  - Add media-type classification for sequential formats:
    - `text/event-stream`
    - `application/jsonl`
    - `application/x-ndjson`
    - `application/json-seq`
    - `application/*+json-seq`
- Typed route model:
  - Replace the current "content type -> single `Model`" response mapping with a sealed response-content descriptor that distinguishes buffered vs streaming content.
  - Represent streaming content as item-based, carrying:
    - stream kind
    - item model
    - optional complete model if `schema` is also present
    - legacy-compatibility flag for pre-3.2 SSE specs
  - Compatibility rule: for `text/event-stream` with no `itemSchema` and a non-array `schema`, treat the declared `schema` as the stream item model.
- Renderer/runtime:
  - Generate streaming methods that return `Flow<T>` for direct success responses.
  - For multi-status responses, keep the existing typed sealed response pattern, but success cases contain `Flow<T>`.
  - Stop using `.body()` for streaming success paths.
  - Add shared typed runtime decoders for:
    - SSE
    - JSON Lines / NDJSON
    - JSON Text Sequences
  - SSE decoder behavior:
    - normalize events per SSE rules
    - ignore comments and unknown fields
    - join repeated `data:` lines with `\n`
    - strict 3.2 mode: deserialize the normalized event object
    - legacy OpenAI mode: deserialize the `data` payload only
    - treat `data: [DONE]` as end-of-stream only in legacy SSE compatibility mode
- Generation rules:
  - If `itemSchema` is present, the generated surface is streaming-first.
  - If both `schema` and `itemSchema` are present, retain both internally, but only generate the streaming API in v1.
  - Non-sequential media types keep current behavior.

## Public API / Type Changes

- `parser.MediaType` gains `itemSchema`, `prefixEncoding`, `itemEncoding`.
- Parsed `Schema` gains `contentMediaType` and `contentSchema`.
- `typed.Route.ReturnType` changes from `Map<ContentType, Model>` to a richer content descriptor that can represent streaming variants.
- Generated client APIs may now expose `kotlinx.coroutines.flow.Flow<...>` in success return types for sequential media.
- Generated sealed response success cases for streaming endpoints wrap `Flow<...>` instead of a single decoded value.

## Test Plan

- Parser tests:
  - OAS 3.2 `itemSchema`/`itemEncoding`/`prefixEncoding` deserialize correctly.
  - `contentMediaType` and `contentSchema` deserialize correctly.
- Typed transformation tests:
  - `text/event-stream + itemSchema` becomes streaming content with the right item model.
  - `application/jsonl` / `application/x-ndjson` / `application/json-seq` become streaming content.
  - Legacy OpenAI-style `text/event-stream + schema` is upgraded to streaming content.
- Renderer tests:
  - Generated OpenAI `textEventStream()` methods return `Flow<ResponseStreamEvent>` or a sealed success wrapper containing that flow.
  - Generated sequential JSON endpoint methods return `Flow<ItemType>`.
  - Existing non-streaming endpoints remain unchanged.
- Runtime tests:
  - SSE parser handles multi-line `data:`, comments, blank-line event boundaries, and legacy `[DONE]`.
  - NDJSON / JSONL / JSON-seq decoders emit items incrementally and stop cleanly on EOF.
- Validation:
  - Run `:typed:allTests` and `:renderer:jvmTest`.

## Assumptions

- `text/even-stream` is a typo; target media type is `text/event-stream`.
- OpenAI support is achieved through the legacy SSE compatibility rule because their current spec is still 3.1 and not OAS 3.2-native.
- Full `contentSchema`-driven typed decoding of SSE `data` strings is not generated in v1; the metadata is preserved for future enhancement.
- Streaming `multipart/*` is out of scope for v1 generation; parse the OAS 3.2 fields now, but fail clearly if generation is attempted for multipart streaming.
