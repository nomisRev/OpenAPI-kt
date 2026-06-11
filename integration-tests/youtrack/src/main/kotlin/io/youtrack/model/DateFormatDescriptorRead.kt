package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents date format.
 */
@Serializable
public data class DateFormatDescriptorRead(
  public val id: String? = null,
  public val presentation: String? = null,
  public val pattern: String? = null,
  public val datePattern: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
