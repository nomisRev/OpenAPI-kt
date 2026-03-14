package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Team(
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
    val type: Type,
    @SerialName("organization_id") val organizationId: Long? = null,
    @SerialName("enterprise_id") val enterpriseId: Long? = null,
    val parent: NullableTeamSimple?,
) {
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
