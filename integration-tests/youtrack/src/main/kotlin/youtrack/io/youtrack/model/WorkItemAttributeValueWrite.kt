package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a value of a work item attribute.
 */
@Serializable
public data class WorkItemAttributeValueWrite(
  public val name: String? = null,
  public val description: String? = null,
  public val autoAttach: Boolean? = null,
  public val prototype: WorkItemAttributePrototypeWrite? = null,
)
