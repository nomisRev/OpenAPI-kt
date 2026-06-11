# Repository Guidelines
- Keep changes focused on the task at hand. Do not refactor unrelated modules while touching shared code.

## Project Layout
- `parser/`: Kotlin Multiplatform OpenAPI parsing and model decoding.
- `typed/`: Kotlin Multiplatform typed client-facing model layer built on top of `parser`.
- `renderer/`: JVM-only code generation and rendering utilities.
- `gradle-plugin/`: Gradle plugin that wires the renderer into builds.
- Ignore generated output under any `build/` directory unless the task is explicitly about generated artifacts.
