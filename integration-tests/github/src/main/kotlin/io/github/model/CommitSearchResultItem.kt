package io.github.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Commit Search Result Item
 */
@Serializable
public data class CommitSearchResultItem(
  public val url: String,
  public val sha: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("comments_url")
  public val commentsUrl: String,
  public val commit: Commit,
  public val author: NullableSimpleUser?,
  public val committer: NullableGitUser?,
  public val parents: List<Parents>,
  public val repository: MinimalRepository,
  public val score: Double,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("text_matches")
  public val textMatches: SearchResultTextMatches? = null,
) {
  @Serializable
  public data class Commit(
    public val author: Author,
    public val committer: NullableGitUser?,
    @SerialName("comment_count")
    public val commentCount: Long,
    public val message: String,
    public val tree: Tree,
    public val url: String,
    public val verification: Verification? = null,
  ) {
    @Serializable
    public data class Author(
      public val name: String,
      public val email: String,
      public val date: Instant,
    )

    @Serializable
    public data class Tree(
      public val sha: String,
      public val url: String,
    )
  }

  @Serializable
  public data class Parents(
    public val url: String? = null,
    @SerialName("html_url")
    public val htmlUrl: String? = null,
    public val sha: String? = null,
  )
}
