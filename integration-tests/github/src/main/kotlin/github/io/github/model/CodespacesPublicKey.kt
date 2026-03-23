package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The public key used for setting Codespaces secrets.
 */
@Serializable
public data class CodespacesPublicKey(
  @SerialName("key_id")
  public val keyId: String,
  public val key: String,
  public val id: Long? = null,
  public val url: String? = null,
  public val title: String? = null,
  @SerialName("created_at")
  public val createdAt: String? = null,
)
