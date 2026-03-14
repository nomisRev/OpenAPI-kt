package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.routes.SchemaContext
import io.ktor.http.HttpMethod
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// TODO refactor model so you can recognise NamingContext.Reference (isTopLevel) by type like before
@Serializable
data class NamingContext(val head: Head, val nested: List<Nested>) {
    fun nest(nest: Nested) = copy(nested = nested + nest)

    companion object {
        fun reference(name: String, context: SchemaContext) = NamingContext(Reference(name, context), emptyList())
        fun path(segments: List<PathSegment>, method: HttpMethod) = NamingContext(Path(segments, method), emptyList())
        fun path(parts: List<String>) = path(parts.map(PathSegment::Literal), HttpMethod.Get)
        fun path(part: String) = path(listOf(part))
    }

    @Serializable
    sealed interface Head

    @Serializable
    @SerialName("Path")
    data class Path(
        val segments: List<PathSegment>,
        @Serializable(with = HttpMethodSerializer::class)
        val method: HttpMethod,
    ) : Head

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
    data class RouteParam(val name: String) : Nested

    @Serializable
    @SerialName("RouteBody")
    data object RouteBody : Nested

    @Serializable
    @SerialName("Response")
    data object Response : Nested
}

private object HttpMethodSerializer : KSerializer<HttpMethod> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("HttpMethod", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: HttpMethod) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): HttpMethod =
        HttpMethod(decoder.decodeString())
}
