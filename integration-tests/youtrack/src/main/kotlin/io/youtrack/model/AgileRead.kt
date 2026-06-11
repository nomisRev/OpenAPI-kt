package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AgileRead(
  public val id: String? = null,
  public val name: String? = null,
  public val owner: UserRead? = null,
  public val visibleFor: UserGroupRead? = null,
  public val visibleForProjectBased: Boolean? = null,
  public val updateableBy: UserGroupRead? = null,
  public val updateableByProjectBased: Boolean? = null,
  public val readSharingSettings: AgileSharingSettings? = null,
  public val updateSharingSettings: AgileSharingSettings? = null,
  public val orphansAtTheTop: Boolean? = null,
  public val hideOrphansSwimlane: Boolean? = null,
  public val estimationField: CustomFieldRead? = null,
  public val originalEstimationField: CustomFieldRead? = null,
  public val projects: List<IssueFolderRead.Project>? = null,
  public val sprints: List<SprintRead>? = null,
  public val currentSprint: SprintRead? = null,
  public val columnSettings: ColumnSettings? = null,
  public val swimlaneSettings: SwimlaneSettingsRead? = null,
  public val sprintsSettings: SprintsSettings? = null,
  public val colorCoding: ColorCodingRead? = null,
  public val status: AgileStatus? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
