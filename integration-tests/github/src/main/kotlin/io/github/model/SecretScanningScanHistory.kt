package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SecretScanningScanHistory(
  @SerialName("incremental_scans")
  public val incrementalScans: List<SecretScanningScan>? = null,
  @SerialName("pattern_update_scans")
  public val patternUpdateScans: List<SecretScanningScan>? = null,
  @SerialName("backfill_scans")
  public val backfillScans: List<SecretScanningScan>? = null,
  @SerialName("custom_pattern_backfill_scans")
  public val customPatternBackfillScans: List<CustomPatternBackfillScans>? = null,
) {
  /**
   * Information on a single scan performed by secret scanning on the repository
   */
  @Serializable
  public data class CustomPatternBackfillScans(
    public val type: String? = null,
    public val status: String? = null,
    @SerialName("completed_at")
    public val completedAt: Instant? = null,
    @SerialName("started_at")
    public val startedAt: Instant? = null,
    @SerialName("pattern_name")
    public val patternName: String? = null,
    @SerialName("pattern_scope")
    public val patternScope: String? = null,
  )
}
