# Generated map-shaped objects can degrade into list-shaped APIs in integration output

## Summary
The GitHub integration output contains several schemas that are defined in OpenAPI as `type: object` with `additionalProperties`, but the generated Kotlin surface is list-shaped and loses the object keys.

This is not faithful to the spec.

## Affected renderer surface
Possible renderer touchpoints:
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ObjectRenderer.kt`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererRequestBody.kt`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/InputFlattening.kt`

Relevant renderer code already handles typed additional properties in some paths:
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ObjectRenderer.kt:305-410`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererRequestBody.kt:189-309`

So this issue is likely an interaction/regression in how additional-properties-only object shapes are normalized before final rendering, not simply a missing serializer implementation.

## Reproduction
Observed in generated GitHub output from:
- spec: `parser/src/commonTest/resources/specs/github.json`

Generated files:
- `integration-tests/github/src/main/kotlin/github/io/github/api/Gists.kt:93-171`
- `integration-tests/github/src/main/kotlin/github/io/github/model/BaseGist.kt:32-60`
- `integration-tests/github/src/main/kotlin/github/io/github/model/GistSimple.kt:84-111`

Examples:
- `POST /gists` request body emits `files: List<Files>`
- `base-gist.files` emits `List<Files>`
- `gist-simple.files` emits `List<Files>`

But the spec defines these as objects with `additionalProperties`, for example:
- request body for `POST /gists`: filenames mapped to `{ content: string }`
- `base-gist.files`: filename-keyed object
- `gist-simple.files`: filename-keyed object whose values may be nullable

## Why this is a problem
A list representation drops the filename keys entirely.
That changes the wire shape:
- expected: `{ "README.md": { "content": "Hello" } }`
- generated API shape: a list of values with no stable key

This makes the generated Kotlin API semantically incorrect even if the value object itself is rendered nicely.

## Expected behavior
Map-shaped OpenAPI objects should remain map-shaped in generated Kotlin APIs.

Examples:
- request bodies should expose `Map<String, FileValue>`-style inputs
- response models should expose `Map<String, FileValue?>` when the value schema is nullable
- scalar-wrapper optimization for map values must not erase the surrounding map container

## Notes
Renderer golden tests already cover some `additionalProperties` cases successfully, which suggests the GitHub failure may require a more specific regression test around:
- object properties whose value is an additional-properties object
- request bodies mixing normal properties with map-shaped properties
- map values that are inline scalar-wrapper objects
- nullable map values in response models

## Acceptance criteria
- GitHub `files`-style schemas render as `Map<String, ...>` instead of `List<...>`.
- Generated request body types preserve object keys for `additionalProperties` maps.
- Generated response model types preserve object keys for `additionalProperties` maps.
- Focused renderer tests reproduce the GitHub shape and prevent regression.
