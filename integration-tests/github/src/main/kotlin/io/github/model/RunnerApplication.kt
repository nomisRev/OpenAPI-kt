package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Runner Application
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RunnerApplication(
  public val os: String,
  public val architecture: String,
  @SerialName("download_url")
  public val downloadUrl: String,
  public val filename: String,
  @SerialName("temp_download_token")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tempDownloadToken: String? = null,
  @SerialName("sha256_checksum")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sha256Checksum: String? = null,
)
