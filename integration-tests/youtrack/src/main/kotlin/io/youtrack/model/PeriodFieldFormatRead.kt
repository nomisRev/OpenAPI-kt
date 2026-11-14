package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the format, in which the period values are displayed to the user.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class PeriodFieldFormatRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
