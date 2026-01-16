package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DatabaseBackupSettingsRequest(
    val location: String? = null,
    val filesToKeep: Int? = null,
    val cronExpression: String? = null,
    val archiveFormat: ArchiveFormat? = null,
    val isOn: Boolean? = null,
    val notifiedUsers: List<UserRequest>? = null,
    val backupStatus: BackupStatusRequest? = null,
) {
    @Serializable
    enum class ArchiveFormat {
        @SerialName("TAR_GZ") TARGZ, ZIP;
    }
}
