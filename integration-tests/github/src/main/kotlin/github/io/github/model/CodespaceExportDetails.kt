package io.github.model

import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An export of a codespace. Also, latest export details for a codespace can be fetched with id = latest
 */
@Serializable
public data class CodespaceExportDetails(
  public val state: String? = null,
  @SerialName("completed_at")
  public val completedAt: Instant? = null,
  public val branch: String? = null,
  public val sha: String? = null,
  public val id: String? = null,
  @SerialName("export_url")
  public val exportUrl: String? = null,
  @SerialName("html_url")
  public val htmlUrl: String? = null,
)
