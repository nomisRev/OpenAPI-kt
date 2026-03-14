package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class IssueEventLabel(val name: String?, val color: String?)
