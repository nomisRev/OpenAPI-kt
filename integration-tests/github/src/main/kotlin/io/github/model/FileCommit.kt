package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * File Commit
 */
@Serializable
public data class FileCommit(
  public val content: Content?,
  public val commit: Commit,
) {
  @Serializable
  public data class Commit(
    public val sha: String? = null,
    @SerialName("node_id")
    public val nodeId: String? = null,
    public val url: String? = null,
    @SerialName("html_url")
    public val htmlUrl: String? = null,
    public val author: Author? = null,
    public val committer: Committer? = null,
    public val message: String? = null,
    public val tree: Tree? = null,
    public val parents: List<Parents>? = null,
    public val verification: Verification? = null,
  ) {
    @Serializable
    public data class Author(
      public val date: String? = null,
      public val name: String? = null,
      public val email: String? = null,
    )

    @Serializable
    public data class Committer(
      public val date: String? = null,
      public val name: String? = null,
      public val email: String? = null,
    )

    @Serializable
    public data class Parents(
      public val url: String? = null,
      @SerialName("html_url")
      public val htmlUrl: String? = null,
      public val sha: String? = null,
    )

    @Serializable
    public data class Tree(
      public val url: String? = null,
      public val sha: String? = null,
    )

    @Serializable
    public data class Verification(
      public val verified: Boolean? = null,
      public val reason: String? = null,
      public val signature: String? = null,
      public val payload: String? = null,
      @SerialName("verified_at")
      public val verifiedAt: String? = null,
    )
  }

  @Serializable
  public data class Content(
    public val name: String? = null,
    public val path: String? = null,
    public val sha: String? = null,
    public val size: Long? = null,
    public val url: String? = null,
    @SerialName("html_url")
    public val htmlUrl: String? = null,
    @SerialName("git_url")
    public val gitUrl: String? = null,
    @SerialName("download_url")
    public val downloadUrl: String? = null,
    public val type: String? = null,
    @SerialName("_links")
    public val links: Links? = null,
  ) {
    @Serializable
    public data class Links(
      public val self: String? = null,
      public val git: String? = null,
      public val html: String? = null,
    )
  }
}
