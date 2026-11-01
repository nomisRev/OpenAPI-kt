package io.github.nomisrev.openapi.parser

import com.charleskorn.kaml.YamlInput
import com.charleskorn.kaml.YamlMap
import com.charleskorn.kaml.YamlNode
import kotlin.collections.component1
import kotlin.collections.component2
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SealedSerializationApi
import kotlinx.serialization.Serializable
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

public sealed interface StatusCodeOrRange {
  /** An exact HTTP status code. */
  @JvmInline
  public value class Code(public val value: Int) : StatusCodeOrRange

  /** A response-code range such as `5XX`. */
  @JvmInline
  public value class Range(public val value: String) : StatusCodeOrRange {
    init {
      require(value.isResponseCodeRange()) { "$value is not a valid response code range" }
    }
  }
}

/**
 * A container for the expected responses of an operation. The container maps HTTP response codes
 * or ranges to their expected responses. It is not expected from the documentation to necessarily
 * cover all possible HTTP response codes, since they may not be known in advance. However, it is
 * expected from the documentation to cover a successful operation response and any known errors.
 */
@Serializable(with = Responses.Companion.Serializer::class)
public data class Responses(
  /**
   * The documentation of responses other than the ones declared for specific HTTP response codes.
   * It can be used to cover undeclared responses.
   */
  public val default: ReferenceOr<Response>? = null,
  /**
   * Maps exact HTTP status codes and status-code ranges to their expected responses.
   */
  public val responses: Map<StatusCodeOrRange, ReferenceOr<Response>>,

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
  ) : this(null, mapOf(StatusCodeOrRange.Code(statusCode) to ReferenceOr.Value(response)))

  public constructor(
    head: Pair<Int, ReferenceOr<Response>>,
    vararg responses: Pair<Int, ReferenceOr<Response>>,
  ) : this(
    null,
    (listOf(head) + responses).associate { (statusCode, response) ->
      StatusCodeOrRange.Code(statusCode) to response
    },
  )

  public operator fun plus(other: Responses): Responses =
    Responses(
      default = other.default ?: default,
      responses = responses + other.responses,
    )

  public companion object {
    internal object Serializer : KSerializer<Responses> {
      override val descriptor: SerialDescriptor = ResponsesDescriptor
      private val responseSerializer = ReferenceOr.serializer(Response.serializer())

      override fun deserialize(decoder: Decoder): Responses =
        when {
          decoder is JsonDecoder -> {
            val json = decoder.decodeSerializableValue(JsonElement.serializer()).jsonObject
            val default =
              if (json.contains("default"))
                decoder.json.decodeFromJsonElement(responseSerializer, json.getValue("default"))
              else null
            val responses = buildMap {
              json.forEach { (key, value) ->
                if (!key.startsWith("x-") && key != "default") {
                  put(key.toStatusCodeOrRange(), decoder.json.decodeFromJsonElement(responseSerializer, value))
                }
              }
            }
            val extensions = json.filter { it.key.startsWith("x-") }
            Responses(default, responses, extensions)
          }

          decoder is YamlInput -> {
            val node = decoder.decodeSerializableValue(YamlNode.serializer()) as YamlMap
            val defaultOrNull =
              node.getOrNull("default")?.let {
                decoder.yaml.decodeFromYamlNode(responseSerializer, it)
              }
            val responses = buildMap {
              node.entries.forEach { (key, value) ->
                val propertyName = key.content
                if (!propertyName.startsWith("x-") && propertyName != "default") {
                  put(
                    propertyName.toStatusCodeOrRange(),
                    decoder.yaml.decodeFromYamlNode(responseSerializer, value),
                  )
                }
              }
            }
            val extensions = buildMap {
              node.entries.forEach { (key, value) ->
                if (key.content.startsWith("x-")) {
                  put(key.content, decoder.yaml.decodeFromYamlNode(JsonElement.serializer(), value))
                }
              }
            }
            Responses(defaultOrNull, responses, extensions)
          }

          else -> error("Unsupported decoder $decoder")
        }

      override fun serialize(encoder: Encoder, value: Responses) {
        encoder as JsonEncoder
        val responses = buildMap {
          value.responses.forEach { (statusCodeOrRange, response) ->
            put(
              statusCodeOrRange.toPropertyName(),
              encoder.json.encodeToJsonElement(responseSerializer, response),
            )
          }
          value.default?.let {
            put("default", encoder.json.encodeToJsonElement(responseSerializer, it))
          }
          putAll(value.extensions)
        }
        val json = JsonObject(responses)
        encoder.encodeSerializableValue(JsonElement.serializer(), json)
      }
    }
  }
}

@OptIn(ExperimentalSerializationApi::class, SealedSerializationApi::class)
private object ResponsesDescriptor : SerialDescriptor {
  override val serialName: String = "arrow.endpoint.docs.openapi.Responses"
  override val kind: SerialKind = StructureKind.MAP
  override val elementsCount: Int = 2
  private val keyDescriptor: SerialDescriptor = String.serializer().descriptor
  private val valueDescriptor: SerialDescriptor =
    ReferenceOr.serializer(Response.serializer()).descriptor

  override fun getElementName(index: Int): String = index.toString()

  override fun getElementIndex(name: String): Int =
    when (val statusCodeOrRange = name.toStatusCodeOrRange()) {
      is StatusCodeOrRange.Code -> statusCodeOrRange.value
      is StatusCodeOrRange.Range -> statusCodeOrRange.value.substring(0, 1).toInt()
    }

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

private fun String.isResponseCodeRange(): Boolean =
  length == 3 && first() in '1'..'5' && substring(1) == "XX"

private fun String.toStatusCodeOrRange(): StatusCodeOrRange =
  toIntOrNull()?.let(StatusCodeOrRange::Code)
    ?: if (isResponseCodeRange()) StatusCodeOrRange.Range(this)
    else throw SerializationException("$this is not a valid response code or range")

private fun StatusCodeOrRange.toPropertyName(): String =
  when (this) {
    is StatusCodeOrRange.Code -> value.toString()
    is StatusCodeOrRange.Range -> value
  }
