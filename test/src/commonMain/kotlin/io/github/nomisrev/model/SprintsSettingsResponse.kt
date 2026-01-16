package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SprintsSettingsResponse(
    val id: String? = null,
    val isExplicit: Boolean? = null,
    val cardOnSeveralSprints: Boolean? = null,
    val defaultSprint: SprintResponse? = null,
    val disableSprints: Boolean? = null,
    val explicitQuery: String? = null,
    val sprintSyncField: CustomFieldResponse? = null,
    val hideSubtasksOfCards: Boolean? = null,
    @SerialName($$"$type") val type: String? = null,
)
