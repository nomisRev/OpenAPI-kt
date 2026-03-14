package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PublicUser(
    val login: String,
    val id: Long,
    @SerialName("user_view_type") val userViewType: String? = null,
    @SerialName("node_id") val nodeId: String,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("gravatar_id") val gravatarId: String?,
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("followers_url") val followersUrl: String,
    @SerialName("following_url") val followingUrl: String,
    @SerialName("gists_url") val gistsUrl: String,
    @SerialName("starred_url") val starredUrl: String,
    @SerialName("subscriptions_url") val subscriptionsUrl: String,
    @SerialName("organizations_url") val organizationsUrl: String,
    @SerialName("repos_url") val reposUrl: String,
    @SerialName("events_url") val eventsUrl: String,
    @SerialName("received_events_url") val receivedEventsUrl: String,
    val type: String,
    @SerialName("site_admin") val siteAdmin: Boolean,
    val name: String?,
    val company: String?,
    val blog: String?,
    val location: String?,
    val email: String?,
    @SerialName("notification_email") val notificationEmail: String? = null,
    val hireable: Boolean?,
    val bio: String?,
    @SerialName("twitter_username") val twitterUsername: String? = null,
    @SerialName("public_repos") val publicRepos: Long,
    @SerialName("public_gists") val publicGists: Long,
    val followers: Long,
    val following: Long,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val plan: Plan? = null,
    @SerialName("private_gists") val privateGists: Long? = null,
    @SerialName("total_private_repos") val totalPrivateRepos: Long? = null,
    @SerialName("owned_private_repos") val ownedPrivateRepos: Long? = null,
    @SerialName("disk_usage") val diskUsage: Long? = null,
    val collaborators: Long? = null,
) {
    @Serializable
    data class Plan(
        val collaborators: Long,
        val name: String,
        val space: Long,
        @SerialName("private_repos") val privateRepos: Long,
    )
}
