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

/**
 * Developer-provided instructions that the model should follow, regardless of
 * messages sent by the user. With o1 models and newer, use `developer` messages
 * for this purpose instead.
 *
 */
@Serializable
public data class ChatCompletionRequestSystemMessage(
  public val content: Content,
  public val role: Role,
  public val name: String? = null,
) {
  /**
   * The contents of the system message.
   */
  @Serializable(with = Content.Serializer::class)
  public sealed interface Content {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Content

    @Serializable
    @JvmInline
    public value class CaseChatCompletionRequestMessageContentPartTextList(
      public val `value`: List<ChatCompletionRequestMessageContentPartText>,
    ) : Content

    public object Serializer : KSerializer<Content> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.ChatCompletionRequestSystemMessage.Content", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseChatCompletionRequestMessageContentPartTextList", ListSerializer(ChatCompletionRequestMessageContentPartText.serializer()).descriptor)
      }

      override fun deserialize(decoder: Decoder): Content {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseChatCompletionRequestMessageContentPartTextList::class to { CaseChatCompletionRequestMessageContentPartTextList(decodeFromJsonElement(ListSerializer(ChatCompletionRequestMessageContentPartText.serializer()), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Content) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseChatCompletionRequestMessageContentPartTextList -> encoder.encodeSerializableValue(ListSerializer(ChatCompletionRequestMessageContentPartText.serializer()), value.value)
        }
      }
    }
  }

  @Serializable
  public enum class Role(
    public val `value`: String,
  ) {
    @SerialName("system")
    System("system"),
    ;
  }
}
