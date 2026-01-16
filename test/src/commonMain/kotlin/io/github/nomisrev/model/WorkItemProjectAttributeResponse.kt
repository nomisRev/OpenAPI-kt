package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WorkItemProjectAttributeResponse(
    val id: String? = null,
    val timeTrackingSettings: ProjectTimeTrackingSettingsResponse? = null,
    val prototype: WorkItemAttributePrototypeResponse? = null,
    val values: List<WorkItemAttributeValueResponse>? = null,
    val name: String? = null,
    val ordinal: Int? = null,
    @SerialName($$"$type") val type: String? = null,
)
