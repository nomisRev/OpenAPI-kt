package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CampaignSummary(
    val number: Long,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val name: String? = null,
    val description: String,
    val managers: List<SimpleUser>,
    @SerialName("team_managers") val teamManagers: List<Team>? = null,
    @SerialName("published_at") val publishedAt: LocalDateTime? = null,
    @SerialName("ends_at") val endsAt: LocalDateTime,
    @SerialName("closed_at") val closedAt: LocalDateTime? = null,
    val state: CampaignState,
    @SerialName("contact_link") val contactLink: String?,
    @SerialName("alert_stats") val alertStats: AlertStats? = null,
) {
    @Serializable
    data class AlertStats(
        @SerialName("open_count") val openCount: Long,
        @SerialName("closed_count") val closedCount: Long,
        @SerialName("in_progress_count") val inProgressCount: Long,
    )
}
