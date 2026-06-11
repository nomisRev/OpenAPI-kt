package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the format, in which the period values are displayed to the user.
 */
@Serializable
public data class PeriodFieldFormatRead(
  public val id: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
