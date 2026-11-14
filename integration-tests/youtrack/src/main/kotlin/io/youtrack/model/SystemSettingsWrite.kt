package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SystemSettingsWrite(
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
)
