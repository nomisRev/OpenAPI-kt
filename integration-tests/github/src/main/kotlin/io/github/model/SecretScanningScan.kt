package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Information on a single scan performed by secret scanning on the repository
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SecretScanningScan(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val status: String? = null,
  @SerialName("completed_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val completedAt: Optional<Instant> = Optional.Absent,
  @SerialName("started_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val startedAt: Optional<Instant> = Optional.Absent,
)
