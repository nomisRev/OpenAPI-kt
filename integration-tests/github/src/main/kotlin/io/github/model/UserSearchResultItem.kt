package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * User Search Result Item
 */
@OptIn(ExperimentalSerializationApi::class)
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val publicRepos: Long? = null,
  @SerialName("public_gists")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val publicGists: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val followers: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val following: Long? = null,
  @SerialName("created_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val createdAt: Instant? = null,
  @SerialName("updated_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val updatedAt: Instant? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val bio: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val email: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val location: Optional<String> = Optional.Absent,
  @SerialName("site_admin")
  public val siteAdmin: Boolean,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hireable: Optional<Boolean> = Optional.Absent,
  @SerialName("text_matches")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val textMatches: SearchResultTextMatches? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val blog: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val company: Optional<String> = Optional.Absent,
  @SerialName("suspended_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val suspendedAt: Optional<Instant> = Optional.Absent,
  @SerialName("user_view_type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val userViewType: String? = null,
)
