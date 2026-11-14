package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
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
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateChatCompletionRequest(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val metadata: Optional<Metadata> = Optional.Absent,
  @SerialName("top_logprobs")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val topLogprobs: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val temperature: Optional<Double> = Optional.Absent,
  @SerialName("top_p")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val topP: Optional<Double> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val user: String? = null,
  @SerialName("safety_identifier")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val safetyIdentifier: String? = null,
  @SerialName("prompt_cache_key")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val promptCacheKey: String? = null,
  @SerialName("service_tier")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val serviceTier: Optional<ServiceTier> = Optional.Absent,
  @SerialName("prompt_cache_retention")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val promptCacheRetention: Optional<PromptCacheRetention> = Optional.Absent,
  public val messages: List<ChatCompletionRequestMessage>,
  public val model: ModelIdsShared,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val modalities: Optional<ResponseModalities> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val verbosity: Optional<Verbosity> = Optional.Absent,
  @SerialName("reasoning_effort")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reasoningEffort: Optional<ReasoningEffort> = Optional.Absent,
  @SerialName("max_completion_tokens")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxCompletionTokens: Optional<Long> = Optional.Absent,
  @SerialName("frequency_penalty")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val frequencyPenalty: Optional<Double> = Optional.Absent,
  @SerialName("presence_penalty")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val presencePenalty: Optional<Double> = Optional.Absent,
  @SerialName("web_search_options")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val webSearchOptions: WebSearchOptions? = null,
  @SerialName("response_format")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val responseFormat: ResponseFormat? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val audio: Optional<Audio> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val store: Optional<Boolean> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val stream: Optional<Boolean> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val stop: Optional<StopConfiguration> = Optional.Absent,
  @SerialName("logit_bias")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val logitBias: Optional<Map<String, Long>> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val logprobs: Optional<Boolean> = Optional.Absent,
  @SerialName("max_tokens")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxTokens: Optional<Long> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val n: Optional<Long> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val prediction: Optional<PredictionContent> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val seed: Optional<Long> = Optional.Absent,
  @SerialName("stream_options")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val streamOptions: Optional<ChatCompletionStreamOptions> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tools: List<Tools>? = null,
  @SerialName("tool_choice")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val toolChoice: ChatCompletionToolChoiceOption? = null,
  @SerialName("parallel_tool_calls")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val parallelToolCalls: ParallelToolCalls? = null,
  @SerialName("function_call")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val functionCall: FunctionCall? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            NoneOrAuto::class to { decodeFromJsonElement(NoneOrAuto.serializer(), it) },
          )
          value is JsonObject -> json.attemptDeserialize(
            value,
            CaseChatCompletionFunctionCallOption::class to { CaseChatCompletionFunctionCallOption(decodeFromJsonElement(ChatCompletionFunctionCallOption.serializer(), it)) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: FunctionCall) {
        when(value) {
          is NoneOrAuto -> encoder.encodeSerializableValue(NoneOrAuto.serializer(), value)
          is CaseChatCompletionFunctionCallOption -> encoder.encodeSerializableValue(ChatCompletionFunctionCallOption.serializer(), value.value)
        }
      }
    }
  }

  @Serializable
  public enum class PromptCacheRetention(
    public val `value`: String,
  ) {
    @SerialName("in-memory")
    InMemory("in-memory"),
    `24h`("24h"),
    ;
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
    @SerialName("text")
    public data object Text : ResponseFormat

    /**
     * JSON Schema response format. Used to generate structured JSON responses.
     * Learn more about [Structured Outputs](/docs/guides/structured-outputs).
     *
     */
    @SerialName("json_schema")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class JsonSchema(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val description: String? = null,
      public val name: String,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val schema: ResponseFormatJsonSchemaSchema? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val strict: Optional<Boolean> = Optional.Absent,
    ) : ResponseFormat

    @Serializable
    @SerialName("json_object")
    public data object JsonObject : ResponseFormat
  }

  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Tools {
    /**
     * A function tool that can be used to generate a response.
     *
     */
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

    /**
     * A custom tool that processes input using a specified format.
     *
     */
    @SerialName("custom")
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Custom(
      public val name: String,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val description: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val format: Format? = null,
    ) : Tools {
      /**
       * The input format for the custom tool. Default is unconstrained text.
       *
       */
      @OptIn(ExperimentalSerializationApi::class)
      @JsonClassDiscriminator("type")
      @Serializable
      public sealed interface Format {
        @Serializable
        @SerialName("text")
        public data object Text : Format

        /**
         * A grammar defined by the user.
         */
        @SerialName("grammar")
        @Serializable
        public data class Grammar(
          public val definition: String,
          public val syntax: Syntax,
        ) : Format {
          @Serializable
          public enum class Syntax(
            public val `value`: String,
          ) {
            @SerialName("lark")
            Lark("lark"),
            @SerialName("regex")
            Regex("regex"),
            ;
          }
        }
      }
    }
  }

  /**
   * This tool searches the web for relevant results to use in a response.
   * Learn more about the [web search tool](/docs/guides/tools-web-search?api-mode=chat).
   *
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class WebSearchOptions(
    @SerialName("user_location")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val userLocation: Optional<UserLocation> = Optional.Absent,
    @SerialName("search_context_size")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
