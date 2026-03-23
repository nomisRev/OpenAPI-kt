package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Key
 */
@Serializable
public data class Key(
  public val key: String,
  public val id: Long,
  public val url: String,
  public val title: String,
  @SerialName("created_at")
  public val createdAt: Instant,
  public val verified: Boolean,
  @SerialName("read_only")
  public val readOnly: Boolean,
  @SerialName("last_used")
  public val lastUsed: Instant? = null,
)
