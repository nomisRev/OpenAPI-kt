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
  public val metadata: Metadata? = null,
  @SerialName("top_logprobs")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val topLogprobs: Long? = null,
  public val temperature: Double? = null,
  @SerialName("top_p")
  public val topP: Double? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val user: String? = null,
  @SerialName("safety_identifier")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val safetyIdentifier: String? = null,
  @SerialName("prompt_cache_key")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val promptCacheKey: String? = null,
  @SerialName("service_tier")
  public val serviceTier: ServiceTier? = null,
  @SerialName("prompt_cache_retention")
  public val promptCacheRetention: PromptCacheRetention? = null,
  public val messages: List<ChatCompletionRequestMessage>,
  public val model: ModelIdsShared,
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val webSearchOptions: WebSearchOptions? = null,
  @SerialName("response_format")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val responseFormat: ResponseFormat? = null,
  public val audio: Audio? = null,
  public val store: Boolean? = null,
  public val stream: Boolean? = null,
  public val stop: StopConfiguration? = null,
  @SerialName("logit_bias")
  public val logitBias: Map<String, Long>? = null,
  public val logprobs: Boolean? = null,
  @SerialName("max_tokens")
  public val maxTokens: Long? = null,
  public val n: Long? = null,
  public val prediction: PredictionContent? = null,
  public val seed: Long? = null,
  @SerialName("stream_options")
  public val streamOptions: ChatCompletionStreamOptions? = null,
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
      public val strict: Boolean? = null,
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
      public val strict: Boolean? = null,
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
    public val userLocation: UserLocation? = null,
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
