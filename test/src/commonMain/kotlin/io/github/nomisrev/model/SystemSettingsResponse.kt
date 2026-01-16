package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SystemSettingsResponse(
    val id: String? = null,
    val baseUrl: String? = null,
    val maxUploadFileSize: Long? = null,
    val maxExportItems: Int? = null,
    val administratorEmail: String? = null,
    val allowStatisticsCollection: Boolean? = null,
    val isApplicationReadOnly: Boolean? = null,
    @SerialName($$"$type") val type: String? = null,
)
