package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable
public data class CompactResponseMethodPublicBody(
  public val model: ModelIdsCompaction,
  public val input: Input? = null,
  @SerialName("previous_response_id")
  public val previousResponseId: String? = null,
  public val instructions: String? = null,
  @SerialName("prompt_cache_key")
  public val promptCacheKey: String? = null,
) {
  /**
   * Text, image, or file inputs to the model, used to generate a response
   */
  @Serializable(with = Input.Serializer::class)
  public sealed interface Input {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Input

    @Serializable
    @JvmInline
    public value class CaseInputItemList(
      public val `value`: List<InputItem>,
    ) : Input

    public object Serializer : KSerializer<Input> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.CompactResponseMethodPublicBody.Input", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseInputItemList", ListSerializer(InputItem.serializer()).descriptor)
      }

      override fun deserialize(decoder: Decoder): Input {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseInputItemList::class to { CaseInputItemList(decodeFromJsonElement(ListSerializer(InputItem.serializer()), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Input) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseInputItemList -> encoder.encodeSerializableValue(ListSerializer(InputItem.serializer()), value.value)
        }
      }
    }
  }
}
