package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkItemAttributePrototypeRequest(
    val name: String? = null,
    val instances: List<WorkItemProjectAttributeRequest>? = null,
    val values: List<WorkItemAttributeValueRequest>? = null,
)
