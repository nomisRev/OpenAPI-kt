package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class EnterpriseTeam(
    val id: Long,
    val name: String,
    val description: String? = null,
    val slug: String,
    val url: String,
    @SerialName("sync_to_organizations") val syncToOrganizations: String? = null,
    @SerialName("organization_selection_type") val organizationSelectionType: String? = null,
    @SerialName("group_id") val groupId: String?,
    @SerialName("group_name") val groupName: String? = null,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("members_url") val membersUrl: String,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
)
