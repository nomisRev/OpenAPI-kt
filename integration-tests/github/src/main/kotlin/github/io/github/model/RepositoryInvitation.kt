package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Repository invitations let you manage who you collaborate with.
 */
@Serializable
public data class RepositoryInvitation(
  public val id: Long,
  public val repository: MinimalRepository,
  public val invitee: NullableSimpleUser?,
  public val inviter: NullableSimpleUser?,
  public val permissions: Permissions,
  @SerialName("created_at")
  public val createdAt: Instant,
  public val expired: Boolean? = null,
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("node_id")
  public val nodeId: String,
) {
  @Serializable
  public enum class Permissions(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    @SerialName("admin")
    Admin("admin"),
    @SerialName("triage")
    Triage("triage"),
    @SerialName("maintain")
    Maintain("maintain"),
    ;
  }
}
