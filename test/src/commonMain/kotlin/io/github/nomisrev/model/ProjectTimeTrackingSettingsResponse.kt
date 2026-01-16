package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ProjectTimeTrackingSettingsResponse(
    val id: String? = null,
    val enabled: Boolean? = null,
    val estimate: ProjectCustomFieldResponse? = null,
    val timeSpent: ProjectCustomFieldResponse? = null,
    val workItemTypes: List<WorkItemTypeResponse>? = null,
    val project: Project? = null,
    val attributes: List<WorkItemProjectAttributeResponse>? = null,
    @SerialName($$"$type") val type: String? = null,
)
