package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkItemAttributeValueRequest(
    val name: String? = null,
    val description: String? = null,
    val autoAttach: Boolean? = null,
    val prototype: WorkItemAttributePrototypeRequest? = null,
)
