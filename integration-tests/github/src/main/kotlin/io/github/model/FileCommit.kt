package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Commit(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val sha: String? = null,
    @SerialName("node_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val nodeId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val url: String? = null,
    @SerialName("html_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val htmlUrl: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val author: Author? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val committer: Committer? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val message: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val tree: Tree? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val parents: List<Parents>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val verification: Verification? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Author(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val date: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val email: String? = null,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Committer(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val date: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val email: String? = null,
    )

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

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Tree(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val url: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val sha: String? = null,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Verification(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val verified: Boolean? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val reason: String? = null,
      public val signature: String? = null,
      public val payload: String? = null,
      @SerialName("verified_at")
      public val verifiedAt: String? = null,
    )
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Content(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val path: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val sha: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val size: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val url: String? = null,
    @SerialName("html_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val htmlUrl: String? = null,
    @SerialName("git_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val gitUrl: String? = null,
    @SerialName("download_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val downloadUrl: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val type: String? = null,
    @SerialName("_links")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val links: Links? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Links(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val self: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val git: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val html: String? = null,
    )
  }
}
