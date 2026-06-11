package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an individual Admin API key in an org.
 */
@Serializable
public data class AdminApiKey(
  public val `object`: String,
  public val id: String,
  public val name: String,
  @SerialName("redacted_value")
  public val redactedValue: String,
  public val `value`: String? = null,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("last_used_at")
  public val lastUsedAt: Long?,
  public val owner: Owner,
) {
  @Serializable
  public data class Owner(
    public val type: String? = null,
    public val `object`: String? = null,
    public val id: String? = null,
    public val name: String? = null,
    @SerialName("created_at")
    public val createdAt: Long? = null,
    public val role: String? = null,
  )
}
