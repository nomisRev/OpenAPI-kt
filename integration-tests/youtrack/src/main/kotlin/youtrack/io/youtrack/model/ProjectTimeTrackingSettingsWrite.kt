package io.youtrack.model

import kotlin.Boolean
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class ProjectTimeTrackingSettingsWrite(
  public val enabled: Boolean? = null,
  public val estimate: ProjectCustomFieldWrite? = null,
  public val timeSpent: ProjectCustomFieldWrite? = null,
  public val workItemTypes: List<WorkItemTypeWrite>? = null,
  public val attributes: List<WorkItemProjectAttributeWrite>? = null,
)
