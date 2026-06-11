package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SystemSettingsRead(
  public val id: String? = null,
  public val baseUrl: String? = null,
  public val maxUploadFileSize: Long? = null,
  public val maxExportItems: Int? = null,
  public val administratorEmail: String? = null,
  public val allowStatisticsCollection: Boolean? = null,
  public val isApplicationReadOnly: Boolean? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
