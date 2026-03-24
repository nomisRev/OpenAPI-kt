package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The aggregated images usage details of the specific time bucket.
 */
@Serializable
public data class UsageImagesResult(
  public val `object`: Object,
  public val images: Long,
  @SerialName("num_model_requests")
  public val numModelRequests: Long,
  public val source: String? = null,
  public val size: String? = null,
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
    @SerialName("organization.usage.images.result")
    OrganizationUsageImagesResult("organization.usage.images.result"),
    ;
  }
}
