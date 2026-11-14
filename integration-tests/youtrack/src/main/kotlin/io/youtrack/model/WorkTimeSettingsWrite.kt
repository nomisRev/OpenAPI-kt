package io.youtrack.model

import kotlin.Int
import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class WorkTimeSettingsWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val minutesADay: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val workDays: List<Int>? = null,
)
