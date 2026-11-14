# A path segment named `get`/`post`/… collides with the operation of the parent path

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 3 of 1,547 documents fail to compile (amazonaws.com:securityhub, amazonaws.com:guardduty, tokenjay.app). 5 documents have the construct; bigoven.com and atlassian.com:jira fail before compilation.
**Source spec valid:** yes — `/members` and `/members/get` are two distinct paths.

## Symptom

```
api/Members.kt:37:14: error: conflicting declarations:
val get: Members.Get
  public val `get`: Get = Get(client)
api/Members.kt:49:16: error: conflicting declarations:
class Get : Any
```

## Minimal reproducer

`<scratchpad>/repro/path-segment-named-like-method.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "Segments", "version": "1" },
  "paths": {
    "/members": { "get": { "responses": { "204": { "description": "ok" } } } },
    "/members/get": { "post": { "responses": { "204": { "description": "ok" } } } }
  }
}
```

Observed: all phases pass, no diagnostics, `compile exit=1`.

```kotlin
public class Members internal constructor(private val client: HttpClient) {
  public val `get`: Get = Get(client)     // GET /members

  public val `get`: Get = Get(client)     // navigation to /members/get

  public class Get internal constructor(private val client: HttpClient) {
    public suspend operator fun invoke() { val response = client.get("members") ... }
  }

  public class Get internal constructor(private val client: HttpClient) {
    public val post: Post = Post(client)
    public class Post ...
  }
}
```

```
api/Members.kt:11:14: error: conflicting declarations:
api/Members.kt:13:14: error: conflicting declarations:
api/Members.kt:15:16: error: conflicting declarations:
api/Members.kt:24:16: error: conflicting declarations:
```

## What the spec says

OAS 3.0.3 §4.7.8 Paths Object: each key is a relative path; only templated paths with the same hierarchy are considered identical. A literal segment may be any text, including an HTTP method name. AWS uses `/members/get` (a POST that fetches members) next to `GET /members`.

## Root cause

Traced. A path node's class holds two kinds of members:

- one property and nested class per operation, named after the method: `PathNode.toTypeSpec` in `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRenderer.kt:139-150` (and `toRootTypeSpec`, lines 89-100);
- one navigation member and nested class per child segment: `ClientRenderer.kt:152-164`, with names from `children.uniqueNames()`.

`uniqueNames()` (`renderer/.../PathParamUtils.kt:47-67`) makes the child names unique among the siblings only. It doesn't know the node's own operations, so a child literal `get` gets member `get` and class `Get`, the same as the GET operation. The collision needs both: a child segment named like a method, and that method on the parent node. `/members/get` under a parent without GET compiles today.

The method set to reserve is whatever `operations.keys` holds: get, put, post, delete, options, head, patch, trace, and `query` on 3.2 documents.

## Correct final API

The operation keeps its name; the child segment is renamed by the rule `uniqueNames()` already applies to colliding siblings (suffix from 2 on both member and class, with the existing "is generated as" warning from `renamedPathDiagnostics`, `GenerationDiagnostics.kt:66-77`):

```kotlin
public class Members internal constructor(private val client: HttpClient) {
  public val `get`: Get = Get(client)       // GET /members:        api.members.get()
  public val get2: Get2 = Get2(client)      // /members/get:        api.members.get2.post()
  public class Get ...
  public class Get2 { public val post: Post ... }
}
```

Wire behaviour: unchanged, `api.members.get2.post()` sends `POST /members/get`.

## Fix slice

- Renderer: give `uniqueNames()` the names reserved by the parent node. Pass `operations.keys` (lower-cased for members, capitalised for classes) and seed `seenClasses` / `seenMembers` with them. Three call sites: `ClientRenderer.kt:32,102,152`, plus `GenerationDiagnostics.kt:67` so the warning matches.
- Reserve the method names only when the parent actually has that operation, or always? Always reserving makes names independent of which operations a path has (adding `GET /members` later would not rename `/members/get`), at the cost of renaming segments that don't collide today. Open decision; reserving only on a real collision is the smaller change and matches how sibling collisions behave.
- Open decision: a more readable name than `get2`, for example `getPath` / `GetPath`. `…Path` is already the suffix of parameter segments (`IdPath`), so it could be confused with a `{get}` parameter.
- The same reservation list should hold the class's other members: `client` and stored path parameters, see `issues/path-literal-segment-loses-navigation-property.md` and `issues/parameter-named-client-shadows-http-client.md`.

## Proof

- Failing test first: a `ClientSpec` golden case with the reproducer; a case in `PathSegmentCollisionRuntimeTest` (it already covers sibling collisions) that calls both `api.members.get()` and `api.members.get2.post()` against MockEngine and asserts the two request lines.
- Integration tests would not catch a regression: no path in `github.json`, `openai.yaml` or `youtrack.json` has a literal segment named like a method its parent path declares (checked by script).

## Affected documents

Observed: amazonaws.com:securityhub [adf992c1542172fe8bbd] (`/members/get`, `/standards/get`); amazonaws.com:guardduty [be5414ce9c16d815c3f5] (`/detector/{detectorId}/member/get`); tokenjay.app [e7e963a7e2524a52eb01] (`/mosaik/tokenburn/get/{uuid}`).

Construct present, not reached: bigoven.com [286dcebaa8930ce6d65b] (`/recipe/post/step`); atlassian.com:jira [ae2b609d3d04e763ffcb] (`/rest/api/3/project/{projectIdOrKey}/delete`).
