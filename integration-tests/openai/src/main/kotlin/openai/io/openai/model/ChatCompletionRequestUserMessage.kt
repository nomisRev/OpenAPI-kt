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
 * Messages sent by an end user, containing prompts or additional context
 * information.
 *
 */
@Serializable
public data class ChatCompletionRequestUserMessage(
  public val content: Content,
  public val role: Role,
  public val name: String? = null,
) {
  /**
   * The contents of the user message.
   *
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
    public value class CaseChatCompletionRequestUserMessageContentPartList(
      public val `value`: List<ChatCompletionRequestUserMessageContentPart>,
    ) : Content

    public object Serializer : KSerializer<Content> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.ChatCompletionRequestUserMessage.Content", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseChatCompletionRequestUserMessageContentPartList", ListSerializer(ChatCompletionRequestUserMessageContentPart.serializer()).descriptor)
      }

      override fun deserialize(decoder: Decoder): Content {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseChatCompletionRequestUserMessageContentPartList::class to { CaseChatCompletionRequestUserMessageContentPartList(decodeFromJsonElement(ListSerializer(ChatCompletionRequestUserMessageContentPart.serializer()), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Content) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseChatCompletionRequestUserMessageContentPartList -> encoder.encodeSerializableValue(ListSerializer(ChatCompletionRequestUserMessageContentPart.serializer()), value.value)
        }
      }
    }
  }

  @Serializable
  public enum class Role(
    public val `value`: String,
  ) {
    @SerialName("user")
    User("user"),
    ;
  }
}
