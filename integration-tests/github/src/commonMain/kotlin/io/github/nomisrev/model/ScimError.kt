package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ScimError(
    val message: String? = null,
    @SerialName("documentation_url") val documentationUrl: String? = null,
    val detail: String? = null,
    val status: Long? = null,
    val scimType: String? = null,
    val schemas: List<String>? = null,
)
