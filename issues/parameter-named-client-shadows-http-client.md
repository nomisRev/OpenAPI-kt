# A parameter named `client` shadows the generated `HttpClient` property

**Status:** fixed for operation arguments (renderer; `client/parameter-name-collisions`): an operation with an argument named `client` reads the property as `this.client`. Still open: a path parameter `{client}` gives the path class two constructor properties named `client`.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 3 of 1,547 documents (cdcgov.local:prime-data-hub, gsmtasks.com, keycloak.local)
**Source spec valid:** yes — `client` is an ordinary parameter name (OAS 3.0.3 §4.7.12.1)

## Symptom

Two shapes, one cause (the generator owns the bare identifier `client` in every generated class):

1. Operation parameter (query, header, flattened body field) named `client`:
   ```
   api/Reports.kt:31:29: error: none of the following candidates is applicable:      // client.post("reports") on a String
   api/RealmPath.kt:5077:31: error: unresolved reference 'status' on receiver of type 'Char'.   // String.get(Int): Char
   api/ClientRoles.kt:87:28: error: only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'String?'.
   ```
2. Path parameter `{client}`: the path class gets two constructor properties named `client`
   (`conflicting declarations`, `overload resolution ambiguity between candidates: val client: HttpClient / val client: HttpClient`,
   `argument type mismatch: actual type is 'String', but 'HttpClient' was expected`). keycloak.local has 40 such classes;
   112 of its 145 non-`conflicting declarations` errors are this ambiguity.

## Minimal reproducer

`<scratchpad>/repro/parameter-named-client-query.json`:

```json
{
  "openapi": "3.0.3",
  "info": { "title": "client", "version": "1" },
  "paths": {
    "/reports": {
      "get": {
        "parameters": [
          { "name": "client", "in": "query", "required": true, "schema": { "type": "string" } }
        ],
        "responses": { "200": { "description": "ok" } }
      }
    }
  }
}
```

```
compile exit=1
api/Reports.kt:18:33: error: argument type mismatch: actual type is 'String', but 'Int' was expected.
api/Reports.kt:18:44: error: too many arguments for 'fun get(index: Int): Char'.
api/Reports.kt:19:9: error: unresolved reference 'url'.
api/Reports.kt:21:20: error: unresolved reference 'status' on receiver of type 'Char'.
api/Reports.kt:21:65: error: argument type mismatch: actual type is 'Char', but 'HttpResponse' was expected.
```

`<scratchpad>/repro/parameter-named-client-path.json` (same document with path `/clients/{client}` and the parameter `in: path`):

```kotlin
public class Clients internal constructor(
  private val client: HttpClient,
) {
  public fun client(client: String): ClientPath = ClientPath(client, client)

  public class ClientPath internal constructor(
    private val client: HttpClient,
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client, client)
```

```
compile exit=1
api/Clients.kt:12:62: error: argument type mismatch: actual type is 'String', but 'HttpClient' was expected.
api/Clients.kt:15:17: error: conflicting declarations:
api/Clients.kt:18:33: error: overload resolution ambiguity between candidates:
```

Note the second property is typed `HttpClient`, not `String`: KotlinPoet merges the two `client` constructor
parameters with the property of the same name.

## What the spec says

OAS 3.0.3 §4.7.12.1 puts no restriction on `name` beyond matching the path template for `in: path`. `client` is a
common domain word (OAuth clients, customers).

## Root cause

The `HttpClient` property has the fixed name `client` and every reference to it is a bare `%L`/literal:

- declaration: `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt:86` (constructor parameter) and
  `:94-96` (property), followed at `:87-90` / `:99-107` by one parameter and property per accumulated path parameter,
  named `param.name.toCamelCase()` with no check against `client`;
- constructor arguments: `ImplRenderer.kt:1157-1162` (`constructorArgs`) and
  `ClientRendererOperations.kt:101-105` (`operationConstructorArgs`), both `add("client")` then the path parameter names;
- request call: `ImplRenderer.kt:351, 354, 361, 364` (`"val response = client.%L(%L)"`), where an operation parameter
  named `client` is the nearer declaration.

## Correct final API

The caller-facing names stay as the document has them; the collision is resolved on the generator's own identifier.

```kotlin
public class Get internal constructor(
  private val httpClient: HttpClient,          // allocated: not a path parameter name of this node
) {
  public suspend operator fun invoke(client: String) {
    val response = this.httpClient.get("reports") {
      this.url.encodedParameters.append("client", client.encodeURLParameter())
    }
    ...
  }
}

public fun client(client: String): ClientPath = ClientPath(httpClient, client)
```

Wire: `GET reports?client=simple_report`, `GET clients/abc`.

## Fix slice

Renderer only.

- Pick the name of the private `HttpClient` property per path node with a `NameAllocator` seeded with the accumulated
  path parameter names (`client`, else `httpClient`, ...), and thread that name through `constructorArgs`,
  `operationConstructorArgs` and `addRequestInvocation`.
- In operation bodies reference it as `this.<name>` so an operation parameter with the same name cannot shadow it
  (operation classes are plain nested classes; `this` is the operation instance outside the request lambda).
- Open decision: rename the internal property unconditionally to something unlikely (`httpClient`) for stable
  output, or only on collision. `this.`-qualification is needed in both cases.

Overlaps: `query-parameter-named-url-shadows-request-builder.md` (same class of bug for `url`),
`operation-parameter-shadows-path-parameter.md` (operation parameter vs path property).

## Proof

- Failing tests first: `ClientSpec` golden cases for a `client` query parameter and a `{client}` path parameter, plus a
  MockEngine runtime test (the golden text compiles only in the runtime tests) asserting the request URL.
- integration-tests: `github.json` and `youtrack.json` contain no parameter whose camel-cased name is `client`
  (scanned path templates and operation parameters); not covered.

## Affected documents

3 total:

- cdcgov.local:prime-data-hub [0a0deda19c15c1509b2b] — query `client` on `POST /reports`; all 8 errors are this.
- gsmtasks.com [daf353ec0ddccdfbd4fa] — optional query `client` on 21 operations (`/client_roles/`, `/contact_addresses/`, ...);
  also empty-string enum entries and `conflicting declarations`.
- keycloak.local [a07a6d9865686cba0b70] — query `client` on 9 operations and path `{client}` in 40 path classes
  (`/{realm}/clients/{id}/scope-mappings/clients/{client}`); also 160 `conflicting declarations` from other causes.

Spec-level scan of all OpenAPI 3 documents in the census: 4 have a `client` query parameter and 1 a `{client}` path
parameter; the one not listed here, jellyfin.local [38ec17f7a8237fcfe9c2] (query `client`), fails in the typed phase.
