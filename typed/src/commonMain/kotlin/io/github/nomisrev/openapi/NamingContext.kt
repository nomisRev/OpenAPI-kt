package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.routes.SchemaContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

// TODO refactor model so you can recognise NamingContext.Reference (isTopLevel) by type like before
@Serializable
data class NamingContext(val head: Head, val nested: List<Nested>) {
    fun nest(nest: Nested) = copy(nested = nested + nest)

    companion object {
        fun reference(name: String, context: SchemaContext) = NamingContext(Reference(name, context), emptyList())
        fun path(parts: List<String>) = NamingContext(Path(parts), emptyList())
        fun path(part: String) = NamingContext(Path(listOf(part)), emptyList())
    }

    @Serializable
    sealed interface Head

    @Serializable
    @JvmInline
    @SerialName("Path")
    value class Path(val parts: List<String>) : Head

    @Serializable
    @SerialName("Reference")
    data class Reference(val name: String, val context: SchemaContext) : Head

    // Nested always seems to be 'value.toString()' unify? How important are these types?
    @Serializable
    sealed interface Nested

    @Serializable
    @SerialName("ObjectProperty")
    data class ObjectProperty(val name: String) : Nested

    @Serializable
    @SerialName("UnionCase")
    data class UnionCase(val value: String) : Nested

    @Serializable
    @SerialName("DiscriminatedObjectCase")
    data class DiscriminatedObjectCase(val discriminator: String) : Nested

    @Serializable
    @SerialName("AdditionalProperties")
    data object AdditionalProperties : Nested

    @Serializable
    @SerialName("RouteParam")
    data class RouteParam(
        val name: String,
        val operationId: String
    ) : Nested

    @Serializable
    @SerialName("RouteBody")
    data class RouteBody(
        val name: String,
        val operationId: String
    ) : Nested

    @Serializable
    @SerialName("Response")
    data class Response(val operationId: String) : Nested
}
