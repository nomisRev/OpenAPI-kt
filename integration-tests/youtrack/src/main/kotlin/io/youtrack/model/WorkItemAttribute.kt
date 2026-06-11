package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the attribute of a specific work item.
 */
@Serializable
public data class WorkItemAttribute(
  public val id: String? = null,
  public val workItem: BaseWorkItemRead? = null,
  public val projectAttribute: WorkItemProjectAttributeRead? = null,
  public val `value`: WorkItemAttributeValueRead? = null,
  public val name: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
