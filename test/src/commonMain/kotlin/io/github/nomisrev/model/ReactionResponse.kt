package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ReactionResponse(
    val id: String? = null,
    val author: UserResponse? = null,
    val reaction: String? = null,
    @SerialName($$"$type") val type: String? = null,
)
