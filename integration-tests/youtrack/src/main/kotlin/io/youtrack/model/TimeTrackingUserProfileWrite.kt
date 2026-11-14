package io.youtrack.model

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class TimeTrackingUserProfileWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val periodFormat: PeriodFieldFormatWrite? = null,
)
