package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the command that was parsed from the provided query.
 */
@Serializable
public data class ParsedCommand(
  public val id: String? = null,
  public val description: String? = null,
  public val error: Boolean? = null,
  public val delete: Boolean? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
