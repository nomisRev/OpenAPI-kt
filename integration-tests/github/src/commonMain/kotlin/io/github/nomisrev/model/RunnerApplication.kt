package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RunnerApplication(
    val os: String,
    val architecture: String,
    @SerialName("download_url") val downloadUrl: String,
    val filename: String,
    @SerialName("temp_download_token") val tempDownloadToken: String? = null,
    @SerialName("sha256_checksum") val sha256Checksum: String? = null,
)
