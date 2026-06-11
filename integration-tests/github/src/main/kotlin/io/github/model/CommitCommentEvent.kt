package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CommitCommentEvent(
  public val action: String,
  public val comment: Comment,
) {
  @Serializable
  public data class Comment(
    @SerialName("html_url")
    public val htmlUrl: String? = null,
    public val url: String? = null,
    public val id: Long? = null,
    @SerialName("node_id")
    public val nodeId: String? = null,
    public val body: String? = null,
    public val path: String? = null,
    public val position: Long? = null,
    public val line: Long? = null,
    @SerialName("commit_id")
    public val commitId: String? = null,
    public val user: NullableSimpleUser? = null,
    @SerialName("created_at")
    public val createdAt: Instant? = null,
    @SerialName("updated_at")
    public val updatedAt: Instant? = null,
    public val reactions: ReactionRollup? = null,
  )
}
