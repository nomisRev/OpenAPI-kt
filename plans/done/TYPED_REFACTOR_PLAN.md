# Hierarchical Path-Based API Tree Refactor (COMPLETED)

Refactor the typed module to produce a path-segment-aware API tree where static segments become property accesses and parameter segments become navigation functions, with HTTP methods as operation names.

**Target API style:**
```kotlin
client.repos.owner(owner).repo(repo).collaborators.get()
```

## Design Decisions

| # | Decision | Choice |
|---|----------|--------|
| 1 | Operation function naming | HTTP method (`get()`, `post()`, etc.) |
| 2 | Parameter segments | Always navigation functions |
| 3 | Navigation function naming | Param name directly: `owner(owner: String)` |
| 4 | Interface naming | Nested classes — renderer concern |
| 5 | `operationId` | Remove entirely from typed module |
| 6 | `PathSegment.Parameter` | Carries `Model` type |
| 7 | `Endpoint` class | Inline into `toRoute()` |
| 8 | NamingContext nested types | `RouteParam(name)`, `data object RouteBody`, `data object Response` |
| 9 | `Route.path` | Replace with `segments: List<PathSegment>` |
| 10 | `PathNode.operations` | `Map<HttpMethod, Route>` |
| 11 | Root `/` operations | On `ApiTree.operations` |
| 12 | `Route.parameters` | Keep all params, renderer filters path vs non-path |
| 13 | `NamingContext.Head.Path` | `Path(segments: List<PathSegment>, method: HttpMethod)` |
