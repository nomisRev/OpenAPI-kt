@file:Suppress("TooManyFunctions")
package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.isFlattenablePathUnion
import io.github.nomisrev.openapi.parsePathSegments
import io.github.nomisrev.openapi.toPathSegment
import io.github.nomisrev.openapi.wireValue
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.PathItem
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.StatusCodeOrRange
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.resolveComponent
import io.github.nomisrev.openapi.routes.Route.Bodies
import io.github.nomisrev.openapi.routes.Route.Body
import io.github.nomisrev.openapi.routes.Route.Input
import io.github.nomisrev.openapi.routes.Route.Returns
import io.github.nomisrev.openapi.transformers.nestedOrNull
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.JsonElement
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.flatMapTo
import kotlin.collections.mapNotNullTo
import kotlin.collections.orEmpty

context(ctx: Registry)
suspend fun OpenAPI.toRoutes(): List<Route> =
    paths.paths.entries.flatMap { [path, declared] ->
        val pathItem = declared.resolve(path, components.pathItems)
        val pathParams = pathItem.parameters
        listOfNotNull(
            pathItem.get?.let { toRoute(path, HttpMethod.Get, it.withParams(pathParams)) },
            pathItem.put?.let { toRoute(path, HttpMethod.Put, it.withParams(pathParams)) },
            pathItem.post?.let { toRoute(path, HttpMethod.Post, it.withParams(pathParams)) },
            pathItem.delete?.let { toRoute(path, HttpMethod.Delete, it.withParams(pathParams)) },
            pathItem.head?.let { toRoute(path, HttpMethod.Head, it.withParams(pathParams)) },
            pathItem.options?.let { toRoute(path, HttpMethod.Options, it.withParams(pathParams)) },
            pathItem.trace?.let { toRoute(path, HttpMethod("Trace"), it.withParams(pathParams)) },
            pathItem.patch?.let { toRoute(path, HttpMethod.Patch, it.withParams(pathParams)) },
        )
    }.flatMap { route ->
        route.expandFiniteEnumPathSegments()
    }

/**
 * Resolves a path item that is a `$ref` to a `components.pathItems` entry, following aliases of path item components
 * (see [resolveComponent]). OpenAPI leaves fields declared on both the referencing and the referenced path item
 * undefined; the referencing item is the more specific one, so its own fields take precedence: its summary,
 * description and servers replace the referenced ones, and its path-level parameters are added after the referenced
 * ones, so one with the same `(name, in)` overrides the referenced one like an operation parameter overrides a
 * path-level one (see [resolveParameters]). Operations are combined, and an operation declared on both fails rather
 * than one of them being dropped.
 */
private fun PathItem.resolve(path: String, components: Map<String, ReferenceOr<PathItem>>): PathItem {
    val ref = ref ?: return this
    val target = ReferenceOr.Reference(ref).resolveComponent("pathItems", components)
    fun Operation?.merge(method: String, referenced: Operation?): Operation? {
        require(this == null || referenced == null) {
            "Path $path declares $method both inline and in its referenced path item $ref."
        }
        return this ?: referenced
    }
    return PathItem(
        ref = null,
        summary = summary ?: target.summary,
        description = description ?: target.description,
        get = get.merge("get", target.get),
        put = put.merge("put", target.put),
        post = post.merge("post", target.post),
        delete = delete.merge("delete", target.delete),
        options = options.merge("options", target.options),
        head = head.merge("head", target.head),
        patch = patch.merge("patch", target.patch),
        trace = trace.merge("trace", target.trace),
        servers = servers ?: target.servers,
        parameters = target.parameters + parameters,
        extensions = target.extensions + extensions,
    )
}

private fun Operation.withParams(pathParams: List<ReferenceOr<io.github.nomisrev.openapi.parser.Parameter>>): Operation =
    copy(parameters = pathParams + parameters)

context(ctx: Registry)
private suspend fun toRoute(path: String, method: HttpMethod, operation: Operation): Route {
    val [params, ignoredParameters] = resolveParameters(path, method, operation)
    val pathParamTypes = params
        .filter { it.input == Parameter.Input.Path }
        .associate { it.name to it.type }
    val segments = parsePathSegments(path, pathParamTypes)

    return Route(
        summary = operation.summary,
        segments = segments,
        method = method,
        parameters = params,
        body = resolveBodies(path, segments, method, operation),
        returns = resolveReturns(path, segments, method, operation),
        extensions = operation.extensions,
        deprecated = operation.deprecated,
        ignoredParameters = ignoredParameters,
    )
}

data class Route(
    val summary: String?,
    val segments: List<PathSegment>,
    val method: HttpMethod,
    val body: Bodies?,
    val parameters: List<Input>,
    val returns: Returns,
    val extensions: Map<String, JsonElement>,
    val deprecated: Boolean,
    /**
     * The `Accept`, `Content-Type` and `Authorization` header parameters the operation declares, which OpenAPI says
     * SHALL be ignored (see [isIgnoredHeader]). They are not [parameters]; they are kept so the generator can report
     * what their definitions declared.
     */
    val ignoredParameters: List<Parameter> = emptyList(),
) {
    val path: String
        get() = segments.joinToString(separator = "/", prefix = "/") { segment ->
            when (segment) {
                is PathSegment.FixedValue -> segment.wireValue
                is PathSegment.Literal -> segment.name
                is PathSegment.Parameter -> "{${segment.name}}"
                is PathSegment.OverloadedParameter -> "{${segment.name}}"
            }
        }

    val nested: Set<Model> = body.nested() + parameters.nested() + returns.nested()

    data class Bodies(
        /** Request bodies are optional by default! */
        val required: Boolean,
        val types: Map<ContentType, Body>,
        val extensions: Map<String, JsonElement>,
    ) {
        data class Variant(
            val contentType: ContentType,
            val body: Body,
        )

        fun variants(): List<Variant> = types.entries.map { [contentType, body] ->
            Variant(contentType, body)
        }

        /**
         * The body an operation renders when its request bodies do not each get their own overload: the first JSON
         * (including `+json`), XML, octet-stream or plain-text body, else the form or multipart body, else the first
         * declared body, which is sent with its own media type. A request body is never dropped for its media type.
         */
        fun defaultOrNull(): Body? =
            defaultBodyOrNull() ?: formUrlEncodedOrNull() ?: multipartOrNull() ?: types.values.firstOrNull()

        private fun defaultBodyOrNull(): Body? =
            types.entries.firstNotNullOfOrNull { (key, value) ->
                val mediaType = key.withoutParameters()
                val isDefault =
                    mediaType.isJsonLike() ||
                            mediaType.match(ContentType.Application.Xml) ||
                            mediaType.match(ContentType.Application.OctetStream) ||
                            mediaType.match(ContentType.Text.Plain)
                if (isDefault) value else null
            }

        fun formUrlEncodedOrNull(): Body.FormUrlEncoded? =
            types.entries.firstNotNullOfOrNull { (key, value) ->
                if (key.match(ContentType.Application.FormUrlEncoded)) value as? Body.FormUrlEncoded
                else null
            }

        fun multipartOrNull(): Body.Multipart? =
            types.entries.firstNotNullOfOrNull { (key, value) ->
                if (key.match(ContentType.MultiPart.Any)) value as? Body.Multipart else null
            }
    }

    sealed interface Body {
        val description: String?
        val extensions: Map<String, JsonElement>

        /**
         * Generic body sent using setBody(...). Includes JSON, XML, octet-stream and other encodings
         * that are directly supported by Ktor serialization/plugins.
         */
        data class SetBody(
            val contentType: ContentType,
            val type: Model,
            override val description: String?,
            override val extensions: Map<String, JsonElement>,
        ) : Body {
            /**
             * The media type range the caller chooses this body's `Content-Type` in, or `null` when [contentType] is
             * the media type the body is sent as. Bytes declared under a range that admits neither media type the
             * client sends bytes as (`application/octet-stream`, `text/plain`; see [requestContentType]), such as
             * any `image` subtype, are sent verbatim: only the caller knows whether they are a PNG or a JPEG, so the
             * caller supplies the concrete media type within this range. A model other than bytes under such a range
             * has no encoding as any of its media types and is not sendable.
             */
            val callerContentTypeRange: ContentType?
                get() = contentType.takeIf { type is Model.ByteArray && it.isWildcard() }
        }

        /**
         * A request body whose inline non-discriminated union has been marked for future overload
         * rendering while still carrying the underlying union model for phased renderer migration.
         */
        data class OverloadedBody(
            val contentType: ContentType,
            val type: Model.Union,
            override val description: String?,
            override val extensions: Map<String, JsonElement>,
        ) : Body {
            val cases: List<Model.Union.Case>
                get() = type.cases
        }

        /** application/x-www-form-urlencoded body. Represented as key/value pairs. */
        data class FormUrlEncoded(
            val parameters: List<Multipart.FormData>,
            val unsupportedFields: List<String> = emptyList(),
            override val description: String?,
            override val extensions: Map<String, JsonElement>,
        ) : Body, List<Multipart.FormData> by parameters {
            val isSupported: Boolean
                get() = unsupportedFields.isEmpty()
        }

        sealed interface Multipart : Body {
            data class FormData(
                val name: String,
                val type: Model,
                val contentType: ContentType? = null,
                val isRequired: Boolean,
            )

            // Inline schemas for multipart bodies do not generate a type,
            // they should be defined as functions parameters.
            data class Value(
                val parameters: List<FormData>,
                override val description: String?,
                override val extensions: Map<String, JsonElement>,
            ) : Multipart

            // Top-level references get a top-level type.
            data class Ref(
                val value: Model,
                override val description: String?,
                override val extensions: Map<String, JsonElement>,
            ) : Multipart
        }
    }

    data class Input(
        val name: String,
        val type: Model,
        val contentType: ContentType?,
        val isRequired: Boolean,
        val input: Parameter.Input,
        val description: String?,
    )

    data class Returns(
        val default: ReturnType?,
        val responses: Map<HttpStatusCode, ReturnType>,
        val extensions: Map<String, JsonElement>,
        val responseRanges: Map<StatusCodeOrRange.Range, ReturnType> = emptyMap(),
    )

    /**
     * The content of a response, per media type.
     *
     * @property types the models of media types that declare a `schema`, describing the complete content.
     * @property streams OpenAPI 3.2: the media types that declare an `itemSchema`, describing every item of a
     * sequential media type such as `application/jsonl` or `text/event-stream`. A media type that declares both
     * `schema` and `itemSchema` appears in both maps, since they describe the same content at different levels.
     */
    data class ReturnType(
        val types: Map<ContentType, Model>,
        val extensions: Map<String, JsonElement>,
        val streams: Map<ContentType, Stream> = emptyMap(),
    ) {
        /** All models of this response: whole-content models and every model referenced by [streams]. */
        val models: List<Model>
            get() = types.values + streams.values.flatMap(Stream::models)
    }
}

private fun Bodies?.nested(): Set<Model> =
    this?.types.orEmpty().flatMapTo(mutableSetOf()) { [_, body] ->
        when (body) {
            is Body.Multipart.Value -> body.parameters.mapNotNull { it.type.nestedOrNull() }
            is Body.Multipart.Ref -> listOfNotNull(body.value.nestedOrNull())
            is Body.FormUrlEncoded -> body.parameters.mapNotNull { it.type.nestedOrNull() }
            is Body.SetBody -> listOfNotNull(body.type.nestedOrNull())
            is Body.OverloadedBody -> body.cases.mapNotNull { it.model.nestedOrNull() }
        }
    }

private fun Returns.nested(): Set<Model> {
    val defaultNested = default?.models.orEmpty().mapNotNullTo(mutableSetOf()) { it.nestedOrNull() }
    val responsesNested = responses.values.flatMapTo(mutableSetOf()) { returnType ->
        returnType.models.mapNotNull { it.nestedOrNull() }
    }
    val responseRangesNested = responseRanges.values.flatMapTo(mutableSetOf()) { returnType ->
        returnType.models.mapNotNull { it.nestedOrNull() }
    }
    return defaultNested + responsesNested + responseRangesNested
}

private fun List<Input>.nested(): Set<Model> =
    flatMapTo(mutableSetOf()) { input ->
        input.type.nestedOrNull()?.let { setOf(it) } ?: emptySet()
    }

private data class ResolvedPathEnumCase(
    val original: Model.Union.Case,
    val enumModel: Model.Enum?,
)

context(ctx: Registry)
private suspend fun Route.expandFiniteEnumPathSegments(index: Int = 0): List<Route> {
    if (index >= segments.size) return listOf(this)

    val expanded = expandFiniteEnumPathSegmentAt(index)
    return if (expanded == null) {
        expandFiniteEnumPathSegments(index + 1)
    } else {
        expanded.flatMap { route ->
            route.expandFiniteEnumPathSegments(index + 1)
        }
    }
}

@Suppress("ReturnCount")
context(ctx: Registry)
private suspend fun Route.expandFiniteEnumPathSegmentAt(index: Int): List<Route>? {
    val segment = segments[index]
    if (segment is PathSegment.Literal || segment is PathSegment.FixedValue) return null

    val pathInput = parameters.firstOrNull { it.input == Parameter.Input.Path && it.name == segment.name }
    val model = pathInput?.type ?: when (segment) {
        is PathSegment.Parameter -> segment.model
        is PathSegment.OverloadedParameter -> segment.type
    }

    val fixedEnum = model.closedEnumOrNull()
    if (fixedEnum != null) {
        return fixedEnum.values
            .map { it.wireValue() }
            .distinct()
            .map { wireValue ->
                replacePathSegment(
                    index = index,
                    paramName = segment.name,
                    segment = PathSegment.FixedValue(wireValue = wireValue, sourceName = segment.name),
                    replacementType = null,
                )
            }
    }

    val union = model as? Model.Union ?: return null
    val cases = union.resolvedPathEnumCases()
    if (cases.none { it.enumModel != null }) return null

    val fixedRoutes: List<Route> = cases
        .flatMap { case ->
            case.enumModel
                ?.values
                .orEmpty()
                .map { enumValue ->
                    val wireValue = enumValue.wireValue()
                    replacePathSegment(
                        index = index,
                        paramName = segment.name,
                        segment = PathSegment.FixedValue(wireValue = wireValue, sourceName = segment.name),
                        replacementType = null,
                    )
                }
        }
        .distinctBy { route -> "${route.method.value} ${route.path}" }

    val dynamicCases = cases
        .filter { it.enumModel == null }
        .map(ResolvedPathEnumCase::original)

    val dynamicRoute = union.rebuildDynamicPathSegment(segment.name, dynamicCases)?.let { [replacementSegment, replacementType] ->
        replacePathSegment(
            index = index,
            paramName = segment.name,
            segment = replacementSegment,
            replacementType = replacementType,
        )
    }

    return fixedRoutes + listOfNotNull(dynamicRoute)
}

private fun Route.replacePathSegment(
    index: Int,
    paramName: String,
    segment: PathSegment,
    replacementType: Model?,
): Route =
    copy(
        segments = segments.replaceAt(index, segment),
        parameters = parameters.mapNotNull { input ->
            if (input.input != Parameter.Input.Path || input.name != paramName) {
                input
            } else {
                replacementType?.let { type -> input.copy(type = type, contentType = null) }
            }
        }
    )

private fun List<PathSegment>.replaceAt(index: Int, segment: PathSegment): List<PathSegment> =
    mapIndexed { currentIndex, current ->
        if (currentIndex == index) segment else current
    }

context(ctx: Registry)
private suspend fun Model.closedEnumOrNull(): Model.Enum? = when (this) {
    is Model.Enum -> this
    is Model.Reference -> {
        val reference = context.head as? NamingContext.Reference ?: return null
        with(ctx) { reference.toModel() as? Model.Enum }
    }

    is Model.ByteArray,
    is Model.Collection,
    is Model.Dictionary,
    is Model.Date,
    is Model.DateTime,
    is Model.DiscriminatedObject,
    is Model.FreeFormJson,
    is Model.Object,
    is Model.Primitive,
    is Model.AnyOf,
    is Model.OneOf,
    is Model.Uuid -> null
}

context(ctx: Registry)
private suspend fun Model.Union.resolvedPathEnumCases(): List<ResolvedPathEnumCase> =
    cases.map { case ->
        ResolvedPathEnumCase(case, case.model.closedEnumOrNull())
    }

private fun Model.Union.rebuildDynamicPathSegment(
    paramName: String,
    dynamicCases: List<Model.Union.Case>,
): Pair<PathSegment, Model>? {
    if (dynamicCases.isEmpty()) return null
    val dynamicModel = if (dynamicCases.size == 1) {
        dynamicCases.single().model
    } else {
        when (this) {
            is Model.OneOf -> copy(cases = dynamicCases)
            is Model.AnyOf -> copy(cases = dynamicCases)
        }
    }

    val dynamicSegment = when (dynamicModel) {
        is Model.Union -> {
            if (dynamicModel.isFlattenablePathUnion()) {
                PathSegment.OverloadedParameter(paramName, dynamicModel)
            } else {
                PathSegment.Parameter(paramName, dynamicModel)
            }
        }

        is Model.ByteArray,
        is Model.Collection,
        is Model.Dictionary,
        is Model.Date,
        is Model.DateTime,
        is Model.DiscriminatedObject,
        is Model.Enum,
        is Model.FreeFormJson,
        is Model.Object,
        is Model.Primitive,
        is Model.Reference,
        is Model.Uuid -> dynamicModel.toPathSegment(paramName)
    }

    return dynamicSegment to dynamicModel
}
