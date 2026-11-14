package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SecretScanningPushProtectionBypass(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reason: SecretScanningPushProtectionBypassReason? = null,
  @SerialName("expire_at")
  public val expireAt: Instant? = null,
  @SerialName("token_type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tokenType: String? = null,
)
