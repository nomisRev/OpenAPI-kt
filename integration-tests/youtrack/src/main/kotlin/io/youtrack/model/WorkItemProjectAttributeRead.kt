package io.youtrack.model

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the project-related settings of a work item attribute.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class WorkItemProjectAttributeRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val timeTrackingSettings: ProjectTimeTrackingSettingsRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val prototype: WorkItemAttributePrototypeRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val values: List<WorkItemAttributeValueRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ordinal: Int? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
