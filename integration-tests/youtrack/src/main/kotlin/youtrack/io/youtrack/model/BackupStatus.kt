package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class BackupStatus(
  public val id: String? = null,
  public val backupInProgress: Boolean? = null,
  public val backupCancelled: Boolean? = null,
  public val backupError: BackupError? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
