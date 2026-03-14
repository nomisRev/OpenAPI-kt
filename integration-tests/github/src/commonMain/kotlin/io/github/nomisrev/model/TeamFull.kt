package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class TeamFull(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    val name: String,
    val slug: String,
    val description: String?,
    val privacy: Privacy? = null,
    @SerialName("notification_setting") val notificationSetting: NotificationSetting? = null,
    val permission: String,
    @SerialName("members_url") val membersUrl: String,
    @SerialName("repositories_url") val repositoriesUrl: String,
    val parent: NullableTeamSimple? = null,
    @SerialName("members_count") val membersCount: Long,
    @SerialName("repos_count") val reposCount: Long,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val organization: TeamOrganization,
    @SerialName("ldap_dn") val ldapDn: LdapDn? = null,
    val type: Type,
    @SerialName("organization_id") val organizationId: Long? = null,
    @SerialName("enterprise_id") val enterpriseId: Long? = null,
) {
    @Serializable
    enum class Privacy {
        @SerialName("closed") Closed, @SerialName("secret") Secret;
    }

    @Serializable
    enum class NotificationSetting {
        @SerialName("notifications_enabled")
        NotificationsEnabled,
        @SerialName("notifications_disabled")
        NotificationsDisabled;
    }

    @Serializable
    enum class Type {
        @SerialName("enterprise") Enterprise, @SerialName("organization") Organization;
    }
}
