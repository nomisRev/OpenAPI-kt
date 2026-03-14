package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodespaceExportDetails(
    val state: String? = null,
    @SerialName("completed_at") val completedAt: LocalDateTime? = null,
    val branch: String? = null,
    val sha: String? = null,
    val id: String? = null,
    @SerialName("export_url") val exportUrl: String? = null,
    @SerialName("html_url") val htmlUrl: String? = null,
)
