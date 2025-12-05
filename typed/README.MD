# Module OpenAPI Kotlin Typed

OpenAPI Kotlin Typed provides a structured model for OpenAPI 3.0.x specification files. It is generated from the OpenAPI
3.0.x specification JSON schema,
this allows for more easily working with the structure to generate code.

# Implemented Features

## Nullability

- Nullability is determined solely by `nullable` property in the schema.
- TODO: Flatten oneOf/anyOf { type: null, type: string } into a single type

## Primitive Types

- Long (type: integer, format: int64 or null)
- Int (type: integer, format: int32)
- Double (type: number, format: double or null)
- Float (type: number, format: float)
- Boolean (type: boolean)
- String (type: string, format: null, byte, and any other string format)
- ByteArray (type: string, format: binary)
- Date (type: string, format: date)
- DateTime (type: string, format: date-time)

Default values are supported for type: integer, number, boolean, and string. and their nullable counterparts.
Constraints are supported for type: integer, number, and string.
