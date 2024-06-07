package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.OpenAPI.Companion.Json
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.jvm.JvmInline

private const val RefKey = "\$ref"

public fun Reference(prefix: String, ref: String): ReferenceOr.Reference =
  ReferenceOr.Reference("$prefix$ref")

/**
 * Defines Union [A] | [Reference].
 * A lot of types like Header, Schema, MediaType, etc. can be either a direct value or a reference to a definition.
 */
@Serializable(with = ReferenceOr.Companion.Serializer::class)
public sealed interface ReferenceOr<out A> {
  @Serializable
  public data class Reference(@SerialName(RefKey) public val ref: String) : ReferenceOr<Nothing>

  @JvmInline
  public value class Value<A>(public val value: A) : ReferenceOr<A>

  public fun valueOrNull(): A? =
    when (this) {
      is Reference -> null
      is Value -> value
    }

  public fun isValue(): Boolean =
    when (this) {
      is Reference -> false
      is Value -> true
    }

  public companion object {
    private const val schema: String = "#/components/schemas/"
    private const val responses: String = "#/components/responses/"
    private const val parameters: String = "#/components/parameters/"
    private const val requestBodies: String = "#/components/requestBodies/"
    private const val pathItems: String = "#/components/pathItems/"

    public fun schema(name: String): Reference =
      Reference("$schema$name")

    public fun <A> value(value: A): ReferenceOr<A> =
      Value(value)

    public operator fun invoke(prefix: String, ref: String): Reference = Reference("$prefix$ref")

    internal class Serializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<ReferenceOr<T>> {

      private val refDescriptor = buildClassSerialDescriptor("Reference") { element<String>(RefKey) }

      override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("arrow.endpoint.docs.openapi.Referenced") {
          element("Ref", refDescriptor)
          element("description", dataSerializer.descriptor)
        }

      override fun serialize(encoder: Encoder, value: ReferenceOr<T>) {
        when (value) {
          is Value -> encoder.encodeSerializableValue(dataSerializer, value.value)
          is Reference -> encoder.encodeSerializableValue(Reference.serializer(), value)
        }
      }

      override fun deserialize(decoder: Decoder): ReferenceOr<T> {
        val json = decoder.decodeSerializableValue(JsonElement.serializer())
        return if ((json as JsonObject).contains(RefKey)) Reference(json[RefKey]!!.jsonPrimitive.content)
        else Value(Json.decodeFromJsonElement(dataSerializer, json))
      }
    }
  }
}
