package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
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
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateRunRequest(
  @SerialName("assistant_id")
  public val assistantId: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val model: Optional<Model> = Optional.Absent,
  @SerialName("reasoning_effort")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reasoningEffort: Optional<ReasoningEffort> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val instructions: Optional<String> = Optional.Absent,
  @SerialName("additional_instructions")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val additionalInstructions: Optional<String> = Optional.Absent,
  @SerialName("additional_messages")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val additionalMessages: Optional<List<CreateMessageRequest>> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tools: Optional<List<Tools>> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val metadata: Optional<Metadata> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val temperature: Optional<Double> = Optional.Absent,
  @SerialName("top_p")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val topP: Optional<Double> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val stream: Optional<Boolean> = Optional.Absent,
  @SerialName("max_prompt_tokens")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxPromptTokens: Optional<Long> = Optional.Absent,
  @SerialName("max_completion_tokens")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxCompletionTokens: Optional<Long> = Optional.Absent,
  @SerialName("truncation_strategy")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val truncationStrategy: Optional<TruncationObject> = Optional.Absent,
  @SerialName("tool_choice")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val toolChoice: Optional<AssistantsApiToolChoiceOption> = Optional.Absent,
  @SerialName("parallel_tool_calls")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val parallelToolCalls: ParallelToolCalls? = null,
  @SerialName("response_format")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            CaseAssistantSupportedModels::class to { CaseAssistantSupportedModels(decodeFromJsonElement(AssistantSupportedModels.serializer(), it)) },
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseAssistantSupportedModels -> encoder.encodeSerializableValue(AssistantSupportedModels.serializer(), value.value)
        }
      }
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Tools {
    @Serializable
    @SerialName("code_interpreter")
    public data object CodeInterpreter : Tools

    @SerialName("file_search")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class FileSearch(
      @SerialName("file_search")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val fileSearch: FileSearch? = null,
    ) : Tools {
      /**
       * Overrides for the file search tool.
       */
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class FileSearch(
        @SerialName("max_num_results")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val maxNumResults: Long? = null,
        @SerialName("ranking_options")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val rankingOptions: FileSearchRankingOptions? = null,
      )
    }

    @SerialName("function")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Function(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val description: String? = null,
      public val name: String,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val parameters: FunctionParameters? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val strict: Optional<Boolean> = Optional.Absent,
    ) : Tools
  }
}
