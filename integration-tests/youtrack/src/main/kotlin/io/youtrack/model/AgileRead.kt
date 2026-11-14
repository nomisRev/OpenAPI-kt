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
public data class AgileRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val owner: UserRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val visibleFor: UserGroupRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val visibleForProjectBased: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val updateableBy: UserGroupRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val updateableByProjectBased: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val readSharingSettings: AgileSharingSettings? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val updateSharingSettings: AgileSharingSettings? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val orphansAtTheTop: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hideOrphansSwimlane: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val estimationField: CustomFieldRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val originalEstimationField: CustomFieldRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val projects: List<IssueFolderRead.Project>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sprints: List<SprintRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val currentSprint: SprintRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val columnSettings: ColumnSettings? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val swimlaneSettings: SwimlaneSettingsRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sprintsSettings: SprintsSettings? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val colorCoding: ColorCodingRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val status: AgileStatus? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
