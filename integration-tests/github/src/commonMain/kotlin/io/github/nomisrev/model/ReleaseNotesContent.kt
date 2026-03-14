package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ReleaseNotesContent(val name: String, val body: String)
