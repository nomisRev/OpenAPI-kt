package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes sprints configuration.
 */
@Serializable
public data class SprintsSettings(
  public val id: String? = null,
  public val isExplicit: Boolean? = null,
  public val cardOnSeveralSprints: Boolean? = null,
  public val defaultSprint: SprintRead? = null,
  public val disableSprints: Boolean? = null,
  public val explicitQuery: String? = null,
  public val sprintSyncField: CustomFieldRead? = null,
  public val hideSubtasksOfCards: Boolean? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
