package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Root
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.Route.Bodies
import io.github.nomisrev.openapi.routes.Route.Body
import io.github.nomisrev.openapi.routes.Route.Input
import io.github.nomisrev.openapi.routes.Route.Returns
import io.github.nomisrev.openapi.sort
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

class ApiModel(
    val routes: List<Route>,
    val models: List<Model>
) {
    fun root(name: String): Root = routes.sort(name)
    override fun toString(): String =
        routes.joinToString { it.operationId } + "\n" + models.joinToString {
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

suspend fun OpenAPI.toApiModel(): ApiModel {
    val registry = Registry(this)
    val routes = with(registry) { endpoints().map { it.toRoute() } }
    val seen = registry.names()
    val models = with(registry) { seen.map { it.toModel() } }
    return ApiModel(routes, models)
}

context(ctx: Registry)
suspend fun Endpoint.toRoute(): Route = Route(
    operationId = operationId,
    summary = operation.summary,
    path = path,
    method = method,
    parameters = parameters(),
    body = bodies(),
    returns = returns(),
    extensions = operation.extensions,
    deprecated = operation.deprecated
)

data class Route(
    val operationId: String,
    val summary: String?,
    val path: String,
    val method: HttpMethod,
    val body: Bodies?,
    val parameters: List<Input>,
    val returns: Returns,
    val extensions: Map<String, JsonElement>,
    val deprecated: Boolean,
) {
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

private fun Returns.nested(): Set<Model> {
    val defaultNested = default?.types?.values.orEmpty().mapNotNullTo(mutableSetOf()) { it.nestedOrNull() }
    val responsesNested = responses.values.flatMapTo(mutableSetOf()) { returnType ->
        returnType.types.values.mapNotNull { it.nestedOrNull() }
    }
    return defaultNested + responsesNested
}

private fun List<Input>.nested(): Set<Model> = mapNotNullTo(mutableSetOf()) { it.type.nestedOrNull() }
