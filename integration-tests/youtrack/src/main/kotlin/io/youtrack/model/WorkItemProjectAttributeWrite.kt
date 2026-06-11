package io.youtrack.model

import kotlin.Int
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents the project-related settings of a work item attribute.
 */
@Serializable
public data class WorkItemProjectAttributeWrite(
  public val timeTrackingSettings: ProjectTimeTrackingSettingsWrite? = null,
  public val prototype: WorkItemAttributePrototypeWrite? = null,
  public val values: List<WorkItemAttributeValueWrite>? = null,
  public val ordinal: Int? = null,
)
