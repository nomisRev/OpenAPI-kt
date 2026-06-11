package io.youtrack.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class BackupFile(
  public val id: String? = null,
  public val name: String? = null,
  public val size: Long? = null,
  public val creationDate: Long? = null,
  public val link: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
