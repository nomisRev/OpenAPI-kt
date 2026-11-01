package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Response
import io.github.nomisrev.openapi.parser.StatusCodeOrRange
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.toModel
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode

context(ctx: Registry)
suspend fun resolveReturns(
    path: String,
    segments: List<PathSegment>,
    method: HttpMethod,
    operation: Operation,
): Route.Returns {
    val exactResponses = operation.responses.responses.filterKeys { it is StatusCodeOrRange.Code }
    val responseRanges = operation.responses.responses.filterKeys { it is StatusCodeOrRange.Range }
    return Route.Returns(
        default = operation.responses.default?.resolve()?.toReturnType(path, segments, method),
        responses = exactResponses.entries.associate { entry ->
            val statusCode = (entry.key as StatusCodeOrRange.Code).value
            HttpStatusCode.fromValue(statusCode) to entry.value.resolve().toReturnType(path, segments, method)
        },
        responseRanges = responseRanges.entries.associate { entry ->
            val range = entry.key as StatusCodeOrRange.Range
            range to entry.value.resolve().toReturnType(path, segments, method)
        },
        extensions = operation.responses.extensions
    )
}

context(ctx: Registry)
private suspend fun Response.toReturnType(
    path: String,
    segments: List<PathSegment>,
    method: HttpMethod,
): Route.ReturnType = Route.ReturnType(
    types = content.entries.associate { [contentType, mediaType] ->
        val schema = requireNotNull(mediaType.schema) {
            "Response without $mediaType schema for ${method.value} $path. $this"
        }
        Pair(
            ContentType.parse(contentType),
            schema.toModel(NamingContext.path(segments, method).nest(NamingContext.Response), SchemaContext.Read)
        )
    },
    extensions = extensions
)

// TODO Move to Registry and support top-level schemas.
context(ctx: Registry)
private fun ReferenceOr<Response>.resolve(): Response = when (this) {
    is ReferenceOr.Value -> value
    is ReferenceOr.Reference -> {
        val referenceName = ref.drop("#/components/responses/".length)
        when (val responses = ctx.openAPI.components.responses[referenceName]) {
            is ReferenceOr.Reference -> TODO("Remote parameters not supported yet.")
            is ReferenceOr.Value<Response> -> responses.value
            null -> error("Response $referenceName could not be found in ${ctx.openAPI.components.responses}.")
        }
    }
}
