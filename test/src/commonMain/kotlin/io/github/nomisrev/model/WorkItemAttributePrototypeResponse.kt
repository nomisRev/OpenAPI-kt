package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WorkItemAttributePrototypeResponse(
    val id: String? = null,
    val name: String? = null,
    val instances: List<WorkItemProjectAttributeResponse>? = null,
    val values: List<WorkItemAttributeValueResponse>? = null,
    @SerialName($$"$type") val type: String? = null,
)
