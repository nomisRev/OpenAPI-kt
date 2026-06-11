package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Legacy Review Comment
 */
@Serializable
public data class ReviewComment(
  public val url: String,
  @SerialName("pull_request_review_id")
  public val pullRequestReviewId: Long?,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("diff_hunk")
  public val diffHunk: String,
  public val path: String,
  public val position: Long?,
  @SerialName("original_position")
  public val originalPosition: Long,
  @SerialName("commit_id")
  public val commitId: String,
  @SerialName("original_commit_id")
  public val originalCommitId: String,
  @SerialName("in_reply_to_id")
  public val inReplyToId: Long? = null,
  public val user: NullableSimpleUser?,
  public val body: String,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("pull_request_url")
  public val pullRequestUrl: String,
  @SerialName("author_association")
  public val authorAssociation: AuthorAssociation,
  @SerialName("_links")
  public val links: Links,
  @SerialName("body_text")
  public val bodyText: String? = null,
  @SerialName("body_html")
  public val bodyHtml: String? = null,
  public val reactions: ReactionRollup? = null,
  public val side: Side? = null,
  @SerialName("start_side")
  public val startSide: StartSide? = null,
  public val line: Long? = null,
  @SerialName("original_line")
  public val originalLine: Long? = null,
  @SerialName("start_line")
  public val startLine: Long? = null,
  @SerialName("original_start_line")
  public val originalStartLine: Long? = null,
  @SerialName("subject_type")
  public val subjectType: SubjectType? = null,
) {
  @Serializable
  public data class Links(
    public val self: Link,
    public val html: Link,
    @SerialName("pull_request")
    public val pullRequest: Link,
  )

  @Serializable
  public enum class Side {
    LEFT,
    RIGHT,
  }

  @Serializable
  public enum class StartSide {
    LEFT,
    RIGHT,
  }

  @Serializable
  public enum class SubjectType(
    public val `value`: String,
  ) {
    @SerialName("line")
    Line("line"),
    @SerialName("file")
    File("file"),
    ;
  }
}
