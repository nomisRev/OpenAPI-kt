# ISSUE: Response Media Types Collapse Into A Single Generated API Shape

## Problem

The client renderer currently picks a single "preferred" response model for each status code:

```kotlin
private fun Route.ReturnType.preferredModel(): Model? {
    if (types.isEmpty()) return null
    val jsonEntry = types.entries.firstOrNull { ContentType.Application.Json.match(it.key) }
    return jsonEntry?.value ?: types.values.first()
}
```

That is only correct when all advertised media types for that response decode to the same schema.
The GitHub spec contains several responses where the `Accept` header changes the payload shape:

1. `/user/starred`
   - `application/json` -> `List<Repository>`
   - `application/vnd.github.v3.star+json` -> `List<StarredRepository>`
2. `/repos/{owner}/{repo}/code-scanning/analyses/{analysis_id}`
   - `application/json` -> `CodeScanningAnalysis`
   - `application/sarif+json` -> free-form JSON object
3. `/repos/{owner}/{repo}/contents/{path}`
   - `application/json` -> discriminated union of content shapes
   - `application/vnd.github.object` -> `ContentTree`

Today the generated client exposes only the preferred model and gives the caller no API to request
the alternate media type. This means part of the contract described by the spec is unreachable.

## Why This Matters

1. The generated client can deserialize the wrong schema for the caller's intended `Accept` value.
2. Alternative payloads documented by the provider are effectively lost from the Kotlin API.
3. GitHub uses vendor media types to opt into additional fields or entirely different payload
   structures, so collapsing them is not just a cosmetic omission.
4. The current approach makes the generated surface misleading: a route appears to have one response
   shape when the spec explicitly says otherwise.

## Current GitHub Examples

### Example 1: Same status, different schema

```json
"200": {
  "content": {
    "application/json": {
      "schema": {
        "type": "array",
        "items": { "$ref": "#/components/schemas/repository" }
      }
    },
    "application/vnd.github.v3.star+json": {
      "schema": {
        "type": "array",
        "items": { "$ref": "#/components/schemas/starred-repository" }
      }
    }
  }
}
```

The generated `/user/starred` client still returns only `List<Repository>`.

### Example 2: Same status, vendor object vs JSON union

```json
"200": {
  "content": {
    "application/vnd.github.object": {
      "schema": { "$ref": "#/components/schemas/content-tree" }
    },
    "application/json": {
      "schema": {
        "oneOf": [
          { "$ref": "#/components/schemas/content-directory" },
          { "$ref": "#/components/schemas/content-file" },
          { "$ref": "#/components/schemas/content-symlink" },
          { "$ref": "#/components/schemas/content-submodule" }
        ]
      }
    }
  }
}
```

These are not interchangeable response types and should not be hidden behind one `get()`.

## Desired Outcome

The generated API should model response content negotiation explicitly.

### Case 1: All media types have the same effective schema

If multiple response media types deserialize to the same model, keep a single method and add an
`Accept` enum parameter that defaults to the current default media type.

Example target shape:

```kotlin
public suspend fun get(
  accept: Accept = Accept.ApplicationJson,
): Response

@Serializable
public enum class Accept(public val value: String) {
  ApplicationJson("application/json"),
  ApplicationProblemJson("application/problem+json"),
}
```

The generated method sets the `Accept` header from that enum, but the return type stays unchanged.

### Case 2: Media types have different schemas

If the response body model differs by media type, generate separate entrypoints.

Keep the unsuffixed method for the current default media type, then add one method per alternate
response media type:

```kotlin
public suspend fun get(): List<Repository>
public suspend fun getGithubStarJson(): List<StarredRepository>

public suspend fun getAnalysis(): CodeScanningAnalysis
public suspend fun getAnalysisSarifJson(): JsonObject
```

For simple conventional types this becomes `getJson()`, `getXml()`, etc. For vendor types, the
suffix should preserve the distinctive media-type token so the API remains unambiguous.

## Naming Rules

1. The current unsuffixed method remains the default method for backwards compatibility.
2. Alternate methods are named from the media type:
   - `application/json` -> `Json`
   - `application/xml` -> `Xml`
   - `application/sarif+json` -> `SarifJson`
   - `application/vnd.github.object` -> `GithubObject`
   - `application/vnd.github.v3.star+json` -> `GithubStarJson`
3. If two media types would map to the same suffix, fall back to a longer deterministic suffix.

## Relationship To `ISSUE_RESPONSE_TYPE.md`

Yes, this issue depends on the response-type naming problem being handled correctly.

Once one operation can expose several media-type-specific response methods, inline payload types
must never be named `Response`, otherwise the generated overload-specific return types become
ambiguous or recursive. The detailed naming collision is documented separately in
`ISSUE_RESPONSE_TYPE.md`, but the two issues should be designed together.

## Scope

This issue is about **response** media-type negotiation.

Multiple request-body content types are related but separate. The renderer already uses the spec's
request `Content-Type` when sending a body. If we later want symmetric request-side selection for
same-schema bodies, it can follow the same grouping idea, but it is not required to fix the GitHub
response mismatches described here.

## Proposed Implementation Strategy

1. Group response content entries by structural model equality, not just by exact media type.
2. Determine the default group using the existing preference order:
   - `application/json` first when present
   - otherwise the first declared content type
3. If there is exactly one structural group, generate one method plus an `Accept` enum.
4. If there are multiple structural groups, generate one method per group:
   - default unsuffixed method for the default group
   - suffixed methods for each alternate group
5. Each generated method sets the appropriate `Accept` header automatically.
6. Response wrapper and inline payload naming must follow `ISSUE_RESPONSE_TYPE.md`.

## Affected Files

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRenderer.kt`
- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt`
- Potentially the typed route model if media-type grouping metadata needs to be carried explicitly

## Acceptance Criteria

1. When multiple response media types map to the same schema, the generated method has an `Accept`
   enum parameter defaulting to the current default media type.
2. When multiple response media types map to different schemas, the generated client exposes
   separate methods instead of collapsing them to one return type.
3. The generated implementation sets the correct `Accept` header for both strategies.
4. `/user/starred` can return both `List<Repository>` and `List<StarredRepository>` through
   distinct generated API paths.
5. `/repos/{owner}/{repo}/code-scanning/analyses/{analysis_id}` can request SARIF explicitly.
6. `/repos/{owner}/{repo}/contents/{path}` can request both the JSON union form and the GitHub
   object form explicitly.
7. The solution composes cleanly with the response-type naming fix from `ISSUE_RESPONSE_TYPE.md`.
8. `./gradlew :renderer:jvmTest` passes.
9. `./gradlew :github:compileKotlin` passes in the integration tests.
