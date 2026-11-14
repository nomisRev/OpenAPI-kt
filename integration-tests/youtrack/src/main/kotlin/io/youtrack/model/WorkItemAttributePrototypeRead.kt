package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a work item attribute prototype that is common for all projects. The project-related settings for work item attributes are stored in the `WorkItemProjectAttribute` entities.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class WorkItemAttributePrototypeRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val instances: List<WorkItemProjectAttributeRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val values: List<WorkItemAttributeValueRead>? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
