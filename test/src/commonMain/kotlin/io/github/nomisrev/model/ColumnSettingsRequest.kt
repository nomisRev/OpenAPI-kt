package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ColumnSettingsRequest(val field: CustomFieldRequest? = null, val columns: List<AgileColumnRequest>? = null)
