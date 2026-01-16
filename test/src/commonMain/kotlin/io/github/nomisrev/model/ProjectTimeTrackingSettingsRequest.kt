package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ProjectTimeTrackingSettingsRequest(
    val enabled: Boolean? = null,
    val estimate: ProjectCustomFieldRequest? = null,
    val timeSpent: ProjectCustomFieldRequest? = null,
    val workItemTypes: List<WorkItemTypeRequest>? = null,
    val project: Project? = null,
    val attributes: List<WorkItemProjectAttributeRequest>? = null,
)
