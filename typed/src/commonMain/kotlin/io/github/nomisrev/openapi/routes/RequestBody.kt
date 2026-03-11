package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.MediaType
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.RequestBody
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.toModel
import io.ktor.http.ContentType
import kotlin.collections.component1
import kotlin.collections.component2

context(ctx: Registry)
suspend fun Endpoint.bodies(): Route.Bodies? =
    operation.requestBody?.resolve()?.value?.toBodies()

context(endpoint: Endpoint, scope: Registry)
private suspend fun RequestBody.toBodies(): Route.Bodies =
    Route.Bodies(
        required,
        content.entries.associate { (contentType, mediaType) ->
            val body = when {
                ContentType.MultiPart.FormData.match(contentType) -> formDataToBody(contentType, mediaType)
                ContentType.Application.FormUrlEncoded.match(contentType) -> formUrlEncoded(contentType, mediaType)
                else -> toBody(contentType, mediaType)
            }
            Pair(ContentType.parse(contentType), body)
        },
        extensions = extensions
    )

context(endpoint: Endpoint, scope: Registry)
private suspend fun RequestBody.formDataToBody(contentType: String, mediaType: MediaType): Route.Body {
    val schema = requireNotNull(mediaType.schema) { "$mediaType without a schema. $this" }
    val name = endpoint.context(NamingContext.RouteBody("body", endpoint.operationId))
    return when (val model = schema.toModel(name, SchemaContext.Write)) {
        is Model.Object -> {
            val params = model.properties.map { (baseName, prop) ->
                Route.Body.Multipart.FormData(baseName, prop.model)
            }
            Route.Body.Multipart.Value(params, description, mediaType.extensions)
        }

        else -> Route.Body.Multipart.Ref(model, description, mediaType.extensions)
    }
}

context(endpoint: Endpoint, scope: Registry)
private suspend fun RequestBody.formUrlEncoded(contentType: String, mediaType: MediaType): Route.Body {
    val schema = requireNotNull(mediaType.schema) { "$mediaType without a schema. $this" }
    val name = endpoint.context(NamingContext.RouteBody("body", endpoint.operationId))
    val obj = requireNotNull(schema.toModel(name, SchemaContext.Write) as? Model.Object) {
        "Form URL encoded body must be an object. $this"
    }
    val params = obj.properties.map { (baseName, prop) -> Route.Body.Multipart.FormData(baseName, prop.model) }
    return Route.Body.FormUrlEncoded(params, description, mediaType.extensions)
}

context(endpoint: Endpoint, scope: Registry)
private suspend fun RequestBody.toBody(contentType: String, mediaType: MediaType): Route.Body {
    val schema = requireNotNull(mediaType.schema) { "$mediaType without a schema. $this" }
    val name = endpoint.context(NamingContext.RouteBody("body", endpoint.operationId))
    return Route.Body.SetBody(
        contentType = ContentType.parse(contentType),
        type = schema.toModel(name, SchemaContext.Write),
        description = description,
        extensions = mediaType.extensions,
    )
}

private sealed interface ResolvedBody {
    val value: RequestBody

    data class Value(override val value: RequestBody) : ResolvedBody
    data class Reference(val ref: String, override val value: RequestBody) : ResolvedBody
}

// TODO Move to Registry and support top-level schemas.
context(ctx: Registry)
private fun ReferenceOr<RequestBody>.resolve(): ResolvedBody = when (this) {
    is ReferenceOr.Value -> ResolvedBody.Value(value)
    is ReferenceOr.Reference -> {
        val referenceName = ref.drop("#/components/requestBodies/".length)
        when (val requestBodies = ctx.openAPI.components.requestBodies[referenceName]) {
            is ReferenceOr.Reference -> TODO("Remote parameters not supported yet.")
            is ReferenceOr.Value<RequestBody> -> ResolvedBody.Reference(referenceName, requestBodies.value)
            null -> throw IllegalStateException("Parameter $referenceName could not be found in ${ctx.openAPI.components.parameters}.")
        }
    }
}
