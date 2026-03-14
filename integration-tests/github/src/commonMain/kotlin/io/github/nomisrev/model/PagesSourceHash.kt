package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class PagesSourceHash(val branch: String, val path: String)
