package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.parser.MediaType
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.RequestBody
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.toModel
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kotlin.collections.component1
import kotlin.collections.component2

context(ctx: Registry)
suspend fun resolveBodies(
    path: String,
    segments: List<PathSegment>,
    method: HttpMethod,
    operation: Operation,
): Route.Bodies? = operation.requestBody?.resolve()?.value?.toBodies(path, segments, method)

context(ctx: Registry)
private suspend fun RequestBody.toBodies(
    path: String,
    segments: List<PathSegment>,
    method: HttpMethod,
): Route.Bodies? {
    val typedBodies = content.entries.mapNotNull { (contentType, mediaType) ->
        val schema = mediaType.schema ?: return@mapNotNull null
        val body = when {
            ContentType.MultiPart.FormData.match(contentType) -> formDataToBody(segments, method, mediaType, schema)
            ContentType.Application.FormUrlEncoded.match(contentType) -> formUrlEncoded(segments, method, mediaType, schema)
            else -> toBody(segments, method, contentType, mediaType, schema)
        }
        Pair(ContentType.parse(contentType), body)
    }.toMap()

    if (typedBodies.isEmpty()) {
        if (!required) return null
        val renderedContentTypes = content.keys.sorted().ifEmpty { listOf("<none>") }.joinToString(", ")
        throw IllegalArgumentException(
            "Required request body for ${method.value} $path " +
                    "has no schema in any content type. Content types: $renderedContentTypes"
        )
    }

    return Route.Bodies(
        required = required,
        types = typedBodies,
        extensions = extensions
    )
}

context(scope: Registry)
private suspend fun RequestBody.formDataToBody(
    segments: List<PathSegment>,
    method: HttpMethod,
    mediaType: MediaType,
    schema: ReferenceOr<Schema>,
): Route.Body {
    val name = NamingContext.path(segments, method).nest(NamingContext.RouteBody)
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

context(scope: Registry)
private suspend fun RequestBody.formUrlEncoded(
    segments: List<PathSegment>,
    method: HttpMethod,
    mediaType: MediaType,
    schema: ReferenceOr<Schema>,
): Route.Body {
    val name = NamingContext.path(segments, method).nest(NamingContext.RouteBody)
    val obj = requireNotNull(schema.toModel(name, SchemaContext.Write) as? Model.Object) {
        "Form URL encoded body must be an object. $this"
    }
    val params = obj.properties.map { (baseName, prop) -> Route.Body.Multipart.FormData(baseName, prop.model) }
    return Route.Body.FormUrlEncoded(params, description, mediaType.extensions)
}

context(scope: Registry)
private suspend fun RequestBody.toBody(
    segments: List<PathSegment>,
    method: HttpMethod,
    contentType: String,
    mediaType: MediaType,
    schema: ReferenceOr<Schema>,
): Route.Body {
    val name = NamingContext.path(segments, method).nest(NamingContext.RouteBody)
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
