package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Blob
 */
@Serializable
public data class Blob(
  public val content: String,
  public val encoding: String,
  public val url: String,
  public val sha: String,
  public val size: Long?,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("highlighted_content")
  public val highlightedContent: String? = null,
)
