package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An autolink reference.
 */
@Serializable
public data class Autolink(
  public val id: Long,
  @SerialName("key_prefix")
  public val keyPrefix: String,
  @SerialName("url_template")
  public val urlTemplate: String,
  @SerialName("is_alphanumeric")
  public val isAlphanumeric: Boolean,
  @SerialName("updated_at")
  public val updatedAt: Instant? = null,
)
