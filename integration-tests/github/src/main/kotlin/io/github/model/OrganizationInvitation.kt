package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Organization Invitation
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class OrganizationInvitation(
  public val id: Long,
  public val login: String?,
  public val email: String?,
  public val role: String,
  @SerialName("created_at")
  public val createdAt: String,
  @SerialName("failed_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val failedAt: Optional<String> = Optional.Absent,
  @SerialName("failed_reason")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val failedReason: Optional<String> = Optional.Absent,
  public val inviter: SimpleUser,
  @SerialName("team_count")
  public val teamCount: Long,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("invitation_teams_url")
  public val invitationTeamsUrl: String,
  @SerialName("invitation_source")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val invitationSource: String? = null,
)
