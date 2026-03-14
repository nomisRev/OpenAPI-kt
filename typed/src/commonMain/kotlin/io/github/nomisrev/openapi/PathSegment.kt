package io.github.nomisrev.openapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PathSegment {
    val name: String

    @Serializable
    @SerialName("Literal")
    data class Literal(override val name: String) : PathSegment

    @Serializable
    @SerialName("Parameter")
    data class Parameter(override val name: String, val type: Model) : PathSegment
}

private val DefaultPathParameterType = Model.Primitive.String(
    default = null,
    description = null,
    constraint = null,
    isNullable = false,
    title = null
)

/**
 * Parses an OpenAPI path template into ordered literal and parameter segments.
 */
fun parsePathSegments(
    path: String,
    pathParamTypes: Map<String, Model>,
): List<PathSegment> = path.split("/")
    .filter(String::isNotEmpty)
    .map { segment ->
        if (segment.startsWith("{") && segment.endsWith("}")) {
            val paramName = segment.removeSurrounding("{", "}")
            PathSegment.Parameter(paramName, pathParamTypes[paramName] ?: DefaultPathParameterType)
        } else {
            PathSegment.Literal(segment)
        }
    }
