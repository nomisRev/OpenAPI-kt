package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class UserRoleAssignment(
    val assignment: Assignment? = null,
    @SerialName("inherited_from") val inheritedFrom: List<TeamSimple>? = null,
    val name: String? = null,
    val email: String? = null,
    val login: String,
    val id: Long,
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
    @SerialName("starred_at") val starredAt: String? = null,
    @SerialName("user_view_type") val userViewType: String? = null,
) {
    @Serializable
    enum class Assignment {
        @SerialName("direct") Direct, @SerialName("indirect") Indirect, @SerialName("mixed") Mixed;
    }
}
