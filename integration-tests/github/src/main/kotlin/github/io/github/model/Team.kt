package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Groups of organization members that gives permissions on specified repositories.
 */
@Serializable
public data class Team(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val name: String,
  public val slug: String,
  public val description: String?,
  public val privacy: String? = null,
  @SerialName("notification_setting")
  public val notificationSetting: String? = null,
  public val permission: String,
  public val permissions: Permissions? = null,
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("members_url")
  public val membersUrl: String,
  @SerialName("repositories_url")
  public val repositoriesUrl: String,
  public val type: Type,
  @SerialName("organization_id")
  public val organizationId: Long? = null,
  @SerialName("enterprise_id")
  public val enterpriseId: Long? = null,
  public val parent: NullableTeamSimple?,
) {
  @Serializable
  public data class Permissions(
    public val pull: Boolean,
    public val triage: Boolean,
    public val push: Boolean,
    public val maintain: Boolean,
    public val admin: Boolean,
  )

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
