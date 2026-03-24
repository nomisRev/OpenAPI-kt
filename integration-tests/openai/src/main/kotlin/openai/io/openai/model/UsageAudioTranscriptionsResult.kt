package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The aggregated audio transcriptions usage details of the specific time bucket.
 */
@Serializable
public data class UsageAudioTranscriptionsResult(
  public val `object`: Object,
  public val seconds: Long,
  @SerialName("num_model_requests")
  public val numModelRequests: Long,
  @SerialName("project_id")
  public val projectId: String? = null,
  @SerialName("user_id")
  public val userId: String? = null,
  @SerialName("api_key_id")
  public val apiKeyId: String? = null,
  public val model: String? = null,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("organization.usage.audio_transcriptions.result")
    OrganizationUsageAudioTranscriptionsResult("organization.usage.audio_transcriptions.result"),
    ;
  }
}
