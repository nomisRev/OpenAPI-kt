package io.github.model

import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Commit Search Result Item
 */
@OptIn(ExperimentalSerializationApi::class)
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val textMatches: SearchResultTextMatches? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Commit(
    public val author: Author,
    public val committer: NullableGitUser?,
    @SerialName("comment_count")
    public val commentCount: Long,
    public val message: String,
    public val tree: Tree,
    public val url: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Parents(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val url: String? = null,
    @SerialName("html_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val htmlUrl: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val sha: String? = null,
  )
}
