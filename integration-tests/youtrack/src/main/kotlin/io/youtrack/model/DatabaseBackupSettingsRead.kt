package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class DatabaseBackupSettingsRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val location: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val filesToKeep: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val cronExpression: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val archiveFormat: ArchiveFormat? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isOn: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val availableDiskSpace: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val notifiedUsers: List<UserRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val backupStatus: BackupStatus? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
