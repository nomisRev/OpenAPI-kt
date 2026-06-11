package io.youtrack.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the project-related settings of a work item attribute.
 */
@Serializable
public data class WorkItemProjectAttributeRead(
  public val id: String? = null,
  public val timeTrackingSettings: ProjectTimeTrackingSettingsRead? = null,
  public val prototype: WorkItemAttributePrototypeRead? = null,
  public val values: List<WorkItemAttributeValueRead>? = null,
  public val name: String? = null,
  public val ordinal: Int? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
