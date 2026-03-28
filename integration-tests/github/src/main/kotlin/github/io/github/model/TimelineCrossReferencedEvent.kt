package io.github.model

import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Timeline Cross Referenced Event
 */
@Serializable
public data class TimelineCrossReferencedEvent(
  public val event: String,
  public val actor: SimpleUser? = null,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val source: Source,
) {
  @Serializable
  public data class Source(
    public val type: String? = null,
    public val issue: Issue? = null,
  )
}
