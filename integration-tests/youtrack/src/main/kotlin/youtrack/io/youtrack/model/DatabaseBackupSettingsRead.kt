package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class DatabaseBackupSettingsRead(
  public val id: String? = null,
  public val location: String? = null,
  public val filesToKeep: Int? = null,
  public val cronExpression: String? = null,
  public val archiveFormat: ArchiveFormat? = null,
  public val isOn: Boolean? = null,
  public val availableDiskSpace: Long? = null,
  public val notifiedUsers: List<UserRead>? = null,
  public val backupStatus: BackupStatus? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
) {
  @Serializable
  public enum class ArchiveFormat(
    public val `value`: String,
  ) {
    @SerialName("TAR_GZ")
    TARGZ("TAR_GZ"),
    ZIP("ZIP"),
    ;
  }
}
