package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class BackupStatusResponse(
    val id: String? = null,
    val backupInProgress: Boolean? = null,
    val backupCancelled: Boolean? = null,
    val backupError: BackupErrorResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
