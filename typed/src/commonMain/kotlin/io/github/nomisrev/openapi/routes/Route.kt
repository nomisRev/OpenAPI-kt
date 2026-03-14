package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.ApiTree
import io.github.nomisrev.openapi.buildTree
import io.github.nomisrev.openapi.parsePathSegments
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Server
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.Route.Bodies
import io.github.nomisrev.openapi.routes.Route.Body
import io.github.nomisrev.openapi.routes.Route.Input
import io.github.nomisrev.openapi.routes.Route.Returns
import io.github.nomisrev.openapi.transformers.nestedOrNull
import io.github.nomisrev.openapi.transformers.topLevelNames
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.JsonElement
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.flatMapTo
import kotlin.collections.mapNotNullTo
import kotlin.collections.orEmpty

class ApiModel(
    val routes: List<Route>,
    val models: List<Model>,
    val servers: List<Server>,
) {
    fun tree(name: String): ApiTree = routes.buildTree(name, servers)
    override fun toString(): String =
        routes.joinToString { "${it.method.value} ${it.path}" } + "\n" + models.joinToString {
            when (it) {
                is Model.Reference -> it.context.toString()
                is Model.DiscriminatedObject -> it.context.toString()
                is Model.Enum -> it.context.toString()
                is Model.Object -> it.context.toString()
                is Model.Union -> it.context.toString()
                is Model.ByteArray,
                is Model.Collection,
                is Model.Date,
                is Model.DateTime,
                is Model.FreeFormJson,
                is Model.Uuid,
                is Model.Primitive -> ""
            }
        }
}

private tailrec suspend fun Set<NamingContext.Reference>.topLevelModels(registry: Registry): List<Model> {
    val models = with(registry) { map { it.toModel() } }
    val newNames: Set<NamingContext.Reference> = models.flatMapTo(mutableSetOf()) { it.topLevelNames() }
    return if (newNames == this@topLevelModels) models else newNames.topLevelModels(registry)
}

suspend fun OpenAPI.toApiModel(): ApiModel =
    Registry(this).use { registry ->
        val globalServers = servers.normalizeForClientGeneration()
        val routes = with(registry) { toRoutes() }

        val models = with(registry) {
            routes.flatMapTo(mutableSetOf()) {
                it.parameters.topLevelNames() + it.returns.topLevelNames() + it.body.topLevelNames()
            }.topLevelModels(registry)
        }

        ApiModel(routes, models, globalServers)
    }

private fun List<Server>.normalizeForClientGeneration(): List<Server> {
    if (size != 1) return this
    val only = first()
    val isImplicitDefault = only.url == "/" &&
        only.description == null &&
        only.variables.isNullOrEmpty() &&
        only.extensions.isNullOrEmpty()
    return if (isImplicitDefault) emptyList() else this
}

context(ctx: Registry)
suspend fun OpenAPI.toRoutes(): List<Route> =
    paths.entries.flatMap { (path, pathItem) ->
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
    }

private fun Operation.withParams(pathParams: List<ReferenceOr<io.github.nomisrev.openapi.parser.Parameter>>): Operation =
    copy(parameters = pathParams + parameters)

context(ctx: Registry)
private suspend fun toRoute(path: String, method: HttpMethod, operation: Operation): Route {
    val params = resolveParameters(path, method, operation)
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
        deprecated = operation.deprecated
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
) {
    val path: String
        get() = segments.joinToString(separator = "/", prefix = "/") { segment ->
            when (segment) {
                is PathSegment.Literal -> segment.name
                is PathSegment.Parameter -> "{${segment.name}}"
            }
        }

    val nested: Set<Model> = body.nested() + parameters.nested() + returns.nested()

    data class Bodies(
        /** Request bodies are optional by default! */
        val required: Boolean,
        val types: Map<ContentType, Body>,
        val extensions: Map<String, JsonElement>,
    ) {
        fun defaultOrNull(): Body? =
            setBodyOrNull() ?: formUrlEncodedOrNull() ?: multipartOrNull()

        private fun setBodyOrNull(): Body.SetBody? =
            types.entries.firstNotNullOfOrNull { (key, value) ->
                val isDefault =
                    ContentType.Application.Json.match(key) ||
                            ContentType.Application.Xml.match(key) ||
                            ContentType.Application.OctetStream.match(key) ||
                            ContentType.Text.Plain.match(key)
                if (isDefault) value as? Body.SetBody else null
            }

        fun formUrlEncodedOrNull(): Body.FormUrlEncoded? =
            types.entries.firstNotNullOfOrNull { (key, value) ->
                if (ContentType.Application.FormUrlEncoded.match(key)) value as? Body.FormUrlEncoded
                else null
            }

        fun multipartOrNull(): Body.Multipart? =
            types.entries.firstNotNullOfOrNull { (key, value) ->
                if (ContentType.MultiPart.FormData.match(key)) value as? Body.Multipart else null
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
        ) : Body

        /** application/x-www-form-urlencoded body. Represented as key/value pairs. */
        data class FormUrlEncoded(
            val parameters: List<Multipart.FormData>,
            override val description: String?,
            override val extensions: Map<String, JsonElement>,
        ) : Body, List<Multipart.FormData> by parameters

        sealed interface Multipart : Body {
            data class FormData(val name: String, val type: Model)

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
        val isRequired: Boolean,
        val input: Parameter.Input,
        val description: String?,
    )

    data class Returns(
        val default: ReturnType?,
        val responses: Map<HttpStatusCode, ReturnType>,
        val extensions: Map<String, JsonElement>,
    )

    // Required
    data class ReturnType(val types: Map<ContentType, Model>, val extensions: Map<String, JsonElement>)
}

private fun Bodies?.nested(): Set<Model> =
    this?.types.orEmpty().flatMapTo(mutableSetOf()) { (_, body) ->
        when (body) {
            is Body.Multipart.Value -> body.parameters.mapNotNull { it.type.nestedOrNull() }
            is Body.Multipart.Ref -> listOfNotNull(body.value.nestedOrNull())
            is Body.FormUrlEncoded -> body.parameters.mapNotNull { it.type.nestedOrNull() }
            is Body.SetBody -> listOfNotNull(body.type.nestedOrNull())
        }
    }

private fun Bodies?.topLevelNames(): Set<NamingContext.Reference> =
    this?.types.orEmpty().flatMapTo(mutableSetOf()) { (_, body) ->
        when (body) {
            is Body.Multipart.Value -> body.parameters.flatMap { it.type.topLevelNames() }
            is Body.Multipart.Ref -> body.value.topLevelNames()
            is Body.FormUrlEncoded -> body.parameters.flatMap { it.type.topLevelNames() }
            is Body.SetBody -> body.type.topLevelNames()
        }
    }

private fun Returns.nested(): Set<Model> {
    val defaultNested = default?.types?.values.orEmpty().mapNotNullTo(mutableSetOf()) { it.nestedOrNull() }
    val responsesNested = responses.values.flatMapTo(mutableSetOf()) { returnType ->
        returnType.types.values.mapNotNull { it.nestedOrNull() }
    }
    return defaultNested + responsesNested
}

private fun Returns.topLevelNames(): Set<NamingContext.Reference> {
    val defaultNested = default?.types?.values.orEmpty().flatMapTo(mutableSetOf()) { it.topLevelNames() }
    val responsesNested = responses.values.flatMapTo(mutableSetOf()) { returnType ->
        returnType.types.values.flatMap { it.topLevelNames() }
    }
    return defaultNested + responsesNested
}

private fun List<Input>.nested(): Set<Model> = mapNotNullTo(mutableSetOf()) { it.type.nestedOrNull() }

private fun List<Input>.topLevelNames(): Set<NamingContext.Reference> =
    flatMapTo(mutableSetOf()) { it.type.topLevelNames() }
