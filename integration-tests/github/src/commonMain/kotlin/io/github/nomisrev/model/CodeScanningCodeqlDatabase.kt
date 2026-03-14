package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningCodeqlDatabase(
    val id: Long,
    val name: String,
    val language: String,
    val uploader: SimpleUser,
    @SerialName("content_type") val contentType: String,
    val size: Long,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val url: String,
    @SerialName("commit_oid") val commitOid: String? = null,
)
