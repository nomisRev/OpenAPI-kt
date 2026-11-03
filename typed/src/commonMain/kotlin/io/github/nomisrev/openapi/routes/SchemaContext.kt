package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.NamingContext.RouteParam
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.parsePathSegments
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.resolveComponent
import io.github.nomisrev.openapi.registry.toModel
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod

enum class SchemaContext {
    Write, Read, Null;
}

/**
 * The parameters of an operation, including its path-level ones: [inputs] become arguments of the generated
 * operation, [ignored] are the header parameters OpenAPI says SHALL be ignored (see [isIgnoredHeader]).
 */
data class ResolvedParameters(
    val inputs: List<Route.Input>,
    val ignored: List<Parameter>,
)

context(ctx: Registry)
suspend fun resolveParameters(
    path: String,
    method: HttpMethod,
    operation: Operation,
): ResolvedParameters {
    val pathParameters = extractPathParameters(path)
    val parameters = withMissingPathParameters(operation.parameters, pathParameters)
    val segments = parsePathSegments(path, emptyMap())
    // Per OpenAPI spec, a unique parameter is identified by (name, in).
    // Path-level parameters are prepended before operation-level ones (see withParams).
    // When both declare the same (name, in), the operation-level definition takes precedence.
    // We deduplicate by keeping the last occurrence (operation-level wins).
    val deduplicated = parameters
        .reversed()
        .distinctBy { it.name to it.input }
        .reversed()
    val [ignored, inputs] = deduplicated.partition(Parameter::isIgnoredHeader)
    return ResolvedParameters(
        inputs = inputs.map { parameter -> parameter.toRouteInput(path, segments, method) },
        ignored = ignored,
    )
}

/**
 * OpenAPI Parameter Object: "If `in` is "header" and the `name` field is "Accept", "Content-Type" or "Authorization",
 * the parameter definition SHALL be ignored." Header names are case-insensitive. The client sends `Content-Type` from
 * the request body, `Accept` from the declared responses, and `Authorization` is described by security schemes, so
 * none of them is an argument of the operation.
 */
val Parameter.isIgnoredHeader: Boolean
    get() = input == Parameter.Input.Header && IgnoredHeaderNames.any { it.equals(name, ignoreCase = true) }

private val IgnoredHeaderNames = listOf("Accept", "Content-Type", "Authorization")

context(ctx: Registry)
private suspend fun Parameter.toRouteInput(
    path: String,
    segments: List<PathSegment>,
    method: HttpMethod,
): Route.Input {
    val context = NamingContext.path(segments, method).nest(RouteParam(name))
    val [type, contentType] = if (schema != null) {
        schema!!.toModel(context, SchemaContext.Write) to null
    } else if (content.isNotEmpty()) {
        val [contentType, mediaType] = content.entries.first()
        val mediaTypeSchema = requireNotNull(mediaType.schema) {
            "Parameter $name with content $contentType without schema for ${method.value} $path"
        }
        mediaTypeSchema.toModel(context, SchemaContext.Write) to ContentType.parse(contentType)
    } else {
        error("Parameter $name without schema or content for ${method.value} $path")
    }
    return Route.Input(name, type, contentType, required, input, description)
}

/**
 * Some specs are missing path parameters even though they're defined in their path. (OpenAI - problem child)
 * We work around this by manually adding the required path parameters and assume they're required.
 */
context(ctx: Registry)
private fun withMissingPathParameters(
    parameters: List<ReferenceOr<Parameter>>,
    pathParameters: List<String>,
): List<Parameter> {
    val resolvedParameters = parameters.map { it.resolve() }
    val parameterNames = resolvedParameters.map { it.name }
    val missing = (pathParameters - parameterNames.toSet()).map { path ->
        Parameter(
            name = path,
            input = Parameter.Input.Path,
            schema = ReferenceOr.value(Schema(type = Schema.Type.Basic.String))
        )
    }

    val priorityOrder = pathParameters.withIndex().associate { it.value to it.index }
    return (missing + resolvedParameters).sortedBy { priorityOrder[it.name] ?: Int.MAX_VALUE }
}

context(ctx: Registry)
private fun ReferenceOr<Parameter>.resolve(): Parameter =
    resolveComponent("parameters", ctx.openAPI.components.parameters)

private val pathParamRegex = Regex("\\{.*?\\}")

private fun extractPathParameters(path: String): List<String> =
    pathParamRegex.findAll(path).map { it.value.removeSurrounding("{", "}") }.toList()
