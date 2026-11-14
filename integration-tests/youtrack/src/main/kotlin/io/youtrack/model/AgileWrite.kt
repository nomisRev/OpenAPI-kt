package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AgileWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val owner: UserWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val visibleFor: UserGroupWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val visibleForProjectBased: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val updateableBy: UserGroupWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val updateableByProjectBased: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val orphansAtTheTop: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hideOrphansSwimlane: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val estimationField: CustomFieldWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val originalEstimationField: CustomFieldWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val projects: List<IssueFolderWrite.Project>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sprints: List<SprintWrite>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val swimlaneSettings: SwimlaneSettingsWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val colorCoding: ColorCodingWrite? = null,
)
