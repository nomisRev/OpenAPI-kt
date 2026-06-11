package io.youtrack.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the period field value.
 */
@Serializable
public data class PeriodValueRead(
  public val id: String? = null,
  public val minutes: Int? = null,
  public val presentation: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
