package io.youtrack.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class WorkTimeSettingsRead(
  public val id: String? = null,
  public val minutesADay: Int? = null,
  public val workDays: List<Int>? = null,
  public val firstDayOfWeek: Int? = null,
  public val daysAWeek: Int? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
