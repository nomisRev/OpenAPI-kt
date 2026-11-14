# Documents without path operations generate nothing, silently: webhooks and unreferenced component schemas are ignored

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** wrong output (every phase passes, zero files, zero diagnostics)
**Impact:** 11 of 1,547 documents emit no source file: 6 `adyen.com:*Notification*` (OpenAPI 3.1 `webhooks` only, 5–83 component schemas each), twilio.com:twilio_numbers_v1 (one path item with no operation, 3 schemas), and 4 empty documents (firstinspires.org, googleapis.com:youtubeAnalytics, zenoti.com, ipinfodb.com). The underlying rule — only schemas reachable from a path operation are generated — affects every document with unreferenced schemas or webhooks (openai.yaml has 15 webhooks; 23 census documents have a root `x-webhooks`).
**Source spec valid:** yes — OAS 3.1 allows a document with only `webhooks` or only `components`; 3.0 documents with `paths: {}` are valid too

## Symptom

`results.jsonl` shows `parse`, `typed`, `render`, `emit` all `pass`, `compile: not-run`, no `sources/` directory and
an empty `diagnostics` array. A user of the Gradle plugin gets a green build and an empty output directory.

| group | documents | what the document contains |
| --- | --- | --- |
| webhooks only (3.1.0) | adyen.com:BalancePlatformTransferNotification-v3, BalancePlatformConfigurationNotification-v1, BalancePlatformPaymentNotification-v1, BalancePlatformReportNotification-v1, ManagementNotificationService-v1, MarketPayNotificationService | `paths` absent/empty, 1–19 `webhooks`, 5–83 `components.schemas` |
| path item without operations | twilio.com:twilio_numbers_v1 | `/v1/HostedNumber/Eligibility` with no HTTP method, 3 schemas |
| nothing to generate | firstinspires.org, zenoti.com, ipinfodb.com (no paths, no components), googleapis.com:youtubeAnalytics (no paths, 11 component parameters, 0 schemas) | — |

## Minimal reproducer

`<scratchpad>/repro/webhooks-only.json` (reduced from adyen.com:BalancePlatformTransferNotification-v3):

```json
{
  "openapi": "3.1.0",
  "info": { "title": "Repro", "version": "1" },
  "paths": {},
  "components": { "schemas": { "TransferEvent": { "type": "object", "required": ["type"],
    "properties": { "type": { "type": "string" }, "amount": { "type": "integer" } } } } },
  "webhooks": { "transfer.created": { "post": {
    "operationId": "onTransfer",
    "requestBody": { "content": { "application/json": { "schema": { "$ref": "#/components/schemas/TransferEvent" } } } },
    "responses": { "200": { "description": "ack" } } } } }
}
```

```
worker exit=0 phases: parse pass, typed pass, render pass, emit pass
diagnostics: []
no sources emitted
```

`<scratchpad>/repro/unreferenced-schema.json` (one `GET /ping` plus a component schema `Orphan` that no operation
references): passes, emits `api/Ping.kt` and `api/Repro.kt`, and no `model/Orphan.kt`, no diagnostic.
`<scratchpad>/repro/empty-paths.json` (`paths: {}` and nothing else): passes, no sources, no diagnostic.

## What the spec says

- OAS 3.1.0 §4.8.1 OpenAPI Object: "`webhooks` — The incoming webhooks that MAY be received as part of this API and
  that the API consumer MAY choose to implement." and "The OpenAPI document MUST contain at least one `paths` field,
  a `components` field or a `webhooks` field."
- A webhook is a request the API *sends to the consumer*. A generated HTTP client has no call to make for it, but
  the payload schemas are exactly what a consumer needs to deserialize the delivery.
- `components.schemas` entries are reusable definitions; nothing says an unreferenced one is dead, and generators
  commonly emit all of them (it is the only way to get the Adyen notification models).

## Root cause

`typed/src/commonMain/kotlin/io/github/nomisrev/openapi/ApiTree.kt:65-82` (`OpenAPI.toApiTree`):

```kotlin
val routes = with(registry) { preprocessed.toRoutes() }
val models = with(registry) {
    routes.flatMapTo(mutableSetOf()) { it.topLevelNames() }.topLevelModels(registry)
}
```

Models are collected only from the names routes reference. `OpenAPI.webhooks` is parsed
(`parser/.../OpenAPI.kt:40`) but nothing in `typed/` reads it (0 matches for `webhooks` under `typed/src/commonMain`).
With no routes, `models` is empty, `generateClientFiles` returns `emptyList()` for a tree without children or
operations (`renderer/.../ClientRenderer.kt:21-22`), and no diagnostic is produced for any of it.

## Correct final API

1. Webhook payload models are generated. For the reproducer: `model/TransferEvent.kt`

   ```kotlin
   @Serializable
   public data class TransferEvent(public val type: String, public val amount: Long? = null)
   ```

   generated in the **read** context (the consumer receives it), plus the response models a webhook declares in the
   write context. No client function is generated for a webhook; a typed receiver (for example a sealed
   `Webhook` hierarchy keyed by webhook name with a `decode(name, body)` helper) is a possible later feature, not
   part of this fix.
2. Unreferenced component schemas: an open decision between
   - generate every `components.schemas` entry (most complete; the read/write context is unknown, so generate the
     context-neutral shape), or
   - keep reachability-based generation and report `Warning: N component schemas are not referenced by any operation
     or webhook and are not generated: A, B, ...`.
   Either satisfies "no silent drops"; today neither happens.
3. A document that yields zero files produces a diagnostic: `Warning: the document declares no operations, webhooks
   or schemas to generate; no files were written.` (an `Error` is arguable for the Gradle task: an empty client is
   almost never what the user wanted.)

## Fix slice

- typed: in `toApiTree`, walk `webhooks` (resolving `ReferenceOr<PathItem>`) and add the schemas their request
  bodies and responses reference to the top-level model set, with request bodies in `SchemaContext.Read` (direction
  is inverted relative to paths). `x-webhooks` (github.json has 265, 23 census documents) is the pre-3.1 convention
  and is currently also lost at the parser, see `issues/root-extensions-dropped.md`; treat it as out of scope or as
  an opt-in.
- renderer: diagnostics for zero output and, depending on decision 2, for unreferenced schemas
  (`GenerationDiagnostics.kt:42-44` is where the list is assembled).
- gradle-plugin: nothing beyond logging the new diagnostics.

## Proof

- Failing test first (typed): `toApiTree()` of the reproducer has `TransferEvent` in `models`.
- Renderer test: the reproducer renders `model/TransferEvent.kt`; `empty-paths.json` renders nothing and returns the
  zero-output diagnostic.
- integration-tests coverage: `openai.yaml` (3.1.0) declares 15 `webhooks`, so after the fix its generated model set
  grows; today nothing asserts on it. `github.json` has `x-webhooks` only; `youtrack.json` none. A regression would
  not be caught by the current compile-only smoke projects.

## Affected documents

adyen.com:BalancePlatformTransferNotification-v3 [2d77a2844fdf636c81dc],
adyen.com:BalancePlatformConfigurationNotification-v1 [3d9933375051b68e0fa6],
adyen.com:ManagementNotificationService-v1 [57719b307f7d6c2e46eb],
adyen.com:BalancePlatformReportNotification-v1 [a69157ddf612038a92d9],
adyen.com:MarketPayNotificationService [b55b84b96005eea66bc4],
adyen.com:BalancePlatformPaymentNotification-v1 [dd44d1bb6330c225fc82],
twilio.com:twilio_numbers_v1 [ca0366588fbf0b3e333b], firstinspires.org [28e1ebef71fe28d291b6],
googleapis.com:youtubeAnalytics [32b273045d0f2c789033], zenoti.com [3480dc95b1e96a2c6ce3] — 11 in total
(the eleventh is ipinfodb.com [d87c5dc0f4266a6e672d]).
