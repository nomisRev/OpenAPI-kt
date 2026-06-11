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
 * `none` means the model will not call any tools and instead generates a message.
 * `auto` is the default value and means the model can pick between generating a message or calling one or more tools.
 * `required` means the model must call one or more tools before responding to the user.
 * Specifying a particular tool like `{"type": "file_search"}` or `{"type": "function", "function": {"name": "my_function"}}` forces the model to call that tool.
 *
 */
@Serializable(with = AssistantsApiToolChoiceOption.Serializer::class)
public sealed interface AssistantsApiToolChoiceOption {
  @Serializable
  public enum class NoneOrAutoOrRequired(
    public val `value`: String,
  ) : AssistantsApiToolChoiceOption {
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
  public value class CaseAssistantsNamedToolChoice(
    public val `value`: AssistantsNamedToolChoice,
  ) : AssistantsApiToolChoiceOption

  public object Serializer : KSerializer<AssistantsApiToolChoiceOption> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.AssistantsApiToolChoiceOption", PolymorphicKind.SEALED) {
      element("NoneOrAutoOrRequired", NoneOrAutoOrRequired.serializer().descriptor)
      element("CaseAssistantsNamedToolChoice", AssistantsNamedToolChoice.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): AssistantsApiToolChoiceOption {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        NoneOrAutoOrRequired::class to { decodeFromJsonElement(NoneOrAutoOrRequired.serializer(), it) },
        CaseAssistantsNamedToolChoice::class to { CaseAssistantsNamedToolChoice(decodeFromJsonElement(AssistantsNamedToolChoice.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: AssistantsApiToolChoiceOption) {
      when(value) {
        is NoneOrAutoOrRequired -> encoder.encodeSerializableValue(NoneOrAutoOrRequired.serializer(), value)
        is CaseAssistantsNamedToolChoice -> encoder.encodeSerializableValue(AssistantsNamedToolChoice.serializer(), value.value)
      }
    }
  }
}
