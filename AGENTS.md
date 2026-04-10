DO NOT CHAT WITH ME! JUST CALL TOOLS! DO NOT EXPLAIN YOURSELF!

# Repository Guidelines
- Keep changes focused on the task at hand. Do not refactor unrelated modules while touching shared code.

## Project Layout
- `parser/`: Kotlin Multiplatform OpenAPI parsing and model decoding.
- `typed/`: Kotlin Multiplatform typed client-facing model layer built on top of `parser`.
- `renderer/`: JVM-only code generation and rendering utilities.
- `gradle-plugin/`: Gradle plugin that wires the renderer into builds.
- `plans/done/`: Archived implementation plans; use for historical context only.
- Ignore generated output under any `build/` directory unless the task is explicitly about generated artifacts.

## Coding Expectations
- Follow the existing Kotlin style; `kotlin.code.style=official` is enabled.
- Treat compiler warnings seriously; modules are configured with strict settings and often fail on warnings.
- Match existing source set boundaries (`commonMain`, `commonTest`, `jvmMain`, `jvmTest`, `main`) instead of moving code across platforms casually.
- Prefer minimal, composable changes over broad rewrites.

## Working Rules
- Read the relevant module build file before changing module-specific build logic.
- When changing public model or rendering behavior, update or add focused tests in the same module.
- If repo-level planning docs are referenced but missing, do not recreate them unless the task explicitly asks for it.
