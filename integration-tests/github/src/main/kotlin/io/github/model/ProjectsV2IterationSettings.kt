package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An iteration setting for an iteration field
 */
@Serializable
public data class ProjectsV2IterationSettings(
  public val id: String,
  @SerialName("start_date")
  public val startDate: LocalDate,
  public val duration: Long,
  public val title: Title,
  public val completed: Boolean,
) {
  /**
   * The iteration title, in raw text and HTML formats.
   */
  @Serializable
  public data class Title(
    public val raw: String,
    public val html: String,
  )
}
