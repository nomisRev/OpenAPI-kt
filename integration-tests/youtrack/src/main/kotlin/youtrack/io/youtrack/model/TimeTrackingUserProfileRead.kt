package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TimeTrackingUserProfileRead(
  public val id: String? = null,
  public val periodFormat: PeriodFieldFormatRead? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
