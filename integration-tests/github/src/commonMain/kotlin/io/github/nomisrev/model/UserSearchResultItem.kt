package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class UserSearchResultItem(
    val login: String,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("gravatar_id") val gravatarId: String?,
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("followers_url") val followersUrl: String,
    @SerialName("subscriptions_url") val subscriptionsUrl: String,
    @SerialName("organizations_url") val organizationsUrl: String,
    @SerialName("repos_url") val reposUrl: String,
    @SerialName("received_events_url") val receivedEventsUrl: String,
    val type: String,
    val score: Double,
    @SerialName("following_url") val followingUrl: String,
    @SerialName("gists_url") val gistsUrl: String,
    @SerialName("starred_url") val starredUrl: String,
    @SerialName("events_url") val eventsUrl: String,
    @SerialName("public_repos") val publicRepos: Long? = null,
    @SerialName("public_gists") val publicGists: Long? = null,
    val followers: Long? = null,
    val following: Long? = null,
    @SerialName("created_at") val createdAt: LocalDateTime? = null,
    @SerialName("updated_at") val updatedAt: LocalDateTime? = null,
    val name: String? = null,
    val bio: String? = null,
    val email: String? = null,
    val location: String? = null,
    @SerialName("site_admin") val siteAdmin: Boolean,
    val hireable: Boolean? = null,
    @SerialName("text_matches") val textMatches: SearchResultTextMatches? = null,
    val blog: String? = null,
    val company: String? = null,
    @SerialName("suspended_at") val suspendedAt: LocalDateTime? = null,
    @SerialName("user_view_type") val userViewType: String? = null,
)
