package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ShortBlob(val url: String, val sha: String)
