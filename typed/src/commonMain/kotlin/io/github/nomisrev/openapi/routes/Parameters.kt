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
import io.github.nomisrev.openapi.registry.toModel
import io.ktor.http.HttpMethod

enum class SchemaContext {
    Write, Read, Null;
}

context(ctx: Registry)
suspend fun resolveParameters(
    path: String,
    method: HttpMethod,
    operation: Operation,
): List<Route.Input> {
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
    return deduplicated.map { parameter ->
        parameter.toRouteInput(path, segments, method)
    }
}

context(ctx: Registry)
private suspend fun Parameter.toRouteInput(
    path: String,
    segments: List<PathSegment>,
    method: HttpMethod,
): Route.Input {
    val context = NamingContext.path(segments, method).nest(RouteParam(name))
    val refOrSchema = requireNotNull(schema) {
        "Parameter $name without schema for ${method.value} $path"
    }
    val type = refOrSchema.toModel(context, SchemaContext.Write)
    return Route.Input(name, type, required, input, description)
}

/**
 * Some specs are missing path parameters even though they're defined in their path. (OpenAI - problem child)
 * We work around this by manually adding the required path parameters and assume they're required.
 *
 * TODO: configure as part of Leniency mode
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

// TODO Move to Registry and support top-level schemas.
context(ctx: Registry)
private fun ReferenceOr<Parameter>.resolve(): Parameter = when (this) {
    is ReferenceOr.Value -> value
    is ReferenceOr.Reference -> {
        val referenceName = ref.drop("#/components/parameters/".length)
        when (val parameter = ctx.openAPI.components.parameters[referenceName]) {
            is ReferenceOr.Reference -> TODO("Remote parameters not supported yet.")
            is ReferenceOr.Value<Parameter> -> parameter.value
            null -> throw IllegalStateException("Parameter $referenceName could not be found in ${ctx.openAPI.components.parameters}.")
        }
    }
}

private val pathParamRegex = Regex("\\{.*?\\}")

private fun extractPathParameters(path: String): List<String> =
    pathParamRegex.findAll(path).map { it.value.removeSurrounding("{", "}") }.toList()
