package io.github.nomisrev.render.test.client.discriminated.subtype.redeclaration

import java.lang.IllegalArgumentException
import kotlin.Any
import kotlin.Pair
import kotlin.String
import kotlin.collections.Map
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer

public class UnionSerializationException(
  public val payload: JsonElement,
  public val errors: Map<KClass<*>, IllegalArgumentException>,
) : SerializationException("Failed to deserialize Json: " + payload + """
|.
|Errors:
|""".trimMargin() + errors.entries.joinToString("""
|
|""".trimMargin()) { it.key.toString() + " - failed to deserialize: " + it.value.stackTraceToString() })

public fun <A> Json.attemptDeserialize(json: JsonElement, vararg block: Pair<KClass<*>, Json.(JsonElement) -> A>): A {
  val errors = linkedMapOf<KClass<*>, IllegalArgumentException>()
  block.forEach {
    try {
      return it.second.invoke(this@attemptDeserialize, json)
    } catch (e: IllegalArgumentException) {
      errors[it.first] = e
    }
  }
  throw UnionSerializationException(json, errors)
}

public fun JsonObjectBuilder.putAll(jsonObject: JsonObject?) {
  jsonObject.orEmpty().forEach { (key, value) -> put(key, value) }
}

public fun <Wrapped, Value> ValueClassSerializer(
  unwrap: (Wrapped) -> Value,
  wrap: (Value) -> Wrapped,
  valueSerializer: KSerializer<Value>,
): KSerializer<Wrapped> = object : KSerializer<Wrapped> {
  override val descriptor: SerialDescriptor = valueSerializer.descriptor
  override fun deserialize(decoder: Decoder): Wrapped = wrap(decoder.decodeSerializableValue(valueSerializer))
  override fun serialize(encoder: Encoder, value: Wrapped) = encoder.encodeSerializableValue(valueSerializer, unwrap(value))
}

/**
 * The serializer of a case of a discriminated base: [serializer], with the property [discriminator] set to [tag]. A value of another case, or without its tag, is an error.
 */
public open class TaggedCaseSerializer<T : Any>(
  serializer: KSerializer<T>,
  private val discriminator: String,
  private val tag: String,
) : JsonTransformingSerializer<T>(serializer) {
  override fun transformSerialize(element: JsonElement): JsonElement = JsonObject(mapOf(discriminator to JsonPrimitive(tag)) + (element as JsonObject))

  override fun transformDeserialize(element: JsonElement): JsonElement {
    val found = element.discriminatorValue(discriminator)
    if (found != tag) {
      throw SerializationException("""Expected '$discriminator' to be '$tag', but it was ${found?.let { "'$it'" } ?: "absent"}""")
    }
    return JsonObject(element as JsonObject - discriminator)
  }
}

internal fun JsonElement.discriminatorValue(discriminator: String): String? {
  val value = (this as? JsonObject ?: throw SerializationException("""Expected a JSON object with '$discriminator', but was $this"""))[discriminator]
  return (value as? JsonPrimitive)?.takeUnless { it is JsonNull }?.content
}
