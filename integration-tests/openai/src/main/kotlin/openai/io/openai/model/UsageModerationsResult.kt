package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The aggregated moderations usage details of the specific time bucket.
 */
@Serializable
public data class UsageModerationsResult(
  public val `object`: Object,
  @SerialName("input_tokens")
  public val inputTokens: Long,
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
    @SerialName("organization.usage.moderations.result")
    OrganizationUsageModerationsResult("organization.usage.moderations.result"),
    ;
  }
}
