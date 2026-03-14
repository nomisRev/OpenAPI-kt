package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class IssueEventRename(val from: String, val to: String)
