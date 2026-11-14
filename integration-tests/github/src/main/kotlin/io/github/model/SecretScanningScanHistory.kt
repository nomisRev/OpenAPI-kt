package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SecretScanningScanHistory(
  @SerialName("incremental_scans")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val incrementalScans: List<SecretScanningScan>? = null,
  @SerialName("pattern_update_scans")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val patternUpdateScans: List<SecretScanningScan>? = null,
  @SerialName("backfill_scans")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val backfillScans: List<SecretScanningScan>? = null,
  @SerialName("custom_pattern_backfill_scans")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val customPatternBackfillScans: List<CustomPatternBackfillScans>? = null,
) {
  /**
   * Information on a single scan performed by secret scanning on the repository
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CustomPatternBackfillScans(
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
    @SerialName("pattern_name")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val patternName: String? = null,
    @SerialName("pattern_scope")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val patternScope: String? = null,
  )
}
