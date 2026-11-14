package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.datetime.LocalDate
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The configuration for iteration fields.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ProjectsV2FieldIterationConfiguration(
  @SerialName("start_date")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val startDate: LocalDate? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val duration: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val iterations: List<Iterations>? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Iterations(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val title: String? = null,
    @SerialName("start_date")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val startDate: LocalDate? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val duration: Long? = null,
  )
}
