package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class GitignoreTemplate(val name: String, val source: String)
