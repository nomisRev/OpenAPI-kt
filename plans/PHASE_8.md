# Phase 8 — Client: Interface Tree + Navigation

Generate the client interface hierarchy from `ApiTree`, mirroring the URL path structure.

## Tasks

- [x] Implement `ApiTree.generateClient(config: RenderConfig): List<FileSpec>`:
  - Root interface named `ApiTree.name` (user-provided, e.g., `"GitHub"`)
  - `val` properties for each `PathNode` child with static segment
  - Navigation functions for each `PathNode` child with parameter segment
  - Suspend functions for `ApiTree.operations` (root-level operations)
- [x] Implement `PathNode.toTypeSpec(config: RenderConfig, parentName: ClassName)`:
  - Nested interface inside parent
  - Recursive: process `children` the same way
  - Static segment children → `val childName: ChildInterface`
  - Parameter segment children → `fun paramName(paramName: Type): ParamInterface`
  - Operations on this node → suspend functions (stubs for now, Phase 9 fills in signatures)
- [x] Interface naming:
  - Static segments: `name.toPascalCase()` (e.g., `repos` → `Repos`)
  - Parameter segments: `name.toPascalCase()` (e.g., `{owner}` → `Owner`, `{pet_id}` → `PetId`)
  - Direct children are top-level interfaces; deeper nesting is nested inside parent
- [x] Navigation function rendering:
  - Non-suspend function
  - Function name: `segment.name.toCamelCase()` (e.g., `owner`, `petId`)
  - Parameter name: same as function name
  - Parameter type: from `PathSegment.Parameter.type.toTypeName(config)`
  - Return type: the child interface
- [x] File organization:
  - Root file (`{Name}.kt`): root interface
  - Direct children get their own files: `Repos.kt`, `Users.kt`
  - Deeper nesting stays inside the direct child file

## Golden Tests

- [x] `client/simple-tree` — flat structure: `/pets`, `/users` → root with two `val` properties
- [x] `client/nested-tree` — `/repos/{owner}/{repo}` → nested interfaces with navigation functions
- [x] `client/mixed-segments` — mix of static and parameter segments
- [x] `client/root-operations` — operations at `/` (root level)
- [x] `client/deep-nesting` — deeply nested path: `/a/b/{c}/d/{e}/f`

## Files to Create/Modify

- **Create**: `renderer/.../ClientRenderer.kt` — `ApiTree.generateClient()`, `PathNode.toTypeSpec()`
- **Modify**: `renderer/.../Generate.kt` — wire client generation
- **Create**: golden test resource files under `renderer/src/test/resources/kotlinTestData/client/`
- **Create**: `renderer/.../ClientSpec.kt` — test suite
- **Modify**: `renderer/.../TestBalloonDsl.kt` — add `clientTest()` helper that creates a spec with paths

## Key Decisions

- Interface tree mirrors URL structure exactly — no flattening or grouping
- Parameter navigation functions are NOT suspend — they just create the next step
- Direct children are top-level interfaces; deeper nesting is nested inside parent
- File splitting: root + one file per direct child segment (deeper nesting stays in the child file)
