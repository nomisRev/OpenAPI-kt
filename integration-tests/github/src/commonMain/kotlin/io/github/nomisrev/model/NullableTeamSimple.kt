package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NullableTeamSimple(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    @SerialName("members_url") val membersUrl: String,
    val name: String,
    val description: String?,
    val permission: String,
    val privacy: String? = null,
    @SerialName("notification_setting") val notificationSetting: String? = null,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("repositories_url") val repositoriesUrl: String,
    val slug: String,
    @SerialName("ldap_dn") val ldapDn: String? = null,
    val type: Type,
    @SerialName("organization_id") val organizationId: Long? = null,
    @SerialName("enterprise_id") val enterpriseId: Long? = null,
) {
    @Serializable
    enum class Type {
        @SerialName("enterprise") Enterprise, @SerialName("organization") Organization;
    }
}
