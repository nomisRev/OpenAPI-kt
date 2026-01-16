package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WorkItemTypeResponse(
    val id: String? = null,
    val name: String? = null,
    val autoAttached: Boolean? = null,
    @SerialName($$"$type") val type: String? = null,
)
