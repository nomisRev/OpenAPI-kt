package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Legacy Review Comment
 */
@OptIn(ExperimentalSerializationApi::class)
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val bodyText: String? = null,
  @SerialName("body_html")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val bodyHtml: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reactions: ReactionRollup? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val side: Side? = null,
  @SerialName("start_side")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val startSide: Optional<StartSide> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val line: Long? = null,
  @SerialName("original_line")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val originalLine: Long? = null,
  @SerialName("start_line")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val startLine: Optional<Long> = Optional.Absent,
  @SerialName("original_start_line")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val originalStartLine: Optional<Long> = Optional.Absent,
  @SerialName("subject_type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
