package io.youtrack.model

import kotlin.Int
import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Represents the project-related settings of a work item attribute.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class WorkItemProjectAttributeWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val timeTrackingSettings: ProjectTimeTrackingSettingsWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val prototype: WorkItemAttributePrototypeWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val values: List<WorkItemAttributeValueWrite>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ordinal: Int? = null,
)
