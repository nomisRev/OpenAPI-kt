package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ReferencedWorkflow(val path: String, val sha: String, val ref: String? = null)
