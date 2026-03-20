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
    data class Parameter(override val name: String, val model: Model) : PathSegment

    @Serializable
    @SerialName("FixedValue")
    data class FixedValue(
        val wireValue: String,
        val sourceName: String,
    ) : PathSegment {
        override val name: String
            get() = wireValue
    }

    @Serializable
    @SerialName("OverloadedParameter")
    data class OverloadedParameter(
        override val name: String,
        val type: Model.Union,
    ) : PathSegment {
        val cases: List<Model.Union.Case>
            get() = type.cases
    }
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
            (pathParamTypes[paramName] ?: DefaultPathParameterType).toPathSegment(paramName)
        } else {
            PathSegment.Literal(segment)
        }
    }

internal fun Model.toPathSegment(paramName: String): PathSegment = when (this) {
    is Model.Union -> toPathSegmentOrOverload(paramName)
    else -> PathSegment.Parameter(paramName, this)
}

internal fun Model.Union.toPathSegmentOrOverload(paramName: String): PathSegment {
    if (!isFlattenablePathUnion()) return PathSegment.Parameter(paramName, this)
    return PathSegment.OverloadedParameter(paramName, this)
}

internal fun Model.Union.isFlattenablePathUnion(): Boolean =
    cases.all { case ->
        case.model is Model.Primitive ||
            case.model is Model.Enum ||
            case.model is Model.Uuid ||
            case.model is Model.Date ||
            case.model is Model.DateTime
    }
