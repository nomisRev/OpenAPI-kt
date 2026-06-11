package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Runner Application
 */
@Serializable
public data class RunnerApplication(
  public val os: String,
  public val architecture: String,
  @SerialName("download_url")
  public val downloadUrl: String,
  public val filename: String,
  @SerialName("temp_download_token")
  public val tempDownloadToken: String? = null,
  @SerialName("sha256_checksum")
  public val sha256Checksum: String? = null,
)
