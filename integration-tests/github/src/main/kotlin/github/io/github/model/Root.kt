package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Root(
  @SerialName("current_user_url")
  public val currentUserUrl: String,
  @SerialName("current_user_authorizations_html_url")
  public val currentUserAuthorizationsHtmlUrl: String,
  @SerialName("authorizations_url")
  public val authorizationsUrl: String,
  @SerialName("code_search_url")
  public val codeSearchUrl: String,
  @SerialName("commit_search_url")
  public val commitSearchUrl: String,
  @SerialName("emails_url")
  public val emailsUrl: String,
  @SerialName("emojis_url")
  public val emojisUrl: String,
  @SerialName("events_url")
  public val eventsUrl: String,
  @SerialName("feeds_url")
  public val feedsUrl: String,
  @SerialName("followers_url")
  public val followersUrl: String,
  @SerialName("following_url")
  public val followingUrl: String,
  @SerialName("gists_url")
  public val gistsUrl: String,
  @SerialName("hub_url")
  public val hubUrl: String? = null,
  @SerialName("issue_search_url")
  public val issueSearchUrl: String,
  @SerialName("issues_url")
  public val issuesUrl: String,
  @SerialName("keys_url")
  public val keysUrl: String,
  @SerialName("label_search_url")
  public val labelSearchUrl: String,
  @SerialName("notifications_url")
  public val notificationsUrl: String,
  @SerialName("organization_url")
  public val organizationUrl: String,
  @SerialName("organization_repositories_url")
  public val organizationRepositoriesUrl: String,
  @SerialName("organization_teams_url")
  public val organizationTeamsUrl: String,
  @SerialName("public_gists_url")
  public val publicGistsUrl: String,
  @SerialName("rate_limit_url")
  public val rateLimitUrl: String,
  @SerialName("repository_url")
  public val repositoryUrl: String,
  @SerialName("repository_search_url")
  public val repositorySearchUrl: String,
  @SerialName("current_user_repositories_url")
  public val currentUserRepositoriesUrl: String,
  @SerialName("starred_url")
  public val starredUrl: String,
  @SerialName("starred_gists_url")
  public val starredGistsUrl: String,
  @SerialName("topic_search_url")
  public val topicSearchUrl: String? = null,
  @SerialName("user_url")
  public val userUrl: String,
  @SerialName("user_organizations_url")
  public val userOrganizationsUrl: String,
  @SerialName("user_repositories_url")
  public val userRepositoriesUrl: String,
  @SerialName("user_search_url")
  public val userSearchUrl: String,
)
