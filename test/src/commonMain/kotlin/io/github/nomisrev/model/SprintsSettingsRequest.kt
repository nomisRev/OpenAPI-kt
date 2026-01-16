package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class SprintsSettingsRequest(
    val isExplicit: Boolean? = null,
    val cardOnSeveralSprints: Boolean? = null,
    val defaultSprint: SprintRequest? = null,
    val disableSprints: Boolean? = null,
    val explicitQuery: String? = null,
    val sprintSyncField: CustomFieldRequest? = null,
    val hideSubtasksOfCards: Boolean? = null,
)
