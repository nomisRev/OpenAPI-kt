package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ReleaseAsset(
    val url: String,
    @SerialName("browser_download_url") val browserDownloadUrl: String,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val name: String,
    val label: String?,
    val state: State,
    @SerialName("content_type") val contentType: String,
    val size: Long,
    val digest: String?,
    @SerialName("download_count") val downloadCount: Long,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val uploader: NullableSimpleUser?,
) {
    @Serializable
    enum class State {
        @SerialName("uploaded") Uploaded, @SerialName("open") Open;
    }
}
