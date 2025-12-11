package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Response
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.toModel
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode

context(ctx: Registry)
suspend fun Endpoint.returns(): Route.Returns =
    Route.Returns(
        default = operation.responses.default?.resolve()?.returnType(),
        responses = operation.responses.responses.entries.associate { (code, response) ->
            Pair(HttpStatusCode.fromValue(code), response.resolve().returnType())
        },
        extensions = operation.responses.extensions
    )

context(endpoint: Endpoint, ctx: Registry)
private suspend fun ResolvedResponse.returnType(): Route.ReturnType =
    Route.ReturnType(types = value.allContentModels(), extensions = value.extensions)

context(endpoint: Endpoint, ctx: Registry)
private suspend fun Response.allContentModels(): Map<ContentType, Model> =
    content.entries.associate { (contentType, mediaType) ->
        val schema = requireNotNull(mediaType.schema) { "Response without $mediaType schema. $this" }
        Pair(
            ContentType.parse(contentType),
            schema.toModel(endpoint.context(NamingContext.Response(endpoint.operationId)), SchemaContext.Read)
        )
    }

private sealed interface ResolvedResponse {
    val value: Response

    data class Value(override val value: Response) : ResolvedResponse
    data class Reference(val ref: String, override val value: Response) : ResolvedResponse
}

// TODO Move to Registry and support top-level schemas.
context(ctx: Registry)
private fun ReferenceOr<Response>.resolve(): ResolvedResponse = when (this) {
    is ReferenceOr.Value -> ResolvedResponse.Value(value)
    is ReferenceOr.Reference -> {
        val referenceName = ref.drop("#/components/responses/".length)
        when (val requestBodies = ctx.openAPI.components.responses[referenceName]) {
            is ReferenceOr.Reference -> TODO("Remote parameters not supported yet.")
            is ReferenceOr.Value<Response> -> ResolvedResponse.Reference(referenceName, requestBodies.value)
            null -> throw IllegalStateException("Parameter $referenceName could not be found in ${ctx.openAPI.components.parameters}.")
        }
    }
}
