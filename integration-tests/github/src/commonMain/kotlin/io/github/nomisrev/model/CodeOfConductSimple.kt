package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeOfConductSimple(
    val url: String,
    val key: String,
    val name: String,
    @SerialName("html_url") val htmlUrl: String?,
)
