package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Groups of organization members that gives permissions on specified repositories.
 */
@Serializable
public data class NullableTeamSimple(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val url: String,
  @SerialName("members_url")
  public val membersUrl: String,
  public val name: String,
  public val description: String?,
  public val permission: String,
  public val privacy: String? = null,
  @SerialName("notification_setting")
  public val notificationSetting: String? = null,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("repositories_url")
  public val repositoriesUrl: String,
  public val slug: String,
  @SerialName("ldap_dn")
  public val ldapDn: String? = null,
  public val type: Type,
  @SerialName("organization_id")
  public val organizationId: Long? = null,
  @SerialName("enterprise_id")
  public val enterpriseId: Long? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("enterprise")
    Enterprise("enterprise"),
    @SerialName("organization")
    Organization("organization"),
    ;
  }
}
