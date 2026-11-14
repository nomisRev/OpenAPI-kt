package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ProjectTimeTrackingSettingsRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val enabled: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val estimate: ProjectCustomFieldRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val timeSpent: ProjectCustomFieldRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val workItemTypes: List<WorkItemTypeRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val project: IssueFolderRead.Project? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val attributes: List<WorkItemProjectAttributeRead>? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
