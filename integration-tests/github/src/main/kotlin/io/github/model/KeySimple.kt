package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Key Simple
 */
@Serializable
public data class KeySimple(
  public val id: Long,
  public val key: String,
  @SerialName("created_at")
  public val createdAt: Instant? = null,
  @SerialName("last_used")
  public val lastUsed: Instant? = null,
)
