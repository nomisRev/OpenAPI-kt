package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Discussion(
    @SerialName("active_lock_reason") val activeLockReason: String?,
    @SerialName("answer_chosen_at") val answerChosenAt: String?,
    @SerialName("answer_chosen_by") val answerChosenBy: AnswerChosenBy?,
    @SerialName("answer_html_url") val answerHtmlUrl: String?,
    @SerialName("author_association") val authorAssociation: AuthorAssociation? = null,
    val body: String,
    val category: Category,
    val comments: Long,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("html_url") val htmlUrl: String,
    val id: Long,
    val locked: Boolean,
    @SerialName("node_id") val nodeId: String,
    val number: Long,
    val reactions: Reactions? = null,
    @SerialName("repository_url") val repositoryUrl: String,
    val state: State,
    @SerialName("state_reason") val stateReason: StateReason?,
    @SerialName("timeline_url") val timelineUrl: String? = null,
    val title: String,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val user: User?,
    val labels: List<Label>? = null,
) {
    @Serializable
    data class AnswerChosenBy(
        @SerialName("avatar_url") val avatarUrl: String? = null,
        val deleted: Boolean? = null,
        val email: String? = null,
        @SerialName("events_url") val eventsUrl: String? = null,
        @SerialName("followers_url") val followersUrl: String? = null,
        @SerialName("following_url") val followingUrl: String? = null,
        @SerialName("gists_url") val gistsUrl: String? = null,
        @SerialName("gravatar_id") val gravatarId: String? = null,
        @SerialName("html_url") val htmlUrl: String? = null,
        val id: Long,
        val login: String,
        val name: String? = null,
        @SerialName("node_id") val nodeId: String? = null,
        @SerialName("organizations_url") val organizationsUrl: String? = null,
        @SerialName("received_events_url") val receivedEventsUrl: String? = null,
        @SerialName("repos_url") val reposUrl: String? = null,
        @SerialName("site_admin") val siteAdmin: Boolean? = null,
        @SerialName("starred_url") val starredUrl: String? = null,
        @SerialName("subscriptions_url") val subscriptionsUrl: String? = null,
        val type: Type? = null,
        val url: String? = null,
        @SerialName("user_view_type") val userViewType: String? = null,
    ) {
        @Serializable
        enum class Type {
            Bot, User, Organization;
        }
    }

    @Serializable
    enum class AuthorAssociation {
        COLLABORATOR,
        CONTRIBUTOR,
        @SerialName("FIRST_TIMER")
        FIRSTTIMER,
        @SerialName("FIRST_TIME_CONTRIBUTOR")
        FIRSTTIMECONTRIBUTOR,
        MANNEQUIN,
        MEMBER,
        NONE,
        OWNER;
    }

    @Serializable
    data class Category(
        @SerialName("created_at") val createdAt: LocalDateTime,
        val description: String,
        val emoji: String,
        val id: Long,
        @SerialName("is_answerable") val isAnswerable: Boolean,
        val name: String,
        @SerialName("node_id") val nodeId: String? = null,
        @SerialName("repository_id") val repositoryId: Long,
        val slug: String,
        @SerialName("updated_at") val updatedAt: String,
    )

    @Serializable
    data class Reactions(
        @SerialName("+1") val `+1`: Long,
        @SerialName("-1") val `-1`: Long,
        val confused: Long,
        val eyes: Long,
        val heart: Long,
        val hooray: Long,
        val laugh: Long,
        val rocket: Long,
        @SerialName("total_count") val totalCount: Long,
        val url: String,
    )

    @Serializable
    enum class State {
        @SerialName("open")
        Open,
        @SerialName("closed")
        Closed,
        @SerialName("locked")
        Locked,
        @SerialName("converting")
        Converting,
        @SerialName("transferring")
        Transferring;
    }

    @Serializable
    enum class StateReason {
        @SerialName("resolved")
        Resolved,
        @SerialName("outdated")
        Outdated,
        @SerialName("duplicate")
        Duplicate,
        @SerialName("reopened")
        Reopened;
    }

    @Serializable
    data class User(
        @SerialName("avatar_url") val avatarUrl: String? = null,
        val deleted: Boolean? = null,
        val email: String? = null,
        @SerialName("events_url") val eventsUrl: String? = null,
        @SerialName("followers_url") val followersUrl: String? = null,
        @SerialName("following_url") val followingUrl: String? = null,
        @SerialName("gists_url") val gistsUrl: String? = null,
        @SerialName("gravatar_id") val gravatarId: String? = null,
        @SerialName("html_url") val htmlUrl: String? = null,
        val id: Long,
        val login: String,
        val name: String? = null,
        @SerialName("node_id") val nodeId: String? = null,
        @SerialName("organizations_url") val organizationsUrl: String? = null,
        @SerialName("received_events_url") val receivedEventsUrl: String? = null,
        @SerialName("repos_url") val reposUrl: String? = null,
        @SerialName("site_admin") val siteAdmin: Boolean? = null,
        @SerialName("starred_url") val starredUrl: String? = null,
        @SerialName("subscriptions_url") val subscriptionsUrl: String? = null,
        val type: Type? = null,
        val url: String? = null,
        @SerialName("user_view_type") val userViewType: String? = null,
    ) {
        @Serializable
        enum class Type {
            Bot, User, Organization;
        }
    }
}
