# OpenAPI-kt

**WORK IN PROGRESS**

OpenKTTP is a toolset for working with OpenAPI in Kotlin.
This project exists out of several pieces, and they can be combined in different ways to achieve different goals.

- Core: A OpenAPI parser, and typed ADT based on KotlinX Serialization
- OpenAPI Typed: A version of the `Core` ADT, structures the data in a convenient way to retrieve.
- Generator: A code generator that generates code from the `OpenAPI Typed` ADT
- Gradle Plugin: Gradle plugin to conveniently generate clients
