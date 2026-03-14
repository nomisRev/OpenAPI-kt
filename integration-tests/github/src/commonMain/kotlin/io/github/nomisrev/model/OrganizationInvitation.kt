package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OrganizationInvitation(
    val id: Long,
    val login: String?,
    val email: String?,
    val role: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("failed_at") val failedAt: String? = null,
    @SerialName("failed_reason") val failedReason: String? = null,
    val inviter: SimpleUser,
    @SerialName("team_count") val teamCount: Long,
    @SerialName("node_id") val nodeId: String,
    @SerialName("invitation_teams_url") val invitationTeamsUrl: String,
    @SerialName("invitation_source") val invitationSource: String? = null,
)
