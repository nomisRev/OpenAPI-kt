package io.github.nomisrev.openapi.registry

import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.Schema

/**
 * Seems a bit redundant, but helps keep track of the schema's origin.
 * - `Value` is a schema that was directly defined in the OpenAPI document.
 * - `Reference` is a schema that was referenced from components or network.
 * - `Recursive` is a schema that was referenced from itself, exit value.
 */
sealed interface ResolvedSchema {
    val schema: Schema
    val name: NamingContext
    val isNullable: Boolean
        get() = schema.nullable ?: false

    data class Value(override val name: NamingContext, override val schema: Schema) : ResolvedSchema
    data class Reference(override val name: NamingContext.Reference, override val schema: Schema) : ResolvedSchema
    data class Recursive(override val name: NamingContext, override val schema: Schema) : ResolvedSchema
}
