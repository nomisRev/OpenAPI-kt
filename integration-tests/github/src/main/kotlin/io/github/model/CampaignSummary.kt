package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The campaign metadata and alert stats.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CampaignSummary(
  public val number: Long,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  public val description: String,
  public val managers: List<SimpleUser>,
  @SerialName("team_managers")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val teamManagers: List<Team>? = null,
  @SerialName("published_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val publishedAt: Instant? = null,
  @SerialName("ends_at")
  public val endsAt: Instant,
  @SerialName("closed_at")
  public val closedAt: Instant? = null,
  public val state: CampaignState,
  @SerialName("contact_link")
  public val contactLink: String?,
  @SerialName("alert_stats")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val alertStats: AlertStats? = null,
) {
  @Serializable
  public data class AlertStats(
    @SerialName("open_count")
    public val openCount: Long,
    @SerialName("closed_count")
    public val closedCount: Long,
    @SerialName("in_progress_count")
    public val inProgressCount: Long,
  )
}
