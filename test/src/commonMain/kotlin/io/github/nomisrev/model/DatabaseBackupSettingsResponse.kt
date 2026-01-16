package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DatabaseBackupSettingsResponse(
    val id: String? = null,
    val location: String? = null,
    val filesToKeep: Int? = null,
    val cronExpression: String? = null,
    val archiveFormat: ArchiveFormat? = null,
    val isOn: Boolean? = null,
    val availableDiskSpace: Long? = null,
    val notifiedUsers: List<UserResponse>? = null,
    val backupStatus: BackupStatusResponse? = null,
    @SerialName($$"$type") val type: String? = null,
) {
    @Serializable
    enum class ArchiveFormat {
        @SerialName("TAR_GZ") TARGZ, ZIP;
    }
}
