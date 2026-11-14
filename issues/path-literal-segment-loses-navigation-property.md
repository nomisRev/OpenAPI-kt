# A literal path segment named `client`, or like a path parameter above it, silently loses its navigation property

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** wrong output (the generated code compiles; operations are unreachable)
**Impact:** 13 of 1,547 documents have a literal segment `client`; 2 of them reach emit and both lose operations (amazonaws.com:sso-oidc, authentiq.io). 1 document has a literal segment named like a path parameter above it (keycloak.local). No compile error points at this, so the census does not count it.
**Source spec valid:** yes — a literal path segment may be any text.

## Symptom

For `/client/register` the root class has no `client` property. The class `Client` and its operations are generated, but its constructor is `internal` and nothing returns an instance, so `POST /client/register` can't be called. No diagnostic is reported and the code compiles.

From the census output of amazonaws.com:sso-oidc (paths `/token`, `/client/register`, `/device_authorization`), the root class has:

```kotlin
public val token: Token = Token(client)
public val deviceAuthorization: DeviceAuthorization = DeviceAuthorization(client)
```

and no member for `/client`.

This is the silent sibling of two compile-time reports: `issues/parameter-named-client-shadows-http-client.md` (a parameter or `{client}` path parameter) and `issues/operation-parameter-shadows-path-parameter.md`.

## Minimal reproducer

`<scratchpad>/repro/path-literal-named-client.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Clients", "version": "1" },
  "paths": {
    "/realms/client": { "get": { "responses": { "204": { "description": "ok" } } } }
  }
}
```

Observed: all phases pass, no diagnostics, `compile exit=0`.

```kotlin
public class Realms internal constructor(
  private val client: HttpClient,
) {
  public class Client internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(private val client: HttpClient) {
      public suspend operator fun invoke() {
        val response = client.get("realms/client")
```

`Realms` has no `val client: Client`.

Second shape, `<scratchpad>/repro/literal-segment-named-like-ancestor-parameter.json` (`GET /{realm}/roles/realm`, path parameter `realm`): `compile exit=0`, and

```kotlin
public class Roles internal constructor(
  private val client: HttpClient,
  private val realm: String,
) {
  public class Realm internal constructor(private val client: HttpClient, private val realm: String) {
    public val `get`: Get = Get(client, realm)
```

`Roles` has no `val realm: Realm`.

## What the spec says

OAS 3.0.3 §4.7.8 Paths Object: a path is a relative URL template; literal segments have no reserved words.

## Root cause

Traced up to KotlinPoet.

- `addClientConstructorAndState` in `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt:75-109` gives every path class a constructor parameter and private property `client`, and one per path parameter above it named `param.name.toCamelCase()`.
- `addConcreteNavigationProperty` (`ImplRenderer.kt:144-155`) adds the navigation `PropertySpec.builder(memberName, childClassName)` to the same `TypeSpec.Builder`. For the literal `client` the member name is `client`; for the literal `realm` it is `realm`.
- `uniqueNames()` (`renderer/.../PathParamUtils.kt:47-67`) makes navigation names unique among sibling segments only. `client` and the stored path parameter names are not reserved.

So the generator hands KotlinPoet two properties with one name, the first of which is a constructor property. The printed class contains only the constructor property. Why KotlinPoet omits the second instead of printing a duplicate is inferred from the output (it keys constructor properties by name); I did not read its source. Either way nothing in the renderer notices.

At the root, the same applies to the root client class (`toRootTypeSpec`, `ClientRenderer.kt:63-65`), which is the sso-oidc case.

## Correct final API

Every operation is reachable under the name the spec gives it:

```kotlin
public class Realms internal constructor(private val client: HttpClient) {
  public val client: Client = Client(...)      // api.realms.client.get()
```

That needs the internal `HttpClient` property (and stored path parameters) to stop occupying spec-derived names. Wire behaviour: `api.realms.client.get()` sends `GET /realms/client`; `api.realm("r").roles.realm.get()` sends `GET /r/roles/realm`.

## Fix slice

- Preferred: rename the private state so no spec name can equal it, for example `_client` and `_realm`. `toParamName()` trims `_` and `toCamelCase()` drops it as a word boundary, so neither can produce a leading underscore. The state is `private`, so no public API changes. This is one mechanism for this report and for the two shadowing reports named above; check what those propose (`this.` qualification) and pick one for all three. `this.` qualification alone does not help here, because the problem is two members with one name.
- Alternative: reserve `client` and the accumulated parameter names in `uniqueNames()` and rename the navigation member (`client2`) with the existing "is generated as" warning. Correct, but `api.realms.client2` is worse than `api.realms.client`.
- Add a `check` when building a path class that no two properties share a name. It would have turned this silent loss into a failure, and it guards the next unreserved name.

## Proof

- Failing test first: this needs a runtime test, since the golden output compiles. A MockEngine case in `PathSegmentCollisionRuntimeTest` that calls `api.realms.client.get()` and asserts `GET /realms/client`; the test does not compile today, which is the proof.
- A general guard for the census: after rendering, assert that every `Route` in the `ApiTree` is reachable from the root class through public members. A compile-only census can't see this class of bug.
- Integration tests would not catch a regression: `github.json`, `openai.yaml` and `youtrack.json` have no literal path segment whose Kotlin name is `client` or a path parameter above it (checked by script).

## Affected documents

Literal segment `client` (13): amazonaws.com:sso-oidc [baa3a9c6ed3f0039e9c5] and authentiq.io [c41d28a88d28eb819b75] reach emit, both confirmed in the emitted sources. These fail before emit: cpy.re:peertube [2f4c975ba6843b78308f]; vimeo.com [315034d21e1f8b798d05]; threatjammer.com [3b6cf8ab04117a4e2ad3]; envoice.in [524930cb24b29fbfde7b]; mist.com [5d2a9c76b41d4671c72b]; motaword.com [7ab666d20e8d3f3df9ea]; vtex.local:Master-Data-API- [94cbdfdffe675e025517]; gambitcomm.local:mimic [c3edba593cb89c99b4a9]; and 3 more.

Literal segment named like a path parameter above it (1): keycloak.local [a07a6d9865686cba0b70] (`/{realm}/users/{id}/role-mappings/realm/composite`), by spec shape; its output has other errors.
