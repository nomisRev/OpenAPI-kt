package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ReviewComment(
    val url: String,
    @SerialName("pull_request_review_id") val pullRequestReviewId: Long?,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    @SerialName("diff_hunk") val diffHunk: String,
    val path: String,
    val position: Long?,
    @SerialName("original_position") val originalPosition: Long,
    @SerialName("commit_id") val commitId: String,
    @SerialName("original_commit_id") val originalCommitId: String,
    @SerialName("in_reply_to_id") val inReplyToId: Long? = null,
    val user: NullableSimpleUser?,
    val body: String,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("pull_request_url") val pullRequestUrl: String,
    @SerialName("author_association") val authorAssociation: AuthorAssociation,
    @SerialName("_links") val links: Links,
    @SerialName("body_text") val bodyText: String? = null,
    @SerialName("body_html") val bodyHtml: String? = null,
    val reactions: ReactionRollup? = null,
    val side: Side? = null,
    @SerialName("start_side") val startSide: StartSide? = null,
    val line: Long? = null,
    @SerialName("original_line") val originalLine: Long? = null,
    @SerialName("start_line") val startLine: Long? = null,
    @SerialName("original_start_line") val originalStartLine: Long? = null,
    @SerialName("subject_type") val subjectType: SubjectType? = null,
) {
    @Serializable
    data class Links(val self: Link, val html: Link, @SerialName("pull_request") val pullRequest: Link)

    @Serializable
    enum class Side {
        LEFT, RIGHT;
    }

    @Serializable
    enum class StartSide {
        LEFT, RIGHT;
    }

    @Serializable
    enum class SubjectType {
        @SerialName("line") Line, @SerialName("file") File;
    }
}
