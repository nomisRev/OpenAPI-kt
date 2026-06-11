package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class AgileWrite(
  public val name: String? = null,
  public val owner: UserWrite? = null,
  public val visibleFor: UserGroupWrite? = null,
  public val visibleForProjectBased: Boolean? = null,
  public val updateableBy: UserGroupWrite? = null,
  public val updateableByProjectBased: Boolean? = null,
  public val orphansAtTheTop: Boolean? = null,
  public val hideOrphansSwimlane: Boolean? = null,
  public val estimationField: CustomFieldWrite? = null,
  public val originalEstimationField: CustomFieldWrite? = null,
  public val projects: List<IssueFolderWrite.Project>? = null,
  public val sprints: List<SprintWrite>? = null,
  public val swimlaneSettings: SwimlaneSettingsWrite? = null,
  public val colorCoding: ColorCodingWrite? = null,
)
