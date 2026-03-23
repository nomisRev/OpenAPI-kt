package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.parser.AdditionalProperties
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
): Route.Bodies? = operation.requestBody?.resolve()?.toBodies(path, segments, method)

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
            ContentType.Application.FormUrlEncoded.match(contentType) ->
                formUrlEncoded(segments, method, mediaType, schema)

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
    val name = when (schema) {
        is ReferenceOr.Reference ->
            NamingContext.reference(schema.ref.drop("#/components/schemas/".length), SchemaContext.Write)
        is ReferenceOr.Value -> NamingContext.path(segments, method).nest(NamingContext.RouteBody)
    }
    val resolvedSchema = schema.resolveSchema()
    return with(scope) { when (val model = ReferenceOr.value(resolvedSchema).toModel(name, SchemaContext.Write)) {
        is Model.Object -> {
            val params = model.properties.map { (baseName, prop) ->
                val contentType = mediaType.encoding[baseName]?.contentType?.let(ContentType::parse)
                Route.Body.Multipart.FormData(baseName, prop.model, contentType, prop.isRequired)
            }
            Route.Body.Multipart.Value(params, description, mediaType.extensions)
        }

        else -> Route.Body.Multipart.Ref(model, description, mediaType.extensions)
    } }
}

context(scope: Registry)
private suspend fun RequestBody.formUrlEncoded(
    segments: List<PathSegment>,
    method: HttpMethod,
    mediaType: MediaType,
    schema: ReferenceOr<Schema>,
): Route.Body {
    val name = NamingContext.path(segments, method).nest(NamingContext.RouteBody)
    val resolvedSchema = schema.resolveSchema()
    val obj = with(scope) {
        requireNotNull(ReferenceOr.value(resolvedSchema).toModel(name, SchemaContext.Write) as? Model.Object) {
            "Form URL encoded body must be an object. $this"
        }
    }
    val params = obj.properties.map { (baseName, prop) -> Route.Body.Multipart.FormData(baseName, prop.model, null, prop.isRequired) }
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
    val model = schema.toRequestBodyModel(name, SchemaContext.Write)
    return if (model is Model.Union && model.discriminator == null && model.context.head is NamingContext.Path) {
        Route.Body.OverloadedBody(
            contentType = ContentType.parse(contentType),
            type = model,
            description = description,
            extensions = mediaType.extensions,
        )
    } else {
        Route.Body.SetBody(
            contentType = ContentType.parse(contentType),
            type = model,
            description = description,
            extensions = mediaType.extensions,
        )
    }
}

context(ctx: Registry)
private suspend fun ReferenceOr<Schema>.toRequestBodyModel(
    name: NamingContext,
    context: SchemaContext,
): Model =
    when (this) {
        is ReferenceOr.Value<Schema> -> {
            val additionalProperties = value.additionalProperties as? AdditionalProperties.PSchema
            if (value.properties.isNullOrEmpty() && additionalProperties != null) {
                Model.Object(
                    context = name,
                    description = value.description?.valueOrNull(),
                    title = value.title,
                    properties = emptyMap(),
                    additionalProperties = Model.Object.AdditionalProperties.Schema(
                        additionalProperties.value.toModel(name.nest(NamingContext.AdditionalProperties), context)
                    ),
                    isNullable = value.nullable == true,
                )
            } else {
                toModel(name, context)
            }
        }

        is ReferenceOr.Reference -> toModel(name, context)
    }

// TODO Move to Registry and support top-level schemas.
context(ctx: Registry)
private fun ReferenceOr<RequestBody>.resolve(): RequestBody = when (this) {
    is ReferenceOr.Value -> value
    is ReferenceOr.Reference -> {
        val referenceName = ref.drop("#/components/requestBodies/".length)
        when (val requestBodies = ctx.openAPI.components.requestBodies[referenceName]) {
            is ReferenceOr.Reference -> TODO("Remote parameters not supported yet.")
            is ReferenceOr.Value<RequestBody> -> requestBodies.value
            null -> error("RequestBody $referenceName could not be found in ${ctx.openAPI.components.requestBodies}.")
        }
    }
}

context(ctx: Registry)
private fun ReferenceOr<Schema>.resolveSchema(): Schema = when (this) {
    is ReferenceOr.Value -> value
    is ReferenceOr.Reference -> {
        val referenceName = ref.drop("#/components/schemas/".length)
        when (val schemas = ctx.openAPI.components.schemas[referenceName]) {
            is ReferenceOr.Reference -> TODO("Remote schemas not supported yet.")
            is ReferenceOr.Value<Schema> -> schemas.value
            null -> error("Schema $referenceName could not be found in ${ctx.openAPI.components.schemas}.")
        }
    }
}
