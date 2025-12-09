package io.github.nomisrev.openapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Name generation is tricky... Still considering if or how to redesign.
 */
@Serializable
sealed interface NamingContext {
    /**
     * Path is used in the name generation of the interfaces:
     * "/fine_tuning/jobs/{fine_tuning_job_id}/events"
     *
     * Should result in nested interface hiearchy: FineTuning.Jobs.Events
     * With syntax `val api.fineTuning.jobs.events.operationId(...)`
     */
    @Serializable
    @JvmInline
    @SerialName("Path")
    value class Path(val part: List<String>) : NamingContext {
        constructor(part: String) : this(listOf(part))
    }

    /**
     * This tracks nested, which is important for generating the correct class names. For example,
     * /threads/{thread_id}/runs/{run_id}/submit_tool_outputs.
     */
    @Serializable
    @ConsistentCopyVisibility
    @SerialName("Nested")
    data class Nested private constructor(val inner: NamingContext, val outer: NamingContext) : NamingContext {
        companion object {
            operator fun invoke(inner: NamingContext, outer: NamingContext): NamingContext = when (outer) {
                is Path if inner is Path -> Path(outer.part + inner.part)
                else -> Nested(inner, outer)
            }
        }
    }

    fun nest(other: NamingContext): NamingContext = Nested(other, this)

    @Serializable
    @SerialName("ObjectProperty")
    data class ObjectProperty(val name: String) : GenerateName

    @Serializable
    @SerialName("Reference")
    data class Reference(val name: String, val context: SchemaContext?) : NamingContext

    sealed interface GenerateName : NamingContext

    @Serializable
    @SerialName("AdditionalProperties")
    data object AdditionalProperties : GenerateName

    @Serializable
    @SerialName("UnionCase")
    data object UnionCase : GenerateName

    /**
     * An input param of a route. The param name is used to generate type names for inline schemas.
     * $OuterClass$MyOperationId$ParamName
     */
    @Serializable
    @SerialName("RouteParam")
    data class RouteParam(
        val name: String,
        val operationId: String
    ) : GenerateName

    // TODO: reuse RouteParam with name == "body" or name == "request"
    @Serializable
    @SerialName("RouteBody")
    data class RouteBody(
        val name: String,
        val operationId: String
    ) : GenerateName

    // TODO: Could be RouteParam with name == "response"
    @Serializable
    @SerialName("Response")
    data class Response(val operationId: String) : GenerateName
}
