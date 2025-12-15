package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.NamingContext.RouteParam
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.toModel

enum class SchemaContext {
    Write, Read, Null;
}

context(ctx: Registry)
suspend fun Endpoint.parameters(): List<Route.Input> {
    val parameters = withMissingPathParameters()
    // TODO: configure as part of Leniency mode
    requireUnique(
        parameters.map { it.name() },
        "Expected all parameters to have unique names. $parameters"
    )
    return parameters.map { parameter ->
        parameter.toRouteInput(context(RouteParam(parameter.name(), operationId)))
    }
}

context(ctx: Registry, endpoint: Endpoint)
private suspend fun ResolvedParameter.toRouteInput(name: NamingContext): Route.Input {
    val refOrSchema = requireNotNull(value.schema) {
        "Parameter ${name()} without schema for ${endpoint.path} ${endpoint.method}"
    }
    val type = refOrSchema.toModel(name, SchemaContext.Write)
    return Route.Input(name(), type, value.required, value.input, value.description)
}

private fun ResolvedParameter.name(): String = when (this) {
    is ResolvedParameter.Reference -> value.name
    is ResolvedParameter.Value -> value.name
}

/**
 * Some specs are missing path parameters even though they're defined in their path. (OpenAI - problem child)
 * We work around this by manually adding the required path parameters and assume they're required.
 *
 * TODO: configure as part of Leniency mode
 */
context(ctx: Registry) // TODO horrible function
private fun Endpoint.withMissingPathParameters(): List<ResolvedParameter> {
    val parameters = operation.parameters.map { it.resolve() }
    val parameterNames = parameters.map { it.name() }
    val pathParameters = pathParameters
    val missing = (this.pathParameters - parameterNames.toSet()).map { path ->
        ResolvedParameter.Value(
            Parameter(
                name = path,
                input = Parameter.Input.Path,
                schema = ReferenceOr.value(Schema(type = Schema.Type.Basic.String))
            )
        )
    }

    val priorityOrder = pathParameters.withIndex().associate { it.value to it.index }
    return (missing + parameters).sortedBy { priorityOrder[it.name()] ?: Int.MAX_VALUE }
}

private sealed interface ResolvedParameter {
    val value: Parameter

    data class Value(override val value: Parameter) : ResolvedParameter
    data class Reference(val ref: String, override val value: Parameter) : ResolvedParameter
}

// TODO Move to Registry and support top-level schemas.
context(ctx: Registry)
private fun ReferenceOr<Parameter>.resolve(): ResolvedParameter = when (this) {
    is ReferenceOr.Value -> ResolvedParameter.Value(value)
    is ReferenceOr.Reference -> {
        val referenceName = ref.drop("#/components/parameters/".length)
        when (val parameter = ctx.openAPI.components.parameters[referenceName]) {
            is ReferenceOr.Reference -> TODO("Remote parameters not supported yet.")
            is ReferenceOr.Value<Parameter> -> ResolvedParameter.Reference(referenceName, parameter.value)
            null -> throw IllegalStateException("Parameter $referenceName could not be found in ${ctx.openAPI.components.parameters}.")
        }
    }
}