package io.openai.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The aggregated completions usage details of the specific time bucket.
 */
@Serializable
public data class UsageCompletionsResult(
  public val `object`: Object,
  @SerialName("input_tokens")
  public val inputTokens: Long,
  @SerialName("input_cached_tokens")
  public val inputCachedTokens: Long? = null,
  @SerialName("output_tokens")
  public val outputTokens: Long,
  @SerialName("input_audio_tokens")
  public val inputAudioTokens: Long? = null,
  @SerialName("output_audio_tokens")
  public val outputAudioTokens: Long? = null,
  @SerialName("num_model_requests")
  public val numModelRequests: Long,
  @SerialName("project_id")
  public val projectId: String? = null,
  @SerialName("user_id")
  public val userId: String? = null,
  @SerialName("api_key_id")
  public val apiKeyId: String? = null,
  public val model: String? = null,
  public val batch: Boolean? = null,
  @SerialName("service_tier")
  public val serviceTier: String? = null,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("organization.usage.completions.result")
    OrganizationUsageCompletionsResult("organization.usage.completions.result"),
    ;
  }
}
