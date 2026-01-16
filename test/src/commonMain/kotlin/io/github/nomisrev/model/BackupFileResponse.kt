package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class BackupFileResponse(
    val id: String? = null,
    val name: String? = null,
    val size: Long? = null,
    val creationDate: Long? = null,
    val link: String? = null,
    @SerialName($$"$type") val type: String? = null,
)
