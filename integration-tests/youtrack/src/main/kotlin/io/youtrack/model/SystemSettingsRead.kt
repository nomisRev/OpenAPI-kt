package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SystemSettingsRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val baseUrl: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxUploadFileSize: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxExportItems: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val administratorEmail: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val allowStatisticsCollection: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isApplicationReadOnly: Boolean? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
