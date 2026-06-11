package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Short Blob
 */
@Serializable
public data class ShortBlob(
  public val url: String,
  public val sha: String,
)
