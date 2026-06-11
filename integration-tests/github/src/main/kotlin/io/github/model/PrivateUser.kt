package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Private User
 */
@Serializable
public data class PrivateUser(
  public val login: String,
  public val id: Long,
  @SerialName("user_view_type")
  public val userViewType: String? = null,
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
  public val name: String?,
  public val company: String?,
  public val blog: String?,
  public val location: String?,
  public val email: String?,
  @SerialName("notification_email")
  public val notificationEmail: String? = null,
  public val hireable: Boolean?,
  public val bio: String?,
  @SerialName("twitter_username")
  public val twitterUsername: String? = null,
  @SerialName("public_repos")
  public val publicRepos: Long,
  @SerialName("public_gists")
  public val publicGists: Long,
  public val followers: Long,
  public val following: Long,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("private_gists")
  public val privateGists: Long,
  @SerialName("total_private_repos")
  public val totalPrivateRepos: Long,
  @SerialName("owned_private_repos")
  public val ownedPrivateRepos: Long,
  @SerialName("disk_usage")
  public val diskUsage: Long,
  public val collaborators: Long,
  @SerialName("two_factor_authentication")
  public val twoFactorAuthentication: Boolean,
  public val plan: Plan? = null,
  @SerialName("business_plus")
  public val businessPlus: Boolean? = null,
  @SerialName("ldap_dn")
  public val ldapDn: String? = null,
) {
  @Serializable
  public data class Plan(
    public val collaborators: Long,
    public val name: String,
    public val space: Long,
    @SerialName("private_repos")
    public val privateRepos: Long,
  )
}
