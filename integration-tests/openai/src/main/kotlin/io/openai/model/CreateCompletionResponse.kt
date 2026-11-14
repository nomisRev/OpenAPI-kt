package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a completion response from the API. Note: both the streamed and non-streamed response objects share the same shape (unlike the chat endpoint).
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateCompletionResponse(
  public val id: String,
  public val choices: List<Choices>,
  public val created: Long,
  public val model: String,
  @SerialName("system_fingerprint")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val systemFingerprint: String? = null,
  public val `object`: Object,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Logprobs(
      @SerialName("text_offset")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val textOffset: List<Long>? = null,
      @SerialName("token_logprobs")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val tokenLogprobs: List<Double>? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val tokens: List<String>? = null,
      @SerialName("top_logprobs")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val topLogprobs: List<Map<String, Double>>? = null,
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
