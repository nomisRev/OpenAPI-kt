package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Verification(
    val verified: Boolean,
    val reason: String,
    val payload: String?,
    val signature: String?,
    @SerialName("verified_at") val verifiedAt: String?,
)
