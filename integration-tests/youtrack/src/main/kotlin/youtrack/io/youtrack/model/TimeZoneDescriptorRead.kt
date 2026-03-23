package io.youtrack.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a time zone.
 */
@Serializable
public data class TimeZoneDescriptorRead(
  public val id: String? = null,
  public val presentation: String? = null,
  public val offset: Int? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
