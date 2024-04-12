# OpenKTTP

OpenKTTP is a toolset for working with OpenAPI in Kotlin.
This project exists out of several pieces, and they can be combined in different ways to achieve different goals.

- Core: A OpenAPI parser, and typed ADT based on KotlinX Serialization
- OpenAPI Typed: A version of the `Core` ADT, structures the data in a convenient way to retrieve.
- Generic: A `Generic` ADT that allows working with content regardless of its format.
