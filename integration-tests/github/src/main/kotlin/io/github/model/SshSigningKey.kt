package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A public SSH key used to sign Git commits
 */
@Serializable
public data class SshSigningKey(
  public val key: String,
  public val id: Long,
  public val title: String,
  @SerialName("created_at")
  public val createdAt: Instant,
)
