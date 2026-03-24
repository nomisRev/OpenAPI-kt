package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Usage statistics for models billed by token usage.
 */
@Serializable
public data class TranscriptTextUsageTokens(
  public val type: Type,
  @SerialName("input_tokens")
  public val inputTokens: Long,
  @SerialName("input_token_details")
  public val inputTokenDetails: InputTokenDetails? = null,
  @SerialName("output_tokens")
  public val outputTokens: Long,
  @SerialName("total_tokens")
  public val totalTokens: Long,
) {
  /**
   * Details about the input tokens billed for this request.
   */
  @Serializable
  public data class InputTokenDetails(
    @SerialName("text_tokens")
    public val textTokens: Long? = null,
    @SerialName("audio_tokens")
    public val audioTokens: Long? = null,
  )

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("tokens")
    Tokens("tokens"),
    ;
  }
}
