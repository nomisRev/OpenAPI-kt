package io.youtrack.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Agile board columns settings.
 */
@Serializable
public data class ColumnSettings(
  public val id: String? = null,
  public val `field`: CustomFieldRead? = null,
  public val columns: List<AgileColumn>? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
