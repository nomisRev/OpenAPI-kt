package io.github.nomisrev.openapi

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
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

/**
 * The Schema Object allows the definition of input and output data types. These types can be
 * objects, but also primitives and arrays. This object is an extended subset of the
 * [JSON Schema Specification Wright Draft 00](https://json-schema.org/). For more information about
 * the properties, see [JSON Schema Core](https://tools.ietf.org/html/draft-wright-json-schema-00)
 * and [JSON Schema Validation](https://tools.ietf.org/html/draft-wright-json-schema-validation-00).
 * Unless stated otherwise, the property definitions follow the JSON Schema.
 */
@Serializable
public data class Schema(
  val title: String? = null,
  val description: ReferenceOr<String>? = null,
  /** required is an object-level attribute, not a property attribute. */
  val required: List<String>? = null,
  val nullable: Boolean? = null,
  val allOf: List<ReferenceOr<Schema>>? = null,
  val oneOf: List<ReferenceOr<Schema>>? = null,
  val not: ReferenceOr<Schema>? = null,
  val anyOf: List<ReferenceOr<Schema>>? = null,
  val properties: Map<String, ReferenceOr<Schema>>? = null,
  val additionalProperties: AdditionalProperties? = null,
  val discriminator: Discriminator? = null,
  val readOnly: Boolean? = null,
  val writeOnly: Boolean? = null,
  val xml: Xml? = null,
  val externalDocs: ExternalDocs? = null,
  val example: ExampleValue? = null,
  val deprecated: Boolean? = null,
  val maxProperties: Int? = null,
  val minProperties: Int? = null,
  /** Unlike JSON Schema this value MUST conform to the defined type for this parameter. */
  val default: DefaultValue? = null,
  val type: Type? = null,
  val format: String? = null,
  val items: ReferenceOr<Schema>? = null,
  val maximum: Double? = null,
  val exclusiveMaximum: Boolean? = null,
  val minimum: Double? = null,
  val exclusiveMinimum: Boolean? = null,
  val maxLength: Int? = null,
  val minLength: Int? = null,
  val pattern: String? = null,
  val maxItems: Int? = null,
  val minItems: Int? = null,
  val uniqueItems: Boolean? = null,
  val enum: List<String>? = null,
  val multipleOf: Double? = null,
  @SerialName("\$id") val id: String? = null,
  @SerialName("\$anchor") val anchor: String? = null,
  @SerialName("\$recursiveAnchor") val recursiveAnchor: Boolean? = null,
) {
  init {
    require(required?.isEmpty() != true) {
      "An empty list required: [] is not valid. If all properties are optional, do not specify the required keyword."
    }
  }

  @Serializable
  public data class Discriminator(
    val propertyName: String,
    val mapping: Map<String, String>? = null,
  )

  @Serializable(with = Type.Serializer::class)
  public sealed interface Type {

    public data class Array(val types: List<Basic>) : Type

    public enum class Basic(public val value: kotlin.String) : Type {
      @SerialName("array") Array("array"),
      @SerialName("object") Object("object"),
      @SerialName("number") Number("number"),
      @SerialName("boolean") Boolean("boolean"),
      @SerialName("integer") Integer("integer"),
      @SerialName("null") Null("null"),
      @SerialName("string") String("string");

      public companion object {
        public fun fromString(value: kotlin.String): Basic? =
          entries.find { it.value.equals(value, ignoreCase = true) }
      }
    }

    public object Serializer : KSerializer<Type> {
      // TODO fix descriptor
      override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("io.github.nomisrev.openapi.Schema.Type")

      override fun deserialize(decoder: Decoder): Type {
        val json = decoder.decodeSerializableValue(JsonElement.serializer())
        return when {
          json is JsonArray ->
            Array(
              decoder
                .decodeSerializableValue(ListSerializer(String.serializer()))
                .mapNotNull(Basic.Companion::fromString)
            )
          json is JsonPrimitive && json.isString ->
            Basic.fromString(json.content)
              ?: throw SerializationException("Invalid Basic.Type value: ${json.content}")
          else -> throw SerializationException("Schema.Type can only be a string or an array")
        }
      }

      override fun serialize(encoder: Encoder, value: Type) {
        when (value) {
          is Array ->
            encoder.encodeSerializableValue(
              ListSerializer(String.serializer()),
              value.types.map { it.value },
            )
          is Basic -> encoder.encodeString(value.value)
        }
      }
    }
  }
}
