package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Organization Invitation
 */
@Serializable
public data class OrganizationInvitation(
  public val id: Long,
  public val login: String?,
  public val email: String?,
  public val role: String,
  @SerialName("created_at")
  public val createdAt: String,
  @SerialName("failed_at")
  public val failedAt: String? = null,
  @SerialName("failed_reason")
  public val failedReason: String? = null,
  public val inviter: SimpleUser,
  @SerialName("team_count")
  public val teamCount: Long,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("invitation_teams_url")
  public val invitationTeamsUrl: String,
  @SerialName("invitation_source")
  public val invitationSource: String? = null,
)
