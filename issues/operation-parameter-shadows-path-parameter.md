# An operation parameter with the Kotlin name of a path parameter replaces it in the request path

**Status:** fixed (path parameters are written `this.name` in the path template); open: the checked-in `integration-tests` clients are not regenerated yet
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** wrong output (silent) when the types are compatible; compile otherwise
**Impact:** 14 of 1,547 documents: 3 compile and send the wrong path (tfl.gov.uk, opentargets.io, vtex.local:Catalog-API-Seller-Portal), 11 fail to compile (combell.com, xero.com:xero_bankfeeds, interactivebrokers.com, docusign.net, authentiq.io, warwick.ac.uk:enterobase, clickmeter.com, trello.com, gsmtasks.com, ix-api.net, amazonaws.com:apigateway). Also `integration-tests/github` (3 operations).
**Source spec valid:** yes — a parameter is unique by name **and** location (OAS 3.0.3 §4.7.10.1), and a request body property is unrelated to parameters

## Symptom

Compile errors, depending on the type of the shadowing parameter:

```
api/Accounts.kt:95:57: error: candidate 'fun String.encodeURLParameter(spaceToPlus: Boolean = ...): String' is inapplicable because of a receiver type mismatch.   // Int, Uuid, List<String>?
api/Client.kt:225:53: error: only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'String?'.                              // optional String
```

No error at all when the shadowing parameter is a required `String`: the client compiles and builds the path from
the wrong value.

## Minimal reproducer

`<scratchpad>/repro/path-and-query-parameter-same-name.json`:

```json
{
  "openapi": "3.0.3",
  "info": { "title": "same name", "version": "1" },
  "paths": {
    "/accounts/{accountId}": {
      "get": {
        "parameters": [
          { "name": "accountId", "in": "path", "required": true, "schema": { "type": "string" } },
          { "name": "account_id", "in": "query", "required": true, "schema": { "type": "integer", "format": "int32" } }
        ],
        "responses": { "200": { "description": "ok" } }
      }
    },
    "/users/{id}": {
      "get": {
        "parameters": [
          { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } },
          { "name": "id", "in": "query", "required": true, "schema": { "type": "string" } }
        ],
        "responses": { "200": { "description": "ok" } }
      },
      "post": {
        "parameters": [
          { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } }
        ],
        "requestBody": { "required": true, "content": { "application/json": { "schema": {
          "type": "object", "required": ["id"],
          "properties": { "id": { "type": "string" }, "name": { "type": "string" } } } } } },
        "responses": { "200": { "description": "ok" } }
      }
    }
  }
}
```

```
compile exit=1
api/Accounts.kt:26:57: error: candidate 'fun String.encodeURLParameter(spaceToPlus: Boolean = ...): String' is inapplicable because of a receiver type mismatch.
```

`api/Users.kt` compiles:

```kotlin
public class IdPath internal constructor(
  private val client: HttpClient,
  private val id: String,
) {
  public class Get internal constructor(private val client: HttpClient, private val id: String) {
    public suspend operator fun invoke(id: String) {
      val response = client.get("users/${id.encodeURLParameter()}") {
        url.encodedParameters.append("id", id.encodeURLParameter())
      }
  ...
  public class Post internal constructor(private val client: HttpClient, private val id: String) {
    public suspend operator fun invoke(id: String, name: String? = null) {
      val response = client.post("users/${id.encodeURLParameter()}") {
        contentType(ContentType.Application.Json)
        setBody(Body(id = id, name = name))
```

`api.users.id("A").post(id = "B")` sends `POST users/B`; the path value `"A"` is never used.

## What the spec says

OAS 3.0.3 §4.7.10.1 (Operation Object `parameters`): "A unique parameter is defined by a combination of a name and
location." `id` in `path` and `id` in `query` are two parameters. The same holds after our camel-casing
(`accountId` / `account_id`, `statementID` / `statementId`). A request body schema property shares no namespace with
parameters at all; "rename" endpoints (`PATCH /variables/{name}` with body `{ "name": "new" }`, as in GitHub) depend on
the two being different values.

## Root cause

A path parameter is a private property of the operation class
(`renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt:99-107`, named `param.name.toCamelCase()`),
and the path template refers to it by bare name
(`ParameterSerialization.kt:447`, `val variable = segment.name.toCamelCase()`, written as `$variable` at `:449` or by
`expansionCode` at `:450-458`). The `invoke` function's parameters — query/header/cookie parameters and the properties
of a flattened inline body (`ClientRendererOperations.kt:741`, `name.toParamName()`) — are not checked against the
accumulated path parameter names, and a function parameter is the nearer declaration.

Before 4f5c259c ("Percent-encode path parameter values as a path segment") the path was `"/users/$id"`, so every
case, including the nullable ones, compiled and was silently wrong (an absent optional body field sent the path
segment `null`). That commit added `.encodeURLParameter()`, which turned the nullable and non-`String` cases into
compile errors; the required-`String` case is still silent.

## Correct final API

Both values keep their names; the path uses the path value.

```kotlin
public suspend operator fun invoke(id: String, name: String? = null) {
  val response = client.post("users/${this.id.encodeURLParameter()}") {
    contentType(ContentType.Application.Json)
    setBody(Body(id = id, name = name))
  }
```

Wire for `api.users.id("A").post(id = "B")`: `POST users/A` with body `{"id":"B"}`;
for `api.accounts.accountId("x").get(accountId = 7)`: `GET accounts/x?account_id=7`.

## Fix slice

Renderer only: qualify path parameter references in `Route.pathLiteral` with `this.` (the operation class instance;
the template is evaluated outside the request lambda, so `this` is the operation). The expression passed to
`expansionCode` becomes `this.id`; `interpolated()` (`ParameterSerialization.kt:521`) then writes `${this.id}` because
it no longer matches `Identifier`. Qualifying unconditionally keeps the output uniform; qualifying only on collision
keeps existing golden files unchanged — an open choice.

Related: `parameter-named-client-shadows-http-client.md`, `query-parameter-named-url-shadows-request-builder.md` (same
shadowing mechanism on generator-owned names). Several documents that look like "nullable path parameter" failures
are this bug: the nullable type is the optional operation parameter, not the path parameter.

## Proof

- Failing tests first: a MockEngine runtime test for `/users/{id}` with a body property `id` and with a query
  parameter `id`, asserting `request.url.encodedPath == "/users/A"` and the body/query carry `B`. A golden
  `ClientSpec` case alone would pin the wrong text.
- integration-tests: `github.json` contains the construct three times — `PATCH /orgs/{org}/actions/variables/{name}`,
  `PATCH /repos/{owner}/{repo}/actions/variables/{name}` and
  `PATCH /repos/{owner}/{repo}/environments/{environment_name}/variables/{name}`, each with an optional body property
  `name`. The checked-in working copy `integration-tests/github/src/main/kotlin/io/github/api/Orgs.kt:3219` (regenerated
  2026-11-14) reads `invoke(name: String? = null, ...)` followed by
  `client.patch("orgs/${org.encodeURLParameter()}/actions/variables/${name.encodeURLParameter()}")`, which by the
  reproducer above does not compile. Not verified by running Gradle (out of scope for this triage); if confirmed, the
  github integration build is currently red and is the regression test for this issue. `youtrack.json` has no such case.

## Affected documents

Counted by scanning every emitted `api/*.kt` for an operation function parameter that has the name of a constructor
property of its class (`<scratchpad>/cc/samename.py`, result in `<scratchpad>/cc/samename.json`); 14 documents.

Compile and send the wrong path (3): tfl.gov.uk [43a05b4b9b41a5afa291] (28 operations, e.g. `postcode`),
opentargets.io [4c4576d29467f3ef3768] (`drugId`), vtex.local:Catalog-API-Seller-Portal [0899e56aa7e10ef2339c] (`productId`).

Fail to compile (11): combell.com [decb78b90a27e36a9a08] (72 operations; query `account_id` vs path `accountId`; its only
errors), xero.com:xero_bankfeeds [7d7fc429660036a5a399] (query `statementId: Uuid` vs path `statementID`),
authentiq.io [c41d28a88d28eb819b75] (only error), docusign.net [ac5d9fe0b91a84bec43a] (only error),
interactivebrokers.com [30288a85c1db53cb1aa5], warwick.ac.uk:enterobase [d2e9fa031854fcef43c4] (15 operations),
clickmeter.com [23d8e065f3aa6785b5e0] (body `id`), trello.com [2b9f6fbebdeb82370e5f] (`token`; same type, so silent
there, the document fails for other reasons), gsmtasks.com [daf353ec0ddccdfbd4fa] (body `id: Uuid?`),
ix-api.net [a50c3324d8688a759a38] (query `id: List<String>?`), amazonaws.com:apigateway [ea24778326624227187f].
