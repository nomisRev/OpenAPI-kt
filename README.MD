# Module OpenAPI-kt

[![Maven Central](https://img.shields.io/maven-central/v/io.github.nomisrev/openapi-kt-parser?color=4caf50&label=latest%20release)](https://central.sonatype.com/search?q=g:io.github.nomisrev/openapi-kt-parser)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

**WORK IN PROGRESS**

OpenKTTP is a toolset for working with OpenAPI in Kotlin.
This project exists out of several pieces, and they can be combined in different ways to achieve different goals.

- Core: A OpenAPI parser, and typed ADT based on KotlinX Serialization
- OpenAPI Typed: A version of the `Core` ADT, structures the data in a convenient way to retrieve.
- Generator: A code generator that generates code from the `OpenAPI Typed` ADT
- Gradle Plugin: Gradle plugin to conveniently generate clients

## Code Generation

Add the following to your `build.gradle.kts` file.

```kotlin
plugin {
  id("io.github.nomisrev.openapi-kt-plugin") version "0.0.4"
}

openApiConfig {
  spec("OpenAI", file("openai.yaml")) {
    // packageName = "my.company.openai.generated"
  }
}
```

Then run the following command to generate the code,
but it will also automatically run when you build your project.

```shell
./gradlew generateOpenApi
```

This will generate a `io.github.nomisrev.openapi` package with the generated code,
in your `/build/generated` directory.
