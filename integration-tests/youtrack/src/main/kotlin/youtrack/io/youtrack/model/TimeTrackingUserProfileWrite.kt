package io.youtrack.model

import kotlinx.serialization.Serializable

@Serializable
public data class TimeTrackingUserProfileWrite(
  public val periodFormat: PeriodFieldFormatWrite? = null,
)
