package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CustomFieldResponse(
    val id: String? = null,
    val name: String? = null,
    val localizedName: String? = null,
    val fieldType: FieldTypeResponse? = null,
    val isAutoAttached: Boolean? = null,
    val isDisplayedInIssueList: Boolean? = null,
    val ordinal: Int? = null,
    val aliases: String? = null,
    val fieldDefaults: CustomFieldDefaultsResponse? = null,
    val hasRunningJob: Boolean? = null,
    val isUpdateable: Boolean? = null,
    val instances: List<ProjectCustomFieldResponse>? = null,
    @SerialName($$"$type") val type: String? = null,
)
