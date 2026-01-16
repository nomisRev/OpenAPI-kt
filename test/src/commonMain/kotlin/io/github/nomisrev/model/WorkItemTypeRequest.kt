package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkItemTypeRequest(val name: String? = null, val autoAttached: Boolean? = null)
