package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.datetime.LocalDateTime

@Serializable
data class SecretScanningScanHistory(
    @SerialName("incremental_scans") val incrementalScans: List<SecretScanningScan>? = null,
    @SerialName("pattern_update_scans") val patternUpdateScans: List<SecretScanningScan>? = null,
    @SerialName("backfill_scans") val backfillScans: List<SecretScanningScan>? = null,
    @SerialName("custom_pattern_backfill_scans") val customPatternBackfillScans: List<CustomPatternBackfillScans>? = null,
) {
    @Serializable
    data class CustomPatternBackfillScans(
        val type: String? = null,
        val status: String? = null,
        @SerialName("completed_at") val completedAt: LocalDateTime? = null,
        @SerialName("started_at") val startedAt: LocalDateTime? = null,
        @SerialName("pattern_name") val patternName: String? = null,
        @SerialName("pattern_scope") val patternScope: String? = null,
    )
}
