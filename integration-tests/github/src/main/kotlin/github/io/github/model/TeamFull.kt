package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Groups of organization members that gives permissions on specified repositories.
 */
@Serializable
public data class TeamFull(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  public val name: String,
  public val slug: String,
  public val description: String?,
  public val privacy: Privacy? = null,
  @SerialName("notification_setting")
  public val notificationSetting: NotificationSetting? = null,
  public val permission: String,
  @SerialName("members_url")
  public val membersUrl: String,
  @SerialName("repositories_url")
  public val repositoriesUrl: String,
  public val parent: NullableTeamSimple? = null,
  @SerialName("members_count")
  public val membersCount: Long,
  @SerialName("repos_count")
  public val reposCount: Long,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val organization: TeamOrganization,
  @SerialName("ldap_dn")
  public val ldapDn: LdapDn? = null,
  public val type: Type,
  @SerialName("organization_id")
  public val organizationId: Long? = null,
  @SerialName("enterprise_id")
  public val enterpriseId: Long? = null,
) {
  @Serializable
  public enum class NotificationSetting(
    public val `value`: String,
  ) {
    @SerialName("notifications_enabled")
    NotificationsEnabled("notifications_enabled"),
    @SerialName("notifications_disabled")
    NotificationsDisabled("notifications_disabled"),
    ;
  }

  @Serializable
  public enum class Privacy(
    public val `value`: String,
  ) {
    @SerialName("closed")
    Closed("closed"),
    @SerialName("secret")
    Secret("secret"),
    ;
  }

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
