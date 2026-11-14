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
 * Branch Restriction Policy
 */
@Serializable
public data class BranchRestrictionPolicy(
  public val url: String,
  @SerialName("users_url")
  public val usersUrl: String,
  @SerialName("teams_url")
  public val teamsUrl: String,
  @SerialName("apps_url")
  public val appsUrl: String,
  public val users: List<Users>,
  public val teams: List<Team>,
  public val apps: List<Apps>,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Apps(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val slug: String? = null,
    @SerialName("node_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val nodeId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val owner: Owner? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @SerialName("client_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val clientId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val description: String? = null,
    @SerialName("external_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val externalUrl: String? = null,
    @SerialName("html_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val htmlUrl: String? = null,
    @SerialName("created_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val createdAt: String? = null,
    @SerialName("updated_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updatedAt: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val permissions: Permissions? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val events: List<String>? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Owner(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val login: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val id: Long? = null,
      @SerialName("node_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val nodeId: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val url: String? = null,
      @SerialName("repos_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val reposUrl: String? = null,
      @SerialName("events_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val eventsUrl: String? = null,
      @SerialName("hooks_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val hooksUrl: String? = null,
      @SerialName("issues_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val issuesUrl: String? = null,
      @SerialName("members_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val membersUrl: String? = null,
      @SerialName("public_members_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val publicMembersUrl: String? = null,
      @SerialName("avatar_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val avatarUrl: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val description: String? = null,
      @SerialName("gravatar_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val gravatarId: String? = null,
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
      @SerialName("received_events_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val receivedEventsUrl: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val type: String? = null,
      @SerialName("site_admin")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val siteAdmin: Boolean? = null,
      @SerialName("user_view_type")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val userViewType: String? = null,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Permissions(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val metadata: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val contents: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val issues: String? = null,
      @SerialName("single_file")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val singleFile: String? = null,
    )
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Users(
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
    public val gravatarId: String? = null,
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
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val type: String? = null,
    @SerialName("site_admin")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val siteAdmin: Boolean? = null,
    @SerialName("user_view_type")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val userViewType: String? = null,
  )
}
