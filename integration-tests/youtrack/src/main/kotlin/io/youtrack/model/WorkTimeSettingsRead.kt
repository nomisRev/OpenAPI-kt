package io.youtrack.model

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class WorkTimeSettingsRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val minutesADay: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val workDays: List<Int>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val firstDayOfWeek: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val daysAWeek: Int? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
