package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OnlineUsersResponse(
    val id: String? = null,
    val users: Int? = null,
    @SerialName($$"$type") val type: String? = null,
)
