package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ProjectTimeTrackingSettingsRead(
  public val id: String? = null,
  public val enabled: Boolean? = null,
  public val estimate: ProjectCustomFieldRead? = null,
  public val timeSpent: ProjectCustomFieldRead? = null,
  public val workItemTypes: List<WorkItemTypeRead>? = null,
  public val project: IssueFolderRead.Project? = null,
  public val attributes: List<WorkItemProjectAttributeRead>? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
