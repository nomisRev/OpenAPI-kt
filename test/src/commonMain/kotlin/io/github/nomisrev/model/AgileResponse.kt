package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AgileResponse(
    val id: String? = null,
    val name: String? = null,
    val owner: UserResponse? = null,
    val visibleFor: UserGroupResponse? = null,
    val visibleForProjectBased: Boolean? = null,
    val updateableBy: UserGroupResponse? = null,
    val updateableByProjectBased: Boolean? = null,
    val readSharingSettings: AgileSharingSettingsResponse? = null,
    val updateSharingSettings: AgileSharingSettingsResponse? = null,
    val orphansAtTheTop: Boolean? = null,
    val hideOrphansSwimlane: Boolean? = null,
    val estimationField: CustomFieldResponse? = null,
    val originalEstimationField: CustomFieldResponse? = null,
    val projects: List<Project>? = null,
    val sprints: List<SprintResponse>? = null,
    val currentSprint: SprintResponse? = null,
    val columnSettings: ColumnSettingsResponse? = null,
    val swimlaneSettings: SwimlaneSettingsResponse? = null,
    val sprintsSettings: SprintsSettingsResponse? = null,
    val colorCoding: ColorCodingResponse? = null,
    val status: AgileStatusResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
