package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningAlertLocation(
    val path: String? = null,
    @SerialName("start_line") val startLine: Long? = null,
    @SerialName("end_line") val endLine: Long? = null,
    @SerialName("start_column") val startColumn: Long? = null,
    @SerialName("end_column") val endColumn: Long? = null,
)
