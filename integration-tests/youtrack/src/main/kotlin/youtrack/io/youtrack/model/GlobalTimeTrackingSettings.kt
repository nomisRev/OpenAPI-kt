package io.youtrack.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GlobalTimeTrackingSettings(
  public val id: String? = null,
  public val workItemTypes: List<WorkItemTypeRead>? = null,
  public val workTimeSettings: WorkTimeSettingsRead? = null,
  public val attributePrototypes: List<WorkItemAttributePrototypeRead>? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
