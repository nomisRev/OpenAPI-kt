# ISSUE: Plain (Non-Suffixed) Model References a `*Read`-Suffixed Nested Type

## Problem

`IssueKey`, a non-suffixed (context-neutral) model, contains a property whose type resolves to
`IssueFolderRead.Project` — a nested class inside the Read-suffixed sealed hierarchy:

```kotlin
// Generated IssueKey.kt
@Serializable
public data class IssueKey(
  public val id: String? = null,
  public val project: IssueFolderRead.Project? = null,   // ← Read-suffixed type leaked in
  public val numberInProject: Long? = null,
  @SerialName("${'$'}type") public val type: String? = null,
)
```

`IssueKey` itself is not context-specific: it was not assigned a Read or Write suffix because
it is only referenced in a single context (or in a context where no suffix is applied). Yet its
`project` field has resolved to the Read variant of the `IssueFolder` sealed hierarchy.

## Why This Matters

The inconsistency creates confusion and potential misuse:

- **Misleading API** — callers working with `IssueKey` in a Write context will find that the
  `project` property already carries the `Read` type, implying they must supply a Read object
  (complete with `id`, `readOnly` fields, etc.) when writing.
- **Fragile reference** — if `IssueFolderRead` is later renamed or the sealed hierarchy changes,
  `IssueKey` breaks without any indication that it was bound to the Read side.
- **Principle of least surprise** — a non-suffixed model should reference non-suffixed types or,
  if referencing a suffixed type is unavoidable, reference the Write variant for a writable field.

The root cause is likely in the type-resolution logic for sealed-hierarchy nested types: when
`IssueFolderRead.Project` and `IssueFolderWrite.Project` both exist, the resolver picks the Read
variant for the plain `IssueKey`, possibly because it encounters the Read sealed interface first
or because the `$ref` in the spec resolves to the component schema which is mapped to the Read
variant by default.

## Current Behavior

```kotlin
public data class IssueKey(
  ...
  public val project: IssueFolderRead.Project? = null,
  ...
)
```

## Desired Outcome

The `project` field in `IssueKey` should reference a context-appropriate type. Since `IssueKey`
is a non-suffixed model (plain / Write context), the reference should be to the plain or Write
variant:

```kotlin
public data class IssueKey(
  ...
  public val project: IssueFolderWrite.Project? = null,
  ...
)
```

Or, if `IssueFolder.Project` itself has no Read/Write split (i.e. no properties are stripped),
the reference should be to the non-suffixed `IssueFolder.Project`.

## Affected Module

- `typed` — the `NamingContext.Reference` resolution inside `Registry.kt` or wherever nested
  sealed-type names are resolved for plain / Write model variants
- `renderer` — `TypeMapping.kt` (`toClassName()`) if the suffix is being applied at render time
  based on the enclosing context

## Acceptance Criteria

1. A non-suffixed model that references a nested type from a suffixed sealed hierarchy resolves
   to the correct context variant (Write for a plain/Write model, Read for a Read model).
2. `IssueKey.project` no longer references `IssueFolderRead.Project`.
3. `./gradlew :renderer:jvmTest` passes.
4. `./gradlew :youtrack:generateOpenApi :youtrack:compileKotlin` passes in the integration tests.
