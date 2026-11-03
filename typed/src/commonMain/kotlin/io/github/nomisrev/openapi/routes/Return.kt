package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Response
import io.github.nomisrev.openapi.parser.StatusCodeOrRange
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.resolveComponent
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
): Route.ReturnType {
    val name = NamingContext.path(segments, method).nest(NamingContext.Response)
    content.forEach { [contentType, mediaType] ->
        require(mediaType.schema != null || mediaType.itemSchema != null) {
            "Response without $mediaType schema for ${method.value} $path ($contentType). $this"
        }
    }
    return Route.ReturnType(
        types = content.entries.mapNotNull { [contentType, mediaType] ->
            mediaType.schema?.let { schema ->
                Pair(ContentType.parse(contentType), schema.toModel(name, SchemaContext.Read))
            }
        }.toMap(),
        extensions = extensions,
        streams = content.entries.mapNotNull { [contentType, mediaType] ->
            mediaType.itemSchema?.let { itemSchema ->
                val parsed = ContentType.parse(contentType)
                Pair(parsed, itemSchema.toStream(parsed, name))
            }
        }.toMap(),
    )
}

context(ctx: Registry)
private fun ReferenceOr<Response>.resolve(): Response =
    resolveComponent("responses", ctx.openAPI.components.responses)
