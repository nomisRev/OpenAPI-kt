package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema



context(ctx: ApiCtx)
suspend fun Endpoint.parameters(): List<Route.Input> {
    val parameters = withMissingPathParameters()
    // TODO: configure as part of Leniency mode
    requireUnique(parameters.map { it.name() }, "Expected all parameters to have unique names. $parameters")
    return parameters.map { it.toRouteInput() }
}

context(ctx: ApiCtx, endpoint: Endpoint)
private suspend fun Resolved<Parameter>.toRouteInput(): Route.Input {
    val schema =
        requireNotNull(resolvedSchema()) { "Parameter ${name()} without schema for ${endpoint.path} ${endpoint.method}" }

    when (schema) {
        is Resolved.Reference<Schema> -> TODO()
        is Resolved.Value<Schema> -> TODO()
    }
}

context(ctx: ApiCtx)
private suspend fun Resolved<Parameter>.resolvedSchema(): Resolved<Schema>? =
    fold({ it.value.schema?.resolve() }, { it.value.schema?.resolve() })

private fun Resolved<Parameter>.name(): String =
    fold({ it.value.name }, { it.value.name })

/**
 * Some specs are missing path parameters even though they're defined in their path. (OpenAI - problem child)
 * We work around this by manually adding the required path parameters and assume they're required.
 *
 * TODO: configure as part of Leniency mode
 */
context(ctx: ApiCtx)
private fun Endpoint.withMissingPathParameters(): List<Resolved<Parameter>> {
    val parameters = operation.parameters.map { it.resolve() }
    val parameterNames = parameters.map { it.name() }
    val pathParameters = pathParameters
    val missing = (this.pathParameters - parameterNames.toSet()).map { path ->
        Resolved.Value(
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
