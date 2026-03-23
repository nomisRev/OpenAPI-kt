package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The public key used for setting Dependabot Secrets.
 */
@Serializable
public data class DependabotPublicKey(
  @SerialName("key_id")
  public val keyId: String,
  public val key: String,
)
