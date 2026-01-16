package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ExternalArticleResponse(
    val id: String? = null,
    val name: String? = null,
    val url: String? = null,
    val key: String? = null,
    @SerialName($$"$type") val type: String? = null,
)
