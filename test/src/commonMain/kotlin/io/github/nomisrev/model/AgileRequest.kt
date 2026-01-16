package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class AgileRequest(
    val name: String? = null,
    val owner: UserRequest? = null,
    val visibleFor: UserGroupRequest? = null,
    val visibleForProjectBased: Boolean? = null,
    val updateableBy: UserGroupRequest? = null,
    val updateableByProjectBased: Boolean? = null,
    val readSharingSettings: AgileSharingSettingsRequest? = null,
    val updateSharingSettings: AgileSharingSettingsRequest? = null,
    val orphansAtTheTop: Boolean? = null,
    val hideOrphansSwimlane: Boolean? = null,
    val estimationField: CustomFieldRequest? = null,
    val originalEstimationField: CustomFieldRequest? = null,
    val projects: List<Project>? = null,
    val sprints: List<SprintRequest>? = null,
    val currentSprint: SprintRequest? = null,
    val columnSettings: ColumnSettingsRequest? = null,
    val swimlaneSettings: SwimlaneSettingsRequest? = null,
    val sprintsSettings: SprintsSettingsRequest? = null,
    val colorCoding: ColorCodingRequest? = null,
    val status: AgileStatusRequest? = null,
)
