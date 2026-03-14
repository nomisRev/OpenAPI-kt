package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class TeamRoleAssignment(
    val assignment: Assignment? = null,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val name: String,
    val slug: String,
    val description: String?,
    val privacy: String? = null,
    @SerialName("notification_setting") val notificationSetting: String? = null,
    val permission: String,
    val permissions: Permissions? = null,
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("members_url") val membersUrl: String,
    @SerialName("repositories_url") val repositoriesUrl: String,
    val parent: NullableTeamSimple?,
    val type: Type,
    @SerialName("organization_id") val organizationId: Long? = null,
    @SerialName("enterprise_id") val enterpriseId: Long? = null,
) {
    @Serializable
    enum class Assignment {
        @SerialName("direct") Direct, @SerialName("indirect") Indirect, @SerialName("mixed") Mixed;
    }

    @Serializable
    data class Permissions(
        val pull: Boolean,
        val triage: Boolean,
        val push: Boolean,
        val maintain: Boolean,
        val admin: Boolean,
    )

    @Serializable
    enum class Type {
        @SerialName("enterprise") Enterprise, @SerialName("organization") Organization;
    }
}
