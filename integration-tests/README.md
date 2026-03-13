# Integration Tests

This directory contains integration tests for large OpenAPI specifications that cannot be tested with in-process `KotlinCompilation` due to memory constraints.

## Modules

| Module | Spec | Description |
|--------|------|-------------|
| `github` | `test-specs/github.json` | GitHub REST API (~600+ schemas) |
| `youtrack` | `test-specs/youtrack.json` | JetBrains YouTrack API (~400+ schemas) |
| `openai` | `test-specs/openai.yaml` | OpenAI API (~200+ schemas) |

## How It Works

1. Each module has a `generateClient` task that invokes the code generator
2. Generated code is written to `src/main/kotlin/`
3. Standard Gradle Kotlin compilation compiles the generated code
4. Gradle handles memory management, caching, and incremental builds

## Running Integration Tests

```bash
# Generate and compile all specs
./gradlew :integration-tests:compileAllClients

# Generate and compile a single spec
./gradlew :integration-tests:github:compileKotlin

# Just generate code (without compiling)
./gradlew :integration-tests:github:generateClient
```

## Adding New Specs

1. Create a new directory under `integration-tests/`
2. Copy `build.gradle.kts` from an existing module
3. Update `specFile`, `clientName`, and format settings
4. Add the module to `settings.gradle.kts`

## Generated Code Strategy

By default, generated code is gitignored and regenerated on each build. This ensures:
- Generated code is always in sync with the generator
- No stale generated code in the repository
- Smaller repository size

To commit generated code (for faster CI builds):
1. Remove the module from `.gitignore`
2. Run `./gradlew :integration-tests:<module>:generateClient`
3. Commit the generated code
4. Consider running regeneration as a CI validation step

## Memory Configuration

For very large specs, you may need to increase Gradle's memory:

```properties
# gradle.properties
org.gradle.jvmargs=-Xmx4g
```
