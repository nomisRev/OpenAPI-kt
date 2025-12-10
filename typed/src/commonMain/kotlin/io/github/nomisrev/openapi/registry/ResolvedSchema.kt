package io.github.nomisrev.openapi.registry

import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.Schema
import kotlin.jvm.JvmInline

/**
 * Type to track how the schema was resolved. This is important to keep proper track of name generation, and recursion.
 *
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

/**
 * Type to peek at a schema, and now its origin, without
 */
sealed interface PeekSchema {
    val schema: Schema

    @JvmInline
    value class Value(override val schema: Schema) : PeekSchema

    @JvmInline
    value class Reference(override val schema: Schema) : PeekSchema

    @JvmInline
    value class Recursive(override val schema: Schema) : PeekSchema
}
