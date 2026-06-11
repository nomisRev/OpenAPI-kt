package io.youtrack.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Represents a work item attribute prototype that is common for all projects. The project-related settings for work item attributes are stored in the `WorkItemProjectAttribute` entities.
 */
@Serializable
public data class WorkItemAttributePrototypeWrite(
  public val name: String? = null,
  public val instances: List<WorkItemProjectAttributeWrite>? = null,
  public val values: List<WorkItemAttributeValueWrite>? = null,
)
