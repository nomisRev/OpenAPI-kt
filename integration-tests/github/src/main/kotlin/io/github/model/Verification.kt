package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Verification(
  public val verified: Boolean,
  public val reason: String,
  public val payload: String?,
  public val signature: String?,
  @SerialName("verified_at")
  public val verifiedAt: String?,
)
