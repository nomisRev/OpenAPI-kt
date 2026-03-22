package io.github.nomisrev.openapi.parser

import com.charleskorn.kaml.YamlInput
import com.charleskorn.kaml.YamlMap
import com.charleskorn.kaml.YamlNode
import com.charleskorn.kaml.yamlScalar
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive

private const val REF_KEY = "\$ref"
private const val RECURSIVE_REF_KEY = "\$recursiveRef"

/**
 * Defines Union [A] | [Reference]. A lot of types like Header, Schema, MediaType, etc. can be
 * either a direct value or a reference to a definition.
 */
@Serializable(with = ReferenceOr.Companion.Serializer::class)
public sealed interface ReferenceOr<out A> {
  @Serializable
  public data class Reference(
    @SerialName(REF_KEY) public val ref: String,
    public val readOnly: Boolean? = null,
    public val writeOnly: Boolean? = null,
  ) : ReferenceOr<Nothing>

  @JvmInline public value class Value<A>(public val value: A) : ReferenceOr<A>

  public fun valueOrNull(): A? =
    when (this) {
      is Reference -> null
      is Value -> value
    }

  public companion object {
    private const val schema: String = "#/components/schemas/"
    private const val responses: String = "#/components/responses/"
    private const val parameters: String = "#/components/parameters/"

    public fun schema(name: String): Reference = Reference("$schema$name")
    public fun parameter(name: String): Reference = Reference("$parameters$name")
    public fun response(name: String): Reference = Reference("$responses$name")

    public fun <A> value(value: A): ReferenceOr<A> = Value(value)

    internal class Serializer<T>(private val dataSerializer: KSerializer<T>) :
      KSerializer<ReferenceOr<T>> {
      @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
      override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.openapi.ReferenceOr", SerialKind.CONTEXTUAL)

      override fun serialize(encoder: Encoder, value: ReferenceOr<T>) {
        when (value) {
          is Value -> encoder.encodeSerializableValue(dataSerializer, value.value)
          is Reference -> encoder.encodeSerializableValue(Reference.serializer(), value)
        }
      }

      override fun deserialize(decoder: Decoder): ReferenceOr<T> =
        when (decoder) {
          is JsonDecoder -> {
            val json = decoder.decodeSerializableValue(JsonElement.serializer())
            val jsobObject = json as? JsonObject
            when {
              jsobObject != null && jsobObject.contains(REF_KEY) ->
                Reference(
                  ref = json[REF_KEY]!!.jsonPrimitive.content,
                  readOnly = jsobObject["readOnly"]?.jsonPrimitive?.booleanOrNull,
                  writeOnly = jsobObject["writeOnly"]?.jsonPrimitive?.booleanOrNull,
                )

              jsobObject != null && jsobObject.contains(RECURSIVE_REF_KEY) ->
                Reference(
                  ref = json[RECURSIVE_REF_KEY]!!.jsonPrimitive.content,
                  readOnly = jsobObject["readOnly"]?.jsonPrimitive?.booleanOrNull,
                  writeOnly = jsobObject["writeOnly"]?.jsonPrimitive?.booleanOrNull,
                )

              else -> Value(decoder.json.decodeFromJsonElement(dataSerializer, json))
            }
          }

          is YamlInput -> {
            val node = decoder.decodeSerializableValue(YamlNode.serializer())
            val map = node as? YamlMap

            val refContentOrNull =
              map?.getOrNull(REF_KEY)?.yamlScalar?.content
                ?: map?.getOrNull(RECURSIVE_REF_KEY)?.yamlScalar?.content

            when {
              refContentOrNull != null -> Reference(
                ref = refContentOrNull,
                readOnly = map?.getOrNull("readOnly")?.yamlScalar?.content?.toBooleanStrictOrNull(),
                writeOnly = map?.getOrNull("writeOnly")?.yamlScalar?.content?.toBooleanStrictOrNull(),
              )
              else -> Value(decoder.yaml.decodeFromYamlNode(dataSerializer, node))
            }
          }

          else -> error("Unknown decoder ($decoder) cannot deserialise ReferenceOr.")
        }
    }
  }
}
