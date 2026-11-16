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
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Transient
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private const val REF_KEY = "\$ref"
private const val RECURSIVE_REF_KEY = "\$recursiveRef"

/** The keys of a reference that are not [ReferenceOr.Reference.siblings]: the pointer, and the two flags it has fields for. */
private val SIBLINGS_KEPT_APART = setOf(REF_KEY, RECURSIVE_REF_KEY, "readOnly", "writeOnly")

/**
 * Siblings that are no schema, such as `required: "name"`, were always ignored, and still are: a keyword next to a
 * `$ref` never fails the document.
 */
private inline fun decodeSiblingsOrNull(decode: () -> Schema): Schema? =
  try {
    decode().takeUnless { it == Schema() }
  } catch (_: SerializationException) {
    null
  } catch (_: IllegalArgumentException) {
    null
  }

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
    /**
     * The other keywords next to a schema `$ref`, such as `required`, `nullable` or `description`; `null` when it has
     * none besides [readOnly] and [writeOnly], and for references to anything but a schema.
     */
    @Transient public val siblings: Schema? = null,
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

      /**
       * Whether [dataSerializer] decodes schemas. In a schema position the keywords next to a `$ref` are schema
       * keywords (OpenAPI 3.1, JSON Schema 2020-12) and are kept as [Reference.siblings]; elsewhere they are not.
       */
      private val isSchema: Boolean =
        dataSerializer.descriptor.serialName == Schema.serializer().descriptor.serialName

      override fun serialize(encoder: Encoder, value: ReferenceOr<T>) {
        when (value) {
          is Value -> encoder.encodeSerializableValue(dataSerializer, value.value)
          is Reference if value.siblings != null && encoder is JsonEncoder -> {
            val reference = encoder.json.encodeToJsonElement(Reference.serializer(), value).jsonObject
            val siblings = encoder.json.encodeToJsonElement(Schema.serializer(), value.siblings).jsonObject
            encoder.encodeJsonElement(JsonObject(reference + (siblings - reference.keys)))
          }
          // Other encoders (YAML) write the reference without its siblings.
          is Reference -> encoder.encodeSerializableValue(Reference.serializer(), value)
        }
      }

      override fun deserialize(decoder: Decoder): ReferenceOr<T> =
        when (decoder) {
          is JsonDecoder -> {
            val json = decoder.decodeSerializableValue(JsonElement.serializer())
            val jsobObject = json as? JsonObject
            val refKey = listOf(REF_KEY, RECURSIVE_REF_KEY).firstOrNull { jsobObject?.contains(it) == true }
            @Suppress("UnsafeCallOnNullableType")
            when {
              jsobObject != null && refKey != null ->
                Reference(
                  ref = json[refKey]!!.jsonPrimitive.content,
                  readOnly = jsobObject["readOnly"]?.jsonPrimitive?.booleanOrNull,
                  writeOnly = jsobObject["writeOnly"]?.jsonPrimitive?.booleanOrNull,
                  siblings = JsonObject(jsobObject - SIBLINGS_KEPT_APART).takeIf { isSchema && it.isNotEmpty() }
                    ?.let { siblings -> decodeSiblingsOrNull { decoder.json.decodeFromJsonElement(Schema.serializer(), siblings) } },
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
              map != null && refContentOrNull != null -> Reference(
                ref = refContentOrNull,
                readOnly = map.getOrNull("readOnly")?.yamlScalar?.content?.toBooleanStrictOrNull(),
                writeOnly = map.getOrNull("writeOnly")?.yamlScalar?.content?.toBooleanStrictOrNull(),
                siblings = YamlMap(map.entries.filterKeys { it.content !in SIBLINGS_KEPT_APART }, map.path)
                  .takeIf { isSchema && it.entries.isNotEmpty() }
                  ?.let { siblings -> decodeSiblingsOrNull { decoder.yaml.decodeFromYamlNode(Schema.serializer(), siblings) } },
              )
              else -> Value(decoder.yaml.decodeFromYamlNode(dataSerializer, node))
            }
          }

          else -> error("Unknown decoder ($decoder) cannot deserialise ReferenceOr.")
        }
    }
  }
}
