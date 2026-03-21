# ISSUE: Flatten Request Bodies Into Function Parameters

## Problem

The generated API forces callers to construct intermediate `Body` wrapper classes and union case wrappers for every request that has a body.
This adds unnecessary ceremony, especially when a body has a single required field with several optionals, or when a union field has only primitive cases.

### Current Behavior

```kotlin
// Issues POST — title is required, everything else optional
repos.owner("ktorio").repo("ktor").issues.post(
    Issues.Post.Body(
        title = Issues.Post.Body.Title.CaseString("My new title!")
    )
)

// Gist comment POST — single String field wrapped in value class
gists.gistId("abc").comments.post(
    Gists.GistIdPath.Comments.Post.Body("Great gist!")
)

// Markdown POST — required `text` + 2 optionals
markdown.post(
    Markdown.Post.Body(text = "# Hello")
)
```

### Desired Behavior

```kotlin
// Issues POST — overload per union case, all body fields flattened
repos.owner("ktorio").repo("ktor").issues.post(title = "My new title!")
repos.owner("ktorio").repo("ktor").issues.post(title = 42L)

// Gist comment POST — direct String parameter
gists.gistId("abc").comments.post(body = "Great gist!")

// Markdown POST — named params with defaults
markdown.post(text = "# Hello")
markdown.post(text = "# Hello", mode = Mode.Gfm, context = "owner/repo")
```

---

## Scope

This plan covers **request body flattening only** — promoting Body class fields into function parameters and eliminating union case wrappers for primitive unions within bodies.

**In scope:**
- Flattening Body data class fields into `invoke()` parameters
- Generating overloads when a body field is a primitive union (e.g., `String | Long`)
- Removing generated `Body` wrapper classes (the nested class no longer appears in the public API)
- Removing generated union sealed interfaces for primitive body field unions

**Out of scope:**
- Path/query/header parameter flattening (covered by `ISSUE_FLATTEN_PRIMITIVE_INPUT.md`)
- Response type changes (covered by `ISSUE_RESPONSE_TYPE.md`)
- Non-body union types in models (those remain sealed interfaces)

---

## Real GitHub API Examples

### Example 1: Single Required Field + Many Optionals

**Endpoint:** `POST /repos/{owner}/{repo}/issues` (Issues.Post)

**OpenAPI body schema:**
```json
{
  "required": ["title"],
  "properties": {
    "title": { "oneOf": [{ "type": "string" }, { "type": "integer" }] },
    "body": { "type": "string" },
    "assignee": { "type": "string" },
    "milestone": { "oneOf": [{ "type": "string" }, { "type": "integer" }] },
    "labels": { "type": "array", "items": { "oneOf": [...] } },
    "assignees": { "type": "array", "items": { "type": "string" } },
    "type": { "type": "string" }
  }
}
```

**Current generated code (ClientRenderer.kt):**
```kotlin
public suspend operator fun invoke(body: Body): Response { ... }

@Serializable
public data class Body(
    val title: Title,                          // sealed: CaseString | CaseLong
    val body: String? = null,
    val assignee: String? = null,
    val milestone: Milestone? = null,          // sealed: CaseString | CaseLong
    val labels: List<Labels>? = null,          // sealed: CaseString | CaseObject
    val assignees: List<String>? = null,
    val type: String? = null,
)
```

**Desired generated code:**

```kotlin
// Overload 1: title as String, milestone as String
public suspend operator fun invoke(
    title: String,
    body: String? = null,
    assignee: String? = null,
    milestone: String? = null,
    labels: List<Labels>? = null,
    assignees: List<String>? = null,
    type: String? = null,
): Response

// Overload 2: title as Long, milestone as String
public suspend operator fun invoke(
    title: Long,
    body: String? = null,
    assignee: String? = null,
    milestone: String? = null,
    labels: List<Labels>? = null,
    assignees: List<String>? = null,
    type: String? = null,
): Response

// ... more overloads for milestone: Long combinations
```

The `Body` data class is still generated internally for serialization but is no longer part of the public API.

### Example 2: Single-Field Body (Value Class)

**Endpoint:** `POST /gists/{gist_id}/comments` (Gists.GistIdPath.Comments.Post)

**Current:**
```kotlin
public suspend operator fun invoke(body: Body): Response { ... }

@JvmInline
@Serializable
public value class Body(public val body: String)
```

**Desired:**
```kotlin
public suspend operator fun invoke(body: String): Response {
    val response = client.post("/gists/$gistId/comments") {
        contentType(ContentType.Application.Json)
        setBody(Body(body))          // Internal Body still used for serialization
    }
    ...
}
```

### Example 3: Multi-Field Body, No Unions

**Endpoint:** `POST /markdown` (Markdown.Post)

**Current:**
```kotlin
public suspend operator fun invoke(body: Body): Response { ... }

@Serializable
public data class Body(
    val text: String,
    val mode: Mode? = null,
    val context: String? = null,
)
```

**Desired:**
```kotlin
public suspend operator fun invoke(
    text: String,
    mode: Mode? = null,
    context: String? = null,
): Response {
    val response = client.post("/markdown") {
        contentType(ContentType.Application.Json)
        setBody(Body(text, mode, context))   // Internal Body still used for serialization
    }
    ...
}
```

### Example 4: All-Optional Body (PATCH)

**Endpoint:** `PATCH /repos/{owner}/{repo}` (Repos.OwnerPath.RepoPath.Patch)

Body has ~26 optional fields (name, description, homepage, private, visibility, ...).

**Desired:**
```kotlin
public suspend operator fun invoke(
    name: String? = null,
    description: String? = null,
    homepage: String? = null,
    private: Boolean? = null,
    visibility: Visibility? = null,
    // ... all 26 fields with = null defaults
): Response
```

All parameters are optional so the caller uses only named arguments for the fields they want to change:
```kotlin
repos.owner("o").repo("r").patch(description = "Updated!", hasWiki = false)
```

---

## Overload Strategy for Primitive Unions

When a body field is a union of **flattenable primitive types** (scalar types that can be promoted to a direct Kotlin type), generate one overload per combination of union cases for **required** union fields only.

### Flattenable Types

Same as `ISSUE_FLATTEN_PRIMITIVE_INPUT.md`: `String`, `Int`, `Long`, `Float`, `Double`, `Boolean`, `Instant`, `LocalDate`, `UUID`, `ByteArray`.

### Overload Rules

1. **Required primitive union fields** produce overloads — one per case.
2. **Optional primitive union fields** are NOT overloaded — they keep their union type with `? = null` default. Callers construct the union case explicitly only when providing a value.
3. **Non-primitive union fields** (containing objects, collections of objects, enums) are NOT flattened — the sealed interface remains.
4. **Multiple required union fields** produce the cartesian product of overloads, but ONLY if the total overload count is ≤ 4. Beyond 4, keep the `Body` class.

### Overload Count Example

`Issues.Post.Body`:
- `title`: required, union `String | Long` → 2 cases
- `milestone`: optional → not overloaded
- `labels`: optional, contains object union → not overloaded
- **Result: 2 overloads** (one with `title: String`, one with `title: Long`)

If a body had two required fields each with 2 primitive union cases → 4 overloads. If 3 such fields → 8, exceeds the cap → keep `Body` class.

### Handling Optional Unions

For optional union fields like `milestone: Milestone? = null`, the caller still needs to wrap:

```kotlin
issues.post(
    title = "My issue",
    milestone = Issues.Post.Milestone.CaseString("v1.0")
)
```

This is acceptable because:
1. Optional fields are rarely provided
2. Overloading on optional parameters creates ambiguity
3. The ceremony is proportional to the complexity the caller chose to use

---

## Serialization Strategy

The internal `Body` class remains generated for JSON serialization. The flattened `invoke()` function constructs the `Body` internally:

```kotlin
// Generated internal Body (not public)
@Serializable
internal data class Body(
    val title: Title,  // sealed union still needed for serialization
    val body: String? = null,
    ...
)

// Generated public overload
public suspend operator fun invoke(
    title: String,
    body: String? = null,
    ...
): Response {
    val response = client.post("/repos/$owner/$repo/issues") {
        contentType(ContentType.Application.Json)
        setBody(Body(
            title = Title.CaseString(title),  // Wrap for serialization
            body = body,
            ...
        ))
    }
    return when (response.status.value) { ... }
}
```

For single-field bodies where the field is not a union, the Body class can be replaced with the direct serializer:

```kotlin
// Gist comment — single String body wraps into {"body": "..."}
public suspend operator fun invoke(body: String): Response {
    val response = client.post("/gists/$gistId/comments") {
        contentType(ContentType.Application.Json)
        setBody(Body(body))           // Body remains internal for wire format
    }
    ...
}
```

---

## Implementation Approach

### Files to Modify

| File | Change |
|------|--------|
| `renderer/.../ClientRenderer.kt` | Modify `Route.toInvokeFunSpec()` and `Route.Bodies.toInvokeParameterSpecs()` to flatten body fields into invoke params. Add overload generation for required primitive unions. |
| `renderer/.../ClientRenderer.kt` | Modify `Route.Bodies.inlineBodyTypeSpec()` to make the Body class `internal` instead of `public` when flattening is active. |
| `renderer/.../UnionRenderer.kt` | No changes needed — union types for internal serialization remain. Public union types for non-flattenable optional fields remain. |
| `renderer/.../ObjectRenderer.kt` | Ensure Body classes that become internal still render correctly for serialization. |

### Key Functions to Change

1. **`Route.Bodies.toInvokeParameterSpecs()`** (ClientRenderer.kt:583-641)
   - Currently returns a single `body: BodyType` parameter
   - Change to: iterate Body object properties, emit each as a separate `ParameterSpec`
   - For required primitive union fields: return multiple `List<ParameterSpec>` representing overloads

2. **`Route.toInvokeFunSpec()`** (ClientRenderer.kt:388-437)
   - Currently generates one `invoke()` function
   - Change to: generate N functions when overloads are needed
   - Each overload constructs the internal `Body` with the appropriate union wrapping

3. **New helper: `Body.flattenedParameterSets()`**
   - Computes the set of overloaded parameter lists from the Body model
   - Returns `List<List<ParameterSpec>>` — one list per overload
   - Applies the overload cap (≤ 4)

4. **Body class visibility**
   - When flattening is active: `Body` becomes `internal` (or `private` to the enclosing class)
   - Union sealed interfaces for optional fields remain `public`
   - Union sealed interfaces for required primitive fields become `internal`

### Overload Deduplication

The existing `distinctCaseSignatures()` (ClientRenderer.kt:882-906) already handles JVM overload deduplication with `@JvmName`. Extend this to the new flattened overloads:

- If two overloads have the same JVM erasure (unlikely with primitives, but possible with generics), add `@JvmName` annotations.
- Kotlin can distinguish `invoke(title: String, ...)` from `invoke(title: Long, ...)` at the call site via named arguments and type inference.

---

## Acceptance Criteria

1. **Single-field bodies** are flattened: `gists.comments.post(body = "text")` compiles.
2. **Multi-field bodies** are flattened: `markdown.post(text = "# Hi")` compiles.
3. **Required primitive union fields** produce overloads: `issues.post(title = "str")` and `issues.post(title = 42L)` both compile.
4. **Optional union fields** keep their sealed interface type: `issues.post(title = "t", milestone = Milestone.CaseString("v1"))`.
5. **Non-primitive unions** (object cases) are not flattened: sealed interface remains for labels.
6. **Large bodies (PATCH)** flatten correctly: all 26 optional fields become function parameters with `= null`.
7. **Overload cap**: bodies with >4 required union combinations keep the `Body` class.
8. **Serialization correctness**: JSON payloads are identical before and after flattening.
9. **Body class** is `internal` — not part of the public API when flattened.
10. **Existing tests pass** and new tests cover the flattened signatures.

---

## Dependencies

- None. This plan is independent of `ISSUE_RESPONSE_TYPE.md` and `ISSUE_MEDIA_TYPE.md`.
- Complementary to `ISSUE_FLATTEN_PRIMITIVE_INPUT.md` (which handles path/query/header params).

---

## Migration / Compatibility

This is a **breaking API change** for generated clients. The `Body` class moves from public to internal, and function signatures change. Since the generated code is output of this tool (not handwritten), this is acceptable — users regenerate their client when upgrading the plugin.
