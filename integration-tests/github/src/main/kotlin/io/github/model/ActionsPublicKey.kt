package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The public key used for setting Actions Secrets.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ActionsPublicKey(
  @SerialName("key_id")
  public val keyId: String,
  public val key: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val url: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val title: String? = null,
  @SerialName("created_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val createdAt: String? = null,
)
