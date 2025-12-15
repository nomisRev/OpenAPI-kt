package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.routes.SchemaContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

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

///**
// * Name generation is tricky... Still considering if or how to redesign.
// * Design is horrible. It needs to be split, check [TypeName] name function for the problem.
// */
//@Serializable
//sealed interface NamingPart {
//    /**
//     * Path is used in the name generation of the interfaces:
//     * "/fine_tuning/jobs/{fine_tuning_job_id}/events"
//     *
//     * Should result in nested interface hiearchy: FineTuning.Jobs.Events
//     * With syntax `val api.fineTuning.jobs.events.operationId(...)`
//     */
//    @Serializable
//    @JvmInline
//    @SerialName("Path")
//    value class Path(val parts: List<String>) : NamingContext {
//        constructor(part: String) : this(listOf(part))
//    }
//
//    /**
//     * This tracks nested, which is important for generating the correct class names. For example,
//     * /threads/{thread_id}/runs/{run_id}/submit_tool_outputs.
//     */
//    @Serializable
//    @ConsistentCopyVisibility
//    @SerialName("Nested")
//    data class Nested private constructor(val inner: NamingContext, val outer: NamingContext) : NamingContext {
//        companion object {
//            operator fun invoke(inner: NamingContext, outer: NamingContext): NamingContext = when (outer) {
//                is Path if inner is Path -> Path(outer.parts + inner.parts)
//                else -> Nested(inner, outer)
//            }
//        }
//    }
//
//    fun nest(other: NamingContext): NamingContext = Nested(other, this)
//
//    @Serializable
//    @SerialName("ObjectProperty")
//    data class ObjectProperty(val name: String) : GenerateName
//
//    @Serializable
//    @SerialName("Reference")
//    data class Reference(val name: String, val context: SchemaContext) : NamingContext
//
//    sealed interface GenerateName : NamingContext
//
//    @Serializable
//    @SerialName("AdditionalProperties")
//    data object AdditionalProperties : GenerateName
//
//    @Serializable
//    @SerialName("UnionCase")
//    // TODO move UnionCase name generation to Model, this way we can provide a nicer API for modifying it.
//    @JvmInline
//    value class UnionCase(val value: String) : GenerateName
//
//    @Serializable
//    @SerialName("DiscriminatedObjectCase")
//    data class DiscriminatedObjectCase(val discriminator: String) : GenerateName
//
//    /**
//     * An input param of a route. The param name is used to generate type names for inline schemas.
//     * $OuterClass$MyOperationId$ParamName
//     */
//    @Serializable
//    @SerialName("RouteParam")
//    data class RouteParam(
//        val name: String,
//        val operationId: String
//    ) : GenerateName
//
//    // TODO: reuse RouteParam with name == "body" or name == "request"
//    @Serializable
//    @SerialName("RouteBody")
//    data class RouteBody(
//        val name: String,
//        val operationId: String
//    ) : GenerateName
//
//    // TODO: Could be RouteParam with name == "response"
//    @Serializable
//    @SerialName("Response")
//    data class Response(val operationId: String) : GenerateName
//}
