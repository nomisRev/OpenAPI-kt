package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a streamed chunk of a chat completion response returned
 * by the model, based on the provided input. 
 * [Learn more](/docs/guides/streaming-responses).
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateChatCompletionStreamResponse(
  public val id: String,
  public val choices: List<Choices>,
  public val created: Long,
  public val model: String,
  @SerialName("service_tier")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val serviceTier: Optional<ServiceTier> = Optional.Absent,
  @SerialName("system_fingerprint")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val systemFingerprint: String? = null,
  public val `object`: Object,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val usage: CompletionUsage? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Choices(
    public val delta: ChatCompletionStreamResponseDelta,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val logprobs: Optional<Logprobs> = Optional.Absent,
    @SerialName("finish_reason")
    public val finishReason: FinishReason?,
    public val index: Long,
  ) {
    @Serializable
    public enum class FinishReason(
      public val `value`: String,
    ) {
      @SerialName("stop")
      Stop("stop"),
      @SerialName("length")
      Length("length"),
      @SerialName("tool_calls")
      ToolCalls("tool_calls"),
      @SerialName("content_filter")
      ContentFilter("content_filter"),
      @SerialName("function_call")
      FunctionCall("function_call"),
      ;
    }

    /**
     * Log probability information for the choice.
     */
    @Serializable
    public data class Logprobs(
      public val content: List<ChatCompletionTokenLogprob>?,
      public val refusal: List<ChatCompletionTokenLogprob>?,
    )
  }

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("chat.completion.chunk")
    ChatCompletionChunk("chat.completion.chunk"),
    ;
  }
}
