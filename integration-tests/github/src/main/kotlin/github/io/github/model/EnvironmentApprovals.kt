package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An entry in the reviews log for environment deployments
 */
@Serializable
public data class EnvironmentApprovals(
  public val environments: List<Environments>,
  public val state: State,
  public val user: SimpleUser,
  public val comment: String,
) {
  @Serializable
  public data class Environments(
    public val id: Long? = null,
    @SerialName("node_id")
    public val nodeId: String? = null,
    public val name: String? = null,
    public val url: String? = null,
    @SerialName("html_url")
    public val htmlUrl: String? = null,
    @SerialName("created_at")
    public val createdAt: Instant? = null,
    @SerialName("updated_at")
    public val updatedAt: Instant? = null,
  )

  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("approved")
    Approved("approved"),
    @SerialName("rejected")
    Rejected("rejected"),
    @SerialName("pending")
    Pending("pending"),
    ;
  }
}
