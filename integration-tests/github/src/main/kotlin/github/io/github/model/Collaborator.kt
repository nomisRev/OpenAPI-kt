package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Collaborator
 */
@Serializable
public data class Collaborator(
  public val login: String,
  public val id: Long,
  public val email: String? = null,
  public val name: String? = null,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("avatar_url")
  public val avatarUrl: String,
  @SerialName("gravatar_id")
  public val gravatarId: String?,
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("followers_url")
  public val followersUrl: String,
  @SerialName("following_url")
  public val followingUrl: String,
  @SerialName("gists_url")
  public val gistsUrl: String,
  @SerialName("starred_url")
  public val starredUrl: String,
  @SerialName("subscriptions_url")
  public val subscriptionsUrl: String,
  @SerialName("organizations_url")
  public val organizationsUrl: String,
  @SerialName("repos_url")
  public val reposUrl: String,
  @SerialName("events_url")
  public val eventsUrl: String,
  @SerialName("received_events_url")
  public val receivedEventsUrl: String,
  public val type: String,
  @SerialName("site_admin")
  public val siteAdmin: Boolean,
  public val permissions: Permissions? = null,
  @SerialName("role_name")
  public val roleName: String,
  @SerialName("user_view_type")
  public val userViewType: String? = null,
) {
  @Serializable
  public data class Permissions(
    public val pull: Boolean,
    public val triage: Boolean? = null,
    public val push: Boolean,
    public val maintain: Boolean? = null,
    public val admin: Boolean,
  )
}
