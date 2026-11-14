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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val state: Optional<String> = Optional.Absent,
  @SerialName("completed_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val completedAt: Optional<Instant> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val branch: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sha: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @SerialName("export_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val exportUrl: String? = null,
  @SerialName("html_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val htmlUrl: Optional<String> = Optional.Absent,
)
