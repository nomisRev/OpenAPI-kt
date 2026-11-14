package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Blob
 */
@OptIn(ExperimentalSerializationApi::class)
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val highlightedContent: String? = null,
)
