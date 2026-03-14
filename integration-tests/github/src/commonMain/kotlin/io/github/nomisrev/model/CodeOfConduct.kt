package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeOfConduct(
    val key: String,
    val name: String,
    val url: String,
    val body: String? = null,
    @SerialName("html_url") val htmlUrl: String?,
)
