# Phase 6: Replace `Root`/`API`/`sort()` with `ApiTree`/`PathNode`/`buildTree()`

## Goal
Replace the current tree structure that strips parameter segments with a new tree that preserves them as first-class nodes. Remove `Route.path` (reconstructible from segments). This is the final structural change.

## New data model

### Update `Root.kt` -> rename to `ApiTree.kt`

Replace entire contents with:

```kotlin
package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.parser.Server
import io.ktor.http.HttpMethod

data class ApiTree(
    val name: String,
    val operations: Map<HttpMethod, Route>,  // operations at "/"
    val children: List<PathNode>,
    val servers: List<Server> = emptyList(),
)

data class PathNode(
    val segment: PathSegment,
    val operations: Map<HttpMethod, Route>,
    val children: List<PathNode>,
)
```

### Tree building function

In the same file:

```kotlin
fun Iterable<Route>.buildTree(name: String, servers: List<Server> = emptyList()): ApiTree {
    val rootBuilder = PathNodeBuilder(segment = null)

    for (route in this) {
        rootBuilder.insert(route.segments, route)
    }

    return ApiTree(
        name = name,
        operations = rootBuilder.operations,
        children = rootBuilder.children.map { it.build() },
        servers = servers,
    )
}

private class PathNodeBuilder(
    val segment: PathSegment?,
    val operations: MutableMap<HttpMethod, Route> = mutableMapOf(),
    val children: MutableList<PathNodeBuilder> = mutableListOf(),
) {
    fun insert(segments: List<PathSegment>, route: Route) {
        if (segments.isEmpty()) {
            operations[route.method] = route
            return
        }
        val head = segments.first()
        val tail = segments.drop(1)
        val existing = children.firstOrNull { it.segment.matches(head) }
        if (existing != null) {
            existing.insert(tail, route)
        } else {
            val child = PathNodeBuilder(head)
            children.add(child)
            child.insert(tail, route)
        }
    }

    fun build(): PathNode = PathNode(
        segment = segment!!,  // only root has null segment
        operations = operations.toMap(),
        children = children.map { it.build() },
    )
}

private fun PathSegment?.matches(other: PathSegment): Boolean = when {
    this is PathSegment.Literal && other is PathSegment.Literal -> name == other.name
    this is PathSegment.Parameter && other is PathSegment.Parameter -> name == other.name
    else -> false
}
```

## Changes

### 1. Delete `Root.kt`, create `ApiTree.kt`

Remove `Root`, `API`, `RootBuilder`, `APIBuilder`, `sort()`, `addRoute()`.
Create `ApiTree`, `PathNode`, `buildTree()` as above.

### 2. Remove `Route.path` — `typed/.../routes/Route.kt`

```kotlin
// Before:
data class Route(
    val summary: String?,
    val path: String,
    val segments: List<PathSegment>,
    val method: HttpMethod,
    ...
)

// After:
data class Route(
    val summary: String?,
    val segments: List<PathSegment>,
    val method: HttpMethod,
    ...
)
```

Add a computed property for convenience:

```kotlin
val path: String get() = segments.joinToString(separator = "/", prefix = "/") { segment ->
    when (segment) {
        is PathSegment.Literal -> segment.name
        is PathSegment.Parameter -> "{${segment.name}}"
    }
}
```

### 3. Update `ApiModel` — `typed/.../routes/Route.kt`

```kotlin
class ApiModel(
    val routes: List<Route>,
    val models: List<Model>,
    val servers: List<Server>,
) {
    fun tree(name: String): ApiTree = routes.buildTree(name, servers)
    // ...
}
```

Remove `root()` method and `import io.github.nomisrev.openapi.sort`.

### 4. Update `toRoute()` — `typed/.../routes/Route.kt`

Remove `path = path` from Route construction (it's now computed from segments).

## Tree structure examples

### `/repos/{owner}/{repo}/collaborators` (GET) + `/repos/{owner}/{repo}` (GET, DELETE)

```
ApiTree("GitHub")
├── operations = {}  (nothing at "/")
└── children:
    └── PathNode(Literal("repos"))
        └── PathNode(Parameter("owner", String))
            └── PathNode(Parameter("repo", String), operations={GET: ..., DELETE: ...})
                └── PathNode(Literal("collaborators"), operations={GET: ...})
```

### `/` (GET)

```
ApiTree("Simple")
├── operations = {GET: ...}
└── children: []
```

### `/pets` (GET) + `/pets/{petId}` (GET, DELETE)

```
ApiTree("PetStore")
├── operations = {}
└── children:
    └── PathNode(Literal("pets"), operations={GET: ...})
        └── PathNode(Parameter("petId", String), operations={GET: ..., DELETE: ...})
```

## Tests

### New file: `typed/src/commonTest/kotlin/io/github/nomisrev/ApiTreeSpec.kt`

Test `buildTree()` with:

1. **Empty routes** -> tree with no children, no operations
2. **Root-level operation** (`/` GET) -> operation on ApiTree.operations
3. **Single static path** (`/pets` GET) -> one Literal child with GET operation
4. **Path with parameter** (`/pets/{petId}` GET) -> Literal("pets") -> Parameter("petId") with GET
5. **Shared prefix** (`GET /pets` + `GET /pets/{petId}`) -> Literal("pets") has GET and Parameter child
6. **Multiple methods** (`GET /pets/{petId}` + `DELETE /pets/{petId}`) -> same PathNode with both in operations map
7. **Deep nesting** (`/repos/{owner}/{repo}/collaborators` GET) -> 4 levels deep
8. **Consecutive parameters** (`/{a}/{b}` GET) -> Parameter("a") -> Parameter("b")
9. **Multiple branches** (`/pets` + `/users`) -> two Literal children at root
10. **Real-world-ish** — multiple routes from a realistic API fragment, verify full tree shape

### Test `Route.path` computed property
Verify that `route.path` reconstructs correctly from segments for various paths.

## Verification
- No references to `Root`, `API`, `sort()` in typed module
- `Route.path` is a computed property, not a stored field
- Project compiles: `./gradlew :typed:build`
- All tests pass: `./gradlew :typed:allTests`
- Tree correctly preserves parameter segment positions
