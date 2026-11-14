# A flattened request-body field and an operation parameter get the same Kotlin name

**Status:** fixed (renderer; `ParameterNameCollisionRuntimeTest`, `client/parameter-name-collisions`). A flattened body property or form/multipart field with the name of a parameter is `body` + its name (`bodyIds`); the parameter keeps its name. A warning names the operation and the property.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 44 of 1,547 documents fail to compile (amazonaws.com 40, spotify.com ×2, stellastra.com, gsmtasks.com). In 36 this is the only parameter-name collision; in 8 the document also has `issues/parameter-kotlin-name-collisions.md`.
**Source spec valid:** yes — a body property and a parameter are unrelated names.

## Symptom

```
api/Me.kt:137:42: error: conflicting declarations:
ids: String
ids: List<String>? = ...
      public suspend operator fun invoke(ids: String, ids: List<String>? = null): Response {
```

When the two have different types, more errors follow (`encodeURLParameter ... receiver type mismatch`, `argument type mismatch`), because the function body can only see one of them.

## Minimal reproducer

`<scratchpad>/repro/body-field-vs-parameter.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Body", "version": "1" },
  "paths": {
    "/me/albums": {
      "put": {
        "parameters": [
          { "name": "ids", "in": "query", "required": true, "schema": { "type": "string" } }
        ],
        "requestBody": {
          "content": { "application/json": { "schema": {
            "type": "object",
            "properties": { "ids": { "type": "array", "items": { "type": "string" } } }
          } } }
        },
        "responses": { "204": { "description": "ok" } }
      }
    }
  }
}
```

Observed: all phases pass, no diagnostics, `compile exit=1`.

```kotlin
public suspend operator fun invoke(ids: String) { ... }

public suspend operator fun invoke(ids: String, ids: List<String>? = null) {
  val response = client.put("me/albums") {
    url.encodedParameters.append("ids", ids.encodeURLParameter())
    contentType(ContentType.Application.Json)
    setBody(Body(ids = ids))
  }
```

```
api/Me.kt:37:42: error: conflicting declarations:
api/Me.kt:37:55: error: conflicting declarations:
api/Me.kt:39:51: error: candidate 'fun String.encodeURLParameter(spaceToPlus: Boolean = ...): String' is inapplicable because of a receiver type mismatch.
```

## What the spec says

OAS 3.0.3 §4.7.10: parameters are unique by name and location among parameters. §4.7.13 Request Body Object and §4.7.24 Schema Object put no relation between a body schema's property names and the operation's parameters. Spotify's `DELETE /me/albums` really accepts `ids` as a query string and as a JSON array; AWS list operations accept `maxResults` in the query (string) and in the body (integer).

## Root cause

Traced. An inline object body is flattened into the `invoke` arguments. `buildFlattenedBodyOverload` in `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererRequestBody.kt:339-340` names each argument `propertyName.toParamName()`. `toInvokeFunSpecForFlattenedBody` (`ClientRendererOperations.kt:546-605`) then adds the route's parameter specs (named by `toParameterSpec`, line 741) and `overload.parameters` to one function, without comparing names.

The same happens for the other flattened body kinds: form fields and multipart parts are named `formData.name.toParamName()` in `toInvokeParameterSpecs` (`ClientRendererOperations.kt:817,829`) and added next to the route's parameters in `ClientRendererOperations.kt:414-453`. gsmtasks.com shows it for `application/json`, `application/x-www-form-urlencoded` and `multipart/form-data` on the same operation.

A related case with one argument: a query parameter named `body` next to a non-flattened body gives `invoke(body: List<String>, body: String? = null)`. I reproduced that by hand (array body plus query `body`). One corpus document has a query parameter named `body` on an operation with a request body (gamesparks.net:game-details [3073c30914f20abdf2fe]) and compiles; I did not check what its body renders as.

Counting note: 201 documents have an operation where a body property and a parameter share a Kotlin name, but flattening applies only to some body shapes, so only the 44 observed failures are counted here.

## Correct final API

All inputs remain, with distinct argument names, each sent where it was declared. Two designs:

1. Keep the flattened signature and rename the body field on collision. The operation parameter keeps its name, since it is addressed by that name on the wire; the body field gets a `body` prefix:

   ```kotlin
   public suspend operator fun invoke(ids: String, bodyIds: List<String>? = null)
   // sends ?ids=... and {"ids":[...]}
   ```

2. Don't flatten a body that has a field colliding with a parameter: generate `invoke(ids: String, body: Body)` with a public nested `Body` class.

Design 1 keeps one calling convention for every flattened body and is the proposal. Either way the wire names don't change, and a warning diagnostic should name the operation and the renamed field.

## Fix slice

- Renderer: `buildFlattenedBodyOverload` and `toInvokeParameterSpecs` need the set of names already taken by the route's parameters. Pass it in and rename colliding fields; the constructor call `Body(ids = bodyIds)` and the form/multipart `append("ids", bodyIds)` calls must use the renamed argument while keeping the wire name.
- This should be the same per-operation name allocator proposed in `issues/parameter-kotlin-name-collisions.md`, which also reserves `body`, `requestType` and `contentType`. `callerContentTypeParameterName` (`ClientRendererRequestBody.kt:133-137`) already does this for one name.
- Open decision: design 1 or 2 above; and for design 1 the prefix (`bodyIds`) versus a numeric suffix (`ids2`).

## Proof

- Failing test first: a `ClientSpec` golden case with the reproducer, plus a form-urlencoded variant.
- Runtime: extend `SeparateMethodsFlattenedBodyRuntimeTest` or `InlineJsonBodyOverloadsRuntimeTest` with a MockEngine assertion that the query has `ids=a` and the JSON body has `"ids":["b"]`.
- Integration tests would not catch a regression: no operation in `github.json`, `openai.yaml` or `youtrack.json` has a body property with the Kotlin name of one of its parameters (checked by script).

## Affected documents

44 observed. spotify.com [2bbdfcbf792b07728f79]; spotify.com:sonallux [1778bb3d1c56116fa4f3]; stellastra.com [903dc3e1910e6143ef24]; gsmtasks.com [daf353ec0ddccdfbd4fa]; amazonaws.com:accessanalyzer [cfeb20130604c31291c2]; amazonaws.com:amplifybackend [e2412986987cca0a417d]; amazonaws.com:appflow [88bc732600798210c260]; amazonaws.com:batch [6e07b9531fef7bcab27d]; amazonaws.com:braket [e0229773021e92967423]; amazonaws.com:kinesis-video-archived-media [0d16276143e9b9d85fdb].
