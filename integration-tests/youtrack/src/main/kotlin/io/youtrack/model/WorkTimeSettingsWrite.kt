package io.youtrack.model

import kotlin.Int
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class WorkTimeSettingsWrite(
  public val minutesADay: Int? = null,
  public val workDays: List<Int>? = null,
)
