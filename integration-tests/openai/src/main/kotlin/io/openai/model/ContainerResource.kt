package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ContainerResource(
  public val id: String,
  public val `object`: String,
  public val name: String,
  @SerialName("created_at")
  public val createdAt: Long,
  public val status: String,
  @SerialName("last_active_at")
  public val lastActiveAt: Long? = null,
  @SerialName("expires_after")
  public val expiresAfter: ExpiresAfter? = null,
  @SerialName("memory_limit")
  public val memoryLimit: MemoryLimit? = null,
  @SerialName("network_policy")
  public val networkPolicy: NetworkPolicy? = null,
) {
  /**
   * The container will expire after this time period.
   * The anchor is the reference point for the expiration.
   * The minutes is the number of minutes after the anchor before the container expires.
   *
   */
  @Serializable
  public data class ExpiresAfter(
    public val anchor: Anchor? = null,
    public val minutes: Long? = null,
  ) {
    @Serializable
    public enum class Anchor(
      public val `value`: String,
    ) {
      @SerialName("last_active_at")
      LastActiveAt("last_active_at"),
      ;
    }
  }

  @Serializable
  public enum class MemoryLimit {
    `1g`,
    `4g`,
    `16g`,
    `64g`,
  }

  /**
   * Network access policy for the container.
   */
  @Serializable
  public data class NetworkPolicy(
    public val type: Type,
    @SerialName("allowed_domains")
    public val allowedDomains: List<String>? = null,
  ) {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("allowlist")
      Allowlist("allowlist"),
      @SerialName("disabled")
      Disabled("disabled"),
      ;
    }
  }
}
