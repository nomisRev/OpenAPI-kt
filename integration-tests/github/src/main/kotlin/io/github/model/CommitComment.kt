package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Commit Comment
 */
@Serializable
public data class CommitComment(
  @SerialName("html_url")
  public val htmlUrl: String,
  public val url: String,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val body: String,
  public val path: String?,
  public val position: Long?,
  public val line: Long?,
  @SerialName("commit_id")
  public val commitId: String,
  public val user: NullableSimpleUser?,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("author_association")
  public val authorAssociation: AuthorAssociation,
  public val reactions: ReactionRollup? = null,
)
