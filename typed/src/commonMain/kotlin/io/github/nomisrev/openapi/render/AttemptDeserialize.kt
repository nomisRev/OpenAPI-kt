package io.github.nomisrev.openapi.render

private val tripleQuote = "\"\"\""

val attemptDeserialize = $$"""
package io.github.nomisrev.model
    
import kotlinx.serialization.SerializationException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlin.reflect.KClass

class UnionSerializationException(
    val payload: JsonElement,
    val errors: Map<KClass<*>, IllegalArgumentException>,
) : SerializationException(
    $$tripleQuote
        Failed to deserialize Json: $payload.
        Errors:
        ${
        errors.entries.joinToString(separator = "\n") { (type, error) ->
            "$type - failed to deserialize: ${error.stackTraceToString()}"
        }
    }$$tripleQuote.trimIndent()
)

fun <A> Json.attemptDeserialize(json: JsonElement, vararg block: Pair<KClass<*>, Json.(JsonElement) -> A>): A {
    val errors = linkedMapOf<KClass<*>, IllegalArgumentException>()
    block.forEach { (kclass, parse) ->
        try {
            return parse(json)
        } catch (e: IllegalArgumentException) {
            errors[kclass] = e
        }
    }
    throw UnionSerializationException(json, errors)
}

fun JsonObjectBuilder.putAll(jsonObject: JsonObject?) =
    jsonObject.orEmpty().forEach { (key, value) -> put(key, value) }
    
fun <Wrapped, Value> ValueClassSerializer(
    unwrap: (Wrapped) -> Value,
    wrap: (Value) -> Wrapped,
    valueSerializer: KSerializer<Value>
) = object : KSerializer<Wrapped> {
    override val descriptor: SerialDescriptor = valueSerializer.descriptor
    override fun deserialize(decoder: Decoder): Wrapped = wrap(decoder.decodeSerializableValue(valueSerializer))
    override fun serialize(encoder: Encoder, value: Wrapped) =
        encoder.encodeSerializableValue(valueSerializer, unwrap(value))
}
""".trimMargin()
