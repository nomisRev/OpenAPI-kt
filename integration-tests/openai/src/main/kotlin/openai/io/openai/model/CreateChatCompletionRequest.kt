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
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable
public data class CreateChatCompletionRequest(
  public val messages: List<ChatCompletionRequestMessage>? = null,
  public val model: ModelIdsShared? = null,
  public val modalities: ResponseModalities? = null,
  public val verbosity: Verbosity? = null,
  @SerialName("reasoning_effort")
  public val reasoningEffort: ReasoningEffort? = null,
  @SerialName("max_completion_tokens")
  public val maxCompletionTokens: Long? = null,
  @SerialName("frequency_penalty")
  public val frequencyPenalty: Double? = null,
  @SerialName("presence_penalty")
  public val presencePenalty: Double? = null,
  @SerialName("web_search_options")
  public val webSearchOptions: WebSearchOptions? = null,
  @SerialName("top_logprobs")
  public val topLogprobs: Long? = null,
  @SerialName("response_format")
  public val responseFormat: ResponseFormat? = null,
  public val audio: Audio? = null,
  public val store: Boolean? = null,
  public val stream: Boolean? = null,
  public val stop: StopConfiguration? = null,
  @SerialName("logit_bias")
  public val logitBias: List<Long>? = null,
  public val logprobs: Boolean? = null,
  @SerialName("max_tokens")
  public val maxTokens: Long? = null,
  public val n: Long? = null,
  public val prediction: PredictionContent? = null,
  public val seed: Long? = null,
  @SerialName("stream_options")
  public val streamOptions: ChatCompletionStreamOptions? = null,
  public val tools: List<Tools>? = null,
  @SerialName("tool_choice")
  public val toolChoice: ChatCompletionToolChoiceOption? = null,
  @SerialName("parallel_tool_calls")
  public val parallelToolCalls: ParallelToolCalls? = null,
  @SerialName("function_call")
  public val functionCall: FunctionCall? = null,
  public val functions: List<ChatCompletionFunctions>? = null,
) {
  /**
   * Parameters for audio output. Required when audio output is requested with
   * `modalities: ["audio"]`. [Learn more](/docs/guides/audio).
   *
   */
  @Serializable
  public data class Audio(
    public val voice: VoiceIdsOrCustomVoice,
    public val format: Format,
  ) {
    @Serializable
    public enum class Format(
      public val `value`: String,
    ) {
      @SerialName("wav")
      Wav("wav"),
      @SerialName("aac")
      Aac("aac"),
      @SerialName("mp3")
      Mp3("mp3"),
      @SerialName("flac")
      Flac("flac"),
      @SerialName("opus")
      Opus("opus"),
      @SerialName("pcm16")
      Pcm16("pcm16"),
      ;
    }
  }

  /**
   * Deprecated in favor of `tool_choice`.
   *
   * Controls which (if any) function is called by the model.
   *
   * `none` means the model will not call a function and instead generates a
   * message.
   *
   * `auto` means the model can pick between generating a message or calling a
   * function.
   *
   * Specifying a particular function via `{"name": "my_function"}` forces the
   * model to call that function.
   *
   * `none` is the default when no functions are present. `auto` is the default
   * if functions are present.
   *
   */
  @Serializable(with = FunctionCall.Serializer::class)
  public sealed interface FunctionCall {
    @Serializable
    public enum class NoneOrAuto(
      public val `value`: String,
    ) : FunctionCall {
      @SerialName("none")
      None("none"),
      @SerialName("auto")
      Auto("auto"),
      ;
    }

    @Serializable
    @JvmInline
    public value class CaseChatCompletionFunctionCallOption(
      public val `value`: ChatCompletionFunctionCallOption,
    ) : FunctionCall

    public object Serializer : KSerializer<FunctionCall> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.CreateChatCompletionRequest.FunctionCall", PolymorphicKind.SEALED) {
        element("NoneOrAuto", NoneOrAuto.serializer().descriptor)
        element("CaseChatCompletionFunctionCallOption", ChatCompletionFunctionCallOption.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): FunctionCall {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          NoneOrAuto::class to { decodeFromJsonElement(NoneOrAuto.serializer(), it) },
          CaseChatCompletionFunctionCallOption::class to { CaseChatCompletionFunctionCallOption(decodeFromJsonElement(ChatCompletionFunctionCallOption.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: FunctionCall) {
        when(value) {
          is NoneOrAuto -> encoder.encodeSerializableValue(NoneOrAuto.serializer(), value)
          is CaseChatCompletionFunctionCallOption -> encoder.encodeSerializableValue(ChatCompletionFunctionCallOption.serializer(), value.value)
        }
      }
    }
  }

  /**
   * An object specifying the format that the model must output.
   *
   * Setting to `{ "type": "json_schema", "json_schema": {...} }` enables
   * Structured Outputs which ensures the model will match your supplied JSON
   * schema. Learn more in the [Structured Outputs
   * guide](/docs/guides/structured-outputs).
   *
   * Setting to `{ "type": "json_object" }` enables the older JSON mode, which
   * ensures the message the model generates is valid JSON. Using `json_schema`
   * is preferred for models that support it.
   *
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface ResponseFormat {
    @Serializable
    @JvmInline
    @SerialName("ResponseFormatText")
    public value class ResponseFormatText(
      public val `value`: io.openai.model.ResponseFormatText,
    ) : ResponseFormat

    @Serializable
    @JvmInline
    @SerialName("ResponseFormatJsonSchema")
    public value class ResponseFormatJsonSchema(
      public val `value`: io.openai.model.ResponseFormatJsonSchema,
    ) : ResponseFormat

    @Serializable
    @JvmInline
    @SerialName("ResponseFormatJsonObject")
    public value class ResponseFormatJsonObject(
      public val `value`: io.openai.model.ResponseFormatJsonObject,
    ) : ResponseFormat
  }

  @Serializable(with = Tools.Serializer::class)
  public sealed interface Tools {
    @Serializable
    @JvmInline
    public value class CaseChatCompletionTool(
      public val `value`: ChatCompletionTool,
    ) : Tools

    @Serializable
    @JvmInline
    public value class CaseCustomToolChatCompletions(
      public val `value`: CustomToolChatCompletions,
    ) : Tools

    public object Serializer : KSerializer<Tools> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.CreateChatCompletionRequest.Tools", PolymorphicKind.SEALED) {
        element("CaseChatCompletionTool", ChatCompletionTool.serializer().descriptor)
        element("CaseCustomToolChatCompletions", CustomToolChatCompletions.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Tools {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseChatCompletionTool::class to { CaseChatCompletionTool(decodeFromJsonElement(ChatCompletionTool.serializer(), it)) },
          CaseCustomToolChatCompletions::class to { CaseCustomToolChatCompletions(decodeFromJsonElement(CustomToolChatCompletions.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Tools) {
        when(value) {
          is CaseChatCompletionTool -> encoder.encodeSerializableValue(ChatCompletionTool.serializer(), value.value)
          is CaseCustomToolChatCompletions -> encoder.encodeSerializableValue(CustomToolChatCompletions.serializer(), value.value)
        }
      }
    }
  }

  /**
   * This tool searches the web for relevant results to use in a response.
   * Learn more about the [web search tool](/docs/guides/tools-web-search?api-mode=chat).
   *
   */
  @Serializable
  public data class WebSearchOptions(
    @SerialName("user_location")
    public val userLocation: UserLocation? = null,
    @SerialName("search_context_size")
    public val searchContextSize: WebSearchContextSize? = null,
  ) {
    /**
     * Approximate location parameters for the search.
     *
     */
    @Serializable
    public data class UserLocation(
      public val type: Type,
      public val approximate: WebSearchLocation,
    ) {
      @Serializable
      public enum class Type(
        public val `value`: String,
      ) {
        @SerialName("approximate")
        Approximate("approximate"),
        ;
      }
    }
  }
}
