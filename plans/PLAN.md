# Hierarchical Path-Based API Tree Refactor

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

## Phases

- [x] [Phase 1: Remove renderer module](./PHASE_1_PLAN.md)
- [x] [Phase 2: Introduce `PathSegment`](./PHASE_2_PLAN.md)
- [x] [Phase 3: Add `segments` to `Endpoint` and `Route`](./PHASE_3_PLAN.md)
- [ ] [Phase 4: Inline `Endpoint` into `toRoute()`](./PHASE_4_PLAN.md)
- [ ] [Phase 5: Remove `operationId` and update `NamingContext`](./PHASE_5_PLAN.md)
- [ ] [Phase 6: Replace `Root`/`API`/`sort()` with `ApiTree`/`PathNode`/`buildTree()`](./PHASE_6_PLAN.md)
