package io.youtrack.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an error that appeared during the backup.
 */
@Serializable
public data class BackupError(
  public val id: String? = null,
  public val date: Long? = null,
  public val errorMessage: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
