package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PorterAuthor(
    val id: Long,
    @SerialName("remote_id") val remoteId: String,
    @SerialName("remote_name") val remoteName: String,
    val email: String,
    val name: String,
    val url: String,
    @SerialName("import_url") val importUrl: String,
)
