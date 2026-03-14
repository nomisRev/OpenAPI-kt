package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class BasicError(
    val message: String? = null,
    @SerialName("documentation_url") val documentationUrl: String? = null,
    val url: String? = null,
    val status: String? = null,
)
