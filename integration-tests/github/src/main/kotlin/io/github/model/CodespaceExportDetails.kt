package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An export of a codespace. Also, latest export details for a codespace can be fetched with id = latest
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodespaceExportDetails(
  public val state: String? = null,
  @SerialName("completed_at")
  public val completedAt: Instant? = null,
  public val branch: String? = null,
  public val sha: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @SerialName("export_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val exportUrl: String? = null,
  @SerialName("html_url")
  public val htmlUrl: String? = null,
)
