package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Org Hook
 */
@Serializable
public data class OrgHook(
  public val id: Long,
  public val url: String,
  @SerialName("ping_url")
  public val pingUrl: String,
  @SerialName("deliveries_url")
  public val deliveriesUrl: String? = null,
  public val name: String,
  public val events: List<String>,
  public val active: Boolean,
  public val config: Config,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("created_at")
  public val createdAt: Instant,
  public val type: String,
) {
  @Serializable
  public data class Config(
    public val url: String? = null,
    @SerialName("insecure_ssl")
    public val insecureSsl: String? = null,
    @SerialName("content_type")
    public val contentType: String? = null,
    public val secret: String? = null,
  )
}
