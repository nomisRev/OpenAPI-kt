package io.github.nomisrev.openapi

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlin.jvm.JvmInline

@Serializable(with = ExampleValue.Companion.Serializer::class)
public sealed interface ExampleValue {

  @JvmInline
  public value class Single(public val value: String) : ExampleValue

  @JvmInline
  public value class Multiple(public val values: List<String>) : ExampleValue

  public companion object {

    internal class Serializer : KSerializer<ExampleValue> {
      private val multipleSerializer = ListSerializer(String.serializer())

      // TODO implement proper SerialDescriptor
      override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("arrow.endpoint.docs.openapi.ExampleValueSerializer")

      override fun serialize(encoder: Encoder, value: ExampleValue) {
        when (value) {
          is Single -> encoder.encodeString(value.value)
          is Multiple ->
            encoder.encodeSerializableValue(multipleSerializer, value.values)
        }
      }

      override fun deserialize(decoder: Decoder): ExampleValue {
        return when (val json = decoder.decodeSerializableValue(JsonElement.serializer())) {
          is JsonArray -> Multiple(decoder.decodeSerializableValue(multipleSerializer))
          is JsonPrimitive -> Single(json.content)
          else -> throw SerializationException("ExampleValue can only be a primitive or an array, found $json")
        }
      }
    }

    public operator fun invoke(v: String): ExampleValue = Single(v)
//    @Suppress("UNCHECKED_CAST")
//    public operator fun invoke(codec: Codec<*, *, *>, e: Any?): ExampleValue? =
//      invoke(codec.schema(), (codec as Codec<*, Any?, *>).encode(e))

//    public operator fun invoke(schema: arrow.endpoint.Schema<*>, raw: Any?): ExampleValue? =
//      when (raw) {
//        is Iterable<*> ->
//          when (schema) {
//            is arrow.endpoint.Schema.List -> Multiple(raw.map(Any?::toString))
//            else -> raw.firstOrNull()?.let { Single(it.toString()) }
//          }
//        is Option<*> -> raw.fold({ null }) { Single(it.toString()) }
//        null -> null
//        else -> Single(raw.toString())
//      }
  }
}