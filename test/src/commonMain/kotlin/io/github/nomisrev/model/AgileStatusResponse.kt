package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AgileStatusResponse(
    val id: String? = null,
    val valid: Boolean? = null,
    val hasJobs: Boolean? = null,
    val errors: List<String>? = null,
    val warnings: List<String>? = null,
    @SerialName($$"$type") val type: String? = null,
)
