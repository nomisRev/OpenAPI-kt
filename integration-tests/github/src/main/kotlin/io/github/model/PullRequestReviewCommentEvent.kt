package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class PullRequestReviewCommentEvent(
  public val action: String,
  @SerialName("pull_request")
  public val pullRequest: PullRequestMinimal,
  public val comment: Comment,
) {
  @OptIn(ExperimentalSerializationApi::class)
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
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val subjectType: Optional<String> = Optional.Absent,
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
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val inReplyToId: Long? = null,
  ) {
    @Serializable
    public data class Links(
      public val html: Html,
      @SerialName("pull_request")
      public val pullRequest: PullRequest,
      public val self: Self,
    ) {
      @Serializable
      public data class Html(
        public val href: String,
      )

      @Serializable
      public data class PullRequest(
        public val href: String,
      )

      @Serializable
      public data class Self(
        public val href: String,
      )
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Reactions(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val `+1`: Long? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val `-1`: Long? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val confused: Long? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val eyes: Long? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val heart: Long? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val hooray: Long? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val laugh: Long? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val rocket: Long? = null,
      @SerialName("total_count")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val totalCount: Long? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val url: String? = null,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class User(
      @SerialName("avatar_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val avatarUrl: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val deleted: Boolean? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val email: Optional<String> = Optional.Absent,
      @SerialName("events_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val eventsUrl: String? = null,
      @SerialName("followers_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val followersUrl: String? = null,
      @SerialName("following_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val followingUrl: String? = null,
      @SerialName("gists_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val gistsUrl: String? = null,
      @SerialName("gravatar_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val gravatarId: String? = null,
      @SerialName("html_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val htmlUrl: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val id: Long? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val login: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
      @SerialName("node_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val nodeId: String? = null,
      @SerialName("organizations_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val organizationsUrl: String? = null,
      @SerialName("received_events_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val receivedEventsUrl: String? = null,
      @SerialName("repos_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val reposUrl: String? = null,
      @SerialName("site_admin")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val siteAdmin: Boolean? = null,
      @SerialName("starred_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val starredUrl: String? = null,
      @SerialName("subscriptions_url")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val subscriptionsUrl: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val type: Type? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val url: String? = null,
      @SerialName("user_view_type")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
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
