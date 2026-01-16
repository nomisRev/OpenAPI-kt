package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GlobalTimeTrackingSettingsResponse(
    val id: String? = null,
    val workItemTypes: List<WorkItemTypeResponse>? = null,
    val workTimeSettings: WorkTimeSettingsResponse? = null,
    val attributePrototypes: List<WorkItemAttributePrototypeResponse>? = null,
    @SerialName($$"$type") val type: String? = null,
)
