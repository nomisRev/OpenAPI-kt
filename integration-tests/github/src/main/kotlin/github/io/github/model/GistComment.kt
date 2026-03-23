package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A comment made to a gist.
 */
@Serializable
public data class GistComment(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val url: String,
  public val body: String,
  public val user: NullableSimpleUser?,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("author_association")
  public val authorAssociation: AuthorAssociation,
)
