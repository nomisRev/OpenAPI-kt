package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * Controls which (if any) tool is called by the model.
 * `none` means the model will not call any tool and instead generates a message.
 * `auto` means the model can pick between generating a message or calling one or more tools.
 * `required` means the model must call one or more tools.
 * Specifying a particular tool via `{"type": "function", "function": {"name": "my_function"}}` forces the model to call that tool.
 *
 * `none` is the default when no tools are present. `auto` is the default if tools are present.
 *
 */
@Serializable(with = ChatCompletionToolChoiceOption.Serializer::class)
public sealed interface ChatCompletionToolChoiceOption {
  @Serializable
  public enum class ToolChoiceMode(
    public val `value`: String,
  ) : ChatCompletionToolChoiceOption {
    @SerialName("none")
    None("none"),
    @SerialName("auto")
    Auto("auto"),
    @SerialName("required")
    Required("required"),
    ;
  }

  @Serializable
  @JvmInline
  public value class CaseChatCompletionAllowedToolsChoice(
    public val `value`: ChatCompletionAllowedToolsChoice,
  ) : ChatCompletionToolChoiceOption

  @Serializable
  @JvmInline
  public value class CaseChatCompletionNamedToolChoice(
    public val `value`: ChatCompletionNamedToolChoice,
  ) : ChatCompletionToolChoiceOption

  @Serializable
  @JvmInline
  public value class CaseChatCompletionNamedToolChoiceCustom(
    public val `value`: ChatCompletionNamedToolChoiceCustom,
  ) : ChatCompletionToolChoiceOption

  public object Serializer : KSerializer<ChatCompletionToolChoiceOption> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.ChatCompletionToolChoiceOption", PolymorphicKind.SEALED) {
      element("ToolChoiceMode", ToolChoiceMode.serializer().descriptor)
      element("CaseChatCompletionAllowedToolsChoice", ChatCompletionAllowedToolsChoice.serializer().descriptor)
      element("CaseChatCompletionNamedToolChoice", ChatCompletionNamedToolChoice.serializer().descriptor)
      element("CaseChatCompletionNamedToolChoiceCustom", ChatCompletionNamedToolChoiceCustom.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): ChatCompletionToolChoiceOption {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        ToolChoiceMode::class to { decodeFromJsonElement(ToolChoiceMode.serializer(), it) },
        CaseChatCompletionAllowedToolsChoice::class to { CaseChatCompletionAllowedToolsChoice(decodeFromJsonElement(ChatCompletionAllowedToolsChoice.serializer(), it)) },
        CaseChatCompletionNamedToolChoice::class to { CaseChatCompletionNamedToolChoice(decodeFromJsonElement(ChatCompletionNamedToolChoice.serializer(), it)) },
        CaseChatCompletionNamedToolChoiceCustom::class to { CaseChatCompletionNamedToolChoiceCustom(decodeFromJsonElement(ChatCompletionNamedToolChoiceCustom.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: ChatCompletionToolChoiceOption) {
      when(value) {
        is ToolChoiceMode -> encoder.encodeSerializableValue(ToolChoiceMode.serializer(), value)
        is CaseChatCompletionAllowedToolsChoice -> encoder.encodeSerializableValue(ChatCompletionAllowedToolsChoice.serializer(), value.value)
        is CaseChatCompletionNamedToolChoice -> encoder.encodeSerializableValue(ChatCompletionNamedToolChoice.serializer(), value.value)
        is CaseChatCompletionNamedToolChoiceCustom -> encoder.encodeSerializableValue(ChatCompletionNamedToolChoiceCustom.serializer(), value.value)
      }
    }
  }
}
