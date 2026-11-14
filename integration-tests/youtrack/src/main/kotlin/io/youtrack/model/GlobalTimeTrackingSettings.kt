package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class GlobalTimeTrackingSettings(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val workItemTypes: List<WorkItemTypeRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val workTimeSettings: WorkTimeSettingsRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val attributePrototypes: List<WorkItemAttributePrototypeRead>? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
