package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class PullRequestReviewCommentEvent(
  public val action: String,
  @SerialName("pull_request")
  public val pullRequest: PullRequestMinimal,
  public val comment: Comment,
) {
  @Serializable
  public data class Comment(
    public val id: Long,
    @SerialName("node_id")
    public val nodeId: String,
    public val url: String,
    @SerialName("pull_request_review_id")
    public val pullRequestReviewId: Long?,
    @SerialName("diff_hunk")
    public val diffHunk: String,
    public val path: String,
    public val position: Long?,
    @SerialName("original_position")
    public val originalPosition: Long,
    @SerialName("subject_type")
    public val subjectType: String? = null,
    @SerialName("commit_id")
    public val commitId: String,
    public val user: User?,
    public val body: String,
    @SerialName("created_at")
    public val createdAt: Instant,
    @SerialName("updated_at")
    public val updatedAt: Instant,
    @SerialName("html_url")
    public val htmlUrl: String,
    @SerialName("pull_request_url")
    public val pullRequestUrl: String,
    @SerialName("_links")
    public val links: Links,
    @SerialName("original_commit_id")
    public val originalCommitId: String,
    public val reactions: Reactions,
    @SerialName("in_reply_to_id")
    public val inReplyToId: Long? = null,
  ) {
    @Serializable
    public data class Links(
      public val html: Html,
      @SerialName("pull_request")
      public val pullRequest: PullRequest,
      public val self: Self,
    ) {
      @JvmInline
      @Serializable
      public value class Html(
        public val href: String,
      )

      @JvmInline
      @Serializable
      public value class PullRequest(
        public val href: String,
      )

      @JvmInline
      @Serializable
      public value class Self(
        public val href: String,
      )
    }

    @Serializable
    public data class Reactions(
      public val `+1`: Long? = null,
      public val `-1`: Long? = null,
      public val confused: Long? = null,
      public val eyes: Long? = null,
      public val heart: Long? = null,
      public val hooray: Long? = null,
      public val laugh: Long? = null,
      public val rocket: Long? = null,
      @SerialName("total_count")
      public val totalCount: Long? = null,
      public val url: String? = null,
    )

    @Serializable
    public data class User(
      @SerialName("avatar_url")
      public val avatarUrl: String? = null,
      public val deleted: Boolean? = null,
      public val email: String? = null,
      @SerialName("events_url")
      public val eventsUrl: String? = null,
      @SerialName("followers_url")
      public val followersUrl: String? = null,
      @SerialName("following_url")
      public val followingUrl: String? = null,
      @SerialName("gists_url")
      public val gistsUrl: String? = null,
      @SerialName("gravatar_id")
      public val gravatarId: String? = null,
      @SerialName("html_url")
      public val htmlUrl: String? = null,
      public val id: Long? = null,
      public val login: String? = null,
      public val name: String? = null,
      @SerialName("node_id")
      public val nodeId: String? = null,
      @SerialName("organizations_url")
      public val organizationsUrl: String? = null,
      @SerialName("received_events_url")
      public val receivedEventsUrl: String? = null,
      @SerialName("repos_url")
      public val reposUrl: String? = null,
      @SerialName("site_admin")
      public val siteAdmin: Boolean? = null,
      @SerialName("starred_url")
      public val starredUrl: String? = null,
      @SerialName("subscriptions_url")
      public val subscriptionsUrl: String? = null,
      public val type: Type? = null,
      public val url: String? = null,
      @SerialName("user_view_type")
      public val userViewType: String? = null,
    ) {
      @Serializable
      public enum class Type {
        Bot,
        User,
        Organization,
      }
    }
  }
}
