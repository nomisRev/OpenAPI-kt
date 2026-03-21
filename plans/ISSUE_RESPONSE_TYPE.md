# ISSUE: Inline Response Schemas Collide With The Generated `Response` Wrapper Type

## Problem

The renderer currently uses the nested name `Response` for two different concepts:

1. the method-level response wrapper returned by the generated client
2. inline schemas that originate from response bodies

When an operation needs a sealed response wrapper and one of its response-body schemas is inline,
those two concepts collapse onto the same type name. The result is a generated API where response
cases recursively refer to the wrapper itself instead of the actual payload model.

## Minimal Reproducer 1: Inline 200 Schema Inside A Multi-Status Operation

Consider an operation with an inline success schema and a referenced error schema:

```json
{
  "responses": {
    "200": {
      "content": {
        "application/json": {
          "schema": {
            "oneOf": [
              { "$ref": "#/components/schemas/content-directory" },
              { "$ref": "#/components/schemas/content-file" }
            ],
            "discriminator": {
              "propertyName": "type"
            }
          }
        }
      }
    },
    "404": {
      "content": {
        "application/json": {
          "schema": { "$ref": "#/components/schemas/basic-error" }
        }
      }
    }
  }
}
```

The correct generated Kotlin shape should be conceptually:

```kotlin
public sealed interface Response {
  public data class Ok(
    public val value: OkBody,
  ) : Response

  public data class NotFound(
    public val value: BasicError,
  ) : Response
}

@Serializable
public sealed interface OkBody
```

Instead, the current generator can produce:

```kotlin
public sealed interface Response {
  public data class Ok(
    public val value: Response,
  ) : Response

  public data class NotFound(
    public val value: BasicError,
  ) : Response
}
```

The success payload type has collapsed into the wrapper interface itself.

## Minimal Reproducer 2: Inline Error Schema

Consider an operation whose non-success branch uses an inline object schema:

```json
{
  "responses": {
    "200": {
      "content": {
        "application/json": {
          "schema": { "$ref": "#/components/schemas/code-scanning-analysis" }
        }
      }
    },
    "503": {
      "content": {
        "application/json": {
          "schema": {
            "type": "object",
            "properties": {
              "code": { "type": "string" },
              "message": { "type": "string" },
              "documentation_url": { "type": "string" }
            }
          }
        }
      }
    }
  }
}
```

The generated branch should look like:

```kotlin
public data class ServiceUnavailable(
  public val value: ServiceUnavailableBody,
) : Response
```

but today it can become:

```kotlin
public data class ServiceUnavailable(
  public val value: Response,
) : Response
```

Again the branch payload type is no longer the JSON body schema.

## Real GitHub Examples

This is not theoretical. The generated GitHub client contains these failures today:

1. `/repos/{owner}/{repo}/contents/{path}`
   - `Ok(value: Response) : Response`
2. `/repos/{owner}/{repo}/code-scanning/analyses/{analysis_id}`
   - `ServiceUnavailable(value: Response) : Response`

Across the generated GitHub client there are 132 response branches in 12 API files where the case
payload type is literally `Response`.

## Root Cause

The renderer reserves `Response` as the nested wrapper name in one place, while inline response
model naming also targets `Response` in another place.

### Wrapper reservation

`ClientRenderer.kt` builds the method wrapper as `Response`:

```kotlin
private fun Route.buildSealedResponseTypeSpec(...): TypeSpec {
    val responseClassName = methodClassName.nestedClass("Response")
    val builder = TypeSpec.interfaceBuilder("Response")
    ...
}
```

### Inline response ownership

`OperationInlineModelScope.kt` also uses `methodClassName.nestedClass("Response")` as the owner for
response inline models:

```kotlin
val responseOwnerClassName =
    if (returns.isSingleDirectModelResponse()) methodClassName
    else methodClassName.nestedClass("Response")
```

### Raw case type emission

`buildSealedResponseTypeSpec()` then emits case payload types from `model.toTypeName(config)`
without reserving a distinct payload type name:

```kotlin
val typeName = model.toTypeName(config)
```

That makes the wrapper and the inline payload fight for the same symbol.

## Why This Matters

1. The public Kotlin API is incorrect: `Response.Ok.value` is typed as the wrapper, not as the
   JSON body.
2. Ktor response decoding is inferred from that wrong type, so runtime deserialization is either
   impossible or nonsensical.
3. Callers cannot inspect the actual payload shape safely.
4. The problem compounds once media-type-specific overloads are introduced, because those overloads
   also need distinct response-body types.

## Desired Outcome

`Response` must be reserved exclusively for the wrapper type.

Inline response payloads should use distinct nested names, for example:

1. single inline response without a sealed wrapper -> `Body`
2. sealed response case payload -> `${StatusCaseName}Body`
3. media-type-specific case payload -> `${StatusCaseName}${MediaTypeSuffix}Body`

Example target shape:

```kotlin
public sealed interface Response {
  public data class Ok(
    public val value: OkBody,
  ) : Response

  public data class ServiceUnavailable(
    public val value: ServiceUnavailableBody,
  ) : Response
}

@Serializable
public sealed interface OkBody

@Serializable
public data class ServiceUnavailableBody(
  public val code: String? = null,
  public val message: String? = null,
  public val documentationUrl: String? = null,
)
```

## Proposed Implementation Strategy

1. Reserve `Response` for wrapper types only.
2. Introduce a payload-type naming strategy for inline response schemas:
   - `Body`
   - `OkBody`
   - `NotFoundBody`
   - `OkSarifJsonBody`
3. Make `OperationInlineModelScope` target those payload-owner names instead of `Response`.
4. Make `buildSealedResponseTypeSpec()` use remapped inline response type names rather than raw
   `model.toTypeName(config)`.
5. Ensure generated nested payload type names are deconflicted against existing nested models.
6. Keep the wrapper/case structure unchanged for callers, only fix the case payload type.

## Relationship To `ISSUE_MEDIA_TYPE.md`

This is a prerequisite for a clean media-type solution.

Once one route can expose separate media-type-specific methods, each method or response branch may
need its own inline payload type. That only works if inline payload names no longer collide with
`Response`.

## Affected Files

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRenderer.kt`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/OperationInlineModelScope.kt`
- Potentially response inline type emission helpers in the renderer

## Acceptance Criteria

1. No generated response branch has a payload type equal to the enclosing wrapper `Response`.
2. Inline 200 response schemas inside multi-status operations generate distinct payload-holder
   types such as `OkBody`.
3. Inline non-200 response schemas generate distinct payload-holder types such as
   `ServiceUnavailableBody`.
4. `/repos/{owner}/{repo}/contents/{path}` no longer generates `Ok(value: Response)`.
5. `/repos/{owner}/{repo}/code-scanning/analyses/{analysis_id}` no longer generates
   `ServiceUnavailable(value: Response)`.
6. The response-type naming scheme composes with the media-type plan from `ISSUE_MEDIA_TYPE.md`.
7. `./gradlew :renderer:jvmTest` passes.
8. `./gradlew :github:compileKotlin` passes in the integration tests.
