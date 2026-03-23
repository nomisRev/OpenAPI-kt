package io.github.model

import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SecretScanningPushProtectionBypass(
  public val reason: SecretScanningPushProtectionBypassReason? = null,
  @SerialName("expire_at")
  public val expireAt: Instant? = null,
  @SerialName("token_type")
  public val tokenType: String? = null,
)
