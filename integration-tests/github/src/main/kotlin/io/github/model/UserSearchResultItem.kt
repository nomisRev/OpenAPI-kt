package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * User Search Result Item
 */
@Serializable
public data class UserSearchResultItem(
  public val login: String,
  public val id: Long,
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
  @SerialName("subscriptions_url")
  public val subscriptionsUrl: String,
  @SerialName("organizations_url")
  public val organizationsUrl: String,
  @SerialName("repos_url")
  public val reposUrl: String,
  @SerialName("received_events_url")
  public val receivedEventsUrl: String,
  public val type: String,
  public val score: Double,
  @SerialName("following_url")
  public val followingUrl: String,
  @SerialName("gists_url")
  public val gistsUrl: String,
  @SerialName("starred_url")
  public val starredUrl: String,
  @SerialName("events_url")
  public val eventsUrl: String,
  @SerialName("public_repos")
  public val publicRepos: Long? = null,
  @SerialName("public_gists")
  public val publicGists: Long? = null,
  public val followers: Long? = null,
  public val following: Long? = null,
  @SerialName("created_at")
  public val createdAt: Instant? = null,
  @SerialName("updated_at")
  public val updatedAt: Instant? = null,
  public val name: String? = null,
  public val bio: String? = null,
  public val email: String? = null,
  public val location: String? = null,
  @SerialName("site_admin")
  public val siteAdmin: Boolean,
  public val hireable: Boolean? = null,
  @SerialName("text_matches")
  public val textMatches: SearchResultTextMatches? = null,
  public val blog: String? = null,
  public val company: String? = null,
  @SerialName("suspended_at")
  public val suspendedAt: Instant? = null,
  @SerialName("user_view_type")
  public val userViewType: String? = null,
)
