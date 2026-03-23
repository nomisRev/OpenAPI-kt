package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents single swimlane in case of IssueBasedSwimlaneSettings.
 */
@Serializable
public data class SwimlaneValueRead(
  public val id: String? = null,
  public val name: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
