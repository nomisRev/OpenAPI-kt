# An inline union with unique-key dispatch in an API file uses `jsonObject` without importing it

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 1 of 1,547 documents (slack.com; it also has the empty enum name error, no excluded errors)
**Source spec valid:** yes — `anyOf` of inline objects in an inline response schema (OAS 3.0.3 §4.7.24)

## Symptom

```
ConversationsReplies.kt:180:32: error: unresolved reference 'jsonObject' on receiver of type 'JsonElement'.
    val keys = value.jsonObject.keys
```

## Minimal reproducer

`/private/tmp/claude-501/-Users-simonvergauwen-Developer-OpenAPI-kt/a0df68b9-ccc6-4d18-981e-753c6b12368d/scratchpad/repro/inline-union-jsonobject-import.json`

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},"paths":{"/replies":{"get":{"responses":{"200":{"description":"ok",
  "content":{"application/json":{"schema":{"type":"object","properties":{"message":{"anyOf":[
    {"type":"object","required":["last_read"],"properties":{"last_read":{"type":"string"}}},
    {"type":"object","required":["bot_id"],"properties":{"bot_id":{"type":"string"}}}]}}}}}}}}}}}
```

Observed: `api/Replies.kt:71:30: error: unresolved reference 'jsonObject' on receiver of type 'JsonElement'.`

## What the spec says

Not a spec question; the generated serializer is ours.

## Root cause

Traced. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/UnionRendererSerialization.kt:463` writes the extension property as text: `addStatement("val keys = value.jsonObject.keys")`. KotlinPoet cannot see it, so each model file adds the import by hand when `needsJsonObjectImport()` (`JsonObjectImport.kt`): `ObjectRenderer.kt:226-227`, `UnionRenderer.kt:38-39`, `DiscriminatedObjectRenderer.kt:75-76`. API files (`ClientRenderer.kt`) have no such step, so a union nested in an operation's inline response or body gets no import.

## Correct final API

Unchanged API; the file imports `kotlinx.serialization.json.jsonObject` and the union decodes by its unique keys.

## Fix slice

- Renderer: emit it as a member, `addStatement("val keys = value.%M.keys", MemberName("kotlinx.serialization.json", "jsonObject"))`, and delete `needsJsonObjectImport` and the three manual `addImport` calls. Check the other raw-text extension uses in `UnionRendererSerialization.kt` / `UnionTokenKindDispatch.kt` (`jsonPrimitive`, `jsonArray`) for the same pattern.

## Proof

- Failing test first: golden `ClientSpec` case of the reproducer, compiled; round-trip test of both cases through MockEngine.
- Integration tests: github/openai/youtrack compile, so none has a unique-key-dispatch `anyOf` inline in an operation. Not covered (not checked by script).

## Affected documents

slack.com [4b4bd2539a8ae61e458d]. Total 1.
