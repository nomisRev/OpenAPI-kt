package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Key Simple
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class KeySimple(
  public val id: Long,
  public val key: String,
  @SerialName("created_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val createdAt: Instant? = null,
  @SerialName("last_used")
  public val lastUsed: Instant? = null,
)
