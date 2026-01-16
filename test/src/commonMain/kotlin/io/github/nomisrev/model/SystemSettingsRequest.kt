package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class SystemSettingsRequest(
    val baseUrl: String? = null,
    val maxUploadFileSize: Long? = null,
    val maxExportItems: Int? = null,
    val administratorEmail: String? = null,
    val allowStatisticsCollection: Boolean? = null,
    val isApplicationReadOnly: Boolean? = null,
)
