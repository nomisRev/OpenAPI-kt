package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable
public data class CreateRunRequest(
  @SerialName("assistant_id")
  public val assistantId: String,
  public val model: Model? = null,
  @SerialName("reasoning_effort")
  public val reasoningEffort: ReasoningEffort? = null,
  public val instructions: String? = null,
  @SerialName("additional_instructions")
  public val additionalInstructions: String? = null,
  @SerialName("additional_messages")
  public val additionalMessages: List<CreateMessageRequest>? = null,
  public val tools: List<Tools>? = null,
  public val metadata: Metadata? = null,
  public val temperature: Double? = null,
  @SerialName("top_p")
  public val topP: Double? = null,
  public val stream: Boolean? = null,
  @SerialName("max_prompt_tokens")
  public val maxPromptTokens: Long? = null,
  @SerialName("max_completion_tokens")
  public val maxCompletionTokens: Long? = null,
  @SerialName("truncation_strategy")
  public val truncationStrategy: TruncationObject? = null,
  @SerialName("tool_choice")
  public val toolChoice: AssistantsApiToolChoiceOption? = null,
  @SerialName("parallel_tool_calls")
  public val parallelToolCalls: ParallelToolCalls? = null,
  @SerialName("response_format")
  public val responseFormat: AssistantsApiResponseFormatOption? = null,
) {
  /**
   * The ID of the [Model](/docs/api-reference/models) to be used to execute this run. If a value is provided here, it will override the model associated with the assistant. If not, the model associated with the assistant will be used.
   */
  @Serializable(with = Model.Serializer::class)
  public sealed interface Model {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Model

    @Serializable
    @JvmInline
    public value class CaseAssistantSupportedModels(
      public val `value`: AssistantSupportedModels,
    ) : Model

    public object Serializer : KSerializer<Model> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.CreateRunRequest.Model", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseAssistantSupportedModels", AssistantSupportedModels.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Model {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseAssistantSupportedModels::class to { CaseAssistantSupportedModels(decodeFromJsonElement(AssistantSupportedModels.serializer(), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseAssistantSupportedModels -> encoder.encodeSerializableValue(AssistantSupportedModels.serializer(), value.value)
        }
      }
    }
  }

  @Serializable(with = Tools.Serializer::class)
  public sealed interface Tools {
    @Serializable
    @JvmInline
    public value class CaseAssistantToolsCode(
      public val `value`: AssistantToolsCode,
    ) : Tools

    @Serializable
    @JvmInline
    public value class CaseAssistantToolsFileSearch(
      public val `value`: AssistantToolsFileSearch,
    ) : Tools

    @Serializable
    @JvmInline
    public value class CaseAssistantToolsFunction(
      public val `value`: AssistantToolsFunction,
    ) : Tools

    public object Serializer : KSerializer<Tools> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.CreateRunRequest.Tools", PolymorphicKind.SEALED) {
        element("CaseAssistantToolsCode", AssistantToolsCode.serializer().descriptor)
        element("CaseAssistantToolsFileSearch", AssistantToolsFileSearch.serializer().descriptor)
        element("CaseAssistantToolsFunction", AssistantToolsFunction.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Tools {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseAssistantToolsCode::class to { CaseAssistantToolsCode(decodeFromJsonElement(AssistantToolsCode.serializer(), it)) },
          CaseAssistantToolsFileSearch::class to { CaseAssistantToolsFileSearch(decodeFromJsonElement(AssistantToolsFileSearch.serializer(), it)) },
          CaseAssistantToolsFunction::class to { CaseAssistantToolsFunction(decodeFromJsonElement(AssistantToolsFunction.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Tools) {
        when(value) {
          is CaseAssistantToolsCode -> encoder.encodeSerializableValue(AssistantToolsCode.serializer(), value.value)
          is CaseAssistantToolsFileSearch -> encoder.encodeSerializableValue(AssistantToolsFileSearch.serializer(), value.value)
          is CaseAssistantToolsFunction -> encoder.encodeSerializableValue(AssistantToolsFunction.serializer(), value.value)
        }
      }
    }
  }
}
