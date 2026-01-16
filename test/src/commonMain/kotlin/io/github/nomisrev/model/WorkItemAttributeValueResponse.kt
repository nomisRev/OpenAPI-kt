package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WorkItemAttributeValueResponse(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val autoAttach: Boolean? = null,
    val prototype: WorkItemAttributePrototypeResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
