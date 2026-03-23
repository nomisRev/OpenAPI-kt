package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The public key used for setting user Codespaces' Secrets.
 */
@Serializable
public data class CodespacesUserPublicKey(
  @SerialName("key_id")
  public val keyId: String,
  public val key: String,
)
