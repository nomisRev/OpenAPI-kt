package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Contributor
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Contributor(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val login: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: Long? = null,
  @SerialName("node_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val nodeId: String? = null,
  @SerialName("avatar_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val avatarUrl: String? = null,
  @SerialName("gravatar_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val gravatarId: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val url: String? = null,
  @SerialName("html_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val htmlUrl: String? = null,
  @SerialName("followers_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val followersUrl: String? = null,
  @SerialName("following_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val followingUrl: String? = null,
  @SerialName("gists_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val gistsUrl: String? = null,
  @SerialName("starred_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val starredUrl: String? = null,
  @SerialName("subscriptions_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val subscriptionsUrl: String? = null,
  @SerialName("organizations_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationsUrl: String? = null,
  @SerialName("repos_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reposUrl: String? = null,
  @SerialName("events_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val eventsUrl: String? = null,
  @SerialName("received_events_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val receivedEventsUrl: String? = null,
  public val type: String,
  @SerialName("site_admin")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val siteAdmin: Boolean? = null,
  public val contributions: Long,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val email: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @SerialName("user_view_type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val userViewType: String? = null,
)
