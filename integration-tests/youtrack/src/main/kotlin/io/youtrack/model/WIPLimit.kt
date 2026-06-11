package io.youtrack.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents WIP limits for particular column. If they are not satisfied, the column will be highlighted in UI.
 */
@Serializable
public data class WIPLimit(
  public val id: String? = null,
  public val max: Int? = null,
  public val min: Int? = null,
  public val column: AgileColumn? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
