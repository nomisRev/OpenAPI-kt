package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.parser.MediaType
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.PathItem
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

private const val SCHEMAS = "#/components/schemas/"

/**
 * The component schemas the payloads of the webhooks refer to, as the consumer uses them: a webhook is a request the
 * API sends, so its request body is read and its responses are written. No client call is generated for a webhook.
 * An inline payload that would need a class of its own has no name to generate it under; it is reported, and the
 * components it refers to are generated.
 */
internal fun OpenAPI.webhookModelNames(registry: Registry): Set<NamingContext.Reference> {
    val names = mutableSetOf<NamingContext.Reference>()
    webhooks.forEach { [webhook, item] ->
        val pathItem = when (item) {
            is ReferenceOr.Value -> item.value
            is ReferenceOr.Reference -> components.pathItems[item.ref.substringAfterLast('/')]?.valueOrNull()
        }
        if (pathItem == null) {
            registry.warn("Webhook '$webhook': the path item ${(item as ReferenceOr.Reference).ref} is not found; its payload is not generated.")
            return@forEach
        }
        pathItem.operations().forEach { [method, operation] ->
            val use = "Webhook '$webhook' ${method.uppercase()}"
            val body = operation.requestBody?.let { body ->
                when (body) {
                    is ReferenceOr.Value -> body.value
                    is ReferenceOr.Reference -> components.requestBodies[body.ref.substringAfterLast('/')]?.valueOrNull()
                }
            }
            body?.content?.payloads("$use request body", SchemaContext.Read, registry, names)
            val responses = listOfNotNull(operation.responses.default) + operation.responses.responses.values
            responses.forEach { response ->
                val value = when (response) {
                    is ReferenceOr.Value -> response.value
                    is ReferenceOr.Reference -> components.responses[response.ref.substringAfterLast('/')]?.valueOrNull()
                }
                value?.content?.payloads("$use response", SchemaContext.Write, registry, names)
            }
        }
    }
    return names
}

private fun PathItem.operations(): List<Pair<String, Operation>> = listOfNotNull(
    get?.let { "get" to it }, put?.let { "put" to it }, post?.let { "post" to it }, delete?.let { "delete" to it },
    options?.let { "options" to it }, head?.let { "head" to it }, patch?.let { "patch" to it }, trace?.let { "trace" to it },
)

private fun Map<String, MediaType>.payloads(
    use: String,
    context: SchemaContext,
    registry: Registry,
    names: MutableSet<NamingContext.Reference>,
) = forEach { [mediaType, media] ->
    val schema = media.schema ?: return@forEach
    if (schema is ReferenceOr.Value && schema.value.needsAClass()) {
        registry.warn("$use ($mediaType): the inline schema is not generated, it has no name; only the schemas it refers to are.")
    }
    schema.collectComponentNames(context, registry, names)
}

/** Whether this inline schema is generated as a class of its own: an object with properties, an enum or a union. */
private fun Schema.needsAClass(): Boolean =
    !properties.isNullOrEmpty() || !enum.isNullOrEmpty() || !oneOf.isNullOrEmpty() || !anyOf.isNullOrEmpty() ||
        allOf.orEmpty().any { it is ReferenceOr.Value && it.value.needsAClass() } ||
        (items as? ReferenceOr.Value)?.value?.needsAClass() == true

private fun ReferenceOr<Schema>.collectComponentNames(
    context: SchemaContext,
    registry: Registry,
    names: MutableSet<NamingContext.Reference>,
) {
    when (this) {
        is ReferenceOr.Reference -> if (ref.startsWith(SCHEMAS)) names += registry.componentReference(ref.removePrefix(SCHEMAS), context)
        is ReferenceOr.Value -> with(value) {
            (listOfNotNull(items, (additionalProperties as? AdditionalProperties.PSchema)?.value) +
                properties.orEmpty().values + allOf.orEmpty() + oneOf.orEmpty() + anyOf.orEmpty())
                .forEach { it.collectComponentNames(context, registry, names) }
        }
    }
}

/**
 * The component schemas no operation or webhook refers to, directly or through other components (responses,
 * parameters, request bodies, schemas, discriminator mappings), as one warning. They are not generated: only the
 * schemas the client uses are. Callbacks are not generated, so what they refer to counts as unreferenced.
 */
internal fun OpenAPI.unreferencedSchemasWarning(): String? {
    if (components.schemas.isEmpty()) return null
    val document = toJsObject()
    val components = document["components"] as? JsonObject ?: JsonObject(emptyMap())
    val visited = mutableSetOf<Pair<String, String>>()
    val pending = ArrayDeque<JsonElement>(listOfNotNull(document["paths"], document["webhooks"]))
    while (pending.isNotEmpty()) {
        pending.removeLast().references().forEach { [section, name] ->
            if (visited.add(section to name)) (components[section] as? JsonObject)?.get(name)?.let(pending::addLast)
        }
    }
    val unreferenced = this.components.schemas.keys.filter { ("schemas" to it) !in visited }
    if (unreferenced.isEmpty()) return null
    val shown = unreferenced.take(MAX_LISTED).joinToString()
    val more = if (unreferenced.size > MAX_LISTED) ", and ${unreferenced.size - MAX_LISTED} more" else ""
    val [count, verb] = if (unreferenced.size == 1) "1 component schema" to "is" else "${unreferenced.size} component schemas" to "are"
    return "$count $verb not referenced by any operation or webhook and $verb not generated: $shown$more"
}

private const val MAX_LISTED = 25

/** The components this JSON refers to, as (section, name): `$ref`s and discriminator mapping values. Callbacks are skipped. */
private fun JsonElement.references(): List<Pair<String, String>> = buildList {
    fun visit(element: JsonElement) {
        when (element) {
            is JsonArray -> element.forEach(::visit)
            is JsonObject -> element.forEach { [key, value] ->
                when {
                    key == "callbacks" -> Unit
                    key == "\$ref" && value is JsonPrimitive && value.isString -> value.content.componentOrNull()?.let(::add)
                    key == "discriminator" && value is JsonObject ->
                        (value["mapping"] as? JsonObject)?.values?.forEach { target ->
                            if (target is JsonPrimitive && target.isString) {
                                add(target.content.componentOrNull() ?: ("schemas" to target.content))
                            }
                        }
                    else -> visit(value)
                }
            }
            is JsonPrimitive -> Unit
        }
    }
    visit(this@references)
}

/** `#/components/schemas/Pet/properties/id` is a use of (schemas, Pet). */
private fun String.componentOrNull(): Pair<String, String>? {
    if (!startsWith("#/components/")) return null
    val parts = removePrefix("#/components/").split('/')
    if (parts.size < 2) return null
    return parts[0] to parts[1].replace("~1", "/").replace("~0", "~")
}
