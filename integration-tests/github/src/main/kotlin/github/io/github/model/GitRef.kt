package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Git references within a repository
 */
@Serializable
public data class GitRef(
  public val ref: String,
  @SerialName("node_id")
  public val nodeId: String,
  public val url: String,
  public val `object`: Object,
) {
  @Serializable
  public data class Object(
    public val type: String,
    public val sha: String,
    public val url: String,
  )
}
