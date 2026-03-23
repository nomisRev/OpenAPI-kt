package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Contributor
 */
@Serializable
public data class Contributor(
  public val login: String? = null,
  public val id: Long? = null,
  @SerialName("node_id")
  public val nodeId: String? = null,
  @SerialName("avatar_url")
  public val avatarUrl: String? = null,
  @SerialName("gravatar_id")
  public val gravatarId: String? = null,
  public val url: String? = null,
  @SerialName("html_url")
  public val htmlUrl: String? = null,
  @SerialName("followers_url")
  public val followersUrl: String? = null,
  @SerialName("following_url")
  public val followingUrl: String? = null,
  @SerialName("gists_url")
  public val gistsUrl: String? = null,
  @SerialName("starred_url")
  public val starredUrl: String? = null,
  @SerialName("subscriptions_url")
  public val subscriptionsUrl: String? = null,
  @SerialName("organizations_url")
  public val organizationsUrl: String? = null,
  @SerialName("repos_url")
  public val reposUrl: String? = null,
  @SerialName("events_url")
  public val eventsUrl: String? = null,
  @SerialName("received_events_url")
  public val receivedEventsUrl: String? = null,
  public val type: String,
  @SerialName("site_admin")
  public val siteAdmin: Boolean? = null,
  public val contributions: Long,
  public val email: String? = null,
  public val name: String? = null,
  @SerialName("user_view_type")
  public val userViewType: String? = null,
)
