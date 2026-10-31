package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Response
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
): Route.Returns =
    Route.Returns(
        default = operation.responses.default?.resolve()?.toReturnType(segments, method),
        responses = operation.responses.responses.entries.associate { [code, response] ->
            Pair(HttpStatusCode.fromValue(code), response.resolve().toReturnType(segments, method))
        },
        extensions = operation.responses.extensions
    )

context(ctx: Registry)
private suspend fun Response.toReturnType(
    segments: List<PathSegment>,
    method: HttpMethod,
): Route.ReturnType {
    val typed = linkedMapOf<ContentType, Model>()
    val raw = linkedSetOf<ContentType>()
    content.entries.forEach { [contentType, mediaType] ->
        val parsedContentType = ContentType.parse(contentType)
        val model = mediaType.schema?.toModel(
            NamingContext.path(segments, method).nest(NamingContext.Response),
            SchemaContext.Read
        ) ?: parsedContentType.inferredModelWithoutSchema()

        if (model == null) raw += parsedContentType
        else typed[parsedContentType] = model
    }
    return Route.ReturnType(
        types = typed,
        rawContentTypes = raw,
        extensions = extensions,
    )
}

private fun ContentType.inferredModelWithoutSchema(): Model? {
    val [type, subtype] = typeAndSubtype()
    return when {
        subtype == "json" || subtype.endsWith("+json") ->
            Model.FreeFormJson(description = null, constraint = null, isNullable = false, title = null)

        type == "text" || subtype.isTextualApplicationSubtype() ->
            Model.Primitive.String(default = null, description = null, constraint = null, isNullable = false, title = null)

        else -> null
    }
}

private fun ContentType.typeAndSubtype(): List<String> {
    val normalized = toString().substringBefore(';').trim().lowercase()
    val parts = normalized.split('/', limit = 2)
    return if (parts.size == 2) parts else listOf(parts.firstOrNull().orEmpty(), "")
}

private fun String.isTextualApplicationSubtype(): Boolean =
    this == "xml" || endsWith("+xml") ||
            this == "yaml" || this == "x-yaml" || endsWith("+yaml") || endsWith("+x-yaml") ||
            this == "javascript" || this == "x-javascript" ||
            this == "graphql" || this == "x-www-form-urlencoded"

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
