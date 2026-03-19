# Phase 1: Introduce `Route.Body.OverloadedBody` in the typed layer

## Goal

Add a new `Route.Body` variant that represents a flattened oneOf union, and produce it from `RequestBody.kt` when the requestBody schema is an inline non-discriminated `oneOf`.

## Motivation

Currently, `RequestBody.toBody()` always produces `Route.Body.SetBody` — even when the model is a `Model.Union`. The renderer then has to inspect the body's model to decide how to render it. By introducing a dedicated `OverloadedBody` variant, the typed layer makes the "this should be overloads" decision explicit, and the renderer can simply pattern-match on the body type.

## Files to modify

### `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/Route.kt`

Add a new variant to the `Route.Body` sealed interface:

```kotlin
/**
 * A request body whose oneOf union has been flattened into individual cases.
 * Each case becomes a separate function overload in the generated client.
 */
data class OverloadedBody(
    val contentType: ContentType,
    val cases: List<Model.Union.Case>,
    override val description: String?,
    override val extensions: Map<String, JsonElement>,
) : Body
```

Update `Bodies.setBodyOrNull()` (private, called by `defaultOrNull()`):
- Rename to something more general, or add `OverloadedBody` to the check. The `defaultOrNull()` method should also return `OverloadedBody` as a valid default body.

Update `Bodies?.nested()`:
- Add a branch for `OverloadedBody` that collects inline models from each case:
```kotlin
is Body.OverloadedBody -> body.cases.mapNotNull { it.model.nestedOrNull() }
```

### `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/RequestBody.kt`

In `RequestBody.toBody()` (lines 94-108), after resolving the model via `schema.toModel(name, SchemaContext.Write)`:

```kotlin
private suspend fun RequestBody.toBody(...): Route.Body {
    val name = NamingContext.path(segments, method).nest(NamingContext.RouteBody)
    val model = schema.toModel(name, SchemaContext.Write)

    // Flatten inline non-discriminated unions into overloads
    if (model is Model.Union && model.discriminator == null && model.context.head is NamingContext.Path) {
        return Route.Body.OverloadedBody(
            contentType = ContentType.parse(contentType),
            cases = model.cases,
            description = description,
            extensions = mediaType.extensions,
        )
    }

    return Route.Body.SetBody(
        contentType = ContentType.parse(contentType),
        type = model,
        description = description,
        extensions = mediaType.extensions,
    )
}
```

The conditions for flattening:
1. `model is Model.Union` — the schema is a oneOf/anyOf
2. `model.discriminator == null` — no discriminator (discriminated unions should stay as sealed interfaces)
3. `model.context.head is NamingContext.Path` — the union is inline to the route (not a `$ref` to a named component)

## Test approach (TDD)

**Step 1**: Add a new test in `ClientSpec.kt` with a simple inline oneOf requestBody (2 cases — an object and a string). This test will initially fail because no golden files exist and the renderer doesn't handle `OverloadedBody` yet.

```kotlin
clientTest(
    """
    {
      "openapi": "3.1.0",
      "info": { "title": "Api", "version": "0.0.1" },
      "paths": {
        "/pets": {
          "post": {
            "requestBody": {
              "required": true,
              "content": {
                "application/json": {
                  "schema": {
                    "oneOf": [
                      {
                        "type": "object",
                        "required": ["name"],
                        "properties": {
                          "name": { "type": "string" },
                          "tag": { "type": "string" }
                        }
                      },
                      { "type": "string" }
                    ]
                  }
                }
              }
            },
            "responses": {
              "201": {
                "description": "Created",
                "content": {
                  "application/json": {
                    "schema": { "type": "string" }
                  }
                }
              }
            }
          }
        }
      }
    }
    """.trimIndent(),
    "client/overloaded-body-simple"
)
```

**Step 2**: Implement the `OverloadedBody` variant and `RequestBody.kt` changes.

**Step 3**: Verify with `./gradlew :typed:allTests` that existing typed-layer tests still pass.

## Acceptance criteria

- [ ] `Route.Body.OverloadedBody` data class exists in `Route.kt`
- [ ] `RequestBody.toBody()` produces `OverloadedBody` for inline non-discriminated oneOf bodies
- [ ] `RequestBody.toBody()` still produces `SetBody` for `$ref` unions and discriminated unions
- [ ] `Bodies.defaultOrNull()` returns `OverloadedBody` when it's the default body type
- [ ] `Bodies?.nested()` correctly collects inline models from `OverloadedBody` cases
- [ ] `./gradlew :typed:allTests` passes
- [ ] Test case added in `ClientSpec.kt` (will fail until Phase 2 completes the renderer)
