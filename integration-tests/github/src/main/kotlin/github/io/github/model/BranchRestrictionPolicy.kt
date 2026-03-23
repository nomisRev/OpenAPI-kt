package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
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
  @Serializable
  public data class Apps(
    public val id: Long? = null,
    public val slug: String? = null,
    @SerialName("node_id")
    public val nodeId: String? = null,
    public val owner: Owner? = null,
    public val name: String? = null,
    @SerialName("client_id")
    public val clientId: String? = null,
    public val description: String? = null,
    @SerialName("external_url")
    public val externalUrl: String? = null,
    @SerialName("html_url")
    public val htmlUrl: String? = null,
    @SerialName("created_at")
    public val createdAt: String? = null,
    @SerialName("updated_at")
    public val updatedAt: String? = null,
    public val permissions: Permissions? = null,
    public val events: List<String>? = null,
  ) {
    @Serializable
    public data class Owner(
      public val login: String? = null,
      public val id: Long? = null,
      @SerialName("node_id")
      public val nodeId: String? = null,
      public val url: String? = null,
      @SerialName("repos_url")
      public val reposUrl: String? = null,
      @SerialName("events_url")
      public val eventsUrl: String? = null,
      @SerialName("hooks_url")
      public val hooksUrl: String? = null,
      @SerialName("issues_url")
      public val issuesUrl: String? = null,
      @SerialName("members_url")
      public val membersUrl: String? = null,
      @SerialName("public_members_url")
      public val publicMembersUrl: String? = null,
      @SerialName("avatar_url")
      public val avatarUrl: String? = null,
      public val description: String? = null,
      @SerialName("gravatar_id")
      public val gravatarId: String? = null,
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
      @SerialName("received_events_url")
      public val receivedEventsUrl: String? = null,
      public val type: String? = null,
      @SerialName("site_admin")
      public val siteAdmin: Boolean? = null,
      @SerialName("user_view_type")
      public val userViewType: String? = null,
    )

    @Serializable
    public data class Permissions(
      public val metadata: String? = null,
      public val contents: String? = null,
      public val issues: String? = null,
      @SerialName("single_file")
      public val singleFile: String? = null,
    )
  }

  @Serializable
  public data class Users(
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
    public val type: String? = null,
    @SerialName("site_admin")
    public val siteAdmin: Boolean? = null,
    @SerialName("user_view_type")
    public val userViewType: String? = null,
  )
}
