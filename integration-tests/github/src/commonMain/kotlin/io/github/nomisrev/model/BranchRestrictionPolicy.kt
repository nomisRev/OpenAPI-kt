package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class BranchRestrictionPolicy(
    val url: String,
    @SerialName("users_url") val usersUrl: String,
    @SerialName("teams_url") val teamsUrl: String,
    @SerialName("apps_url") val appsUrl: String,
    val users: List<Users>,
    val teams: List<Team>,
    val apps: List<Apps>,
) {
    @Serializable
    data class Users(
        val login: String? = null,
        val id: Long? = null,
        @SerialName("node_id") val nodeId: String? = null,
        @SerialName("avatar_url") val avatarUrl: String? = null,
        @SerialName("gravatar_id") val gravatarId: String? = null,
        val url: String? = null,
        @SerialName("html_url") val htmlUrl: String? = null,
        @SerialName("followers_url") val followersUrl: String? = null,
        @SerialName("following_url") val followingUrl: String? = null,
        @SerialName("gists_url") val gistsUrl: String? = null,
        @SerialName("starred_url") val starredUrl: String? = null,
        @SerialName("subscriptions_url") val subscriptionsUrl: String? = null,
        @SerialName("organizations_url") val organizationsUrl: String? = null,
        @SerialName("repos_url") val reposUrl: String? = null,
        @SerialName("events_url") val eventsUrl: String? = null,
        @SerialName("received_events_url") val receivedEventsUrl: String? = null,
        val type: String? = null,
        @SerialName("site_admin") val siteAdmin: Boolean? = null,
        @SerialName("user_view_type") val userViewType: String? = null,
    )

    @Serializable
    data class Apps(
        val id: Long? = null,
        val slug: String? = null,
        @SerialName("node_id") val nodeId: String? = null,
        val owner: Owner? = null,
        val name: String? = null,
        @SerialName("client_id") val clientId: String? = null,
        val description: String? = null,
        @SerialName("external_url") val externalUrl: String? = null,
        @SerialName("html_url") val htmlUrl: String? = null,
        @SerialName("created_at") val createdAt: String? = null,
        @SerialName("updated_at") val updatedAt: String? = null,
        val permissions: Permissions? = null,
        val events: List<String>? = null,
    ) {
        @Serializable
        data class Owner(
            val login: String? = null,
            val id: Long? = null,
            @SerialName("node_id") val nodeId: String? = null,
            val url: String? = null,
            @SerialName("repos_url") val reposUrl: String? = null,
            @SerialName("events_url") val eventsUrl: String? = null,
            @SerialName("hooks_url") val hooksUrl: String? = null,
            @SerialName("issues_url") val issuesUrl: String? = null,
            @SerialName("members_url") val membersUrl: String? = null,
            @SerialName("public_members_url") val publicMembersUrl: String? = null,
            @SerialName("avatar_url") val avatarUrl: String? = null,
            val description: String? = null,
            @SerialName("gravatar_id") val gravatarId: String? = null,
            @SerialName("html_url") val htmlUrl: String? = null,
            @SerialName("followers_url") val followersUrl: String? = null,
            @SerialName("following_url") val followingUrl: String? = null,
            @SerialName("gists_url") val gistsUrl: String? = null,
            @SerialName("starred_url") val starredUrl: String? = null,
            @SerialName("subscriptions_url") val subscriptionsUrl: String? = null,
            @SerialName("organizations_url") val organizationsUrl: String? = null,
            @SerialName("received_events_url") val receivedEventsUrl: String? = null,
            val type: String? = null,
            @SerialName("site_admin") val siteAdmin: Boolean? = null,
            @SerialName("user_view_type") val userViewType: String? = null,
        )

        @Serializable
        data class Permissions(
            val metadata: String? = null,
            val contents: String? = null,
            val issues: String? = null,
            @SerialName("single_file") val singleFile: String? = null,
        )
    }
}
