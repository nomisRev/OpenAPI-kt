package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.UnionDispatch
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.parser.Encoding
import io.github.nomisrev.openapi.parser.MediaType
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.RequestBody
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Style
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.resolveComponent
import io.github.nomisrev.openapi.registry.toModel
import io.ktor.http.BadContentTypeFormatException
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
    val typedBodies = content.entries.mapNotNull { [contentType, mediaType] ->
        val schema = mediaType.schema
        if (schema == null) {
            // Only `itemSchema` (OpenAPI 3.2): a stream of items, which request bodies do not model.
            if (mediaType.itemSchema != null) return@mapNotNull null
            // A key that is no media type (plaid.com has `examples` next to `application/json`) describes no body.
            val declared = try {
                ContentType.parse(contentType)
            } catch (_: BadContentTypeFormatException) {
                ctx.warn(
                    "Request body of ${method.value} $path has the content key '$contentType', which is not a " +
                        "media type and has no schema. It is ignored."
                )
                return@mapNotNull null
            }
            return@mapNotNull Pair(declared, schemalessBody(segments, method, declared, mediaType))
        }
        val declared = ContentType.parse(contentType)
        val body = when {
            declared.isMultipartFormData() -> formDataToBody(segments, method, mediaType, schema)
            declared.match(ContentType.Application.FormUrlEncoded) ->
                formUrlEncoded(segments, method, mediaType, schema)

            else -> toBody(segments, method, declared, mediaType, schema)
        }
        Pair(declared, body)
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

/**
 * The body of a media type declared without `schema`: any JSON and a string are sent like the body of a hand-written
 * schema, and the caller builds the body of any other media type ([Route.Body.CallerSet]). `example` and `examples`
 * never decide the type.
 */
context(scope: Registry)
private suspend fun RequestBody.schemalessBody(
    segments: List<PathSegment>,
    method: HttpMethod,
    declared: ContentType,
    mediaType: MediaType,
): Route.Body {
    val name = NamingContext.path(segments, method).nest(NamingContext.RouteBody)
    val model = declared.schemalessModelOrNull(name, SchemaContext.Write)
        ?: return Route.Body.CallerSet(declared, description, mediaType.extensions)
    return Route.Body.SetBody(declared.requestContentType(model), model, description, mediaType.extensions)
}

/**
 * `multipart/form-data`, or the range of every `multipart` subtype, of which only `form-data` has a form rendering.
 * The declared media type is matched as a pattern: the any-media-type and any-`application`-subtype ranges also admit
 * `multipart/form-data`, but a body declared under them is sent as the media type its model has
 * ([requestContentType]), not as a form.
 */
private fun ContentType.isMultipartFormData(): Boolean =
    match(ContentType.MultiPart.FormData) || (contentType == "multipart" && contentSubtype == "*")

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
            val params = model.properties.map { [baseName, prop] ->
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
    // A form field is a string on the wire, so an untyped one is a `JsonPrimitive` (see [asParameterValue]).
    val properties = obj.properties.mapValues { [baseName, prop] ->
        prop.copy(model = prop.model.asParameterValue(resolvedSchema.properties?.get(baseName)))
    }
    val styles = obj.properties.keys.mapNotNull { baseName ->
        mediaType.encoding[baseName]?.formStyleOrNull()?.let { baseName to it }
    }.toMap()
    val unsupportedFields = with(scope) {
        properties.mapNotNull { [baseName, prop] ->
            val encoding = mediaType.encoding[baseName]
            val style = if (encoding == null) Route.Body.FormStyle.FormExploded else styles[baseName]
            if (prop.model.isFormUrlEncodable(encoding, style)) null else baseName
        }
    }
    val params = properties.map { [baseName, prop] -> Route.Body.Multipart.FormData(baseName, prop.model, null, prop.isRequired) }
    return Route.Body.FormUrlEncoded(params, unsupportedFields, description, mediaType.extensions, styles)
}

context(scope: Registry)
private suspend fun RequestBody.toBody(
    segments: List<PathSegment>,
    method: HttpMethod,
    declared: ContentType,
    mediaType: MediaType,
    schema: ReferenceOr<Schema>,
): Route.Body {
    val name = NamingContext.path(segments, method).nest(NamingContext.RouteBody)
    val resolvedSchema = schema.resolveSchema()
    val model = schema.toRequestBodyModel(name, SchemaContext.Write)
    val contentType = declared.requestContentType(model)
    val hasExplicitDiscriminator = resolvedSchema.discriminator != null
    val isStructuralPathUnion =
        model is Model.Union &&
                model.context.head is NamingContext.Path &&
                model.dispatch is UnionDispatch.Structural
    return if (isStructuralPathUnion && !hasExplicitDiscriminator) {
        Route.Body.OverloadedBody(
            contentType = contentType,
            type = model,
            description = description,
            extensions = mediaType.extensions,
        )
    } else {
        Route.Body.SetBody(
            contentType = contentType,
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
            val normalizedValue = value.normalizedInlineRequestBodySchema()
            val additionalProperties = normalizedValue.additionalProperties as? AdditionalProperties.PSchema
            if (normalizedValue.properties.isNullOrEmpty() && additionalProperties != null) {
                Model.Object(
                    context = name,
                    description = normalizedValue.description?.valueOrNull(),
                    title = normalizedValue.title,
                    properties = emptyMap(),
                    additionalProperties = Model.Object.AdditionalProperties.Schema(
                        additionalProperties.value.toModel(name.nest(NamingContext.AdditionalProperties), context)
                    ),
                    isNullable = normalizedValue.nullable == true,
                )
            } else {
                ReferenceOr.value(normalizedValue).toModel(name, context)
            }
        }

        is ReferenceOr.Reference -> toModel(name, context)
    }

// Client operations are not ready to expose typed object composites as request-body unions yet.
private fun Schema.normalizedInlineRequestBodySchema(): Schema =
    if (type == Schema.Type.Basic.Object && (allOf != null || oneOf != null || anyOf != null)) {
        copy(
            allOf = null,
            oneOf = null,
            anyOf = null,
            discriminator = null,
        )
    } else {
        this
    }

context(ctx: Registry)
private fun ReferenceOr<RequestBody>.resolve(): RequestBody =
    resolveComponent("requestBodies", ctx.openAPI.components.requestBodies)

/**
 * The schema of a request body, following local aliases (`Alias: {$ref: X}`) and failing on cyclic ones,
 * like every other `$ref` the [Registry] resolves.
 */
context(ctx: Registry)
internal suspend fun ReferenceOr<Schema>.resolveSchema(): Schema = with(ctx.scope()) { peek() }

/**
 * The [Route.Body.FormStyle] an Encoding Object declares for a form field, or `null` when it declares none that is
 * known. `style` defaults to `form`, whose `explode` defaults to `true` (OpenAPI 3.1.1, 4.8.15.2); the parser keeps
 * an absent `explode` as `null`, so the default is applied here.
 */
private fun Encoding.formStyleOrNull(): Route.Body.FormStyle? {
    val style = if (style == null) Style.form else Style.entries.firstOrNull { it.name == style } ?: return null
    return formStyleOf(style, explode ?: (style == Style.form))
}

/**
 * Whether a form field of this model has a serialization the client generates, as the Encoding Object of the field
 * describes it (OpenAPI 3.1.1, 4.8.15): a field is serialized like a query parameter of its `style` and `explode`,
 * `form` and `explode: true` by default.
 *
 * - A scalar is its value, so it is supported whatever its style.
 * - An array of scalars is repeated keys (`form`, `explode: true`), or one value joined by `,` (`form`,
 *   `explode: false`), a space (`spaceDelimited`) or `|` (`pipeDelimited`). `deepObject` defines no array.
 * - An object of scalar properties, and nothing else, is one key per property (`form`, `explode: true`) or one
 *   `name[key]` per property (`deepObject`). It needs an explicit `style` or `explode`: without one, its default is
 *   its `contentType`, `application/json`, a JSON string the client does not send.
 */
context(scope: Registry)
private suspend fun Model.isFormUrlEncodable(encoding: Encoding?, style: Route.Body.FormStyle?): Boolean {
    val model = formModel() ?: return false
    if (model.isFormScalar()) return true
    return when (model) {
        is Model.Collection ->
            style != null && style != Route.Body.FormStyle.DeepObject &&
                !model.inner.isNullable && model.inner.formModel()?.isFormScalar() == true

        is Model.Object ->
            encoding != null && (encoding.style != null || encoding.explode != null) &&
                (style == Route.Body.FormStyle.FormExploded || style == Route.Body.FormStyle.DeepObject) &&
                model.additionalProperties == Model.Object.AdditionalProperties.False &&
                model.properties.values.all { it.model.formModel()?.isFormScalar() == true }

        else -> false
    }
}

/**
 * The model a form field or parameter of this model is serialized as: a [Model.Reference] is the model it names.
 */
context(scope: Registry)
internal suspend fun Model.formModel(visited: Set<NamingContext.Reference> = emptySet()): Model? = when (this) {
    is Model.Reference -> {
        val reference = context.head as? NamingContext.Reference
        if (reference == null || reference in visited) null
        else with(scope) { reference.toModel() }.formModel(visited + reference)
    }

    else -> this
}

/**
 * A model whose form or parameter value is a single string: a primitive, date, UUID, enum or `JsonPrimitive`, or a wrapper around one.
 */
context(scope: Registry)
internal suspend fun Model.isFormScalar(visited: Set<NamingContext.Reference> = emptySet()): Boolean = when (this) {
    is Model.Date,
    is Model.DateTime,
    is Model.Enum,
    is Model.Primitive.Boolean,
    is Model.Primitive.Double,
    is Model.Primitive.Float,
    is Model.Primitive.Int,
    is Model.Primitive.Long,
    is Model.Primitive.String,
    is Model.Uuid -> true

    // A `JsonPrimitive` ([asParameterValue]): its string is its `content`. Any other JSON has no string form.
    is Model.FreeFormJson -> isPrimitive

    is Model.ByteArray,
    is Model.RawResponse,
    is Model.Collection,
    is Model.Dictionary,
    is Model.DiscriminatedObject,
    is Model.OneOf,
    is Model.AnyOf,
    is Model.Primitive.Unit -> false

    is Model.Object ->
        isScalarWrapper &&
            additionalProperties == Model.Object.AdditionalProperties.False &&
            properties.size == 1 &&
            properties["value"]?.let { property ->
                property.isRequired && property.model.isFormScalar(visited)
            } == true

    is Model.Reference -> {
        val reference = context.head as? NamingContext.Reference
        if (reference == null || reference in visited) false
        else with(scope) { reference.toModel() }.isFormScalar(visited + reference)
    }
}
