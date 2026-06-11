package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Tag
 */
@Serializable
public data class Tag(
  public val name: String,
  public val commit: Commit,
  @SerialName("zipball_url")
  public val zipballUrl: String,
  @SerialName("tarball_url")
  public val tarballUrl: String,
  @SerialName("node_id")
  public val nodeId: String,
) {
  @Serializable
  public data class Commit(
    public val sha: String,
    public val url: String,
  )
}
