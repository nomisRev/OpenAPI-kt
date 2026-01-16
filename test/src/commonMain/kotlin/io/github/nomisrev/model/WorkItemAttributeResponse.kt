package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WorkItemAttributeResponse(
    val id: String? = null,
    val workItem: BaseWorkItemResponse? = null,
    val projectAttribute: WorkItemProjectAttributeResponse? = null,
    val value: WorkItemAttributeValueResponse? = null,
    val name: String? = null,
    @SerialName($$"$type") val type: String? = null,
)
