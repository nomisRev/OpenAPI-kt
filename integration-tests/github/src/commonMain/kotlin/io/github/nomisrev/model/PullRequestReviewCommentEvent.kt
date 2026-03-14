package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.datetime.LocalDateTime
import kotlin.jvm.JvmInline

@Serializable
data class PullRequestReviewCommentEvent(
    val action: String,
    @SerialName("pull_request") val pullRequest: PullRequestMinimal,
    val comment: Comment,
) {
    @Serializable
    data class Comment(
        val id: Long,
        @SerialName("node_id") val nodeId: String,
        val url: String,
        @SerialName("pull_request_review_id") val pullRequestReviewId: Long?,
        @SerialName("diff_hunk") val diffHunk: String,
        val path: String,
        val position: Long?,
        @SerialName("original_position") val originalPosition: Long,
        @SerialName("subject_type") val subjectType: String? = null,
        @SerialName("commit_id") val commitId: String,
        val user: User?,
        val body: String,
        @SerialName("created_at") val createdAt: LocalDateTime,
        @SerialName("updated_at") val updatedAt: LocalDateTime,
        @SerialName("html_url") val htmlUrl: String,
        @SerialName("pull_request_url") val pullRequestUrl: String,
        @SerialName("_links") val links: Links,
        @SerialName("original_commit_id") val originalCommitId: String,
        val reactions: Reactions,
        @SerialName("in_reply_to_id") val inReplyToId: Long? = null,
    ) {
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
            val id: Long? = null,
            val login: String? = null,
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
        data class Links(val html: Html, @SerialName("pull_request") val pullRequest: PullRequest, val self: Self) {
            @Serializable
            @JvmInline
            value class Html(val href: String)

            @Serializable
            @JvmInline
            value class PullRequest(val href: String)

            @Serializable
            @JvmInline
            value class Self(val href: String)
        }

        @Serializable
        data class Reactions(
            @SerialName("+1") val `+1`: Long? = null,
            @SerialName("-1") val `-1`: Long? = null,
            val confused: Long? = null,
            val eyes: Long? = null,
            val heart: Long? = null,
            val hooray: Long? = null,
            val laugh: Long? = null,
            val rocket: Long? = null,
            @SerialName("total_count") val totalCount: Long? = null,
            val url: String? = null,
        )
    }
}
