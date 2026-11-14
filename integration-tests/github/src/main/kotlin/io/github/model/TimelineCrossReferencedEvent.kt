package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Timeline Cross Referenced Event
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class TimelineCrossReferencedEvent(
  public val event: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val actor: SimpleUser? = null,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val source: Source,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Source(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val type: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val issue: Issue? = null,
  )
}
