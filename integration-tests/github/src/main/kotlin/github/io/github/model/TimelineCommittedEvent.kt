package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Timeline Committed Event
 */
@Serializable
public data class TimelineCommittedEvent(
  public val event: String? = null,
  public val sha: String,
  @SerialName("node_id")
  public val nodeId: String,
  public val url: String,
  public val author: Author,
  public val committer: Committer,
  public val message: String,
  public val tree: Tree,
  public val parents: List<Parents>,
  public val verification: Verification,
  @SerialName("html_url")
  public val htmlUrl: String,
) {
  /**
   * Identifying information for the git-user
   */
  @Serializable
  public data class Author(
    public val date: Instant,
    public val email: String,
    public val name: String,
  )

  /**
   * Identifying information for the git-user
   */
  @Serializable
  public data class Committer(
    public val date: Instant,
    public val email: String,
    public val name: String,
  )

  @Serializable
  public data class Parents(
    public val sha: String,
    public val url: String,
    @SerialName("html_url")
    public val htmlUrl: String,
  )

  @Serializable
  public data class Tree(
    public val sha: String,
    public val url: String,
  )

  @Serializable
  public data class Verification(
    public val verified: Boolean,
    public val reason: String,
    public val signature: String?,
    public val payload: String?,
    @SerialName("verified_at")
    public val verifiedAt: String?,
  )
}
