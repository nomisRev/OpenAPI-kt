package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable(with = ChatCompletionRequestUserMessageContentPart.Serializer::class)
public sealed interface ChatCompletionRequestUserMessageContentPart {
  @Serializable
  @JvmInline
  public value class CaseChatCompletionRequestMessageContentPartText(
    public val `value`: ChatCompletionRequestMessageContentPartText,
  ) : ChatCompletionRequestUserMessageContentPart

  @Serializable
  @JvmInline
  public value class CaseChatCompletionRequestMessageContentPartImage(
    public val `value`: ChatCompletionRequestMessageContentPartImage,
  ) : ChatCompletionRequestUserMessageContentPart

  @Serializable
  @JvmInline
  public value class CaseChatCompletionRequestMessageContentPartAudio(
    public val `value`: ChatCompletionRequestMessageContentPartAudio,
  ) : ChatCompletionRequestUserMessageContentPart

  @Serializable
  @JvmInline
  public value class CaseChatCompletionRequestMessageContentPartFile(
    public val `value`: ChatCompletionRequestMessageContentPartFile,
  ) : ChatCompletionRequestUserMessageContentPart

  public object Serializer : KSerializer<ChatCompletionRequestUserMessageContentPart> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.ChatCompletionRequestUserMessageContentPart", PolymorphicKind.SEALED) {
      element("CaseChatCompletionRequestMessageContentPartText", ChatCompletionRequestMessageContentPartText.serializer().descriptor)
      element("CaseChatCompletionRequestMessageContentPartImage", ChatCompletionRequestMessageContentPartImage.serializer().descriptor)
      element("CaseChatCompletionRequestMessageContentPartAudio", ChatCompletionRequestMessageContentPartAudio.serializer().descriptor)
      element("CaseChatCompletionRequestMessageContentPartFile", ChatCompletionRequestMessageContentPartFile.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): ChatCompletionRequestUserMessageContentPart {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseChatCompletionRequestMessageContentPartText::class to { CaseChatCompletionRequestMessageContentPartText(decodeFromJsonElement(ChatCompletionRequestMessageContentPartText.serializer(), it)) },
        CaseChatCompletionRequestMessageContentPartImage::class to { CaseChatCompletionRequestMessageContentPartImage(decodeFromJsonElement(ChatCompletionRequestMessageContentPartImage.serializer(), it)) },
        CaseChatCompletionRequestMessageContentPartAudio::class to { CaseChatCompletionRequestMessageContentPartAudio(decodeFromJsonElement(ChatCompletionRequestMessageContentPartAudio.serializer(), it)) },
        CaseChatCompletionRequestMessageContentPartFile::class to { CaseChatCompletionRequestMessageContentPartFile(decodeFromJsonElement(ChatCompletionRequestMessageContentPartFile.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: ChatCompletionRequestUserMessageContentPart) {
      when(value) {
        is CaseChatCompletionRequestMessageContentPartText -> encoder.encodeSerializableValue(ChatCompletionRequestMessageContentPartText.serializer(), value.value)
        is CaseChatCompletionRequestMessageContentPartImage -> encoder.encodeSerializableValue(ChatCompletionRequestMessageContentPartImage.serializer(), value.value)
        is CaseChatCompletionRequestMessageContentPartAudio -> encoder.encodeSerializableValue(ChatCompletionRequestMessageContentPartAudio.serializer(), value.value)
        is CaseChatCompletionRequestMessageContentPartFile -> encoder.encodeSerializableValue(ChatCompletionRequestMessageContentPartFile.serializer(), value.value)
      }
    }
  }
}
