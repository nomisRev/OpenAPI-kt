# Status codes Ktor has no name for all become `UnknownStatusCode`

**Status:** fixed on openapi-3.2-streaming: a code Ktor has no name for is `Status<code>`, the nine registered codes Ktor lacks get their IANA name (with a generated `HttpStatusCode.Companion` extension per used code, in the api package), and the hyphenated 203, 207 and 414 are `NonAuthoritativeInformation`, `MultiStatus`, `RequestUriTooLong`. Proof: `StatusCodeCaseNameTest`, golden `client/status-code-case-names`, `StatusCodeCaseNamesRuntimeTest`.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 209 of 1,547 documents fail to compile (all amazonaws.com). 263 documents have an operation with two or more such codes; the other 54 fail before compilation. 98 documents have an operation with exactly one such code: those compile, with a case named `UnknownStatusCode`.
**Source spec valid:** yes — any HTTP status code may be a key of the Responses Object.

## Symptom

```
error: conflicting declarations:
data class UnknownStatusCode : #XAmzTarget=AWSMigrationHubAssociateCreatedArtifact.Post.Response
data class UnknownStatusCode : #XAmzTarget=AWSMigrationHubAssociateCreatedArtifact.Post.Response
... (8 times: responses 480–487)
```

## Minimal reproducer

`<scratchpad>/repro/status-code-case-names.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Status", "version": "1" },
  "paths": {
    "/ping": {
      "get": {
        "responses": {
          "200": { "description": "ok", "content": { "application/json": { "schema": { "type": "string" } } } },
          "480": { "description": "first vendor error", "content": { "application/json": { "schema": { "type": "object", "properties": { "a": { "type": "string" } } } } } },
          "481": { "description": "second vendor error", "content": { "application/json": { "schema": { "type": "object", "properties": { "b": { "type": "string" } } } } } }
        }
      }
    }
  }
}
```

Observed: all phases pass, no diagnostics, `compile exit=1`.

```kotlin
return when (response.status.value) {
  200 -> Response.Ok(...)
  480 -> response.body<Response.UnknownStatusCode>()
  481 -> response.body<Response.UnknownStatusCode>()
  else -> throw ResponseException(response, "")
}

public sealed interface Response {
  public data class Ok(public val `value`: String) : Response
  public data class UnknownStatusCode(public val a: String? = null) : Response
  public data class UnknownStatusCode(public val b: String? = null) : Response
}
```

```
api/Ping.kt:40:25: error: conflicting declarations:
data class UnknownStatusCode : Ping.Get.Response
api/Ping.kt:47:25: error: conflicting declarations:
data class UnknownStatusCode : Ping.Get.Response
```

## What the spec says

OAS 3.0.3 §4.7.16 Responses Object, patterned field `HTTP Status Code`: "Any HTTP status code can be used as the property name, but only one property per code". The spec does not limit codes to the IANA registry. AWS documents use 480–499 (and 508–527) for their modelled exceptions.

## Root cause

Traced.

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/Return.kt:29` turns each declared code into `HttpStatusCode.fromValue(statusCode)`. For a code outside Ktor's table, Ktor returns `HttpStatusCode(value, "Unknown Status Code")`. The value is kept, so dispatch (`480 ->`, `481 ->`) is right.
- `HttpStatusCode.toCaseName()` in `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ContentTypeStrategy.kt:268-271` builds the case name from `description` alone. Every unknown code yields `UnknownStatusCode`.
- All 16 call sites of `toCaseName()` share this (`ImplRenderer.kt:510,577,589,609,613,749,757`, `ContentTypeStrategy.kt:29,104`, `ClientRendererResponses.kt:108,118,128,140,396,647`), so sealed responses, multi-content-type responses and error dispatch are all affected.

Which codes: checked by running `HttpStatusCode.fromValue` for 100–599 against the Ktor version on the runner classpath. Ktor names 53 codes: 100–102, 200–207, 300–308, 400–417, 422–426, 429, 431, 500–507. Every other code maps to `UnknownStatusCode`. That includes registered codes 103, 208, 226, 418, 421, 428, 451, 508, 510 and 511.

Codes found in colliding operations in the corpus, by operation count: 480 and 481 (10,520 each), 482 (8,775), 483 (7,130), 484 (5,225), 485 (3,312), 486 (1,938), 487 (890), 488 (518), 489–499 (48–356 each), 508–527 (5–20 each). Lone unknown codes seen: 480, 596, 451, 553, 420, 555, 449, 510, 299, 209.

Side observation from the same run: three Ktor descriptions contain a hyphen, so 203, 207 and 414 get the case names `Non-authoritativeInformation`, `Multi-status` and `Request-uriTooLong`, which need backticks.

## Correct final API

One case per declared code, named so the code is visible, with the declared body type of that code:

```kotlin
public sealed interface Response {
  public data class Ok(public val `value`: String) : Response
  public data class Status480(public val a: String? = null) : Response
  public data class Status481(public val b: String? = null) : Response
}
```

Dispatch stays by numeric value (`480 -> response.body<Response.Status480>()`). Codes Ktor names keep their current names (`NotFound`), so existing generated APIs don't change.

## Fix slice

- Renderer: in `toCaseName()`, return `Status<value>` when the code is not in `HttpStatusCode.allStatusCodes` (compare by value, not by the description string). One function, all call sites follow.
- Renderer: normalise the three hyphenated names (`NonAuthoritativeInformation`, `MultiStatus`, `RequestUriTooLong`). This renames existing cases, so decide whether to do it in the same change.
- Open decision: `Status480` versus `Http480` versus a name from the response `description` ("first vendor error"). Descriptions are free text and often long or repeated, so the numeric name is the stable choice; the description can go into KDoc.
- Open decision: name the registered codes Ktor lacks (`EarlyHints`, `ImATeapot`, `UnavailableForLegalReasons`, …) from a small table in the renderer, or leave them as `Status451`.
- No typed-layer change is needed: `Route.Returns.responses` already keeps the value.

## Proof

- Failing test first: a `ClientSpec` golden case with the reproducer, asserting `Status480` and `Status481`.
- Runtime: a MockEngine test next to `DirectResponseStatusRuntimeTest` that answers 480 and 481 with different JSON and asserts each decodes to its own case.
- Integration tests would not catch a regression: `github.json`, `openai.yaml` and `youtrack.json` declare no status code outside Ktor's table (checked with the same script that counted the corpus).

## Affected documents

209 observed, all amazonaws.com. Examples: AWSMigrationHub [00059ba6ca9e7ae965de]; transcribe [021cd15b8c8d2ff9267e]; mediapackage-vod [032212ce36e243a35ad6]; accessanalyzer [cfeb20130604c31291c2]; acm [1aee9615d431b1a4b397]; acm-pca [c46ba8a2977b1622ce9e]; alexaforbusiness [2de6ed6b992b3531c9eb]; amp [66ff214bc837f9158933]; amplify [d770f29265d32491bcce]; apigateway [ea24778326624227187f].

Every one of the 209 also fails on `issues/server-case-names-collide.md`.
