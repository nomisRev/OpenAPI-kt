# ISSUE: ApiTree.collectHttpMethods() Does Not Walk Children

## Problem

`ClientRenderer.kt` calls `ApiTree.collectHttpMethods()` to determine which Ktor HTTP-method
extension functions to import into the generated root file:

```kotlin
for (method in collectHttpMethods()) {
    rootFileBuilder.addImport("io.ktor.client.request", method)
}
```

The `ApiTree` implementation of `collectHttpMethods()` only inspects the root-level `operations`
map:

```kotlin
internal fun ApiTree.collectHttpMethods(): Set<String> =
    operations.keys.mapTo(mutableSetOf()) { it.value.lowercase() }
```

The `PathNode` implementation correctly recurses through children:

```kotlin
internal fun PathNode.collectHttpMethods(): Set<String> {
    val methods = operations.keys.mapTo(mutableSetOf()) { it.value.lowercase() }
    for (child in children) {
        methods.addAll(child.collectHttpMethods())
    }
    return methods
}
```

The root file is generated separately from the child files. Child `PathNode`s are emitted as
individual `FileSpec` objects and get their own import loop:

```kotlin
for (method in child.collectHttpMethods()) {
    fileBuilder.addImport("io.ktor.client.request", method)
}
```

However, the root `FileSpec` currently only adds HTTP-method imports for operations that sit
directly at the tree root. Any API where all operations are nested under at least one path segment
(the common case) will produce a root file with no HTTP-method imports at all, or a partial set if
some root-level operations exist alongside nested ones.

## Impact

The generated root `GitHub.kt` (or equivalent) will be missing `import io.ktor.client.request.get`
and similar imports for every HTTP method that does not appear in a root-level operation. This
causes a compile error in the generated code if the root file references those extension functions.

In practice the current generated code places invocations of HTTP extension functions only inside
leaf operation classes, which are nested types. The Kotlin compiler resolves their imports from the
file that declares them (i.e., the child `FileSpec`), so the bug is currently latent — it manifests
only if a root-level operation uses an HTTP method that no child file also uses, or if the import
strategy for the root file changes.

## Fix Applied

`ApiTree.collectHttpMethods()` was updated to mirror `PathNode.collectHttpMethods()` and recurse
into all children:

```kotlin
internal fun ApiTree.collectHttpMethods(): Set<String> {
    val methods = operations.keys.mapTo(mutableSetOf()) { it.value.lowercase() }
    for (child in children) {
        methods.addAll(child.collectHttpMethods())
    }
    return methods
}
```

## Affected Files

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt` — bug and fix

## Acceptance Criteria

1. `ApiTree.collectHttpMethods()` returns the union of HTTP methods from the root and all
   descendant nodes, matching the behaviour of `PathNode.collectHttpMethods()`.
2. The generated root file contains import statements for every HTTP method used anywhere in the
   tree.
3. `./gradlew :renderer:jvmTest` passes.
