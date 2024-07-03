# Module OpenAPI Kotlin Gradle Plugin

OpenAPI Kotlin Gradle Plugin is a Gradle plugin that uses `generation` to generate Kotlin Code.

To use the plugin, you need to add the following to your `build.gradle.kts` file:

```kotlin
openApiConfig {
  spec("OpenAI", file("openai.yaml")) {
    // packageName = "io.github.nomisrev.openai"
  }
}
```

This will generate the Kotlin code from the `openai.yaml` file,
and the generated code will be placed in the `build/generated` directory.

The Gradle plugin automatically adds the generated code to the source set of the project.
