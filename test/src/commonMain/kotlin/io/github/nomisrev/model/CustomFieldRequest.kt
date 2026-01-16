package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class CustomFieldRequest(
    val name: String? = null,
    val localizedName: String? = null,
    val fieldType: FieldTypeRequest? = null,
    val isAutoAttached: Boolean? = null,
    val isDisplayedInIssueList: Boolean? = null,
    val ordinal: Int? = null,
    val aliases: String? = null,
    val fieldDefaults: CustomFieldDefaultsRequest? = null,
    val instances: List<ProjectCustomFieldRequest>? = null,
)
