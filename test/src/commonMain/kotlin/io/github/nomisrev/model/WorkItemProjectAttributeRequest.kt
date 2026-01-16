package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkItemProjectAttributeRequest(
    val timeTrackingSettings: ProjectTimeTrackingSettingsRequest? = null,
    val prototype: WorkItemAttributePrototypeRequest? = null,
    val values: List<WorkItemAttributeValueRequest>? = null,
    val ordinal: Int? = null,
)
