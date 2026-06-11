package io.youtrack.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the duration value and its visual presentation.
 */
@Serializable
public data class DurationValueRead(
  public val id: String? = null,
  public val minutes: Int? = null,
  public val presentation: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
