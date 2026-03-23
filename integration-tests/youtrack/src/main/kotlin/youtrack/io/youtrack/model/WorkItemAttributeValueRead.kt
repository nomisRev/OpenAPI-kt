package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a value of a work item attribute.
 */
@Serializable
public data class WorkItemAttributeValueRead(
  public val id: String? = null,
  public val name: String? = null,
  public val description: String? = null,
  public val autoAttach: Boolean? = null,
  public val prototype: WorkItemAttributePrototypeRead? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
