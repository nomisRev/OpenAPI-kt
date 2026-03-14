package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SecretScanningLocationWikiCommit(
    val path: String,
    @SerialName("start_line") val startLine: Double,
    @SerialName("end_line") val endLine: Double,
    @SerialName("start_column") val startColumn: Double,
    @SerialName("end_column") val endColumn: Double,
    @SerialName("blob_sha") val blobSha: String,
    @SerialName("page_url") val pageUrl: String,
    @SerialName("commit_sha") val commitSha: String,
    @SerialName("commit_url") val commitUrl: String,
)
