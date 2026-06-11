package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a completion response from the API. Note: both the streamed and non-streamed response objects share the same shape (unlike the chat endpoint).
 *
 */
@Serializable
public data class CreateCompletionResponse(
  public val id: String,
  public val choices: List<Choices>,
  public val created: Long,
  public val model: String,
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
    public val logprobs: Logprobs?,
    public val text: String,
  ) {
    @Serializable
    public enum class FinishReason(
      public val `value`: String,
    ) {
      @SerialName("stop")
      Stop("stop"),
      @SerialName("length")
      Length("length"),
      @SerialName("content_filter")
      ContentFilter("content_filter"),
      ;
    }

    @Serializable
    public data class Logprobs(
      @SerialName("text_offset")
      public val textOffset: List<Long>? = null,
      @SerialName("token_logprobs")
      public val tokenLogprobs: List<Double>? = null,
      public val tokens: List<String>? = null,
      @SerialName("top_logprobs")
      public val topLogprobs: List<List<Double>>? = null,
    )
  }

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("text_completion")
    TextCompletion("text_completion"),
    ;
  }
}
