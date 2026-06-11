package io.github.model

import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A commit.
 */
@Serializable
public data class SimpleCommit(
  public val id: String,
  @SerialName("tree_id")
  public val treeId: String,
  public val message: String,
  public val timestamp: Instant,
  public val author: Author?,
  public val committer: Committer?,
) {
  /**
   * Information about the Git author
   */
  @Serializable
  public data class Author(
    public val name: String,
    public val email: String,
  )

  /**
   * Information about the Git committer
   */
  @Serializable
  public data class Committer(
    public val name: String,
    public val email: String,
  )
}
