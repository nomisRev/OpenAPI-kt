package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An SSH key granting access to a single repository.
 */
@Serializable
public data class DeployKey(
  public val id: Long,
  public val key: String,
  public val url: String,
  public val title: String,
  public val verified: Boolean,
  @SerialName("created_at")
  public val createdAt: String,
  @SerialName("read_only")
  public val readOnly: Boolean,
  @SerialName("added_by")
  public val addedBy: String? = null,
  @SerialName("last_used")
  public val lastUsed: Instant? = null,
  public val enabled: Boolean? = null,
)
