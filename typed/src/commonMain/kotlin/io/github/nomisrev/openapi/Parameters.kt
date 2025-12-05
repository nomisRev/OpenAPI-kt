package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ResolvedSchema.Value
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema

enum class SchemaContext {
    Input, Output;
}

context(ctx: ApiCtx)
suspend fun Endpoint.parameters(): List<Route.Input> {
    val parameters = withMissingPathParameters()
    // TODO: configure as part of Leniency mode
    requireUnique(parameters.map { it.name() }, "Expected all parameters to have unique names. $parameters")
    return parameters.map { it.toRouteInput() }
}

context(ctx: ApiCtx, endpoint: Endpoint)
private suspend fun ResolvedParameter.toRouteInput(): Route.Input {
    val schema =
        requireNotNull(resolvedSchema()) { "Parameter ${name()} without schema for ${endpoint.path} ${endpoint.method}" }

    when(schema) {
        is ResolvedSchema.Reference -> TODO()
        is Value -> TODO()
    }
}

context(ctx: ApiCtx)
private suspend fun ResolvedParameter.resolvedSchema(): ResolvedSchema? = when(this) {
    is ResolvedParameter.Reference -> value.schema?.resolve(SchemaContext.Input)
    is ResolvedParameter.Value -> value.schema?.resolve(SchemaContext.Input)
}

private fun ResolvedParameter.name(): String = when(this) {
    is ResolvedParameter.Reference -> value.name
    is ResolvedParameter.Value -> value.name
}

/**
 * Some specs are missing path parameters even though they're defined in their path. (OpenAI - problem child)
 * We work around this by manually adding the required path parameters and assume they're required.
 *
 * TODO: configure as part of Leniency mode
 */
context(ctx: ApiCtx)
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
    data class Value(val value: Parameter) : ResolvedParameter
    data class Reference(val ref: String, val value: Parameter) : ResolvedParameter
}

context(ctx: ApiCtx)
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