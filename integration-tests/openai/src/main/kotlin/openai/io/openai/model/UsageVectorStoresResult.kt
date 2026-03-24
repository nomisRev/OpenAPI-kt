package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The aggregated vector stores usage details of the specific time bucket.
 */
@Serializable
public data class UsageVectorStoresResult(
  public val `object`: Object,
  @SerialName("usage_bytes")
  public val usageBytes: Long,
  @SerialName("project_id")
  public val projectId: String? = null,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("organization.usage.vector_stores.result")
    OrganizationUsageVectorStoresResult("organization.usage.vector_stores.result"),
    ;
  }
}
