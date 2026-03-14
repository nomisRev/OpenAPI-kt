package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Root(
    @SerialName("current_user_url") val currentUserUrl: String,
    @SerialName("current_user_authorizations_html_url") val currentUserAuthorizationsHtmlUrl: String,
    @SerialName("authorizations_url") val authorizationsUrl: String,
    @SerialName("code_search_url") val codeSearchUrl: String,
    @SerialName("commit_search_url") val commitSearchUrl: String,
    @SerialName("emails_url") val emailsUrl: String,
    @SerialName("emojis_url") val emojisUrl: String,
    @SerialName("events_url") val eventsUrl: String,
    @SerialName("feeds_url") val feedsUrl: String,
    @SerialName("followers_url") val followersUrl: String,
    @SerialName("following_url") val followingUrl: String,
    @SerialName("gists_url") val gistsUrl: String,
    @SerialName("hub_url") val hubUrl: String? = null,
    @SerialName("issue_search_url") val issueSearchUrl: String,
    @SerialName("issues_url") val issuesUrl: String,
    @SerialName("keys_url") val keysUrl: String,
    @SerialName("label_search_url") val labelSearchUrl: String,
    @SerialName("notifications_url") val notificationsUrl: String,
    @SerialName("organization_url") val organizationUrl: String,
    @SerialName("organization_repositories_url") val organizationRepositoriesUrl: String,
    @SerialName("organization_teams_url") val organizationTeamsUrl: String,
    @SerialName("public_gists_url") val publicGistsUrl: String,
    @SerialName("rate_limit_url") val rateLimitUrl: String,
    @SerialName("repository_url") val repositoryUrl: String,
    @SerialName("repository_search_url") val repositorySearchUrl: String,
    @SerialName("current_user_repositories_url") val currentUserRepositoriesUrl: String,
    @SerialName("starred_url") val starredUrl: String,
    @SerialName("starred_gists_url") val starredGistsUrl: String,
    @SerialName("topic_search_url") val topicSearchUrl: String? = null,
    @SerialName("user_url") val userUrl: String,
    @SerialName("user_organizations_url") val userOrganizationsUrl: String,
    @SerialName("user_repositories_url") val userRepositoriesUrl: String,
    @SerialName("user_search_url") val userSearchUrl: String,
)
