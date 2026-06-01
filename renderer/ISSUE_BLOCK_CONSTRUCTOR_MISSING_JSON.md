# Configurable API constructor drops JSON serialization

## Summary
The renderer generates two root API constructors:
- a configurable constructor that accepts `block: HttpClientConfig<*>.() -> Unit`
- a convenience constructor with no block

Only the convenience constructor installs `ContentNegotiation { json() }`.
As a result, the configurable constructor silently produces a client that cannot deserialize normal JSON responses unless callers remember to install the plugin themselves.

## Affected files
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/FactoryRenderer.kt`

## Current behavior
`FactoryRenderer.kt` intentionally emits:
- `baseUrlConstructorWithBlock()` / `serverConstructorWithBlock()` without JSON installation
- `baseUrlConstructorNoBlock()` / `serverConstructorNoBlock()` with JSON installation

Relevant code:
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/FactoryRenderer.kt:80-108`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/FactoryRenderer.kt:111-151`

Generated GitHub example:
- `integration-tests/github/src/main/kotlin/github/io/github/api/GitHubV3RESTAPI.kt:90-99`

## Why this is a problem
For generated API clients, the configurable constructor is the natural entry point for adding logging, auth, timeouts, or engine tweaks.

Today that overload is a footgun:
- `GitHubV3RESTAPI(baseUrl)` works
- `GitHubV3RESTAPI(baseUrl) { ... }` loses JSON support

That behavior is surprising, non-idiomatic, and easy to miss because both constructors otherwise look equivalent.

## Expected behavior
The configurable constructor should remain configurable without dropping baseline JSON support.

Acceptable outcomes:
1. install `ContentNegotiation { json() }` before `block()` and document how callers override it, or
2. generate a clearer API shape that makes the opt-out explicit instead of silent

## Reproduction
Observed in generated GitHub client:
- spec: `parser/src/commonTest/resources/specs/github.json`
- generated file: `integration-tests/github/src/main/kotlin/github/io/github/api/GitHubV3RESTAPI.kt`

## Acceptance criteria
- The generated configurable constructor preserves working JSON serialization by default.
- Caller customization still works for normal `HttpClientConfig` use cases.
- Renderer tests cover both constructor variants and verify that both can deserialize JSON responses.
