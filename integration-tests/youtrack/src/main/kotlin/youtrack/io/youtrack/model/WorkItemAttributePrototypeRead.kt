package io.youtrack.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a work item attribute prototype that is common for all projects. The project-related settings for work item attributes are stored in the `WorkItemProjectAttribute` entities.
 */
@Serializable
public data class WorkItemAttributePrototypeRead(
  public val id: String? = null,
  public val name: String? = null,
  public val instances: List<WorkItemProjectAttributeRead>? = null,
  public val values: List<WorkItemAttributeValueRead>? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
