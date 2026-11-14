package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The Relationship a User has with a role.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class UserRoleAssignment(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val assignment: Assignment? = null,
  @SerialName("inherited_from")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val inheritedFrom: List<TeamSimple>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val email: Optional<String> = Optional.Absent,
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
  @SerialName("starred_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val starredAt: String? = null,
  @SerialName("user_view_type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val userViewType: String? = null,
) {
  @Serializable
  public enum class Assignment(
    public val `value`: String,
  ) {
    @SerialName("direct")
    Direct("direct"),
    @SerialName("indirect")
    Indirect("indirect"),
    @SerialName("mixed")
    Mixed("mixed"),
    ;
  }
}
