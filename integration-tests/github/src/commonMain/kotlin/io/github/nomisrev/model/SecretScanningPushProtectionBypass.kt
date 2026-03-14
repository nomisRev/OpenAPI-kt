package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SecretScanningPushProtectionBypass(
    val reason: SecretScanningPushProtectionBypassReason? = null,
    @SerialName("expire_at") val expireAt: LocalDateTime? = null,
    @SerialName("token_type") val tokenType: String? = null,
)
