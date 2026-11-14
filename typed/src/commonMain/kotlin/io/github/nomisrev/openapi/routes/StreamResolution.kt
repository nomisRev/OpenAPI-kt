package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.enumLikeValues
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.Stream.ServerSentEvents.Event
import io.github.nomisrev.openapi.routes.Stream.ServerSentEvents.EventData
import io.github.nomisrev.openapi.routes.Stream.ServerSentEvents.EventType
import io.ktor.http.ContentType
import kotlinx.serialization.json.JsonPrimitive

private const val EVENT = "event"
private const val DATA = "data"
private const val ID = "id"
private const val RETRY = "retry"

/** Resolves the OpenAPI 3.2 `itemSchema` of a response media type of [contentType] into a [Stream]. */
context(ctx: Registry)
internal suspend fun ReferenceOr<Schema>.toStream(contentType: ContentType, name: NamingContext): Stream =
    when {
        contentType.isServerSentEvents() -> toServerSentEvents(name)
        else -> when (val framing = contentType.jsonSequenceFramingOrNull()) {
            null -> Stream.Unsupported(toModel(name, SchemaContext.Read))
            else -> Stream.JsonSequence(framing, toModel(name, SchemaContext.Read))
        }
    }

private fun ContentType.mediaType(): String = "$contentType/$contentSubtype".lowercase()

private fun ContentType.isServerSentEvents(): Boolean = mediaType() == "text/event-stream"

private fun ContentType.jsonSequenceFramingOrNull(): Stream.JsonSequence.Framing? {
    val mediaType = mediaType()
    return when {
        mediaType == "application/jsonl" || mediaType == "application/x-ndjson" ->
            Stream.JsonSequence.Framing.NewlineDelimited

        mediaType == "application/json-seq" || mediaType.endsWith("+json-seq") ->
            Stream.JsonSequence.Framing.RecordSeparated

        else -> null
    }
}

private fun String.isJsonMediaType(): Boolean {
    val mediaType = substringBefore(';').trim().lowercase()
    return mediaType == "application/json" || mediaType.endsWith("+json")
}

/**
 * All schemas that constrain one property, ordered from most to least specific (the `oneOf` branch before the
 * shared item schema). JSON Schema applies all of them; the most specific declaration of a keyword wins here.
 */
private class PropertyConstraints(val schemas: List<Pair<ReferenceOr<Schema>, Schema>>) {
    fun <A : Any> first(select: (Schema) -> A?): A? = schemas.firstNotNullOfOrNull { [_, schema] -> select(schema) }

    /** The most specific schema that describes a value (a `$ref`, or one declaring a type, enum or const). */
    fun describing(): Pair<ReferenceOr<Schema>, Schema>? =
        schemas.firstOrNull { [ref, schema] ->
            ref is ReferenceOr.Reference || schema.type != null || schema.enum != null || schema.const != null
        }
}

/** The flattened object shape of an SSE event: its property constraints and required properties. */
private class EventShape(
    val properties: Map<String, PropertyConstraints>,
    val required: Set<String>,
) {
    operator fun get(name: String): PropertyConstraints? = properties[name]
}

private data class ShapeBuilder(
    val properties: MutableMap<String, MutableList<Pair<ReferenceOr<Schema>, Schema>>> = linkedMapOf(),
    val required: MutableSet<String> = mutableSetOf(),
) {
    fun build(): EventShape = EventShape(properties.mapValues { PropertyConstraints(it.value) }, required)
}

context(ctx: Registry)
private suspend fun ReferenceOr<Schema>.peekSchema(): Schema = with(ctx.scope()) { peek() }

/** Collects `properties` and `required` of [schema] and of its `allOf` members (not of `oneOf`/`anyOf`). */
context(ctx: Registry)
private suspend fun ShapeBuilder.collect(schema: Schema, visited: Set<String>) {
    required.addAll(schema.required)
    schema.properties.orEmpty().forEach { [property, refOrSchema] ->
        properties.getOrPut(property) { mutableListOf() }.add(refOrSchema to refOrSchema.peekSchema())
    }
    schema.allOf.orEmpty().forEach { member ->
        val ref = (member as? ReferenceOr.Reference)?.ref
        if (ref == null || ref !in visited) collect(member.peekSchema(), visited + listOfNotNull(ref))
    }
}

context(ctx: Registry)
private suspend fun ReferenceOr<Schema>.toServerSentEvents(name: NamingContext): Stream.ServerSentEvents {
    val itemSchema = peekSchema()
    val discriminator = itemSchema.discriminator?.takeIf { it.propertyName == EVENT }
    val branches = itemSchema.oneOf ?: itemSchema.anyOf
    val visited = setOfNotNull((this as? ReferenceOr.Reference)?.ref)

    if (branches.isNullOrEmpty()) {
        val shape = ShapeBuilder().apply { collect(itemSchema, visited) }.build()
        return Stream.ServerSentEvents(shape.eventKinds(names = emptyList()).map { it.toEvent(name, shape) })
    }

    data class Kind(val eventName: String?, val shape: EventShape)

    val kinds = branches.flatMap { branch ->
        val branchRef = (branch as? ReferenceOr.Reference)?.ref
        val shape = ShapeBuilder().apply {
            // Branch first: it is more specific than the shared item schema.
            collect(branch.peekSchema(), visited + listOfNotNull(branchRef))
            collect(itemSchema.copy(oneOf = null, anyOf = null), visited)
        }.build()
        val discriminatedNames = branchRef?.let { ref -> discriminator?.namesFor(ref) }.orEmpty()
        shape.eventKinds(discriminatedNames).map { Kind(it, shape) }
    }

    val events = kinds.mapIndexed { index, [eventName, shape] ->
        val eventContext = if (kinds.size == 1) name else name.nest(NamingContext.UnionCase(eventName ?: "Event${index + 1}"))
        eventName.toEvent(eventContext, shape)
    }
    return Stream.ServerSentEvents(events)
}

/** Event names a discriminator maps to the branch [ref], falling back to the implicit component name. */
private fun Schema.Discriminator.namesFor(ref: String): List<String> {
    val componentName = ref.substringAfterLast('/')
    val explicit = mapping.orEmpty().filterValues { it == ref || it == componentName }.keys.toList()
    return explicit.ifEmpty { listOf(componentName) }
}

/**
 * The fixed event names of this shape: a `const`, the values of an `enum`, or the [names] mapped by a
 * discriminator. A single `null` represents an unconstrained event name.
 */
private fun EventShape.eventKinds(names: List<String>): List<String?> {
    val event = this[EVENT]
    val const = event?.first { (it.const as? JsonPrimitive)?.takeIf(JsonPrimitive::isString)?.content }
    val enum = event?.first { schema -> schema.enumLikeValues()?.filterNotNull()?.takeIf { it.isNotEmpty() } }
    return when {
        const != null -> listOf(const)
        enum != null -> enum
        names.isNotEmpty() -> names
        else -> listOf(null)
    }
}

context(ctx: Registry)
private suspend fun String?.toEvent(name: NamingContext, shape: EventShape): Event {
    val eventName = this
    val type = when (eventName) {
        null -> EventType.Unconstrained(
            model = shape[EVENT].toModel(name.nest(NamingContext.ObjectProperty(EVENT)), Schema.Type.Basic.String),
            isRequired = EVENT in shape.required,
        )

        else -> EventType.Named(eventName)
    }
    return Event(
        type = type,
        data = shape.toEventData(name.nest(NamingContext.ObjectProperty(DATA))),
        id = shape[ID]?.let { constraints ->
            Model.Object.Property(
                constraints.toModel(name.nest(NamingContext.ObjectProperty(ID)), Schema.Type.Basic.String),
                ID in shape.required,
            )
        },
        retry = shape[RETRY]?.let { constraints ->
            Model.Object.Property(
                constraints.toModel(name.nest(NamingContext.ObjectProperty(RETRY)), Schema.Type.Basic.Integer),
                RETRY in shape.required,
            )
        },
    )
}

context(ctx: Registry)
private suspend fun EventShape.toEventData(name: NamingContext): EventData {
    val data = this[DATA]
    val isRequired = DATA in required
    val contentMediaType = data?.first { it.contentMediaType }
    val contentSchema = data?.first { it.contentSchema }
    return when {
        contentMediaType != null && contentMediaType.isJsonMediaType() -> EventData.Json(
            model = contentSchema?.toModel(name, SchemaContext.Read) ?: freeFormJson(),
            contentMediaType = contentMediaType,
            isRequired = isRequired,
        )

        contentMediaType == null && data?.describing()?.second?.isTextual() == false -> EventData.Json(
            model = requireNotNull(data.describing()).first.toModel(name, SchemaContext.Read),
            contentMediaType = null,
            isRequired = isRequired,
        )

        else -> EventData.Text(
            model = data.toModel(name, Schema.Type.Basic.String),
            contentMediaType = contentMediaType,
            isRequired = isRequired,
        )
    }
}

/** Whether a schema describes a plain string, which is what SSE fields are on the wire. */
private fun Schema.isTextual(): Boolean = when (val type = type) {
    Schema.Type.Basic.String -> true
    is Schema.Type.Array -> type.types.filterNot { it == Schema.Type.Basic.Null } == listOf(Schema.Type.Basic.String)
    null -> properties == null && additionalProperties == null && items == null &&
        allOf == null && oneOf == null && anyOf == null

    else -> false
}

/**
 * The model of an SSE field. SSE fields are text on the wire (with `retry` an integer), so a schema that does
 * not declare a type is typed with [defaultType].
 */
context(ctx: Registry)
private suspend fun PropertyConstraints?.toModel(name: NamingContext, defaultType: Schema.Type.Basic): Model {
    val describing = this?.describing()
    val schema = when {
        describing == null -> ReferenceOr.value(Schema(type = defaultType))
        describing.first is ReferenceOr.Reference -> describing.first
        else -> ReferenceOr.value(describing.second.withoutContentKeywords().let { schema ->
            if (schema.type == null && schema.enum == null && schema.const == null) schema.copy(type = defaultType)
            else schema
        })
    }
    return schema.toModel(name, SchemaContext.Read)
}

private fun Schema.withoutContentKeywords(): Schema = copy(contentMediaType = null, contentSchema = null)

private fun freeFormJson(): Model =
    Model.FreeFormJson(description = null, constraint = null, isNullable = false, title = null)
