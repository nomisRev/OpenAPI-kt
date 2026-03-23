package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Shows if the board has any configuration problems.
 */
@Serializable
public data class AgileStatus(
  public val id: String? = null,
  public val valid: Boolean? = null,
  public val hasJobs: Boolean? = null,
  public val errors: List<String>? = null,
  public val warnings: List<String>? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
