package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a chat completion response returned by model, based on the provided input.
 */
@Serializable
public data class CreateChatCompletionResponse(
  public val id: String,
  public val choices: List<Choices>,
  public val created: Long,
  public val model: String,
  @SerialName("service_tier")
  public val serviceTier: ServiceTier? = null,
  @SerialName("system_fingerprint")
  public val systemFingerprint: String? = null,
  public val `object`: Object,
  public val usage: CompletionUsage? = null,
) {
  @Serializable
  public data class Choices(
    @SerialName("finish_reason")
    public val finishReason: FinishReason,
    public val index: Long,
    public val message: ChatCompletionResponseMessage,
    public val logprobs: Logprobs?,
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
    @SerialName("chat.completion")
    ChatCompletion("chat.completion"),
    ;
  }
}
