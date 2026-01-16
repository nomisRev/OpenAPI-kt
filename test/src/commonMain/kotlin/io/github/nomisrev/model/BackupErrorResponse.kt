package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class BackupErrorResponse(
    val id: String? = null,
    val date: Long? = null,
    val errorMessage: String? = null,
    @SerialName($$"$type") val type: String? = null,
)
