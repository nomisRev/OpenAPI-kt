package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Autolink(
    val id: Long,
    @SerialName("key_prefix") val keyPrefix: String,
    @SerialName("url_template") val urlTemplate: String,
    @SerialName("is_alphanumeric") val isAlphanumeric: Boolean,
    @SerialName("updated_at") val updatedAt: LocalDateTime? = null,
)
