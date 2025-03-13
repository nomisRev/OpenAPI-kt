package io.github.nomisrev.openapi

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SealedSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

/**
 * A container for the expected responses of an operation. The container maps a HTTP response code
 * to the expected response. It is not expected from the documentation to necessarily cover all
 * possible HTTP response codes, since they may not be known in advance. However, it is expected
 * from the documentation to cover a successful operation response and any known errors.
 */
@Serializable(with = Responses.Companion.Serializer::class)
public data class Responses(
  /**
   * The documentation of responses other than the ones declared for specific HTTP response codes.
   * It can be used to cover undeclared responses.
   */
  public val default: ReferenceOr<Response>? = null,
  /**
   * Any HTTP status code can be used as the property name (one property per HTTP status code).
   * Describes the expected response for those HTTP status codes.
   */
  public val responses: Map<Int, ReferenceOr<Response>>,
  /**
   * Any additional external documentation for this OpenAPI document. The key is the name of the
   * extension (beginning with x-), and the value is the data. The value can be a [JsonNull],
   * [JsonPrimitive], [JsonArray] or [JsonObject].
   */
  public val extensions: Map<String, JsonElement> = emptyMap(),
) {

  public constructor(
    statusCode: Int,
    response: Response,
  ) : this(null, mapOf(statusCode to ReferenceOr.Value(response)))

  public constructor(
    head: Pair<Int, ReferenceOr<Response>>,
    vararg responses: Pair<Int, ReferenceOr<Response>>,
  ) : this(null, mapOf(head) + responses)

  public operator fun plus(other: Responses): Responses =
    Responses(other.default ?: default, responses + other.responses)

  public companion object {
    internal object Serializer : KSerializer<Responses> {
      override val descriptor: SerialDescriptor = ResponsesDescriptor
      private val responseSerializer = ReferenceOr.serializer(Response.serializer())
      private val responsesSerializer = MapSerializer(Int.serializer(), responseSerializer)

      override fun deserialize(decoder: Decoder): Responses {
        decoder as JsonDecoder
        val json = decoder.decodeSerializableValue(JsonElement.serializer()).jsonObject
        val default =
          if (json.contains("default"))
            decoder.json.decodeFromJsonElement(responseSerializer, json.getValue("default"))
          else null
        val responsesJs = json.filterNot { it.key.startsWith("x-") || it.key == "default" }
        val responses =
          if (responsesJs.isNotEmpty())
            decoder.json.decodeFromJsonElement(responsesSerializer, JsonObject(responsesJs))
          else emptyMap()
        val extensions = json.filter { it.key.startsWith("x-") }
        return Responses(default, responses, extensions)
      }

      override fun serialize(encoder: Encoder, value: Responses) {
        encoder as JsonEncoder
        val default =
          value.default?.let {
            encoder.json
              .encodeToJsonElement(ReferenceOr.serializer(Response.serializer()), it)
              .jsonObject
          }
        val responses =
          encoder.json.encodeToJsonElement(responsesSerializer, value.responses).jsonObject
        val json = JsonObject((default ?: emptyMap()) + responses + value.extensions)
        encoder.encodeSerializableValue(JsonElement.serializer(), json)
      }
    }
  }
}

@OptIn(SealedSerializationApi::class)
private object ResponsesDescriptor : SerialDescriptor {
  override val serialName: String = "arrow.endpoint.docs.openapi.Responses"
  override val kind: SerialKind = StructureKind.MAP
  override val elementsCount: Int = 2
  private val keyDescriptor: SerialDescriptor = Int.serializer().descriptor
  private val valueDescriptor: SerialDescriptor =
    ReferenceOr.serializer(Response.serializer()).descriptor

  override fun getElementName(index: Int): String = index.toString()

  override fun getElementIndex(name: String): Int =
    name.toIntOrNull() ?: throw IllegalArgumentException("$name is not a valid list index")

  override fun isElementOptional(index: Int): Boolean {
    require(index >= 0) { "Illegal index $index, $serialName expects only non-negative indices" }
    return false
  }

  override fun getElementAnnotations(index: Int): List<Annotation> {
    require(index >= 0) { "Illegal index $index, $serialName expects only non-negative indices" }
    return emptyList()
  }

  override fun getElementDescriptor(index: Int): SerialDescriptor {
    require(index >= 0) { "Illegal index $index, $serialName expects only non-negative indices" }
    return when (index % 2) {
      0 -> keyDescriptor
      1 -> valueDescriptor
      else -> error("Unreached")
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is ResponsesDescriptor) return false
    if (serialName != serialName) return false
    if (keyDescriptor != keyDescriptor) return false
    if (valueDescriptor != valueDescriptor) return false
    return true
  }

  override fun hashCode(): Int {
    var result = serialName.hashCode()
    result = 31 * result + keyDescriptor.hashCode()
    result = 31 * result + valueDescriptor.hashCode()
    return result
  }

  override fun toString(): String = "$serialName($keyDescriptor, $valueDescriptor)"
}
