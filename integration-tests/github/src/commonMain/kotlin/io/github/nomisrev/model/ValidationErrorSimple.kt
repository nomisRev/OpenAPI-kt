package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ValidationErrorSimple(
    val message: String,
    @SerialName("documentation_url") val documentationUrl: String,
    val errors: List<String>? = null,
)
