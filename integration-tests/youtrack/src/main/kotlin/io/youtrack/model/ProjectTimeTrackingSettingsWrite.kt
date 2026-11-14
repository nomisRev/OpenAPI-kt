package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ProjectTimeTrackingSettingsWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val enabled: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val estimate: ProjectCustomFieldWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val timeSpent: ProjectCustomFieldWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val workItemTypes: List<WorkItemTypeWrite>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val attributes: List<WorkItemProjectAttributeWrite>? = null,
)
