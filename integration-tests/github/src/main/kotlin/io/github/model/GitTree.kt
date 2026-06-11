package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * The hierarchy between files in a Git repository.
 */
@Serializable
public data class GitTree(
  public val sha: String,
  public val url: String? = null,
  public val truncated: Boolean,
  public val tree: List<Tree>,
) {
  @Serializable
  public data class Tree(
    public val path: String,
    public val mode: String,
    public val type: String,
    public val sha: String,
    public val size: Long? = null,
    public val url: String? = null,
  )
}
