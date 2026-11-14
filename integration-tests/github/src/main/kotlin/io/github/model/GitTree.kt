package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * The hierarchy between files in a Git repository.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class GitTree(
  public val sha: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val url: String? = null,
  public val truncated: Boolean,
  public val tree: List<Tree>,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Tree(
    public val path: String,
    public val mode: String,
    public val type: String,
    public val sha: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val size: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val url: String? = null,
  )
}
