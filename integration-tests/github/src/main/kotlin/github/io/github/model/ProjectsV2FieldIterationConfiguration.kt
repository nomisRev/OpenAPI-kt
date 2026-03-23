package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The configuration for iteration fields.
 */
@Serializable
public data class ProjectsV2FieldIterationConfiguration(
  @SerialName("start_date")
  public val startDate: LocalDate? = null,
  public val duration: Long? = null,
  public val iterations: List<Iterations>? = null,
) {
  @Serializable
  public data class Iterations(
    public val title: String? = null,
    @SerialName("start_date")
    public val startDate: LocalDate? = null,
    public val duration: Long? = null,
  )
}
