package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WIPLimitResponse(
    val id: String? = null,
    val max: Int? = null,
    val min: Int? = null,
    val column: AgileColumnResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
