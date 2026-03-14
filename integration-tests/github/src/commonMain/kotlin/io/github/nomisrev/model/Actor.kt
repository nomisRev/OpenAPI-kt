package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Actor(
    val id: Long,
    val login: String,
    @SerialName("display_login") val displayLogin: String? = null,
    @SerialName("gravatar_id") val gravatarId: String?,
    val url: String,
    @SerialName("avatar_url") val avatarUrl: String,
)
