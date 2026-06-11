package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a transition from one value to another for a custom field that is managed by a state-machine rule in workflow.
 */
@Serializable
public data class Event(
  public val id: String? = null,
  public val presentation: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
