package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class PageBuildStatus(val url: String, val status: String)
